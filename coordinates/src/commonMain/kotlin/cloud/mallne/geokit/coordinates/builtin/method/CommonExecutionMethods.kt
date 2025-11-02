package cloud.mallne.geokit.coordinates.builtin.method

import cloud.mallne.geokit.coordinates.execution.ExecutionDispatchMethod

enum class CommonExecutionMethods(
    method: ExecutionDispatchMethod,
): ExecutionDispatchMethod by method {
    `EPSG:1031`(EPSG1031),
    `EPSG:9602`(EPSG9602),
    `EPSG:9603`(EPSG9603),
    `EPSG:9659`(EPSG9659),
    `EPSG:9807`(EPSG9807),
    ;
}