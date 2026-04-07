package io.fastpayd.wallet.modules.send

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.navGraphViewModels
import io.fastpayd.wallet.R
import io.fastpayd.wallet.core.BaseComposeFragment
import io.fastpayd.wallet.modules.amount.AmountInputModeViewModel
import io.fastpayd.wallet.modules.send.bitcoin.SendBitcoinConfirmationScreen
import io.fastpayd.wallet.modules.send.bitcoin.SendBitcoinViewModel
import io.fastpayd.wallet.modules.send.solana.SendSolanaConfirmationScreen
import io.fastpayd.wallet.modules.send.solana.SendSolanaViewModel
import io.fastpayd.wallet.modules.send.stellar.SendStellarConfirmationScreen
import io.fastpayd.wallet.modules.send.stellar.SendStellarViewModel
import io.fastpayd.wallet.modules.send.ton.SendTonConfirmationScreen
import io.fastpayd.wallet.modules.send.ton.SendTonViewModel
import io.fastpayd.wallet.modules.send.tron.SendTronConfirmationScreen
import io.fastpayd.wallet.modules.send.tron.SendTronViewModel
import io.fastpayd.wallet.modules.send.zcash.SendZCashConfirmationScreen
import io.fastpayd.wallet.modules.send.zcash.SendZCashViewModel
import kotlinx.parcelize.Parcelize

class SendConfirmationFragment : BaseComposeFragment() {
    val amountInputModeViewModel by navGraphViewModels<AmountInputModeViewModel>(R.id.sendXFragment)

    @Composable
    override fun GetContent(navController: NavController) {
        withInput<Input>(navController) { input ->
            when (input.type) {
                Type.Bitcoin -> {
                    val sendBitcoinViewModel by navGraphViewModels<SendBitcoinViewModel>(R.id.sendXFragment)

                    SendBitcoinConfirmationScreen(
                        navController,
                        sendBitcoinViewModel,
                        amountInputModeViewModel,
                        input.sendEntryPointDestId
                    )
                }

                Type.ZCash -> {
                    val sendZCashViewModel by navGraphViewModels<SendZCashViewModel>(R.id.sendXFragment)

                    SendZCashConfirmationScreen(
                        navController,
                        sendZCashViewModel,
                        amountInputModeViewModel,
                        input.sendEntryPointDestId
                    )
                }

                Type.Tron -> {
                    val sendTronViewModel by navGraphViewModels<SendTronViewModel>(R.id.sendXFragment)
                    SendTronConfirmationScreen(
                        navController,
                        sendTronViewModel,
                        amountInputModeViewModel,
                        input.sendEntryPointDestId
                    )
                }

                Type.Solana -> {
                    val sendSolanaViewModel by navGraphViewModels<SendSolanaViewModel>(R.id.sendXFragment)

                    SendSolanaConfirmationScreen(
                        navController,
                        sendSolanaViewModel,
                        amountInputModeViewModel,
                        input.sendEntryPointDestId
                    )
                }

                Type.Ton -> {
                    val sendTonViewModel by navGraphViewModels<SendTonViewModel>(R.id.sendXFragment)

                    SendTonConfirmationScreen(
                        navController,
                        sendTonViewModel,
                        amountInputModeViewModel,
                        input.sendEntryPointDestId
                    )
                }

                Type.Stellar -> {
                    val sendStellarViewModel by navGraphViewModels<SendStellarViewModel>(R.id.sendXFragment)

                    SendStellarConfirmationScreen(
                        navController,
                        sendStellarViewModel,
                        amountInputModeViewModel,
                        input.sendEntryPointDestId
                    )
                }
            }
        }
    }

    @Parcelize
    enum class Type : Parcelable {
        Bitcoin, ZCash, Solana, Tron, Ton, Stellar
    }

    @Parcelize
    data class Input(val type: Type, val sendEntryPointDestId: Int) : Parcelable
}
