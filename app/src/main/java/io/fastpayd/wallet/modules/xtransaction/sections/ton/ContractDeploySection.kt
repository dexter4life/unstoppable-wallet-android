package io.fastpayd.wallet.modules.xtransaction.sections.ton

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.fastpayd.wallet.R
import io.fastpayd.wallet.modules.xtransaction.cells.HeaderCell
import io.fastpayd.wallet.ui.compose.components.cell.SectionUniversalLawrence

@Composable
fun ContractDeploySection(
    interfaces: List<String>,
) {
    SectionUniversalLawrence {
        HeaderCell(
            title = stringResource(R.string.Transactions_ContractDeploy),
            value = interfaces.joinToString(),
            painter = null
        )
    }
}