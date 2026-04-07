package io.fastpayd.solanakit.models

data class RpcSource(
    val name: String,
    val url: String = "https://example.invalid"
) {
    companion object {
        val TritonOne = RpcSource("TritonOne")
        val Serum = RpcSource("Serum")
    }
}

data class MintAccount(
    val address: String,
    val isNft: Boolean = false
)

data class FullTokenAccount(
    val mintAccount: MintAccount
)