package io.fastpayd.wallet.modules.info

import android.os.Parcelable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.fastpayd.wallet.R
import io.fastpayd.wallet.core.BaseComposeFragment
import io.fastpayd.wallet.modules.info.ui.InfoHeader
import io.fastpayd.wallet.ui.compose.ComposeAppTheme
import io.fastpayd.wallet.ui.compose.TranslatableString
import io.fastpayd.wallet.ui.compose.components.AppBar
import io.fastpayd.wallet.ui.compose.components.InfoTextBody
import io.fastpayd.wallet.ui.compose.components.MenuItem
import kotlinx.parcelize.Parcelize

class TransactionLockTimeInfoFragment : BaseComposeFragment() {

    @Composable
    override fun GetContent(navController: NavController) {
        withInput<Input>(navController) { input ->
            InfoScreen(input.lockTime, navController)
        }
    }

    @Parcelize
    data class Input(val lockTime: String) : Parcelable
}

@Composable
private fun InfoScreen(
    lockDate: String,
    navController: NavController
) {

    val description = stringResource(R.string.Info_LockTime_Description, lockDate)

    Surface(color = ComposeAppTheme.colors.tyler) {
        Column {
            AppBar(
                menuItems = listOf(
                    MenuItem(
                        title = TranslatableString.ResString(R.string.Button_Close),
                        icon = R.drawable.ic_close,
                        onClick = { navController.popBackStack() }
                    )
                )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                InfoHeader(R.string.Info_LockTime_Title)
                InfoTextBody(description)
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}
