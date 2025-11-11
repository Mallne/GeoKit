package cloud.mallne.geokit.coordinates.execution

import cloud.mallne.geokit.coordinates.annotations.Careful
import cloud.mallne.geokit.coordinates.execution.IdentityLocator.Companion.findById
import cloud.mallne.geokit.coordinates.execution.IdentityLocator.Companion.findByName
import cloud.mallne.geokit.coordinates.model.AbstractCoordinate
import cloud.mallne.geokit.coordinates.tokens.ast.expression.AbstractParameter
import cloud.mallne.geokit.coordinates.tokens.ast.expression.MapProjectionMethod
import cloud.mallne.geokit.coordinates.tokens.ast.expression.OperationMethod

data class MethodDispatcher(
    private val methods: List<ExecutionDispatchMethod> = emptyList(),
    private val context: GeokitCoordinateConversionContext,
) : List<ExecutionDispatchMethod> by methods {

    fun dispatch(
        method: OperationMethod,
        coordinate: AbstractCoordinate,
        parameters: List<AbstractParameter> = emptyList(),
        reverse: Boolean = false
    ): AbstractCoordinate {
        val match = method.identifiers.firstOrNull()
        return if (match != null) {
            dispatchById(match.epsgId, coordinate, parameters, reverse)
        } else {
            dispatchByName(method.name, coordinate, parameters, reverse)
        }
    }

    fun dispatch(
        method: MapProjectionMethod,
        coordinate: AbstractCoordinate,
        parameters: List<AbstractParameter> = emptyList(),
        reverse: Boolean = false
    ): AbstractCoordinate {
        val match = method.identifiers.firstOrNull()
        return if (match != null) {
            dispatchById(match.epsgId, coordinate, parameters, reverse)
        } else {
            dispatchByName(method.name, coordinate, parameters, reverse)
        }
    }

    @OptIn(Careful::class)
    fun dispatchById(
        id: String,
        coordinate: AbstractCoordinate,
        parameters: List<AbstractParameter> = emptyList(),
        reverse: Boolean = false
    ): AbstractCoordinate {
        val match = findById(id)
        return if (match != null) {
            doDispatch(match, coordinate, parameters, reverse)
        } else {
            throw IllegalArgumentException("No method found for id: $id")
        }
    }

    @OptIn(Careful::class)
    fun dispatchByName(
        name: String,
        coordinate: AbstractCoordinate,
        parameters: List<AbstractParameter> = emptyList(),
        reverse: Boolean = false
    ): AbstractCoordinate {
        val match = findByName(name)
        return if (match != null) {
            doDispatch(match, coordinate, parameters, reverse)
        } else {
            throw IllegalArgumentException("No method found for name: $name")
        }
    }

    @Careful("While you could call this method directly, it would skip the internal Method Registry and therefore will not use any overwritten methods! Use `dispatch` or `dispatchById` or `dispatchByName` instead.")
    fun doDispatch(
        method: ExecutionDispatchMethod,
        coordinate: AbstractCoordinate,
        parameters: List<AbstractParameter> = emptyList(),
        reverse: Boolean = false
    ): AbstractCoordinate {
        return method.execute(coordinate, parameters, context, this, reverse)
    }
}