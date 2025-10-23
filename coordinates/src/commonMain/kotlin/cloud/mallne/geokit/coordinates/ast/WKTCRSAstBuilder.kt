package cloud.mallne.geokit.coordinates.ast

import cloud.mallne.geokit.coordinates.ast.expression.*
import cloud.mallne.geokit.coordinates.generated.WKTCRSParser
import cloud.mallne.geokit.coordinates.generated.WKTCRSParserBaseVisitor
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
@Suppress("RedundantOverride") //TODO REMOVE THIS WHEN ALL METHODS ARE FULLY IMPLEMENTED
class WKTCRSAstBuilder : WKTCRSParserBaseVisitor<WKTCRSExpression>() {
    override fun defaultResult(): WKTCRSExpression =
        throw IllegalStateException("Visitor method not implemented or rule produced no result.")

    override fun visitAngleUnit(ctx: WKTCRSParser.AngleUnitContext) = AngleUnit(
        unitName = ctx.quotedText().computed, conversionFactor = ctx.number().computed, identifiers = ctx.id().computed
    )

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

    override fun visitStep(ctx: WKTCRSParser.StepContext) = visitChildren(ctx) as SteppedOperation

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

    override fun visitCrs(ctx: WKTCRSParser.CrsContext) = visitChildren(ctx) as CoordinateReferenceSystem

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

    override fun visitDynamicCrsCoordinateMetadata(ctx: WKTCRSParser.DynamicCrsCoordinateMetadataContext) =
        visitChildren(ctx) as DynamicCrsCoordinateMetadata

    override fun visitStaticCrsCoordinateMetadata(ctx: WKTCRSParser.StaticCrsCoordinateMetadataContext) =
        visitChildren(ctx) as StaticCrsCoordinateMetadata

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

    override fun visitSingleCrs(ctx: WKTCRSParser.SingleCrsContext) =
        visitChildren(ctx) as SingleCoordinateReferenceSystem

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

    override fun visitBaseTemporalCrs(ctx: WKTCRSParser.BaseTemporalCrsContext): WKTCRSExpression {
        return super.visitBaseTemporalCrs(ctx)
    }

    override fun visitDerivedTemporalCrs(ctx: WKTCRSParser.DerivedTemporalCrsContext): WKTCRSExpression {
        return super.visitDerivedTemporalCrs(ctx)
    }

    override fun visitBaseParametricCrs(ctx: WKTCRSParser.BaseParametricCrsContext): WKTCRSExpression {
        return super.visitBaseParametricCrs(ctx)
    }

    override fun visitDerivedParametricCrs(ctx: WKTCRSParser.DerivedParametricCrsContext): WKTCRSExpression {
        return super.visitDerivedParametricCrs(ctx)
    }

    override fun visitBaseEngeneeringCrs(ctx: WKTCRSParser.BaseEngeneeringCrsContext): WKTCRSExpression {
        return super.visitBaseEngeneeringCrs(ctx)
    }

    override fun visitDerivedEngineeringCrs(ctx: WKTCRSParser.DerivedEngineeringCrsContext): WKTCRSExpression {
        return super.visitDerivedEngineeringCrs(ctx)
    }

    override fun visitStaticBaseVerticalCrs(ctx: WKTCRSParser.StaticBaseVerticalCrsContext): WKTCRSExpression {
        return super.visitStaticBaseVerticalCrs(ctx)
    }

    override fun visitBaseVerticalCrs(ctx: WKTCRSParser.BaseVerticalCrsContext): WKTCRSExpression {
        return super.visitBaseVerticalCrs(ctx)
    }

    override fun visitDerivedVerticalCrs(ctx: WKTCRSParser.DerivedVerticalCrsContext): WKTCRSExpression {
        return super.visitDerivedVerticalCrs(ctx)
    }

    override fun visitBaseProjectedCrs(ctx: WKTCRSParser.BaseProjectedCrsContext): WKTCRSExpression {
        return super.visitBaseProjectedCrs(ctx)
    }

    override fun visitDerivedProjectedCrs(ctx: WKTCRSParser.DerivedProjectedCrsContext): WKTCRSExpression {
        return super.visitDerivedProjectedCrs(ctx)
    }

    override fun visitStaticDerivedGeographicCrs(ctx: WKTCRSParser.StaticDerivedGeographicCrsContext): WKTCRSExpression {
        return super.visitStaticDerivedGeographicCrs(ctx)
    }

    override fun visitStaticDerivedGeodeticCrs(ctx: WKTCRSParser.StaticDerivedGeodeticCrsContext): WKTCRSExpression {
        return super.visitStaticDerivedGeodeticCrs(ctx)
    }

    override fun visitDerivedGeographicCrs(ctx: WKTCRSParser.DerivedGeographicCrsContext): WKTCRSExpression {
        return super.visitDerivedGeographicCrs(ctx)
    }

    override fun visitDerivedGeodeticCrs(ctx: WKTCRSParser.DerivedGeodeticCrsContext): WKTCRSExpression {
        return super.visitDerivedGeodeticCrs(ctx)
    }

    override fun visitDerivingConversion(ctx: WKTCRSParser.DerivingConversionContext): WKTCRSExpression {
        return super.visitDerivingConversion(ctx)
    }

    override fun visitTemporalOrigin(ctx: WKTCRSParser.TemporalOriginContext): WKTCRSExpression {
        return super.visitTemporalOrigin(ctx)
    }

    override fun visitCalendar(ctx: WKTCRSParser.CalendarContext) = visitQuotedText(ctx.quotedText())

    override fun visitTemporalDatum(ctx: WKTCRSParser.TemporalDatumContext): WKTCRSExpression {
        return super.visitTemporalDatum(ctx)
    }

    override fun visitTemporalCrs(ctx: WKTCRSParser.TemporalCrsContext): WKTCRSExpression {
        return super.visitTemporalCrs(ctx)
    }

    override fun visitParametricDatum(ctx: WKTCRSParser.ParametricDatumContext): WKTCRSExpression {
        return super.visitParametricDatum(ctx)
    }

    override fun visitParametricCrs(ctx: WKTCRSParser.ParametricCrsContext): WKTCRSExpression {
        return super.visitParametricCrs(ctx)
    }

    override fun visitEngineeringDatum(ctx: WKTCRSParser.EngineeringDatumContext): WKTCRSExpression {
        return super.visitEngineeringDatum(ctx)
    }

    override fun visitEngineeringCrs(ctx: WKTCRSParser.EngineeringCrsContext): WKTCRSExpression {
        return super.visitEngineeringCrs(ctx)
    }

    override fun visitVerticalReferenceFrame(ctx: WKTCRSParser.VerticalReferenceFrameContext): WKTCRSExpression {
        return super.visitVerticalReferenceFrame(ctx)
    }

    override fun visitGeoidModelId(ctx: WKTCRSParser.GeoidModelIdContext): WKTCRSExpression {
        return super.visitGeoidModelId(ctx)
    }

    override fun visitStaticVerticalCrs(ctx: WKTCRSParser.StaticVerticalCrsContext): WKTCRSExpression {
        return super.visitStaticVerticalCrs(ctx)
    }

    override fun visitVerticalCrs(ctx: WKTCRSParser.VerticalCrsContext): WKTCRSExpression {
        return super.visitVerticalCrs(ctx)
    }

    override fun visitMapProjectionParameterUnit(ctx: WKTCRSParser.MapProjectionParameterUnitContext): WKTCRSExpression {
        return super.visitMapProjectionParameterUnit(ctx)
    }

    override fun visitMapProjectionParameter(ctx: WKTCRSParser.MapProjectionParameterContext): WKTCRSExpression {
        return super.visitMapProjectionParameter(ctx)
    }

    override fun visitMapProjectionMethod(ctx: WKTCRSParser.MapProjectionMethodContext): WKTCRSExpression {
        return super.visitMapProjectionMethod(ctx)
    }

    override fun visitMapProjection(ctx: WKTCRSParser.MapProjectionContext): WKTCRSExpression {
        return super.visitMapProjection(ctx)
    }

    override fun visitBaseStaticGeographicCrs(ctx: WKTCRSParser.BaseStaticGeographicCrsContext): WKTCRSExpression {
        return super.visitBaseStaticGeographicCrs(ctx)
    }

    override fun visitBaseStaticGeodeticCrs(ctx: WKTCRSParser.BaseStaticGeodeticCrsContext): WKTCRSExpression {
        return super.visitBaseStaticGeodeticCrs(ctx)
    }

    override fun visitBaseGeodeticCrs(ctx: WKTCRSParser.BaseGeodeticCrsContext): WKTCRSExpression {
        return super.visitBaseGeodeticCrs(ctx)
    }

    override fun visitProjectedCrs(ctx: WKTCRSParser.ProjectedCrsContext): WKTCRSExpression {
        return super.visitProjectedCrs(ctx)
    }

    override fun visitDatumAnchorEpoch(ctx: WKTCRSParser.DatumAnchorEpochContext) = visitNumber(ctx.number())

    override fun visitDatumAnchor(ctx: WKTCRSParser.DatumAnchorContext) = visitQuotedText(ctx.quotedText())


    override fun visitGeodeticReferenceFrame(ctx: WKTCRSParser.GeodeticReferenceFrameContext) = GeodeticReferenceFrame(
        name = ctx.quotedText().computed,
        ellipsoid = visitEllipsoid(ctx.ellipsoid()),
        anchor = ctx.datumAnchor()?.let { visitDatumAnchor(it).text },
        anchorEpoch = ctx.datumAnchorEpoch()?.let { visitDatumAnchorEpoch(it).value },
        identifiers = ctx.id().computed,
        primeMeridian = ctx.primeMeridian()?.let { visitPrimeMeridian(it) },
    )

    override fun visitIrmLongitude(ctx: WKTCRSParser.IrmLongitudeContext) = IrmLongitude(
        longitude = ctx.number().computed,
        unit = ctx.angleUnit()?.let { visitAngleUnit(it) }
    )

    override fun visitPrimeMeridian(ctx: WKTCRSParser.PrimeMeridianContext) = PrimeMeridian(
        name = ctx.quotedText().computed,
        irmLongitude = visitIrmLongitude(ctx.irmLongitude()),
        identifiers = ctx.id().computed
    )

    override fun visitEllipsoid(ctx: WKTCRSParser.EllipsoidContext) = Ellipsoid(
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

    override fun visitGeographicCrs(ctx: WKTCRSParser.GeographicCrsContext) =
        visitChildren(ctx) as GeographicCoordinateReferenceSystem

    override fun visitGeodeticCrs(ctx: WKTCRSParser.GeodeticCrsContext) =
        visitChildren(ctx) as GeodeticCoordinateReferenceSystem

    override fun visitDatumEnsembleAccuracy(ctx: WKTCRSParser.DatumEnsembleAccuracyContext) = visitNumber(ctx.number())

    override fun visitDatumEnsembleMember(ctx: WKTCRSParser.DatumEnsembleMemberContext) = DatumEnsembleMember(
        name = ctx.quotedText().computed,
        identifiers = ctx.id().computed
    )

    override fun visitVerticalDatumEnsemble(ctx: WKTCRSParser.VerticalDatumEnsembleContext) = VerticalDatumEnsemble(
        name = ctx.quotedText().computed,
        members = ctx.datumEnsembleMember().map { visitDatumEnsembleMember(it) },
        accuracy = visitDatumEnsembleAccuracy(ctx.datumEnsembleAccuracy()).value,
        identifiers = ctx.id().computed
    )

    override fun visitGeodeticDatumEnsemble(ctx: WKTCRSParser.GeodeticDatumEnsembleContext) = GeodeticDatumEnsemble(
        name = ctx.quotedText().computed,
        members = ctx.datumEnsembleMember().map { visitDatumEnsembleMember(it) },
        ellipsoid = visitEllipsoid(ctx.ellipsoid()),
        accuracy = visitDatumEnsembleAccuracy(ctx.datumEnsembleAccuracy()).value,
        identifiers = ctx.id().computed,
        primeMeridian = ctx.primeMeridian()?.let { visitPrimeMeridian(it) },
    )

    override fun visitDatumEnsemble(ctx: WKTCRSParser.DatumEnsembleContext) = visitChildren(ctx) as DatumEnsemble

    override fun visitAxisOrder(ctx: WKTCRSParser.AxisOrderContext) = ExpressionWrapper(ctx.INTEGER().text.toInt())

    override fun visitBearing(ctx: WKTCRSParser.BearingContext) = visitNumber(ctx.number())

    override fun visitMeridian(ctx: WKTCRSParser.MeridianContext) = Meridian(
        number = ctx.number().computed,
        unit = visitAngleUnit(ctx.angleUnit())
    )

    override fun visitAxisDirection(ctx: WKTCRSParser.AxisDirectionContext): AxisDirection {
        return when {
            ctx.NORTH() != null -> AxisDirection.NORTH(meridian = ctx.meridian()?.let { visitMeridian(it) })
            ctx.NORTHNORTHEAST() != null -> AxisDirection.NORTHNORTHEAST
            ctx.NORTHEAST() != null -> AxisDirection.NORTHEAST
            ctx.EASTNORTHEAST() != null -> AxisDirection.EASTNORTHEAST
            ctx.EAST() != null -> AxisDirection.EAST
            ctx.EASTSOUTHEAST() != null -> AxisDirection.EASTSOUTHEAST
            ctx.SOUTHEAST() != null -> AxisDirection.SOUTHEAST
            ctx.SOUTHSOUTHEAST() != null -> AxisDirection.SOUTHSOUTHEAST
            ctx.SOUTH() != null -> AxisDirection.SOUTH(meridian = ctx.meridian()?.let { visitMeridian(it) })
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
            ctx.CLOCKWISE() != null -> AxisDirection.CLOCKWISE(visitBearing(ctx.bearing()!!).value)
            ctx.COUNTERCLOCKWISE() != null -> AxisDirection.COUNTERCLOCKWISE(visitBearing(ctx.bearing()!!).value)
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

    override fun visitOrdinalDateTimeAxis(ctx: WKTCRSParser.OrdinalDateTimeAxisContext) = OrdinalDateTimeAxis(
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

    override fun visitSpatialAxis(ctx: WKTCRSParser.SpatialAxisContext) = SpatialAxis(
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

    override fun visitOrdinalDateTimeCS(ctx: WKTCRSParser.OrdinalDateTimeCSContext) = OrdinalDateTimeCS(
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

    override fun visitTemporalCountMeasureCS(ctx: WKTCRSParser.TemporalCountMeasureCSContext) = TemporalCountMeasureCS(
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

    override fun visitSpatialCS(ctx: WKTCRSParser.SpatialCSContext) = SpatialCS(
        type = visitSpatialCSType(ctx.spatialCSType()),
        dimension = visitDimension(ctx.dimension()),
        identifiers = ctx.id().computed,
        spatialAxis = ctx.spatialAxis().map { visitSpatialAxis(it) },
        unit = ctx.unit()?.let { visitUnit(it) },
    )

    override fun visitCs(ctx: WKTCRSParser.CsContext) = visitChildren(ctx) as CoordinateSystem

    override fun visitUnit(ctx: WKTCRSParser.UnitContext) = visitChildren(ctx) as WKTUnit

    override fun visitSpatialUnit(ctx: WKTCRSParser.SpatialUnitContext) = visitChildren(ctx) as SpatialUnit

    override fun visitTimeUnit(ctx: WKTCRSParser.TimeUnitContext) = TimeUnit(
        unitName = ctx.quotedText().computed,
        conversionFactor = ctx.number().computed,
        identifiers = ctx.id().computed
    )

    override fun visitParametricUnit(ctx: WKTCRSParser.ParametricUnitContext) = ParametricUnit(
        unitName = ctx.quotedText().computed,
        conversionFactor = ctx.number().computed,
        identifiers = ctx.id().computed
    )

    override fun visitScaleUnit(ctx: WKTCRSParser.ScaleUnitContext) = ScaleUnit(
        unitName = ctx.quotedText().computed,
        conversionFactor = ctx.number().computed,
        identifiers = ctx.id().computed
    )

    override fun visitId(ctx: WKTCRSParser.IdContext) = Identifier(
        authorityName = ctx.quotedText().computed,
        uid = visitLiteral(ctx.literal(0)!!),
        version = ctx.literal(1)?.let { visitLiteral(it) },
        citation = ctx.citation()?.let { visitCitation(it).text },
        uri = ctx.uri()?.let { visitUri(it).text })

    override fun visitUsage(ctx: WKTCRSParser.UsageContext) = Usage(
        scope = visitScope(ctx.scope()).text, extent = visitExtent(ctx.extent())
    )

    override fun visitExtent(ctx: WKTCRSParser.ExtentContext) = Extent(
        area = ctx.area()?.let { visitArea(it) },
        bbox = ctx.bbox()?.let { visitBbox(it) },
        verticalExtent = ctx.verticalExtent()?.let { visitVerticalExtent(it) },
        temporalExtent = ctx.temporalExtent()?.let { visitTemporalExtent(it) },
    )

    @OptIn(ExperimentalTime::class)
    override fun visitTemporalExtent(ctx: WKTCRSParser.TemporalExtentContext) = TemporalExtent(
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
        return ExpressionWrapper(LocalDateTime(date, time).toInstant(zone))
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
        return ExpressionWrapper(LocalDateTime(date, time).toInstant(zone))
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

    override fun visitUtcTZ(ctx: WKTCRSParser.UtcTZContext) = ExpressionWrapper(TimeZone.UTC.offset)
    override fun visitLocalTZ(ctx: WKTCRSParser.LocalTZContext) =
        ExpressionWrapper(UtcOffset.Formats.ISO.parse(ctx.text))

    override fun visitCalendarDate(ctx: WKTCRSParser.CalendarDateContext): ExpressionWrapper<LocalDate> =
        ExpressionWrapper(
            LocalDate(
                year = visitYear(ctx.year()).value,
                month = ctx.month()?.let { visitMonth(it).value } ?: 1,
                day = ctx.day()?.let { visitDay(it).value } ?: 1,
            ))

    override fun visitYear(ctx: WKTCRSParser.YearContext) = ExpressionWrapper(ctx.INTEGER().text.toInt())
    override fun visitMonth(ctx: WKTCRSParser.MonthContext) = ExpressionWrapper(ctx.INTEGER().text.toInt())
    override fun visitDay(ctx: WKTCRSParser.DayContext) = ExpressionWrapper(ctx.INTEGER().text.toInt())
    override fun visitHour(ctx: WKTCRSParser.HourContext) = ExpressionWrapper(ctx.INTEGER().text.toInt())
    override fun visitMinute(ctx: WKTCRSParser.MinuteContext) = ExpressionWrapper(ctx.INTEGER().text.toInt())
    override fun visitSecond(ctx: WKTCRSParser.SecondContext) = visitNumber(ctx.number())

    override fun visitScope(ctx: WKTCRSParser.ScopeContext) = visitQuotedText(ctx.quotedText())
    override fun visitCitation(ctx: WKTCRSParser.CitationContext) = visitQuotedText(ctx.quotedText())
    override fun visitUri(ctx: WKTCRSParser.UriContext) = visitQuotedText(ctx.quotedText())
    override fun visitArea(ctx: WKTCRSParser.AreaContext) = Area(ctx.quotedText().computed)
    override fun visitAxisRange(ctx: WKTCRSParser.AxisRangeContext) = AxisRange(
        min = ctx.axisMinimumValue()?.let { visitAxisMinimumValue(it).value },
        max = ctx.axisMaximumValue()?.let { visitAxisMaximumValue(it).value },
        meaning = ctx.axisRangeMeaning()?.let { visitAxisRangeMeaning(it) },
    )

    override fun visitVerticalExtent(ctx: WKTCRSParser.VerticalExtentContext) = VerticalExtent(
        minHeight = ctx.number(0)!!.computed,
        maxHeight = ctx.number(1)!!.computed,
        unit = ctx.lengthUnit()?.let { visitLengthUnit(it) })

    override fun visitLengthUnit(ctx: WKTCRSParser.LengthUnitContext) = LengthUnit(
        unitName = ctx.quotedText().computed, conversionFactor = ctx.number().computed, identifiers = ctx.id().computed
    )

    override fun visitAxisMinimumValue(ctx: WKTCRSParser.AxisMinimumValueContext) = visitNumber(ctx.number())
    override fun visitAxisMaximumValue(ctx: WKTCRSParser.AxisMaximumValueContext) = visitNumber(ctx.number())
    override fun visitAxisRangeMeaning(ctx: WKTCRSParser.AxisRangeMeaningContext) = when {
        ctx.EXACT() != null -> AxisRangeMeaning.EXACT
        ctx.WRAPAROUND() != null -> AxisRangeMeaning.WRAPAROUND
        else -> throw IllegalArgumentException("AxisRange meaning required.")
    }

    override fun visitBbox(ctx: WKTCRSParser.BboxContext) =
        Bbox(positions = ctx.number().map { it.computed }.toDoubleArray())

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

    override fun visitOperationParameter(ctx: WKTCRSParser.OperationParameterContext) = OperationParameter(
        name = ctx.quotedText().computed,
        value = ctx.number().computed,
        unit = visitParameterUnit(ctx.parameterUnit()),
        identifiers = ctx.id().computed
    )

    override fun visitParameterUnit(ctx: WKTCRSParser.ParameterUnitContext): ParameterUnit =
        visitChildren(ctx) as ParameterUnit

    override fun visitOperationParameterFile(ctx: WKTCRSParser.OperationParameterFileContext) = OperationParameterFile(
        name = ctx.quotedText(0)!!.computed, filename = ctx.quotedText(1)!!.computed, identifiers = ctx.id().computed
    )

    override fun visitAbridgedTransformationParameter(ctx: WKTCRSParser.AbridgedTransformationParameterContext) =
        AbridgedTransformationParameter(
            name = ctx.quotedText().computed, value = ctx.number().computed, identifiers = ctx.id().computed
        )

    override fun visitOperationMethod(ctx: WKTCRSParser.OperationMethodContext) = OperationMethod(
        name = ctx.quotedText().computed, identifiers = ctx.id().computed
    )

    override fun visitOperationVersion(ctx: WKTCRSParser.OperationVersionContext) = visitQuotedText(ctx.quotedText())

    override fun visitScopeExtentIdentifierRemark(ctx: WKTCRSParser.ScopeExtentIdentifierRemarkContext) =
        ScopeExtentIdentifierRemarkProxy(
            usages = ctx.usage().computed,
            identifiers = ctx.id().computed,
            remark = ctx.remark()?.let { visitRemark(it).text })

    override fun visitRemark(ctx: WKTCRSParser.RemarkContext) = visitQuotedText(ctx.quotedText())

    override fun visitSourceCrs(ctx: WKTCRSParser.SourceCrsContext) = visit(ctx.crs()) as CoordinateReferenceSystem
    override fun visitTargetCrs(ctx: WKTCRSParser.TargetCrsContext) = visit(ctx.crs()) as CoordinateReferenceSystem

    val WKTCRSParser.QuotedTextContext.computed
        get() = visitQuotedText(this).text
    val WKTCRSParser.NumberContext.computed
        get() = visitNumber(this).value
    val WKTCRSParser.IdContext.computed
        get() = visitId(this)
    val List<WKTCRSParser.IdContext>.computed
        get() = this.map { visitId(it) }
    val List<WKTCRSParser.UsageContext>.computed
        get() = this.map { visitUsage(it) }


    // --- Basic literals ---
    override fun visitQuotedText(ctx: WKTCRSParser.QuotedTextContext) = TextLiteral(unquote(ctx.text))

    override fun visitNumber(ctx: WKTCRSParser.NumberContext): NumberLiteral {
        val num = ctx.text.trim().toDoubleOrNull() ?: Double.NaN
        return NumberLiteral(num)
    }

    override fun visitLiteral(ctx: WKTCRSParser.LiteralContext) = visitChildren(ctx) as Literal

    companion object {
        private fun unquote(quotedText: String): String {
            return quotedText.trim { it == '"' || it == '\'' }
        }
    }
}