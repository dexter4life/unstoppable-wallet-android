package io.fastpayd.ethereumkit.core

object EthereumKit {
    fun init() {
    }
}

object AddressValidator {
    open class AddressValidationException(message: String) : IllegalArgumentException(message)

    fun validate(address: String) {
        if (address.isBlank()) {
            throw AddressValidationException("Address is blank")
        }
    }
}

fun ByteArray.toRawHexString(): String = joinToString(separator = "") { "%02x".format(it) }

fun String.hexStringToByteArray(): ByteArray {
    val clean = removePrefix("0x")
    if (clean.isEmpty()) return byteArrayOf()

    return clean.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
}