package cloud.mallne.geokit.coordinates.tokens.ast

import cloud.mallne.geokit.coordinates.generated.WKTCRSParser
import cloud.mallne.geokit.coordinates.generated.WKTCRSParserBaseVisitor
import cloud.mallne.geokit.coordinates.tokens.ast.expression.*
import kotlinx.datetime.*
import kotlinx.datetime.format.char
import kotlinx.datetime.format.optional
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Builds an AST from WKT-CRS input using the generated ANTLR visitor.
 * This step introduces basic literal handling and a top-level parse helper,
 * keeping behavior safe while more nodes are implemented incrementally.
 */
class WKTCRSAstBuilder :
    WKTCRSParserBaseVisitor<WKTCRSExpression>() {
    override fun defaultResult(): WKTCRSExpression =
        throw IllegalStateException("Visitor method not implemented or rule produced no result.")

    override fun visitAngleUnit(ctx: WKTCRSParser.AngleUnitContext) =
        AngleUnit(
            unitName = ctx.quotedText().computed,
            conversionFactor = ctx.number().computed,
            identifiers = ctx.id().computed
        )

    override fun visitWktCRS(ctx: WKTCRSParser.WktCRSContext): RootNode = when {
        ctx.boundCrs() != null -> visitBoundCrs(ctx.boundCrs()!!)
        ctx.compoundCrs() != null -> visitCompoundCrs(ctx.compoundCrs()!!)
        ctx.engineeringCrs() != null -> visitEngineeringCrs(ctx.engineeringCrs()!!)
        ctx.geodeticCrs() != null -> visitGeodeticCrs(ctx.geodeticCrs()!!)
        ctx.geographicCrs() != null -> visitGeographicCrs(ctx.geographicCrs()!!)
        ctx.parametricCrs() != null -> visitParametricCrs(ctx.parametricCrs()!!)
        ctx.projectedCrs() != null -> visitProjectedCrs(ctx.projectedCrs()!!)
        ctx.temporalCrs() != null -> visitTemporalCrs(ctx.temporalCrs()!!)
        ctx.verticalCrs() != null -> visitVerticalCrs(ctx.verticalCrs()!!)
        ctx.derivedGeodeticCrs() != null -> visitDerivedGeodeticCrs(ctx.derivedGeodeticCrs()!!)
        ctx.derivedProjectedCrs() != null -> visitDerivedProjectedCrs(ctx.derivedProjectedCrs()!!)
        ctx.derivedVerticalCrs() != null -> visitDerivedVerticalCrs(ctx.derivedVerticalCrs()!!)
        ctx.derivedEngineeringCrs() != null -> visitDerivedEngineeringCrs(ctx.derivedEngineeringCrs()!!)
        ctx.derivedParametricCrs() != null -> visitDerivedParametricCrs(ctx.derivedParametricCrs()!!)
        ctx.derivedTemporalCrs() != null -> visitDerivedTemporalCrs(ctx.derivedTemporalCrs()!!)
        ctx.coordinateOperation() != null -> visitCoordinateOperation(ctx.coordinateOperation()!!)
        ctx.pmo() != null -> visitPmo(ctx.pmo()!!)
        ctx.concatenatedOperation() != null -> visitConcatenatedOperation(ctx.concatenatedOperation()!!)
        ctx.coordinateMetadata() != null -> visitCoordinateMetadata(ctx.coordinateMetadata()!!)
        ctx.datumEnsemble() != null -> visitDatumEnsemble(ctx.datumEnsemble()!!)
        else -> throw IllegalArgumentException("Visitor method not implemented or rule produced no result.")
    }

    override fun visitConcatenatedOperation(ctx: WKTCRSParser.ConcatenatedOperationContext) =
        visitScopeExtentIdentifierRemark(ctx.scopeExtentIdentifierRemark()).let {
            ConcatenatedOperation(
                name = ctx.quotedText().computed,
                version = ctx.operationVersion()?.let { visitOperationVersion(it).text },
                source = visitSourceCrs(ctx.sourceCrs()),
                target = visitTargetCrs(ctx.targetCrs()),
                steps = ctx.step().map { visitStep(it) },
                accuracy = ctx.operationAccuracy()?.let { visitOperationAccuracy(it).value },
                usages = it.usages,
                identifiers = it.identifiers,
                remark = it.remark
            )
        }

    override fun visitStep(ctx: WKTCRSParser.StepContext): SteppedOperation = when {
        ctx.coordinateOperation() != null -> visitCoordinateOperation(ctx.coordinateOperation()!!)
        ctx.pmo() != null -> visitPmo(ctx.pmo()!!)
        ctx.mapProjection() != null -> visitMapProjection(ctx.mapProjection()!!)
        ctx.derivingConversion() != null -> visitDerivingConversion(ctx.derivingConversion()!!)
        else -> throw IllegalArgumentException("Visitor method not implemented or rule produced no result.")
    }

    override fun visitPmo(ctx: WKTCRSParser.PmoContext) =
        visitScopeExtentIdentifierRemark(ctx.scopeExtentIdentifierRemark()).let { proxy ->
            val p = ctx.operationParameter().map { visitOperationParameter(it) }
            val pf = ctx.operationParameterFile().map { visitOperationParameterFile(it) }
            PointMotionOperation(
                name = ctx.quotedText().computed,
                version = ctx.operationVersion()?.let { visitOperationVersion(it).text },
                source = visitSourceCrs(ctx.sourceCrs()),
                method = visitOperationMethod(ctx.operationMethod()),
                parameters = p + pf,
                accuracy = ctx.operationAccuracy()?.let { visitOperationAccuracy(it).value },
                usages = proxy.usages,
                identifiers = proxy.identifiers,
                remark = proxy.remark
            )
        }


    override fun visitOperationAccuracy(ctx: WKTCRSParser.OperationAccuracyContext) = visitNumber(ctx.number())

    override fun visitInterpolationCrs(ctx: WKTCRSParser.InterpolationCrsContext) = visitCrs(ctx.crs())

    override fun visitCrs(ctx: WKTCRSParser.CrsContext) = when {
        ctx.singleCrs() != null -> visitSingleCrs(ctx.singleCrs()!!)
        ctx.compoundCrs() != null -> visitCompoundCrs(ctx.compoundCrs()!!)
        else -> throw IllegalArgumentException("Visitor method not implemented or rule produced no result.")
    }

    override fun visitCoordinateOperation(ctx: WKTCRSParser.CoordinateOperationContext) =
        visitScopeExtentIdentifierRemark(ctx.scopeExtentIdentifierRemark()).let { proxy ->
            val p = ctx.operationParameter().map { visitOperationParameter(it) }
            val pf = ctx.operationParameterFile().map { visitOperationParameterFile(it) }
            CoordinateOperation(
                name = ctx.quotedText().computed,
                version = ctx.operationVersion()?.let { visitOperationVersion(it).text },
                source = visitSourceCrs(ctx.sourceCrs()),
                method = visitOperationMethod(ctx.operationMethod()),
                parameters = p + pf,
                target = visitTargetCrs(ctx.targetCrs()),
                interpolation = ctx.interpolationCrs()?.let { visitInterpolationCrs(it) },
                accuracy = ctx.operationAccuracy()?.let { visitOperationAccuracy(it).value },
                usages = proxy.usages,
                identifiers = proxy.identifiers,
                remark = proxy.remark
            )
        }

    override fun visitDynamicCrsCoordinateMetadata(ctx: WKTCRSParser.DynamicCrsCoordinateMetadataContext): DynamicCrsCoordinateMetadata =
        when {
            ctx.projectedCrs() != null -> visitProjectedCrs(ctx.projectedCrs()!!)
            ctx.derivedGeodeticCrs() != null -> visitDerivedGeodeticCrs(ctx.derivedGeodeticCrs()!!)
            ctx.derivedProjectedCrs() != null -> visitDerivedProjectedCrs(ctx.derivedProjectedCrs()!!)
            ctx.derivedVerticalCrs() != null -> visitDerivedVerticalCrs(ctx.derivedVerticalCrs()!!)
            ctx.compoundCrs() != null -> visitCompoundCrs(ctx.compoundCrs()!!)
            else -> throw IllegalArgumentException("Visitor method not implemented or rule produced no result.")
        }

    override fun visitStaticCrsCoordinateMetadata(ctx: WKTCRSParser.StaticCrsCoordinateMetadataContext): StaticCrsCoordinateMetadata =
        when {
            ctx.staticGeodeticCrs() != null -> visitStaticGeodeticCrs(ctx.staticGeodeticCrs()!!)
            ctx.staticGeographicCrs() != null -> visitStaticGeographicCrs(ctx.staticGeographicCrs()!!)
            ctx.projectedCrs() != null -> visitProjectedCrs(ctx.projectedCrs()!!)
            ctx.staticVerticalCrs() != null -> visitStaticVerticalCrs(ctx.staticVerticalCrs()!!)
            ctx.engineeringCrs() != null -> visitEngineeringCrs(ctx.engineeringCrs()!!)
            ctx.parametricCrs() != null -> visitParametricCrs(ctx.parametricCrs()!!)
            ctx.temporalCrs() != null -> visitTemporalCrs(ctx.temporalCrs()!!)
            ctx.derivedGeodeticCrs() != null -> visitDerivedGeodeticCrs(ctx.derivedGeodeticCrs()!!)
            ctx.derivedProjectedCrs() != null -> visitDerivedProjectedCrs(ctx.derivedProjectedCrs()!!)
            ctx.derivedVerticalCrs() != null -> visitDerivedVerticalCrs(ctx.derivedVerticalCrs()!!)
            ctx.derivedEngineeringCrs() != null -> visitDerivedEngineeringCrs(ctx.derivedEngineeringCrs()!!)
            ctx.derivedParametricCrs() != null -> visitDerivedParametricCrs(ctx.derivedParametricCrs()!!)
            ctx.derivedTemporalCrs() != null -> visitDerivedTemporalCrs(ctx.derivedTemporalCrs()!!)
            ctx.compoundCrs() != null -> visitCompoundCrs(ctx.compoundCrs()!!)
            else -> throw IllegalArgumentException("Visitor method not implemented or rule produced no result.")
        }

    override fun visitCoordinateMetadata(ctx: WKTCRSParser.CoordinateMetadataContext) = when {
        ctx.staticCrsCoordinateMetadata() != null -> CoordinateMetadata.StaticCoordinateMetadata(
            visitStaticCrsCoordinateMetadata(ctx.staticCrsCoordinateMetadata()!!)
        )

        ctx.dynamicCrsCoordinateMetadata() != null -> CoordinateMetadata.DynamicCoordinateMetadata(
            visitDynamicCrsCoordinateMetadata(ctx.dynamicCrsCoordinateMetadata()!!),
            visitCoordinateEpoch(ctx.coordinateEpoch()!!).value
        )

        else -> throw IllegalArgumentException("Coordinate metadata cannot be null")
    }

    override fun visitCoordinateEpoch(ctx: WKTCRSParser.CoordinateEpochContext) = visitNumber(ctx.number())

    override fun visitSingleCrs(ctx: WKTCRSParser.SingleCrsContext): SingleCoordinateReferenceSystem = when {
        ctx.geodeticCrs() != null -> visitGeodeticCrs(ctx.geodeticCrs()!!)
        ctx.derivedGeodeticCrs() != null -> visitDerivedGeodeticCrs(ctx.derivedGeodeticCrs()!!)
        ctx.projectedCrs() != null -> visitProjectedCrs(ctx.projectedCrs()!!)
        ctx.derivedProjectedCrs() != null -> visitDerivedProjectedCrs(ctx.derivedProjectedCrs()!!)
        ctx.verticalCrs() != null -> visitVerticalCrs(ctx.verticalCrs()!!)
        ctx.derivedVerticalCrs() != null -> visitDerivedVerticalCrs(ctx.derivedVerticalCrs()!!)
        ctx.engineeringCrs() != null -> visitEngineeringCrs(ctx.engineeringCrs()!!)
        ctx.derivedEngineeringCrs() != null -> visitDerivedEngineeringCrs(ctx.derivedEngineeringCrs()!!)
        ctx.parametricCrs() != null -> visitParametricCrs(ctx.parametricCrs()!!)
        ctx.derivedParametricCrs() != null -> visitDerivedParametricCrs(ctx.derivedParametricCrs()!!)
        ctx.temporalCrs() != null -> visitTemporalCrs(ctx.temporalCrs()!!)
        ctx.derivedTemporalCrs() != null -> visitDerivedTemporalCrs(ctx.derivedTemporalCrs()!!)
        else -> throw IllegalArgumentException("Visitor method not implemented or rule produced no result.")
    }

    override fun visitCompoundCrs(ctx: WKTCRSParser.CompoundCrsContext) =
        visitScopeExtentIdentifierRemark(ctx.scopeExtentIdentifierRemark()).let {
            CompoundCrs(
                name = ctx.quotedText().computed,
                systems = ctx.singleCrs().map { visitSingleCrs(it) },
                usages = it.usages,
                identifiers = it.identifiers,
                remark = it.remark
            )
        }

    override fun visitBaseTemporalCrs(ctx: WKTCRSParser.BaseTemporalCrsContext) =
        BaseTemporalCrs(
            name = ctx.quotedText().computed,
            datum = visitTemporalDatum(ctx.temporalDatum()),
            identifiers = ctx.id().computed
        )

    override fun visitDerivedTemporalCrs(ctx: WKTCRSParser.DerivedTemporalCrsContext) =
        visitScopeExtentIdentifierRemark(ctx.scopeExtentIdentifierRemark()).let { proxy ->
            DerivedTemporalCrs(
                name = ctx.quotedText().computed,
                base = visitBaseTemporalCrs(ctx.baseTemporalCrs()),
                derivingConversion = visitDerivingConversion(ctx.derivingConversion()),
                cs = visitCs(ctx.cs()),
                usages = proxy.usages,
                identifiers = proxy.identifiers,
                remark = proxy.remark
            )
        }

    override fun visitBaseParametricCrs(ctx: WKTCRSParser.BaseParametricCrsContext) =
        BaseParametricCrs(
            name = ctx.quotedText().computed,
            datum = visitParametricDatum(ctx.parametricDatum()),
            identifiers = ctx.id().computed
        )

    override fun visitDerivedParametricCrs(ctx: WKTCRSParser.DerivedParametricCrsContext) =
        visitScopeExtentIdentifierRemark(ctx.scopeExtentIdentifierRemark()).let { proxy ->
            DerivedParametricCrs(
                name = ctx.quotedText().computed,
                base = visitBaseParametricCrs(ctx.baseParametricCrs()),
                derivingConversion = visitDerivingConversion(ctx.derivingConversion()),
                cs = visitCs(ctx.cs()),
                usages = proxy.usages,
                identifiers = proxy.identifiers,
                remark = proxy.remark
            )
        }

    override fun visitBaseEngeneeringCrs(ctx: WKTCRSParser.BaseEngeneeringCrsContext) =
        BaseEngineeringCrs(
            name = ctx.quotedText().computed,
            datum = visitEngineeringDatum(ctx.engineeringDatum()),
            identifiers = ctx.id().computed
        )

    override fun visitDerivedEngineeringCrs(ctx: WKTCRSParser.DerivedEngineeringCrsContext) =
        visitScopeExtentIdentifierRemark(ctx.scopeExtentIdentifierRemark()).let { proxy ->
            DerivedEngineeringCrs(
                name = ctx.quotedText().computed,
                base = visitBaseEngeneeringCrs(ctx.baseEngeneeringCrs()),
                derivingConversion = visitDerivingConversion(ctx.derivingConversion()),
                cs = visitCs(ctx.cs()),
                usages = proxy.usages,
                identifiers = proxy.identifiers,
                remark = proxy.remark
            )
        }

    override fun visitStaticBaseVerticalCrs(ctx: WKTCRSParser.StaticBaseVerticalCrsContext) =
        StaticBaseVerticalCrs(
            name = ctx.quotedText().computed,
            constraints = when {
                ctx.verticalReferenceFrame() != null -> visitVerticalReferenceFrame(ctx.verticalReferenceFrame()!!)
                ctx.verticalDatumEnsemble() != null -> visitVerticalDatumEnsemble(ctx.verticalDatumEnsemble()!!)
                else -> throw IllegalArgumentException("Wrong value")
            },
            identifiers = ctx.id().computed
        )

    override fun visitBaseVerticalCrs(ctx: WKTCRSParser.BaseVerticalCrsContext): BaseVerticalCrs =
        visitStaticBaseVerticalCrs(ctx.staticBaseVerticalCrs())

    override fun visitDerivedVerticalCrs(ctx: WKTCRSParser.DerivedVerticalCrsContext) =
        visitScopeExtentIdentifierRemark(ctx.scopeExtentIdentifierRemark()).let { proxy ->
            DerivedVerticalCrs(
                name = ctx.quotedText().computed,
                base = visitBaseVerticalCrs(ctx.baseVerticalCrs()),
                derivingConversion = visitDerivingConversion(ctx.derivingConversion()),
                coordinateSystem = visitCs(ctx.cs()),
                usages = proxy.usages,
                identifiers = proxy.identifiers,
                remark = proxy.remark
            )
        }

    override fun visitBaseProjectedCrs(ctx: WKTCRSParser.BaseProjectedCrsContext) =
        BaseProjectedCrs(
            name = ctx.quotedText().computed,
            base = visitBaseGeodeticCrs(ctx.baseGeodeticCrs()),
            projection = visitMapProjection(ctx.mapProjection()),
            identifier = ctx.id()?.computed
        )

    override fun visitDerivedProjectedCrs(ctx: WKTCRSParser.DerivedProjectedCrsContext) =
        visitScopeExtentIdentifierRemark(ctx.scopeExtentIdentifierRemark()).let { proxy ->
            DerivedProjectedCrs(
                name = ctx.quotedText().computed,
                base = visitBaseProjectedCrs(ctx.baseProjectedCrs()),
                derivingConversion = visitDerivingConversion(ctx.derivingConversion()),
                coordinateSystem = visitCs(ctx.cs()),
                usages = proxy.usages,
                identifiers = proxy.identifiers,
                remark = proxy.remark
            )
        }

    override fun visitStaticDerivedGeographicCrs(ctx: WKTCRSParser.StaticDerivedGeographicCrsContext) =
        visitScopeExtentIdentifierRemark(ctx.scopeExtentIdentifierRemark()).let { proxy ->
            StaticDerivedGeographicCrs(
                name = ctx.quotedText().computed,
                system = when {
                    ctx.baseStaticGeodeticCrs() != null -> visitBaseStaticGeodeticCrs(ctx.baseStaticGeodeticCrs()!!)
                    ctx.baseStaticGeographicCrs() != null -> visitBaseStaticGeographicCrs(ctx.baseStaticGeographicCrs()!!)
                    else -> throw IllegalArgumentException("Wrong Value")
                },
                derivingConversion = visitDerivingConversion(ctx.derivingConversion()),
                coordinateSystem = visitCs(ctx.cs()),
                usages = proxy.usages,
                identifiers = proxy.identifiers,
                remark = proxy.remark
            )
        }

    override fun visitStaticDerivedGeodeticCrs(ctx: WKTCRSParser.StaticDerivedGeodeticCrsContext) =
        visitScopeExtentIdentifierRemark(ctx.scopeExtentIdentifierRemark()).let { proxy ->
            StaticDerivedGeodeticCrs(
                name = ctx.quotedText().computed,
                system = when {
                    ctx.baseStaticGeodeticCrs() != null -> visitBaseStaticGeodeticCrs(ctx.baseStaticGeodeticCrs()!!)
                    ctx.baseStaticGeographicCrs() != null -> visitBaseStaticGeographicCrs(ctx.baseStaticGeographicCrs()!!)
                    else -> throw IllegalArgumentException("Wrong Value")
                },
                derivingConversion = visitDerivingConversion(ctx.derivingConversion()),
                coordinateSystem = visitCs(ctx.cs()),
                usages = proxy.usages,
                identifiers = proxy.identifiers,
                remark = proxy.remark
            )
        }

    override fun visitDerivedGeographicCrs(ctx: WKTCRSParser.DerivedGeographicCrsContext): DerivedGeographicCoordinateReferenceSystem =
        visitStaticDerivedGeographicCrs(ctx.staticDerivedGeographicCrs())

    override fun visitDerivedGeodeticCrs(ctx: WKTCRSParser.DerivedGeodeticCrsContext): DerivedGeodeticCoordinateReferenceSystem =
        when {
            ctx.staticDerivedGeodeticCrs() != null -> visitStaticDerivedGeodeticCrs(ctx.staticDerivedGeodeticCrs()!!)
            ctx.derivedGeographicCrs() != null -> visitDerivedGeographicCrs(ctx.derivedGeographicCrs()!!)
            else -> throw IllegalArgumentException("Visitor method not implemented or rule produced no result.")
        }

    override fun visitDerivingConversion(ctx: WKTCRSParser.DerivingConversionContext): DerivingConversion {
        val p = ctx.operationParameter().map { visitOperationParameter(it) }
        val pf = ctx.operationParameterFile().map { visitOperationParameterFile(it) }
        return DerivingConversion(
            name = ctx.quotedText().computed,
            method = visitOperationMethod(ctx.operationMethod()),
            parameters = p + pf,
            identifiers = ctx.id().computed
        )
    }

    @OptIn(ExperimentalTime::class)
    override fun visitTemporalOrigin(ctx: WKTCRSParser.TemporalOriginContext) = when {
        ctx.dateTime() != null -> TemporalOrigin.TemporalDateTimeOrigin(
            visitDateTime(ctx.dateTime()!!).value
        )

        ctx.quotedText() != null -> TemporalOrigin.TemporalStringOrigin(
            ctx.quotedText()!!.computed
        )

        else -> throw IllegalArgumentException("Temporal origin is not defined")
    }

    override fun visitCalendar(ctx: WKTCRSParser.CalendarContext) = visitQuotedText(ctx.quotedText())

    override fun visitTemporalDatum(ctx: WKTCRSParser.TemporalDatumContext) =
        TemporalDatum(
            name = ctx.quotedText().computed,
            calendar = ctx.calendar()?.let { visitCalendar(it).text },
            origin = ctx.temporalOrigin()?.let { visitTemporalOrigin(it) },
            identifiers = ctx.id().computed
        )

    override fun visitTemporalCrs(ctx: WKTCRSParser.TemporalCrsContext) =
        visitScopeExtentIdentifierRemark(ctx.scopeExtentIdentifierRemark()).let { proxy ->
            TemporalCrs(
                name = ctx.quotedText().computed,
                datum = visitTemporalDatum(ctx.temporalDatum()),
                coordinateSystem = visitCs(ctx.cs()),
                usages = proxy.usages,
                identifiers = proxy.identifiers,
                remark = proxy.remark
            )
        }

    override fun visitParametricDatum(ctx: WKTCRSParser.ParametricDatumContext) =
        ParametricDatum(
            name = ctx.quotedText().computed,
            anchor = ctx.datumAnchor()?.let { visitDatumAnchor(it).text },
            identifiers = ctx.id().computed
        )

    override fun visitParametricCrs(ctx: WKTCRSParser.ParametricCrsContext) =
        visitScopeExtentIdentifierRemark(ctx.scopeExtentIdentifierRemark()).let { proxy ->
            ParametricCrs(
                name = ctx.quotedText().computed,
                datum = visitParametricDatum(ctx.parametricDatum()),
                coordinateSystem = visitCs(ctx.cs()),
                usages = proxy.usages,
                identifiers = proxy.identifiers,
                remark = proxy.remark
            )
        }

    override fun visitEngineeringDatum(ctx: WKTCRSParser.EngineeringDatumContext) =
        EngineeringDatum(
            name = ctx.quotedText().computed,
            anchor = ctx.datumAnchor()?.let { visitDatumAnchor(it).text },
            identifiers = ctx.id().computed
        )

    override fun visitEngineeringCrs(ctx: WKTCRSParser.EngineeringCrsContext) =
        visitScopeExtentIdentifierRemark(ctx.scopeExtentIdentifierRemark()).let { proxy ->
            EngineeringCrs(
                name = ctx.quotedText().computed,
                datum = visitEngineeringDatum(ctx.engineeringDatum()),
                coordinateSystem = visitCs(ctx.cs()),
                usages = proxy.usages,
                identifiers = proxy.identifiers,
                remark = proxy.remark
            )
        }

    override fun visitVerticalReferenceFrame(ctx: WKTCRSParser.VerticalReferenceFrameContext) =
        VerticalReferenceFrame(
            name = ctx.quotedText().computed,
            anchor = ctx.datumAnchor()?.let { visitDatumAnchor(it).text },
            epoch = ctx.datumAnchorEpoch()?.let { visitDatumAnchorEpoch(it).value },
            identifiers = ctx.id().computed
        )

    override fun visitGeoidModelId(ctx: WKTCRSParser.GeoidModelIdContext) =
        GeoidModelId(
            name = ctx.quotedText().computed,
            identifier = ctx.id()?.computed
        )

    override fun visitStaticVerticalCrs(ctx: WKTCRSParser.StaticVerticalCrsContext) =
        visitScopeExtentIdentifierRemark(ctx.scopeExtentIdentifierRemark()).let { proxy ->
            StaticVerticalCrs(
                name = ctx.quotedText().computed,
                constraints = when {
                    ctx.verticalReferenceFrame() != null -> visitVerticalReferenceFrame(ctx.verticalReferenceFrame()!!)
                    ctx.verticalDatumEnsemble() != null -> visitVerticalDatumEnsemble(ctx.verticalDatumEnsemble()!!)
                    else -> throw IllegalArgumentException("Wrong value")
                },
                coordinateSystem = visitCs(ctx.cs()),
                geoidModelIds = ctx.geoidModelId().map { visitGeoidModelId(it) },
                usages = proxy.usages,
                identifiers = proxy.identifiers,
                remark = proxy.remark
            )
        }

    override fun visitVerticalCrs(ctx: WKTCRSParser.VerticalCrsContext) =
        visitStaticVerticalCrs(ctx.staticVerticalCrs())

    override fun visitMapProjectionParameterUnit(ctx: WKTCRSParser.MapProjectionParameterUnitContext): MapProjectionParameterUnit =
        when {
            ctx.lengthUnit() != null -> visitLengthUnit(ctx.lengthUnit()!!)
            ctx.angleUnit() != null -> visitAngleUnit(ctx.angleUnit()!!)
            ctx.scaleUnit() != null -> visitScaleUnit(ctx.scaleUnit()!!)
            else -> throw IllegalArgumentException("Visitor method not implemented or rule produced no result.")
        }

    override fun visitMapProjectionParameter(ctx: WKTCRSParser.MapProjectionParameterContext) =
        MapProjectionParameter(
            name = ctx.quotedText().computed,
            value = ctx.number().computed,
            unit = ctx.mapProjectionParameterUnit()?.let { visitMapProjectionParameterUnit(it) },
            identifiers = ctx.id().computed
        )

    override fun visitMapProjectionMethod(ctx: WKTCRSParser.MapProjectionMethodContext) =
        MapProjectionMethod(
            name = ctx.quotedText().computed,
            identifiers = ctx.id().computed
        )

    override fun visitMapProjection(ctx: WKTCRSParser.MapProjectionContext) =
        MapProjection(
            name = ctx.quotedText().computed,
            projectionMethod = visitMapProjectionMethod(ctx.mapProjectionMethod()),
            parameters = ctx.mapProjectionParameter().map { visitMapProjectionParameter(it) },
            identifiers = ctx.id().computed
        )

    override fun visitBaseStaticGeographicCrs(ctx: WKTCRSParser.BaseStaticGeographicCrsContext) =
        BaseStaticGeographicCrs(
            name = ctx.quotedText().computed,
            datumEnsemble = when {
                ctx.geodeticReferenceFrame() != null -> visitGeodeticReferenceFrame(ctx.geodeticReferenceFrame()!!)
                ctx.geodeticDatumEnsemble() != null -> visitGeodeticDatumEnsemble(ctx.geodeticDatumEnsemble()!!)
                else -> throw IllegalArgumentException("Geodetic Constraints is not defined")
            },
            unit = ctx.angleUnit()?.let { visitAngleUnit(it) },
            identifiers = ctx.id().computed
        )

    override fun visitBaseStaticGeodeticCrs(ctx: WKTCRSParser.BaseStaticGeodeticCrsContext) =
        BaseStaticGeodeticCrs(
            name = ctx.quotedText().computed,
            constraints = when {
                ctx.geodeticReferenceFrame() != null -> visitGeodeticReferenceFrame(ctx.geodeticReferenceFrame()!!)
                ctx.geodeticDatumEnsemble() != null -> visitGeodeticDatumEnsemble(ctx.geodeticDatumEnsemble()!!)
                else -> throw IllegalArgumentException("Geodetic Constraints is not defined")
            },
            unit = ctx.angleUnit()?.let { visitAngleUnit(it) },
            identifiers = ctx.id().computed
        )

    override fun visitBaseGeodeticCrs(ctx: WKTCRSParser.BaseGeodeticCrsContext): BaseGeodeticCoordinateReferenceSystem =
        when {
            ctx.baseStaticGeodeticCrs() != null -> visitBaseStaticGeodeticCrs(ctx.baseStaticGeodeticCrs()!!)
            ctx.baseStaticGeographicCrs() != null -> visitBaseStaticGeographicCrs(ctx.baseStaticGeographicCrs()!!)
            else -> throw IllegalArgumentException("Visitor method not implemented or rule produced no result.")
        }

    override fun visitProjectedCrs(ctx: WKTCRSParser.ProjectedCrsContext) =
        visitScopeExtentIdentifierRemark(ctx.scopeExtentIdentifierRemark()).let { proxy ->
            ProjectedCrs(
                name = ctx.quotedText().computed,
                base = visitBaseGeodeticCrs(ctx.baseGeodeticCrs()),
                projection = visitMapProjection(ctx.mapProjection()),
                coordinateSystem = visitCs(ctx.cs()),
                usages = proxy.usages,
                identifiers = proxy.identifiers,
                remark = proxy.remark
            )
        }

    override fun visitDatumAnchorEpoch(ctx: WKTCRSParser.DatumAnchorEpochContext) = visitNumber(ctx.number())

    override fun visitDatumAnchor(ctx: WKTCRSParser.DatumAnchorContext) = visitQuotedText(ctx.quotedText())


    override fun visitGeodeticReferenceFrame(ctx: WKTCRSParser.GeodeticReferenceFrameContext) =
        GeodeticReferenceFrame(
            name = ctx.quotedText().computed,
            ellipsoid = visitEllipsoid(ctx.ellipsoid()),
            anchor = ctx.datumAnchor()?.let { visitDatumAnchor(it).text },
            anchorEpoch = ctx.datumAnchorEpoch()?.let { visitDatumAnchorEpoch(it).value },
            identifiers = ctx.id().computed,
            primeMeridian = ctx.primeMeridian()?.let { visitPrimeMeridian(it) },
        )

    override fun visitIrmLongitude(ctx: WKTCRSParser.IrmLongitudeContext) =
        IrmLongitude(
            longitude = ctx.number().computed,
            unit = ctx.angleUnit()?.let { visitAngleUnit(it) }
        )

    override fun visitPrimeMeridian(ctx: WKTCRSParser.PrimeMeridianContext) =
        PrimeMeridian(
            name = ctx.quotedText().computed,
            irmLongitude = visitIrmLongitude(ctx.irmLongitude()),
            identifiers = ctx.id().computed
        )

    override fun visitEllipsoid(ctx: WKTCRSParser.EllipsoidContext) =
        Ellipsoid(
            name = ctx.quotedText().computed,
            semiMajorAxis = ctx.number(0)!!.computed,
            inverseFlattening = ctx.number(1)!!.computed,
            unit = ctx.lengthUnit()?.let { visitLengthUnit(it) },
            identifiers = ctx.id().computed
        )

    override fun visitDefiningTransformationID(ctx: WKTCRSParser.DefiningTransformationIDContext) =
        DefiningTransformationID(
            name = ctx.quotedText().computed,
            identifier = ctx.id()?.computed
        )

    override fun visitStaticGeographicCrs(ctx: WKTCRSParser.StaticGeographicCrsContext) =
        visitScopeExtentIdentifierRemark(ctx.scopeExtentIdentifierRemark()).let { proxy ->
            StaticGeographicCrs(
                name = ctx.quotedText().computed,
                system = when {
                    ctx.geodeticDatumEnsemble() != null -> visitGeodeticDatumEnsemble(ctx.geodeticDatumEnsemble()!!)
                    ctx.geodeticReferenceFrame() != null -> visitGeodeticReferenceFrame(ctx.geodeticReferenceFrame()!!)
                    else -> throw NotImplementedError("The rule does not know what to do with this Tree")
                },
                coordinateSystem = visitCs(ctx.cs()),
                definingTransformationIDs = ctx.definingTransformationID().map { visitDefiningTransformationID(it) },
                usages = proxy.usages,
                identifiers = proxy.identifiers,
                remark = proxy.remark,
            )
        }

    override fun visitStaticGeodeticCrs(ctx: WKTCRSParser.StaticGeodeticCrsContext) =
        visitScopeExtentIdentifierRemark(ctx.scopeExtentIdentifierRemark()).let { proxy ->
            StaticGeodeticCrs(
                name = ctx.quotedText().computed,
                system = when {
                    ctx.geodeticDatumEnsemble() != null -> visitGeodeticDatumEnsemble(ctx.geodeticDatumEnsemble()!!)
                    ctx.geodeticReferenceFrame() != null -> visitGeodeticReferenceFrame(ctx.geodeticReferenceFrame()!!)
                    else -> throw NotImplementedError("The rule does not know what to do with this Tree")
                },
                coordinateSystem = visitCs(ctx.cs()),
                definingTransformationIDs = ctx.definingTransformationID().map { visitDefiningTransformationID(it) },
                usages = proxy.usages,
                identifiers = proxy.identifiers,
                remark = proxy.remark,
            )
        }

    override fun visitGeographicCrs(ctx: WKTCRSParser.GeographicCrsContext): GeographicCoordinateReferenceSystem =
        visitStaticGeographicCrs(ctx.staticGeographicCrs())

    override fun visitGeodeticCrs(ctx: WKTCRSParser.GeodeticCrsContext): GeodeticCoordinateReferenceSystem = when {
        ctx.geographicCrs() != null -> visitGeographicCrs(ctx.geographicCrs()!!)
        ctx.staticGeodeticCrs() != null -> visitStaticGeodeticCrs(ctx.staticGeodeticCrs()!!)
        else -> throw IllegalArgumentException("Visitor method not implemented or rule produced no result.")
    }

    override fun visitDatumEnsembleAccuracy(ctx: WKTCRSParser.DatumEnsembleAccuracyContext) = visitNumber(ctx.number())

    override fun visitDatumEnsembleMember(ctx: WKTCRSParser.DatumEnsembleMemberContext) =
        DatumEnsembleMember(
            name = ctx.quotedText().computed,
            identifiers = ctx.id().computed
        )

    override fun visitVerticalDatumEnsemble(ctx: WKTCRSParser.VerticalDatumEnsembleContext) =
        VerticalDatumEnsemble(
            name = ctx.quotedText().computed,
            members = ctx.datumEnsembleMember().map { visitDatumEnsembleMember(it) },
            accuracy = visitDatumEnsembleAccuracy(ctx.datumEnsembleAccuracy()).value,
            identifiers = ctx.id().computed
        )

    override fun visitGeodeticDatumEnsemble(ctx: WKTCRSParser.GeodeticDatumEnsembleContext) =
        GeodeticDatumEnsemble(
            name = ctx.quotedText().computed,
            members = ctx.datumEnsembleMember().map { visitDatumEnsembleMember(it) },
            ellipsoid = visitEllipsoid(ctx.ellipsoid()),
            accuracy = visitDatumEnsembleAccuracy(ctx.datumEnsembleAccuracy()).value,
            identifiers = ctx.id().computed,
            primeMeridian = ctx.primeMeridian()?.let { visitPrimeMeridian(it) },
        )

    override fun visitDatumEnsemble(ctx: WKTCRSParser.DatumEnsembleContext): DatumEnsemble = when {
        ctx.geodeticDatumEnsemble() != null -> visitGeodeticDatumEnsemble(ctx.geodeticDatumEnsemble()!!)
        ctx.verticalDatumEnsemble() != null -> visitVerticalDatumEnsemble(ctx.verticalDatumEnsemble()!!)
        else -> throw IllegalArgumentException("Visitor method not implemented or rule produced no result.")
    }

    override fun visitAxisOrder(ctx: WKTCRSParser.AxisOrderContext) =
        ExpressionWrapper(ctx.INTEGER().text.toInt())

    override fun visitBearing(ctx: WKTCRSParser.BearingContext) = visitNumber(ctx.number())

    override fun visitMeridian(ctx: WKTCRSParser.MeridianContext) =
        Meridian(
            number = ctx.number().computed,
            unit = visitAngleUnit(ctx.angleUnit())
        )

    override fun visitAxisDirection(ctx: WKTCRSParser.AxisDirectionContext): AxisDirection {
        return when {
            ctx.NORTH() != null -> AxisDirection.NORTH(
                meridian = ctx.meridian()?.let { visitMeridian(it) })

            ctx.NORTHNORTHEAST() != null -> AxisDirection.NORTHNORTHEAST
            ctx.NORTHEAST() != null -> AxisDirection.NORTHEAST
            ctx.EASTNORTHEAST() != null -> AxisDirection.EASTNORTHEAST
            ctx.EAST() != null -> AxisDirection.EAST
            ctx.EASTSOUTHEAST() != null -> AxisDirection.EASTSOUTHEAST
            ctx.SOUTHEAST() != null -> AxisDirection.SOUTHEAST
            ctx.SOUTHSOUTHEAST() != null -> AxisDirection.SOUTHSOUTHEAST
            ctx.SOUTH() != null -> AxisDirection.SOUTH(
                meridian = ctx.meridian()?.let { visitMeridian(it) })

            ctx.SOUTHSOUTHWEST() != null -> AxisDirection.SOUTHSOUTHWEST
            ctx.SOUTHWEST() != null -> AxisDirection.SOUTHWEST
            ctx.WESTSOUTHWEST() != null -> AxisDirection.WESTSOUTHWEST
            ctx.WEST() != null -> AxisDirection.WEST
            ctx.WESTNORTHWEST() != null -> AxisDirection.WESTNORTHWEST
            ctx.NORTHWEST() != null -> AxisDirection.NORTHWEST
            ctx.NORTHNORTHWEST() != null -> AxisDirection.NORTHNORTHWEST
            ctx.GEOCENTRICX() != null -> AxisDirection.GEOCENTRICX
            ctx.GEOCENTRICY() != null -> AxisDirection.GEOCENTRICY
            ctx.GEOCENTRICZ() != null -> AxisDirection.GEOCENTRICZ
            ctx.UP() != null -> AxisDirection.UP
            ctx.DOWN() != null -> AxisDirection.DOWN
            ctx.FORWARD() != null -> AxisDirection.FORWARD
            ctx.AFT() != null -> AxisDirection.AFT
            ctx.PORT() != null -> AxisDirection.PORT
            ctx.STARBOARD() != null -> AxisDirection.STARBOARD
            ctx.CLOCKWISE() != null -> AxisDirection.CLOCKWISE(
                visitBearing(ctx.bearing()!!).value
            )

            ctx.COUNTERCLOCKWISE() != null -> AxisDirection.COUNTERCLOCKWISE(
                visitBearing(ctx.bearing()!!).value
            )

            ctx.COLUMNPOSITIVE() != null -> AxisDirection.COLUMNPOSITIVE
            ctx.COLUMNNEGATIVE() != null -> AxisDirection.COLUMNNEGATIVE
            ctx.ROWPOSITIVE() != null -> AxisDirection.ROWPOSITIVE
            ctx.ROWNEGATIVE() != null -> AxisDirection.ROWNEGATIVE
            ctx.DISPLAYRIGHT() != null -> AxisDirection.DISPLAYRIGHT
            ctx.DISPLAYLEFT() != null -> AxisDirection.DISPLAYLEFT
            ctx.DISPLAYUP() != null -> AxisDirection.DISPLAYUP
            ctx.DISPLAYDOWN() != null -> AxisDirection.DISPLAYDOWN
            ctx.FUTURE() != null -> AxisDirection.FUTURE
            ctx.PAST() != null -> AxisDirection.PAST
            ctx.TOWARDS() != null -> AxisDirection.TOWARDS
            ctx.AWAYFROM() != null -> AxisDirection.AWAYFROM
            ctx.UNSPECIFIED() != null -> AxisDirection.UNSPECIFIED
            else -> throw IllegalArgumentException("Axis Direction not implemented yet")
        }
    }

    override fun visitOrdinalDateTimeAxis(ctx: WKTCRSParser.OrdinalDateTimeAxisContext) =
        OrdinalDateTimeAxis(
            name = ctx.quotedText().computed,
            direction = visitAxisDirection(ctx.axisDirection()),
            order = ctx.axisOrder()?.let { visitAxisOrder(it).value },
            range = ctx.axisRange()?.let { visitAxisRange(it) },
            identifiers = ctx.id().computed
        )

    override fun visitTemporalCountMeasureAxis(ctx: WKTCRSParser.TemporalCountMeasureAxisContext) =
        TemporalCountMeasureAxis(
            name = ctx.quotedText().computed,
            direction = visitAxisDirection(ctx.axisDirection()),
            order = ctx.axisOrder()?.let { visitAxisOrder(it).value },
            unit = ctx.timeUnit()?.let { visitTimeUnit(it) },
            range = ctx.axisRange()?.let { visitAxisRange(it) },
            identifiers = ctx.id().computed
        )

    override fun visitSpatialAxis(ctx: WKTCRSParser.SpatialAxisContext) =
        SpatialAxis(
            name = ctx.quotedText().computed,
            identifiers = ctx.id().computed,
            direction = visitAxisDirection(ctx.axisDirection()),
            order = ctx.axisOrder()?.let { visitAxisOrder(it).value },
            unit = ctx.spatialUnit()?.let { visitSpatialUnit(it) },
            range = ctx.axisRange()?.let { visitAxisRange(it) },
        )

    override fun visitOrdinalDateTimeCSType(ctx: WKTCRSParser.OrdinalDateTimeCSTypeContext) = when {
        ctx.ORDINAL() != null -> OrdinalDateTimeCSType.ORDINAL
        ctx.TEMPORALDATETIME() != null -> OrdinalDateTimeCSType.TEMPORALDATETIME
        else -> throw IllegalArgumentException("OrdinalDateTimeCSType ${ctx.text} not supported")
    }

    override fun visitOrdinalDateTimeCS(ctx: WKTCRSParser.OrdinalDateTimeCSContext) =
        OrdinalDateTimeCS(
            type = visitOrdinalDateTimeCSType(ctx.ordinalDateTimeCSType()),
            dimension = visitDimension(ctx.dimension()),
            identifiers = ctx.id().computed,
            axis = ctx.ordinalDateTimeAxis().map { visitOrdinalDateTimeAxis(it) }
        )

    override fun visitTemporalCountMeasureCSType(ctx: WKTCRSParser.TemporalCountMeasureCSTypeContext) = when {
        ctx.TEMPORALCOUNT() != null -> TemporalCountMeasureCSType.TEMPORALCOUNT
        ctx.TEMPORALMEASURE() != null -> TemporalCountMeasureCSType.TEMPORALMEASURE
        else -> throw IllegalArgumentException("Temporal Count Measure CS type ${ctx.text} not supported")
    }

    override fun visitTemporalCountMeasureCS(ctx: WKTCRSParser.TemporalCountMeasureCSContext) =
        TemporalCountMeasureCS(
            type = visitTemporalCountMeasureCSType(ctx.temporalCountMeasureCSType()),
            dimension = visitDimension(ctx.dimension()),
            identifiers = ctx.id().computed,
            axis = visitTemporalCountMeasureAxis(ctx.temporalCountMeasureAxis())
        )

    override fun visitDimension(ctx: WKTCRSParser.DimensionContext) = ctx.number().computed.let {
        when (it) {
            1.0 -> Dimension.D1
            2.0 -> Dimension.D2
            3.0 -> Dimension.D3
            else -> throw IllegalArgumentException("Dimension ${ctx.text} not supported")
        }
    }

    override fun visitSpatialCSType(ctx: WKTCRSParser.SpatialCSTypeContext) = when {
        ctx.AFFINE() != null -> SpatialCSType.AFFINE
        ctx.CARTESIAN() != null -> SpatialCSType.CARTESIAN
        ctx.CYLINDRICAL() != null -> SpatialCSType.CYLINDRICAL
        ctx.ELLIPSOIDAL() != null -> SpatialCSType.ELLIPSOIDAL
        ctx.LINEAR() != null -> SpatialCSType.LINEAR
        ctx.PARAMETRIC() != null -> SpatialCSType.PARAMETRIC
        ctx.POLAR() != null -> SpatialCSType.POLAR
        ctx.SPHERICAL() != null -> SpatialCSType.SPHERICAL
        ctx.VERTICAL() != null -> SpatialCSType.VERTICAL
        else -> throw IllegalArgumentException("SpatialCSType ${ctx.text} not supported")
    }

    override fun visitSpatialCS(ctx: WKTCRSParser.SpatialCSContext) =
        SpatialCS(
            type = visitSpatialCSType(ctx.spatialCSType()),
            dimension = visitDimension(ctx.dimension()),
            identifiers = ctx.id().computed,
            spatialAxis = ctx.spatialAxis().map { visitSpatialAxis(it) },
            unit = ctx.unit()?.let { visitUnit(it) },
        )

    override fun visitCs(ctx: WKTCRSParser.CsContext): CoordinateSystem = when {
        ctx.spatialCS() != null -> visitSpatialCS(ctx.spatialCS()!!)
        ctx.temporalCountMeasureCS() != null -> visitTemporalCountMeasureCS(ctx.temporalCountMeasureCS()!!)
        ctx.ordinalDateTimeCS() != null -> visitOrdinalDateTimeCS(ctx.ordinalDateTimeCS()!!)
        else -> throw IllegalArgumentException("Visitor method not implemented or rule produced no result.")
    }

    override fun visitUnit(ctx: WKTCRSParser.UnitContext): WKTUnit = when {
        ctx.spatialUnit() != null -> visitSpatialUnit(ctx.spatialUnit()!!)
        ctx.timeUnit() != null -> visitTimeUnit(ctx.timeUnit()!!)
        else -> throw IllegalArgumentException("Visitor method not implemented or rule produced no result.")
    }

    override fun visitSpatialUnit(ctx: WKTCRSParser.SpatialUnitContext): SpatialUnit = when {
        ctx.angleUnit() != null -> visitAngleUnit(ctx.angleUnit()!!)
        ctx.lengthUnit() != null -> visitLengthUnit(ctx.lengthUnit()!!)
        ctx.parametricUnit() != null -> visitParametricUnit(ctx.parametricUnit()!!)
        ctx.scaleUnit() != null -> visitScaleUnit(ctx.scaleUnit()!!)
        else -> throw IllegalArgumentException("Visitor method not implemented or rule produced no result.")
    }

    override fun visitTimeUnit(ctx: WKTCRSParser.TimeUnitContext) =
        TimeUnit(
            unitName = ctx.quotedText().computed,
            conversionFactor = ctx.number().computed,
            identifiers = ctx.id().computed
        )

    override fun visitParametricUnit(ctx: WKTCRSParser.ParametricUnitContext) =
        ParametricUnit(
            unitName = ctx.quotedText().computed,
            conversionFactor = ctx.number().computed,
            identifiers = ctx.id().computed
        )

    override fun visitScaleUnit(ctx: WKTCRSParser.ScaleUnitContext) =
        ScaleUnit(
            unitName = ctx.quotedText().computed,
            conversionFactor = ctx.number().computed,
            identifiers = ctx.id().computed
        )

    override fun visitId(ctx: WKTCRSParser.IdContext) =
        Identifier(
            authorityName = ctx.quotedText().computed,
            uid = visitLiteral(ctx.literal(0)!!),
            version = ctx.literal(1)?.let { visitLiteral(it) },
            citation = ctx.citation()?.let { visitCitation(it).text },
            uri = ctx.uri()?.let { visitUri(it).text })

    override fun visitUsage(ctx: WKTCRSParser.UsageContext) =
        Usage(
            scope = visitScope(ctx.scope()).text, extent = visitExtent(ctx.extent())
        )

    override fun visitExtent(ctx: WKTCRSParser.ExtentContext) =
        Extent(
            area = ctx.area()?.let { visitArea(it) },
            bbox = ctx.bbox()?.let { visitBbox(it) },
            verticalExtent = ctx.verticalExtent()?.let { visitVerticalExtent(it) },
            temporalExtent = ctx.temporalExtent()?.let { visitTemporalExtent(it) },
        )

    @OptIn(ExperimentalTime::class)
    override fun visitTemporalExtent(ctx: WKTCRSParser.TemporalExtentContext) =
        TemporalExtent(
            start = ctx.dateTime(0)?.let { visitDateTime(it).value },
            end = ctx.dateTime(1)?.let { visitDateTime(it).value },
            startString = ctx.quotedText(0)?.computed,
            endString = ctx.quotedText(1)?.computed,
        )

    @OptIn(ExperimentalTime::class)
    override fun visitDateTime(ctx: WKTCRSParser.DateTimeContext) = when {
        ctx.calendarDateTime() != null -> visitCalendarDateTime(ctx.calendarDateTime()!!)
        ctx.ordinalDateTime() != null -> visitOrdinalDateTime(ctx.ordinalDateTime()!!)
        else -> throw NotImplementedError("Visitor method not implemented or rule produced no result.")
    }

    @OptIn(ExperimentalTime::class)
    override fun visitOrdinalDateTime(ctx: WKTCRSParser.OrdinalDateTimeContext): ExpressionWrapper<Instant> {
        val date = visitOrdinalDate(ctx.ordinalDate()).value
        val (time, zone) = ctx.clock24H()?.let { visitClock24H(it).value } ?: (LocalTime(0, 0) to UtcOffset.ZERO)
        return ExpressionWrapper(
            LocalDateTime(
                date,
                time
            ).toInstant(zone)
        )
    }

    override fun visitOrdinalDate(ctx: WKTCRSParser.OrdinalDateContext): ExpressionWrapper<LocalDate> {
        val format = LocalDate.Format {
            year()
            optional {
                char('-')
                dayOfYear()
            }
        }
        val date = format.parse(ctx.text)
        return ExpressionWrapper(date)
    }

    @OptIn(ExperimentalTime::class)
    override fun visitCalendarDateTime(ctx: WKTCRSParser.CalendarDateTimeContext): ExpressionWrapper<Instant> {
        val date = visitCalendarDate(ctx.calendarDate()).value
        val (time, zone) = ctx.clock24H()?.let { visitClock24H(it).value } ?: (LocalTime(0, 0) to UtcOffset.ZERO)
        return ExpressionWrapper(
            LocalDateTime(
                date,
                time
            ).toInstant(zone)
        )
    }

    override fun visitClock24H(ctx: WKTCRSParser.Clock24HContext): ExpressionWrapper<Pair<LocalTime, UtcOffset>> {
        val sec = ctx.second()?.let { visitSecond(it).value } ?: 0.0
        val nanoSecond = (sec - sec.toInt()) * 1_000_000_000
        val time = LocalTime(
            hour = visitHour(ctx.hour()).value,
            minute = ctx.minute()?.let { visitMinute(it).value } ?: 0,
            second = sec.toInt(),
            nanosecond = nanoSecond.toInt())
        val zone = visitTimeZone(ctx.timeZone()).value
        return ExpressionWrapper(time to zone)
    }

    override fun visitTimeZone(ctx: WKTCRSParser.TimeZoneContext) = when {
        ctx.utcTZ() != null -> visitUtcTZ(ctx.utcTZ()!!)
        ctx.localTZ() != null -> visitLocalTZ(ctx.localTZ()!!)
        else -> throw NotImplementedError("Visitor method not implemented or rule produced no result.")
    }

    override fun visitUtcTZ(ctx: WKTCRSParser.UtcTZContext) =
        ExpressionWrapper(TimeZone.UTC.offset)

    override fun visitLocalTZ(ctx: WKTCRSParser.LocalTZContext) =
        ExpressionWrapper(
            UtcOffset.Formats.ISO.parse(
                ctx.text
            )
        )

    override fun visitCalendarDate(ctx: WKTCRSParser.CalendarDateContext): ExpressionWrapper<LocalDate> =
        ExpressionWrapper(
            LocalDate(
                year = visitYear(ctx.year()).value,
                month = ctx.month()?.let { visitMonth(it).value } ?: 1,
                day = ctx.day()?.let { visitDay(it).value } ?: 1,
            ))

    override fun visitYear(ctx: WKTCRSParser.YearContext) =
        ExpressionWrapper(ctx.INTEGER().text.toInt())

    override fun visitMonth(ctx: WKTCRSParser.MonthContext) =
        ExpressionWrapper(ctx.INTEGER().text.toInt())

    override fun visitDay(ctx: WKTCRSParser.DayContext) =
        ExpressionWrapper(ctx.INTEGER().text.toInt())

    override fun visitHour(ctx: WKTCRSParser.HourContext) =
        ExpressionWrapper(ctx.INTEGER().text.toInt())

    override fun visitMinute(ctx: WKTCRSParser.MinuteContext) =
        ExpressionWrapper(ctx.INTEGER().text.toInt())

    override fun visitSecond(ctx: WKTCRSParser.SecondContext) = visitNumber(ctx.number())

    override fun visitScope(ctx: WKTCRSParser.ScopeContext) = visitQuotedText(ctx.quotedText())
    override fun visitCitation(ctx: WKTCRSParser.CitationContext) = visitQuotedText(ctx.quotedText())
    override fun visitUri(ctx: WKTCRSParser.UriContext) = visitQuotedText(ctx.quotedText())
    override fun visitArea(ctx: WKTCRSParser.AreaContext) =
        Area(ctx.quotedText().computed)

    override fun visitAxisRange(ctx: WKTCRSParser.AxisRangeContext) =
        AxisRange(
            min = ctx.axisMinimumValue()?.let { visitAxisMinimumValue(it).value },
            max = ctx.axisMaximumValue()?.let { visitAxisMaximumValue(it).value },
            meaning = ctx.axisRangeMeaning()?.let { visitAxisRangeMeaning(it) },
        )

    override fun visitVerticalExtent(ctx: WKTCRSParser.VerticalExtentContext) =
        VerticalExtent(
            minHeight = ctx.number(0)!!.computed,
            maxHeight = ctx.number(1)!!.computed,
            unit = ctx.lengthUnit()?.let { visitLengthUnit(it) })

    override fun visitLengthUnit(ctx: WKTCRSParser.LengthUnitContext) =
        LengthUnit(
            unitName = ctx.quotedText().computed,
            conversionFactor = ctx.number().computed,
            identifiers = ctx.id().computed
        )

    override fun visitAxisMinimumValue(ctx: WKTCRSParser.AxisMinimumValueContext) = visitNumber(ctx.number())
    override fun visitAxisMaximumValue(ctx: WKTCRSParser.AxisMaximumValueContext) = visitNumber(ctx.number())
    override fun visitAxisRangeMeaning(ctx: WKTCRSParser.AxisRangeMeaningContext) = when {
        ctx.EXACT() != null -> AxisRangeMeaning.EXACT
        ctx.WRAPAROUND() != null -> AxisRangeMeaning.WRAPAROUND
        else -> throw IllegalArgumentException("AxisRange meaning required.")
    }

    override fun visitBbox(ctx: WKTCRSParser.BboxContext) =
        Bbox(
            positions = ctx.number().map { it.computed }.toDoubleArray()
        )

    override fun visitBoundCrs(ctx: WKTCRSParser.BoundCrsContext) =
        visitScopeExtentIdentifierRemark(ctx.scopeExtentIdentifierRemark()).let {
            BoundCrs(
                source = visitSourceCrs(ctx.sourceCrs()),
                target = visitTargetCrs(ctx.targetCrs()),
                abridgedCoordinateTransformation = visitAbridgedCoordinateTransformation(ctx.abridgedCoordinateTransformation()),
                usages = it.usages,
                identifiers = it.identifiers,
                remark = it.remark
            )
        }

    override fun visitAbridgedCoordinateTransformation(ctx: WKTCRSParser.AbridgedCoordinateTransformationContext) =
        visitScopeExtentIdentifierRemark(ctx.scopeExtentIdentifierRemark()).let { proxy ->
            val atp = ctx.abridgedTransformationParameter().map { visitAbridgedTransformationParameter(it) }
            val opfile = ctx.operationParameterFile().map { visitOperationParameterFile(it) }
            AbridgedCoordinateTransformation(
                name = ctx.quotedText().computed,
                version = ctx.operationVersion()?.let { visitOperationVersion(it).text },
                method = visitOperationMethod(ctx.operationMethod()),
                parameter = atp + opfile,
                usages = proxy.usages,
                identifiers = proxy.identifiers,
                remark = proxy.remark
            )
        }

    override fun visitOperationParameter(ctx: WKTCRSParser.OperationParameterContext) =
        OperationParameter(
            name = ctx.quotedText().computed,
            value = ctx.number().computed,
            unit = visitParameterUnit(ctx.parameterUnit()),
            identifiers = ctx.id().computed
        )

    override fun visitParameterUnit(ctx: WKTCRSParser.ParameterUnitContext): ParameterUnit = when {
        ctx.lengthUnit() != null -> visitLengthUnit(ctx.lengthUnit()!!)
        ctx.angleUnit() != null -> visitAngleUnit(ctx.angleUnit()!!)
        ctx.scaleUnit() != null -> visitScaleUnit(ctx.scaleUnit()!!)
        ctx.timeUnit() != null -> visitTimeUnit(ctx.timeUnit()!!)
        ctx.parametricUnit() != null -> visitParametricUnit(ctx.parametricUnit()!!)
        else -> throw IllegalArgumentException("Visitor method not implemented or rule produced no result.")
    }

    override fun visitOperationParameterFile(ctx: WKTCRSParser.OperationParameterFileContext) =
        OperationParameterFile(
            name = ctx.quotedText(0)!!.computed,
            filename = ctx.quotedText(1)!!.computed,
            identifiers = ctx.id().computed
        )

    override fun visitAbridgedTransformationParameter(ctx: WKTCRSParser.AbridgedTransformationParameterContext) =
        AbridgedTransformationParameter(
            name = ctx.quotedText().computed, value = ctx.number().computed, identifiers = ctx.id().computed
        )

    override fun visitOperationMethod(ctx: WKTCRSParser.OperationMethodContext) =
        OperationMethod(
            name = ctx.quotedText().computed, identifiers = ctx.id().computed
        )

    override fun visitOperationVersion(ctx: WKTCRSParser.OperationVersionContext) = visitQuotedText(ctx.quotedText())

    override fun visitScopeExtentIdentifierRemark(ctx: WKTCRSParser.ScopeExtentIdentifierRemarkContext) =
        ScopeExtentIdentifierRemarkProxy(
            usages = ctx.usage().map { visitUsage(it) },
            identifiers = ctx.id().computed,
            remark = ctx.remark()?.let { visitRemark(it).text })

    override fun visitRemark(ctx: WKTCRSParser.RemarkContext) = visitQuotedText(ctx.quotedText())

    override fun visitSourceCrs(ctx: WKTCRSParser.SourceCrsContext) =
        visit(
            ctx.crs()
        ) as CoordinateReferenceSystem

    override fun visitTargetCrs(ctx: WKTCRSParser.TargetCrsContext) =
        visit(
            ctx.crs()
        ) as CoordinateReferenceSystem

    val WKTCRSParser.QuotedTextContext.computed
        get() = visitQuotedText(this).text
    val WKTCRSParser.NumberContext.computed
        get() = visitNumber(this).value
    val WKTCRSParser.IdContext.computed
        get() = visitId(this)
    val List<WKTCRSParser.IdContext>.computed
        get() = this.map { visitId(it) }

    // --- Basic literals ---
    override fun visitQuotedText(ctx: WKTCRSParser.QuotedTextContext) =
        TextLiteral(
            unquote(ctx.text)
        )

    override fun visitNumber(ctx: WKTCRSParser.NumberContext): NumberLiteral {
        val num = ctx.text.trim().toDoubleOrNull() ?: Double.NaN
        return NumberLiteral(num)
    }

    override fun visitLiteral(ctx: WKTCRSParser.LiteralContext): Literal = when {
        ctx.quotedText() != null -> visitQuotedText(ctx.quotedText()!!)
        ctx.number() != null -> visitNumber(ctx.number()!!)
        else -> throw IllegalArgumentException("Visitor method not implemented or rule produced no result.")
    }

    companion object {
        private fun unquote(quotedText: String): String {
            return quotedText.trim { it == '"' || it == '\'' }
        }
    }
}