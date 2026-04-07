package io.fastpayd.wallet.core.adapters

import io.fastpayd.wallet.core.IAdapter
import io.fastpayd.wallet.core.IBalanceAdapter
import io.fastpayd.wallet.core.IReceiveAdapter
import io.fastpayd.wallet.core.managers.StellarKitWrapper

abstract class BaseStellarAdapter(
    stellarKitWrapper: StellarKitWrapper
): IAdapter, IBalanceAdapter, IReceiveAdapter {
    protected val stellarKit = stellarKitWrapper.stellarKit
    override val receiveAddress: String = stellarKit.receiveAddress

    override val debugInfo: String
        get() = ""

    // IReceiveAdapter

    override val isMainNet = stellarKit.isMainNet
}
