package io.fastpayd.wallet.core.factories

import io.fastpayd.wallet.core.IAccountFactory
import io.fastpayd.wallet.core.IAccountManager
import io.fastpayd.wallet.core.managers.UserManager
import io.fastpayd.wallet.entities.Account
import io.fastpayd.wallet.entities.AccountOrigin
import io.fastpayd.wallet.entities.AccountType
import java.util.UUID

class AccountFactory(
    private val accountManager: IAccountManager,
    private val userManager: UserManager
) : IAccountFactory {

    override fun account(
        name: String,
        type: AccountType,
        origin: AccountOrigin,
        backedUp: Boolean,
        fileBackedUp: Boolean
    ): Account {
        val id = UUID.randomUUID().toString()

        return Account(
            id = id,
            name = name,
            type = type,
            origin = origin,
            level = userManager.getUserLevel(),
            isBackedUp = backedUp,
            isFileBackedUp = fileBackedUp
        )
    }

    override fun watchAccount(name: String, type: AccountType): Account {
        val id = UUID.randomUUID().toString()
        return Account(
            id = id,
            name = name,
            type = type,
            origin = AccountOrigin.Restored,
            level = userManager.getUserLevel(),
            isBackedUp = true
        )
    }

    override fun getNextWatchAccountName(): String {
        val watchAccountsCount = accountManager.accounts.count { it.isWatchAccount }

        return "Watch Address ${watchAccountsCount + 1}"
    }

    override fun getNextAccountName(): String {
        val nonWatchAccountsCount = accountManager.accounts.count { !it.isWatchAccount }

        return "Wallet ${nonWatchAccountsCount + 1}"
    }

}
