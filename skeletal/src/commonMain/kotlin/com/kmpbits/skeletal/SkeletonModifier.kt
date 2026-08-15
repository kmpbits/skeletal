package com.kmpbits.skeletal

import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp

internal object SkeletonTestTags {
    const val SKELETON = "skeletal:skeleton"
}

internal val SkeletonLoadingKey = SemanticsPropertyKey<Boolean>("SkeletalLoading")
internal var SemanticsPropertyReceiver.skeletalLoading by SkeletonLoadingKey

/**
 * Draws a shimmering placeholder over this element while an ancestor [SkeletonContainer] is
 * loading, sized and shaped per [shape]. A no-op (draws real content only) with no ancestor
 * [SkeletonContainer].
 */
fun Modifier.skeleton(shape: SkeletonShape = SkeletonShape.Auto): Modifier = composed {
    val scope = LocalSkeletonScope.current
        ?: return@composed this.semantics { skeletalLoading = false }

    val loading = scope.loading
    val contentAlpha = remember { Animatable(if (loading) 0f else 1f) }

    LaunchedEffect(loading) {
        contentAlpha.animateTo(
            targetValue = if (loading) 0f else 1f,
            animationSpec = SkeletonDefaults.crossfadeSpec,
        )
    }

    // Order matters: drawWithContent must wrap graphicsLayer, not the other way round.
    // graphicsLayer's alpha only scopes what it wraps (the real content, via drawContent()
    // inside it) — the shimmer is drawn by the outer block using its own `alpha` draw
    // param, so it fades independently instead of being dragged down by the same alpha.
    this
        .semantics { skeletalLoading = loading }
        .drawWithContent {
            drawContent()
            val shimmerAlpha = 1f - contentAlpha.value
            if (shimmerAlpha > 0f) {
                drawShimmer(
                    shape = shape,
                    colors = scope.shimmerColors,
                    cornerRadius = scope.cornerRadius,
                    phase = scope.shimmerPhase.value,
                    alpha = shimmerAlpha,
                )
            }
        }
        .graphicsLayer {
            alpha = contentAlpha.value
        }
}

private fun ContentDrawScope.drawShimmer(
    shape: SkeletonShape,
    colors: List<Color>,
    cornerRadius: Dp,
    phase: Float,
    alpha: Float,
) {
    val travel = size.width + size.height
    val offset = -size.width + phase * (travel + size.width)
    val brush = Brush.linearGradient(
        colors = colors,
        start = Offset(offset, offset),
        end = Offset(offset + size.width, offset + size.height),
    )

    when (shape) {
        SkeletonShape.Auto -> drawRoundRect(
            brush = brush,
            alpha = alpha,
            cornerRadius = CornerRadius(cornerRadius.toPx(), cornerRadius.toPx()),
        )

        SkeletonShape.Circle -> drawCircle(
            brush = brush,
            alpha = alpha,
            radius = minOf(size.width, size.height) / 2f,
            center = Offset(size.width / 2f, size.height / 2f),
        )

        is SkeletonShape.RoundedCorner -> drawRoundRect(
            brush = brush,
            alpha = alpha,
            cornerRadius = CornerRadius(
                shape.radius.toPx(),
                shape.radius.toPx(),
            ),
        )
    }
}
