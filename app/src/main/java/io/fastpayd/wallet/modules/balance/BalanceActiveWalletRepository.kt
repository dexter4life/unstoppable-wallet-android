package io.fastpayd.wallet.modules.balance

import io.fastpayd.wallet.core.IWalletManager
import io.fastpayd.wallet.core.managers.EvmSyncSourceManager
import io.fastpayd.wallet.entities.Wallet
import io.reactivex.Observable

class BalanceActiveWalletRepository(
    private val walletManager: IWalletManager,
    evmSyncSourceManager: EvmSyncSourceManager
) {

    val itemsObservable: Observable<List<Wallet>> =
        Observable
            .merge(
                Observable.just(Unit),
                walletManager.activeWalletsUpdatedObservable,
                evmSyncSourceManager.syncSourceObservable
            )
            .map {
                walletManager.activeWallets
            }

    fun disable(wallet: Wallet) {
        walletManager.delete(listOf(wallet))
    }

    fun enable(wallet: Wallet) {
        walletManager.save(listOf(wallet))
    }

}
