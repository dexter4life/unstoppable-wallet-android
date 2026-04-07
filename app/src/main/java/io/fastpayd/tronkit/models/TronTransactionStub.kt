package io.fastpayd.tronkit.models

data class Transaction(
    val hashString: String,
    val blockNumber: Long? = null,
    val timestamp: Long = 0,
    val isFailed: Boolean = false,
    val fee: Long? = null,
    val confirmed: Boolean = false,
    val contract: Contract? = null,
)