package io.fastpayd.wallet.modules.authentication.register

import androidx.compose.runtime.mutableStateOf
import io.fastpayd.wallet.core.providers.resource.Country
import io.fastpayd.wallet.core.providers.resource.CountryData
import io.fastpayd.wallet.core.providers.resource.CountryFlag

class RegisterActivityModel(arg1: Any?, arg2: Any?) {
    val filteredCountries = mutableStateOf<List<Country>>(emptyList())
    val countryData = mutableStateOf<CountryData?>(null)

    companion object {
        val countryFlags: List<CountryFlag> = emptyList()
    }
}