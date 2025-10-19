import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskExecutionException
import java.io.File

/**
 * A custom Gradle task to parse WKT files and generate immutable Kotlin data
 * structures and constants for the GeoKit CRS registry.
 */
@CacheableTask
abstract class WktCodeGeneratorTask : DefaultTask() {

    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val wktSourceDir: DirectoryProperty

    @get:OutputDirectory
    abstract val kotlinOutputDir: DirectoryProperty

    @TaskAction
    fun generate() {
        // Ensure the output directory exists
        val outputDirFile = kotlinOutputDir.get().asFile
        outputDirFile.mkdirs()

        // Define the target package
        val packageName = "geokit.crs.systems"

        // Process each WKT file found in the input directory
        wktSourceDir.get().asFileTree.files.filter { it.extension == "wkt" }.forEach { wktFile ->
            try {
                processWktFile(wktFile, outputDirFile, packageName)
            } catch (e: Exception) {
                logger.error("Failed to process WKT file: ${wktFile.name}", e)
                throw TaskExecutionException(this, e)
            }
        }
    }

    private fun processWktFile(wktFile: File, outputDir: File, packageName: String) {
        val wktContent = wktFile.readText().replace("\\s+".toRegex(), " ") // Normalize whitespace

        // 1. Extract EPSG Code (e.g., ID["EPSG",4326])
        val epsgMatch = "ID\\[\"EPSG\",(\\d+)\\]".toRegex().find(wktContent)
        val epsgCode = epsgMatch?.groupValues?.get(1)?.toIntOrNull()
            ?: throw IllegalStateException("Could not find EPSG code in ${wktFile.name}")

        // 2. Extract Ellipsoid parameters (Name, a, 1/f)
        val ellipsoidRegex = "ELLIPSOID\\[\"([^\"]+)\",(\\d+\\.?\\d*),(\\d+\\.?\\d*)\\]".toRegex()
        val ellipsoidMatch = ellipsoidRegex.find(wktContent)
            ?: throw IllegalStateException("Could not find ELLIPSOID data in ${wktFile.name}")

        val ellipsoidName = ellipsoidMatch.groupValues[1]
        val semiMajorAxisA = ellipsoidMatch.groupValues[2] // 6378137.0
        val inverseFlattening = ellipsoidMatch.groupValues[3] // 298.257223563

        // 3. Extract CRS Name and Type (GEOGCRS or PROJCRS)
        val crsMatch = "(GEOGCRS|PROJCRS)\\[\"([^\"]+)\"".toRegex().find(wktContent)
        val crsTypeString = crsMatch?.groupValues?.get(1) ?: "GEOGCRS" // Default to GEOGCRS if primary type is ambiguous
        val crsName = crsMatch?.groupValues?.get(2) ?: "Unknown CRS $epsgCode"

        // 4. Extract Axis Order (simplified: looking for (Lat, Lon) or (Lon, Lat))
        val axisOrderString = wktContent.split("AXIS").drop(1).joinToString(",") {
            it.split(",").firstOrNull()?.trim('"', '[') ?: ""
        }.split(",").filter { it.isNotBlank() }

        val axisOrder = when {
            axisOrderString.contains("Geodetic latitude (Lat)") && axisOrderString.contains("Geodetic longitude (Lon)") ->
                "listOf(\"Lat\", \"Lon\")" // EPSG:4326 order
            else -> "listOf(\"Lon\", \"Lat\")" // Assume Lon, Lat if not explicitly Lat, Lon
        }

        // Determine the generated file name
        val baseName = crsName.replace("\\s+".toRegex(), "_").replace("[^A-Za-z0-9_]".toRegex(), "")
        val outputFileName = "${baseName}_${epsgCode}.kt"
        val outputFile = outputDir.resolve(outputFileName)

        // 5. Generate Kotlin Code
        outputFile.writeText(
            """
package $packageName

import geokit.crs.*

// --- $crsName (EPSG:$epsgCode) - Generated from WKT by WktCodeGeneratorTask ---

private val GENERATED_ELLIPSOID_$epsgCode = Ellipsoid(
    name = "$ellipsoidName",
    a = $semiMajorAxisA,
    inverseFlattening = $inverseFlattening
)

val GENERATED_CRS_$epsgCode = CrsDefinition(
    epsgCode = $epsgCode,
    name = "$crsName",
    type = CrsType.${crsTypeString},
    ellipsoid = GENERATED_ELLIPSOID_$epsgCode,
    axisOrder = $axisOrder
)

// Automatic registration on library load
init {
    CrsRegistry.register(GENERATED_CRS_$epsgCode)
}
            """.trimIndent()
        )
        logger.quiet("Generated CRS Kotlin code for EPSG:$epsgCode at ${outputFile.path}")
    }
}