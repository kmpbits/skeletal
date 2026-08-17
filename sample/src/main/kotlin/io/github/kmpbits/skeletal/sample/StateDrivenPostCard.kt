package io.github.kmpbits.skeletal.sample

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.kmpbits.skeletal.SkeletonContainer

/**
 * Same card as [PostCard], but driven by a [SampleState] instead of a nullable [SamplePost]
 * plus a plain [Boolean] — demonstrating [SkeletonContainer]'s state-driven overload, including
 * its dedicated `onFailure` slot.
 */
@Composable
fun StateDrivenPostCard(state: SampleState<SamplePost>) {
    SkeletonContainer(
        state = state,
        dataOrNull = { (it as? SampleState.Success)?.data },
        isFailure = { it is SampleState.Failure },
        onFailure = { failure -> FailureCard((failure as SampleState.Failure).message) },
    ) { post ->
        PostCardBody(post)
    }
}

@Composable
private fun FailureCard(message: String) {
    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(16.dp),
        )
    }
}
