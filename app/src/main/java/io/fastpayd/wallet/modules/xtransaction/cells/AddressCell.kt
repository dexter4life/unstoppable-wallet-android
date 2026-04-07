package io.fastpayd.wallet.modules.xtransaction.cells

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.fastpayd.wallet.R
import io.fastpayd.wallet.core.slideFromRight
import io.fastpayd.wallet.core.stats.StatEntity
import io.fastpayd.wallet.core.stats.StatEvent
import io.fastpayd.wallet.core.stats.StatPage
import io.fastpayd.wallet.core.stats.StatSection
import io.fastpayd.wallet.core.stats.stat
import io.fastpayd.wallet.modules.contacts.ContactsFragment
import io.fastpayd.wallet.modules.contacts.ContactsModule
import io.fastpayd.wallet.modules.contacts.Mode
import io.fastpayd.wallet.ui.compose.components.ButtonSecondaryCircle
import io.fastpayd.wallet.ui.compose.components.HSpacer
import io.fastpayd.wallet.ui.compose.components.SelectorDialogCompose
import io.fastpayd.wallet.ui.compose.components.SelectorItem
import io.fastpayd.wallet.ui.compose.components.cell.CellUniversal
import io.fastpayd.wallet.ui.compose.components.subhead1_leah
import io.fastpayd.wallet.ui.compose.components.subhead2_grey
import io.fastpayd.wallet.ui.helpers.TextHelper
import io.horizontalsystems.core.helpers.HudHelper
import io.horizontalsystems.marketkit.models.BlockchainType

@Composable
fun AddressCell(
    title: String,
    value: String,
    showAddContactButton: Boolean,
    blockchainType: BlockchainType?,
    statPage: StatPage,
    statSection: StatSection,
    navController: NavController? = null,
    borderTop: Boolean = true
) {
    val view = LocalView.current
    var showSaveAddressDialog by remember { mutableStateOf(false) }
    CellUniversal(borderTop = borderTop) {
        subhead2_grey(text = title)

        HSpacer(16.dp)
        subhead1_leah(
            modifier = Modifier.weight(1f),
            text = value,
            textAlign = TextAlign.Right
        )

        if (showAddContactButton) {
            HSpacer(16.dp)
            ButtonSecondaryCircle(
                icon = R.drawable.icon_20_user_plus,
                onClick = { showSaveAddressDialog = true }
            )
        }

        HSpacer(16.dp)
        ButtonSecondaryCircle(
            icon = R.drawable.ic_copy_20,
            onClick = {
                TextHelper.copyText(value)
                HudHelper.showSuccessMessage(view, R.string.Hud_Text_Copied)

                stat(
                    page = statPage,
                    event = StatEvent.Copy(StatEntity.Address),
                    section = statSection
                )
            }
        )
    }

    if (showSaveAddressDialog) {
        SelectorDialogCompose(
            title = stringResource(R.string.Contacts_AddAddress),
            items = ContactsModule.AddAddressAction.entries.map {
                SelectorItem(stringResource(it.title), false, it)
            },
            onDismissRequest = {
                showSaveAddressDialog = false
            },
            onSelectItem = { action ->
                blockchainType?.let {
                    val args = when (action) {
                        ContactsModule.AddAddressAction.AddToNewContact -> {
                            stat(
                                page = statPage,
                                event = StatEvent.Open(StatPage.ContactNew),
                                section = statSection
                            )
                            ContactsFragment.Input(
                                Mode.AddAddressToNewContact(
                                    blockchainType,
                                    value
                                )
                            )
                        }

                        ContactsModule.AddAddressAction.AddToExistingContact -> {
                            stat(
                                page = statPage,
                                event = StatEvent.Open(StatPage.ContactAddToExisting),
                                section = statSection
                            )
                            ContactsFragment.Input(
                                Mode.AddAddressToExistingContact(
                                    blockchainType,
                                    value
                                )
                            )
                        }
                    }
                    navController?.slideFromRight(R.id.contactsFragment, args)
                }
            })
    }
}