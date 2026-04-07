package io.fastpayd.wallet.core.adapters

import io.fastpayd.wallet.core.AdapterState
import io.fastpayd.wallet.core.BalanceData
import io.fastpayd.wallet.core.IAdapter
import io.fastpayd.wallet.core.IBalanceAdapter
import io.fastpayd.wallet.core.IReceiveAdapter
import io.fastpayd.wallet.core.ISendTronAdapter
import io.fastpayd.wallet.core.ITransactionsAdapter
import io.fastpayd.wallet.core.managers.TronKitWrapper
import io.fastpayd.wallet.entities.LastBlockInfo
import io.fastpayd.wallet.entities.Wallet
import io.fastpayd.wallet.modules.transactions.FilterTransactionType
import io.fastpayd.wallet.entities.transactionrecords.TransactionRecord
import io.horizontalsystems.marketkit.models.Token
import io.horizontalsystems.marketkit.models.TokenQuery
import io.fastpayd.tronkit.models.Address as TronAddress
import io.fastpayd.tronkit.transaction.Fee
import io.reactivex.Flowable
import io.reactivex.Single
import java.math.BigDecimal

open class BaseTronAdapter(
    tronKitWrapper: TronKitWrapper,
    val decimal: Int,
) : IAdapter, IBalanceAdapter, IReceiveAdapter {
    override fun start() {}

    override fun stop() {}

    override fun refresh() {}

    override val debugInfo: String
        get() = ""

    val statusInfo: Map<String, Any>
        get() = mapOf("status" to "disabled")

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

    override suspend fun isAddressActive(address: String): Boolean = false

    open suspend fun isAddressActive(address: TronAddress): Boolean = false

    open fun isOwnAddress(address: TronAddress): Boolean = false

    companion object {
        const val confirmationsThreshold: Int = 19
    }
}

class TronAdapter(kitWrapper: TronKitWrapper) : BaseTronAdapter(kitWrapper, decimal), ISendTronAdapter {
    override val trxBalanceData: BalanceData
        get() = BalanceData(BigDecimal.ZERO)

    override suspend fun estimateFee(amount: BigDecimal, to: TronAddress): List<Fee> = listOf()

    override suspend fun send(amount: BigDecimal, to: TronAddress, feeLimit: Long?) {}

    override suspend fun isAddressActive(address: TronAddress): Boolean = false

    override fun isOwnAddress(address: TronAddress): Boolean = false

    companion object {
        const val decimal: Int = 6

        fun clear(accountId: String) {
        }
    }
}

class Trc20Adapter(
    tronKitWrapper: TronKitWrapper,
    val contractAddress: String,
    val wallet: Wallet,
) : BaseTronAdapter(tronKitWrapper, wallet.decimal), ISendTronAdapter {
    override val trxBalanceData: BalanceData
        get() = BalanceData(BigDecimal.ZERO)

    override suspend fun estimateFee(amount: BigDecimal, to: TronAddress): List<Fee> = listOf()

    override suspend fun send(amount: BigDecimal, to: TronAddress, feeLimit: Long?) {}

    override suspend fun isAddressActive(address: TronAddress): Boolean = false

    override fun isOwnAddress(address: TronAddress): Boolean = false
}

class TronTransactionConverter(
    val tronKitWrapper: TronKitWrapper,
    val source: io.fastpayd.wallet.modules.transactions.TransactionSource,
    val baseToken: Token,
)

class TronTransactionsAdapter(
    val tronKitWrapper: TronKitWrapper,
    private val transactionConverter: TronTransactionConverter,
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