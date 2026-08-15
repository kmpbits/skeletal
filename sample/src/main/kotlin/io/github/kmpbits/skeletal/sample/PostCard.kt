package io.github.kmpbits.skeletal.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.kmpbits.skeletal.SkeletonContainer
import io.github.kmpbits.skeletal.SkeletonShape
import io.github.kmpbits.skeletal.skeleton

/**
 * A feed card demonstrating all three [SkeletonShape] variants: [SkeletonShape.Circle] for the
 * avatar, the default [SkeletonShape.Auto] for the title/subtitle text, and
 * [SkeletonShape.RoundedCorner] for the banner.
 */
@Composable
fun PostCard(post: SamplePost?) {
    SkeletonContainer(loading = post == null) {
        Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .skeleton(shape = SkeletonShape.Circle),
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = post?.title ?: "",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.fillMaxWidth(0.6f).skeleton(),
                        )
                        Text(
                            text = post?.subtitle ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth(0.4f).skeleton(),
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .skeleton(shape = SkeletonShape.RoundedCorner(12.dp)),
                )
            }
        }
    }
}
