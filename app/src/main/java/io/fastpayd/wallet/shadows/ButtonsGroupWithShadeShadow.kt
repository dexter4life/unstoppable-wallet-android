package io.fastpayd.wallet.modules.evmfee

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import io.fastpayd.wallet.ui.compose.ComposeAppTheme

@Composable
fun ButtonsGroupWithShade(
    ButtonsContent: @Composable (() -> Unit)
) {
    Column(
        modifier = Modifier
            .offset(y = -(24.dp))
            .navigationBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .background(
                    brush = Brush.verticalGradient(
                        listOf(ComposeAppTheme.colors.transparent, ComposeAppTheme.colors.tyler)
                    )
                )
        )
        Box(modifier = Modifier.background(ComposeAppTheme.colors.tyler)) {
            ButtonsContent()
        }
    }
}