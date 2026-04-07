package io.fastpayd.wallet.core.adapters

import io.fastpayd.wallet.entities.TransactionValue
import io.fastpayd.wallet.entities.transactionrecords.TransactionRecord
import io.fastpayd.wallet.modules.transactions.TransactionSource
import io.fastpayd.wallet.modules.transactions.TransactionStatus
import io.horizontalsystems.marketkit.models.Token
import java.math.BigDecimal

class TonTransactionRecord(
    source: TransactionSource,
    uidValue: String,
    timestampValue: Long,
    baseToken: Token,
    val actions: List<Action>,
    val lt: Long = 0,
    val inProgress: Boolean = false,
    spam: Boolean = false,
) : TransactionRecord(
    uid = uidValue,
    transactionHash = uidValue,
    transactionIndex = 0,
    blockHeight = null,
    confirmationsThreshold = null,
    timestamp = timestampValue,
    failed = false,
    spam = spam,
    source = source,
) {
    val fee = TransactionValue.CoinValue(baseToken, BigDecimal.ZERO)

    override fun status(lastBlockHeight: Int?) = if (inProgress) TransactionStatus.Pending else TransactionStatus.Completed

    override val mainValue: TransactionValue?
        get() = actions.singleOrNull()?.let { action ->
            when (val type = action.type) {
                is Action.Type.Receive -> type.value
                is Action.Type.Send -> type.value
                is Action.Type.Burn -> type.value
                is Action.Type.Mint -> type.value
                is Action.Type.ContractCall -> type.value
                is Action.Type.ContractDeploy,
                is Action.Type.Swap,
                is Action.Type.Unsupported -> null
            }
        }

    data class Action(
        val type: Type,
        val status: TransactionStatus,
    ) {
        sealed class Type {
            data class Send(
                val value: TransactionValue,
                val to: String,
                val sentToSelf: Boolean,
                val comment: String?,
            ) : Type()

            data class Receive(
                val value: TransactionValue,
                val from: String,
                val comment: String?,
            ) : Type()

            data class Burn(val value: TransactionValue) : Type()

            data class Mint(val value: TransactionValue) : Type()

            data class Swap(
                val routerName: String?,
                val routerAddress: String,
                val valueIn: TransactionValue,
                val valueOut: TransactionValue,
            ) : Type()

            data class ContractDeploy(val interfaces: List<String>) : Type()

            data class ContractCall(
                val address: String,
                val value: TransactionValue,
                val operation: String,
            ) : Type()

            data class Unsupported(val type: String) : Type()
        }
    }
}

class StellarTransactionRecord(
    baseToken: Token,
    source: TransactionSource,
    uidValue: String,
    transactionHashValue: String,
    timestampValue: Long,
    val type: Type,
    val memo: String? = null,
    failed: Boolean = false,
) : TransactionRecord(
    uid = uidValue,
    transactionHash = transactionHashValue,
    transactionIndex = 0,
    blockHeight = null,
    confirmationsThreshold = null,
    timestamp = timestampValue,
    failed = failed,
    spam = false,
    source = source,
) {
    override val mainValue = type.mainValue
    val fee = TransactionValue.CoinValue(baseToken, BigDecimal.ZERO)

    sealed class Type {
        data class Send(
            val value: TransactionValue,
            val to: String,
            val sentToSelf: Boolean,
            val comment: String?,
            val accountCreated: Boolean,
        ) : Type()

        data class Receive(
            val value: TransactionValue,
            val from: String,
            val comment: String?,
            val accountCreated: Boolean,
        ) : Type()

        data class ChangeTrust(
            val trustee: String,
            val value: TransactionValue,
        ) : Type()

        class Unsupported(val type: String) : Type()

        val mainValue: TransactionValue?
            get() = when (this) {
                is Receive -> value
                is Send -> value
                is ChangeTrust -> value
                is Unsupported -> null
            }
    }

    override fun status(lastBlockHeight: Int?) = if (failed) TransactionStatus.Failed else TransactionStatus.Completed
}

object TonHelper {
    fun getViewItemsForAction(
        action: TonTransactionRecord.Action,
        rates: Map<String, io.fastpayd.wallet.entities.CurrencyValue>,
        blockchainType: io.horizontalsystems.marketkit.models.BlockchainType,
        hideAmount: Boolean,
        showHistoricalRate: Boolean,
    ): List<io.fastpayd.wallet.modules.transactionInfo.TransactionInfoViewItem> = emptyList()
}