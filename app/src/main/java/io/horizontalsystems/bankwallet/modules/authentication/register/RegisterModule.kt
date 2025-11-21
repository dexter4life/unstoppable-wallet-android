package io.horizontalsystems.bankwallet.modules.authentication.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.horizontalsystems.bankwallet.core.App

object RegisterModule {
    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RegisterActivityModel(App.localStorage) as T
        }
    }
}
