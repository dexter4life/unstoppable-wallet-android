package io.horizontalsystems.bankwallet.modules.authentication.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.modules.authentication.AuthActivityModel
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import io.horizontalsystems.bankwallet.ui.compose.components.ButtonPrimary
import io.horizontalsystems.bankwallet.ui.compose.components.ButtonPrimaryDefaults
import io.horizontalsystems.bankwallet.ui.compose.components.HSpacer

@Composable
fun LabeledDivider(label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Divider(
            modifier = Modifier
                .weight(1f)
                .height(1.dp),
            color = ComposeAppTheme.colors.dark100
        )

        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 8.dp),
            color = ComposeAppTheme.colors.dark100
        )

        Divider(
            modifier = Modifier
                .weight(1f)
                .height(1.dp),
            color = ComposeAppTheme.colors.dark100
        )
    }
}

@Composable
fun LoginWithActionGroup(viewModel: AuthActivityModel, nightMode: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 30.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        LabeledDivider(stringResource(R.string.LoginWithAction_Title))
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            )
            {
                for ((resource, contentDescription, onClick) in viewModel.quickLoginButtons) {
                    ButtonPrimary(
                        modifier = Modifier.weight(1f),
                        onClick = onClick,
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (nightMode) ComposeAppTheme.colors.dark500
                            else ComposeAppTheme.colors.dark300
                        ),
                        buttonColors = ButtonPrimaryDefaults.textButtonColors(
                            backgroundColor = ComposeAppTheme.colors.transparent,
                            contentColor = ComposeAppTheme.colors.leah,
                            disabledBackgroundColor = ComposeAppTheme.colors.blade,
                            disabledContentColor = ComposeAppTheme.colors.andy,
                        ),
                        content = {
                            Image(
                                painterResource(resource),
                                modifier = Modifier.height(28.dp),
                                contentDescription = contentDescription
                            )
                        },
                        enabled = true
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            ButtonPrimary(
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.onPasskeyLoginClick() },
                border = BorderStroke(
                    width = 1.dp,
                    color = if (nightMode) ComposeAppTheme.colors.dark500
                    else ComposeAppTheme.colors.dark300
                ),
                buttonColors = ButtonPrimaryDefaults.textButtonColors(
                    backgroundColor = ComposeAppTheme.colors.transparent,
                    contentColor = ComposeAppTheme.colors.leah,
                    disabledBackgroundColor = ComposeAppTheme.colors.blade,
                    disabledContentColor = ComposeAppTheme.colors.andy,
                ),
                content = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painterResource(R.drawable.passkey_24px),
                            tint = if (nightMode) ComposeAppTheme.colors.gray200 else ComposeAppTheme.colors.dark,
                            contentDescription = stringResource(R.string.Button_PassKey_Description)
                        )
                    }
                    HSpacer(20.dp)
                    Text(
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        text = stringResource(R.string.Button_Passkey),
                        fontSize = 18.sp,
                        color = if(nightMode) ComposeAppTheme.colors.white else ComposeAppTheme.colors.dark,
                        fontWeight = FontWeight.Bold
                    )
                },
                enabled = true
            )
        }
    }
}