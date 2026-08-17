package io.github.kmpbits.skeletal

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

/**
 * Resolves [state] into the [loading]-driven overload above. [isFailure] is checked first — if
 * true, only [onFailure] is composed with [state] and [content] is skipped entirely (no shimmer
 * is shown either). Otherwise [dataOrNull] determines both [content]'s argument and whether the
 * shimmer is shown (`loading = data == null`), unchanged from the plain [Boolean] overload's
 * behavior.
 */
@Composable
fun <S, T> SkeletonContainer(
    state: S,
    dataOrNull: (S) -> T?,
    isFailure: (S) -> Boolean,
    modifier: Modifier = Modifier,
    shimmerColors: List<Color> = SkeletonDefaults.shimmerColors,
    cornerRadius: Dp = SkeletonDefaults.cornerRadius,
    onFailure: @Composable (S) -> Unit,
    content: @Composable (T?) -> Unit,
) {
    if (isFailure(state)) {
        onFailure(state)
        return
    }
    val data = dataOrNull(state)
    SkeletonContainer(
        loading = data == null,
        modifier = modifier,
        shimmerColors = shimmerColors,
        cornerRadius = cornerRadius,
    ) {
        content(data)
    }
}

/**
 * Resolves a [LoadState] into the [loading]-driven overload above. Unlike the `isFailure`/
 * `dataOrNull` overload, [state]'s shape is fixed to [LoadState] in exchange for [onFailure]
 * receiving a concretely typed [F] — the [LoadState.Failure.reason] — instead of the raw state.
 */
@Composable
fun <T, F> SkeletonContainer(
    state: LoadState<T, F>,
    modifier: Modifier = Modifier,
    shimmerColors: List<Color> = SkeletonDefaults.shimmerColors,
    cornerRadius: Dp = SkeletonDefaults.cornerRadius,
    onFailure: @Composable (F) -> Unit,
    content: @Composable (T?) -> Unit,
) {
    if (state is LoadState.Failure) {
        onFailure(state.reason)
        return
    }
    val data = (state as? LoadState.Success)?.data
    SkeletonContainer(
        loading = data == null,
        modifier = modifier,
        shimmerColors = shimmerColors,
        cornerRadius = cornerRadius,
    ) {
        content(data)
    }
}
