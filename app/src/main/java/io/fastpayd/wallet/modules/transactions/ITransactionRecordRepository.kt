package io.fastpayd.wallet.modules.transactions

import io.fastpayd.wallet.core.Clearable
import io.fastpayd.wallet.entities.transactionrecords.TransactionRecord
import io.fastpayd.wallet.modules.contacts.model.Contact
import io.horizontalsystems.marketkit.models.Blockchain
import io.reactivex.Observable

interface ITransactionRecordRepository : Clearable {
    val itemsObservable: Observable<List<TransactionRecord>>

    fun set(
        transactionWallets: List<TransactionWallet>,
        wallet: TransactionWallet?,
        transactionType: FilterTransactionType,
        blockchain: Blockchain?,
        contact: Contact?
    )
    fun loadNext()
    fun reload()
}
