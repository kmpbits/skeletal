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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

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
    val transition = rememberInfiniteTransition(label = "skeletal-shimmer")
    val shimmerPhase = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "skeletal-shimmer-phase",
    )

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
