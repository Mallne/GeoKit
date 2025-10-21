lexer grammar WKTCRSLexer;

options {
    caseInsensitive = true;
}

// ----------------------------------------------------------------------
// 1. Punctuation and Separators
// ----------------------------------------------------------------------

// The standard specifies that WKT strings shall include only one of the two forms
// of delimiters (brackets or parentheses) and that they cannot be mixed.
// The lexer defines both, and the parser or a subsequent processing step must enforce the non-mixing rule.
LBRACKET : '[';
RBRACKET : ']';
LPAREN   : '(';
RPAREN   : ')';

COMMA    : ',';
PLUS     : '+';
MINUS    : '-';
COLON    : ':'; // Used in time zone designators e.g., in DATETIME

// ----------------------------------------------------------------------
// 2. Reserved Keywords (Case-Insensitive - now using readable string literals)
// ----------------------------------------------------------------------

// With 'caseInsensitive = true', these string literals will match keywords
// regardless of input capitalization (e.g., 'GEODCRS', 'geodcrs', or 'GeodCrs').

// Core CRS Keywords
GEODCRS                 : 'GEODCRS';
GEODETICCRS                 : 'GEODETICCRS';
GEOGCRS                 : 'GEOGCRS';
GEOGRAPHICCRS                 : 'GEOGRAPHICCRS';
PROJCRS                 : 'PROJCRS';
PROJECTEDCRS                 : 'PROJECTEDCRS';
VERTCRS                 : 'VERTCRS';
ENGCRS                  : 'ENGCRS';
ENGINEERINGCRS                  : 'ENGINEERINGCRS';
PARAMCRS                : 'PARAMCRS';
TIMECRS                 : 'TIMECRS';
COMPOUNDCRS             : 'COMPOUNDCRS';
DERIVEDPROJCRS          : 'DERIVEDPROJCRS';
BASEGEOGCRS             : 'BASEGEOGCRS';
BASEGEODCRS             : 'BASEGEODCRS';
BASEPROJCRS             : 'BASEPROJCRS';
BASEVERTCRS             : 'BASEVERTCRS';
BASEENGCRS              : 'BASEENGCRS';
BASEPARAMCRS              : 'BASEPARAMCRS';
BASETIMECRS              : 'BASETIMECRS';
DEFININGTRANSFORMATION : 'DEFININGTRANSFORMATION';

AFFINE : 'affine';
CARTESIAN : 'Cartesian';
CYLINDRICAL : 'cylindrical';
ELLIPSOIDAL : 'ellipsoidal';
LINEAR : 'linear';
PARAMETRIC : 'parametric';
POLAR : 'polar';
SPHERICAL : 'spherical';
VERTICAL : 'vertical';

TEMPORALCOUNT : 'temporalCount';
TEMPORALMEASURE : 'temporalMeasure';

ORDINAL : 'ordinal' ;
TEMPORALDATETIME : 'temporalDateTime' ;

EXACT: 'exact';
WRAPAROUND: 'wraparound';

NORTH:'north';
NORTHNORTHEAST:'northNorthEast';
NORTHEAST:'northEast';
EASTNORTHEAST:'eastNorthEast';
EAST:'east';
EASTSOUTHEAST:'eastSouthEast';
SOUTHEAST:'southEast';
SOUTHSOUTHEAST:'southSouthEast';
SOUTH:'south';
SOUTHSOUTHWEST:'southSouthWest';
SOUTHWEST:'southWest';
WESTSOUTHWEST:'westSouthWest';
WEST:'west';
WESTNORTHWEST:'westNorthWest';
NORTHWEST:'northWest';
NORTHNORTHWEST:'northNorthWest';
GEOCENTRICX:'geocentricX';
GEOCENTRICY:'geocentricY';
GEOCENTRICZ:'geocentricZ';
UP:'up';
DOWN:'down';
FORWARD:'forward';
AFT:'aft';
PORT:'port';
STARBOARD:'starboard';
CLOCKWISE:'clockwise';
COUNTERCLOCKWISE:'counterClockwise';
COLUMNPOSITIVE:'columnPositive';
COLUMNNEGATIVE:'columnNegative';
ROWPOSITIVE:'rowPositive';
ROWNEGATIVE:'rowNegative';
DISPLAYRIGHT:'displayRight';
DISPLAYLEFT:'displayLeft';
DISPLAYUP:'displayUp';
DISPLAYDOWN:'displayDown';
FUTURE:'future';
PAST:'past';
TOWARDS:'towards';
AWAYFROM:'awayFrom';
UNSPECIFIED:'unspecified';

// Datum/Reference Frame Keywords
DATUM                   : 'DATUM';
GEODETICDATUM                   : 'GEODETICDATUM';
ANCHOR                   : 'ANCHOR';
ANCHOREPOCH                   : 'ANCHOREPOCH';
ELLIPSOID               : 'ELLIPSOID';
SPHEROID               : 'SPHEROID';
PRIMEMERIDIAN           : 'PRIMEMERIDIAN';
PRIMEM           : 'PRIMEM';
TRF                     : 'TRF';                      // Terrestrial Reference Frame (Annex D)
VRF                     : 'VRF';                      // Vertical Reference Frame (Annex D)
VDATUM                     : 'VDATUM';                      // Vertical Reference Frame (Annex D)
VERTICALDATUM                     : 'VERTICALDATUM';                      // Vertical Reference Frame (Annex D)
EDATUM                  : 'EDATUM';
ENGINEERINGDATUM                  : 'ENGINEERINGDATUM';
PDATUM                  : 'PDATUM';
PARAMETRICDATUM                  : 'PARAMETRICDATUM';
PARAMETRICCRS                  : 'PARAMETRICCRS';
TDATUM                  : 'TDATUM';
TIMEDATUM                  : 'TIMEDATUM';
TIMEORIGIN                  : 'TIMEORIGIN';
CALENDAR                  : 'CALENDAR';
ENSEMBLE                : 'ENSEMBLE';            // Annex D
ENSEMBLEACCURACY        : 'ENSEMBLEACCURACY'; // Annex D
GEOIDMODEL              : 'GEOIDMODEL';        // Annex D
MEMBER                  : 'MEMBER';                // Annex D
DYNAMIC                 : 'DYNAMIC';              // Annex D
FRAMEEPOCH              : 'FRAMEEPOCH';        // Annex D
MODEL                   : 'MODEL';                  // Annex D
USAGE                   : 'USAGE';                  // Annex D
TIMEDESIGNATOR          : 'T';

// Coordinate System and Axis Keywords
CS                      : 'CS';
AXIS                    : 'AXIS';
AXISMINVALUE : 'AXISMINVALUE';
AXISMAXVALUE : 'AXISMAXVALUE';
RANGEMEANING:'RANGEMEANING';
SPATIAL                 : 'SPATIAL';
TEMPORAL                : 'TEMPORAL';
ORDINALDATETIMECS       : 'ORDINALDATETIMECS';
COORDINATEOPERATION       : 'COORDINATEOPERATION';
INTERPOLATIONCRS       : 'INTERPOLATIONCRS';
OPERATIONACCURACY       : 'OPERATIONACCURACY';
VERSION       : 'VERSION';
MERIDIAN: 'MERIDIAN';
BEARING: 'BEARING';
ORDER: 'ORDER';

// Unit Keywords
UNIT                    : 'UNIT';
LENGTHUNIT              : 'LENGTHUNIT';
ANGLEUNIT               : 'ANGLEUNIT';
SCALEUNIT               : 'SCALEUNIT';
PARAMUNIT               : 'PARAMUNIT';
TIMEUNIT                : 'TIMEUNIT';

// Operation/Conversion Keywords
CONVERSION              : 'CONVERSION';
DERIVINGCONVERSION      : 'DERIVINGCONVERSION';
OPERATION               : 'OPERATION';
METHOD                  : 'METHOD';
PROJECTION: 'PROJECTION';
PARAMETER               : 'PARAMETER';
PARAMETERFILE           : 'PARAMETERFILE';
POINTMOTIONOPERATION    : 'POINTMOTIONOPERATION';
CONCATENATEDOPERATION   : 'CONCATENATEDOPERATION';
STEP                    : 'STEP';

// CRS/Operation Metadata Keywords
ID                      : 'ID';
CITATION                : 'CITATION';
URI                     : 'URI';
UTC                     : 'Z';
SCOPE                   : 'SCOPE';
EXTENT                  : 'EXTENT';
REMARK                  : 'REMARK';
AREA                    : 'AREA';
BBOX                    : 'BBOX';
VERTICALEXTENT          : 'VERTICALEXTENT';
VERTICALCRS             : 'VERTICALCRS';
TIMEEXTENT              : 'TIMEEXTENT';
TIMEPOINT               : 'TIMEPOINT';
COORDEPOCH              : 'COORDEPOCH';
EPOCH              : 'EPOCH';
COORDINATEMETADATA      : 'COORDINATEMETADATA';
TEMPORALQUANTITY        : 'TEMPORALQUANTITY';
ACCURACY                : 'ACCURACY';
// T O W G S 8 4 is deprecated but included for WKT1 backward compatibility.
TOWGS84                 : 'TOWGS84';

// Target/Source CRS Keywords
SOURCECRS               : 'SOURCECRS';
TARGETCRS               : 'TARGETCRS';
BOUNDCRS                : 'BOUNDCRS';
ABRIDGEDTRANSFORMATION  : 'ABRIDGEDTRANSFORMATION';

// ----------------------------------------------------------------------
// 3. Numbers and Literals
// ----------------------------------------------------------------------

// Defines a valid WKT number:
// [+-]? ([0-9]+ | [0-9]* '.' [0-9]+) ([Ee] [+-]? [0-9]+)?
NUMBER : SIGN? ( DIGIT+ ( '.' DIGIT* )? | '.' DIGIT+ ) EXPONENT?;
INTEGER : DIGIT+;

fragment SIGN : [+-];
fragment DIGIT : [0-9];
fragment EXPONENT : [eE] SIGN? DIGIT+;

// ----------------------------------------------------------------------
// 4. Quoted Text
// ----------------------------------------------------------------------

// Quoted text starts with a double quote, contains characters other than double
// quote, or an escaped double quote (""), and ends with a double quote.
QUOTED_TEXT : '"' ( NONDOUBLEQUOTE_CHAR | DOUBLEQUOTE_SYMBOL )* '"';

fragment NONDOUBLEQUOTE_CHAR : ~["\n\r];
fragment DOUBLEQUOTE_SYMBOL : '""';

// ----------------------------------------------------------------------
// 5. Ignored Characters (Whitespace)
// ----------------------------------------------------------------------

// Per Section 6.1, whitespace outside of double quotes is ignored by parsers.
WS : [ \t\r\n]+ -> skip;