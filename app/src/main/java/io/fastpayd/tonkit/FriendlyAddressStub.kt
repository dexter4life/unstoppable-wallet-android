package io.fastpayd.tonkit

data class FriendlyAddress(val value: String) {
    companion object {
        fun parse(raw: String): FriendlyAddress = FriendlyAddress(raw)
    }
}