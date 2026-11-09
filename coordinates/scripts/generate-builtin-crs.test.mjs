import assert from 'node:assert/strict';
import { createHash } from 'node:crypto';
import { mkdtemp, readFile, unlink, rmdir, writeFile } from 'node:fs/promises';
import { tmpdir } from 'node:os';
import { join } from 'node:path';
import test from 'node:test';
import { DEFINITIONS, fetchWkt, generate, kotlinLiteral, render } from './generate-builtin-crs.mjs';

test('fetches the WKT2 endpoint with a timeout and identifying user agent', async () => {
    const wkt = 'GEOGCRS["WGS 84",ID["EPSG",4326]]\n';
    let called = false;
    const result = await fetchWkt(4326, 'GEOGCRS', async (url, options) => {
        called = true;
        assert.equal(url, 'https://epsg.io/4326.wkt2');
        assert.match(options.headers['User-Agent'], /GeoKit/);
        assert.ok(options.signal instanceof AbortSignal);
        return new Response(wkt);
    });
    assert.ok(called);
    assert.deepEqual(result, { wkt, sourceUrl: 'https://epsg.io/4326.wkt2' });
});

test('extracts the published WKT2 block when the download endpoint returns 404', async () => {
    const wkt = 'COORDINATEOPERATION["A & B",\n    ID["EPSG",1149]]\n';
    const urls = [];
    const result = await fetchWkt(1149, 'COORDINATEOPERATION', async (url) => {
        urls.push(url);
        if (url.endsWith('.wkt2')) return new Response('Not found', { status: 404 });
        return new Response('<html><pre>+proj=noop</pre><pre><span></span>' +
            '<span class="nx">COORDINATEOPERATION</span>[&#34;A &amp; B&#34;,\n' +
            '    ID[&quot;EPSG&quot;,1149]]\n</pre></html>');
    });
    assert.deepEqual(urls, ['https://epsg.io/1149.wkt2', 'https://epsg.io/1149']);
    assert.deepEqual(result, { wkt, sourceUrl: 'https://epsg.io/1149' });
});

test('fails rather than synthesizing WKT when the fallback has no matching definition', async () => {
    await assert.rejects(fetchWkt(1149, 'COORDINATEOPERATION', async (url) =>
        url.endsWith('.wkt2') ? new Response('Not found', { status: 404 }) : new Response('<pre>+proj=noop</pre>'),
    ), /WKT2/);
});

test('rejects HTTP errors, HTML, empty responses, wrong definitions and invalid UTF-8', async () => {
    const responses = [
        new Response('unavailable', { status: 503 }),
        new Response('<html>error</html>'),
        new Response(''),
        new Response('GEOGCRS["Other",ID["EPSG",4258]]'),
        new Response(Uint8Array.of(0xff)),
    ];
    for (const response of responses) {
        await assert.rejects(fetchWkt(4326, 'GEOGCRS', async () => response));
    }
});

test('escapes Kotlin literals without changing quotes, backslashes, dollars or whitespace', () => {
    assert.equal(kotlinLiteral('a"b\\c$d\n\r\t\f°'), '"a\\"b\\\\c\\$d\\n\\r\\t\\u000c°"');
});

test('renders each complete response verbatim with source URL and digest', () => {
    const wkt = 'GEOGCRS["WGS 84",\n  ID["EPSG",4326]]\n';
    const definitions = new Map([[4326, { wkt, sourceUrl: 'https://epsg.io/4326.wkt2' }]]);
    const result = render(definitions);
    assert.match(result, /DO NOT EDIT/);
    assert.ok(result.includes('https://epsg.io/4326.wkt2'));
    assert.ok(result.includes(createHash('sha256').update(wkt).digest('hex')));
    assert.ok(result.includes(`const val EPSG4326 = ${kotlinLiteral(wkt)}`));
    assert.equal(result, render(definitions));
    assert.ok(!result.includes('WGS84_ENSEMBLE'));
});

test('fetches every definition and check mode never changes output', async (t) => {
    const directory = await mkdtemp(join(tmpdir(), 'geokit-epsg-test-'));
    const output = join(directory, 'BuiltInCoordinateReferenceSystems.kt');
    t.after(async () => { await unlink(output); await rmdir(directory); });
    const definitions = new Map(DEFINITIONS.map(([code, kind]) => [code, {
        wkt: `${kind}["Definition ${code}",ID["EPSG",${code}]]`, sourceUrl: `https://epsg.io/${code}.wkt2`,
    }]));
    const calls = [];
    const fetchDefinition = async (code, kind) => {
        calls.push([code, kind]);
        return definitions.get(code);
    };
    assert.equal(await generate({ output, fetchDefinition }), true);
    assert.deepEqual(calls, DEFINITIONS);
    assert.equal(await readFile(output, 'utf8'), render(definitions));
    assert.equal(await generate({ output, fetchDefinition, check: true }), true);
    await writeFile(output, 'stale content', 'utf8');
    assert.equal(await generate({ output, fetchDefinition, check: true }), false);
    assert.equal(await readFile(output, 'utf8'), 'stale content');
});

test('a failed download leaves the existing class untouched', async (t) => {
    const directory = await mkdtemp(join(tmpdir(), 'geokit-epsg-test-'));
    const output = join(directory, 'BuiltInCoordinateReferenceSystems.kt');
    t.after(async () => { await unlink(output); await rmdir(directory); });
    await writeFile(output, 'existing content', 'utf8');
    await assert.rejects(generate({
        output,
        fetchDefinition: async (code) => {
            if (code !== 4326) throw new Error('unavailable');
            return { wkt: 'GEOGCRS["WGS 84",ID["EPSG",4326]]', sourceUrl: 'https://epsg.io/4326.wkt2' };
        },
    }), /unavailable/);
    assert.equal(await readFile(output, 'utf8'), 'existing content');
});
