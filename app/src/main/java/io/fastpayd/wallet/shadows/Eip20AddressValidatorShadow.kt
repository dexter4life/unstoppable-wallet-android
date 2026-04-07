package io.fastpayd.wallet.core.address

import io.fastpayd.wallet.core.managers.EvmSyncSourceManager
import io.fastpayd.wallet.entities.Address
import io.horizontalsystems.marketkit.models.BlockchainType
import io.horizontalsystems.marketkit.models.Token

class Eip20AddressValidator(val evmSyncSourceManager: EvmSyncSourceManager) {
    suspend fun isClear(address: Address, token: Token): Boolean = true

    suspend fun isClear(
        address: Address,
        coinUid: String,
        blockchainType: BlockchainType,
        contractAddress: String,
    ): Boolean = true

    fun supports(token: Token): Boolean = false
}

sealed class Method

sealed class TokenError : Exception() {
    object InvalidTokenType : TokenError()
    object InvalidAddress : TokenError()
    object InvalidContractAddress : TokenError()
    object NoSyncSource : TokenError()
    object NoMethod : TokenError()
}