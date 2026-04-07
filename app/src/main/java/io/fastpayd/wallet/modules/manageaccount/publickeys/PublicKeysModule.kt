package io.fastpayd.wallet.modules.manageaccount.publickeys

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.fastpayd.wallet.core.App
import io.fastpayd.wallet.entities.Account
import io.fastpayd.wallet.modules.manageaccount.showextendedkey.ShowExtendedKeyModule.DisplayKeyType.AccountPublicKey
import io.fastpayd.hdwalletkit.HDExtendedKey

object PublicKeysModule {

    class Factory(private val account: Account) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PublicKeysViewModel(account, App.evmBlockchainManager) as T
        }
    }

    data class ViewState(
        val evmAddress: String? = null,
        val extendedPublicKey: ExtendedPublicKey? = null
    )

    data class ExtendedPublicKey(
        val hdKey: HDExtendedKey,
        val accountPublicKey: AccountPublicKey
    )
}