package io.horizontalsystems.bankwallet.modules.authentication.login

import androidx.lifecycle.ViewModel
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.core.ILocalStorage

class LoginActivityModel (
    public val localStorage: ILocalStorage?
): ViewModel() {

    val slides = listOf(
        LoginModule.IntroSliderData(
            R.string.Intro_Wallet_Screen2Title,
            R.string.Intro_Wallet_Screen2Description,
            R.drawable.ic_global_payment,
            R.drawable.ic_global_payment
        ),
        LoginModule.IntroSliderData(
            R.string.Intro_Wallet_Screen3Title,
            R.string.Intro_Wallet_Screen3Description,
            R.drawable.ic_secure_transfer,
            R.drawable.ic_secure_transfer
        ),
        LoginModule.IntroSliderData(
            R.string.Intro_Wallet_Screen4Title,
            R.string.Intro_Wallet_Screen4Description,
            R.drawable.ic_smart_app,
            R.drawable.ic_smart_app
        ),
    )

    val quickLoginButtons = listOf(
        LoginModule.ActionButtons(R.drawable.ic_google, description = "Google", onClick = {}),
        LoginModule.ActionButtons(R.drawable.ic_facebook, description = "Facebook", onClick =  {}),
        LoginModule.ActionButtons(R.drawable.ic_apple, description = "Apple", onClick =  {})
    )

    fun onStartClicked() {
        if (localStorage != null){
            localStorage.mainShowedOnce = true
        }
    }

    fun onPasskeyLoginClick(): Unit {
        //TODO() implement login with passkey
    }


    fun onLoginClicked() {
        if(localStorage != null){
            localStorage.showLoginScreen = true
        }
    }

}
