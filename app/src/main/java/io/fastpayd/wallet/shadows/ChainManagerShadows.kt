package io.fastpayd.wallet.core.managers

import io.fastpayd.wallet.core.ICoinManager
import io.fastpayd.wallet.core.ILocalStorage
import io.fastpayd.wallet.core.IAccountManager
import io.fastpayd.wallet.core.IWalletManager
import io.fastpayd.wallet.core.storage.SpamAddressStorage
import io.fastpayd.wallet.entities.Account
import io.fastpayd.wallet.entities.SpamAddress
import io.fastpayd.wallet.entities.transactionrecords.evm.TransferEvent
import io.horizontalsystems.marketkit.models.BlockchainType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SpamManager(
    private val localStorage: ILocalStorage,
    private val coinManager: ICoinManager,
    private val spamAddressStorage: SpamAddressStorage,
    marketKitWrapper: MarketKitWrapper,
    appConfigProvider: io.fastpayd.wallet.core.providers.AppConfigProvider
) {
    var hideSuspiciousTx = localStorage.hideSuspiciousTransactions
        private set

    fun isSpam(incomingEvents: List<TransferEvent>, outgoingEvents: List<TransferEvent>): Boolean = false

    fun updateFilterHideSuspiciousTx(hide: Boolean) {
        localStorage.hideSuspiciousTransactions = hide
        hideSuspiciousTx = hide
    }

    fun subscribeToKitStart(evmKitManager: EvmKitManager, blockchainType: BlockchainType) {
    }

    fun find(address: String): SpamAddress? = spamAddressStorage.findByAddress(address)

    fun isSpam(transactionHash: ByteArray): Boolean = spamAddressStorage.isSpam(transactionHash)
}

class StellarAccountManager(
    private val accountManager: IAccountManager,
    private val walletManager: IWalletManager,
    private val stellarKitManager: StellarKitManager,
    private val tokenAutoEnableManager: TokenAutoEnableManager,
) {
    fun start() {
    }
}

class TonAccountManager(
    private val accountManager: IAccountManager,
    private val walletManager: IWalletManager,
    private val tonKitManager: TonKitManager,
    private val tokenAutoEnableManager: TokenAutoEnableManager,
) {
    fun start() {
    }
}

class DummyTonKit {
    var receiveAddress: String = ""
    val account: DummyTonAccount? = null

    fun refresh() {
    }

    fun start() {
    }

    fun stop() {
    }

    suspend fun send(boc: ByteArray) {
    }

    fun statusInfo(): Map<String, Any> = mapOf("status" to "disabled")
}

class DummyTonAccount(val balance: Long = 0)

class TonKitWrapper(val tonKit: DummyTonKit)

class TonKitManager(
    private val backgroundManager: io.horizontalsystems.core.BackgroundManager,
) {
    private val _kitStartedFlow = MutableStateFlow(false)
    val kitStartedFlow: StateFlow<Boolean> = _kitStartedFlow

    var tonKitWrapper: TonKitWrapper? = null
        private set

    var currentAccount: Account? = null
        private set

    val statusInfo: Map<String, Any>?
        get() = tonKitWrapper?.tonKit?.statusInfo()

    @Synchronized
    fun getTonKitWrapper(account: Account): TonKitWrapper {
        currentAccount = account
        val wrapper = tonKitWrapper ?: TonKitWrapper(DummyTonKit()).also {
            tonKitWrapper = it
            _kitStartedFlow.value = true
        }
        return wrapper
    }

    fun getNonActiveTonKitWrapper(account: Account): TonKitWrapper = TonKitWrapper(DummyTonKit())

    @Synchronized
    fun unlink(account: Account) {
        if (account == currentAccount) {
            tonKitWrapper = null
            currentAccount = null
            _kitStartedFlow.value = false
        }
    }
}

class DummyStellarKit {
    fun refresh() {
    }

    fun start() {
    }

    fun stop() {
    }

    fun statusInfo(): Map<String, Any> = mapOf("status" to "disabled")
}

class StellarKitWrapper(val stellarKit: DummyStellarKit)

class StellarKitManager(
    private val backgroundManager: io.horizontalsystems.core.BackgroundManager,
) {
    private val _kitStartedFlow = MutableStateFlow(false)
    val kitStartedFlow: StateFlow<Boolean> = _kitStartedFlow

    var stellarKitWrapper: StellarKitWrapper? = null
        private set

    var currentAccount: Account? = null
        private set

    val statusInfo: Map<String, Any>?
        get() = stellarKitWrapper?.stellarKit?.statusInfo()

    @Synchronized
    fun getStellarKitWrapper(account: Account): StellarKitWrapper {
        currentAccount = account
        val wrapper = stellarKitWrapper ?: StellarKitWrapper(DummyStellarKit()).also {
            stellarKitWrapper = it
            _kitStartedFlow.value = true
        }
        return wrapper
    }

    @Synchronized
    fun unlink(account: Account) {
        if (account == currentAccount) {
            stellarKitWrapper = null
            currentAccount = null
            _kitStartedFlow.value = false
        }
    }
}