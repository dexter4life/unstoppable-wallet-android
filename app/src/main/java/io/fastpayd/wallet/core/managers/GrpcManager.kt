package io.fastpayd.wallet.core.managers

import io.fastpayd.wallet.core.providers.AppConfigProvider
import io.grpc.ManagedChannel
import io.grpc.okhttp.OkHttpChannelBuilder
import java.net.URI
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

class GrpcManager(
    private val appConfigProvider: AppConfigProvider,
) {

    private val channels = ConcurrentHashMap<String, ManagedChannel>()

    fun defaultChannel(): ManagedChannel {
        return channel(appConfigProvider.fastpaydGrpcAuthority, appConfigProvider.fastpaydGrpcUseTls)
    }

    fun channel(authority: String, useTls: Boolean = appConfigProvider.fastpaydGrpcUseTls): ManagedChannel {
        val normalizedAuthority = normalizeAuthority(authority, useTls)
        return channels.getOrPut(normalizedAuthority) {
            val endpoint = endpoint(normalizedAuthority)
            OkHttpChannelBuilder.forAddress(endpoint.host, endpoint.port)
                .apply {
                    if (endpoint.useTls) {
                        useTransportSecurity()
                    } else {
                        usePlaintext()
                    }
                }
                .keepAliveWithoutCalls(true)
                .build()
        }
    }

    fun shutdown() {
        channels.values.forEach { channel ->
            channel.shutdown().awaitTermination(3, TimeUnit.SECONDS)
        }
        channels.clear()
    }

    private fun normalizeAuthority(authority: String, useTls: Boolean): String {
        return if (authority.contains("://")) {
            authority
        } else {
            "${if (useTls) "https" else "http"}://$authority"
        }
    }

    private fun endpoint(authority: String): Endpoint {
        val uri = URI(authority)
        val isTls = uri.scheme.equals("https", ignoreCase = true)
        val port = if (uri.port != -1) uri.port else if (isTls) 443 else 80
        return Endpoint(uri.host, port, isTls)
    }

    private data class Endpoint(
        val host: String,
        val port: Int,
        val useTls: Boolean,
    )
}