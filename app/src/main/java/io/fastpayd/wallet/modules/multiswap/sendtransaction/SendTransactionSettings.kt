package io.fastpayd.wallet.modules.multiswap.sendtransaction

import io.fastpayd.wallet.modules.evmfee.GasPriceInfo
import io.fastpayd.ethereumkit.models.Address

sealed class SendTransactionSettings {
    data class Evm(val gasPriceInfo: GasPriceInfo?, val receiveAddress: Address) : SendTransactionSettings()
    class Btc() : SendTransactionSettings()
}
