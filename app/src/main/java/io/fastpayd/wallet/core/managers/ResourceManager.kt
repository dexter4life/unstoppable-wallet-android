package io.fastpayd.wallet.core.managers

import io.fastpayd.wallet.core.providers.AppConfigProvider
import io.fastpayd.wallet.core.providers.resource.ResourceProvider

open class ResourceManager(
    appConfigProvider: AppConfigProvider
) {
    private val provider by lazy {
        ResourceProvider(appConfigProvider.baseUrl)
    }


    open fun provider(): ResourceProvider {
        return provider
    }
}