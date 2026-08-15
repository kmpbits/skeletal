package io.github.kmpbits.skeletal

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertNull

class SkeletonScopeTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun defaultLocalSkeletonScopeIsNullWithNoProvider() = runComposeUiTest {
        var scope: SkeletonScope? = SkeletonScope(
            loading = true,
            shimmerPhase = androidx.compose.runtime.mutableStateOf(0f),
            shimmerColors = emptyList(),
            cornerRadius = androidx.compose.ui.unit.Dp.Unspecified,
        )
        setContent {
            MaterialTheme {
                scope = LocalSkeletonScope.current
            }
        }
        assertNull(scope)
    }
}
