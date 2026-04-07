package io.fastpayd.wallet.modules.settings.appearance

import io.fastpayd.wallet.core.ILocalStorage
import io.fastpayd.wallet.entities.LaunchPage
import io.fastpayd.wallet.ui.compose.Select
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LaunchScreenService(private val localStorage: ILocalStorage) {
    private val screens by lazy { LaunchPage.entries }

    val selectedLaunchScreen: LaunchPage
        get() = localStorage.launchPage ?: LaunchPage.Auto

    private val _optionsFlow = MutableStateFlow(
        Select(selectedLaunchScreen, screens)
    )
    val optionsFlow = _optionsFlow.asStateFlow()

    fun setLaunchScreen(launchPage: LaunchPage) {
        localStorage.launchPage = launchPage

        _optionsFlow.update {
            Select(launchPage, screens)
        }
    }
}
