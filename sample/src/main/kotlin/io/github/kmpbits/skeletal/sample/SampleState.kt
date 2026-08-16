package io.github.kmpbits.skeletal.sample

/**
 * Minimal Loading/Success/Failure state used by [StateDrivenPostCard] to demonstrate
 * [io.github.kmpbits.skeletal.SkeletonContainer]'s state-driven overload.
 */
sealed interface SampleState<out T> {
    data object Loading : SampleState<Nothing>
    data class Success<T>(val data: T) : SampleState<T>
    data class Failure(val message: String) : SampleState<Nothing>
}
