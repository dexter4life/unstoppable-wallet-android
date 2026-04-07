package io.fastpayd.wallet.modules.profeatures

import io.fastpayd.wallet.core.IAccountManager
import io.fastpayd.wallet.core.orNull
import io.fastpayd.wallet.core.providers.AppConfigProvider
import io.fastpayd.wallet.core.storage.SecretString
import io.fastpayd.wallet.entities.Account
import io.fastpayd.wallet.entities.AccountType
import io.fastpayd.wallet.modules.profeatures.storage.ProFeaturesSessionKey
import io.fastpayd.wallet.modules.profeatures.storage.ProFeaturesStorage
import io.fastpayd.ethereumkit.core.Eip1155Provider
import io.fastpayd.ethereumkit.core.signer.EthSigner
import io.fastpayd.ethereumkit.core.signer.Signer
import io.fastpayd.ethereumkit.crypto.CryptoUtils
import io.fastpayd.ethereumkit.crypto.EIP712Encoder
import io.fastpayd.ethereumkit.models.Address
import io.fastpayd.ethereumkit.models.Chain
import io.fastpayd.ethereumkit.models.RpcSource
import io.reactivex.Single
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.rx2.await
import kotlinx.coroutines.withContext
import java.math.BigInteger
import java.net.URI
import java.util.Optional

class ProFeaturesAuthorizationManager(
    private val storage: ProFeaturesStorage,
    private val accountManager: IAccountManager,
    private val appConfigProvider: AppConfigProvider
) {

    data class AccountData(
        val id: String,
        val address: Address
    )

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val contractAddress = Address("0x495f947276749ce646f68ac8c248420045cb7b5e")

    private val _sessionKeyFlow = MutableStateFlow<ProFeaturesSessionKey?>(null)
    val sessionKeyFlow = _sessionKeyFlow.asStateFlow()

    private val getAllAccountData: List<AccountData>
        get() {
            val accounts = mutableListOf<Account>()

            val activeAccount = accountManager.activeAccount
            activeAccount?.let { accounts.add(it) }

            val inactiveAccounts = accountManager.accounts.filter { it.id != activeAccount?.id }
            accounts.addAll(inactiveAccounts)

            return accounts.mapNotNull { account ->
                when (account.type) {
                    is AccountType.EvmPrivateKey -> {
                        val address = Signer.address(account.type.key)
                        AccountData(account.id, address)
                    }

                    is AccountType.Mnemonic -> {
                        val address = Signer.address(account.type.seed, Chain.Ethereum)
                        AccountData(account.id, address)
                    }

                    else -> null
                }
            }
        }

    init {
        coroutineScope.launch {
            accountManager.accountsDeletedFlowable.asFlow().collect {
                handleDeletedAccounts()
            }
        }
    }

    fun getSessionKey(nftType: ProNft): ProFeaturesSessionKey? =
        storage.get(nftType)

    fun saveSessionKey(nft: ProNft, accountData: AccountData, key: String) {
        val sessionKey = ProFeaturesSessionKey(nft.keyName, accountData.id, accountData.address.eip55, SecretString(key))

        storage.add(sessionKey)
        _sessionKeyFlow.update { sessionKey }
    }

    suspend fun getNFTHolderAccountData(nftType: ProNft): AccountData? = withContext(Dispatchers.IO) {
        val accounts = getAllAccountData
        val provider = Eip1155Provider.instance(RpcSource.Http(listOf(URI(appConfigProvider.blocksDecodedEthereumRpc)), null))

        return@withContext first1155TokenHolder(provider, nftType.tokenId, accounts).await().orNull
    }

    fun signMessage(accountData: AccountData, message: String): ByteArray {
        val account = accountManager.account(accountData.id) ?: throw Exception("Account not found")
        val privateKey = when (account.type) {
            is AccountType.EvmPrivateKey -> {
                account.type.key
            }

            is AccountType.Mnemonic -> {
                Signer.privateKey(account.type.seed, Chain.Ethereum)
            }

            else -> throw Exception("AccountType not supported")
        }

        val ethSigner = EthSigner(privateKey, CryptoUtils, EIP712Encoder())

        return ethSigner.signByteArray(message.toByteArray(Charsets.UTF_8))
    }

    private fun first1155TokenHolder(provider: Eip1155Provider, tokenId: BigInteger, accounts: List<AccountData>): Single<Optional<AccountData>> {
        val firstAccount = accounts.firstOrNull() ?: return Single.just(Optional.ofNullable(null))

        return provider.getTokenBalance(contractAddress, tokenId, firstAccount.address).flatMap { balance ->
            if (balance > BigInteger.ZERO) return@flatMap Single.just(Optional.of(firstAccount))

            return@flatMap first1155TokenHolder(provider, tokenId, accounts.subList(1, accounts.size))
        }
    }

    private fun handleDeletedAccounts() {
        val accountIds = accountManager.accounts.map { it.id }

        storage.deleteAllExcept(accountIds)
    }

}
