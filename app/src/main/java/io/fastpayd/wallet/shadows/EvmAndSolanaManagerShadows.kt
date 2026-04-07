package io.fastpayd.wallet.core.managers

import io.fastpayd.wallet.core.IAccountManager
import io.fastpayd.wallet.core.IWalletManager
import io.fastpayd.wallet.core.factories.EvmAccountManagerFactory
import io.fastpayd.wallet.core.providers.AppConfigProvider
import io.fastpayd.wallet.core.storage.BlockchainSettingsStorage
import io.fastpayd.wallet.core.storage.EvmSyncSourceStorage
import io.fastpayd.wallet.entities.Account
import io.fastpayd.wallet.entities.BtcRestoreMode
import io.fastpayd.wallet.entities.EvmSyncSource
import io.fastpayd.wallet.entities.EvmSyncSourceRecord
import io.fastpayd.wallet.entities.TransactionDataSortMode
import io.fastpayd.ethereumkit.models.Address
import io.fastpayd.ethereumkit.models.Chain
import io.fastpayd.ethereumkit.models.FullTransaction
import io.fastpayd.ethereumkit.models.GasPrice
import io.fastpayd.ethereumkit.models.RpcSource
import io.fastpayd.ethereumkit.models.Transaction
import io.fastpayd.ethereumkit.models.TransactionData
import io.fastpayd.ethereumkit.models.TransactionSource
import io.horizontalsystems.marketkit.models.Blockchain
import io.horizontalsystems.marketkit.models.BlockchainType
import io.horizontalsystems.marketkit.models.Token
import io.horizontalsystems.marketkit.models.TokenQuery
import io.horizontalsystems.marketkit.models.TokenType
import io.fastpayd.solanakit.models.FullTokenAccount
import io.fastpayd.solanakit.models.RpcSource as SolanaRpcSource
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.net.URI

class EvmAccountManager(
    blockchainType: BlockchainType,
    accountManager: IAccountManager,
    walletManager: IWalletManager,
    marketKit: MarketKitWrapper,
    evmKitManager: EvmKitManager,
    tokenAutoEnableManager: TokenAutoEnableManager,
)

class DummyEvmSigner {
    fun signature(rawTransaction: ByteArray): ByteArray = rawTransaction
}

class DummyEvmKit(
    val chain: Chain,
    val receiveAddress: Address = Address("0x0000000000000000000000000000000000000000")
) {
    fun debugInfo(): String = ""

    fun statusInfo(): Map<String, Any> = mapOf("status" to "disabled")

    fun refresh() {
    }

    fun stop() {
    }

    fun start() {
    }

    fun decorate(transactionData: TransactionData): Any? = null

    fun rawTransaction(transactionData: TransactionData, gasPrice: GasPrice, gasLimit: Long, nonce: Long?): Single<ByteArray> {
        return Single.just(byteArrayOf())
    }

    fun send(rawTransaction: ByteArray, signature: ByteArray): Single<FullTransaction> {
        return Single.just(FullTransaction(Transaction(hashString = "0x0")))
    }
}

class EvmKitWrapper(
    val evmKit: DummyEvmKit,
    val nftKit: Any? = null,
    val blockchainType: BlockchainType,
    val signer: DummyEvmSigner? = null,
) {
    fun sendSingle(
        transactionData: TransactionData,
        gasPrice: GasPrice,
        gasLimit: Long,
        nonce: Long?
    ): Single<FullTransaction> {
        return evmKit.send(byteArrayOf(), byteArrayOf())
    }
}

class EvmKitManager(
    private val chain: Chain,
    private val backgroundManager: io.horizontalsystems.core.BackgroundManager,
    private val syncSourceManager: EvmSyncSourceManager,
) {
    private val evmKitUpdatedSubject = PublishSubject.create<Unit>()

    var evmKitWrapper: EvmKitWrapper? = null
    var currentAccount: Account? = null
        private set

    val evmKitUpdatedObservable: Observable<Unit>
        get() = evmKitUpdatedSubject

    val statusInfo: Map<String, Any>?
        get() = evmKitWrapper?.evmKit?.statusInfo()

    fun getEvmKitWrapper(account: Account, blockchainType: BlockchainType): EvmKitWrapper {
        currentAccount = account
        return evmKitWrapper ?: EvmKitWrapper(DummyEvmKit(chain), blockchainType = blockchainType).also {
            evmKitWrapper = it
            evmKitUpdatedSubject.onNext(Unit)
        }
    }

    fun unlink(account: Account) {
        if (account == currentAccount) {
            evmKitWrapper = null
            currentAccount = null
            evmKitUpdatedSubject.onNext(Unit)
        }
    }
}

class EvmBlockchainManager(
    private val backgroundManager: io.horizontalsystems.core.BackgroundManager,
    private val syncSourceManager: EvmSyncSourceManager,
    private val marketKit: MarketKitWrapper,
    private val accountManagerFactory: EvmAccountManagerFactory,
    private val spamManager: SpamManager,
) {
    private val evmKitManagersMap = mutableMapOf<BlockchainType, Pair<EvmKitManager, EvmAccountManager>>()

    val allBlockchains: List<Blockchain>
        get() = marketKit.blockchains(blockchainTypes.map { it.uid })

    val allMainNetBlockchains: List<Blockchain>
        get() = allBlockchains

    private fun getEvmKitManagers(blockchainType: BlockchainType): Pair<EvmKitManager, EvmAccountManager> {
        return evmKitManagersMap.getOrPut(blockchainType) {
            val evmKitManager = EvmKitManager(getChain(blockchainType), backgroundManager, syncSourceManager)
            evmKitManager to accountManagerFactory.evmAccountManager(blockchainType, evmKitManager)
        }
    }

    fun getChain(blockchainType: BlockchainType) = when (blockchainType) {
        BlockchainType.Ethereum -> Chain.Ethereum
        BlockchainType.BinanceSmartChain -> Chain.BinanceSmartChain
        BlockchainType.Polygon -> Chain.Polygon
        BlockchainType.Avalanche -> Chain.Avalanche
        BlockchainType.Optimism -> Chain.Optimism
        BlockchainType.Base -> Chain.Base
        BlockchainType.ZkSync -> Chain.ZkSync
        BlockchainType.ArbitrumOne -> Chain.ArbitrumOne
        BlockchainType.Gnosis -> Chain.Gnosis
        BlockchainType.Fantom -> Chain.Fantom
        else -> Chain.Ethereum
    }

    fun getBlockchain(chainId: Int): Blockchain? = allBlockchains.firstOrNull { getChain(it.type).id == chainId }

    fun getBlockchain(token: Token): Blockchain? = allBlockchains.firstOrNull { token.blockchain == it }

    fun getBlockchain(blockchainType: BlockchainType): Blockchain? = allBlockchains.firstOrNull { it.type == blockchainType }

    fun getEvmKitManager(blockchainType: BlockchainType): EvmKitManager = getEvmKitManagers(blockchainType).first

    fun getEvmAccountManager(blockchainType: BlockchainType): EvmAccountManager = getEvmKitManagers(blockchainType).second

    fun getBaseToken(blockchainType: BlockchainType): Token? = marketKit.token(TokenQuery(blockchainType, TokenType.Native))

    companion object {
        val blockchainTypes = listOf(
            BlockchainType.Ethereum,
            BlockchainType.BinanceSmartChain,
            BlockchainType.Polygon,
            BlockchainType.Avalanche,
            BlockchainType.Optimism,
            BlockchainType.ArbitrumOne,
            BlockchainType.Gnosis,
            BlockchainType.Fantom,
            BlockchainType.Base,
            BlockchainType.ZkSync,
        )
    }
}

class EvmSyncSourceManager(
    private val appConfigProvider: AppConfigProvider,
    private val blockchainSettingsStorage: BlockchainSettingsStorage,
    private val evmSyncSourceStorage: EvmSyncSourceStorage,
) {
    private val syncSourceSubject = PublishSubject.create<BlockchainType>()
    private val _syncSourcesUpdatedFlow = MutableSharedFlow<BlockchainType>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private val customSources = mutableMapOf<BlockchainType, MutableList<EvmSyncSource>>()
    private val selectedSources = mutableMapOf<BlockchainType, EvmSyncSource>()

    val syncSourceObservable: Observable<BlockchainType>
        get() = syncSourceSubject

    val syncSourcesUpdatedFlow = _syncSourcesUpdatedFlow.asSharedFlow()

    fun defaultSyncSources(blockchainType: BlockchainType): List<EvmSyncSource> {
        return listOf(
            EvmSyncSource(
                id = "${blockchainType.uid}|default",
                name = "Default",
                rpcSource = RpcSource.Http(listOf(URI("https://example.invalid")), null),
                transactionSource = TransactionSource.Rpc
            )
        )
    }

    fun customSyncSources(blockchainType: BlockchainType): List<EvmSyncSource> = customSources[blockchainType].orEmpty()

    fun allSyncSources(blockchainType: BlockchainType): List<EvmSyncSource> = defaultSyncSources(blockchainType) + customSyncSources(blockchainType)

    fun getSyncSource(blockchainType: BlockchainType): EvmSyncSource = selectedSources[blockchainType] ?: defaultSyncSources(blockchainType).first()

    fun getHttpSyncSource(blockchainType: BlockchainType): EvmSyncSource? = getSyncSource(blockchainType).takeIf { it.rpcSource is RpcSource.Http }

    fun save(syncSource: EvmSyncSource, blockchainType: BlockchainType) {
        selectedSources[blockchainType] = syncSource
        syncSourceSubject.onNext(blockchainType)
        _syncSourcesUpdatedFlow.tryEmit(blockchainType)
    }

    fun delete(syncSource: EvmSyncSource, blockchainType: BlockchainType) {
        customSources[blockchainType]?.removeAll { it.id == syncSource.id }
        if (selectedSources[blockchainType]?.id == syncSource.id) {
            selectedSources.remove(blockchainType)
        }
        syncSourceSubject.onNext(blockchainType)
        _syncSourcesUpdatedFlow.tryEmit(blockchainType)
    }

    fun saveSyncSource(blockchainType: BlockchainType, url: String, auth: String?) {
        val syncSource = EvmSyncSource(
            id = "${blockchainType.uid}|$url",
            name = url,
            rpcSource = RpcSource.Http(listOf(URI(url)), auth),
            transactionSource = TransactionSource.Rpc
        )
        customSources.getOrPut(blockchainType) { mutableListOf() }.add(syncSource)
        save(syncSource, blockchainType)
    }
}

class SolanaWalletManager(
    private val walletManager: IWalletManager,
    private val accountManager: IAccountManager,
    private val marketKit: MarketKitWrapper,
) {
    fun add(tokenAccounts: List<FullTokenAccount>) {
    }
}

class DummySolanaKit {
    fun statusInfo(): Map<String, Any> = mapOf("status" to "disabled")

    fun stop() {
    }

    fun start() {
    }

    fun refresh() {
    }
}

class SolanaKitWrapper(val solanaKit: DummySolanaKit, val signer: Any?)

class SolanaKitManager(
    private val appConfigProvider: AppConfigProvider,
    private val rpcSourceManager: SolanaRpcSourceManager,
    private val walletManager: SolanaWalletManager,
    private val backgroundManager: io.horizontalsystems.core.BackgroundManager,
) {
    private val solanaKitStoppedSubject = PublishSubject.create<Unit>()

    var solanaKitWrapper: SolanaKitWrapper? = null
    var currentAccount: Account? = null
        private set

    val kitStoppedObservable: Observable<Unit>
        get() = solanaKitStoppedSubject

    val statusInfo: Map<String, Any>?
        get() = solanaKitWrapper?.solanaKit?.statusInfo()

    fun getSolanaKitWrapper(account: Account): SolanaKitWrapper {
        currentAccount = account
        return solanaKitWrapper ?: SolanaKitWrapper(DummySolanaKit(), null).also { solanaKitWrapper = it }
    }

    fun unlink(account: Account) {
        if (account == currentAccount) {
            solanaKitWrapper = null
            currentAccount = null
            solanaKitStoppedSubject.onNext(Unit)
        }
    }
}

class SolanaRpcSourceManager(
    private val blockchainSettingsStorage: BlockchainSettingsStorage,
    private val marketKitWrapper: MarketKitWrapper,
) {
    private val blockchainType = BlockchainType.Solana
    private val rpcSourceSubjectUpdate = PublishSubject.create<Unit>()

    val rpcSourceUpdateObservable: Observable<Unit>
        get() = rpcSourceSubjectUpdate

    val allRpcSources = listOf(SolanaRpcSource.TritonOne, SolanaRpcSource.Serum)

    val rpcSource: SolanaRpcSource
        get() {
            val rpcSourceName = blockchainSettingsStorage.evmSyncSourceUrl(blockchainType)
            return allRpcSources.firstOrNull { it.name == rpcSourceName } ?: allRpcSources.first()
        }

    val blockchain: Blockchain?
        get() = marketKitWrapper.blockchain(blockchainType.uid)

    fun save(rpcSource: SolanaRpcSource) {
        blockchainSettingsStorage.save(rpcSource.name, blockchainType)
        rpcSourceSubjectUpdate.onNext(Unit)
    }
}