package io.fastpayd.wallet.core.managers

import io.fastpayd.wallet.core.IAccountManager
import io.fastpayd.wallet.core.IWalletManager
import io.fastpayd.wallet.entities.EnabledWallet
import io.horizontalsystems.marketkit.models.BlockchainType
import io.horizontalsystems.marketkit.models.TokenQuery
import io.horizontalsystems.marketkit.models.TokenType
import io.fastpayd.solanakit.models.FullTokenAccount

class SolanaWalletManager(
        private val walletManager: IWalletManager,
        private val accountManager: IAccountManager,
        private val marketKit: MarketKitWrapper
) {

    @Synchronized
    fun add(tokenAccounts: List<FullTokenAccount>) {
        val account = accountManager.activeAccount ?: return
        val queries = tokenAccounts
                .filter { !it.mintAccount.isNft }
                .map { TokenQuery(BlockchainType.Solana, TokenType.Spl(it.mintAccount.address)) }
        val existingWallets = walletManager.activeWallets
        val existingTokenTypeIds = existingWallets.map { it.token.type.id }
        val newTokenQueries = queries.filter { !existingTokenTypeIds.contains(it.tokenType.id) }
        val tokens = marketKit.tokens(newTokenQueries)

        val enabledWallets = tokens.map { token ->
            EnabledWallet(
                    tokenQueryId = token.tokenQuery.id,
                    accountId = account.id,
                    coinName = token.coin.name,
                    coinCode = token.coin.code,
                    coinDecimals = token.decimals,
                    coinImage = token.coin.image
            )
        }

        if (enabledWallets.isNotEmpty()) {
            walletManager.saveEnabledWallets(enabledWallets)
        }
    }

}
