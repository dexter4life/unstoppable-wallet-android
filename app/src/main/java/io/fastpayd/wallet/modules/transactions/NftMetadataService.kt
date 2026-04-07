package io.fastpayd.wallet.modules.transactions

import io.fastpayd.wallet.core.managers.NftMetadataManager
import io.fastpayd.wallet.entities.nft.NftAssetBriefMetadata
import io.fastpayd.wallet.entities.nft.NftUid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NftMetadataService(
    val nftMetadataManager: NftMetadataManager
) {
    private val _assetsBriefMetadataFlow = MutableStateFlow<Map<NftUid, NftAssetBriefMetadata>>(mapOf())
    val assetsBriefMetadataFlow = _assetsBriefMetadataFlow.asStateFlow()

    fun assetsBriefMetadata(nftUids: Set<NftUid>): Map<NftUid, NftAssetBriefMetadata> =
        nftMetadataManager.assetsBriefMetadata(nftUids).associateBy { it.nftUid }

    suspend fun fetch(nftUids: Set<NftUid>) {
        //val assetBriefMetadata = nftMetadataManager.fetchAssetsBriefMetadata(nftUids)

        //handle(assetBriefMetadata)
    }

    private fun handle(assetsBriefMetadata: List<NftAssetBriefMetadata>) {
        nftMetadataManager.save(assetsBriefMetadata)

        _assetsBriefMetadataFlow.tryEmit(assetsBriefMetadata.associateBy { it.nftUid })
    }
}