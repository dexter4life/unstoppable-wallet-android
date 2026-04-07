package io.fastpayd.wallet.modules.profeatures

import io.fastpayd.wallet.core.IAccountManager
import io.fastpayd.wallet.core.providers.AppConfigProvider
import io.fastpayd.wallet.core.storage.SecretString
import io.fastpayd.wallet.modules.profeatures.storage.ProFeaturesSessionKey
import io.fastpayd.wallet.modules.profeatures.storage.ProFeaturesStorage
import io.fastpayd.ethereumkit.models.Address
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProFeaturesAuthorizationManager(
    private val storage: ProFeaturesStorage,
    private val accountManager: IAccountManager,
    private val appConfigProvider: AppConfigProvider,
) {
    data class AccountData(val accountId: String, val address: Address)

    private val _sessionKeyFlow = MutableStateFlow<ProFeaturesSessionKey?>(null)
    val sessionKeyFlow = _sessionKeyFlow.asStateFlow()

    fun getSessionKey(nftType: ProNft): ProFeaturesSessionKey? = null

    fun saveSessionKey(nft: ProNft, accountData: AccountData, key: String) {
        _sessionKeyFlow.value = ProFeaturesSessionKey(nft.name, accountData.accountId, accountData.address.hex, SecretString(key))
    }

    suspend fun getNFTHolderAccountData(nftType: ProNft): AccountData? = null

    fun signMessage(accountData: AccountData, message: String): ByteArray = message.toByteArray()
}