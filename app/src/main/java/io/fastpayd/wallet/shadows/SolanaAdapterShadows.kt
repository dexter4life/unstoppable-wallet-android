package io.fastpayd.wallet.core.adapters

import io.fastpayd.wallet.core.AdapterState
import io.fastpayd.wallet.core.BalanceData
import io.fastpayd.wallet.core.IAdapter
import io.fastpayd.wallet.core.IBalanceAdapter
import io.fastpayd.wallet.core.IReceiveAdapter
import io.fastpayd.wallet.core.ISendSolanaAdapter
import io.fastpayd.wallet.core.ITransactionsAdapter
import io.fastpayd.wallet.core.managers.SolanaKitWrapper
import io.fastpayd.wallet.entities.LastBlockInfo
import io.fastpayd.wallet.entities.Wallet
import io.fastpayd.wallet.modules.transactions.FilterTransactionType
import io.fastpayd.wallet.entities.transactionrecords.TransactionRecord
import io.fastpayd.wallet.modules.transactions.TransactionSource
import io.horizontalsystems.marketkit.models.Token
import io.horizontalsystems.marketkit.models.TokenQuery
import io.fastpayd.solanakit.models.Address as SolanaAddress
import io.fastpayd.solanakit.models.FullTransaction
import io.reactivex.Flowable
import io.reactivex.Single
import java.math.BigDecimal

open class BaseSolanaAdapter(
    kitWrapper: SolanaKitWrapper,
    val decimal: Int,
) : IAdapter, IBalanceAdapter, IReceiveAdapter {
    override fun start() {}
    override fun stop() {}
    override fun refresh() {}

    override val debugInfo: String
        get() = ""

    override val balanceState: AdapterState
        get() = AdapterState.Synced

    override val balanceStateUpdatedFlowable: Flowable<Unit>
        get() = Flowable.empty()

    override val balanceData: BalanceData
        get() = BalanceData(BigDecimal.ZERO)

    override val balanceUpdatedFlowable: Flowable<Unit>
        get() = Flowable.empty()

    override val receiveAddress: String
        get() = ""

    override val isMainNet: Boolean
        get() = true

    companion object {
        const val confirmationsThreshold: Int = 15
    }
}

class SolanaAdapter(kitWrapper: SolanaKitWrapper) : BaseSolanaAdapter(kitWrapper, decimal), ISendSolanaAdapter {
    override val availableBalance: BigDecimal
        get() = BigDecimal.ZERO

    override suspend fun send(amount: BigDecimal, to: SolanaAddress): FullTransaction = FullTransaction()

    companion object {
        const val decimal: Int = 9

        fun clear(accountId: String) {
        }

        fun balanceInBigDecimal(balance: Long?, decimal: Int): BigDecimal {
            val value = balance ?: 0L
            return BigDecimal(value).movePointLeft(decimal)
        }
    }
}

class SplAdapter(
    solanaKitWrapper: SolanaKitWrapper,
    val address: String,
    val wallet: Wallet,
) : BaseSolanaAdapter(solanaKitWrapper, wallet.decimal), ISendSolanaAdapter {
    override val availableBalance: BigDecimal
        get() = BigDecimal.ZERO

    override suspend fun send(amount: BigDecimal, to: SolanaAddress): FullTransaction = FullTransaction()
}

class SolanaTransactionConverter(
    val solanaKitWrapper: SolanaKitWrapper,
    val source: TransactionSource,
    val baseToken: Token,
) {
    fun transactionRecord(fullTransaction: Any): TransactionRecord = DummyShadowTransactionRecord(source)
}

class SolanaTransactionsAdapter(
    val solanaKitWrapper: SolanaKitWrapper,
    private val solanaTransactionConverter: SolanaTransactionConverter,
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