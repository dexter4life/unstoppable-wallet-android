package io.fastpayd.wallet.modules.evmfee

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

class FeeSettingsInfoDialog {
    @Parcelize
    data class Input(val title: String, val info: String) : Parcelable
}