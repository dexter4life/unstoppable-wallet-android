package io.fastpayd.hodler

import io.fastpayd.bitcoincore.core.IPluginData

enum class LockTimeInterval {
    hour,
    month,
    halfYear,
    year,
}

data class HodlerData(val lockTimeInterval: LockTimeInterval) : IPluginData

object HodlerPlugin {
    const val id: Byte = 1
}