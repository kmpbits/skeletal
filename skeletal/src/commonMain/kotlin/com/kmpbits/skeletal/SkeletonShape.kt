package com.kmpbits.skeletal

import androidx.compose.ui.unit.Dp

/**
 * The placeholder shape a `.skeleton()` element draws while loading.
 */
sealed interface SkeletonShape {

    /** Rounded rectangle matching the element's own measured bounds, using [SkeletonDefaults.cornerRadius]. */
    data object Auto : SkeletonShape

    /** Circle inscribed in the element's own measured bounds. */
    data object Circle : SkeletonShape

    /** Rounded rectangle matching the element's own measured bounds with a custom corner radius. */
    data class RoundedCorner(val radius: Dp) : SkeletonShape
}
