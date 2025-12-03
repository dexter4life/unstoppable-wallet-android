package io.horizontalsystems.bankwallet.modules.authentication.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.horizontalsystems.bankwallet.core.App


object RegisterModule {
    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val resourceManager = App.resourceManager
            val localStorage = App.localStorage
            return RegisterActivityModel(localStorage = localStorage, resourceProvider = resourceManager.provider()) as T
        }
    }
}
