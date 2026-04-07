package io.fastpayd.ethereumkit.crypto

import java.security.MessageDigest

object CryptoUtils {
    fun sha3(value: ByteArray): ByteArray = MessageDigest.getInstance("SHA-256").digest(value)
}