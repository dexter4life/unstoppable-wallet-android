package io.fastpayd.wallet.modules.send.address

import io.fastpayd.wallet.core.IAdapterManager
import io.fastpayd.wallet.entities.Address
import io.horizontalsystems.marketkit.models.Token

interface EnterAddressValidator {
    suspend fun validate(address: Address)
}

class BitcoinAddressValidator(private val token: Token, private val adapterManager: IAdapterManager) : EnterAddressValidator {
    override suspend fun validate(address: Address) {
    }
}

class EvmAddressValidator : EnterAddressValidator {
    override suspend fun validate(address: Address) {
    }
}

class SolanaAddressValidator : EnterAddressValidator {
    override suspend fun validate(address: Address) {
    }
}

class TonAddressValidator : EnterAddressValidator {
    override suspend fun validate(address: Address) {
    }
}

class StellarAddressValidator(private val token: Token) : EnterAddressValidator {
    override suspend fun validate(address: Address) {
    }
}

class TronAddressValidator(private val token: Token, private val adapterManager: IAdapterManager) : EnterAddressValidator {
    override suspend fun validate(address: Address) {
    }
}

class ZcashAddressValidator(private val token: Token, private val adapterManager: IAdapterManager) : EnterAddressValidator {
    override suspend fun validate(address: Address) {
    }
}

sealed class AddressValidationError : Throwable() {
    class NoAdapter : AddressValidationError()
    class SendToSelfForbidden(override val message: String) : AddressValidationError()
}