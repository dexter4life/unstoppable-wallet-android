package io.fastpayd.wallet.modules.activatetoken

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

class ActivateTokenFragment {
    @Parcelize
    data class Result(val activated: Boolean = false) : Parcelable
}