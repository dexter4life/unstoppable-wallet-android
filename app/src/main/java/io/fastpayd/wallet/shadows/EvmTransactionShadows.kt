package io.fastpayd.wallet.core.adapters

import io.fastpayd.wallet.core.AdapterState
import io.fastpayd.wallet.core.ITransactionsAdapter
import io.fastpayd.wallet.entities.LastBlockInfo
import io.fastpayd.wallet.modules.transactions.FilterTransactionType
import io.fastpayd.wallet.entities.transactionrecords.TransactionRecord
import io.fastpayd.wallet.modules.transactions.TransactionSource
import io.horizontalsystems.marketkit.models.Token
import io.horizontalsystems.marketkit.models.TokenQuery
import io.reactivex.Flowable
import io.reactivex.Single

open class EvmTransactionConverter(
    coinManager: Any,
    evmKitWrapper: Any,
    private val source: TransactionSource,
    spamManager: Any,
    baseToken: Token,
    evmLabelManager: Any,
) {
    fun transactionRecord(transaction: Any): TransactionRecord = DummyShadowTransactionRecord(source)
}

class EvmTransactionsAdapter(
    source: TransactionSource,
    blockchainType: Any,
    coinManager: Any,
    evmBlockchainManager: Any,
    evmLabelManager: Any,
) : ITransactionsAdapter {
    override val explorerTitle: String
        get() = ""

    override val transactionsState: AdapterState
        get() = AdapterState.Synced

    override val transactionsStateUpdatedFlowable: Flowable<Unit>
        get() = Flowable.empty()

    override val lastBlockInfo: LastBlockInfo?
        get() = null

    override val lastBlockUpdatedFlowable: Flowable<Unit>
        get() = Flowable.empty()

    override val additionalTokenQueries: List<TokenQuery>
        get() = listOf()

    override fun getTransactionsAsync(
        from: TransactionRecord?,
        token: Token?,
        limit: Int,
        transactionType: FilterTransactionType,
        address: String?,
    ): Single<List<TransactionRecord>> = Single.just(listOf())

    override fun getTransactionRecordsFlowable(
        token: Token?,
        transactionType: FilterTransactionType,
        address: String?,
    ): Flowable<List<TransactionRecord>> = Flowable.just(listOf())

    override fun getTransactionUrl(transactionHash: String): String = ""
}

internal class DummyShadowTransactionRecord(source: TransactionSource) : TransactionRecord(
    uid = "shadow",
    transactionHash = "",
    transactionIndex = 0,
    blockHeight = null,
    confirmationsThreshold = null,
    timestamp = 0,
    source = source,
)