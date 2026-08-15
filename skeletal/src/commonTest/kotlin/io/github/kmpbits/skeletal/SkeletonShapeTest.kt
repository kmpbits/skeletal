package io.github.kmpbits.skeletal

import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class SkeletonShapeTest {

    @Test
    fun autoIsASingleton() {
        assertEquals(SkeletonShape.Auto, SkeletonShape.Auto)
    }

    @Test
    fun circleIsASingleton() {
        assertEquals(SkeletonShape.Circle, SkeletonShape.Circle)
    }

    @Test
    fun roundedCornerEqualityIsByRadius() {
        assertEquals(SkeletonShape.RoundedCorner(8.dp), SkeletonShape.RoundedCorner(8.dp))
        assertNotEquals(SkeletonShape.RoundedCorner(8.dp), SkeletonShape.RoundedCorner(12.dp))
    }

    @Test
    fun differentShapeTypesAreNotEqual() {
        assertNotEquals<SkeletonShape>(SkeletonShape.Auto, SkeletonShape.Circle)
    }
}
