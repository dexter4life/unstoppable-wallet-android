package io.fastpayd.wallet.core

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import com.walletconnect.android.Core
import com.walletconnect.android.CoreClient
import com.walletconnect.android.relay.ConnectionType
import com.walletconnect.web3.wallet.client.Wallet
import com.walletconnect.web3.wallet.client.Web3Wallet
import io.fastpayd.wallet.BuildConfig
import io.fastpayd.wallet.core.factories.AccountFactory
import io.fastpayd.wallet.core.factories.AdapterFactory
import io.fastpayd.wallet.core.factories.EvmAccountManagerFactory
import io.fastpayd.wallet.core.managers.AccountCleaner
import io.fastpayd.wallet.core.managers.AccountManager
import io.fastpayd.wallet.core.managers.ActionCompletedDelegate
import io.fastpayd.wallet.core.managers.AdapterManager
import io.fastpayd.wallet.core.managers.AppVersionManager
import io.fastpayd.wallet.core.managers.BackupManager
import io.fastpayd.wallet.core.managers.BalanceHiddenManager
import io.fastpayd.wallet.core.managers.BaseTokenManager
import io.fastpayd.wallet.core.managers.BtcBlockchainManager
import io.fastpayd.wallet.core.managers.CoinManager
import io.fastpayd.wallet.core.managers.ConnectivityManager
import io.fastpayd.wallet.core.managers.CurrencyManager
import io.fastpayd.wallet.core.managers.DonationShowManager
import io.fastpayd.wallet.core.managers.EvmBlockchainManager
import io.fastpayd.wallet.core.managers.EvmLabelManager
import io.fastpayd.wallet.core.managers.EvmSyncSourceManager
import io.fastpayd.wallet.core.managers.KeyStoreCleaner
import io.fastpayd.wallet.core.managers.LanguageManager
import io.fastpayd.wallet.core.managers.Libp2pManager
import io.fastpayd.wallet.core.managers.LocalStorageManager
import io.fastpayd.wallet.core.managers.MarketFavoritesManager
import io.fastpayd.wallet.core.managers.MarketKitWrapper
import io.fastpayd.wallet.core.managers.GrpcManager
import io.fastpayd.wallet.core.managers.NetworkManager
import io.fastpayd.wallet.core.managers.NftAdapterManager
import io.fastpayd.wallet.core.managers.NftMetadataManager
import io.fastpayd.wallet.core.managers.NftMetadataSyncer
import io.fastpayd.wallet.core.managers.NumberFormatter
import io.fastpayd.wallet.core.managers.PriceManager
import io.fastpayd.wallet.core.managers.RateAppManager
import io.fastpayd.wallet.core.managers.RecentAddressManager
import io.fastpayd.wallet.core.managers.ReleaseNotesManager
import io.fastpayd.wallet.core.managers.ResourceManager
import io.fastpayd.wallet.core.managers.RestoreSettingsManager
import io.fastpayd.wallet.core.managers.SolanaKitManager
import io.fastpayd.wallet.core.managers.SolanaRpcSourceManager
import io.fastpayd.wallet.core.managers.SolanaWalletManager
import io.fastpayd.wallet.core.managers.SpamManager
import io.fastpayd.wallet.core.managers.StellarAccountManager
import io.fastpayd.wallet.core.managers.StellarKitManager
import io.fastpayd.wallet.core.managers.SystemInfoManager
import io.fastpayd.wallet.core.managers.TermsManager
import io.fastpayd.wallet.core.managers.TokenAutoEnableManager
import io.fastpayd.wallet.core.managers.TonAccountManager
import io.fastpayd.wallet.core.managers.TonConnectManager
import io.fastpayd.wallet.core.managers.TonKitManager
import io.fastpayd.wallet.core.managers.TorManager
import io.fastpayd.wallet.core.managers.TransactionAdapterManager
import io.fastpayd.wallet.core.managers.TronAccountManager
import io.fastpayd.wallet.core.managers.TronKitManager
import io.fastpayd.wallet.core.managers.UserManager
import io.fastpayd.wallet.core.managers.WalletActivator
import io.fastpayd.wallet.core.managers.WalletManager
import io.fastpayd.wallet.core.managers.WalletStorage
import io.fastpayd.wallet.core.managers.WordsManager
import io.fastpayd.wallet.core.managers.ZcashBirthdayProvider
import io.fastpayd.wallet.core.providers.AppConfigProvider
import io.fastpayd.wallet.core.providers.EvmLabelProvider
import io.fastpayd.wallet.core.providers.FeeRateProvider
import io.fastpayd.wallet.core.providers.FeeTokenProvider
import io.fastpayd.wallet.core.stats.StatsManager
import io.fastpayd.wallet.core.storage.AccountsStorage
import io.fastpayd.wallet.core.storage.AppDatabase
import io.fastpayd.wallet.core.storage.BlockchainSettingsStorage
import io.fastpayd.wallet.core.storage.EnabledWalletsStorage
import io.fastpayd.wallet.core.storage.EvmSyncSourceStorage
import io.fastpayd.wallet.core.storage.NftStorage
import io.fastpayd.wallet.core.storage.RestoreSettingsStorage
import io.fastpayd.wallet.core.storage.SpamAddressStorage
import io.fastpayd.wallet.modules.backuplocal.fullbackup.BackupProvider
import io.fastpayd.wallet.modules.balance.BalanceViewTypeManager
import io.fastpayd.wallet.modules.chart.ChartIndicatorManager
import io.fastpayd.wallet.modules.contacts.ContactsRepository
import io.fastpayd.wallet.modules.market.favorites.MarketFavoritesMenuService
import io.fastpayd.wallet.modules.market.topplatforms.TopPlatformsRepository
import io.fastpayd.wallet.modules.pin.PinComponent
import io.fastpayd.wallet.modules.pin.core.PinDbStorage
import io.fastpayd.wallet.modules.profeatures.ProFeaturesAuthorizationManager
import io.fastpayd.wallet.modules.profeatures.storage.ProFeaturesStorage
import io.fastpayd.wallet.modules.roi.RoiManager
import io.fastpayd.wallet.modules.settings.appearance.AppIconService
import io.fastpayd.wallet.modules.settings.appearance.LaunchScreenService
import io.fastpayd.wallet.modules.theme.ThemeService
import io.fastpayd.wallet.modules.theme.ThemeType
import io.fastpayd.wallet.modules.walletconnect.WCManager
import io.fastpayd.wallet.modules.walletconnect.WCSessionManager
import io.fastpayd.wallet.modules.walletconnect.WCWalletRequestHandler
import io.fastpayd.wallet.modules.walletconnect.storage.WCSessionStorage
import io.fastpayd.wallet.widgets.MarketWidgetManager
import io.fastpayd.wallet.widgets.MarketWidgetRepository
import io.fastpayd.wallet.widgets.MarketWidgetWorker
import io.horizontalsystems.core.BackgroundManager
import io.horizontalsystems.core.BackgroundManagerState.AllActivitiesDestroyed
import io.horizontalsystems.core.BackgroundManagerState.EnterBackground
import io.horizontalsystems.core.BackgroundManagerState.EnterForeground
import io.horizontalsystems.core.CoreApp
import io.horizontalsystems.core.ICoreApp
import io.horizontalsystems.core.security.EncryptionManager
import io.horizontalsystems.core.security.KeyStoreManager
import io.fastpayd.ethereumkit.core.EthereumKit
import io.fastpayd.hdwalletkit.Mnemonic
import io.horizontalsystems.subscriptions.core.UserSubscriptionManager
import io.reactivex.plugins.RxJavaPlugins
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.logging.Level
import java.util.logging.Logger
import androidx.work.Configuration as WorkConfiguration

class App : CoreApp(), WorkConfiguration.Provider, ImageLoaderFactory {

    companion object : ICoreApp by CoreApp {

        lateinit var preferences: SharedPreferences
        lateinit var feeRateProvider: FeeRateProvider
        lateinit var localStorage: ILocalStorage
        lateinit var marketStorage: IMarketStorage
        lateinit var torKitManager: ITorManager
        lateinit var restoreSettingsStorage: IRestoreSettingsStorage
        lateinit var currencyManager: CurrencyManager
        lateinit var languageManager: LanguageManager

        lateinit var blockchainSettingsStorage: BlockchainSettingsStorage
        lateinit var evmSyncSourceStorage: EvmSyncSourceStorage
        lateinit var btcBlockchainManager: BtcBlockchainManager
        lateinit var wordsManager: WordsManager
        lateinit var networkManager: INetworkManager
        lateinit var grpcManager: GrpcManager
        lateinit var libp2pManager: Libp2pManager
        lateinit var appConfigProvider: AppConfigProvider
        lateinit var adapterManager: IAdapterManager
        lateinit var transactionAdapterManager: TransactionAdapterManager
        lateinit var walletManager: IWalletManager
        lateinit var walletActivator: WalletActivator
        lateinit var tokenAutoEnableManager: TokenAutoEnableManager
        lateinit var walletStorage: IWalletStorage
        lateinit var accountManager: IAccountManager
        lateinit var userManager: UserManager
        lateinit var accountFactory: IAccountFactory
        lateinit var backupManager: IBackupManager
        lateinit var proFeatureAuthorizationManager: ProFeaturesAuthorizationManager
        lateinit var zcashBirthdayProvider: ZcashBirthdayProvider

        lateinit var connectivityManager: ConnectivityManager
        lateinit var appDatabase: AppDatabase
        lateinit var accountsStorage: IAccountsStorage
        lateinit var enabledWalletsStorage: IEnabledWalletStorage
        lateinit var solanaKitManager: SolanaKitManager
        lateinit var tronKitManager: TronKitManager
        lateinit var tonKitManager: TonKitManager
        lateinit var stellarKitManager: StellarKitManager
        lateinit var numberFormatter: IAppNumberFormatter
        lateinit var feeCoinProvider: FeeTokenProvider
        lateinit var accountCleaner: IAccountCleaner
        lateinit var rateAppManager: IRateAppManager
        lateinit var coinManager: ICoinManager
        lateinit var wcSessionManager: WCSessionManager
        lateinit var wcManager: WCManager
        lateinit var wcWalletRequestHandler: WCWalletRequestHandler
        lateinit var termsManager: ITermsManager
        lateinit var marketFavoritesManager: MarketFavoritesManager
        lateinit var marketKit: MarketKitWrapper
        lateinit var priceManager: PriceManager
        lateinit var releaseNotesManager: ReleaseNotesManager
        lateinit var donationShowManager: DonationShowManager
        lateinit var restoreSettingsManager: RestoreSettingsManager
        lateinit var evmSyncSourceManager: EvmSyncSourceManager
        lateinit var evmBlockchainManager: EvmBlockchainManager
        lateinit var solanaRpcSourceManager: SolanaRpcSourceManager
        lateinit var nftMetadataManager: NftMetadataManager

        lateinit var resourceManager: ResourceManager
        lateinit var nftAdapterManager: NftAdapterManager
        lateinit var nftMetadataSyncer: NftMetadataSyncer
        lateinit var evmLabelManager: EvmLabelManager
        lateinit var baseTokenManager: BaseTokenManager
        lateinit var balanceViewTypeManager: BalanceViewTypeManager
        lateinit var balanceHiddenManager: BalanceHiddenManager
        lateinit var marketWidgetManager: MarketWidgetManager
        lateinit var marketWidgetRepository: MarketWidgetRepository
        lateinit var contactsRepository: ContactsRepository
        lateinit var chartIndicatorManager: ChartIndicatorManager
        lateinit var backupProvider: BackupProvider
        lateinit var spamManager: SpamManager
        lateinit var statsManager: StatsManager
        lateinit var tonConnectManager: TonConnectManager
        lateinit var recentAddressManager: RecentAddressManager
        lateinit var roiManager: RoiManager
        var trialExpired: Boolean = false
    }

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()

        if (!BuildConfig.DEBUG) {
            //Disable logging for lower levels in Release build
            Logger.getLogger("").level = Level.SEVERE
        }

        RxJavaPlugins.setErrorHandler { e: Throwable? ->
            Log.w("RxJava ErrorHandler", e)
        }

        instance = this
        preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        LocalStorageManager(preferences).apply {
            localStorage = this
            pinSettingsStorage = this
            lockoutStorage = this
            thirdKeyboardStorage = this
            marketStorage = this
        }

        val appConfig = AppConfigProvider(localStorage)
        appConfigProvider = appConfig

        torKitManager = TorManager(instance, localStorage)

        marketKit = MarketKitWrapper(
            context = this,
            hsApiBaseUrl = appConfig.marketApiBaseUrl,
            hsApiKey = appConfig.marketApiKey,
        )

        priceManager = PriceManager(localStorage)

        feeRateProvider = FeeRateProvider(appConfigProvider)
        backgroundManager = BackgroundManager(this)

        appDatabase = AppDatabase.getInstance(this)

        blockchainSettingsStorage = BlockchainSettingsStorage(appDatabase)
        evmSyncSourceStorage = EvmSyncSourceStorage(appDatabase)
        evmSyncSourceManager = EvmSyncSourceManager(appConfigProvider, blockchainSettingsStorage, evmSyncSourceStorage)

        btcBlockchainManager = BtcBlockchainManager(blockchainSettingsStorage, marketKit)

        accountsStorage = AccountsStorage(appDatabase)
        restoreSettingsStorage = RestoreSettingsStorage(appDatabase)

        AppLog.logsDao = appDatabase.logsDao()

        accountCleaner = AccountCleaner()
        accountManager = AccountManager(accountsStorage, accountCleaner)
        userManager = UserManager(accountManager)

        val proFeaturesStorage = ProFeaturesStorage(appDatabase)
        proFeatureAuthorizationManager = ProFeaturesAuthorizationManager(proFeaturesStorage, accountManager, appConfigProvider)

        enabledWalletsStorage = EnabledWalletsStorage(appDatabase)
        walletStorage = WalletStorage(marketKit, enabledWalletsStorage)

        walletManager = WalletManager(accountManager, walletStorage)
        coinManager = CoinManager(marketKit, walletManager)

        solanaRpcSourceManager = SolanaRpcSourceManager(blockchainSettingsStorage, marketKit)
        val solanaWalletManager = SolanaWalletManager(walletManager, accountManager, marketKit)
        solanaKitManager = SolanaKitManager(appConfigProvider, solanaRpcSourceManager, solanaWalletManager, backgroundManager)

        tronKitManager = TronKitManager(appConfigProvider, backgroundManager)
        tonKitManager = TonKitManager(backgroundManager)
        stellarKitManager = StellarKitManager(backgroundManager)

        wordsManager = WordsManager(Mnemonic())
        networkManager = NetworkManager()
        grpcManager = GrpcManager(appConfigProvider)
        libp2pManager = Libp2pManager(appConfigProvider)
        accountFactory = AccountFactory(accountManager, userManager)
        backupManager = BackupManager(accountManager)


        KeyStoreManager(
            keyAlias = "MASTER_KEY",
            keyStoreCleaner = KeyStoreCleaner(localStorage, accountManager, walletManager),
            logger = AppLogger("key-store")
        ).apply {
            keyStoreManager = this
            keyProvider = this
        }

        encryptionManager = EncryptionManager(keyProvider)

        walletActivator = WalletActivator(walletManager, marketKit)
        tokenAutoEnableManager = TokenAutoEnableManager(appDatabase.tokenAutoEnabledBlockchainDao())

        spamManager = SpamManager(localStorage, coinManager, SpamAddressStorage(appDatabase.spamAddressDao()), marketKit, appConfigProvider)
        recentAddressManager = RecentAddressManager(accountManager, appDatabase.recentAddressDao(), ActionCompletedDelegate)
        val evmAccountManagerFactory = EvmAccountManagerFactory(
            accountManager,
            walletManager,
            marketKit,
            tokenAutoEnableManager
        )
        evmBlockchainManager = EvmBlockchainManager(
            backgroundManager,
            evmSyncSourceManager,
            marketKit,
            evmAccountManagerFactory,
            spamManager
        )

        val tronAccountManager = TronAccountManager(
            accountManager,
            walletManager,
            tronKitManager,
            marketKit,
            tokenAutoEnableManager
        )
        tronAccountManager.start()

        val tonAccountManager = TonAccountManager(accountManager, walletManager, tonKitManager, tokenAutoEnableManager)
        tonAccountManager.start()

        val stellarAccountManager = StellarAccountManager(accountManager, walletManager, stellarKitManager, tokenAutoEnableManager)
        stellarAccountManager.start()

        systemInfoManager = SystemInfoManager(appConfigProvider)

        languageManager = LanguageManager()
        currencyManager = CurrencyManager(localStorage, appConfigProvider)
        numberFormatter = NumberFormatter(languageManager)

        connectivityManager = ConnectivityManager(backgroundManager)

        zcashBirthdayProvider = ZcashBirthdayProvider(this)
        restoreSettingsManager = RestoreSettingsManager(restoreSettingsStorage, zcashBirthdayProvider)

        evmLabelManager = EvmLabelManager(
            EvmLabelProvider(),
            appDatabase.evmAddressLabelDao(),
            appDatabase.evmMethodLabelDao(),
            appDatabase.syncerStateDao()
        )

        val adapterFactory = AdapterFactory(
            context = instance,
            btcBlockchainManager = btcBlockchainManager,
            evmBlockchainManager = evmBlockchainManager,
            evmSyncSourceManager = evmSyncSourceManager,
            solanaKitManager = solanaKitManager,
            tronKitManager = tronKitManager,
            tonKitManager = tonKitManager,
            stellarKitManager = stellarKitManager,
            backgroundManager = backgroundManager,
            restoreSettingsManager = restoreSettingsManager,
            coinManager = coinManager,
            evmLabelManager = evmLabelManager,
            localStorage = localStorage
        )
        adapterManager = AdapterManager(
            walletManager,
            adapterFactory,
            btcBlockchainManager,
            evmBlockchainManager,
            solanaKitManager,
            tronKitManager,
            tonKitManager,
            stellarKitManager,
        )
        transactionAdapterManager = TransactionAdapterManager(adapterManager, adapterFactory)

        feeCoinProvider = FeeTokenProvider(marketKit)
        resourceManager = ResourceManager(appConfigProvider)

        pinComponent = PinComponent(
            pinSettingsStorage = pinSettingsStorage,
            userManager = userManager,
            pinDbStorage = PinDbStorage(appDatabase.pinDao()),
            backgroundManager = backgroundManager
        )

        statsManager = StatsManager(appDatabase.statsDao(), localStorage, marketKit, appConfigProvider, backgroundManager)

        rateAppManager = RateAppManager(walletManager, adapterManager, localStorage)

        wcManager = WCManager(accountManager)
        wcWalletRequestHandler = WCWalletRequestHandler(evmBlockchainManager)

        termsManager = TermsManager(localStorage)

        marketWidgetManager = MarketWidgetManager()
        marketFavoritesManager = MarketFavoritesManager(appDatabase, localStorage, marketWidgetManager)

        marketWidgetRepository = MarketWidgetRepository(
            marketKit,
            marketFavoritesManager,
            MarketFavoritesMenuService(localStorage, marketWidgetManager),
            TopPlatformsRepository(marketKit),
            currencyManager
        )

        releaseNotesManager = ReleaseNotesManager(systemInfoManager, localStorage, appConfigProvider)
        donationShowManager = DonationShowManager(localStorage)

        setAppTheme()

        val nftStorage = NftStorage(appDatabase.nftDao(), marketKit)
        nftMetadataManager = NftMetadataManager(marketKit, appConfigProvider, nftStorage)
        nftAdapterManager = NftAdapterManager(walletManager, evmBlockchainManager)
        nftMetadataSyncer = NftMetadataSyncer(nftAdapterManager, nftMetadataManager, nftStorage)

        initializeWalletConnectV2(appConfig)

        wcSessionManager = WCSessionManager(accountManager, WCSessionStorage(appDatabase))

        baseTokenManager = BaseTokenManager(coinManager, localStorage)
        balanceViewTypeManager = BalanceViewTypeManager(localStorage)
        balanceHiddenManager = BalanceHiddenManager(localStorage, backgroundManager)

        contactsRepository = ContactsRepository(marketKit)
        chartIndicatorManager = ChartIndicatorManager(appDatabase.chartIndicatorSettingsDao(), localStorage)

        backupProvider = BackupProvider(
            localStorage = localStorage,
            languageManager = languageManager,
            walletStorage = enabledWalletsStorage,
            settingsManager = restoreSettingsManager,
            accountManager = accountManager,
            accountFactory = accountFactory,
            walletManager = walletManager,
            restoreSettingsManager = restoreSettingsManager,
            blockchainSettingsStorage = blockchainSettingsStorage,
            evmBlockchainManager = evmBlockchainManager,
            marketFavoritesManager = marketFavoritesManager,
            balanceViewTypeManager = balanceViewTypeManager,
            appIconService = AppIconService(localStorage),
            themeService = ThemeService(localStorage),
            chartIndicatorManager = chartIndicatorManager,
            chartIndicatorSettingsDao = appDatabase.chartIndicatorSettingsDao(),
            balanceHiddenManager = balanceHiddenManager,
            baseTokenManager = baseTokenManager,
            launchScreenService = LaunchScreenService(localStorage),
            currencyManager = currencyManager,
            btcBlockchainManager = btcBlockchainManager,
            evmSyncSourceManager = evmSyncSourceManager,
            evmSyncSourceStorage = evmSyncSourceStorage,
            solanaRpcSourceManager = solanaRpcSourceManager,
            contactsRepository = contactsRepository
        )

        tonConnectManager = TonConnectManager(
            context = this,
            adapterFactory = adapterFactory,
            appName = "unstoppable",
            appVersion = appConfigProvider.appVersion
        )
        tonConnectManager.start()

        roiManager = RoiManager(localStorage)

        startTasks()
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .crossfade(true)
            .components {
                add(SvgDecoder.Factory())
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
    }

    private fun initializeWalletConnectV2(appConfig: AppConfigProvider) {
        val projectId = appConfig.walletConnectProjectId
        val serverUrl = "wss://${appConfig.walletConnectUrl}?projectId=$projectId"
        val connectionType = ConnectionType.AUTOMATIC
        val appMetaData = Core.Model.AppMetaData(
            name = appConfig.walletConnectAppMetaDataName,
            description = "",
            url = appConfig.walletConnectAppMetaDataUrl,
            icons = listOf(appConfig.walletConnectAppMetaDataIcon),
            redirect = null,
        )

        CoreClient.initialize(
            metaData = appMetaData,
            relayServerUrl = serverUrl,
            connectionType = connectionType,
            application = this,
            onError = { error ->
                Log.w("AAA", "error", error.throwable)
            },
        )
        Web3Wallet.initialize(Wallet.Params.Init(core = CoreClient)) { error ->
            Log.e("AAA", "error", error.throwable)
        }
    }

    private fun setAppTheme() {
        val nightMode = when (localStorage.currentTheme) {
            ThemeType.Light -> AppCompatDelegate.MODE_NIGHT_NO
            ThemeType.Dark -> AppCompatDelegate.MODE_NIGHT_YES
            ThemeType.System -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }

        if (AppCompatDelegate.getDefaultNightMode() != nightMode) {
            AppCompatDelegate.setDefaultNightMode(nightMode)
        }
    }

    override val workManagerConfiguration: androidx.work.Configuration
        get() = if (BuildConfig.DEBUG) {
            WorkConfiguration.Builder()
                .setMinimumLoggingLevel(Log.DEBUG)
                .build()
        } else {
            WorkConfiguration.Builder()
                .setMinimumLoggingLevel(Log.ERROR)
                .build()
        }

    override fun localizedContext(): Context {
        return localeAwareContext(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(localeAwareContext(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localeAwareContext(this)
    }

    override val isSwapEnabled = true

    override fun getApplicationSignatures() = try {
        val signatureList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val signingInfo = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNING_CERTIFICATES
            ).signingInfo

            when {
                signingInfo?.hasMultipleSigners() == true -> signingInfo.apkContentsSigners // Send all with apkContentsSigners
                else -> signingInfo?.signingCertificateHistory // Send one with signingCertificateHistory
            }
        } else {
            @Suppress("DEPRECATION")
            packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures
        }

        signatureList?.map {
            val digest = MessageDigest.getInstance("SHA")
            digest.update(it.toByteArray())
            digest.digest()
        } ?: emptyList()
    } catch (e: Exception) {
        // Handle error
        emptyList()
    }

    private fun startTasks() {
        coroutineScope.launch {
            EthereumKit.init()
            adapterManager.startAdapterManager()
            marketKit.sync()
            rateAppManager.onAppLaunch()
            nftMetadataSyncer.start()
            pinComponent.initDefaultPinLevel()
            accountManager.clearAccounts()
            wcSessionManager.start()

            AppVersionManager(systemInfoManager, localStorage).apply { storeAppVersion() }

            if (MarketWidgetWorker.hasEnabledWidgets(instance)) {
                MarketWidgetWorker.enqueueWork(instance)
            } else {
                MarketWidgetWorker.cancel(instance)
            }

            evmLabelManager.sync()
            contactsRepository.initialize()
            trialExpired = !UserSubscriptionManager.hasFreeTrial()
        }

        coroutineScope.launch {
            backgroundManager.stateFlow.collect { state ->
                when (state) {
                    EnterForeground -> UserSubscriptionManager.onResume()
                    EnterBackground -> UserSubscriptionManager.pause()
                    AllActivitiesDestroyed -> Unit
                }
            }
        }
    }
}
