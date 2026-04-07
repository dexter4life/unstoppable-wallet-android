package io.fastpayd.wallet.modules.authentication

import android.content.Context
import io.fastpayd.wallet.modules.intro.IntroActivity

data class AuthData(var email: String = "", var password: String = "")

class AuthenticationActivity {
    companion object {
        fun start(context: Context) {
            IntroActivity.start(context)
        }
    }
}