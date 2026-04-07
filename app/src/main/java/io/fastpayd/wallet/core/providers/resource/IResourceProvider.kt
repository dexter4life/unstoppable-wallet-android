package io.fastpayd.wallet.core.providers.resource

interface IResourceProvider {
    suspend fun availableCountries(): CountryData
}