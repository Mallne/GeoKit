package cloud.mallne.geokit.ogc

import cloud.mallne.geokit.Boundary
import cloud.mallne.geokit.Vertex
import cloud.mallne.geokit.ogc.model.Envelope
import cloud.mallne.geokit.ogc.model.LowerCorner
import cloud.mallne.geokit.ogc.model.UpperCorner
import cloud.mallne.geokit.ogc.model.fes.BBOX
import cloud.mallne.geokit.ogc.model.wfs.WfsBoundedBy


object Extensions {
    fun Vertex.toGml(): List<Double> = listOf(latitude, longitude)
    fun List<Vertex>.toGml(): List<Double> = this.flatMap { it.toGml() }
    fun List<Double>.toVertex(): List<Vertex> {
        require(this.size % 2 == 0) {
            "Vertexlist must have an even number of values"
        }
        val l = mutableListOf<Vertex>()
        for (i in 0 until (this.size - 1)) {
            if (i % 2 == 0) {
                val thisOne = this[i]
                val thisTwo = this[i + 1]
                val vertex = Vertex(thisOne, thisTwo)
                l.add(vertex)
            }
        }
        return l
    }

    fun Boundary.toGmlBBOX(): BBOX = BBOX(
        envelope = Envelope(
            lowerCorner = LowerCorner(this.southWest.toGml()),
            upperCorner = UpperCorner(this.northEast.toGml())
        ),
    )

    fun BBOX.toBoundary(): Boundary = Boundary(
        northEast = this.envelope.upperCorner.values.toVertex().first(),
        southWest = this.envelope.lowerCorner.values.toVertex().first(),
    )

    fun Boundary.toGmlBoundedBy(): WfsBoundedBy = WfsBoundedBy(
        Envelope(lowerCorner = LowerCorner(this.southWest.toGml()), upperCorner = UpperCorner(this.northEast.toGml())),
    )

    fun WfsBoundedBy.toBoundary(): Boundary? = this.envelope?.let {
        Boundary(
            northEast = it.upperCorner.values.toVertex().first(),
            southWest = it.lowerCorner.values.toVertex().first(),
        )
    }


}