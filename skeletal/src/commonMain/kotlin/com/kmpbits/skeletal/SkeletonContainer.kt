package com.kmpbits.skeletal

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

/** Duration of one full shimmer sweep. Not public API — see design spec's shimmer animation. */
private const val SHIMMER_PERIOD_MILLIS = 1000

/**
 * Wraps [content], making [loading] and a shared shimmer animation available to every
 * `Modifier.skeleton()` inside it. All placeholders in this subtree animate in sync,
 * sharing a single animation driver instead of one per element.
 */
@Composable
fun SkeletonContainer(
    loading: Boolean,
    modifier: Modifier = Modifier,
    shimmerColors: List<Color> = SkeletonDefaults.shimmerColors,
    cornerRadius: Dp = SkeletonDefaults.cornerRadius,
    content: @Composable () -> Unit,
) {
    // The infinite transition (and the frame-callback loop backing it) is only composed while
    // loading is true. When loading flips to false, this branch leaves composition, which
    // disposes the transition's underlying LaunchedEffect and genuinely stops the clock — rather
    // than leaving it running forever and merely having the draw side ignore its value.
    val shimmerPhase: State<Float> = if (loading) {
        val transition = rememberInfiniteTransition(label = "skeletal-shimmer")
        transition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = SHIMMER_PERIOD_MILLIS, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
            label = "skeletal-shimmer-phase",
        )
    } else {
        remember { mutableStateOf(0f) }
    }

    val scope = remember(loading, shimmerColors, cornerRadius, shimmerPhase) {
        SkeletonScope(
            loading = loading,
            shimmerPhase = shimmerPhase,
            shimmerColors = shimmerColors,
            cornerRadius = cornerRadius,
        )
    }

    CompositionLocalProvider(LocalSkeletonScope provides scope) {
        Box(modifier) {
            content()
        }
    }
}
