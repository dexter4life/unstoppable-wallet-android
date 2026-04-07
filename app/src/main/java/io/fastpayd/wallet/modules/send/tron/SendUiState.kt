package io.fastpayd.wallet.modules.send.tron

import io.fastpayd.wallet.core.HSCaution
import io.fastpayd.wallet.entities.Address
import io.fastpayd.wallet.entities.ViewState
import java.math.BigDecimal

data class SendUiState(
    val availableBalance: BigDecimal,
    val amountCaution: HSCaution?,
    val addressError: Throwable?,
    val proceedEnabled: Boolean,
    val sendEnabled: Boolean,
    val feeViewState: ViewState,
    val cautions: List<HSCaution>,
    val showAddressInput: Boolean,
    val address: Address,
)
