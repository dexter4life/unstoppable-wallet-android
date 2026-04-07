package io.fastpayd.wallet.modules.sendtokenselect

import android.os.Parcelable
import io.horizontalsystems.marketkit.models.BlockchainType
import io.horizontalsystems.marketkit.models.TokenType
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

class SendTokenSelectFragment {
    @Parcelize
    data class Input(
        val blockchainTypes: List<BlockchainType>? = null,
        val tokenTypes: List<TokenType>? = null,
        val address: String? = null,
        val amount: BigDecimal? = null,
    ) : Parcelable
}