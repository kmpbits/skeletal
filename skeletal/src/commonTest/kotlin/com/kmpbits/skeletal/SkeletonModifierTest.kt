package com.kmpbits.skeletal

import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
import kotlin.test.Test

class SkeletonModifierTest {

    private fun hasSkeletonLoading(expected: Boolean) = SemanticsMatcher(
        "SkeletalLoading = $expected"
    ) { node ->
        node.config.getOrElseNullable(SkeletonLoadingKey) { null } == expected
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun outsideContainerSkeletonIsNoOpAndContentRenders() = runComposeUiTest {
        setContent {
            MaterialTheme {
                Text(
                    text = "hello",
                    modifier = Modifier
                        .testTag(SkeletonTestTags.SKELETON)
                        .skeleton(),
                )
            }
        }
        onNodeWithTag(SkeletonTestTags.SKELETON).assert(hasSkeletonLoading(false))
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun insideLoadingContainerSemanticsReportsLoadingTrue() = runComposeUiTest {
        setContent {
            MaterialTheme {
                SkeletonContainer(loading = true) {
                    Text(
                        text = "hello",
                        modifier = Modifier
                            .testTag(SkeletonTestTags.SKELETON)
                            .skeleton(),
                    )
                }
            }
        }
        onNodeWithTag(SkeletonTestTags.SKELETON).assert(hasSkeletonLoading(true))
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun insideNonLoadingContainerSemanticsReportsLoadingFalse() = runComposeUiTest {
        setContent {
            MaterialTheme {
                SkeletonContainer(loading = false) {
                    Text(
                        text = "hello",
                        modifier = Modifier
                            .testTag(SkeletonTestTags.SKELETON)
                            .skeleton(),
                    )
                }
            }
        }
        onNodeWithTag(SkeletonTestTags.SKELETON).assert(hasSkeletonLoading(false))
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allShapeVariantsRenderAndReportLoading() = runComposeUiTest {
        setContent {
            MaterialTheme {
                SkeletonContainer(loading = true) {
                    Text(
                        "a",
                        modifier = Modifier.size(40.dp).testTag("auto").skeleton(SkeletonShape.Auto),
                    )
                    Text(
                        "b",
                        modifier = Modifier.size(40.dp).testTag("circle")
                            .skeleton(SkeletonShape.Circle),
                    )
                    Text(
                        "c",
                        modifier = Modifier.size(40.dp).testTag("rounded")
                            .skeleton(SkeletonShape.RoundedCorner(12.dp)),
                    )
                }
            }
        }

        // Verifies each shape variant composes, is measured, and correctly reports the
        // container's loading state — not pixel-level draw correctness, which is a visual
        // concern outside the scope of this unit test suite.
        onNodeWithTag("auto").assertExists().assert(hasSkeletonLoading(true))
        onNodeWithTag("circle").assertExists().assert(hasSkeletonLoading(true))
        onNodeWithTag("rounded").assertExists().assert(hasSkeletonLoading(true))
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun revealCrossfadesSemanticsEventuallyToFalse() = runComposeUiTest {
        var loading by mutableStateOf(true)
        setContent {
            MaterialTheme {
                SkeletonContainer(loading = loading) {
                    Text(
                        text = "hello",
                        modifier = Modifier
                            .testTag(SkeletonTestTags.SKELETON)
                            .skeleton(),
                    )
                }
            }
        }
        onNodeWithTag(SkeletonTestTags.SKELETON).assert(hasSkeletonLoading(true))

        loading = false
        mainClock.advanceTimeBy(300L)
        waitForIdle()

        onNodeWithTag(SkeletonTestTags.SKELETON).assert(hasSkeletonLoading(false))
    }
}
