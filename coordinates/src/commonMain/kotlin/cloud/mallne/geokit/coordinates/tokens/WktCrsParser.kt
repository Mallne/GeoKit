package cloud.mallne.geokit.coordinates.tokens

import cloud.mallne.geokit.coordinates.generated.WKTCRSLexer
import cloud.mallne.geokit.coordinates.generated.WKTCRSParser
import cloud.mallne.geokit.coordinates.tokens.ast.WKTCRSAstBuilder
import cloud.mallne.geokit.coordinates.tokens.ast.expression.RootNode
import org.antlr.v4.kotlinruntime.BailErrorStrategy
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream
import org.antlr.v4.kotlinruntime.misc.ParseCancellationException

object WktCrsParser {
    operator fun invoke(contents: String): RootNode =
        parse(contents)

    fun parse(contents: String): RootNode {
        val charStream = CharStreams.fromString(contents)
        val lexer = WKTCRSLexer(charStream)
        lexer.removeErrorListeners() // Remove default console error logging from lexer

        val tokens = CommonTokenStream(lexer)
        val parser = WKTCRSParser(tokens)

        parser.removeErrorListeners() // Remove default console error logging from parser
        parser.errorHandler = BailErrorStrategy() // Configure parser to fail fast on syntax errors

        try {
            val parseTree = parser.wktCRS() // Start parsing from the top-level rule

            val astBuilder = WKTCRSAstBuilder()

            // The visit method now returns a non-nullable AccessorExpression or throws an exception.
            val ast = astBuilder.visit(parseTree) as RootNode

            return ast // Directly return the non-nullable AST

        } catch (e: ParseCancellationException) {
            // This catches syntax errors identified by BailErrorStrategy during the parsing phase.
            throw IllegalArgumentException("Invalid Accessor WOL syntax: ${e.message}", e)
        } catch (e: IllegalArgumentException) {
            // Catch specific IllegalArgumentExceptions thrown by the AST builder (e.g., from visitErrorNode)
            throw IllegalArgumentException("Error in Accessor WOL syntax or AST construction: ${e.message}", e)
        } catch (e: IllegalStateException) {
            // Catches exceptions thrown by defaultResult() in the visitor,
            // indicating an unhandled or unexpected parse tree state during AST building.
            throw RuntimeException(
                "Unhandled parse tree node during Accessor WOL AST construction for: '${contents}'. ${e.message}",
                e
            )
        } catch (e: Exception) {
            // Catch any other unexpected exceptions during the process.
            throw RuntimeException(
                "An unexpected error occurred during Accessor WOL parsing or AST building for: '${contents}'",
                e
            )
        }
    }
}