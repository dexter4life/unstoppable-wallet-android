package io.fastpayd.wallet.modules.multiswap

sealed class SwapError : Throwable() {
    object InsufficientBalanceFrom : SwapError()
}
