package io.github.kmpbits.skeletal

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Default values used by [SkeletonContainer] when the caller doesn't override them. */
object SkeletonDefaults {

    /** Corner radius used by [SkeletonShape.Auto] and as the container-wide default. */
    val cornerRadius: Dp
        @Composable get() = 4.dp

    /** Three-stop shimmer gradient derived from the current [MaterialTheme.colorScheme]. */
    val shimmerColors: List<Color>
        @Composable get() = listOf(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.surfaceVariant,
        )

    /** Duration/easing used to crossfade between shimmer and revealed content. */
    val crossfadeSpec: AnimationSpec<Float> = tween(durationMillis = 180)
}
