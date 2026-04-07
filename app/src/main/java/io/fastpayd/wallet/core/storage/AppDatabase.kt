package io.fastpayd.wallet.core.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.fastpayd.wallet.core.storage.migrations.Migration_31_32
import io.fastpayd.wallet.core.storage.migrations.Migration_32_33
import io.fastpayd.wallet.core.storage.migrations.Migration_33_34
import io.fastpayd.wallet.core.storage.migrations.Migration_34_35
import io.fastpayd.wallet.core.storage.migrations.Migration_35_36
import io.fastpayd.wallet.core.storage.migrations.Migration_36_37
import io.fastpayd.wallet.core.storage.migrations.Migration_37_38
import io.fastpayd.wallet.core.storage.migrations.Migration_38_39
import io.fastpayd.wallet.core.storage.migrations.Migration_39_40
import io.fastpayd.wallet.core.storage.migrations.Migration_40_41
import io.fastpayd.wallet.core.storage.migrations.Migration_41_42
import io.fastpayd.wallet.core.storage.migrations.Migration_42_43
import io.fastpayd.wallet.core.storage.migrations.Migration_43_44
import io.fastpayd.wallet.core.storage.migrations.Migration_44_45
import io.fastpayd.wallet.core.storage.migrations.Migration_45_46
import io.fastpayd.wallet.core.storage.migrations.Migration_46_47
import io.fastpayd.wallet.core.storage.migrations.Migration_47_48
import io.fastpayd.wallet.core.storage.migrations.Migration_48_49
import io.fastpayd.wallet.core.storage.migrations.Migration_49_50
import io.fastpayd.wallet.core.storage.migrations.Migration_50_51
import io.fastpayd.wallet.core.storage.migrations.Migration_51_52
import io.fastpayd.wallet.core.storage.migrations.Migration_52_53
import io.fastpayd.wallet.core.storage.migrations.Migration_53_54
import io.fastpayd.wallet.core.storage.migrations.Migration_54_55
import io.fastpayd.wallet.core.storage.migrations.Migration_55_56
import io.fastpayd.wallet.core.storage.migrations.Migration_56_57
import io.fastpayd.wallet.core.storage.migrations.Migration_57_58
import io.fastpayd.wallet.core.storage.migrations.Migration_58_59
import io.fastpayd.wallet.core.storage.migrations.Migration_59_60
import io.fastpayd.wallet.core.storage.migrations.Migration_60_61
import io.fastpayd.wallet.core.storage.migrations.Migration_61_62
import io.fastpayd.wallet.core.storage.migrations.Migration_62_63
import io.fastpayd.wallet.core.storage.migrations.Migration_63_64
import io.fastpayd.wallet.entities.ActiveAccount
import io.fastpayd.wallet.entities.BlockchainSettingRecord
import io.fastpayd.wallet.entities.EnabledWallet
import io.fastpayd.wallet.entities.EnabledWalletCache
import io.fastpayd.wallet.entities.EvmAddressLabel
import io.fastpayd.wallet.entities.EvmMethodLabel
import io.fastpayd.wallet.entities.EvmSyncSourceRecord
import io.fastpayd.wallet.entities.LogEntry
import io.fastpayd.wallet.entities.RecentAddress
import io.fastpayd.wallet.entities.RestoreSettingRecord
import io.fastpayd.wallet.entities.SpamAddress
import io.fastpayd.wallet.entities.SpamScanState
import io.fastpayd.wallet.entities.StatRecord
import io.fastpayd.wallet.entities.SyncerState
import io.fastpayd.wallet.entities.TokenAutoEnabledBlockchain
import io.fastpayd.wallet.entities.nft.NftAssetBriefMetadataRecord
import io.fastpayd.wallet.entities.nft.NftAssetRecord
import io.fastpayd.wallet.entities.nft.NftCollectionRecord
import io.fastpayd.wallet.entities.nft.NftMetadataSyncRecord
import io.fastpayd.wallet.modules.chart.ChartIndicatorSetting
import io.fastpayd.wallet.modules.chart.ChartIndicatorSettingsDao
import io.fastpayd.wallet.modules.pin.core.Pin
import io.fastpayd.wallet.modules.pin.core.PinDao
import io.fastpayd.wallet.modules.profeatures.storage.ProFeaturesDao
import io.fastpayd.wallet.modules.profeatures.storage.ProFeaturesSessionKey
import io.fastpayd.wallet.modules.walletconnect.storage.WCSessionDao
import io.fastpayd.wallet.modules.walletconnect.storage.WalletConnectV2Session

@Database(version = 64, exportSchema = false, entities = [
    EnabledWallet::class,
    EnabledWalletCache::class,
    AccountRecord::class,
    BlockchainSettingRecord::class,
    EvmSyncSourceRecord::class,
    LogEntry::class,
    FavoriteCoin::class,
    WalletConnectV2Session::class,
    RestoreSettingRecord::class,
    ActiveAccount::class,
    NftCollectionRecord::class,
    NftAssetRecord::class,
    NftMetadataSyncRecord::class,
    NftAssetBriefMetadataRecord::class,
    ProFeaturesSessionKey::class,
    EvmAddressLabel::class,
    EvmMethodLabel::class,
    SyncerState::class,
    TokenAutoEnabledBlockchain::class,
    ChartIndicatorSetting::class,
    Pin::class,
    StatRecord::class,
    SpamAddress::class,
    SpamScanState::class,
    RecentAddress::class,
])

@TypeConverters(DatabaseConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun chartIndicatorSettingsDao(): ChartIndicatorSettingsDao
    abstract fun walletsDao(): EnabledWalletsDao
    abstract fun enabledWalletsCacheDao(): EnabledWalletsCacheDao
    abstract fun accountsDao(): AccountsDao
    abstract fun blockchainSettingDao(): BlockchainSettingDao
    abstract fun evmSyncSourceDao(): EvmSyncSourceDao
    abstract fun restoreSettingDao(): RestoreSettingDao
    abstract fun logsDao(): LogsDao
    abstract fun marketFavoritesDao(): MarketFavoritesDao
    abstract fun wcSessionDao(): WCSessionDao
    abstract fun nftDao(): NftDao
    abstract fun proFeaturesDao(): ProFeaturesDao
    abstract fun evmAddressLabelDao(): EvmAddressLabelDao
    abstract fun evmMethodLabelDao(): EvmMethodLabelDao
    abstract fun syncerStateDao(): SyncerStateDao
    abstract fun tokenAutoEnabledBlockchainDao(): TokenAutoEnabledBlockchainDao
    abstract fun pinDao(): PinDao
    abstract fun statsDao(): StatsDao
    abstract fun spamAddressDao(): SpamAddressDao
    abstract fun recentAddressDao(): RecentAddressDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "dbBankWallet")
//                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .addMigrations(
                            Migration_31_32,
                            Migration_32_33,
                            Migration_33_34,
                            Migration_34_35,
                            Migration_35_36,
                            Migration_36_37,
                            Migration_37_38,
                            Migration_38_39,
                            Migration_39_40,
                            Migration_40_41,
                            Migration_41_42,
                            Migration_42_43,
                            Migration_43_44,
                            Migration_44_45,
                            Migration_45_46,
                            Migration_46_47,
                            Migration_47_48,
                            Migration_48_49,
                            Migration_49_50,
                            Migration_50_51,
                            Migration_51_52,
                            Migration_52_53,
                            Migration_53_54,
                            Migration_54_55,
                            Migration_55_56,
                            Migration_56_57,
                            Migration_57_58,
                            Migration_58_59,
                            Migration_59_60,
                            Migration_60_61,
                            Migration_61_62,
                            Migration_62_63,
                            Migration_63_64,
                    )
                    .build()
        }

    }
}
