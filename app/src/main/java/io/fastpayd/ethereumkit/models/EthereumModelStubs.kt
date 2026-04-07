package io.fastpayd.ethereumkit.models

import java.net.URI

data class Chain(
    val id: Int = 1,
    val name: String = "Ethereum",
    val isMainNet: Boolean = true
) {
    companion object {
        val Ethereum = Chain(1, "Ethereum")
        val BinanceSmartChain = Chain(56, "BSC")
        val Polygon = Chain(137, "Polygon")
        val Avalanche = Chain(43114, "Avalanche")
        val Optimism = Chain(10, "Optimism")
        val Base = Chain(8453, "Base")
        val ZkSync = Chain(324, "ZkSync")
        val ArbitrumOne = Chain(42161, "Arbitrum")
        val Gnosis = Chain(100, "Gnosis")
        val Fantom = Chain(250, "Fantom")
    }
}

sealed class RpcSource {
    data class Http(
        val uris: List<URI>,
        val auth: String? = null
    ) : RpcSource()

    data class WebSocket(
        val uri: URI,
        val auth: String? = null
    ) : RpcSource()
}

enum class TransactionSource {
    Rpc,
    Explorer;

    companion object {
        fun ethereumEtherscan(apiKey: String): TransactionSource = Explorer
        fun bscscan(apiKey: String): TransactionSource = Explorer
        fun polygonscan(apiKey: String): TransactionSource = Explorer
        fun snowtrace(apiKey: String): TransactionSource = Explorer
        fun optimisticEtherscan(apiKey: String): TransactionSource = Explorer
        fun arbiscan(apiKey: String): TransactionSource = Explorer
        fun gnosis(apiKey: String): TransactionSource = Explorer
        fun fantom(apiKey: String): TransactionSource = Explorer
        fun basescan(apiKey: String): TransactionSource = Explorer
        fun eraZkSync(apiKey: String): TransactionSource = Explorer
    }
}

data class Address(val hex: String) {
    val eip55: String
        get() = hex

    companion object {
        fun fromHex(hex: String): Address = Address(hex)
    }
}

open class TransactionData

data class Transaction(
    val hashString: String,
    val transactionIndex: Int? = 0,
    val blockNumber: Long? = null,
    val timestamp: Long = 0,
    val isFailed: Boolean = false,
    val gasUsed: Long? = null,
    val gasLimit: Long? = null,
    val gasPrice: Long? = null
)

data class FullTransaction(
    val transaction: Transaction,
    val decoration: Any? = null
)

sealed class GasPrice {
    data class Legacy(val value: Long) : GasPrice()
}