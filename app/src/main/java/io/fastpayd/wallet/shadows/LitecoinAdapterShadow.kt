package io.fastpayd.wallet.core.adapters

import io.fastpayd.wallet.entities.AccountType
import io.horizontalsystems.marketkit.models.TokenType

class LitecoinAdapter {
    companion object {
        fun clear(walletId: String) {
        }

        fun firstAddress(accountType: AccountType, tokenType: TokenType): String {
            return when (accountType) {
                is AccountType.BitcoinAddress -> accountType.address
                else -> ""
            }
        }
    }
}