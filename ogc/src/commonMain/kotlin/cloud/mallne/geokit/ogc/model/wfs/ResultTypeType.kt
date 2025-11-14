package cloud.mallne.geokit.ogc.model.wfs

/**
 * A representation of the literals of the enumeration '***Result Type Type***',
 * and utility methods for working with them.
 */
enum class ResultTypeType(
    val value: Int, val named: String, val literal: String
) {
    /**
     * The '***Results***' literal object.
     */
    RESULTS(0, "results", "results"),

    /**
     * The '***Hits***' literal object.
     */
    HITS(1, "hits", "hits");


    /**
     * Returns the literal value of the enumerator, which is its string representation.
     */
    override fun toString(): String {
        return literal
    }
}

