package io.fastpayd.wallet.modules.walletconnect

object WCUtils {
    fun getChainData(string: String): ChainData? {
        val chunks = string.split(":")
        if (chunks.size < 2) {
            return null
        }
        val eip = chunks[0]
        if (eip != "eip155") return null

        val chainId = chunks[1].toIntOrNull() ?: return null
        val address: String? = when {
            chunks.size >= 3 -> chunks[2]
            else -> null
        }

        return ChainData(chainId, address)
    }

    data class ChainData(
        val chainId: Int,
        val address: String?
    )
}