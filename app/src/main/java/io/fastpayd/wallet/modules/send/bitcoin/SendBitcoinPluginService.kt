package io.fastpayd.wallet.modules.send.bitcoin

import io.fastpayd.bitcoincore.core.IPluginData
import io.fastpayd.hodler.HodlerData
import io.fastpayd.hodler.HodlerPlugin
import io.fastpayd.hodler.LockTimeInterval
import io.horizontalsystems.marketkit.models.BlockchainType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SendBitcoinPluginService(blockchainType: BlockchainType) {
    val isLockTimeEnabled = blockchainType is BlockchainType.Bitcoin
    val lockTimeIntervals = listOf(null) + LockTimeInterval.values().toList()

    private var lockTimeInterval: LockTimeInterval? = null
    private var pluginData: Map<Byte, IPluginData>? = null

    private val _stateFlow = MutableStateFlow(
        State(
            lockTimeInterval = lockTimeInterval,
            pluginData = pluginData
        )
    )
    val stateFlow = _stateFlow.asStateFlow()

    fun setLockTimeInterval(lockTimeInterval: LockTimeInterval?) {
        this.lockTimeInterval = lockTimeInterval

        refreshPluginData()

        emitState()
    }

    private fun refreshPluginData() {
        pluginData = lockTimeInterval?.let {
            mapOf(HodlerPlugin.id to HodlerData(it))
        }
    }

    private fun emitState() {
        _stateFlow.update {
            State(
                lockTimeInterval = lockTimeInterval,
                pluginData = pluginData
            )
        }
    }

    data class State(
        val lockTimeInterval: LockTimeInterval?,
        val pluginData: Map<Byte, IPluginData>?
    )

}
