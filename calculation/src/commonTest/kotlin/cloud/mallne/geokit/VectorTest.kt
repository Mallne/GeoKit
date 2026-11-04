package cloud.mallne.geokit

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class VectorTest {
    @Test
    fun equalsReturnsFalseForNullAndOtherTypes() {
        val vector = Vector(Vertex(1.0, 2.0), doubleArrayOf(3.0, 4.0))

        assertFalse(vector.equals(null))
        assertFalse(vector.equals("vector"))
    }

    @Test
    fun equalsComparesVectorValuesAndHashCodesConsistently() {
        val vector = Vector(Vertex(1.0, 2.0), doubleArrayOf(3.0, 4.0))
        val equalVector = Vector(Vertex(1.0, 2.0), doubleArrayOf(3.0, 4.0))
        val differentVector = Vector(Vertex(1.0, 2.0), doubleArrayOf(3.0, 5.0))

        assertTrue(vector == equalVector)
        assertEquals(vector.hashCode(), equalVector.hashCode())
        assertFalse(vector == differentVector)
    }
}
