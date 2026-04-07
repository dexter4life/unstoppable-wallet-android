package io.fastpayd.wallet.modules.main

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.walletconnect.web3.wallet.client.Wallet
import io.fastpayd.wallet.core.App
import io.fastpayd.wallet.core.IAccountManager
import io.fastpayd.wallet.core.ILocalStorage
import io.fastpayd.wallet.core.managers.UserManager
import io.fastpayd.wallet.modules.walletconnect.WCDelegate
import io.horizontalsystems.core.IKeyStoreManager
import io.horizontalsystems.core.IPinComponent
import io.horizontalsystems.core.ISystemInfoManager
import io.horizontalsystems.core.security.KeyStoreValidationError
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val userManager: UserManager,
    private val accountManager: IAccountManager,
    private val pinComponent: IPinComponent,
    private val systemInfoManager: ISystemInfoManager,
    private val keyStoreManager: IKeyStoreManager,
    private val localStorage: ILocalStorage,
) : ViewModel() {

    val navigateToMainLiveData = MutableLiveData(false)
    val wcEvent = MutableLiveData<Wallet.Model?>()
    val intentLiveData = MutableLiveData<Intent?>()

    private val _contentHidden = MutableLiveData(true)
    val contentHidden: LiveData<Boolean> = _contentHidden

    init {
        viewModelScope.launch {
            userManager.currentUserLevelFlow.collect {
                navigateToMainLiveData.postValue(true)
            }
        }
        viewModelScope.launch {
            WCDelegate.walletEvents.collect {
                wcEvent.postValue(it)
            }
        }
        viewModelScope.launch {
            pinComponent.isLockedFlowable.collect {
                _contentHidden.postValue(it)
            }
        }
    }

    fun onWcEventHandled() {
        wcEvent.postValue(null)
    }

    fun validate() {
        if (systemInfoManager.isSystemLockOff) {
            throw MainScreenValidationError.NoSystemLock()
        }

        try {
            keyStoreManager.validateKeyStore()
        } catch (e: KeyStoreValidationError.UserNotAuthenticated) {
            throw MainScreenValidationError.UserAuthentication()
        } catch (e: KeyStoreValidationError.KeyIsInvalid) {
            throw MainScreenValidationError.KeyInvalidated()
        } catch (e: RuntimeException) {
            throw MainScreenValidationError.KeystoreRuntimeException()
        }

        if (accountManager.isAccountsEmpty && !localStorage.mainShowedOnce) {
            throw MainScreenValidationError.Welcome()
        }

        if (pinComponent.isLocked) {
            throw MainScreenValidationError.Unlock()
        }
    }

    fun onNavigatedToMain() {
        navigateToMainLiveData.postValue(false)
    }

    fun setIntent(intent: Intent) {
        intentLiveData.postValue(intent)
    }

    fun intentHandled() {
        intentLiveData.postValue(null)
    }

    fun onResume() {
        _contentHidden.postValue(pinComponent.isLocked)
    }

    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainActivityViewModel(
                App.userManager,
                App.accountManager,
                App.pinComponent,
                App.systemInfoManager,
                App.keyStoreManager,
                App.localStorage,
            ) as T
        }
    }
}

sealed class MainScreenValidationError : Exception() {
    class Welcome : MainScreenValidationError()
    class Unlock : MainScreenValidationError()
    class NoSystemLock : MainScreenValidationError()
    class KeyInvalidated : MainScreenValidationError()
    class UserAuthentication : MainScreenValidationError()
    class KeystoreRuntimeException : MainScreenValidationError()
}
