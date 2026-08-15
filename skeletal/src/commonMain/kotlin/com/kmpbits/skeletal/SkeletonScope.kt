package com.kmpbits.skeletal

import androidx.compose.runtime.State
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

/**
 * Loading state and shared shimmer animation provided by an ancestor [SkeletonContainer].
 * `null` means there is no ancestor container, in which case `.skeleton()` is a no-op.
 */
internal class SkeletonScope(
    val loading: Boolean,
    val shimmerPhase: State<Float>,
    val shimmerColors: List<Color>,
    val cornerRadius: Dp,
)

internal val LocalSkeletonScope = compositionLocalOf<SkeletonScope?> { null }
