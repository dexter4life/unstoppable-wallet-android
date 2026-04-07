package io.fastpayd.wallet.modules.send.ton

import io.fastpayd.wallet.R
import io.fastpayd.wallet.core.providers.Translator
import io.fastpayd.wallet.entities.Address
import io.fastpayd.tonkit.FriendlyAddress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SendTonAddressService {
    private var address: Address? = null
    private var addressError: Throwable? = null
    private var tonAddress: FriendlyAddress? = null

    private val _stateFlow = MutableStateFlow(
        State(
            address = address,
            tonAddress = tonAddress,
            addressError = addressError,
            canBeSend = tonAddress != null,
        )
    )
    val stateFlow = _stateFlow.asStateFlow()

    fun setAddress(address: Address?) {
        this.address = address

        validateAddress()

        emitState()
    }

    private fun validateAddress() {
        addressError = null
        tonAddress = null
        val address = this.address ?: return

        try {
            tonAddress = FriendlyAddress.parse(address.hex)
        } catch (e: Exception) {
            addressError = Throwable(Translator.getString(R.string.SwapSettings_Error_InvalidAddress))
        }
    }

    private fun emitState() {
        _stateFlow.update {
            State(
                address = address,
                tonAddress = tonAddress,
                addressError = addressError,
                canBeSend = tonAddress != null
            )
        }
    }

    data class State(
        val address: Address?,
        val tonAddress: FriendlyAddress?,
        val addressError: Throwable?,
        val canBeSend: Boolean
    )
}
