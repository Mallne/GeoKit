package cloud.mallne.geokit.interop

import cloud.mallne.geokit.geojson.*
import cloud.mallne.geokit.geojson.CalculationInterop.toPosition
import cloud.mallne.geokit.geojson.LineString
import cloud.mallne.geokit.geojson.MultiLineString
import cloud.mallne.geokit.geojson.MultiPoint
import cloud.mallne.geokit.geojson.MultiPolygon
import cloud.mallne.geokit.geojson.Point
import cloud.mallne.geokit.geojson.Polygon
import cloud.mallne.geokit.interop.GmlExtensions.toGeoJson
import cloud.mallne.geokit.interop.GmlExtensions.toGml
import cloud.mallne.geokit.ogc.model.gml.AbstractRingPropertyType
import cloud.mallne.geokit.ogc.model.gml.DirectPositionType
import cloud.mallne.geokit.ogc.model.gml.geometry.*
import cloud.mallne.geokit.ogc.model.gml.member.LineStringMember
import cloud.mallne.geokit.ogc.model.gml.member.PointMember
import cloud.mallne.geokit.ogc.model.gml.member.PolygonMember
import cloud.mallne.geokit.ogc.model.gml.geometry.AbstractGeometryType as GmlGeometry
import cloud.mallne.geokit.ogc.model.gml.geometry.LineString as GmlLineString
import cloud.mallne.geokit.ogc.model.gml.geometry.MultiLineString as GmlMultiLineString
import cloud.mallne.geokit.ogc.model.gml.geometry.MultiPoint as GmlMultiPoint
import cloud.mallne.geokit.ogc.model.gml.geometry.MultiPolygon as GmlMultiPolygon
import cloud.mallne.geokit.ogc.model.gml.geometry.Point as GmlPoint
import cloud.mallne.geokit.ogc.model.gml.geometry.Polygon as GmlPolygon

object GmlGeometryExtensions {
    // GML <-> GeoJSON conversions
    // LineString <-> LineString
    fun LineString.toGml(): GmlLineString = GmlLineString(posList = listOf(this.coordinates.toGml()))
    fun GmlLineString.toGeoJson(): LineString = LineString(this.positions.toGeoJson().flatten())

    //MultiLineString <-> MultiLineString
    fun MultiLineString.toGml(): GmlMultiLineString =
        GmlMultiLineString(lineStringMember = this.coordinates.map { LineStringMember(GmlLineString(listOf(it.toGml()))) })

    fun GmlMultiLineString.toGeoJson(): MultiLineString =
        MultiLineString(this.lineStringMember.map { it.lineString.positions.toGeoJson().flatten() })

    //MultiPoint <-> MultiPoint
    fun MultiPoint.toGml(): GmlMultiPoint = GmlMultiPoint(this.coordinates.map { PointMember(it.toGmlPoint()) })
    fun GmlMultiPoint.toGeoJson(): MultiPoint = MultiPoint(this.pointMember.map { it.point.toGeoJsonPoint() })

    //MultiPolygon <-> MultiPolygon
    fun MultiPolygon.toGml(): GmlMultiPolygon =
        GmlMultiPolygon(this.coordinates.map { lists ->
            val dpt = lists.toGml()
            PolygonMember(
                GmlPolygon(
                    exterior = AbstractRingPropertyType(LinearRing(posList = listOf(dpt.first()))),
                    interior = if (dpt.size > 1) dpt.subList(1, dpt.size)
                        .map { AbstractRingPropertyType(LinearRing(posList = listOf(it))) } else listOf()
                )
            )
        })

    fun GmlMultiPolygon.toGeoJson(): MultiPolygon =
        MultiPolygon(this.polygonMember.map { polygonMember ->
            polygonMember.polygon.toGeoJson().coordinates
        })

    //Point <-> Point
    fun Point.toGml(): GmlPoint = GmlPoint(pos = this.coordinates.toGml())
    fun GmlPoint.toGeoJson(): Point = Point(this.pos.toVertex()!!.toPosition())

    //Polygon <-> Polygon
    fun Polygon.toGml(): GmlPolygon {
        val dpt = this.coordinates.toGml()
        return GmlPolygon(
            exterior = AbstractRingPropertyType(LinearRing(posList = listOf(dpt.first()))),
            interior = if (dpt.size > 1) dpt.subList(1, dpt.size)
                .map { AbstractRingPropertyType(LinearRing(posList = listOf(it))) } else listOf()
        )
    }

    fun GmlPolygon.toGeoJson(): Polygon {
        // 1. Extract exterior ring coordinates
        val exteriorCoords = this.exterior.ring.extractCoordinates().toGeoJson().flatten()

        // 2. Extract and map all interior ring coordinates
        val interiorCoords = this.interior.map { it.ring.extractCoordinates().toGeoJson() }.flatten()

        // 3. Build the final GeoJSON structure: [ [exterior], [interior1], [interior2], ... ]
        val allRings = mutableListOf<List<Position>>()
        allRings.add(exteriorCoords)
        allRings.addAll(interiorCoords)

        return Polygon(allRings)
    }

    private fun AbstractRingType.extractCoordinates() = when (this) {
        is LinearRing -> this.positions
        is Ring -> this.curveMember.map { it.curve.extractCoordinates() }.flatten()
    }

    private fun AbstractCurveType.extractCoordinates() = when (this) {
        is Curve -> this.segments.segments.map { it.positions }.flatten()
        is GmlLineString -> this.positions
    }

    //Curve -> LineString
    fun Curve.toGeoJson(): LineString = LineString(this.extractCoordinates().toGeoJson().flatten())

    //MultiCurve -> MultiLineString
    fun MultiCurve.toGeoJson(): MultiLineString =
        MultiLineString(this.curveMember.map { it.curve.extractCoordinates().toGeoJson().flatten() })

    //LinearRing -> Polygon
    fun LinearRing.toGeoJson(): Polygon = Polygon(this.positions.toGeoJson().flatten())
    //Ring -> Polygon
    fun Ring.toGeoJson(): Polygon = Polygon(this.curveMember.map { it.curve.extractCoordinates().toGeoJson() }.flatten())

    // MultiSurface -> MultiPolygon
    fun MultiSurface.toGeoJson(): MultiPolygon = MultiPolygon(this.surfaceMember.map { it.surface.extractCoordinates().flatten().toGeoJson() })

    private fun AbstractSurfaceType.extractCoordinates() = when (this) {
        is GmlPolygon -> listOf(this.exterior.ring.extractCoordinates()) + this.interior.map { it.ring.extractCoordinates() }
    }

    fun Geometry.toGml(): GmlGeometry = when (this) {
        is GeometryCollection<*> -> TODO("Converting from a GeometryCollection is not yet supported!")
        is LineString -> this.toGml()
        is MultiLineString -> this.toGml()
        is MultiPoint -> this.toGml()
        is MultiPolygon -> this.toGml()
        is Point -> this.toGml()
        is Polygon -> this.toGml()
    }

    fun GmlGeometry.toGeoJson(): Geometry = when (this) {
        is GmlLineString -> this.toGeoJson()
        is GmlMultiLineString -> this.toGeoJson()
        is GmlMultiPoint -> this.toGeoJson()
        is GmlMultiPolygon -> this.toGeoJson()
        is MultiSurface -> this.toGeoJson()
        is GmlPoint -> this.toGeoJson()
        is GmlPolygon -> this.toGeoJson()
        is Curve -> this.toGeoJson()
        is MultiCurve -> this.toGeoJson()
        is LinearRing -> this.toGeoJson()
        is Ring -> this.toGeoJson()
    }

    fun DirectPositionType.toGeoJson(): List<Position> = this.value.toGeoJson()
    fun List<DirectPositionType>.toGeoJson(): List<List<Position>> = this.map { it.toGeoJson() }

    fun Position.toGmlPoint(): GmlPoint = GmlPoint(pos = this.toGml())
    fun GmlPoint.toGeoJsonPoint(): Position = this.pos.toVertex()!!.toPosition()
}