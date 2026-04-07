package io.fastpayd.wallet.core.adapters

import io.fastpayd.wallet.core.AdapterState
import io.fastpayd.wallet.core.BalanceData
import io.fastpayd.wallet.core.IAdapter
import io.fastpayd.wallet.core.IBalanceAdapter
import io.fastpayd.wallet.core.ICoinManager
import io.fastpayd.wallet.core.IReceiveAdapter
import io.fastpayd.wallet.core.ISendEthereumAdapter
import io.fastpayd.wallet.core.managers.EvmKitWrapper
import io.fastpayd.ethereumkit.models.Address
import io.fastpayd.ethereumkit.models.TransactionData
import io.reactivex.Flowable
import java.math.BigDecimal
import java.math.BigInteger

abstract class BaseEvmAdapter(
    final override val evmKitWrapper: EvmKitWrapper,
    val decimal: Int,
    val coinManager: ICoinManager,
) : IAdapter, ISendEthereumAdapter, IBalanceAdapter, IReceiveAdapter {
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

    override fun getTransactionData(amount: BigDecimal, address: Address): TransactionData = TransactionData()

    protected fun scaleDown(amount: BigDecimal, decimals: Int = decimal): BigDecimal = amount.movePointLeft(decimals)

    protected fun scaleUp(amount: BigDecimal, decimals: Int = decimal): BigInteger = amount.movePointRight(decimals).toBigInteger()

    companion object {
        const val confirmationsThreshold: Int = 12
    }
}

class EvmAdapter(evmKitWrapper: EvmKitWrapper, coinManager: ICoinManager) : BaseEvmAdapter(evmKitWrapper, decimal, coinManager) {
    companion object {
        const val decimal: Int = 18

        fun clear(accountId: String) {
        }
    }
}

class Eip20Adapter(
    evmKitWrapper: EvmKitWrapper,
    coinManager: ICoinManager,
    wallet: Any,
    contractAddress: String,
    baseToken: Any,
    evmLabelManager: Any,
) : BaseEvmAdapter(evmKitWrapper, 0, coinManager) {
    companion object {
        fun clear(accountId: String) {
        }
    }
}