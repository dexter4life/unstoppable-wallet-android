package io.fastpayd.wallet.modules.walletconnect

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.walletconnect.web3.wallet.client.Wallet
import com.walletconnect.web3.wallet.client.Web3Wallet
import io.fastpayd.wallet.core.managers.EvmBlockchainManager

class WCWalletRequestHandler(
    private val evmBlockchainManager: EvmBlockchainManager
) {
    private val gson by lazy { Gson() }

    fun handle(sessionRequest: Wallet.Model.SessionRequest): Boolean {
        try {
            val request = sessionRequest.request
            val params = JsonParser.parseString(sessionRequest.request.params).asJsonArray
            val chain = gson.fromJson(params.first(), WalletConnectChain::class.java)

            return when (request.method) {
                "wallet_addEthereumChain",
                "wallet_switchEthereumChain" -> {
                    val blockchain = parseChainId(chain.chainId)?.let { evmBlockchainManager.getBlockchain(it) }
                    if (blockchain != null) {
                        val response = Wallet.Params.SessionRequestResponse(
                            sessionTopic = sessionRequest.topic,
                            jsonRpcResponse = Wallet.Model.JsonRpcResponse.JsonRpcResult(
                                id = request.id,
                                result = "null"
                            )
                        )
                        Web3Wallet.respondSessionRequest(
                            params = response,
                            onSuccess = {},
                            onError = { error ->
                                Log.e("WCWalletHandler", "${request.method} response error: $error")
                            })

                    } else {
                        val result = Wallet.Params.SessionRequestResponse(
                            sessionTopic = sessionRequest.topic,
                            jsonRpcResponse = Wallet.Model.JsonRpcResponse.JsonRpcError(
                                id = request.id,
                                code = 4902,
                                message = "Unrecognized chain ID"
                            )
                        )
                        Web3Wallet.respondSessionRequest(result,
                            onSuccess = {},
                            onError = { error ->
                                Log.e("WCWalletHandler", "${request.method} response error: $error")
                            })
                    }

                    true
                }

                else -> false
            }
        } catch (error: Throwable) {
            return false
        }
    }

    private fun parseChainId(value: String): Int? {
        val sanitized = value.removePrefix("0x")
        return sanitized.toIntOrNull(16) ?: value.toIntOrNull()
    }

    data class WalletConnectChain(
        val chainId: String,
        val chainName: String?,
        val rpcUrls: List<String>?,
        val iconUrls: List<String>?,
        val nativeCurrency: WalletConnectNativeCurrency?,
        val blockExplorerUrls: List<String>?,
    )

    data class WalletConnectNativeCurrency(
        val name: String,
        val symbol: String,
        val decimals: Int,
    )

}
