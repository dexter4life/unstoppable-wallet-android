package io.fastpayd.wallet.modules.managewallets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.fastpayd.wallet.core.App
import io.fastpayd.wallet.modules.enablecoin.restoresettings.RestoreSettingsService
import io.fastpayd.wallet.modules.enablecoin.restoresettings.RestoreSettingsViewModel
import io.fastpayd.wallet.modules.receive.FullCoinsProvider

object ManageWalletsModule {

    class Factory : ViewModelProvider.Factory {

        private val restoreSettingsService by lazy {
            RestoreSettingsService(App.restoreSettingsManager, App.zcashBirthdayProvider)
        }

        private val manageWalletsService by lazy {
            val activeAccount = App.accountManager.activeAccount
            ManageWalletsService(
                App.walletManager,
                restoreSettingsService,
                App.accountManager.activeAccount?.let { account ->
                    FullCoinsProvider(App.marketKit, account)
                },
                activeAccount
            )
        }

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when (modelClass) {
                RestoreSettingsViewModel::class.java -> {
                    RestoreSettingsViewModel(restoreSettingsService, listOf(restoreSettingsService)) as T
                }
                ManageWalletsViewModel::class.java -> {
                    ManageWalletsViewModel(manageWalletsService, listOf(manageWalletsService)) as T
                }
                else -> throw IllegalArgumentException()
            }
        }
    }
}
