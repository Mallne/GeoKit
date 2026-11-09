package cloud.mallne.geokit.coordinates.execution

import cloud.mallne.geokit.coordinates.tokens.ast.expression.AbstractOperationParameter
import cloud.mallne.geokit.coordinates.tokens.ast.expression.AbstractParameter

interface IdentityLocator {
    val commonNames: List<String>
    val identity: String?
    companion object {
        fun List<AbstractParameter>.findParameterByName(name: String): AbstractParameter? {
            val exactMatch = find { it.name.equals(name, ignoreCase = true) }
            return exactMatch ?: find { parameter -> parameter.name.contains(name, ignoreCase = true) }
        }
        fun List<AbstractParameter>.findParameterById(id: String): AbstractParameter? {
            return find { parameter -> parameter.identifiers.find { it.epsgId == id } != null }
        }
        fun List<AbstractParameter>.findParameter(parameter: IdentityLocator): AbstractParameter? {
            return parameter.identity?.let {findParameterById(it)} ?: parameter.commonNames.firstNotNullOfOrNull { findParameterByName(it) }
        }

        fun <T: IdentityLocator> List<T>.findByName(name: String): T? {
            val exactMatch = find { loc -> loc.commonNames.any { it.equals(name, ignoreCase = true) } }
            return exactMatch ?: find { loc -> loc.commonNames.any { it.contains(name, ignoreCase = true) } }
        }
        fun <T: IdentityLocator>  List<T>.findById(id: String): T? {
            return find { it.identity == id }
        }
    }
}
