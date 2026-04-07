package io.fastpayd.wallet.core.address

import HashDitAddressValidator
import io.fastpayd.wallet.core.managers.EvmBlockchainManager
import io.fastpayd.wallet.core.managers.EvmSyncSourceManager
import io.fastpayd.wallet.core.managers.SpamManager
import io.fastpayd.wallet.core.providers.AppConfigProvider
import io.fastpayd.wallet.entities.Address
import io.horizontalsystems.marketkit.models.Token

class AddressCheckManager(
    spamManager: SpamManager,
    appConfigProvider: AppConfigProvider,
    evmBlockchainManager: EvmBlockchainManager,
    evmSyncSourceManager: EvmSyncSourceManager
) {
    private val checkers = mapOf(
        AddressCheckType.Phishing to PhishingAddressChecker(spamManager),
        AddressCheckType.Blacklist to BlacklistAddressChecker(
            HashDitAddressValidator(
                appConfigProvider.hashDitBaseUrl,
                appConfigProvider.hashDitApiKey,
                evmBlockchainManager
            ),
            Eip20AddressValidator(evmSyncSourceManager)
        ),
        AddressCheckType.Sanction to SanctionAddressChecker(
            ChainalysisAddressValidator(
                appConfigProvider.chainalysisBaseUrl,
                appConfigProvider.chainalysisApiKey
            )
        )
    )

    fun availableCheckTypes(token: Token): List<AddressCheckType> {
        return checkers.mapNotNull { (type, checker) -> if (checker.supports(token)) type else null }
    }

    suspend fun isClear(type: AddressCheckType, address: Address, token: Token): Boolean {
        return checkers[type]?.isClear(address, token) ?: true
    }
}
