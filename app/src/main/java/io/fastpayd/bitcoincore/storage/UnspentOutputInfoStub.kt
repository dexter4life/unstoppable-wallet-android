package io.fastpayd.bitcoincore.storage

data class UnspentOutput(
    val value: Long = 0,
)

data class UnspentOutputInfo(
    val transactionHash: ByteArray = byteArrayOf(),
    val outputIndex: Int = 0,
)