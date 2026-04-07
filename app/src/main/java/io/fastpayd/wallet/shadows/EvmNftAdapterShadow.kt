package io.fastpayd.wallet.core.adapters.nft

import io.fastpayd.wallet.entities.nft.NftRecord
import io.fastpayd.wallet.entities.nft.NftUid
import io.fastpayd.ethereumkit.models.Address
import io.fastpayd.ethereumkit.models.TransactionData
import io.horizontalsystems.marketkit.models.BlockchainType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.math.BigInteger

class EvmNftAdapter(
    val blockchainType: BlockchainType,
    val nftKit: Any,
    private val rawUserAddress: Any,
) : INftAdapter {
    override val userAddress: String
        get() = rawUserAddress.toString()

    override val nftRecordsFlow: Flow<List<NftRecord>>
        get() = flowOf(listOf())

    override val nftRecords: List<NftRecord>
        get() = listOf()

    override fun nftRecord(nftUid: NftUid): NftRecord? = null

    override fun sync() {}

    override fun transferEip721TransactionData(
        contractAddress: String,
        to: Address,
        tokenId: String,
    ): TransactionData? = null

    override fun transferEip1155TransactionData(
        contractAddress: String,
        to: Address,
        tokenId: String,
        value: BigInteger,
    ): TransactionData? = null
}