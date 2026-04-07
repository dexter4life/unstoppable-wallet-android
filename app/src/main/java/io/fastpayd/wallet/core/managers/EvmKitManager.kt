package io.fastpayd.wallet.core.managers

import android.os.Handler
import android.os.Looper
import android.util.Log
import io.fastpayd.wallet.core.App
import io.fastpayd.wallet.core.UnsupportedAccountException
import io.fastpayd.wallet.entities.Account
import io.fastpayd.wallet.entities.AccountType
import io.horizontalsystems.core.BackgroundManager
import io.horizontalsystems.core.BackgroundManagerState
import io.horizontalsystems.erc20kit.core.Erc20Kit
import io.fastpayd.ethereumkit.core.EthereumKit
import io.fastpayd.ethereumkit.core.signer.Signer
import io.fastpayd.ethereumkit.models.Address
import io.fastpayd.ethereumkit.models.Chain
import io.fastpayd.ethereumkit.models.FullTransaction
import io.fastpayd.ethereumkit.models.GasPrice
import io.fastpayd.ethereumkit.models.RpcSource
import io.fastpayd.ethereumkit.models.TransactionData
import io.horizontalsystems.marketkit.models.BlockchainType
import io.fastpayd.nftkit.core.NftKit
import io.horizontalsystems.oneinchkit.OneInchKit
import io.horizontalsystems.uniswapkit.TokenFactory.UnsupportedChainError
import io.horizontalsystems.uniswapkit.UniswapKit
import io.horizontalsystems.uniswapkit.UniswapV3Kit
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.asFlow
import java.net.URI

class EvmKitManager(
    val chain: Chain,
    private val backgroundManager: BackgroundManager,
    private val syncSourceManager: EvmSyncSourceManager
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private var job: Job? = null

    init {
        coroutineScope.launch {
            syncSourceManager.syncSourceObservable.asFlow().collect { blockchain ->
                handleUpdateNetwork(blockchain)
            }
        }
    }

    private fun handleUpdateNetwork(blockchainType: BlockchainType) {
        if (blockchainType != evmKitWrapper?.blockchainType) return

        stopEvmKit()

        evmKitUpdatedSubject.onNext(Unit)
    }

    private val kitStartedSubject = BehaviorSubject.createDefault(false)
    val kitStartedObservable: Observable<Boolean> = kitStartedSubject

    var evmKitWrapper: EvmKitWrapper? = null
        private set(value) {
            field = value

            kitStartedSubject.onNext(value != null)
        }

    private var useCount = 0
    var currentAccount: Account? = null
        private set
    private val evmKitUpdatedSubject = PublishSubject.create<Unit>()

    val evmKitUpdatedObservable: Observable<Unit>
        get() = evmKitUpdatedSubject

    val statusInfo: Map<String, Any>?
        get() = evmKitWrapper?.evmKit?.statusInfo()

    @Synchronized
    fun getEvmKitWrapper(account: Account, blockchainType: BlockchainType): EvmKitWrapper {
        if (evmKitWrapper != null && currentAccount != account) {
            stopEvmKit()
        }

        if (this.evmKitWrapper == null) {
            val accountType = account.type
            evmKitWrapper = createKitInstance(accountType, account, blockchainType)
            useCount = 0
            currentAccount = account
            subscribeToEvents()
        }

        useCount++
        return this.evmKitWrapper!!
    }

    private fun createKitInstance(
        accountType: AccountType,
        account: Account,
        blockchainType: BlockchainType
    ): EvmKitWrapper {
        val syncSource = syncSourceManager.getSyncSource(blockchainType)

        val address: Address
        var signer: Signer? = null

        when (accountType) {
            is AccountType.Mnemonic -> {
                val seed: ByteArray = accountType.seed
                address = Signer.address(seed, chain)
                signer = Signer.getInstance(seed, chain)
            }
            is AccountType.EvmPrivateKey -> {
                address = Signer.address(accountType.key)
                signer = Signer.getInstance(accountType.key, chain)
            }
            is AccountType.EvmAddress -> {
                address = Address(accountType.address)
            }
            else -> throw UnsupportedAccountException()
        }

        val evmKit = EthereumKit.getInstance(
            App.instance,
            address,
            chain,
            syncSource.rpcSource,
            syncSource.transactionSource,
            account.id
        )

        Erc20Kit.addTransactionSyncer(evmKit)
        Erc20Kit.addDecorators(evmKit)

        UniswapKit.addDecorators(evmKit)
        try {
            UniswapV3Kit.addDecorators(evmKit)
        } catch (e: UnsupportedChainError.NoWethAddress) {
            //do nothing
        }
        OneInchKit.addDecorators(evmKit)

        val nftKit: NftKit? = null
//        var nftKit: NftKit? = null
//        val supportedNftTypes = blockchainType.supportedNftTypes
//        if (supportedNftTypes.isNotEmpty()) {
//            val nftKitInstance = NftKit.getInstance(App.instance, evmKit)
//            supportedNftTypes.forEach {
//                when (it) {
//                    NftType.Eip721 -> {
//                        nftKitInstance.addEip721TransactionSyncer()
//                        nftKitInstance.addEip721Decorators()
//                    }
//                    NftType.Eip1155 -> {
//                        nftKitInstance.addEip1155TransactionSyncer()
//                        nftKitInstance.addEip1155Decorators()
//                    }
//                }
//            }
//            nftKit = nftKitInstance
//        }

        evmKit.start()

        return EvmKitWrapper(evmKit, nftKit, blockchainType, signer)
    }

    @Synchronized
    fun unlink(account: Account) {
        if (account == currentAccount) {
            useCount -= 1

            if (useCount < 1) {
                Log.d("AAA", "stopEvmKit()")
                stopEvmKit()
            }
        }
    }

    private fun subscribeToEvents(){
        job = coroutineScope.launch {
            backgroundManager.stateFlow.collect { state ->
                if (state == BackgroundManagerState.EnterForeground) {
                    evmKitWrapper?.evmKit?.let { kit ->
                        Handler(Looper.getMainLooper()).postDelayed({
                            kit.refresh()
                        }, 1000)
                    }
                }
            }
        }
    }

    private fun stopEvmKit() {
        job?.cancel()
        evmKitWrapper?.evmKit?.stop()
        evmKitWrapper = null
        currentAccount = null
    }
}

val RpcSource.uris: List<URI>
    get() = when (this) {
        is RpcSource.WebSocket -> listOf(uri)
        is RpcSource.Http -> uris
    }

class EvmKitWrapper(
    val evmKit: EthereumKit,
    val nftKit: NftKit?,
    val blockchainType: BlockchainType,
    val signer: Signer?
) {

    fun sendSingle(
        transactionData: TransactionData,
        gasPrice: GasPrice,
        gasLimit: Long,
        nonce: Long?
    ): Single<FullTransaction> {
        return if (signer != null) {
            evmKit.rawTransaction(transactionData, gasPrice, gasLimit, nonce)
                .flatMap { rawTransaction ->
                    val signature = signer.signature(rawTransaction)
                    evmKit.send(rawTransaction, signature)
                }
        } else {
            Single.error(Exception())
        }
    }

}
