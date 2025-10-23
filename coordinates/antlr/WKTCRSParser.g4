parser grammar WKTCRSParser;

options {
    tokenVocab = WKTCRSLexer;
}

// ----------------------------------------------------------------------
// Top-Level Entry Point
// ----------------------------------------------------------------------

/**
 * The main entry rule for a WKT string. It allows any major WKT 2 object
 * type to be parsed, including CRSs, Operations, Datums, Units, and Annex D
 * metadata (like point motion and ensemble definitions), to support
 * comprehensive datasets like EPSG.
 */
wktCRS
    : (boundCrs
    | compoundCrs
    | engineeringCrs
    | geodeticCrs
    | geographicCrs
    | parametricCrs
    | projectedCrs
    | temporalCrs
    | verticalCrs
    | derivedGeodeticCrs
    | derivedProjectedCrs
    | derivedVerticalCrs
    | derivedEngineeringCrs
    | derivedParametricCrs
    | derivedTemporalCrs
    | coordinateOperation
    | pmo
    | concatenatedOperation
    | coordinateMetadata
    | datumEnsemble
    ) EOF
    ;

delimiterOpen : LBRACKET
    | LPAREN
    ;

delimiterClose : RBRACKET
    | RPAREN
    ;


usage
    : USAGE delimiterOpen
    scope COMMA
    extent
    delimiterClose
    ;

scope
    : SCOPE delimiterOpen quotedText delimiterClose
    ;

dateTime
    : calendarDateTime
    | ordinalDateTime
    ;

calendarDateTime : calendarDate (clock24H)?;

calendarDate
    : year (MINUS month (MINUS day)?)?
    ;
year : INTEGER ;
month : INTEGER ;
day : INTEGER ;

ordinalDateTime : ordinalDate (clock24H)?;

ordinalDate : year (MINUS ordinalDay)?;

ordinalDay : INTEGER ;

clock24H : TIMEDESIGNATOR hour (MINUS minute (MINUS second)?)? timeZone ;

hour : INTEGER ;
minute : INTEGER ;
second : number ;

timeZone : utcTZ | localTZ ;

utcTZ : UTC ;
localTZ : (PLUS | MINUS) hour (COLON minute)? ;

// ----------------------------------------------------------------------
// Extent Rules (Complex Optional Metadata)
// ----------------------------------------------------------------------
scopeExtentIdentifierRemark: (COMMA usage)* (COMMA id)* (COMMA remark)?;

/**
 * EXTENT structure can contain multiple specific extent definitions.
 */
extent
    : area | bbox | verticalExtent | temporalExtent
    | ( area COMMA bbox )
    | ( area COMMA verticalExtent )
    | ( area COMMA temporalExtent )
    | ( bbox COMMA verticalExtent )
    | ( bbox COMMA temporalExtent )
    | ( verticalExtent COMMA temporalExtent )
    | ( area COMMA bbox COMMA verticalExtent )
    | ( area COMMA bbox COMMA temporalExtent )
    | ( area COMMA verticalExtent COMMA temporalExtent )
    | ( bbox COMMA verticalExtent COMMA temporalExtent )
    | ( area COMMA bbox COMMA verticalExtent COMMA temporalExtent )
    ;

/**
 * AREA defines a scope by quoted description.
 */
area
    : AREA delimiterOpen quotedText delimiterClose
    ;

/**
 * BBOX defines a scope by bounding box coordinates.
 * Format: lower_lat, lower_lon, upper_lat, upper_lon
 */
bbox
    : BBOX delimiterOpen
        number COMMA number COMMA
        number COMMA number
    delimiterClose
    ;

/**
 * VERTICALEXTENT defines min/max height/depth and the vertical unit.
 */
verticalExtent
    : VERTICALEXTENT delimiterOpen
        number COMMA number
        (COMMA lengthUnit)?
    delimiterClose
    ;

/**
 * TIMEEXTENT defines the start and end time point as quoted text strings.
 */
temporalExtent
    : TIMEEXTENT delimiterOpen
        (dateTime | quotedText) COMMA         // Start Time Point
        (dateTime | quotedText)
    delimiterClose
    ;

// Standard definition of an authority ID
id
    : ID delimiterOpen
    quotedText COMMA     // Authority name (e.g., "EPSG")
        literal             // Authority code (e.g., "4326")
        (COMMA literal)?    // Optional version
        (COMMA citation)?    // Optional Citation
        (COMMA uri)?    // Optional URI
    delimiterClose
    ;

citation
    : CITATION delimiterOpen quotedText delimiterClose;
uri
    : URI delimiterOpen quotedText delimiterClose;
remark : REMARK delimiterOpen quotedText delimiterClose ;

// ----------------------------------------------------------------------
// Unit Structures
// ----------------------------------------------------------------------

unit
    : spatialUnit
    | timeUnit
    ;

spatialUnit: angleUnit | lengthUnit | parametricUnit | scaleUnit;

lengthUnit
    : LENGTHUNIT delimiterOpen quotedText COMMA number (COMMA id)* delimiterClose
    ;

angleUnit
    : ANGLEUNIT delimiterOpen quotedText COMMA number (COMMA id)* delimiterClose
    ;

scaleUnit
    : SCALEUNIT delimiterOpen quotedText COMMA number (COMMA id)* delimiterClose
    ;

timeUnit
    : TIMEUNIT delimiterOpen quotedText COMMA number (COMMA id)* delimiterClose
    ;

parametricUnit
    : PARAMUNIT delimiterOpen quotedText COMMA number (COMMA id)* delimiterClose
    ;

// ----------------------------------------------------------------------
// Coordinate System Structures
// ----------------------------------------------------------------------

cs
    : spatialCS | temporalCountMeasureCS | ordinalDateTimeCS
    ;

spatialCS : CS delimiterOpen spatialCSType COMMA dimension (COMMA id)* delimiterClose (COMMA spatialAxis)+ (COMMA unit)?;
spatialCSType: AFFINE|CARTESIAN|CYLINDRICAL|ELLIPSOIDAL|LINEAR
               |PARAMETRIC|POLAR|SPHERICAL|VERTICAL ;
dimension: number ;
temporalCountMeasureCS : CS delimiterOpen temporalCountMeasureCSType COMMA dimension (COMMA id)* delimiterClose COMMA temporalCountMeasureAxis;
temporalCountMeasureCSType: TEMPORALCOUNT|TEMPORALMEASURE ;
ordinalDateTimeCS : CS delimiterOpen ordinalDateTimeCSType COMMA dimension (COMMA id)* delimiterClose (COMMA ordinalDateTimeAxis)+ ;
ordinalDateTimeCSType : ORDINAL|TEMPORALDATETIME ;

spatialAxis: AXIS delimiterOpen quotedText COMMA axisDirection (COMMA axisOrder)? (COMMA spatialUnit)? (COMMA axisRange)? (COMMA id)* delimiterClose ;
temporalCountMeasureAxis: AXIS delimiterOpen quotedText COMMA axisDirection (COMMA axisOrder)? (COMMA timeUnit)? (COMMA axisRange)? (COMMA id)* delimiterClose ;
ordinalDateTimeAxis: AXIS delimiterOpen quotedText COMMA axisDirection (COMMA axisOrder)? (COMMA axisRange)? (COMMA id)* delimiterClose ;
axisRange
    : axisMinimumValue
    | axisMaximumValue
    | (axisMinimumValue COMMA axisMaximumValue)
    | (axisMinimumValue COMMA axisMaximumValue COMMA axisRangeMeaning)
    ;
axisMinimumValue: AXISMINVALUE delimiterOpen number delimiterClose ;
axisMaximumValue: AXISMAXVALUE delimiterOpen number delimiterClose ;
axisRangeMeaning: RANGEMEANING delimiterOpen (EXACT | WRAPAROUND) delimiterClose ;
axisDirection
    : NORTH (COMMA meridian)?
    | NORTHNORTHEAST
    | NORTHEAST
    | EASTNORTHEAST
    | EAST
    | EASTSOUTHEAST
    | SOUTHEAST
    | SOUTHSOUTHEAST
    | SOUTH (COMMA meridian)?
    | SOUTHSOUTHWEST
    | SOUTHWEST
    | WESTSOUTHWEST
    | WEST
    | WESTNORTHWEST
    | NORTHWEST
    | NORTHNORTHWEST
    | GEOCENTRICX
    | GEOCENTRICY
    | GEOCENTRICZ
    | UP
    | DOWN
    | FORWARD
    | AFT
    | PORT
    | STARBOARD
    | CLOCKWISE COMMA bearing
    | COUNTERCLOCKWISE COMMA bearing
    | COLUMNPOSITIVE
    | COLUMNNEGATIVE
    | ROWPOSITIVE
    | ROWNEGATIVE
    | DISPLAYRIGHT
    | DISPLAYLEFT
    | DISPLAYUP
    | DISPLAYDOWN
    | FUTURE
    | PAST
    | TOWARDS
    | AWAYFROM
    | UNSPECIFIED
    ;

meridian: MERIDIAN delimiterOpen number COMMA angleUnit delimiterClose;
bearing: BEARING delimiterOpen number delimiterClose;
axisOrder: ORDER delimiterOpen INTEGER delimiterClose;

// ----------------------------------------------------------------------
// 3. Component Structures (Datum, Ellipsoid, Unit)
// ----------------------------------------------------------------------

datumEnsemble
    : geodeticDatumEnsemble
    | verticalDatumEnsemble
    ;
geodeticDatumEnsemble
    : ENSEMBLE delimiterOpen quotedText
    (COMMA datumEnsembleMember)+
    COMMA ellipsoid
    COMMA datumEnsembleAccuracy
    (COMMA id)*
    delimiterClose
    (COMMA primeMeridian)?;
verticalDatumEnsemble
    : ENSEMBLE delimiterOpen quotedText
    (COMMA datumEnsembleMember)+
    COMMA datumEnsembleAccuracy
    (COMMA id)*
    delimiterClose ;

datumEnsembleMember : MEMBER delimiterOpen quotedText (COMMA id)* delimiterClose;
datumEnsembleAccuracy : ENSEMBLEACCURACY delimiterOpen number delimiterClose;



// ----------------------------------------------------------------------
// Main CRS Structures
// ----------------------------------------------------------------------
geodeticCrs : staticGeodeticCrs | geographicCrs ;
geographicCrs : staticGeographicCrs ;

staticGeodeticCrs
    : (GEODCRS | GEODETICCRS) delimiterOpen quotedText
      COMMA (geodeticReferenceFrame | geodeticDatumEnsemble)
      COMMA cs
      (COMMA definingTransformationID)*
      scopeExtentIdentifierRemark delimiterClose
    ;
staticGeographicCrs
    : (GEOGCRS | GEOGRAPHICCRS) delimiterOpen quotedText
      COMMA (geodeticReferenceFrame | geodeticDatumEnsemble)
      COMMA cs
      (COMMA definingTransformationID)*
      scopeExtentIdentifierRemark delimiterClose
    ;
definingTransformationID : DEFININGTRANSFORMATION delimiterOpen quotedText (COMMA id)? delimiterClose;

ellipsoid
    : (ELLIPSOID | SPHEROID) delimiterOpen
        quotedText COMMA
        number COMMA               // Semi-major axis (metres)
        number                     // Inverse flattening
        (COMMA lengthUnit)?        // Optional length unit for semi-major axis
        (COMMA id)*
    delimiterClose
    ;
primeMeridian: (PRIMEM | PRIMEMERIDIAN) delimiterOpen quotedText COMMA irmLongitude (COMMA id)* delimiterClose;
irmLongitude: number (COMMA angleUnit)? ;

geodeticReferenceFrame
    : (DATUM | TRF | GEODETICDATUM) delimiterOpen quotedText COMMA ellipsoid
    (COMMA datumAnchor)?
    (COMMA datumAnchorEpoch)?
    (COMMA id)* delimiterClose
    (COMMA primeMeridian)?
    ;

datumAnchor : ANCHOR delimiterOpen quotedText delimiterClose ;
datumAnchorEpoch : ANCHOREPOCH delimiterOpen number delimiterClose ;


// ----------------------------------------------------------------------
// Projected CRS
// ----------------------------------------------------------------------
projectedCrs
    : (PROJCRS | PROJECTEDCRS) delimiterOpen
        quotedText COMMA
        baseGeodeticCrs COMMA             // BASEGEOGCRS or BASEGEODCRS (Annex D)
        mapProjection COMMA
        cs
        scopeExtentIdentifierRemark
    delimiterClose
    ;

baseGeodeticCrs : baseStaticGeodeticCrs | baseStaticGeographicCrs;
baseStaticGeodeticCrs
    : BASEGEODCRS delimiterOpen
    quotedText COMMA
    (geodeticReferenceFrame | geodeticDatumEnsemble)
    ( COMMA angleUnit )?
    ( COMMA id )* delimiterClose
    ;
baseStaticGeographicCrs
    : BASEGEOGCRS delimiterOpen
    quotedText COMMA
    (geodeticReferenceFrame | geodeticDatumEnsemble)
    ( COMMA angleUnit )?
    ( COMMA id )* delimiterClose
    ;

mapProjection
    : CONVERSION delimiterOpen
    quotedText
    COMMA mapProjectionMethod
    (COMMA mapProjectionParameter)*
    (COMMA id)*
    delimiterClose
    ;
mapProjectionMethod:(METHOD | PROJECTION) delimiterOpen quotedText (COMMA id)* delimiterClose;
mapProjectionParameter:PARAMETER delimiterOpen quotedText COMMA number (COMMA mapProjectionParameterUnit)? (COMMA id)* delimiterClose;
mapProjectionParameterUnit: lengthUnit | angleUnit | scaleUnit;

// ----------------------------------------------------------------------
// Vertical CRS
// ----------------------------------------------------------------------
verticalCrs : staticVerticalCrs ;

staticVerticalCrs
    : (VERTCRS | VERTICALCRS) delimiterOpen quotedText
    COMMA (verticalReferenceFrame | verticalDatumEnsemble)
    COMMA cs
    (COMMA geoidModelId)*
    scopeExtentIdentifierRemark
    delimiterClose
    ;
geoidModelId: GEOIDMODEL delimiterOpen quotedText (COMMA id)? delimiterClose;

verticalReferenceFrame
    : (VDATUM | VRF | VERTICALDATUM) delimiterOpen
    quotedText
    (COMMA datumAnchor)?
    ( COMMA datumAnchorEpoch )?
    ( COMMA id )* delimiterClose
    ;

// ----------------------------------------------------------------------
// Engeneering CRS
// ----------------------------------------------------------------------
engineeringCrs
    : (ENGCRS | ENGINEERINGCRS) delimiterOpen
        quotedText
        COMMA engineeringDatum
        COMMA cs
        scopeExtentIdentifierRemark
    delimiterClose
    ;
engineeringDatum:(EDATUM | ENGINEERINGDATUM) delimiterOpen quotedText (COMMA datumAnchor)? (COMMA id)* delimiterClose;

// ----------------------------------------------------------------------
// Parametric CRS
// ----------------------------------------------------------------------
parametricCrs
    : PARAMETRICCRS delimiterOpen quotedText
    COMMA parametricDatum
    COMMA cs
    scopeExtentIdentifierRemark delimiterClose
    ;
parametricDatum:(PDATUM | PARAMETRICDATUM) delimiterOpen quotedText (COMMA datumAnchor)? (COMMA id)* delimiterClose;

// ----------------------------------------------------------------------
// Temporal CRS
// ----------------------------------------------------------------------
temporalCrs
    : TIMECRS delimiterOpen quotedText
    COMMA temporalDatum
    COMMA cs
    scopeExtentIdentifierRemark delimiterClose
    ;
temporalDatum : (TDATUM | TIMEDATUM) delimiterOpen quotedText (COMMA calendar)? (COMMA temporalOrigin)? (COMMA id)* delimiterClose;
calendar : CALENDAR delimiterOpen quotedText delimiterClose;
temporalOrigin : TIMEORIGIN delimiterOpen (dateTime | quotedText) delimiterClose;

// ----------------------------------------------------------------------
// Derrived CRS
// ----------------------------------------------------------------------
derivingConversion
    : DERIVINGCONVERSION delimiterOpen
    quotedText
    COMMA operationMethod
    ( COMMA  (operationParameter | operationParameterFile ))*
    ( COMMA id )* delimiterClose
    ;

operationMethod: METHOD delimiterOpen quotedText (COMMA id)* delimiterClose;
operationParameter: PARAMETER delimiterOpen quotedText COMMA number COMMA parameterUnit (COMMA id)* delimiterClose;
parameterUnit: lengthUnit | angleUnit | scaleUnit | timeUnit | parametricUnit;
operationParameterFile: PARAMETERFILE delimiterOpen quotedText COMMA quotedText (COMMA id)* delimiterClose ;

derivedGeodeticCrs: staticDerivedGeodeticCrs | derivedGeographicCrs;
derivedGeographicCrs: staticDerivedGeographicCrs;

staticDerivedGeodeticCrs
    : (GEODCRS | GEODETICCRS) delimiterOpen quotedText
    COMMA ( baseStaticGeodeticCrs | baseStaticGeographicCrs )
    COMMA derivingConversion
    COMMA cs
    scopeExtentIdentifierRemark delimiterClose
    ;

staticDerivedGeographicCrs
    : (GEOGCRS | GEOGRAPHICCRS) delimiterOpen quotedText
    COMMA ( baseStaticGeodeticCrs | baseStaticGeographicCrs )
    COMMA derivingConversion
    COMMA cs
    scopeExtentIdentifierRemark delimiterClose
    ;

derivedProjectedCrs
    :DERIVEDPROJCRS delimiterOpen
    quotedText COMMA baseProjectedCrs
    COMMA derivingConversion
    COMMA cs
    scopeExtentIdentifierRemark delimiterClose
    ;
baseProjectedCrs
    : BASEPROJCRS delimiterOpen
    quotedText
    COMMA baseGeodeticCrs
    COMMA mapProjection
    ( COMMA id )? delimiterClose
    ;

derivedVerticalCrs
    :(VERTCRS | VERTICALCRS) delimiterOpen quotedText
    COMMA baseVerticalCrs
    COMMA derivingConversion
    COMMA cs
    scopeExtentIdentifierRemark delimiterClose
    ;

baseVerticalCrs: staticBaseVerticalCrs;
staticBaseVerticalCrs
    : BASEVERTCRS delimiterOpen quotedText
    COMMA ( verticalReferenceFrame | verticalDatumEnsemble )
    ( COMMA id )* delimiterClose
    ;

derivedEngineeringCrs
    : (ENGCRS | ENGINEERINGCRS) delimiterOpen quotedText
    COMMA baseEngeneeringCrs
    COMMA derivingConversion
    COMMA cs
    scopeExtentIdentifierRemark delimiterClose
    ;
baseEngeneeringCrs
    : BASEENGCRS delimiterOpen quotedText
    COMMA engineeringDatum
    ( COMMA id )* delimiterClose
    ;

derivedParametricCrs
    : PARAMETRICCRS delimiterOpen quotedText
    COMMA baseParametricCrs
    COMMA derivingConversion
    COMMA cs
    scopeExtentIdentifierRemark delimiterClose
    ;
baseParametricCrs
    : BASEPARAMCRS delimiterOpen quotedText
    COMMA parametricDatum
    ( COMMA id )* delimiterClose
    ;

derivedTemporalCrs
    : TIMECRS delimiterOpen quotedText
    COMMA baseTemporalCrs
    COMMA derivingConversion
    COMMA cs
    scopeExtentIdentifierRemark delimiterClose
    ;
baseTemporalCrs
    : BASETIMECRS delimiterOpen quotedText
    COMMA temporalDatum
    ( COMMA id )* delimiterClose
    ;

// ----------------------------------------------------------------------
// Compound CRS
// ----------------------------------------------------------------------
compoundCrs
    : COMPOUNDCRS delimiterOpen quotedText
    COMMA singleCrs
    COMMA singleCrs
    (COMMA singleCrs)*
    scopeExtentIdentifierRemark delimiterClose
    ;
singleCrs
    : geodeticCrs
    | derivedGeodeticCrs
    | projectedCrs
    | derivedProjectedCrs
    | verticalCrs
    | derivedVerticalCrs
    | engineeringCrs
    | derivedEngineeringCrs
    | parametricCrs
    | derivedParametricCrs
    | temporalCrs
    | derivedTemporalCrs
    ;

// ----------------------------------------------------------------------
// Coordinate Epoch
// ----------------------------------------------------------------------
coordinateEpoch: (EPOCH | COORDEPOCH) delimiterOpen number delimiterClose;

// ----------------------------------------------------------------------
// Coordinate Metadata
// ----------------------------------------------------------------------
coordinateMetadata
    : COORDINATEMETADATA delimiterOpen staticCrsCoordinateMetadata |
    ( dynamicCrsCoordinateMetadata COMMA coordinateEpoch )
    delimiterClose
    ;
staticCrsCoordinateMetadata
    : staticGeodeticCrs
    | staticGeographicCrs
    | projectedCrs
    | staticVerticalCrs
    | engineeringCrs
    | parametricCrs
    | temporalCrs
    | derivedGeodeticCrs
    | derivedProjectedCrs
    | derivedVerticalCrs
    | derivedEngineeringCrs
    | derivedParametricCrs
    | derivedTemporalCrs
    | compoundCrs
    ;
dynamicCrsCoordinateMetadata
: //dynamicGeodeticCrs
//| dynamicGeographicCrs
| projectedCrs
//| dynamicVerticalCrs
| derivedGeodeticCrs
| derivedProjectedCrs
| derivedVerticalCrs
| compoundCrs
;

// ----------------------------------------------------------------------
// Coordinate Operation
// ----------------------------------------------------------------------
coordinateOperation
    : COORDINATEOPERATION delimiterOpen quotedText
    ( COMMA operationVersion )?
    COMMA sourceCrs
    COMMA targetCrs
    COMMA operationMethod
    ( COMMA ( operationParameter | operationParameterFile ) )*
    ( COMMA interpolationCrs )?
    ( COMMA operationAccuracy )?
    scopeExtentIdentifierRemark delimiterClose
    ;

operationVersion: VERSION delimiterOpen quotedText delimiterClose;
sourceCrs : SOURCECRS delimiterOpen crs delimiterClose;
targetCrs : TARGETCRS delimiterOpen crs delimiterClose;

crs: singleCrs | compoundCrs;

interpolationCrs: INTERPOLATIONCRS delimiterOpen crs delimiterClose;
operationAccuracy: OPERATIONACCURACY delimiterOpen number delimiterClose;

// ----------------------------------------------------------------------
// Point Motion Operation
// ----------------------------------------------------------------------
pmo
    :POINTMOTIONOPERATION delimiterOpen quotedText
    ( COMMA operationVersion )?
    COMMA sourceCrs
    COMMA operationMethod
    ( COMMA ( operationParameter | operationParameterFile ) )*
    ( COMMA operationAccuracy )?
    scopeExtentIdentifierRemark delimiterClose
    ;

// ----------------------------------------------------------------------
// Concatenated Operation
// ----------------------------------------------------------------------
concatenatedOperation
    : CONCATENATEDOPERATION delimiterOpen quotedText
    ( COMMA operationVersion )?
    COMMA sourceCrs COMMA targetCrs
    ( COMMA step )+
    ( COMMA operationAccuracy )?
    scopeExtentIdentifierRemark delimiterClose
    ;

step : STEP delimiterOpen ( coordinateOperation | pmo | mapProjection | derivingConversion ) delimiterClose;

// ----------------------------------------------------------------------
// Bound CRS
// ----------------------------------------------------------------------
boundCrs
    : BOUNDCRS delimiterOpen sourceCrs
    COMMA targetCrs
    COMMA abridgedCoordinateTransformation
    scopeExtentIdentifierRemark
    delimiterClose
    ;
abridgedCoordinateTransformation
    : ABRIDGEDTRANSFORMATION delimiterOpen quotedText
    ( COMMA operationVersion )?
    COMMA operationMethod
    ( COMMA ( abridgedTransformationParameter | operationParameterFile ) )*
    scopeExtentIdentifierRemark delimiterClose
    ;

abridgedTransformationParameter
    :PARAMETER delimiterOpen quotedText
    COMMA number
    ( COMMA id )* delimiterClose
    ;

// ----------------------------------------------------------------------
// 6. Identifier and Literal Rules
// ----------------------------------------------------------------------

quotedText
    : QUOTED_TEXT
    ;

number
    : NUMBER
    ;

literal
    : quotedText
    | number
    ;
