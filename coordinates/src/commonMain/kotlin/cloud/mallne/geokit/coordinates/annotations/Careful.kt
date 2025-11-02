package cloud.mallne.geokit.coordinates.annotations

@RequiresOptIn(
    message = "You should be careful about using this API, please read the message.",
    level = RequiresOptIn.Level.WARNING,
)
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class Careful(
    val message: String,
)
