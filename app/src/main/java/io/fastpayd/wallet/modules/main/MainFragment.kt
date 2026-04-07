package io.fastpayd.wallet.modules.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BadgedBox
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import io.fastpayd.wallet.R
import io.fastpayd.wallet.core.BaseComposeFragment
import io.fastpayd.wallet.core.managers.RateAppManager
import io.fastpayd.wallet.core.slideFromBottom
import io.fastpayd.wallet.core.slideFromRight
import io.fastpayd.wallet.core.stats.StatEntity
import io.fastpayd.wallet.core.stats.StatEvent
import io.fastpayd.wallet.core.stats.StatPage
import io.fastpayd.wallet.core.stats.stat
import io.fastpayd.wallet.core.stats.statTab
import io.fastpayd.wallet.modules.balance.ui.BalanceScreen
import io.fastpayd.wallet.modules.main.MainModule.MainNavigation
import io.fastpayd.wallet.modules.manageaccount.dialogs.BackupRequiredDialog
import io.fastpayd.wallet.modules.market.HomeScreen
import io.fastpayd.wallet.modules.rateapp.RateApp
import io.fastpayd.wallet.modules.releasenotes.ReleaseNotesFragment
import io.fastpayd.wallet.modules.rooteddevice.RootedDeviceModule
import io.fastpayd.wallet.modules.rooteddevice.RootedDeviceScreen
import io.fastpayd.wallet.modules.rooteddevice.RootedDeviceViewModel
import io.fastpayd.wallet.modules.sendtokenselect.SendTokenSelectFragment
import io.fastpayd.wallet.modules.settings.main.SettingsScreen
import io.fastpayd.wallet.modules.tor.TorStatusView
import io.fastpayd.wallet.modules.transactions.TransactionsModule
import io.fastpayd.wallet.modules.transactions.TransactionsScreen
import io.fastpayd.wallet.modules.transactions.TransactionsViewModel
import io.fastpayd.wallet.modules.walletconnect.WCAccountTypeNotSupportedDialog
import io.fastpayd.wallet.modules.walletconnect.WCManager.SupportState
import io.fastpayd.wallet.ui.compose.ComposeAppTheme
import io.fastpayd.wallet.ui.compose.components.BadgeText
import io.fastpayd.wallet.ui.compose.components.HsBottomNavigation
import io.fastpayd.wallet.ui.compose.components.HsBottomNavigationItem
import io.fastpayd.wallet.ui.extensions.WalletSwitchBottomSheet
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainFragment : BaseComposeFragment() {
    private val mainActivityViewModel by activityViewModels<MainActivityViewModel>()

    @Composable
    override fun GetContent(navController: NavController) {
        val backStackEntry = navController.safeGetBackStackEntry(R.id.mainFragment)

        backStackEntry?.let {
            val viewModel = ViewModelProvider(backStackEntry.viewModelStore,  TransactionsModule.Factory())
                .get(TransactionsViewModel::class.java)
            MainScreenWithRootedDeviceCheck(
                transactionsViewModel = viewModel,
                navController = navController,
                mainActivityViewModel = mainActivityViewModel
            )
        } ?: run {
            // Back stack entry doesn't exist, restart activity
            val intent = Intent(context, MainActivity::class.java)
            requireActivity().startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().moveTaskToBack(true)
                }
            })
    }

}

@Composable
private fun MainScreenWithRootedDeviceCheck(
    transactionsViewModel: TransactionsViewModel,
    navController: NavController,
    rootedDeviceViewModel: RootedDeviceViewModel = viewModel(factory = RootedDeviceModule.Factory()),
    mainActivityViewModel: MainActivityViewModel
) {
    if (rootedDeviceViewModel.showRootedDeviceWarning) {
        RootedDeviceScreen { rootedDeviceViewModel.ignoreRootedDeviceWarning() }
    } else {
        MainScreen(mainActivityViewModel, transactionsViewModel, navController)
    }
}

@Composable
private fun MainScreen(
    mainActivityViewModel: MainActivityViewModel,
    transactionsViewModel: TransactionsViewModel,
    fragmentNavController: NavController,
    viewModel: MainViewModel = viewModel(factory = MainModule.Factory())
) {
    val activityIntent by mainActivityViewModel.intentLiveData.observeAsState()
    LaunchedEffect(activityIntent) {
        activityIntent?.data?.let {
            mainActivityViewModel.intentHandled()
            viewModel.handleDeepLink(it)
        }
    }

    val uiState = viewModel.uiState
    val selectedPage = uiState.selectedTabIndex
    val pagerState = rememberPagerState(initialPage = selectedPage) { uiState.mainNavItems.size }

    val coroutineScope = rememberCoroutineScope()
    val modalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetBackgroundColor = ComposeAppTheme.colors.transparent,
        sheetContent = {
            WalletSwitchBottomSheet(
                wallets = viewModel.wallets,
                watchingAddresses = viewModel.watchWallets,
                selectedAccount = uiState.activeWallet,
                onSelectListener = {
                    coroutineScope.launch {
                        modalBottomSheetState.hide()
                        viewModel.onSelect(it)

                        stat(
                            page = StatPage.SwitchWallet,
                            event = StatEvent.Select(StatEntity.Wallet)
                        )
                    }
                },
                onCancelClick = {
                    coroutineScope.launch {
                        modalBottomSheetState.hide()
                    }
                }
            )
        },
    ) {
        Scaffold(
            backgroundColor = ComposeAppTheme.colors.tyler,
            bottomBar = {
                Column {
                    if (uiState.torEnabled) {
                        TorStatusView()
                    }
                    HsBottomNavigation(
                        backgroundColor = ComposeAppTheme.colors.blade,
                        elevation = 0.dp
                    ) {
                        uiState.mainNavItems.forEach { item ->
                            HsBottomNavigationItem(
                                icon = {
                                    BadgedIcon(item.badge) {
                                        Icon(
                                            painter = painterResource(item.mainNavItem.iconRes),
                                            contentDescription = stringResource(item.mainNavItem.titleRes)
                                        )
                                    }
                                },
                                selected = item.selected,
                                enabled = item.enabled,
                                selectedContentColor = ComposeAppTheme.colors.jacob,
                                unselectedContentColor = if (item.enabled) ComposeAppTheme.colors.grey else ComposeAppTheme.colors.andy,
                                onClick = {
                                    viewModel.onSelect(item.mainNavItem)

                                    stat(
                                        page = StatPage.Main,
                                        event = StatEvent.SwitchTab(item.mainNavItem.statTab)
                                    )
                                },
                                onLongClick = {
//                                    if (item.mainNavItem == MainNavigation.Balance) {
//                                        coroutineScope.launch {
//                                            modalBottomSheetState.show()
//
//                                            stat(
//                                                page = StatPage.Main,
//                                                event = StatEvent.Open(StatPage.SwitchWallet)
//                                            )
//                                        }
//                                    }
                                }
                            )
                        }
                    }
                }
            }
        ) {
            BackHandler(enabled = modalBottomSheetState.isVisible) {
                coroutineScope.launch {
                    modalBottomSheetState.hide()
                }
            }
            Column(modifier = Modifier.padding(it)) {
                LaunchedEffect(key1 = selectedPage, block = {
                    pagerState.scrollToPage(selectedPage)
                })

                HorizontalPager(
                    modifier = Modifier.weight(1f),
                    state = pagerState,
                    userScrollEnabled = false,
                    verticalAlignment = Alignment.Top
                ) { page ->
                    when (uiState.mainNavItems[page].mainNavItem) {
                        MainNavigation.Home -> HomeScreen(fragmentNavController)
                        MainNavigation.Balance -> BalanceScreen(fragmentNavController)
                        MainNavigation.Transactions -> TransactionsScreen(
                            fragmentNavController,
                            transactionsViewModel
                        )

                        MainNavigation.Settings -> SettingsScreen(fragmentNavController)
                    }
                }
            }
        }
    }

    if (uiState.showWhatsNew) {
        LaunchedEffect(Unit) {
            fragmentNavController.slideFromBottom(
                R.id.releaseNotesFragment,
                ReleaseNotesFragment.Input(true)
            )
            viewModel.whatsNewShown()
        }
    }

    if (uiState.showDonationPage) {
        LaunchedEffect(Unit) {
            fragmentNavController.slideFromBottom(R.id.whyDonateFragment)
            viewModel.donationShown()
        }
    }

    if (uiState.showRateAppDialog) {
        val context = LocalContext.current
        RateApp(
            onRateClick = {
                RateAppManager.openPlayMarket(context)
                viewModel.closeRateDialog()
            },
            onCancelClick = { viewModel.closeRateDialog() }
        )
    }

    if (uiState.wcSupportState != null) {
        when (val wcSupportState = uiState.wcSupportState) {
            SupportState.NotSupportedDueToNoActiveAccount -> {
                fragmentNavController.slideFromBottom(R.id.wcErrorNoAccountFragment)
            }

            is SupportState.NotSupportedDueToNonBackedUpAccount -> {
                val text = stringResource(R.string.WalletConnect_Error_NeedBackup)
                fragmentNavController.slideFromBottom(
                    R.id.backupRequiredDialog,
                    BackupRequiredDialog.Input(wcSupportState.account, text)
                )

                stat(page = StatPage.Main, event = StatEvent.Open(StatPage.BackupRequired))
            }

            is SupportState.NotSupported -> {
                fragmentNavController.slideFromBottom(
                    R.id.wcAccountTypeNotSupportedDialog,
                    WCAccountTypeNotSupportedDialog.Input(wcSupportState.accountTypeDescription)
                )
            }

            else -> {}
        }
        viewModel.wcSupportStateHandled()
    }

    uiState.deeplinkPage?.let { deepLinkPage ->
        LaunchedEffect(Unit) {
            delay(500)
            fragmentNavController.slideFromRight(
                deepLinkPage.navigationId,
                deepLinkPage.input
            )
            viewModel.deeplinkPageHandled()
        }
    }

    uiState.openSend?.let { openSend ->
        fragmentNavController.slideFromRight(
            R.id.sendTokenSelectFragment,
            SendTokenSelectFragment.Input(
                openSend.blockchainTypes,
                openSend.tokenTypes,
                openSend.address,
                openSend.amount
            )
        )
        viewModel.onSendOpened()
    }

    LifecycleEventEffect(event = Lifecycle.Event.ON_RESUME) {
        viewModel.onResume()
    }
}

@Composable
private fun BadgedIcon(
    badge: MainModule.BadgeType?,
    icon: @Composable BoxScope.() -> Unit,
) {
    when (badge) {
        is MainModule.BadgeType.BadgeNumber ->
            BadgedBox(
                badge = {
                    BadgeText(
                        text = badge.number.toString(),
                    )
                },
                content = icon
            )

        MainModule.BadgeType.BadgeDot ->
            BadgedBox(
                badge = {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                ComposeAppTheme.colors.lucian,
                                shape = RoundedCornerShape(4.dp)
                            )
                    ) { }
                },
                content = icon
            )

        else -> {
            Box {
                icon()
            }
        }
    }
}

fun NavController.safeGetBackStackEntry(destinationId: Int): NavBackStackEntry? {
    return try {
        this.getBackStackEntry(destinationId)
    } catch (e: IllegalArgumentException) {
        null
    }
}