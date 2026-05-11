package cloud.mallne.geokit.fwi.model

import kotlinx.serialization.Serializable

/**
 * Fire danger classification based on Fire Weather Index (FWI) value.
 *
 * This enum provides a simple 6-tier classification of fire danger that is
 * suitable for UI display, alerts, and public warnings.
 *
 * ## FWI to Danger Level Mapping
 * | FWI Value | Level | Color (typical) | Actions |
 * |-----------|-------|----------------|----------|
 * | < 5 | VERY_LOW | Green | Normal activities |
 * | 5-12 | LOW | Yellow | Exercise caution |
 * | 12-24 | MODERATE | Orange | Restricted fire use |
 * | 24-32 | HIGH | Red | Fire bans possible |
 * | 32-50 | VERY_HIGH | Red+ | Active fire management |
 * | > 50 | EXTREME | Purple | Emergency alerts |
 *
 * ## Usage Examples
 * ```kotlin
 * // Get danger level from FWI value
 * val level = FireDangerLevel.fromFWI(28.5)
 * println(level.displayName)  // "High"
 * println(level.description)   // "Fires spread quickly. Active suppression required..."
 *
 * // Use in conditional logic
 * val needsAlert = when (engine.calculate(input).dangerLevel) {
 *     FireDangerLevel.EXTREME -> true
 *     FireDangerLevel.VERY_HIGH -> true
 *     else -> false
 * }
 *
 * // Compare danger levels
 * if (level.ordinal >= FireDangerLevel.HIGH.ordinal) {
 *     showWarningIcon()
 * }
 * ```
 *
 * @property displayName Human-readable name suitable for UI display.
 * @property description Brief description of the fire behavior at this danger level.
 */
@Serializable
enum class FireDangerLevel(
    /** Human-readable name for display in UI. */
    val displayName: String,
    /** Description of fire behavior at this danger level. */
    val description: String
) {
    /** Fire spread is unlikely. Small fires can be easily controlled. */
    VERY_LOW("Very Low", "Fire spread is unlikely. Small fires can be easily controlled."),

    /** Fires may spread slowly but can be controlled. Some fire hazard. */
    LOW("Low", "Fires may spread slowly but can be controlled. Some fire hazard."),

    /** Fires can spread but control is feasible. Moderate fire hazard. */
    MODERATE("Moderate", "Fires can spread but control is feasible. Moderate fire hazard."),

    /** Fires spread quickly. Active suppression required. High fire hazard. */
    HIGH("High", "Fires spread quickly. Active suppression required. High fire hazard."),

    /** Fires spread very quickly. Extreme caution required. Very high fire hazard. */
    VERY_HIGH("Very High", "Fires spread very quickly. Extreme caution required. Very high fire hazard."),

    /** Fires spread explosively. Life-threatening conditions. Extreme fire hazard. */
    EXTREME("Extreme", "Fires spread explosively. Life-threatening conditions. Extreme fire hazard.");

    companion object {
        /**
         * Converts a raw FWI value to the appropriate [FireDangerLevel].
         *
         * @param fwi The Fire Weather Index value (typically 0-100+).
         * @return The corresponding danger level.
         * @throws IllegalArgumentException if fwi is negative.
         */
        fun fromFWI(fwi: Double): FireDangerLevel = when {
            fwi < 0.0 -> throw IllegalArgumentException("FWI cannot be negative")
            fwi < 5.0 -> VERY_LOW
            fwi < 12.0 -> LOW
            fwi < 24.0 -> MODERATE
            fwi < 32.0 -> HIGH
            fwi < 50.0 -> VERY_HIGH
            else -> EXTREME
        }
    }
}