## Built-in EPSG definitions

`generate-builtin-crs.mjs` fetches the complete WKT2 definitions for EPSG:4326,
EPSG:25832, EPSG:25833, and operation EPSG:1149 directly from
`https://epsg.io/<code>.wkt2`. It preserves the responses, applies only Kotlin
string escaping, and records source URLs and SHA-256 hashes in the generated
class. Do not edit that class manually or invent aliases for its EPSG metadata.

EPSG.io currently returns 404 for `/1149.wkt2` despite linking to it. On a 404,
the generator queries the EPSG record page and extracts its published WKT2 code
block (removing syntax-highlighting markup and decoding HTML entities only).
The generated source comment records the actual URL used. If no unique matching
WKT2 block exists, generation fails rather than reconstructing a definition.

From the GeoKit repository root, using Node from the pinned Nix devShell:

```sh
node coordinates/scripts/generate-builtin-crs.mjs
node coordinates/scripts/generate-builtin-crs.mjs --check
node --test coordinates/scripts/generate-builtin-crs.test.mjs
```

Generation and `--check` require network access. The generator tests do not.
Downloads are validated before the class is atomically replaced; a failed
download leaves the previous class untouched. Normal Kotlin builds use the
checked-in generated class and do not query EPSG.io.

After updating, run the coordinates tests. If EPSG changes the dataset, review
the generated diff and update the dataset-specific regression expectations as
needed; never alter fetched WKT to satisfy a test.
