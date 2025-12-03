package io.horizontalsystems.bankwallet.modules.authentication.register


import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme

@Composable
fun RegisterView(
    onBackClick: (() -> Unit)? = null,
    nightMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = if (nightMode) ComposeAppTheme.colors.dark700 else ComposeAppTheme.colors.gray200)
            .padding(top = 30.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 13.dp)
                    .height(50.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onBackClick?.invoke() },
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(
                            if (nightMode) ComposeAppTheme.colors.dark500
                            else ComposeAppTheme.colors.gray100
                        )
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        tint = if (nightMode) ComposeAppTheme.colors.gray200
                        else ComposeAppTheme.colors.dark,
                        contentDescription = null
                    )
                }
            }
            content()
        }
    }
}

