package cloud.mallne.geokit.gml

object ParserTest {
    const val WHERE =
        "https://www.geoproxy.geoportal-th.de/geoproxy/services/adv_alkis_wfs?SERVICE=WFS&VERSION=2.0.0&REQUEST=GetFeature&typeNames=ave%3AFlurstueck&SRSName=urn%3Aogc%3Adef%3Acrs%3AEPSG%3A%3A4326&bbox=645021.4301836416%2C5647295.731612682%2C645406.5624732533%2C5646594.367474664&count=100"
    const val GPRXY = """
        <?xml version='1.0' encoding='UTF-8'?>
<wfs:FeatureCollection xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:wfs="http://www.opengis.net/wfs/2.0"
                       xmlns:gml="http://www.opengis.net/gml/3.2"
                       xsi:schemaLocation="http://www.opengis.net/wfs/2.0 http://schemas.opengis.net/wfs/2.0/wfs.xsd http://www.opengis.net/gml/3.2 http://schemas.opengis.net/gml/3.2.1/gml.xsd http://repository.gdi-de.org/schemas/adv/produkt/alkis-vereinfacht/1.0 https://www.geoproxy.geoportal-th.de/geoproxy/services/adv_alkis_wfs?SERVICE=WFS&amp;VERSION=2.0.0&amp;REQUEST=DescribeFeatureType&amp;OUTPUTFORMAT=application%2Fgml%2Bxml%3B+version%3D3.2&amp;TYPENAME=ave:Flurstueck&amp;NAMESPACES=xmlns(ave,http%3A%2F%2Frepository.gdi-de.org%2Fschemas%2Fadv%2Fprodukt%2Falkis-vereinfacht%2F1.0)"
                       timeStamp="2025-11-04T07:44:36Z" numberMatched="10" numberReturned="10">
    <wfs:boundedBy>
        <gml:Envelope srsName="urn:ogc:def:crs:EPSG::4326">
            <gml:lowerCorner>50.954994 11.062164</gml:lowerCorner>
            <gml:upperCorner>50.958648 11.071499</gml:upperCorner>
        </gml:Envelope>
    </wfs:boundedBy>
    <wfs:member>
        <ave:Flurstueck xmlns:ave="http://repository.gdi-de.org/schemas/adv/produkt/alkis-vereinfacht/1.0"
                        gml:id="Flurstueck_DETHL51P00005SoZFL">
            <gml:identifier codeSpace="urn:adv:oid:">DETHL51P00005SoZFL</gml:identifier>
            <ave:oid>DETHL51P00005SoZFL</ave:oid>
            <ave:aktualit>2022-10-11</ave:aktualit>
            <ave:geometrie>
                <!--Inlined geometry 'Flurstueck_DETHL51P00005SoZFL_AVE_GEOMETRIE'-->
                <gml:MultiSurface gml:id="Flurstueck_DETHL51P00005SoZFL_AVE_GEOMETRIE"
                                  srsName="urn:ogc:def:crs:EPSG::4326">
                    <gml:surfaceMember>
                        <gml:Polygon gml:id="GEOMETRY_6b22045a-ec68-41fa-83f4-810e3bc500b5"
                                     srsName="urn:ogc:def:crs:EPSG::4326">
                            <gml:exterior>
                                <gml:LinearRing>
                                    <gml:posList>50.958479 11.069321 50.958528 11.069119 50.958493 11.069158 50.958450
                                        11.069193 50.958394 11.069216 50.958323 11.069244 50.958279 11.069261 50.958217
                                        11.069268 50.958182 11.069272 50.958155 11.069279 50.958117 11.069296 50.958102
                                        11.069302 50.958085 11.069304 50.958057 11.069319 50.957926 11.069389 50.957883
                                        11.069412 50.957826 11.069458 50.957807 11.069474 50.957764 11.069508 50.957703
                                        11.069557 50.957686 11.069577 50.957650 11.069619 50.957648 11.069618 50.957615
                                        11.069667 50.957563 11.069745 50.957515 11.069844 50.957448 11.069981 50.957420
                                        11.070039 50.957369 11.070216 50.957359 11.070252 50.957268 11.070562 50.957216
                                        11.070741 50.957226 11.070745 50.957287 11.070766 50.957315 11.070672 50.957342
                                        11.070578 50.957404 11.070365 50.957443 11.070229 50.957484 11.070136 50.957497
                                        11.070107 50.957504 11.070090 50.957527 11.070040 50.957565 11.069959 50.957602
                                        11.069878 50.957651 11.069817 50.957700 11.069756 50.957770 11.069681 50.957854
                                        11.069615 50.957915 11.069582 50.957970 11.069561 50.958048 11.069547 50.958133
                                        11.069533 50.958132 11.069544 50.958195 11.069557 50.958248 11.069578 50.958286
                                        11.069593 50.958314 11.069613 50.958329 11.069624 50.958344 11.069642 50.958357
                                        11.069666 50.958367 11.069694 50.958373 11.069726 50.958374 11.069752 50.958427
                                        11.069537 50.958479 11.069321
                                    </gml:posList>
                                </gml:LinearRing>
                            </gml:exterior>
                        </gml:Polygon>
                    </gml:surfaceMember>
                </gml:MultiSurface>
            </ave:geometrie>
            <ave:idflurst>DETHL51P00005SoZ</ave:idflurst>
            <ave:flaeche>2462</ave:flaeche>
            <ave:flstkennz>160115002004250001__</ave:flstkennz>
            <ave:land>Thüringen</ave:land>
            <ave:gemarkung>Melchendorf</ave:gemarkung>
            <ave:flur>Flur 2</ave:flur>
            <ave:flurstnr>425/1</ave:flurstnr>
            <ave:gmdschl>16051000</ave:gmdschl>
            <ave:regbezirk>unbesetzt</ave:regbezirk>
            <ave:kreis>Erfurt</ave:kreis>
            <ave:gemeinde>Erfurt</ave:gemeinde>
            <ave:lagebeztxt>Tungerstraße</ave:lagebeztxt>
            <ave:tntxt>Strassenverkehr; 2462</ave:tntxt>
        </ave:Flurstueck>
    </wfs:member>
    <wfs:member>
        <ave:Flurstueck xmlns:ave="http://repository.gdi-de.org/schemas/adv/produkt/alkis-vereinfacht/1.0"
                        gml:id="Flurstueck_DETHL51P00005SiCFL">
            <gml:identifier codeSpace="urn:adv:oid:">DETHL51P00005SiCFL</gml:identifier>
            <ave:oid>DETHL51P00005SiCFL</ave:oid>
            <ave:aktualit>2021-03-23</ave:aktualit>
            <ave:geometrie>
                <!--Inlined geometry 'Flurstueck_DETHL51P00005SiCFL_AVE_GEOMETRIE'-->
                <gml:MultiSurface gml:id="Flurstueck_DETHL51P00005SiCFL_AVE_GEOMETRIE"
                                  srsName="urn:ogc:def:crs:EPSG::4326">
                    <gml:surfaceMember>
                        <gml:Polygon gml:id="GEOMETRY_6dd91904-55bd-43f1-8469-987de2617a1c"
                                     srsName="urn:ogc:def:crs:EPSG::4326">
                            <gml:exterior>
                                <gml:Ring>
                                    <gml:curveMember>
                                        <gml:Curve gml:id="GEOMETRY_5e678b0a-4683-4139-83ed-cb2360dbab4d"
                                                   srsName="urn:ogc:def:crs:EPSG::4326">
                                            <gml:segments>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.957359 11.070252 50.957369 11.070216</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.957369 11.070216 50.957420 11.070039</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.957420 11.070039 50.957448 11.069981</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.957448 11.069981 50.957515 11.069844</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.957515 11.069844 50.957563 11.069745</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.957563 11.069745 50.957615 11.069667</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.957615 11.069667 50.957648 11.069618</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.957648 11.069618 50.957650 11.069619</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.957650 11.069619 50.957686 11.069577</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.957686 11.069577 50.957424 11.069407</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:Arc>
                                                    <gml:posList>50.957424 11.069407 50.957427 11.069389 50.957427
                                                        11.069370
                                                    </gml:posList>
                                                </gml:Arc>
                                                <gml:Arc>
                                                    <gml:posList>50.957427 11.069370 50.957423 11.069329 50.957410
                                                        11.069293
                                                    </gml:posList>
                                                </gml:Arc>
                                                <gml:Arc>
                                                    <gml:posList>50.957410 11.069293 50.957367 11.069242 50.957313
                                                        11.069253
                                                    </gml:posList>
                                                </gml:Arc>
                                                <gml:Arc>
                                                    <gml:posList>50.957313 11.069253 50.957294 11.069270 50.957278
                                                        11.069295
                                                    </gml:posList>
                                                </gml:Arc>
                                                <gml:Arc>
                                                    <gml:posList>50.957278 11.069295 50.957267 11.069326 50.957262
                                                        11.069361
                                                    </gml:posList>
                                                </gml:Arc>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.957262 11.069361 50.957233 11.069342</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.957233 11.069342 50.957201 11.069320</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.957201 11.069320 50.957175 11.069415</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.957175 11.069415 50.957113 11.069373</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.957113 11.069373 50.957111 11.069386</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.957111 11.069386 50.956988 11.069303</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956988 11.069303 50.956984 11.069300</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956984 11.069300 50.956958 11.069283</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956958 11.069283 50.956970 11.069237</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956970 11.069237 50.956957 11.069228</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956957 11.069228 50.956964 11.069202</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956964 11.069202 50.956977 11.069211</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956977 11.069211 50.957011 11.069085</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.957011 11.069085 50.956988 11.069070</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956988 11.069070 50.956967 11.069103</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956967 11.069103 50.956952 11.069136</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956952 11.069136 50.956872 11.069313</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956872 11.069313 50.956781 11.069514</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956781 11.069514 50.956686 11.069754</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956686 11.069754 50.956687 11.069778</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956687 11.069778 50.956695 11.069809</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956695 11.069809 50.956595 11.069860</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956595 11.069860 50.956574 11.069885</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956574 11.069885 50.956542 11.069939</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956542 11.069939 50.956524 11.069974</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956524 11.069974 50.956506 11.070025</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956506 11.070025 50.956493 11.070078</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956493 11.070078 50.956481 11.070132</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956481 11.070132 50.956527 11.070161</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956527 11.070161 50.956524 11.070188</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956524 11.070188 50.956504 11.070283</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956504 11.070283 50.956466 11.070456</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956466 11.070456 50.956457 11.070525</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956457 11.070525 50.956434 11.070623</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956434 11.070623 50.956421 11.070663</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956421 11.070663 50.956411 11.070707</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956411 11.070707 50.956385 11.070755</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956385 11.070755 50.956377 11.070762</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956377 11.070762 50.956369 11.070783</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956369 11.070783 50.956370 11.070821</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956370 11.070821 50.956384 11.070822</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956384 11.070822 50.956405 11.070829</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956405 11.070829 50.956428 11.070838</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956428 11.070838 50.956439 11.070855</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956439 11.070855 50.956454 11.070880</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956454 11.070880 50.956468 11.070908</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956468 11.070908 50.956477 11.070934</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956477 11.070934 50.956486 11.070966</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956486 11.070966 50.956492 11.070997</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956492 11.070997 50.956493 11.071000</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956493 11.071000 50.956486 11.071034</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956486 11.071034 50.956496 11.071003</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956496 11.071003 50.956499 11.070996</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956499 11.070996 50.956561 11.071046</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956561 11.071046 50.956583 11.071065</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956583 11.071065 50.956609 11.071084</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956609 11.071084 50.956644 11.071108</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956644 11.071108 50.956753 11.071179</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956753 11.071179 50.956885 11.071265</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956885 11.071265 50.956890 11.071283</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956890 11.071283 50.956908 11.071325</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956908 11.071325 50.956918 11.071342</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956918 11.071342 50.956919 11.071344</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956919 11.071344 50.957023 11.071434</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.957023 11.071434 50.957047 11.071346</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.957047 11.071346 50.957072 11.071255</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.957072 11.071255 50.957106 11.071134</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.957106 11.071134 50.957133 11.071037</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.957133 11.071037 50.957216 11.070741</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.957216 11.070741 50.957268 11.070562</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.957268 11.070562 50.957359 11.070252</gml:posList>
                                                </gml:LineStringSegment>
                                            </gml:segments>
                                        </gml:Curve>
                                    </gml:curveMember>
                                </gml:Ring>
                            </gml:exterior>
                            <gml:interior>
                                <gml:LinearRing>
                                    <gml:posList>50.957242 11.069418 50.957233 11.069455 50.957189 11.069424 50.957197
                                        11.069388 50.957242 11.069418
                                    </gml:posList>
                                </gml:LinearRing>
                            </gml:interior>
                        </gml:Polygon>
                    </gml:surfaceMember>
                </gml:MultiSurface>
            </ave:geometrie>
            <ave:idflurst>DETHL51P00005SiC</ave:idflurst>
            <ave:flaeche>12066</ave:flaeche>
            <ave:flstkennz>16011500200872______</ave:flstkennz>
            <ave:land>Thüringen</ave:land>
            <ave:gemarkung>Melchendorf</ave:gemarkung>
            <ave:flur>Flur 2</ave:flur>
            <ave:flurstnr>872</ave:flurstnr>
            <ave:gmdschl>16051000</ave:gmdschl>
            <ave:regbezirk>unbesetzt</ave:regbezirk>
            <ave:kreis>Erfurt</ave:kreis>
            <ave:gemeinde>Erfurt</ave:gemeinde>
            <ave:lagebeztxt>Tungerstraße 3,4,2</ave:lagebeztxt>
            <ave:tntxt>Wohnbauflaeche; 7584|Wohnbauflaeche; 2856|Strassenverkehr; 1140|Weg; 486</ave:tntxt>
        </ave:Flurstueck>
    </wfs:member>
    <wfs:member>
        <ave:Flurstueck xmlns:ave="http://repository.gdi-de.org/schemas/adv/produkt/alkis-vereinfacht/1.0"
                        gml:id="Flurstueck_DETHL51P00005SpoFL">
            <gml:identifier codeSpace="urn:adv:oid:">DETHL51P00005SpoFL</gml:identifier>
            <ave:oid>DETHL51P00005SpoFL</ave:oid>
            <ave:aktualit>2021-03-23</ave:aktualit>
            <ave:geometrie>
                <!--Inlined geometry 'Flurstueck_DETHL51P00005SpoFL_AVE_GEOMETRIE'-->
                <gml:MultiSurface gml:id="Flurstueck_DETHL51P00005SpoFL_AVE_GEOMETRIE"
                                  srsName="urn:ogc:def:crs:EPSG::4326">
                    <gml:surfaceMember>
                        <gml:Polygon gml:id="GEOMETRY_e9b415a7-decd-4603-8381-06115247d02f"
                                     srsName="urn:ogc:def:crs:EPSG::4326">
                            <gml:exterior>
                                <gml:LinearRing>
                                    <gml:posList>50.958332 11.069938 50.958357 11.069822 50.958374 11.069752 50.958373
                                        11.069726 50.958367 11.069694 50.958357 11.069666 50.958344 11.069642 50.958329
                                        11.069624 50.958314 11.069613 50.958286 11.069593 50.958248 11.069578 50.958195
                                        11.069557 50.958132 11.069544 50.958133 11.069533 50.958048 11.069547 50.957970
                                        11.069561 50.957915 11.069582 50.957854 11.069615 50.957770 11.069681 50.957700
                                        11.069756 50.957651 11.069817 50.957602 11.069878 50.957565 11.069959 50.957527
                                        11.070040 50.957504 11.070090 50.957497 11.070107 50.957826 11.070372 50.957840
                                        11.070386 50.957858 11.070420 50.957867 11.070463 50.957771 11.070931 50.958094
                                        11.071043 50.958179 11.070638 50.958213 11.070483 50.958270 11.070223 50.958296
                                        11.070102 50.958332 11.069938
                                    </gml:posList>
                                </gml:LinearRing>
                            </gml:exterior>
                        </gml:Polygon>
                    </gml:surfaceMember>
                </gml:MultiSurface>
            </ave:geometrie>
            <ave:idflurst>DETHL51P00005Spo</ave:idflurst>
            <ave:flaeche>5780</ave:flaeche>
            <ave:flstkennz>160115002004260001__</ave:flstkennz>
            <ave:land>Thüringen</ave:land>
            <ave:gemarkung>Melchendorf</ave:gemarkung>
            <ave:flur>Flur 2</ave:flur>
            <ave:flurstnr>426/1</ave:flurstnr>
            <ave:gmdschl>16051000</ave:gmdschl>
            <ave:regbezirk>unbesetzt</ave:regbezirk>
            <ave:kreis>Erfurt</ave:kreis>
            <ave:gemeinde>Erfurt</ave:gemeinde>
            <ave:lagebeztxt>Tungerstraße</ave:lagebeztxt>
            <ave:tntxt>Wohnbauflaeche; 5780</ave:tntxt>
        </ave:Flurstueck>
    </wfs:member>
    <wfs:member>
        <ave:Flurstueck xmlns:ave="http://repository.gdi-de.org/schemas/adv/produkt/alkis-vereinfacht/1.0"
                        gml:id="Flurstueck_DETHL51P00005SpqFL">
            <gml:identifier codeSpace="urn:adv:oid:">DETHL51P00005SpqFL</gml:identifier>
            <ave:oid>DETHL51P00005SpqFL</ave:oid>
            <ave:aktualit>2018-08-22</ave:aktualit>
            <ave:geometrie>
                <!--Inlined geometry 'Flurstueck_DETHL51P00005SpqFL_AVE_GEOMETRIE'-->
                <gml:MultiSurface gml:id="Flurstueck_DETHL51P00005SpqFL_AVE_GEOMETRIE"
                                  srsName="urn:ogc:def:crs:EPSG::4326">
                    <gml:surfaceMember>
                        <gml:Polygon gml:id="GEOMETRY_49a42c3f-84b3-4e3f-af81-ef1a18395211"
                                     srsName="urn:ogc:def:crs:EPSG::4326">
                            <gml:exterior>
                                <gml:LinearRing>
                                    <gml:posList>50.957867 11.070463 50.957858 11.070420 50.957840 11.070386 50.957826
                                        11.070372 50.957497 11.070107 50.957484 11.070136 50.957443 11.070229 50.957404
                                        11.070365 50.957342 11.070578 50.957315 11.070672 50.957597 11.070871 50.957771
                                        11.070931 50.957867 11.070463
                                    </gml:posList>
                                </gml:LinearRing>
                            </gml:exterior>
                        </gml:Polygon>
                    </gml:surfaceMember>
                </gml:MultiSurface>
            </ave:geometrie>
            <ave:idflurst>DETHL51P00005Spq</ave:idflurst>
            <ave:flaeche>2155</ave:flaeche>
            <ave:flstkennz>160115002004270001__</ave:flstkennz>
            <ave:land>Thüringen</ave:land>
            <ave:gemarkung>Melchendorf</ave:gemarkung>
            <ave:flur>Flur 2</ave:flur>
            <ave:flurstnr>427/1</ave:flurstnr>
            <ave:gmdschl>16051000</ave:gmdschl>
            <ave:regbezirk>unbesetzt</ave:regbezirk>
            <ave:kreis>Erfurt</ave:kreis>
            <ave:gemeinde>Erfurt</ave:gemeinde>
            <ave:lagebeztxt>Tungerstraße</ave:lagebeztxt>
            <ave:tntxt>Sport Freizeit Und Erholungsflaeche; 2155</ave:tntxt>
        </ave:Flurstueck>
    </wfs:member>
    <wfs:member>
        <ave:Flurstueck xmlns:ave="http://repository.gdi-de.org/schemas/adv/produkt/alkis-vereinfacht/1.0"
                        gml:id="Flurstueck_DETHL51P0000UxmOFL">
            <gml:identifier codeSpace="urn:adv:oid:">DETHL51P0000UxmOFL</gml:identifier>
            <ave:oid>DETHL51P0000UxmOFL</ave:oid>
            <ave:aktualit>2025-04-03</ave:aktualit>
            <ave:geometrie>
                <!--Inlined geometry 'Flurstueck_DETHL51P0000UxmOFL_AVE_GEOMETRIE'-->
                <gml:MultiSurface gml:id="Flurstueck_DETHL51P0000UxmOFL_AVE_GEOMETRIE"
                                  srsName="urn:ogc:def:crs:EPSG::4326">
                    <gml:surfaceMember>
                        <gml:Polygon gml:id="GEOMETRY_15151eb3-40b2-4d94-a2a4-918f0cda69c7"
                                     srsName="urn:ogc:def:crs:EPSG::4326">
                            <gml:exterior>
                                <gml:LinearRing>
                                    <gml:posList>50.956464 11.062654 50.956479 11.062597 50.956496 11.062545 50.956509
                                        11.062513 50.956524 11.062480 50.956540 11.062462 50.956545 11.062436 50.956520
                                        11.062423 50.956515 11.062421 50.956490 11.062404 50.956308 11.062280 50.956229
                                        11.062225 50.956210 11.062211 50.956197 11.062233 50.956177 11.062267 50.956150
                                        11.062395 50.956107 11.062562 50.956096 11.062605 50.956038 11.062827 50.956009
                                        11.062932 50.955955 11.063135 50.955943 11.063175 50.955886 11.063384 50.955845
                                        11.063527 50.955834 11.063517 50.955802 11.063621 50.955805 11.063623 50.955794
                                        11.063661 50.955740 11.063834 50.955652 11.064117 50.955608 11.064258 50.955594
                                        11.064303 50.955536 11.064490 50.955500 11.064604 50.955458 11.064752 50.955367
                                        11.065075 50.955392 11.065081 50.955380 11.065116 50.955347 11.065217 50.955305
                                        11.065347 50.955252 11.065515 50.955202 11.065679 50.955172 11.065786 50.955137
                                        11.065912 50.955121 11.065971 50.955116 11.065988 50.955143 11.065995 50.955103
                                        11.066198 50.955084 11.066295 50.955136 11.066298 50.955145 11.066299 50.955202
                                        11.066302 50.955216 11.066303 50.955223 11.066304 50.955274 11.066307 50.955284
                                        11.066307 50.955320 11.066309 50.955327 11.066271 50.955456 11.065804 50.955476
                                        11.065740 50.955500 11.065656 50.955539 11.065538 50.955563 11.065465 50.955589
                                        11.065383 50.955616 11.065301 50.955663 11.065155 50.955668 11.065141 50.955725
                                        11.064971 50.955799 11.064748 50.955864 11.064554 50.955917 11.064395 50.955969
                                        11.064237 50.956016 11.064097 50.956023 11.064075 50.956081 11.063902 50.956117
                                        11.063794 50.956138 11.063741 50.956155 11.063692 50.956179 11.063711 50.956216
                                        11.063596 50.956248 11.063498 50.956242 11.063493 50.956247 11.063474 50.956231
                                        11.063461 50.956246 11.063412 50.956293 11.063266 50.956359 11.063043 50.956390
                                        11.062934 50.956402 11.062902 50.956437 11.062768 50.956440 11.062769 50.956464
                                        11.062654
                                    </gml:posList>
                                </gml:LinearRing>
                            </gml:exterior>
                        </gml:Polygon>
                    </gml:surfaceMember>
                </gml:MultiSurface>
            </ave:geometrie>
            <ave:idflurst>DETHL51P0000UxmO</ave:idflurst>
            <ave:flaeche>10583</ave:flaeche>
            <ave:flstkennz>160115002003380008__</ave:flstkennz>
            <ave:land>Thüringen</ave:land>
            <ave:gemarkung>Melchendorf</ave:gemarkung>
            <ave:flur>Flur 2</ave:flur>
            <ave:flurstnr>338/8</ave:flurstnr>
            <ave:gmdschl>16051000</ave:gmdschl>
            <ave:regbezirk>unbesetzt</ave:regbezirk>
            <ave:kreis>Erfurt</ave:kreis>
            <ave:gemeinde>Erfurt</ave:gemeinde>
            <ave:lagebeztxt>Kranichfelder Straße</ave:lagebeztxt>
            <ave:tntxt>Strassenverkehr; 7028|Bahnverkehr; 3555</ave:tntxt>
        </ave:Flurstueck>
    </wfs:member>
    <wfs:member>
        <ave:Flurstueck xmlns:ave="http://repository.gdi-de.org/schemas/adv/produkt/alkis-vereinfacht/1.0"
                        gml:id="Flurstueck_DETHL51P0000UxmMFL">
            <gml:identifier codeSpace="urn:adv:oid:">DETHL51P0000UxmMFL</gml:identifier>
            <ave:oid>DETHL51P0000UxmMFL</ave:oid>
            <ave:aktualit>2022-10-11</ave:aktualit>
            <ave:geometrie>
                <!--Inlined geometry 'Flurstueck_DETHL51P0000UxmMFL_AVE_GEOMETRIE'-->
                <gml:MultiSurface gml:id="Flurstueck_DETHL51P0000UxmMFL_AVE_GEOMETRIE"
                                  srsName="urn:ogc:def:crs:EPSG::4326">
                    <gml:surfaceMember>
                        <gml:Polygon gml:id="GEOMETRY_26dc4970-599b-4f0a-bfd3-549e43553630"
                                     srsName="urn:ogc:def:crs:EPSG::4326">
                            <gml:exterior>
                                <gml:Ring>
                                    <gml:curveMember>
                                        <gml:Curve gml:id="GEOMETRY_bd3e88b1-e708-4578-839b-a24b0c709ded"
                                                   srsName="urn:ogc:def:crs:EPSG::4326">
                                            <gml:segments>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956877 11.064566 50.956884 11.064540</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956884 11.064540 50.956938 11.064336</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956938 11.064336 50.956984 11.064161</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956984 11.064161 50.956996 11.064113</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956996 11.064113 50.957036 11.063959</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.957036 11.063959 50.957077 11.063804</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.957077 11.063804 50.957117 11.063650</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.957117 11.063650 50.957166 11.063462</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.957166 11.063462 50.957153 11.063455</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.957153 11.063455 50.957096 11.063425</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.957096 11.063425 50.957069 11.063411</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:Arc>
                                                    <gml:posList>50.957069 11.063411 50.957086 11.063441 50.957086
                                                        11.063481
                                                    </gml:posList>
                                                </gml:Arc>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.957086 11.063481 50.957025 11.063716</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.957025 11.063716 50.956973 11.063899</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956973 11.063899 50.956915 11.064108</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956915 11.064108 50.956855 11.064330</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956855 11.064330 50.956808 11.064509</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956808 11.064509 50.956757 11.064703</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956757 11.064703 50.956709 11.064889</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956709 11.064889 50.956662 11.065072</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956662 11.065072 50.956619 11.065241</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956619 11.065241 50.956565 11.065455</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:Arc>
                                                    <gml:posList>50.956565 11.065455 50.956531 11.065513 50.956489
                                                        11.065555
                                                    </gml:posList>
                                                </gml:Arc>
                                                <gml:Arc>
                                                    <gml:posList>50.956489 11.065555 50.956463 11.065570 50.956436
                                                        11.065580
                                                    </gml:posList>
                                                </gml:Arc>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956436 11.065580 50.956483 11.065604</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956483 11.065604 50.956550 11.065638</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956550 11.065638 50.956568 11.065647</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956568 11.065647 50.956636 11.065425</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956636 11.065425 50.956660 11.065345</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956660 11.065345 50.956682 11.065268</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956682 11.065268 50.956692 11.065238</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956692 11.065238 50.956702 11.065202</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956702 11.065202 50.956766 11.064994</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956766 11.064994 50.956830 11.064746</gml:posList>
                                                </gml:LineStringSegment>
                                                <gml:LineStringSegment interpolation="linear">
                                                    <gml:posList>50.956830 11.064746 50.956877 11.064566</gml:posList>
                                                </gml:LineStringSegment>
                                            </gml:segments>
                                        </gml:Curve>
                                    </gml:curveMember>
                                </gml:Ring>
                            </gml:exterior>
                        </gml:Polygon>
                    </gml:surfaceMember>
                </gml:MultiSurface>
            </ave:geometrie>
            <ave:idflurst>DETHL51P0000UxmM</ave:idflurst>
            <ave:flaeche>1266</ave:flaeche>
            <ave:flstkennz>160115002000250029__</ave:flstkennz>
            <ave:land>Thüringen</ave:land>
            <ave:gemarkung>Melchendorf</ave:gemarkung>
            <ave:flur>Flur 2</ave:flur>
            <ave:flurstnr>25/29</ave:flurstnr>
            <ave:gmdschl>16051000</ave:gmdschl>
            <ave:regbezirk>unbesetzt</ave:regbezirk>
            <ave:kreis>Erfurt</ave:kreis>
            <ave:gemeinde>Erfurt</ave:gemeinde>
            <ave:lagebeztxt>Kruspeweg</ave:lagebeztxt>
            <ave:tntxt>Strassenverkehr; 1266</ave:tntxt>
        </ave:Flurstueck>
    </wfs:member>
    <wfs:member>
        <ave:Flurstueck xmlns:ave="http://repository.gdi-de.org/schemas/adv/produkt/alkis-vereinfacht/1.0"
                        gml:id="Flurstueck_DETHL51P00005SqFFL">
            <gml:identifier codeSpace="urn:adv:oid:">DETHL51P00005SqFFL</gml:identifier>
            <ave:oid>DETHL51P00005SqFFL</ave:oid>
            <ave:aktualit>2023-01-09</ave:aktualit>
            <ave:geometrie>
                <!--Inlined geometry 'Flurstueck_DETHL51P00005SqFFL_AVE_GEOMETRIE'-->
                <gml:MultiSurface gml:id="Flurstueck_DETHL51P00005SqFFL_AVE_GEOMETRIE"
                                  srsName="urn:ogc:def:crs:EPSG::4326">
                    <gml:surfaceMember>
                        <gml:Polygon gml:id="GEOMETRY_500feb9b-9700-479a-bda6-4da585044c0a"
                                     srsName="urn:ogc:def:crs:EPSG::4326">
                            <gml:exterior>
                                <gml:LinearRing>
                                    <gml:posList>50.957086 11.064647 50.956884 11.064540 50.956877 11.064566 50.956830
                                        11.064746 50.957082 11.064910 50.957155 11.064958 50.957240 11.065013 50.957306
                                        11.064763 50.957086 11.064647
                                    </gml:posList>
                                </gml:LinearRing>
                            </gml:exterior>
                        </gml:Polygon>
                    </gml:surfaceMember>
                </gml:MultiSurface>
            </ave:geometrie>
            <ave:idflurst>DETHL51P00005SqF</ave:idflurst>
            <ave:flaeche>850</ave:flaeche>
            <ave:flstkennz>160115002000250017__</ave:flstkennz>
            <ave:land>Thüringen</ave:land>
            <ave:gemarkung>Melchendorf</ave:gemarkung>
            <ave:flur>Flur 2</ave:flur>
            <ave:flurstnr>25/17</ave:flurstnr>
            <ave:gmdschl>16051000</ave:gmdschl>
            <ave:regbezirk>unbesetzt</ave:regbezirk>
            <ave:kreis>Erfurt</ave:kreis>
            <ave:gemeinde>Erfurt</ave:gemeinde>
            <ave:lagebeztxt>Dornheimstraße 13</ave:lagebeztxt>
            <ave:tntxt>Wohnbauflaeche; 850</ave:tntxt>
        </ave:Flurstueck>
    </wfs:member>
    <wfs:member>
        <ave:Flurstueck xmlns:ave="http://repository.gdi-de.org/schemas/adv/produkt/alkis-vereinfacht/1.0"
                        gml:id="Flurstueck_DETHL51P00005SrhFL">
            <gml:identifier codeSpace="urn:adv:oid:">DETHL51P00005SrhFL</gml:identifier>
            <ave:oid>DETHL51P00005SrhFL</ave:oid>
            <ave:aktualit>2018-08-22</ave:aktualit>
            <ave:geometrie>
                <!--Inlined geometry 'Flurstueck_DETHL51P00005SrhFL_AVE_GEOMETRIE'-->
                <gml:MultiSurface gml:id="Flurstueck_DETHL51P00005SrhFL_AVE_GEOMETRIE"
                                  srsName="urn:ogc:def:crs:EPSG::4326">
                    <gml:surfaceMember>
                        <gml:Polygon gml:id="GEOMETRY_bac63f96-bea5-43d8-9c7a-3161f0dc5b84"
                                     srsName="urn:ogc:def:crs:EPSG::4326">
                            <gml:exterior>
                                <gml:LinearRing>
                                    <gml:posList>50.956283 11.064806 50.956023 11.064672 50.955936 11.064606 50.955864
                                        11.064554 50.955799 11.064748 50.955972 11.064832 50.956163 11.064927 50.956204
                                        11.064947 50.956232 11.064961 50.956277 11.064825 50.956283 11.064806
                                    </gml:posList>
                                </gml:LinearRing>
                            </gml:exterior>
                        </gml:Polygon>
                    </gml:surfaceMember>
                </gml:MultiSurface>
            </ave:geometrie>
            <ave:idflurst>DETHL51P00005Srh</ave:idflurst>
            <ave:flaeche>647</ave:flaeche>
            <ave:flstkennz>160115002000250026__</ave:flstkennz>
            <ave:land>Thüringen</ave:land>
            <ave:gemarkung>Melchendorf</ave:gemarkung>
            <ave:flur>Flur 2</ave:flur>
            <ave:flurstnr>25/26</ave:flurstnr>
            <ave:gmdschl>16051000</ave:gmdschl>
            <ave:regbezirk>unbesetzt</ave:regbezirk>
            <ave:kreis>Erfurt</ave:kreis>
            <ave:gemeinde>Erfurt</ave:gemeinde>
            <ave:lagebeztxt>Kranichfelder Straße 73</ave:lagebeztxt>
            <ave:tntxt>Wohnbauflaeche; 647</ave:tntxt>
        </ave:Flurstueck>
    </wfs:member>
    <wfs:member>
        <ave:Flurstueck xmlns:ave="http://repository.gdi-de.org/schemas/adv/produkt/alkis-vereinfacht/1.0"
                        gml:id="Flurstueck_DETHL51P00005SrIFL">
            <gml:identifier codeSpace="urn:adv:oid:">DETHL51P00005SrIFL</gml:identifier>
            <ave:oid>DETHL51P00005SrIFL</ave:oid>
            <ave:aktualit>2018-08-22</ave:aktualit>
            <ave:geometrie>
                <!--Inlined geometry 'Flurstueck_DETHL51P00005SrIFL_AVE_GEOMETRIE'-->
                <gml:MultiSurface gml:id="Flurstueck_DETHL51P00005SrIFL_AVE_GEOMETRIE"
                                  srsName="urn:ogc:def:crs:EPSG::4326">
                    <gml:surfaceMember>
                        <gml:Polygon gml:id="GEOMETRY_21422e6e-5533-45fc-9463-8790016939e5"
                                     srsName="urn:ogc:def:crs:EPSG::4326">
                            <gml:exterior>
                                <gml:LinearRing>
                                    <gml:posList>50.956338 11.064638 50.956334 11.064651 50.956283 11.064806 50.956277
                                        11.064825 50.956543 11.064996 50.956608 11.065038 50.956662 11.065072 50.956709
                                        11.064889 50.956338 11.064638
                                    </gml:posList>
                                </gml:LinearRing>
                            </gml:exterior>
                        </gml:Polygon>
                    </gml:surfaceMember>
                </gml:MultiSurface>
            </ave:geometrie>
            <ave:idflurst>DETHL51P00005SrI</ave:idflurst>
            <ave:flaeche>638</ave:flaeche>
            <ave:flstkennz>160115002000250012__</ave:flstkennz>
            <ave:land>Thüringen</ave:land>
            <ave:gemarkung>Melchendorf</ave:gemarkung>
            <ave:flur>Flur 2</ave:flur>
            <ave:flurstnr>25/12</ave:flurstnr>
            <ave:gmdschl>16051000</ave:gmdschl>
            <ave:regbezirk>unbesetzt</ave:regbezirk>
            <ave:kreis>Erfurt</ave:kreis>
            <ave:gemeinde>Erfurt</ave:gemeinde>
            <ave:lagebeztxt>Kruspeweg 16</ave:lagebeztxt>
            <ave:tntxt>Wohnbauflaeche; 638</ave:tntxt>
        </ave:Flurstueck>
    </wfs:member>
    <wfs:member>
        <ave:Flurstueck xmlns:ave="http://repository.gdi-de.org/schemas/adv/produkt/alkis-vereinfacht/1.0"
                        gml:id="Flurstueck_DETHL51P00005SqHFL">
            <gml:identifier codeSpace="urn:adv:oid:">DETHL51P00005SqHFL</gml:identifier>
            <ave:oid>DETHL51P00005SqHFL</ave:oid>
            <ave:aktualit>2018-08-22</ave:aktualit>
            <ave:geometrie>
                <!--Inlined geometry 'Flurstueck_DETHL51P00005SqHFL_AVE_GEOMETRIE'-->
                <gml:MultiSurface gml:id="Flurstueck_DETHL51P00005SqHFL_AVE_GEOMETRIE"
                                  srsName="urn:ogc:def:crs:EPSG::4326">
                    <gml:surfaceMember>
                        <gml:Polygon gml:id="GEOMETRY_eee5a898-d45a-4225-88b8-7b8288457a3f"
                                     srsName="urn:ogc:def:crs:EPSG::4326">
                            <gml:exterior>
                                <gml:LinearRing>
                                    <gml:posList>50.957082 11.064910 50.956830 11.064746 50.956766 11.064994 50.957188
                                        11.065213 50.957240 11.065013 50.957155 11.064958 50.957082 11.064910
                                    </gml:posList>
                                </gml:LinearRing>
                            </gml:exterior>
                        </gml:Polygon>
                    </gml:surfaceMember>
                </gml:MultiSurface>
            </ave:geometrie>
            <ave:idflurst>DETHL51P00005SqH</ave:idflurst>
            <ave:flaeche>850</ave:flaeche>
            <ave:flstkennz>160115002000250018__</ave:flstkennz>
            <ave:land>Thüringen</ave:land>
            <ave:gemarkung>Melchendorf</ave:gemarkung>
            <ave:flur>Flur 2</ave:flur>
            <ave:flurstnr>25/18</ave:flurstnr>
            <ave:gmdschl>16051000</ave:gmdschl>
            <ave:regbezirk>unbesetzt</ave:regbezirk>
            <ave:kreis>Erfurt</ave:kreis>
            <ave:gemeinde>Erfurt</ave:gemeinde>
            <ave:lagebeztxt>Dornheimstraße 15</ave:lagebeztxt>
            <ave:tntxt>Wohnbauflaeche; 850</ave:tntxt>
        </ave:Flurstueck>
    </wfs:member>
</wfs:FeatureCollection>
    """

    val SURFACE_MEMBER = """
        <gml:MultiSurface xmlns:gml="${Namespaces.GML}" gml:id="Flurstueck_DETHL51P00005SoZFL_AVE_GEOMETRIE"
                                  srsName="urn:ogc:def:crs:EPSG::4326">
                    <gml:surfaceMember>
                        <gml:Polygon gml:id="GEOMETRY_6b22045a-ec68-41fa-83f4-810e3bc500b5"
                                     srsName="urn:ogc:def:crs:EPSG::4326">
                            <gml:exterior>
                                <gml:LinearRing>
                                    <gml:posList>50.958479 11.069321 50.958528 11.069119 50.958493 11.069158 50.958450
                                        11.069193 50.958394 11.069216 50.958323 11.069244 50.958279 11.069261 50.958217
                                        11.069268 50.958182 11.069272 50.958155 11.069279 50.958117 11.069296 50.958102
                                        11.069302 50.958085 11.069304 50.958057 11.069319 50.957926 11.069389 50.957883
                                        11.069412 50.957826 11.069458 50.957807 11.069474 50.957764 11.069508 50.957703
                                        11.069557 50.957686 11.069577 50.957650 11.069619 50.957648 11.069618 50.957615
                                        11.069667 50.957563 11.069745 50.957515 11.069844 50.957448 11.069981 50.957420
                                        11.070039 50.957369 11.070216 50.957359 11.070252 50.957268 11.070562 50.957216
                                        11.070741 50.957226 11.070745 50.957287 11.070766 50.957315 11.070672 50.957342
                                        11.070578 50.957404 11.070365 50.957443 11.070229 50.957484 11.070136 50.957497
                                        11.070107 50.957504 11.070090 50.957527 11.070040 50.957565 11.069959 50.957602
                                        11.069878 50.957651 11.069817 50.957700 11.069756 50.957770 11.069681 50.957854
                                        11.069615 50.957915 11.069582 50.957970 11.069561 50.958048 11.069547 50.958133
                                        11.069533 50.958132 11.069544 50.958195 11.069557 50.958248 11.069578 50.958286
                                        11.069593 50.958314 11.069613 50.958329 11.069624 50.958344 11.069642 50.958357
                                        11.069666 50.958367 11.069694 50.958373 11.069726 50.958374 11.069752 50.958427
                                        11.069537 50.958479 11.069321
                                    </gml:posList>
                                </gml:LinearRing>
                            </gml:exterior>
                        </gml:Polygon>
                    </gml:surfaceMember>
                </gml:MultiSurface>
    """.trimIndent()
    val GEOM = """
                        <gml:Polygon xmlns:gml="${Namespaces.GML}" gml:id="GEOMETRY_6b22045a-ec68-41fa-83f4-810e3bc500b5"
                                     srsName="urn:ogc:def:crs:EPSG::4326">
                            <gml:exterior>
                                <gml:LinearRing>
                                    <gml:posList>50.958479 11.069321 50.958528 11.069119</gml:posList>
                                </gml:LinearRing>
                            </gml:exterior>
                        </gml:Polygon>
    """.trimIndent()
}