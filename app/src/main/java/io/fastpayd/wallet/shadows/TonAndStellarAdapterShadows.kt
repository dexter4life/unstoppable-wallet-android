package io.fastpayd.wallet.core.adapters

import io.fastpayd.wallet.core.AdapterState
import io.fastpayd.wallet.core.IAdapter
import io.fastpayd.wallet.core.ISendStellarAdapter
import io.fastpayd.wallet.core.ISendTonAdapter
import io.fastpayd.wallet.core.ITransactionsAdapter
import io.fastpayd.wallet.core.managers.StellarKitWrapper
import io.fastpayd.wallet.core.managers.TonKitWrapper
import io.fastpayd.wallet.entities.LastBlockInfo
import io.fastpayd.wallet.modules.transactions.FilterTransactionType
import io.fastpayd.wallet.entities.transactionrecords.TransactionRecord
import io.fastpayd.wallet.modules.transactions.TransactionSource
import io.horizontalsystems.marketkit.models.Token
import io.horizontalsystems.marketkit.models.TokenQuery
import io.fastpayd.tonkit.FriendlyAddress
import io.reactivex.Flowable
import io.reactivex.Single
import java.math.BigDecimal

class TonAdapter(tonKitWrapper: TonKitWrapper) : ISendTonAdapter {
    override val availableBalance: BigDecimal
        get() = BigDecimal.ZERO

    override suspend fun send(amount: BigDecimal, address: FriendlyAddress, memo: String?) {}

    override suspend fun estimateFee(amount: BigDecimal, address: FriendlyAddress, memo: String?): BigDecimal = BigDecimal.ZERO

    val statusInfo: Map<String, Any>
        get() = mapOf("status" to "disabled")

    companion object {
        fun clear(accountId: String) {
        }

        fun getAmount(value: Long): BigDecimal = BigDecimal.ZERO
    }
}

class JettonAdapter(
    tonKitWrapper: TonKitWrapper,
    address: String,
    wallet: Token,
) : ISendTonAdapter {
    override val availableBalance: BigDecimal
        get() = BigDecimal.ZERO

    override suspend fun send(amount: BigDecimal, address: FriendlyAddress, memo: String?) {}

    override suspend fun estimateFee(amount: BigDecimal, address: FriendlyAddress, memo: String?): BigDecimal = BigDecimal.ZERO
}

class TonTransactionsAdapter(private val source: TransactionSource) : ITransactionsAdapter {
    class NotSupportedException : Exception()

    override val explorerTitle: String get() = ""
    override val transactionsState: AdapterState get() = AdapterState.Synced
    override val transactionsStateUpdatedFlowable: Flowable<Unit> get() = Flowable.empty()
    override val lastBlockInfo: LastBlockInfo? get() = null
    override val lastBlockUpdatedFlowable: Flowable<Unit> get() = Flowable.empty()
    override val additionalTokenQueries: List<TokenQuery> get() = listOf()
    override fun getTransactionsAsync(from: TransactionRecord?, token: Token?, limit: Int, transactionType: FilterTransactionType, address: String?): Single<List<TransactionRecord>> = Single.just(listOf())
    override fun getTransactionRecordsFlowable(token: Token?, transactionType: FilterTransactionType, address: String?): Flowable<List<TransactionRecord>> = Flowable.just(listOf())
    override fun getTransactionUrl(transactionHash: String): String = ""
}

class StellarAdapter(stellarKitWrapper: StellarKitWrapper) : ISendStellarAdapter {
    override val maxSendableBalance: BigDecimal
        get() = BigDecimal.ZERO

    override val fee: BigDecimal
        get() = BigDecimal.ZERO

    override fun validate(address: String) {}

    override suspend fun getMinimumSendAmount(address: String): BigDecimal? = null

    override suspend fun send(amount: BigDecimal, address: String, memo: String?) {}
}

class StellarAssetAdapter(
    stellarKitWrapper: StellarKitWrapper,
    code: String,
    issuer: String,
) : ISendStellarAdapter, IAdapter {
    class NoTrustlineError(val assetCode: String) : Exception()

    override fun start() {}
    override fun stop() {}
    override fun refresh() {}
    override val debugInfo: String get() = ""
    override val maxSendableBalance: BigDecimal get() = BigDecimal.ZERO
    override val fee: BigDecimal get() = BigDecimal.ZERO
    val receiveAddress: String get() = ""
    val isMainNet: Boolean get() = true
    val activationFee: BigDecimal get() = BigDecimal.ZERO
    override fun validate(address: String) {}
    override suspend fun getMinimumSendAmount(address: String): BigDecimal? = null
    override suspend fun send(amount: BigDecimal, address: String, memo: String?) {}
    fun isTrustlineEstablished(): Boolean = true
    fun validateActivation() {}
    suspend fun activate() {}
}

class StellarTransactionsAdapter(private val source: TransactionSource) : ITransactionsAdapter {
    override val explorerTitle: String get() = ""
    override val transactionsState: AdapterState get() = AdapterState.Synced
    override val transactionsStateUpdatedFlowable: Flowable<Unit> get() = Flowable.empty()
    override val lastBlockInfo: LastBlockInfo? get() = null
    override val lastBlockUpdatedFlowable: Flowable<Unit> get() = Flowable.empty()
    override val additionalTokenQueries: List<TokenQuery> get() = listOf()
    override fun getTransactionsAsync(from: TransactionRecord?, token: Token?, limit: Int, transactionType: FilterTransactionType, address: String?): Single<List<TransactionRecord>> = Single.just(listOf())
    override fun getTransactionRecordsFlowable(token: Token?, transactionType: FilterTransactionType, address: String?): Flowable<List<TransactionRecord>> = Flowable.just(listOf())
    override fun getTransactionUrl(transactionHash: String): String = ""
}