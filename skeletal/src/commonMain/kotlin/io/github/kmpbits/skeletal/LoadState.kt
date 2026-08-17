package io.github.kmpbits.skeletal

/**
 * A minimal loading/success/failure state, typed on both the success payload [T] and the
 * failure payload [F]. Feeding this into [SkeletonContainer] gives its `onFailure` slot a
 * concretely typed [Failure.reason] instead of the caller having to re-derive it from an
 * arbitrary state type — see the `isFailure`/`dataOrNull`-based overload for that flexibility
 * traded away here.
 */
sealed interface LoadState<out T, out F> {
    data object Loading : LoadState<Nothing, Nothing>
    data class Success<T>(val data: T) : LoadState<T, Nothing>
    data class Failure<F>(val reason: F) : LoadState<Nothing, F>
}
