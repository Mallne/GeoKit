package cloud.mallne.geokit.coordinates.ast.expression

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class TemporalExtent(
    val start: Instant? = null,
    val end: Instant? = null,
    val startString: String? = null,
    val endString: String? = null,
) : ExtendStructure