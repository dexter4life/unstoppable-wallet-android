package io.fastpayd.wallet.core.managers

import io.fastpayd.wallet.core.IAccountCleaner
import io.fastpayd.wallet.core.adapters.BitcoinAdapter
import io.fastpayd.wallet.core.adapters.BitcoinCashAdapter
import io.fastpayd.wallet.core.adapters.DashAdapter
import io.fastpayd.wallet.core.adapters.ECashAdapter
import io.fastpayd.wallet.core.adapters.Eip20Adapter
import io.fastpayd.wallet.core.adapters.EvmAdapter
import io.fastpayd.wallet.core.adapters.SolanaAdapter
import io.fastpayd.wallet.core.adapters.TronAdapter
import io.fastpayd.wallet.core.adapters.zcash.ZcashAdapter

class AccountCleaner : IAccountCleaner {

    override fun clearAccounts(accountIds: List<String>) {
        accountIds.forEach { clearAccount(it) }
    }

    private fun clearAccount(accountId: String) {
        BitcoinAdapter.clear(accountId)
        BitcoinCashAdapter.clear(accountId)
        ECashAdapter.clear(accountId)
        DashAdapter.clear(accountId)
        EvmAdapter.clear(accountId)
        Eip20Adapter.clear(accountId)
        ZcashAdapter.clear(accountId)
        SolanaAdapter.clear(accountId)
        TronAdapter.clear(accountId)
    }

}
