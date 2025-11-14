package cloud.mallne.geokit.ogc.model.wfs

enum class ResolveValueType(
    val value: Int, val named: String, val literal: String
) {
    /**
     * The '***Local***' literal object.
     */
    LOCAL(0, "local", "local"),

    /**
     * The '***Remote***' literal object.
     */
    REMOTE(1, "remote", "remote"),

    /**
     * The '***All***' literal object.
     */
    ALL(2, "all", "all"),

    /**
     * The '***None***' literal object.
     */
    NONE(3, "none", "none");

    /**
     * Returns the literal value of the enumerator, which is its string representation.
     */
    override fun toString(): String {
        return literal
    }
}