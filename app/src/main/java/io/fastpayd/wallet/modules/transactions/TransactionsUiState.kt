package io.fastpayd.wallet.modules.transactions

import io.fastpayd.wallet.entities.ViewState

data class TransactionsUiState(
    val transactions: Map<String, List<TransactionViewItem>>?,
    val viewState: ViewState,
    val transactionListId: String?,
    val syncing: Boolean
)
