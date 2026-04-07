package io.fastpayd.wallet.modules.settings.security

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.fastpayd.wallet.R
import io.fastpayd.wallet.core.authorizedAction
import io.fastpayd.wallet.core.ensurePinSet
import io.fastpayd.wallet.core.paidAction
import io.fastpayd.wallet.core.slideFromRight
import io.fastpayd.wallet.core.stats.StatEvent
import io.fastpayd.wallet.core.stats.StatPage
import io.fastpayd.wallet.core.stats.StatPremiumTrigger
import io.fastpayd.wallet.core.stats.stat
import io.fastpayd.wallet.modules.settings.security.passcode.SecuritySettingsViewModel
import io.fastpayd.wallet.ui.compose.ComposeAppTheme
import io.fastpayd.wallet.ui.compose.components.PremiumHeader
import io.fastpayd.wallet.ui.compose.components.body_leah
import io.fastpayd.wallet.ui.compose.components.body_lucian
import io.fastpayd.wallet.ui.compose.components.cell.SectionPremiumUniversalLawrence
import io.horizontalsystems.subscriptions.core.DuressMode

@Composable
fun DuressPasscodeBlock(
    viewModel: SecuritySettingsViewModel,
    navController: NavController
) {
    val uiState = viewModel.uiState
    PremiumHeader()
    SectionPremiumUniversalLawrence {
        SecurityCenterCell(
            start = {
                Icon(
                    painter = painterResource(R.drawable.ic_switch_wallet_24),
                    tint = ComposeAppTheme.colors.jacob,
                    modifier = Modifier.size(24.dp),
                    contentDescription = null,
                )
            },
            center = {
                val text = if (uiState.duressPinEnabled) {
                    R.string.SettingsSecurity_EditDuressPin
                } else {
                    R.string.SettingsSecurity_SetDuressPin
                }
                body_leah(
                    text = stringResource(text),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            onClick = {
                navController.paidAction(DuressMode) {
                    if (uiState.pinEnabled) {
                        navController.authorizedAction {
                            if (uiState.duressPinEnabled) {
                                navController.slideFromRight(R.id.editDuressPinFragment)
                            } else {
                                navController.slideFromRight(R.id.setDuressPinIntroFragment)
                            }
                        }
                    } else {
                        navController.ensurePinSet(R.string.PinSet_ForDuress) {
                            navController.slideFromRight(R.id.setDuressPinIntroFragment)
                        }
                    }
                }
                stat(
                    page = StatPage.Security,
                    event = StatEvent.OpenPremium(StatPremiumTrigger.DuressMode)
                )
            }
        )
        if (uiState.duressPinEnabled) {
            SecurityCenterCell(
                start = {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete_20),
                        tint = ComposeAppTheme.colors.lucian,
                        modifier = Modifier.size(24.dp),
                        contentDescription = null,
                    )
                },
                center = {
                    body_lucian(
                        text = stringResource(R.string.SettingsSecurity_DisableDuressPin),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                onClick = {
                    navController.authorizedAction {
                        viewModel.disableDuressPin()
                    }
                }
            )
        }
    }
}
