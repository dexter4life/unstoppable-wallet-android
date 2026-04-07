package io.fastpayd.wallet.core.adapters

import io.fastpayd.wallet.core.ICoinManager
import io.fastpayd.wallet.modules.transactions.TransactionSource
import io.horizontalsystems.marketkit.models.Token

class TonTransactionConverter(
    address: Any,
    coinManager: ICoinManager,
    source: TransactionSource,
    baseToken: Token,
)