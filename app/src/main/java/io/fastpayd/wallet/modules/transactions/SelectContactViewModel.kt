package io.fastpayd.wallet.modules.transactions

import androidx.lifecycle.viewmodel.CreationExtras
import io.fastpayd.wallet.core.App
import io.fastpayd.wallet.core.ViewModelUiState
import io.fastpayd.wallet.core.managers.EvmBlockchainManager
import io.fastpayd.wallet.modules.contacts.ContactsRepository
import io.fastpayd.wallet.modules.contacts.model.Contact
import io.horizontalsystems.marketkit.models.BlockchainType

class SelectContactViewModel(
    private val contactsRepository: ContactsRepository,
    private val selected: Contact?,
    private val blockchainType: BlockchainType?,
) : ViewModelUiState<SelectContactUiState>() {

    private var items: List<Contact?>

    init {
        val contacts = fetchContacts()

        items = when {
            contacts.isNotEmpty() -> listOf(null) + contacts
            else -> contacts
        }
        emitState()
    }

    private fun fetchContacts(): List<Contact> {
        if (blockchainType != null && !supportedBlockchainTypes.contains(blockchainType)) {
            return listOf()
        }

        return contactsRepository.getContactsFiltered(blockchainType).filter { contact ->
            contact.addresses.any {
                supportedBlockchainTypes.contains(it.blockchain.type)
            }
        }
    }

    override fun createState() = SelectContactUiState(
        items = items,
        selected = selected
    )

    companion object {
        val supportedBlockchainTypes =
            EvmBlockchainManager.blockchainTypes +
                    BlockchainType.Zcash +
                    BlockchainType.Tron +
                    BlockchainType.Ton +
                    BlockchainType.Stellar

        fun init(selected: Contact?, blockchainType: BlockchainType?): CreationExtras.() -> SelectContactViewModel = {
            SelectContactViewModel(App.contactsRepository, selected, blockchainType)
        }
    }
}

data class SelectContactUiState(
    val items: List<Contact?>,
    val selected: Contact?,
)
