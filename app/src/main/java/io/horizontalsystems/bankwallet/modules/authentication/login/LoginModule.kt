package io.horizontalsystems.bankwallet.modules.authentication.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.horizontalsystems.bankwallet.core.App
import io.horizontalsystems.bankwallet.modules.authentication.AuthActivityModel

object LoginModule {
    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AuthActivityModel(App.localStorage) as T
        }
    }

    data class IntroSliderData(
        val title: Int,
        val subtitle: Int,
        val imageLight: Int,
        val imageDark: Int
    )

    data class ActionButtons(val resource: Int, val description: String, val onClick: () -> Unit)
}
