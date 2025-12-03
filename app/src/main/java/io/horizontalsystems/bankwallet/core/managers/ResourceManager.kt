package io.horizontalsystems.bankwallet.core.managers

import io.horizontalsystems.bankwallet.core.providers.AppConfigProvider
import io.horizontalsystems.bankwallet.core.providers.resource.ResourceProvider

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