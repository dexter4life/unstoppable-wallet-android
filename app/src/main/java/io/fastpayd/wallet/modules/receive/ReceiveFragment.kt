package io.fastpayd.wallet.modules.receive

import android.os.Parcelable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.fastpayd.wallet.R
import io.fastpayd.wallet.core.BaseComposeFragment
import io.fastpayd.wallet.core.slideFromRight
import io.fastpayd.wallet.entities.Wallet
import io.fastpayd.wallet.modules.receive.ui.ReceiveAddressScreen
import io.fastpayd.wallet.modules.receive.ui.UsedAddressesParams
import io.fastpayd.wallet.modules.receive.viewmodels.ReceiveAddressViewModel
import io.fastpayd.wallet.ui.compose.ComposeAppTheme
import io.fastpayd.wallet.ui.compose.components.HsDivider
import io.fastpayd.wallet.ui.compose.components.RowUniversal
import io.fastpayd.wallet.ui.compose.components.subhead2_grey
import io.horizontalsystems.marketkit.models.BlockchainType
import kotlinx.parcelize.Parcelize

class ReceiveFragment : BaseComposeFragment() {

    @Composable
    override fun GetContent(navController: NavController) {
        withInput<Input>(navController) {
            val wallet = it.wallet
            ReceiveScreen(navController, wallet, it.receiveEntryPointDestId)
        }
    }

    @Parcelize
    data class Input(val wallet: Wallet, val receiveEntryPointDestId: Int = 0) : Parcelable

}

@Composable
fun ReceiveScreen(navController: NavController, wallet: Wallet, receiveEntryPointDestId: Int) {
    val addressViewModel = viewModel<ReceiveAddressViewModel>(factory = ReceiveModule.Factory(wallet))

    val uiState = addressViewModel.uiState
    ReceiveAddressScreen(
        title = stringResource(R.string.Deposit_Title, wallet.coin.code),
        uiState = uiState,
        setAmount = { amount -> addressViewModel.setAmount(amount) },
        onErrorClick = { addressViewModel.onErrorClick() },
        slot1 = {
            if (uiState.usedAddresses.isNotEmpty()) {
                HsDivider(modifier = Modifier.fillMaxWidth())
                RowUniversal(
                    modifier = Modifier.height(48.dp),
                    onClick = {
                        navController.slideFromRight(
                            R.id.btcUsedAddressesFragment,
                            UsedAddressesParams(
                                wallet.coin.name,
                                uiState.usedAddresses,
                                uiState.usedChangeAddresses
                            )
                        )
                    }
                ) {
                    subhead2_grey(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .weight(1f),
                        text = stringResource(R.string.Balance_Receive_UsedAddresses),
                    )

                    Icon(
                        modifier = Modifier.padding(end = 16.dp),
                        painter = painterResource(id = R.drawable.ic_arrow_right),
                        contentDescription = null,
                        tint = ComposeAppTheme.colors.grey
                    )
                }
            }
        },
        onBackPress = { navController.popBackStack() },
        closeModule = {
            if (receiveEntryPointDestId == 0) {
                navController.popBackStack()
            } else {
                navController.popBackStack(receiveEntryPointDestId, true)
            }
        }
    )
}
