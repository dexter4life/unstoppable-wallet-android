package io.fastpayd.ethereumkit.api.jsonrpc

class JsonRpc {
    open class Error(
        val code: Int = 0,
        override val message: String = ""
    ) : Exception(message)

    class ResponseError {
        class RpcError(val error: Error) : Exception(error.message)
    }
}