package io.fastpayd.bitcoincash

class MainNetBitcoinCash {
    enum class CoinType {
        Type0,
        Type145,
    }

    val coinType: Int
        get() = 145
}