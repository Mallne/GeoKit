package cloud.mallne.geokit.coordinates.execution

import cloud.mallne.geokit.Vertex
import cloud.mallne.geokit.coordinates.model.Coordinate
import cloud.mallne.geokit.coordinates.tokens.ast.expression.*

data class CoordinateOperationTransformer(
    override val reversed: Boolean,
    override val operation: CoordinateOperation,
) : CoordinateOperationPipeline<CoordinateOperation> {

    override fun execute(input: Vertex): Coordinate {
        CoordinateOperation(
            name = "ETRS89 to WGS 84 (1)",
            version = "EPSG-eur",
            source = StaticGeographicCrs(
                name = "ETRS89",
                system = GeodeticDatumEnsemble(
                    name = "European Terrestrial Reference System 1989 ensemble",
                    members = listOf(
                        DatumEnsembleMember(
                            name = "European Terrestrial Reference Frame 1989",
                            identifiers = listOf(
                                Identifier(
                                    authorityName = "EPSG",
                                    uid = 1178.0,
                                    version = null,
                                    citation = null,
                                    uri = null
                                )
                            )
                        ), DatumEnsembleMember(
                            name = "European Terrestrial Reference Frame 1990",
                            identifiers = listOf(
                                Identifier(
                                    authorityName = "EPSG",
                                    uid = 1179.0,
                                    version = null,
                                    citation = null,
                                    uri = null
                                )
                            )
                        ), DatumEnsembleMember(
                            name = "European Terrestrial Reference Frame 1991",
                            identifiers = listOf(
                                Identifier(
                                    authorityName = "EPSG",
                                    uid = 1180.0,
                                    version = null,
                                    citation = null,
                                    uri = null
                                )
                            )
                        ), DatumEnsembleMember(
                            name = "European Terrestrial Reference Frame 1992",
                            identifiers = listOf(
                                Identifier(
                                    authorityName = "EPSG",
                                    uid = 1181.0,
                                    version = null,
                                    citation = null,
                                    uri = null
                                )
                            )
                        ), DatumEnsembleMember(
                            name = "European Terrestrial Reference Frame 1993",
                            identifiers = listOf(
                                Identifier(
                                    authorityName = "EPSG",
                                    uid = 1182.0,
                                    version = null,
                                    citation = null,
                                    uri = null
                                )
                            )
                        ), DatumEnsembleMember(
                            name = "European Terrestrial Reference Frame 1994",
                            identifiers = listOf(
                                Identifier(
                                    authorityName = "EPSG",
                                    uid = 1183.0,
                                    version = null,
                                    citation = null,
                                    uri = null
                                )
                            )
                        ), DatumEnsembleMember(
                            name = "European Terrestrial Reference Frame 1996",
                            identifiers = listOf(
                                Identifier(
                                    authorityName = "EPSG",
                                    uid = 1184.0,
                                    version = null,
                                    citation = null,
                                    uri = null
                                )
                            )
                        ), DatumEnsembleMember(
                            name = "European Terrestrial Reference Frame 1997",
                            identifiers = listOf(
                                Identifier(
                                    authorityName = "EPSG",
                                    uid = 1185.0,
                                    version = null,
                                    citation = null,
                                    uri = null
                                )
                            )
                        ), DatumEnsembleMember(
                            name = "European Terrestrial Reference Frame 2000",
                            identifiers = listOf(
                                Identifier(
                                    authorityName = "EPSG",
                                    uid = 1186.0,
                                    version = null,
                                    citation = null,
                                    uri = null
                                )
                            )
                        ), DatumEnsembleMember(
                            name = "European Terrestrial Reference Frame 2005",
                            identifiers = listOf(
                                Identifier(
                                    authorityName = "EPSG",
                                    uid = 1204.0,
                                    version = null,
                                    citation = null,
                                    uri = null
                                )
                            )
                        ), DatumEnsembleMember(
                            name = "European Terrestrial Reference Frame 2014",
                            identifiers = listOf(
                                Identifier(
                                    authorityName = "EPSG",
                                    uid = 1206.0,
                                    version = null,
                                    citation = null,
                                    uri = null
                                )
                            )
                        ), DatumEnsembleMember(
                            name = "European Terrestrial Reference Frame 2020",
                            identifiers = listOf(
                                Identifier(
                                    authorityName = "EPSG",
                                    uid = 1382.0,
                                    version = null,
                                    citation = null,
                                    uri = null
                                )
                            )
                        )
                    ),
                    ellipsoid = Ellipsoid(
                        name = "GRS 1980",
                        semiMajorAxis = 6378137.0,
                        inverseFlattening = 298.257222101,
                        unit = LengthUnit(
                            unitName = "metre",
                            conversionFactor = 1.0,
                            identifiers = listOf(
                                Identifier(
                                    authorityName = "EPSG",
                                    uid = 9001.0,
                                    version = null,
                                    citation = null,
                                    uri = null
                                )
                            )
                        ),
                        identifiers = listOf(
                            Identifier(
                                authorityName = "EPSG",
                                uid = 7019.0,
                                version = null,
                                citation = null,
                                uri = null
                            )
                        )
                    ),
                    accuracy = 0.1,
                    identifiers = listOf(
                        Identifier(
                            authorityName = "EPSG",
                            uid = 6258.0,
                            version = null,
                            citation = null,
                            uri = null
                        )
                    ),
                    primeMeridian = null
                ),
                coordinateSystem = SpatialCS(
                    type = SpatialCSType.ELLIPSOIDAL,
                    dimension = Dimension.D2,
                    identifiers = listOf(
                        Identifier(
                            authorityName = "EPSG",
                            uid = 6422.0,
                            version = null,
                            citation = null,
                            uri = null
                        )
                    ),
                    spatialAxis = listOf(
                        SpatialAxis(
                            name = "Geodetic latitude (Lat)",
                            direction = AxisDirection.NORTH(meridian = null),
                            order = null,
                            unit = null,
                            range = null,
                            identifiers = listOf()
                        ), SpatialAxis(
                            name = "Geodetic longitude (Lon)",
                            direction = AxisDirection.EAST,
                            order = null,
                            unit = null,
                            range = null,
                            identifiers = listOf()
                        )
                    ),
                    unit = AngleUnit(
                        unitName = "degree",
                        conversionFactor = 0.0174532925199433,
                        identifiers = listOf(
                            Identifier(
                                authorityName = "EPSG",
                                uid = 9102.0,
                                version = null,
                                citation = null,
                                uri = null
                            )
                        )
                    )
                ),
                definingTransformationIDs = listOf(),
                usages = listOf(),
                identifiers = listOf(
                    Identifier(
                        authorityName = EPSG,
                        uid = 4258.0,
                        version = null,
                        citation = null,
                        uri = null
                    )
                ),
                remark = null
            ),
            target = StaticGeographicCrs(
                name = "WGS 84",
                system = GeodeticDatumEnsemble(
                    name = "World Geodetic System 1984 ensemble",
                    members = listOf(
                        DatumEnsembleMember(
                            name = "World Geodetic System 1984 (Transit)",
                            identifiers = listOf(
                                Identifier(
                                    authorityName = "EPSG",
                                    uid = 1166.0,
                                    version = null,
                                    citation = null,
                                    uri = null
                                )
                            )
                        ),
                        DatumEnsembleMember(
                            name = "World Geodetic System 1984(G730)",
                            identifiers = listOf(
                                Identifier(
                                    authorityName = "EPSG",
                                    uid = 1152.0,
                                    version = null,
                                    citation = null,
                                    uri = null
                                )
                            )
                        ),
                        DatumEnsembleMember(
                            name = "World Geodetic System 1984(G873)",
                            identifiers = listOf(
                                Identifier(
                                    authorityName = "EPSG",
                                    uid = 1153.0,
                                    version = null,
                                    citation = null,
                                    uri = null
                                )
                            )
                        ),
                        DatumEnsembleMember(
                            name = "World Geodetic System 1984(G1150)",
                            identifiers = listOf(
                                Identifier(
                                    authorityName = "EPSG",
                                    uid = 1154.0,
                                    version = null,
                                    citation = null,
                                    uri = null
                                )
                            )
                        ),
                        DatumEnsembleMember(
                            name = "World Geodetic System 1984(G1674)",
                            identifiers = listOf(
                                Identifier(
                                    authorityName = EPSG,
                                    uid = 1155.0,
                                    version = null,
                                    citation = null,
                                    uri = null
                                )
                            )
                        ),
                        DatumEnsembleMember(
                            name = "World Geodetic System 1984(G1762)",
                            identifiers = listOf(
                                Identifier(
                                    authorityName = EPSG,
                                    uid = 1156.0,
                                    version = null,
                                    citation = null,
                                    uri = null
                                )
                            )
                        ),
                        DatumEnsembleMember(
                            name = "World Geodetic System 1984(G2139)",
                            identifiers = listOf(
                                Identifier(
                                    authorityName = EPSG,
                                    uid = 1309.0,
                                    version = null,
                                    citation = null,
                                    uri = null
                                )
                            )
                        ),
                        DatumEnsembleMember(
                            name = "World Geodetic System 1984(G2296)",
                            identifiers = listOf(
                                Identifier(
                                    authorityName = EPSG,
                                    uid = 1383.0,
                                    version = null,
                                    citation = null,
                                    uri = null
                                )
                            )
                        )
                    ),
                    ellipsoid = Ellipsoid(
                        name = "WGS 84",
                        semiMajorAxis = 6378137.0,
                        inverseFlattening = 298.257223563,
                        unit = LengthUnit(
                            unitName = "metre",
                            conversionFactor = 1.0,
                            identifiers = listOf(
                                Identifier(
                                    authorityName = "EPSG",
                                    uid = 9001.0,
                                    version = null,
                                    citation = null,
                                    uri = null
                                )
                            )
                        ),
                        identifiers = listOf(
                            Identifier(
                                authorityName = "EPSG",
                                uid = 7030.0,
                                version = null,
                                citation = null,
                                uri = null
                            )
                        )
                    ),
                    accuracy = 2.0,
                    identifiers = listOf(
                        Identifier(
                            authorityName = "EPSG",
                            uid = 6326.0,
                            version = null,
                            citation = null,
                            uri = null
                        )
                    ),
                    primeMeridian = null
                ),
                coordinateSystem = SpatialCS(
                    type = SpatialCSType.ELLIPSOIDAL,
                    dimension = Dimension.D2,
                    identifiers = listOf(
                        Identifier(
                            authorityName = "EPSG",
                            uid = 6422.0,
                            version = null,
                            citation = null,
                            uri = null
                        )
                    ),
                    spatialAxis = listOf(
                        SpatialAxis(
                            name = "Geodetic latitude (Lat)",
                            direction = AxisDirection.NORTH(meridian = null),
                            order = null,
                            unit = null,
                            range = null,
                            identifiers = listOf()
                        ),
                        SpatialAxis(
                            name = "Geodetic longitude (Lon)",
                            direction = AxisDirection.EAST,
                            order = null,
                            unit = null,
                            range = null,
                            identifiers = listOf()
                        )
                    ),
                    unit = AngleUnit(
                        unitName = "degree",
                        conversionFactor = 0.0174532925199433,
                        identifiers = listOf(
                            Identifier(
                                authorityName = "EPSG",
                                uid = 9102.0,
                                version = null,
                                citation = null,
                                uri = null
                            )
                        )
                    )
                ),
                definingTransformationIDs = listOf(),
                usages = listOf(),
                identifiers = listOf(
                    Identifier(
                        authorityName = "EPSG",
                        uid = 4326.0,
                        version = null,
                        citation = null,
                        uri = null
                    )
                ),
                remark = null
            ),
            method = OperationMethod(
                name = "Geocentric translations (geog2D domain)",
                identifiers = listOf(
                    Identifier(
                        authorityName = "EPSG",
                        uid = 9603.0,
                        version = null,
                        citation = null,
                        uri = null
                    )
                )
            ),
            parameters = listOf(
                OperationParameter(
                    name = "X - axis translation",
                    value = 0.0,
                    unit = LengthUnit(
                        unitName = "metre",
                        conversionFactor = 1.0,
                        identifiers = listOf(
                            Identifier(
                                authorityName = "EPSG",
                                uid = 9001.0,
                                version = null,
                                citation = null,
                                uri = null
                            )
                        )
                    ),
                    identifiers = listOf(
                        Identifier(
                            authorityName = "EPSG",
                            uid = 8605.0,
                            version = null,
                            citation = null,
                            uri = null
                        )
                    )
                ),
                OperationParameter(
                    name = "Y - axis translation",
                    value = 0.0,
                    unit = LengthUnit(
                        unitName = "metre",
                        conversionFactor = 1.0,
                        identifiers = listOf(
                            Identifier(
                                authorityName = "EPSG",
                                uid = 9001.0,
                                version = null,
                                citation = null,
                                uri = null
                            )
                        )
                    ),
                    identifiers = listOf(
                        Identifier(
                            authorityName = "EPSG",
                            uid = 8606.0,
                            version = null,
                            citation = null,
                            uri = null
                        )
                    )
                ),
                OperationParameter(
                    name = "Z - axis translation",
                    value = 0.0,
                    unit = LengthUnit(
                        unitName = "metre",
                        conversionFactor = 1.0,
                        identifiers = listOf(
                            Identifier(
                                authorityName = "EPSG",
                                uid = 9001.0,
                                version = null,
                                citation = null,
                                uri = null
                            )
                        )
                    ),
                    identifiers = listOf(
                        Identifier(
                            authorityName = "EPSG",
                            uid = 8607.0,
                            version = null,
                            citation = null,
                            uri = null
                        )
                    )
                )
            ),
            interpolation = null,
            accuracy = 1.0,
            usages = listOf(),
            identifiers = listOf(
                Identifier(
                    authorityName = "EPSG",
                    uid = 1149.0,
                    version = null,
                    citation = null,
                    uri = null
                )
            ),
            remark = null
        )
    }
}