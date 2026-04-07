package io.fastpayd.wallet.modules.send.evm.processing

import io.fastpayd.wallet.core.ViewModelUiState

class SendEvmProcessingViewModel: ViewModelUiState<SendEvmProcessingUiState>() {

    override fun createState(): SendEvmProcessingUiState {
            TODO("Not yet implemented")
    }
}

data class SendEvmProcessingUiState(
    val processing: Boolean
)