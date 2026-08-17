package io.github.kmpbits.skeletal.sample

import androidx.compose.runtime.Composable
import io.github.kmpbits.skeletal.LoadState
import io.github.kmpbits.skeletal.SkeletonContainer

/**
 * Same card as [StateDrivenPostCard], but driven by [LoadState] instead of the sample's own
 * [SampleState] — demonstrating [SkeletonContainer]'s `LoadState` overload, where `onFailure`
 * gets a concretely typed failure payload with no cast required.
 */
@Composable
fun LoadStatePostCard(state: LoadState<SamplePost, String>) {
    SkeletonContainer(
        state = state,
        onFailure = { reason -> FailureCard(reason) },
    ) { post ->
        PostCardBody(post)
    }
}
