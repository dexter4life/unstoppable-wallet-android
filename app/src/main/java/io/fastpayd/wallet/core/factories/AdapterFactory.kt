package io.fastpayd.wallet.core.factories

import android.content.Context
import android.util.Log
import io.fastpayd.wallet.core.IAdapter
import io.fastpayd.wallet.core.ICoinManager
import io.fastpayd.wallet.core.ILocalStorage
import io.fastpayd.wallet.core.ITransactionsAdapter
import io.fastpayd.wallet.core.adapters.BitcoinAdapter
import io.fastpayd.wallet.core.adapters.BitcoinCashAdapter
import io.fastpayd.wallet.core.adapters.DashAdapter
import io.fastpayd.wallet.core.adapters.ECashAdapter
import io.fastpayd.wallet.core.adapters.Eip20Adapter
import io.fastpayd.wallet.core.adapters.EvmAdapter
import io.fastpayd.wallet.core.adapters.EvmTransactionsAdapter
import io.fastpayd.wallet.core.adapters.JettonAdapter
import io.fastpayd.wallet.core.adapters.LitecoinAdapter
import io.fastpayd.wallet.core.adapters.PlaceholderTransactionsAdapter
import io.fastpayd.wallet.core.adapters.PlaceholderWalletAdapter
import io.fastpayd.wallet.core.adapters.SolanaAdapter
import io.fastpayd.wallet.core.adapters.SolanaTransactionConverter
import io.fastpayd.wallet.core.adapters.SolanaTransactionsAdapter
import io.fastpayd.wallet.core.adapters.SplAdapter
import io.fastpayd.wallet.core.adapters.StellarAdapter
import io.fastpayd.wallet.core.adapters.StellarAssetAdapter
import io.fastpayd.wallet.core.adapters.StellarTransactionsAdapter
import io.fastpayd.wallet.core.adapters.TonAdapter
import io.fastpayd.wallet.core.adapters.TonTransactionConverter
import io.fastpayd.wallet.core.adapters.TonTransactionsAdapter
import io.fastpayd.wallet.core.adapters.Trc20Adapter
import io.fastpayd.wallet.core.adapters.TronAdapter
import io.fastpayd.wallet.core.adapters.TronTransactionConverter
import io.fastpayd.wallet.core.adapters.TronTransactionsAdapter
import io.fastpayd.wallet.core.adapters.zcash.ZcashAdapter
import io.fastpayd.wallet.core.managers.BtcBlockchainManager
import io.fastpayd.wallet.core.managers.EvmBlockchainManager
import io.fastpayd.wallet.core.managers.EvmLabelManager
import io.fastpayd.wallet.core.managers.EvmSyncSourceManager
import io.fastpayd.wallet.core.managers.RestoreSettingsManager
import io.fastpayd.wallet.core.managers.SolanaKitManager
import io.fastpayd.wallet.core.managers.StellarKitManager
import io.fastpayd.wallet.core.managers.TonKitManager
import io.fastpayd.wallet.core.managers.TronKitManager
import io.fastpayd.wallet.entities.Wallet
import io.fastpayd.wallet.modules.transactions.TransactionSource
import io.horizontalsystems.core.BackgroundManager
import io.horizontalsystems.marketkit.models.BlockchainType
import io.horizontalsystems.marketkit.models.TokenQuery
import io.horizontalsystems.marketkit.models.TokenType
import io.fastpayd.tonkit.Address

class AdapterFactory(
    private val context: Context,
    private val btcBlockchainManager: BtcBlockchainManager,
    private val evmBlockchainManager: EvmBlockchainManager,
    private val evmSyncSourceManager: EvmSyncSourceManager,
    private val solanaKitManager: SolanaKitManager,
    private val tronKitManager: TronKitManager,
    private val tonKitManager: TonKitManager,
    private val stellarKitManager: StellarKitManager,
    private val backgroundManager: BackgroundManager,
    private val restoreSettingsManager: RestoreSettingsManager,
    private val coinManager: ICoinManager,
    private val evmLabelManager: EvmLabelManager,
    private val localStorage: ILocalStorage,
) {

    private fun placeholderText(blockchainType: BlockchainType): String {
        return "${blockchainType.uid} features are unavailable in this build. Placeholder data is shown to preserve the UI."
    }

    private fun placeholderAdapter(wallet: Wallet): IAdapter {
        return PlaceholderWalletAdapter(wallet, placeholderText(wallet.token.blockchainType))
    }

    private fun placeholderTransactionsAdapter(source: TransactionSource): ITransactionsAdapter {
        return PlaceholderTransactionsAdapter(source, placeholderText(source.blockchain.type))
    }

    private fun getEvmAdapter(wallet: Wallet): IAdapter? {
        return placeholderAdapter(wallet)
    }

    private fun getEip20Adapter(wallet: Wallet, address: String): IAdapter? {
        return placeholderAdapter(wallet)
    }

    private fun getSplAdapter(wallet: Wallet, address: String): IAdapter? {
        return placeholderAdapter(wallet)
    }

    private fun getTrc20Adapter(wallet: Wallet, address: String): IAdapter {
        return placeholderAdapter(wallet)
    }

    private fun getJettonAdapter(wallet: Wallet, address: String): IAdapter {
        return placeholderAdapter(wallet)
    }

    private fun getStellarAssetAdapter(wallet: Wallet, code: String, issuer: String): IAdapter {
        return placeholderAdapter(wallet)
    }

    fun getAdapterOrNull(wallet: Wallet) = try {
        getAdapter(wallet)
    } catch (e: Throwable) {
        Log.e("AAA", "get adapter error", e)
        null
    }

    private fun getAdapter(wallet: Wallet) = when (val tokenType = wallet.token.type) {
        is TokenType.Derived -> {
            when (wallet.token.blockchainType) {
                BlockchainType.Bitcoin -> {
                    placeholderAdapter(wallet)
                }
                BlockchainType.Litecoin -> {
                    placeholderAdapter(wallet)
                }
                else -> null
            }
        }
        is TokenType.AddressTyped -> {
            if (wallet.token.blockchainType == BlockchainType.BitcoinCash) {
                placeholderAdapter(wallet)
            }
            else null
        }
        TokenType.Native -> when (wallet.token.blockchainType) {
            BlockchainType.ECash -> {
                placeholderAdapter(wallet)
            }
            BlockchainType.Dash -> {
                placeholderAdapter(wallet)
            }
            BlockchainType.Zcash -> {
                placeholderAdapter(wallet)
            }
            BlockchainType.Ethereum,
            BlockchainType.BinanceSmartChain,
            BlockchainType.Polygon,
            BlockchainType.Avalanche,
            BlockchainType.Optimism,
            BlockchainType.Base,
            BlockchainType.ZkSync,
            BlockchainType.Gnosis,
            BlockchainType.Fantom,
            BlockchainType.ArbitrumOne -> {
                getEvmAdapter(wallet)
            }

            BlockchainType.Solana -> {
                placeholderAdapter(wallet)
            }
            BlockchainType.Tron -> {
                placeholderAdapter(wallet)
            }
            BlockchainType.Ton -> {
                placeholderAdapter(wallet)
            }
            BlockchainType.Stellar -> {
                placeholderAdapter(wallet)
            }

            else -> null
        }
        is TokenType.Eip20 -> {
            if (wallet.token.blockchainType == BlockchainType.Tron) {
                getTrc20Adapter(wallet, tokenType.address)
            } else {
                getEip20Adapter(wallet, tokenType.address)
            }
        }
        is TokenType.Spl -> getSplAdapter(wallet, tokenType.address)
        is TokenType.Jetton -> getJettonAdapter(wallet, tokenType.address)
        is TokenType.Asset -> getStellarAssetAdapter(wallet, tokenType.code, tokenType.issuer)
        is TokenType.Unsupported -> null
    }

    fun evmTransactionsAdapter(source: TransactionSource, blockchainType: BlockchainType): ITransactionsAdapter? {
        return placeholderTransactionsAdapter(source)
    }

    fun solanaTransactionsAdapter(source: TransactionSource): ITransactionsAdapter? {
        return placeholderTransactionsAdapter(source)
    }

    fun tronTransactionsAdapter(source: TransactionSource): ITransactionsAdapter? {
        return placeholderTransactionsAdapter(source)
    }

    fun tonTransactionsAdapter(source: TransactionSource): ITransactionsAdapter? {
        return placeholderTransactionsAdapter(source)
    }

    fun stellarTransactionsAdapter(source: TransactionSource): ITransactionsAdapter? {
        return placeholderTransactionsAdapter(source)
    }

    fun tonTransactionConverter(
        address: Address,
        source: TransactionSource,
    ): TonTransactionConverter? {
        val query = TokenQuery(BlockchainType.Ton, TokenType.Native)
        val baseToken = coinManager.getToken(query) ?: return null
        return TonTransactionConverter(
            address,
            coinManager,
            source,
            baseToken
        )
    }

    fun unlinkAdapter(wallet: Wallet) {
        when (val blockchainType = wallet.transactionSource.blockchain.type) {
            BlockchainType.Ethereum,
            BlockchainType.BinanceSmartChain,
            BlockchainType.Polygon,
            BlockchainType.Optimism,
            BlockchainType.Base,
            BlockchainType.ZkSync,
            BlockchainType.ArbitrumOne -> {
                val evmKitManager = evmBlockchainManager.getEvmKitManager(blockchainType)
                evmKitManager.unlink(wallet.account)
            }
            BlockchainType.Solana -> {
                solanaKitManager.unlink(wallet.account)
            }
            BlockchainType.Tron -> {
                tronKitManager.unlink(wallet.account)
            }
            BlockchainType.Ton -> {
                tonKitManager.unlink(wallet.account)
            }
            BlockchainType.Stellar -> {
                stellarKitManager.unlink(wallet.account)
            }
            else -> Unit
        }
    }

    fun unlinkAdapter(transactionSource: TransactionSource) {
        when (val blockchainType = transactionSource.blockchain.type) {
            BlockchainType.Ethereum,
            BlockchainType.BinanceSmartChain,
            BlockchainType.Polygon,
            BlockchainType.Optimism,
            BlockchainType.Base,
            BlockchainType.ZkSync,
            BlockchainType.ArbitrumOne -> {
                val evmKitManager = evmBlockchainManager.getEvmKitManager(blockchainType)
                evmKitManager.unlink(transactionSource.account)
            }
            BlockchainType.Solana -> {
                solanaKitManager.unlink(transactionSource.account)
            }
            BlockchainType.Tron -> {
                tronKitManager.unlink(transactionSource.account)
            }
            BlockchainType.Ton -> {
                tonKitManager.unlink(transactionSource.account)
            }
            BlockchainType.Stellar -> {
                stellarKitManager.unlink(transactionSource.account)
            }
            else -> Unit
        }
    }
}
