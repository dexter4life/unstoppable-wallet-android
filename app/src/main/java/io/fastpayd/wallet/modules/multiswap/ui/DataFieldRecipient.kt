package io.fastpayd.wallet.modules.multiswap.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import io.fastpayd.wallet.R
import io.fastpayd.wallet.entities.Address
import io.fastpayd.wallet.modules.multiswap.QuoteInfoRow
import io.fastpayd.wallet.ui.compose.components.subhead2_grey
import io.fastpayd.wallet.ui.compose.components.subhead2_leah

data class DataFieldRecipient(val address: Address) : DataField {
    @Composable
    override fun GetContent(navController: NavController, borderTop: Boolean) {
        QuoteInfoRow(
            borderTop = borderTop,
            title = {
                subhead2_grey(text = stringResource(R.string.Swap_Recipient))
            },
            value = {
                subhead2_leah(
                    text = address.hex,
                    textAlign = TextAlign.End
                )
            }
        )
    }
}
