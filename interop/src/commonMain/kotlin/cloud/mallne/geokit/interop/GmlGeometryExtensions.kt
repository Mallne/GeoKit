package cloud.mallne.geokit.interop

import cloud.mallne.geokit.geojson.*
import cloud.mallne.geokit.geojson.CalculationInterop.toPosition
import cloud.mallne.geokit.gml.model.Exterior
import cloud.mallne.geokit.gml.model.Interior
import cloud.mallne.geokit.gml.model.LinearRing
import cloud.mallne.geokit.gml.model.SurfaceMember
import cloud.mallne.geokit.gml.model.geometry.MultiSurface
import cloud.mallne.geokit.interop.GmlExtensions.toGeoJson
import cloud.mallne.geokit.interop.GmlExtensions.toGml
import cloud.mallne.geokit.gml.model.geometry.Geometry as GmlGeometry
import cloud.mallne.geokit.gml.model.geometry.LineString as GmlLineString
import cloud.mallne.geokit.gml.model.geometry.MultiLineString as GmlMultiLineString
import cloud.mallne.geokit.gml.model.geometry.MultiPoint as GmlMultiPoint
import cloud.mallne.geokit.gml.model.geometry.MultiPolygon as GmlMultiPolygon
import cloud.mallne.geokit.gml.model.geometry.Point as GmlPoint
import cloud.mallne.geokit.gml.model.geometry.Polygon as GmlPolygon

object GmlGeometryExtensions {
    fun Geometry.toGml(): GmlGeometry = when (this) {
        is GeometryCollection<*> -> this.toGml()
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
    }

    fun GeometryCollection<*>.toGml(): MultiSurface =
        MultiSurface(surfaceMembers = this.geometries.map { SurfaceMember(it.toGml()) })

    fun MultiSurface.toGeoJson(): GeometryCollection<Geometry> =
        GeometryCollection(geometries = this.surfaceMembers.map { it.geometry.toGeoJson() })

    fun LineString.toGml(): GmlLineString = GmlLineString(posList = this.coordinates.toGml())
    fun GmlLineString.toGeoJson(): LineString = LineString(this.posList.toGeoJson())
    fun MultiLineString.toGml(): GmlMultiLineString =
        GmlMultiLineString(lineStringMember = this.coordinates.map { GmlLineString(posList = it.toGml()) })

    fun GmlMultiLineString.toGeoJson(): MultiLineString =
        MultiLineString(this.lineStringMember.map { it.posList.toGeoJson() })

    fun MultiPoint.toGml(): GmlMultiPoint = GmlMultiPoint(this.coordinates.map { it.toGmlPoint() })
    fun GmlMultiPoint.toGeoJson(): MultiPoint = MultiPoint(this.pointMember.map { it.toGeoJsonPoint() })
    fun MultiPolygon.toGml(): GmlMultiPolygon =
        GmlMultiPolygon(this.coordinates.map { lists ->
            GmlPolygon(
                exterior = Exterior(LinearRing(lists.first().toGml())),
                interior = if (lists.size > 1) Interior(
                    lists.subList(1, lists.size).map { LinearRing(it.toGml()) }) else Interior()
            )
        })

    fun GmlMultiPolygon.toGeoJson(): MultiPolygon =
        MultiPolygon(this.polygonMember.map { polygon ->
            polygon.toGeoJson().coordinates
        })

    fun Point.toGml(): GmlPoint = GmlPoint(pos = this.coordinates.toGml())
    fun GmlPoint.toGeoJson(): Point = Point(this.vertex.toPosition())
    fun Polygon.toGml(): GmlPolygon = GmlPolygon(
        exterior = Exterior(LinearRing(this.coordinates.first().toGml())),
        interior = if (this.coordinates.size > 1) Interior(
            this.coordinates.subList(1, this.coordinates.size).map { LinearRing(it.toGml()) }) else Interior()
    )

    fun GmlPolygon.toGeoJson(): Polygon {
        val ext = this.exterior.ring.posList.toGeoJson()
        val int = this.interior.map { it.posList.toGeoJson() }.toMutableList()
        int.add(0, ext)
        return Polygon(int)
    }

    fun Position.toGmlPoint(): GmlPoint = GmlPoint(pos = this.toGml())
    fun GmlPoint.toGeoJsonPoint(): Position = this.vertex.toPosition()
}