package com.kmpbits.skeletal

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals

class SkeletonDefaultsTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun cornerRadiusDefaultsTo4Dp() = runComposeUiTest {
        var radius: androidx.compose.ui.unit.Dp? = null
        setContent {
            MaterialTheme {
                radius = SkeletonDefaults.cornerRadius
            }
        }
        assertEquals(4.dp, radius)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun shimmerColorsHasThreeStopsDerivedFromColorScheme() = runComposeUiTest {
        var colors: List<androidx.compose.ui.graphics.Color>? = null
        var surfaceVariant: androidx.compose.ui.graphics.Color? = null
        var surface: androidx.compose.ui.graphics.Color? = null
        setContent {
            MaterialTheme {
                colors = SkeletonDefaults.shimmerColors
                surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
                surface = MaterialTheme.colorScheme.surface
            }
        }
        assertEquals(listOf(surfaceVariant!!, surface!!, surfaceVariant!!), colors)
    }

    @Test
    fun crossfadeSpecIs180Milliseconds() {
        val spec = SkeletonDefaults.crossfadeSpec as androidx.compose.animation.core.TweenSpec<Float>
        assertEquals(180, spec.durationMillis)
    }
}
