package io.fastpayd.wallet.modules.send

import io.fastpayd.wallet.entities.Address
import io.fastpayd.wallet.modules.contacts.model.Contact
import io.fastpayd.hodler.LockTimeInterval
import io.horizontalsystems.marketkit.models.Coin
import java.math.BigDecimal

data class SendConfirmationData(
    val amount: BigDecimal,
    val fee: BigDecimal?,
    val address: Address?,
    val contact: Contact?,
    val coin: Coin,
    val feeCoin: Coin,
    val lockTimeInterval: LockTimeInterval? = null,
    val memo: String?,
    val rbfEnabled: Boolean? = null
)
