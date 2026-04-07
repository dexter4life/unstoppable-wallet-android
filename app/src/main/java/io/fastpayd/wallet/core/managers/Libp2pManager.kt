package io.fastpayd.wallet.core.managers

import io.fastpayd.wallet.core.providers.AppConfigProvider

class Libp2pManager(
    private val appConfigProvider: AppConfigProvider,
) {

    val bootstrapPeers: List<String>
        get() = appConfigProvider.fastpaydLibp2pBootstrapPeers

    val isConfigured: Boolean
        get() = bootstrapPeers.isNotEmpty()

    fun bootstrapPeer(index: Int): String? {
        return bootstrapPeers.getOrNull(index)
    }

    fun allBootstrapPeers(): List<String> {
        return bootstrapPeers
    }
}