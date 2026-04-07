package io.fastpayd.wallet.modules.market.topcoins

import io.fastpayd.wallet.modules.market.SortingField
import io.fastpayd.wallet.ui.compose.Select

sealed class SelectorDialogState {
    object Closed : SelectorDialogState()
    class Opened(val select: Select<SortingField>) : SelectorDialogState()
}
