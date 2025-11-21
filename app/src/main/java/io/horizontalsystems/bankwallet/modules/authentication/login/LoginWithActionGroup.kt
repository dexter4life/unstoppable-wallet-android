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
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import io.horizontalsystems.bankwallet.ui.compose.components.ButtonPrimary
import io.horizontalsystems.bankwallet.ui.compose.components.ButtonPrimaryDefaults
import io.horizontalsystems.bankwallet.ui.compose.components.HSpacer

@Composable
fun LoginWithActionGroup(viewModel: LoginActivityModel){
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 30.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
     ) {
        Text(
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth(),
            fontWeight = FontWeight.Medium,
            text = stringResource(R.string.LoginWithAction_Title),
            textAlign = TextAlign.Center,
            color = colorResource(R.color.leah)
        )
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
                           width = 2.dp,
                           color = ComposeAppTheme.colors.lightGrey
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
               Spacer(Modifier.height(10.dp))
               ButtonPrimary(
                   modifier = Modifier.fillMaxWidth(),
                   onClick = { viewModel.onPasskeyLoginClick() },
                   border = BorderStroke(
                       width = 2.dp,
                       color = ComposeAppTheme.colors.andy
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
                               tint = ComposeAppTheme.colors.leah,
                               contentDescription = stringResource(R.string.Button_PassKey_Description)
                           )
                       }
                       HSpacer(20.dp)
                       Text(
                           maxLines = 1,
                           overflow = TextOverflow.Ellipsis,
                           text = stringResource(R.string.Button_Passkey),
                           fontSize = 18.sp,
                           fontWeight = FontWeight.Bold
                       )
                   },
                   enabled = true
               )
       }
    }
}