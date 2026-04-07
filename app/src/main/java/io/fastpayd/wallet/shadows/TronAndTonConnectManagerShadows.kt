package io.fastpayd.wallet.core.managers

import android.content.Context
import io.fastpayd.wallet.core.IAccountManager
import io.fastpayd.wallet.core.IWalletManager
import io.fastpayd.wallet.core.factories.AdapterFactory
import io.fastpayd.wallet.core.providers.AppConfigProvider
import io.fastpayd.wallet.entities.Account
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DummyTronKit {
    fun refresh() {}
    fun start() {}
    fun stop() {}
    fun statusInfo(): Map<String, Any> = mapOf("status" to "disabled")
}

class DummyTronSigner

class TronKitWrapper(val tronKit: DummyTronKit, val signer: DummyTronSigner? = null)

class TronKitManager(
    appConfigProvider: AppConfigProvider,
    private val backgroundManager: io.horizontalsystems.core.BackgroundManager,
) {
    private val kitStoppedSubject = PublishSubject.create<Unit>()
    private val _kitStartedFlow = MutableStateFlow(false)

    val kitStoppedObservable: Observable<Unit>
        get() = kitStoppedSubject

    val kitStartedFlow: StateFlow<Boolean>
        get() = _kitStartedFlow

    var tronKitWrapper: TronKitWrapper? = null
        private set

    val statusInfo: Map<String, Any>?
        get() = tronKitWrapper?.tronKit?.statusInfo()

    fun getTronKitWrapper(account: Account): TronKitWrapper {
        return tronKitWrapper ?: TronKitWrapper(DummyTronKit()).also {
            tronKitWrapper = it
            _kitStartedFlow.value = true
        }
    }

    fun getNonActiveTronKitWrapper(account: Account): TronKitWrapper = TronKitWrapper(DummyTronKit())

    fun unlink(account: Account) {
        tronKitWrapper = null
        _kitStartedFlow.value = false
        kitStoppedSubject.onNext(Unit)
    }
}

class TronAccountManager(
    accountManager: IAccountManager,
    walletManager: IWalletManager,
    tronKitManager: TronKitManager,
    marketKitWrapper: MarketKitWrapper,
    tokenAutoEnableManager: TokenAutoEnableManager,
) {
    fun start() {}
}

class DummyTonConnectKit

class DummyTonTransactionSigner

class TonConnectManager(
    context: Context,
    val adapterFactory: AdapterFactory,
    appName: String,
    appVersion: String,
) {
    val kit = DummyTonConnectKit()
    val transactionSigner = DummyTonTransactionSigner()

    fun start() {}

    fun handle(uri: String, closeApp: Boolean = false) {}
}