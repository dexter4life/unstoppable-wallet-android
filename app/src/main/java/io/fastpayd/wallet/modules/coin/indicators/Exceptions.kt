package io.fastpayd.wallet.modules.coin.indicators

import io.fastpayd.wallet.R
import io.fastpayd.wallet.core.providers.Translator

class NotIntegerException : Exception() {
    override fun getLocalizedMessage(): String {
        return Translator.getString(R.string.Error_NotInteger)
    }
}

class OutOfRangeException(val lower: Int, val upper: Int) : Exception() {
    override fun getLocalizedMessage(): String {
        return Translator.getString(R.string.Error_OutOfRange, lower, upper)
    }
}