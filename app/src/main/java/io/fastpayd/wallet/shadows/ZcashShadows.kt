package io.fastpayd.wallet.core.adapters.zcash

import java.math.BigDecimal

class ZcashAdapter {
    val statusInfo: Map<String, Any>
        get() = mapOf("status" to "disabled")

    enum class ZCashAddressType {
        Transparent,
        Shielded
    }

    sealed class ZcashError : Exception() {
        object InvalidAddress : ZcashError()
        object SendToSelfNotAllowed : ZcashError()
    }

    companion object {
        fun clear(accountId: String) {
        }
    }
}

class ZcashTransaction {
    enum class ShieldDirection {
        Shield,
        Unshield
    }
}