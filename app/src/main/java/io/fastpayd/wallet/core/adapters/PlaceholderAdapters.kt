package io.fastpayd.wallet.core.adapters

import io.fastpayd.wallet.core.AdapterState
import io.fastpayd.wallet.core.BalanceData
import io.fastpayd.wallet.core.IAdapter
import io.fastpayd.wallet.core.IBalanceAdapter
import io.fastpayd.wallet.core.IPlaceholderAdapter
import io.fastpayd.wallet.core.IReceiveAdapter
import io.fastpayd.wallet.core.ITransactionsAdapter
import io.fastpayd.wallet.entities.LastBlockInfo
import io.fastpayd.wallet.entities.Wallet
import io.fastpayd.wallet.entities.transactionrecords.TransactionRecord
import io.fastpayd.wallet.modules.transactions.FilterTransactionType
import io.fastpayd.wallet.modules.transactions.TransactionSource
import io.horizontalsystems.marketkit.models.Token
import io.reactivex.Flowable
import io.reactivex.Single
import java.math.BigDecimal

class PlaceholderWalletAdapter(
    private val wallet: Wallet,
    override val placeholderText: String,
) : IAdapter, IBalanceAdapter, ITransactionsAdapter, IReceiveAdapter, IPlaceholderAdapter {

    override fun start() = Unit

    override fun stop() = Unit

    override fun refresh() = Unit

    override val debugInfo: String
        get() = placeholderText

    override val balanceState: AdapterState = AdapterState.Synced
    override val balanceStateUpdatedFlowable: Flowable<Unit> = Flowable.never()
    override val balanceData: BalanceData = BalanceData(BigDecimal.ZERO)
    override val balanceUpdatedFlowable: Flowable<Unit> = Flowable.never()

    override val explorerTitle: String
        get() = wallet.token.blockchain.name

    override val transactionsState: AdapterState = AdapterState.Synced
    override val transactionsStateUpdatedFlowable: Flowable<Unit> = Flowable.never()
    override val lastBlockInfo: LastBlockInfo? = null
    override val lastBlockUpdatedFlowable: Flowable<Unit> = Flowable.never()

    override fun getTransactionsAsync(
        from: TransactionRecord?,
        token: Token?,
        limit: Int,
        transactionType: FilterTransactionType,
        address: String?,
    ): Single<List<TransactionRecord>> = Single.just(emptyList())

    override fun getTransactionRecordsFlowable(
        token: Token?,
        transactionType: FilterTransactionType,
        address: String?,
    ): Flowable<List<TransactionRecord>> = Flowable.just(emptyList())

    override fun getTransactionUrl(transactionHash: String): String = ""

    override val receiveAddress: String
        get() = "placeholder:${wallet.token.blockchainType.uid}:${wallet.coin.code.lowercase()}"

    override val isMainNet: Boolean = true
}

class PlaceholderTransactionsAdapter(
    private val source: TransactionSource,
    override val placeholderText: String,
) : ITransactionsAdapter, IPlaceholderAdapter {

    override val explorerTitle: String
        get() = source.blockchain.name

    override val transactionsState: AdapterState = AdapterState.Synced
    override val transactionsStateUpdatedFlowable: Flowable<Unit> = Flowable.never()
    override val lastBlockInfo: LastBlockInfo? = null
    override val lastBlockUpdatedFlowable: Flowable<Unit> = Flowable.never()

    override fun getTransactionsAsync(
        from: TransactionRecord?,
        token: Token?,
        limit: Int,
        transactionType: FilterTransactionType,
        address: String?,
    ): Single<List<TransactionRecord>> = Single.just(emptyList())

    override fun getTransactionRecordsFlowable(
        token: Token?,
        transactionType: FilterTransactionType,
        address: String?,
    ): Flowable<List<TransactionRecord>> = Flowable.just(emptyList())

    override fun getTransactionUrl(transactionHash: String): String = ""
}