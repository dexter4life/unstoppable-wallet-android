package io.fastpayd.tronkit.models

data class Address(val raw: String = "") {
    val base58: String
        get() = raw

    companion object {
        fun fromBase58(address: String): Address = Address(address)
    }
}