package io.horizontalsystems.bankwallet.core.providers.resource
import com.tonapps.tonkeeper.api.withRetry
import io.horizontalsystems.bankwallet.core.managers.APIClient
import io.horizontalsystems.bankwallet.core.providers.EvmLabelProvider.HsLabelApi
import io.horizontalsystems.bankwallet.core.providers.nft.HsNftApi
import retrofit2.http.GET

data class Country(
    val code: String,
    val name: String,
)

data class CountryCode(
    val code: String,
    val name: String,
    val dialCode: String
)

data class CountryData (
    val countries: List<Country>,
    val currentCountry: Country? = null
)

data class CountryFlag(val code: String, val flagId: Int)

class ResourceService(
    private val baseUrl: String
) {
    private val service by lazy {
        APIClient.build("https://e99734ed3c7c.ngrok-free.app/api/", mapOf("apikey" to ""))
            .create(ResourceApi::class.java)
    }

    suspend fun allAvailableCountries(): CountryData
    {
        return try {
            service.availableCountries()
        } catch (e: Exception) {
            e.printStackTrace()
            CountryData(emptyList())
        }
    }
}

interface ResourceApi {
    @GET("/api/countries")
    suspend fun availableCountries(): CountryData
}