package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class Identifier(
    val authorityName: String,
    val uid: Literal,
    val version: Literal? = null,
    val citation: String? = null,
    val uri: String? = null,
) : WKTCRSExpression {
    val epsgId = "${authorityName}:$uid"

    fun urn(base: String = COMMON_URN_BASE) = constructUrn(authorityName, uid.toString(), base)

    companion object {
        private const val COMMON_URN_BASE: String = "urn:ogc:def:crs"
        fun constructUrn(authorityName: String, uid: String, base: String = COMMON_URN_BASE) =
            "$base:$authorityName::$uid"

        fun deconstructUrn(urn: String): Pair<String, String> {
            val uidSplit = urn.split("::")
            val uid = uidSplit.last()
            val list = uidSplit.dropLast(1)
            val closestLast = list.last()
            val authSplit = closestLast.split(":")
            val auth = authSplit.last()
            return auth to uid
        }

        fun deconstructUrnId(urn: String): String {
            val (auth, uid) = deconstructUrn(urn)
            return "$auth:$uid"
        }

        val List<Identifier>.epsgId: String?
            get() {
                return if (size == 1) {
                    first().epsgId
                } else {
                    null
                }
            }
    }
}
