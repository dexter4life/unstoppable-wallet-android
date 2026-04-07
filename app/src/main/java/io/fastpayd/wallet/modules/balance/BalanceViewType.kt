package io.fastpayd.wallet.modules.balance

import androidx.annotation.StringRes
import com.google.gson.annotations.SerializedName
import io.fastpayd.wallet.R
import io.fastpayd.wallet.ui.compose.TranslatableString
import io.fastpayd.wallet.ui.compose.WithTranslatableTitle

enum class BalanceViewType(@StringRes val titleResId: Int, @StringRes val subtitleResId: Int) :
    WithTranslatableTitle {
    @SerializedName("coin")
    CoinThenFiat(R.string.BalanceViewType_CoinValue, R.string.BalanceViewType_FiatValue),

    @SerializedName("currency")
    FiatThenCoin(R.string.BalanceViewType_FiatValue, R.string.BalanceViewType_CoinValue);

    override val title: TranslatableString
        get() = TranslatableString.ResString(titleResId)
}
