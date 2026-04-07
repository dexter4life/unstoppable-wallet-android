package io.fastpayd.stellarkit.room

sealed class StellarAsset {
    abstract val id: String

    data object Native : StellarAsset() {
        override val id: String = "native"
    }

    data class Asset(val code: String, val issuer: String) : StellarAsset() {
        override val id: String = "$code:$issuer"
    }
}