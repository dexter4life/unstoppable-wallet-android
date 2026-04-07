package io.fastpayd.wallet.modules.walletconnect.session

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

object WCSessionModule {
    @Parcelize
    data class Input(val sessionTopic: String? = null) : Parcelable
}