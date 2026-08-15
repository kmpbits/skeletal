package io.github.kmpbits.skeletal

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test

class SmokeTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun composeMultiplatformSetupWorks() = runComposeUiTest {
        setContent {
            MaterialTheme {
                Text(LIBRARY_NAME)
            }
        }

        onNodeWithText(LIBRARY_NAME).assertExists()
    }
}
