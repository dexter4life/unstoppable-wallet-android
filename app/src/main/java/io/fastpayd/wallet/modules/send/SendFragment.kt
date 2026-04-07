package io.fastpayd.wallet.modules.send

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import io.fastpayd.wallet.R
import io.fastpayd.wallet.core.BaseFragment
import io.fastpayd.wallet.core.requireInput
import io.fastpayd.wallet.entities.Address
import io.fastpayd.wallet.entities.Wallet
import io.fastpayd.wallet.ui.compose.ComposeAppTheme
import io.fastpayd.wallet.ui.compose.components.AppBar
import io.fastpayd.wallet.ui.compose.components.HsBackButton
import io.fastpayd.wallet.ui.compose.components.MenuItem
import io.fastpayd.wallet.ui.compose.components.ScreenMessageWithAction
import io.fastpayd.wallet.ui.compose.components.ButtonPrimaryYellow
import io.fastpayd.wallet.ui.compose.TranslatableString
import io.horizontalsystems.core.findNavController
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

class SendFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
            )
            try {
                val navController = findNavController()
                val input = navController.requireInput<Input>()
                val wallet = input.wallet
                val title = input.title
                setContent {
                    UnsupportedSendScreen(
                        title = title,
                        coinCode = wallet.coin.code,
                        onBack = { findNavController().popBackStack() }
                    )
                }
            } catch (t: Throwable) {
                findNavController().popBackStack()
            }
        }
    }

    @Parcelize
    data class Input(
        val wallet: Wallet,
        val title: String,
        val sendEntryPointDestId: Int,
        val address: Address,
        val riskyAddress: Boolean = false,
        val amount: BigDecimal? = null,
        val hideAddress: Boolean = false
    ) : Parcelable
}

@androidx.compose.runtime.Composable
private fun UnsupportedSendScreen(
    title: String,
    coinCode: String,
    onBack: () -> Unit,
) {
    Scaffold(
        backgroundColor = ComposeAppTheme.colors.tyler,
        topBar = {
            AppBar(
                title = title,
                navigationIcon = {
                    HsBackButton(onClick = onBack)
                },
                menuItems = listOf(
                    MenuItem(
                        title = TranslatableString.ResString(R.string.Button_Close),
                        onClick = onBack,
                        tint = ComposeAppTheme.colors.jacob
                    )
                )
            )
        }
    ) { paddingValues ->
        ScreenMessageWithAction(
            text = stringResource(R.string.Send_Placeholder_Message, coinCode),
            icon = R.drawable.ic_sync_error,
            paddingValues = paddingValues,
        ) {
            ButtonPrimaryYellow(
                modifier = androidx.compose.ui.Modifier
                    .padding(horizontal = 48.dp)
                    .fillMaxWidth(),
                title = stringResource(R.string.Button_Close),
                onClick = onBack
            )
        }
    }
}
