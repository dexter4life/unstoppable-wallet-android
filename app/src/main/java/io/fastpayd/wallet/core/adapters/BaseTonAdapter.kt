package io.fastpayd.wallet.core.adapters

import io.fastpayd.wallet.core.IAdapter
import io.fastpayd.wallet.core.IBalanceAdapter
import io.fastpayd.wallet.core.IReceiveAdapter
import io.fastpayd.wallet.core.managers.TonKitWrapper
import io.fastpayd.wallet.core.managers.statusInfo
import io.fastpayd.tonkit.models.Network

abstract class BaseTonAdapter(
    tonKitWrapper: TonKitWrapper,
    val decimals: Int
) : IAdapter, IBalanceAdapter, IReceiveAdapter {

    protected val tonKit = tonKitWrapper.tonKit

    override val debugInfo: String
        get() = ""

    val statusInfo: Map<String, Any>
        get() = tonKit.statusInfo()

    // IReceiveAdapter

    override val receiveAddress: String
        get() = tonKit.receiveAddress.toUserFriendly(false)

    override val isMainNet: Boolean
        get() = tonKit.network == Network.MainNet

    // ISendTronAdapter
}
