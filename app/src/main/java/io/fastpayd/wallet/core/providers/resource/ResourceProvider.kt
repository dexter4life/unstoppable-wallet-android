package io.fastpayd.wallet.core.providers.resource

open class ResourceProvider (baseUrl: String) : IResourceProvider {
    private val service: ResourceService = ResourceService(baseUrl)

    override suspend fun availableCountries(): CountryData {
        val response = service.allAvailableCountries()
        return response
    }
}