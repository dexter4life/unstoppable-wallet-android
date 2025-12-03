package io.horizontalsystems.bankwallet.core.providers.resource

interface IResourceProvider {
    suspend fun availableCountries(): CountryData
}