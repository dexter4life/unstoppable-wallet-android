package io.fastpayd.wallet.modules.profeatures.storage

import io.fastpayd.wallet.core.storage.AppDatabase
import io.fastpayd.wallet.modules.profeatures.ProNft

class ProFeaturesStorage(appDatabase: AppDatabase) {

    private val dao: ProFeaturesDao by lazy {
        appDatabase.proFeaturesDao()
    }

    fun add(sessionKey: ProFeaturesSessionKey) {
        dao.insert(sessionKey)
    }

    fun get(nftType: ProNft): ProFeaturesSessionKey? =
        dao.getOne(nftType.keyName)

    fun deleteAllExcept(accountIds: List<String>) {
        dao.deleteAllExcept(accountIds)
    }

    fun clear() {
        dao.clear()
    }

}
