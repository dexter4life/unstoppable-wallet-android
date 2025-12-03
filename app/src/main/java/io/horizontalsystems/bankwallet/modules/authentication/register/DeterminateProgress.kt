package io.horizontalsystems.bankwallet.modules.authentication.register

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp


@Composable
fun DeterminateProgress(viewModel: RegisterActivityModel) {
    LaunchedEffect(Unit) {
//        while (currentProgress < 1f) {
//            delay(100) // update every 0.1 seconds
//            currentProgress += 0.01f
//        }
    }

    LinearProgressIndicator(
        progress = { viewModel.currentProgress.value },
        modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
            .clip(RectangleShape),
        trackColor = ProgressIndicatorDefaults.linearTrackColor,
        strokeCap = StrokeCap.Butt,
    )
}