package io.fastpayd.wallet.modules.evmfee

import io.fastpayd.wallet.core.Warning

sealed class FeeSettingsWarning : Warning() {
    data object RiskOfGettingStuck : FeeSettingsWarning()
    data object RiskOfGettingStuckLegacy : FeeSettingsWarning()
    data object Overpricing : FeeSettingsWarning()
}

sealed class FeeSettingsError : Exception() {
    data object InsufficientBalance : FeeSettingsError()
    data object UsedNonce : FeeSettingsError()
}