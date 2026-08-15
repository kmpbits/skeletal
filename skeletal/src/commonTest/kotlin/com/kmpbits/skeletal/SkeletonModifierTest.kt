package com.kmpbits.skeletal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

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

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun shimmerPaintsOverElementCenterWhileLoadingAndRevealChangesIt() = runComposeUiTest {
        val backgroundColor = Color.Black
        val shimmerColor = Color.White
        var loading by mutableStateOf(true)

        setContent {
            MaterialTheme {
                Box(Modifier.background(backgroundColor)) {
                    SkeletonContainer(
                        loading = loading,
                        // A solid, theme-independent color: any pixel painted by the shimmer is
                        // guaranteed distinguishable from both the backdrop and any real Material
                        // theme background/surface color, so the assertions below can't pass by
                        // coincidence.
                        shimmerColors = listOf(shimmerColor, shimmerColor),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .testTag(SkeletonTestTags.SKELETON)
                                .skeleton(),
                        )
                    }
                }
            }
        }
        waitForIdle()

        val loadingCenterPixel = centerPixelOf(SkeletonTestTags.SKELETON)
        assertNotEquals(
            backgroundColor,
            loadingCenterPixel,
            "expected the shimmer to paint over the element's center while loading",
        )

        loading = false
        mainClock.advanceTimeBy(400L)
        waitForIdle()

        val loadedCenterPixel = centerPixelOf(SkeletonTestTags.SKELETON)
        assertNotEquals(
            loadingCenterPixel,
            loadedCenterPixel,
            "expected the reveal crossfade to change what's painted at the element's center",
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun circleShapeLeavesBoundingSquareCornerUntouchedUnlikeAutoShape() = runComposeUiTest {
        val backgroundColor = Color.Black
        val shimmerColor = Color.White

        setContent {
            MaterialTheme {
                Box(Modifier.background(backgroundColor)) {
                    SkeletonContainer(
                        loading = true,
                        shimmerColors = listOf(shimmerColor, shimmerColor),
                        // No rounding, so Auto's rounded rect is a full square and its
                        // corner is expected to be painted, unlike Circle's.
                        cornerRadius = 0.dp,
                    ) {
                        // Column, not Box's default overlapping stack: the two elements below
                        // must not visually overlap, or captureToImage on one could pick up
                        // pixels painted by the other.
                        Column {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .testTag("circle")
                                    .skeleton(SkeletonShape.Circle),
                            )
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .testTag("square")
                                    .skeleton(SkeletonShape.Auto),
                            )
                        }
                    }
                }
            }
        }
        waitForIdle()

        assertEquals(
            backgroundColor,
            cornerPixelOf("circle"),
            "expected Circle to leave its bounding square's corner untouched",
        )
        assertNotEquals(
            backgroundColor,
            cornerPixelOf("square"),
            "expected Auto (a full square here) to paint its corner",
        )
    }

    @OptIn(ExperimentalTestApi::class)
    private fun ComposeUiTest.centerPixelOf(tag: String): Color {
        val image = onNodeWithTag(tag).captureToImage()
        val map = image.toPixelMap()
        return map[map.width / 2, map.height / 2]
    }

    @OptIn(ExperimentalTestApi::class)
    private fun ComposeUiTest.cornerPixelOf(tag: String): Color {
        val image = onNodeWithTag(tag).captureToImage()
        val map = image.toPixelMap()
        // 1px in from the true corner to sidestep anti-aliasing at the very edge pixel.
        return map[1, 1]
    }
}
