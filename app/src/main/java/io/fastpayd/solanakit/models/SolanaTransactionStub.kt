package io.fastpayd.solanakit.models

import java.math.BigDecimal

data class Transaction(
    val hash: String,
    val pending: Boolean = false,
    val timestamp: Long = 0,
    val error: Throwable? = null,
    val fee: BigDecimal? = null
)