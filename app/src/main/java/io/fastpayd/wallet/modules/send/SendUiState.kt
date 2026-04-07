package io.fastpayd.wallet.modules.send

import io.fastpayd.wallet.core.HSCaution
import io.fastpayd.wallet.entities.Address
import java.math.BigDecimal

data class SendUiState(
    val availableBalance: BigDecimal,
    val amountCaution: HSCaution?,
    val canBeSend: Boolean,
    val showAddressInput: Boolean,
    val address: Address,
)
