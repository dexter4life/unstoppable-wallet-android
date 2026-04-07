package io.fastpayd.wallet.core.factories

import io.fastpayd.wallet.core.IAccountManager
import io.fastpayd.wallet.core.IWalletManager
import io.fastpayd.wallet.core.managers.EvmAccountManager
import io.fastpayd.wallet.core.managers.EvmKitManager
import io.fastpayd.wallet.core.managers.MarketKitWrapper
import io.fastpayd.wallet.core.managers.TokenAutoEnableManager
import io.horizontalsystems.marketkit.models.BlockchainType

class EvmAccountManagerFactory(
    private val accountManager: IAccountManager,
    private val walletManager: IWalletManager,
    private val marketKit: MarketKitWrapper,
    private val tokenAutoEnableManager: TokenAutoEnableManager
) {

    fun evmAccountManager(blockchainType: BlockchainType, evmKitManager: EvmKitManager) =
        EvmAccountManager(
            blockchainType,
            accountManager,
            walletManager,
            marketKit,
            evmKitManager,
            tokenAutoEnableManager
        )

}
