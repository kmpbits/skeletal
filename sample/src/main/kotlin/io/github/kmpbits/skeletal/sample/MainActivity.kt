package io.github.kmpbits.skeletal.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

private const val SIMULATED_LOAD_DELAY_MILLIS = 2500L

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                SampleScreen()
            }
        }
    }
}

@Composable
private fun SampleScreen() {
    var loading by remember { mutableStateOf(true) }
    var reloadKey by remember { mutableIntStateOf(0) }
    var stateDrivenState by remember {
        mutableStateOf<SampleState<SamplePost>>(SampleState.Loading)
    }

    LaunchedEffect(reloadKey) {
        loading = true
        stateDrivenState = SampleState.Loading
        delay(SIMULATED_LOAD_DELAY_MILLIS)
        loading = false
        // Alternates Success/Failure on each reload, so the "Reload" button exercises all
        // three SampleState cases without needing a separate control.
        stateDrivenState = if (reloadKey % 2 == 0) {
            SampleState.Success(samplePosts.first())
        } else {
            SampleState.Failure("Couldn't refresh this post")
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { reloadKey++ }) {
                Text("Reload")
            }
        },
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            item {
                StateDrivenPostCard(state = stateDrivenState)
            }
            items(samplePosts) { post ->
                PostCard(post = if (loading) null else post)
            }
        }
    }
}
