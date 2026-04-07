package io.fastpayd.ethereumkit.core.signer

import io.fastpayd.ethereumkit.models.Chain
import java.math.BigInteger
import java.security.MessageDigest

object Signer {
    private fun digest(bytes: ByteArray): String {
        return MessageDigest.getInstance("SHA-256")
            .digest(bytes)
            .joinToString(separator = "") { "%02x".format(it) }
            .takeLast(40)
    }

    fun address(seed: ByteArray, chain: Chain): String = "0x${digest(seed + chain.id.toByte())}"

    fun address(key: BigInteger): String = "0x${key.toString(16).padStart(40, '0').takeLast(40)}"

    fun getInstance(seed: ByteArray, chain: Chain): Instance = Instance(seed)

    fun getInstance(key: BigInteger, chain: Chain): Instance = Instance(key.toByteArray())

    class Instance(private val payload: ByteArray) {
        fun signByteArrayLegacy(message: ByteArray): ByteArray = message

        fun signByteArray(message: ByteArray): ByteArray = message
    }
}