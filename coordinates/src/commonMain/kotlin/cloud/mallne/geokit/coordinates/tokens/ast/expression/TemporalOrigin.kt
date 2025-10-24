package cloud.mallne.geokit.coordinates.tokens.ast.expression

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

sealed interface TemporalOrigin : WKTCRSExpression {
    data class TemporalDateTimeOrigin @OptIn(ExperimentalTime::class) constructor(val dateTime: Instant) :
        TemporalOrigin

    data class TemporalStringOrigin @OptIn(ExperimentalTime::class) constructor(val dateTime: String) :
        TemporalOrigin
}
