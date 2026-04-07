package io.fastpayd.tronkit.transaction

data class Fee(
    val amount: Long = 0,
    val resource: String = "",
)