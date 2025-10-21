package cloud.mallne.geokit.coordinates.ast

import cloud.mallne.geokit.coordinates.ast.expression.NumberLiteral
import cloud.mallne.geokit.coordinates.ast.expression.TextLiteral
import cloud.mallne.geokit.coordinates.ast.expression.WKTCRSExpression
import cloud.mallne.geokit.coordinates.generated.WKTCRSParser
import cloud.mallne.geokit.coordinates.generated.WKTCRSParserBaseVisitor

/**
 * Builds an AST from WKT-CRS input using the generated ANTLR visitor.
 * This step introduces basic literal handling and a top-level parse helper,
 * keeping behavior safe while more nodes are implemented incrementally.
 */
class WKTCRSAstBuilder : WKTCRSParserBaseVisitor<WKTCRSExpression>() {
    override fun defaultResult(): WKTCRSExpression =
        throw IllegalStateException("Visitor method not implemented or rule produced no result.")

    // --- Basic literals ---
    override fun visitQuotedText(ctx: WKTCRSParser.QuotedTextContext): WKTCRSExpression {
        val raw = ctx.text
        return TextLiteral(unquote(raw))
    }

    override fun visitNumber(ctx: WKTCRSParser.NumberContext): WKTCRSExpression {
        val num = ctx.text.trim().toDoubleOrNull() ?: Double.NaN
        return NumberLiteral(num)
    }
}