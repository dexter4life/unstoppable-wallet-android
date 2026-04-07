package io.fastpayd.wallet.modules.address

import android.os.Parcelable
import io.fastpayd.wallet.entities.Address
import io.horizontalsystems.marketkit.models.BlockchainType
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
import java.util.UUID

interface IAddressHandler {
    val blockchainType: BlockchainType
    fun isSupported(value: String): Boolean
    fun parseAddress(value: String): Address
}

@Parcelize
sealed class AddressValidationException : Exception(), Parcelable {
    data object Blank : AddressValidationException()
    class Unsupported(val blockchain: String? = null) : AddressValidationException()
    class Invalid(override val cause: Throwable, val blockchain: String? = null) : AddressValidationException()
}

private class DefaultAddressHandler(
    override val blockchainType: BlockchainType,
) : IAddressHandler {
    override fun isSupported(value: String): Boolean = value.isNotBlank()

    override fun parseAddress(value: String): Address {
        if (value.isBlank()) throw AddressValidationException.Blank
        return Address(value, blockchainType = blockchainType)
    }
}

class AddressParserChain(
    handlers: List<IAddressHandler> = emptyList(),
    domainHandlers: List<IAddressHandler> = emptyList(),
) {
    private val handlers = handlers.toMutableList()
    private val domainHandlers = domainHandlers.toMutableList()

    fun supportedAddressHandlers(address: String): List<IAddressHandler> {
        return handlers.filter { it.isSupported(address) }.ifEmpty { listOf(DefaultAddressHandler(BlockchainType.Unsupported(""))) }
    }

    fun supportedHandler(address: String): IAddressHandler? {
        return supportedAddressHandlers(address).firstOrNull()
    }

    fun addHandler(handler: IAddressHandler) {
        handlers.add(handler)
    }

    fun getAddressFromDomain(address: String): Address? {
        return domainHandlers.firstOrNull { it.isSupported(address) }?.parseAddress(address)
    }
}

class AddressHandlerFactory(
    private val udnApiKey: String,
) {
    fun parserChain(blockchainType: BlockchainType?, resolveDomains: Boolean = false): AddressParserChain {
        val defaultHandler = DefaultAddressHandler(blockchainType ?: BlockchainType.Unsupported(""))
        return AddressParserChain(handlers = listOf(defaultHandler), domainHandlers = if (resolveDomains) listOf(defaultHandler) else emptyList())
    }
}

data class AmountUnique(val amount: BigDecimal, val id: Long = UUID.randomUUID().leastSignificantBits)