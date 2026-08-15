package io.github.kmpbits.skeletal

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SkeletonContainerTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun loadingTruePropagatesToScope() = runComposeUiTest {
        var scope: SkeletonScope? = null
        setContent {
            MaterialTheme {
                SkeletonContainer(loading = true) {
                    scope = LocalSkeletonScope.current
                }
            }
        }
        assertEquals(true, scope?.loading)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun loadingFalsePropagatesToScope() = runComposeUiTest {
        var scope: SkeletonScope? = null
        setContent {
            MaterialTheme {
                SkeletonContainer(loading = false) {
                    scope = LocalSkeletonScope.current
                }
            }
        }
        assertEquals(false, scope?.loading)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun customColorsAndRadiusOverrideDefaults() = runComposeUiTest {
        var scope: SkeletonScope? = null
        val customColors = listOf(Color.Red, Color.Blue)
        val customRadius = 12.dp
        setContent {
            MaterialTheme {
                SkeletonContainer(
                    loading = true,
                    shimmerColors = customColors,
                    cornerRadius = customRadius,
                ) {
                    scope = LocalSkeletonScope.current
                }
            }
        }
        assertEquals(customColors, scope?.shimmerColors)
        assertEquals(customRadius, scope?.cornerRadius)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun shimmerPhaseIsProvided() = runComposeUiTest {
        var scope: SkeletonScope? = null
        setContent {
            MaterialTheme {
                SkeletonContainer(loading = true) {
                    scope = LocalSkeletonScope.current
                }
            }
        }
        assertNotNull(scope?.shimmerPhase)
    }
}
