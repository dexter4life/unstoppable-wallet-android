package io.fastpayd.wallet.core.adapters

import io.fastpayd.wallet.core.AdapterState
import io.fastpayd.wallet.core.BalanceData
import io.fastpayd.wallet.core.IAdapter
import io.fastpayd.wallet.core.IBalanceAdapter
import io.fastpayd.wallet.core.IReceiveAdapter
import io.fastpayd.wallet.core.ISendBitcoinAdapter
import io.fastpayd.wallet.core.ITransactionsAdapter
import io.fastpayd.wallet.entities.LastBlockInfo
import io.fastpayd.wallet.entities.TransactionDataSortMode
import io.fastpayd.wallet.entities.Wallet
import io.fastpayd.wallet.entities.transactionrecords.TransactionRecord
import io.fastpayd.wallet.entities.AccountType
import io.fastpayd.wallet.entities.transactionrecords.bitcoin.BitcoinTransactionRecord
import io.fastpayd.wallet.modules.transactions.FilterTransactionType
import io.horizontalsystems.marketkit.models.BlockchainType
import io.horizontalsystems.marketkit.models.Token
import io.horizontalsystems.marketkit.models.TokenType
import io.fastpayd.bitcoincore.BitcoinCore
import io.fastpayd.bitcoincore.core.IPluginData
import io.fastpayd.bitcoincore.rbf.ReplacementTransaction
import io.fastpayd.bitcoincore.rbf.ReplacementTransactionInfo
import io.fastpayd.bitcoincore.storage.UnspentOutputInfo
import io.fastpayd.bitcoincore.storage.UtxoFilters
import io.horizontalsystems.core.BackgroundManager
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import java.math.BigDecimal

data class BitcoinFeeInfo(
    val unspentOutputs: List<io.fastpayd.bitcoincore.storage.UnspentOutput>,
    val fee: BigDecimal,
    val changeValue: BigDecimal?,
    val changeAddress: io.fastpayd.bitcoincore.models.Address?,
)

abstract class BitcoinBaseAdapter(
    open val kit: Any,
    open val syncMode: BitcoinCore.SyncMode,
    private val backgroundManager: BackgroundManager,
    val wallet: Wallet,
    protected val decimal: Int = 8,
) : IAdapter, ITransactionsAdapter, IBalanceAdapter, IReceiveAdapter, ISendBitcoinAdapter {

    open val satoshisInBitcoin: BigDecimal = BigDecimal.TEN.pow(decimal)

    protected val balanceUpdatedSubject: PublishSubject<Unit> = PublishSubject.create()
    protected val lastBlockUpdatedSubject: PublishSubject<Unit> = PublishSubject.create()
    protected val adapterStateUpdatedSubject: PublishSubject<Unit> = PublishSubject.create()
    protected val transactionRecordsSubject: PublishSubject<List<TransactionRecord>> = PublishSubject.create()

    override fun start() {}

    override fun stop() {}

    override fun refresh() {}

    override val debugInfo: String
        get() = ""

    override val explorerTitle: String
        get() = ""

    override val transactionsState: AdapterState
        get() = AdapterState.Synced

    override val transactionsStateUpdatedFlowable: Flowable<Unit>
        get() = adapterStateUpdatedSubject.toFlowable(BackpressureStrategy.BUFFER)

    override val lastBlockInfo: LastBlockInfo?
        get() = null

    override val lastBlockUpdatedFlowable: Flowable<Unit>
        get() = lastBlockUpdatedSubject.toFlowable(BackpressureStrategy.BUFFER)

    override val balanceState: AdapterState
        get() = AdapterState.Synced

    override val balanceStateUpdatedFlowable: Flowable<Unit>
        get() = adapterStateUpdatedSubject.toFlowable(BackpressureStrategy.BUFFER)

    override val balanceData: BalanceData
        get() = BalanceData(BigDecimal.ZERO)

    override val balanceUpdatedFlowable: Flowable<Unit>
        get() = balanceUpdatedSubject.toFlowable(BackpressureStrategy.BUFFER)

    override val receiveAddress: String
        get() = ""

    override val isMainNet: Boolean
        get() = true

    override val unspentOutputs: List<UnspentOutputInfo>
        get() = listOf()

    override val blockchainType: BlockchainType
        get() = wallet.token.blockchainType

    val statusInfo: Map<String, Any>
        get() = mapOf("status" to "disabled")

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

    override fun availableBalance(
        feeRate: Int,
        address: String?,
        memo: String?,
        unspentOutputs: List<UnspentOutputInfo>?,
        pluginData: Map<Byte, IPluginData>?,
        dustThreshold: Int?,
        changeToFirstInput: Boolean,
        utxoFilters: UtxoFilters,
    ): BigDecimal = BigDecimal.ZERO

    override fun minimumSendAmount(address: String?, dustThreshold: Int?): BigDecimal? = null

    override fun bitcoinFeeInfo(
        amount: BigDecimal,
        feeRate: Int,
        address: String?,
        memo: String?,
        unspentOutputs: List<UnspentOutputInfo>?,
        pluginData: Map<Byte, IPluginData>?,
        dustThreshold: Int?,
        changeToFirstInput: Boolean,
        filters: UtxoFilters,
    ): BitcoinFeeInfo? = null

    override fun validate(address: String, pluginData: Map<Byte, IPluginData>?) {}

    override fun send(
        amount: BigDecimal,
        address: String,
        memo: String?,
        feeRate: Int,
        unspentOutputs: List<UnspentOutputInfo>?,
        pluginData: Map<Byte, IPluginData>?,
        transactionSorting: TransactionDataSortMode?,
        rbfEnabled: Boolean,
        dustThreshold: Int?,
        changeToFirstInput: Boolean,
        utxoFilters: UtxoFilters,
    ): BitcoinTransactionRecord? = null

    fun speedUpTransactionInfo(transactionHash: String): ReplacementTransactionInfo? = null

    fun cancelTransactionInfo(transactionHash: String): ReplacementTransactionInfo? = null

    fun speedUpTransaction(transactionHash: String, minFee: Long): Pair<ReplacementTransaction, BitcoinTransactionRecord> =
        throw UnsupportedOperationException()

    fun cancelTransaction(transactionHash: String, minFee: Long): Pair<ReplacementTransaction, BitcoinTransactionRecord> =
        throw UnsupportedOperationException()

    fun send(replacementTransaction: ReplacementTransaction): Any = Unit

    protected fun setState(state: Any) {
        adapterStateUpdatedSubject.onNext(Unit)
    }

    protected fun transactionRecord(info: Any): TransactionRecord = DummyShadowTransactionRecord(wallet.transactionSource)
}

class BitcoinAdapter {
    companion object {
        fun clear(accountId: String) {
        }

        fun firstAddress(accountType: AccountType, tokenType: TokenType): String {
            return when (accountType) {
                is AccountType.BitcoinAddress -> accountType.address
                else -> ""
            }
        }
    }
}

class BitcoinCashAdapter {
    companion object {
        fun clear(accountId: String) {
        }

        fun firstAddress(accountType: io.fastpayd.wallet.entities.AccountType, tokenType: TokenType): String {
            return when (accountType) {
                is io.fastpayd.wallet.entities.AccountType.BitcoinAddress -> accountType.address
                else -> ""
            }
        }
    }
}

class DashAdapter {
    companion object {
        fun clear(accountId: String) {
        }
    }
}

class ECashAdapter {
    companion object {
        fun clear(accountId: String) {
        }
    }
}