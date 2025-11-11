package cloud.mallne.geokit.coordinates.builtin.method

import cloud.mallne.geokit.coordinates.execution.ExecutionDispatchMethod

enum class CommonExecutionMethods(
    method: ExecutionDispatchMethod,
) : ExecutionDispatchMethod by method {
    EPSG_1031(EPSG1031),
    EPSG_9602(EPSG9602),
    EPSG_9603(EPSG9603),
    EPSG_9659(EPSG9659),
    EPSG_9807(EPSG9807),
    ;
}