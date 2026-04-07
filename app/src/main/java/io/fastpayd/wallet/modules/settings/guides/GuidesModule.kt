package io.fastpayd.wallet.modules.settings.guides

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.fastpayd.wallet.core.App
import io.fastpayd.wallet.core.managers.GuidesManager

object GuidesModule {

    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val guidesService = GuidesRepository(GuidesManager, App.connectivityManager, App.languageManager)

            return GuidesViewModel(guidesService) as T
        }
    }
}
