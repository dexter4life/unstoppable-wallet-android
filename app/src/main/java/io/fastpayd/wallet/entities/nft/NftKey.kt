package io.fastpayd.wallet.entities.nft

import io.fastpayd.wallet.entities.Account
import io.horizontalsystems.marketkit.models.BlockchainType

data class NftKey(
    val account: Account,
    val blockchainType: BlockchainType
)