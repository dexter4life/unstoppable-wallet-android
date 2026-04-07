package io.fastpayd.wallet.modules.balance.token

import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import io.fastpayd.wallet.R
import io.fastpayd.wallet.core.isCustom
import io.fastpayd.wallet.core.providers.Translator
import io.fastpayd.wallet.core.slideFromBottom
import io.fastpayd.wallet.core.slideFromRight
import io.fastpayd.wallet.core.stats.StatEvent
import io.fastpayd.wallet.core.stats.StatPage
import io.fastpayd.wallet.core.stats.stat
import io.fastpayd.wallet.modules.balance.BackupRequiredError
import io.fastpayd.wallet.modules.balance.BalanceViewItem
import io.fastpayd.wallet.modules.balance.BalanceViewModel
import io.fastpayd.wallet.modules.balance.DeemedValue
import io.fastpayd.wallet.modules.balance.ZcashLockedValue
import io.fastpayd.wallet.modules.coin.CoinFragment
import io.fastpayd.wallet.modules.manageaccount.dialogs.BackupRequiredDialog
import io.fastpayd.wallet.modules.receive.ReceiveFragment
import io.fastpayd.wallet.modules.send.SendFragment
import io.fastpayd.wallet.modules.syncerror.SyncErrorDialog
import io.fastpayd.wallet.modules.transactions.TransactionViewItem
import io.fastpayd.wallet.modules.transactions.TransactionsViewModel
import io.fastpayd.wallet.modules.transactions.transactionList
import io.fastpayd.wallet.ui.compose.ComposeAppTheme
import io.fastpayd.wallet.ui.compose.components.AppBar
import io.fastpayd.wallet.ui.compose.components.ButtonPrimaryCircle
import io.fastpayd.wallet.ui.compose.components.ButtonPrimaryDefault
import io.fastpayd.wallet.ui.compose.components.ButtonPrimaryYellow
import io.fastpayd.wallet.ui.compose.components.CoinImage
import io.fastpayd.wallet.ui.compose.components.HSpacer
import io.fastpayd.wallet.ui.compose.components.HsBackButton
import io.fastpayd.wallet.ui.compose.components.HsIconButton
import io.fastpayd.wallet.ui.compose.components.ListEmptyView
import io.fastpayd.wallet.ui.compose.components.RowUniversal
import io.fastpayd.wallet.ui.compose.components.TextImportantWarning
import io.fastpayd.wallet.ui.compose.components.VSpacer
import io.fastpayd.wallet.ui.compose.components.body_bran
import io.fastpayd.wallet.ui.compose.components.body_grey
import io.fastpayd.wallet.ui.compose.components.subhead2_grey
import io.fastpayd.wallet.ui.extensions.BottomSheetHeader
import io.fastpayd.wallet.ui.extensions.RotatingCircleProgressView
import io.horizontalsystems.core.helpers.HudHelper
import kotlinx.coroutines.launch


@Composable
fun TokenBalanceScreen(
    viewModel: TokenBalanceViewModel,
    transactionsViewModel: TransactionsViewModel,
    navController: NavController
) {
    val uiState = viewModel.uiState

    Scaffold(
        backgroundColor = ComposeAppTheme.colors.tyler,
        topBar = {
            AppBar(
                title = uiState.title,
                navigationIcon = {
                    HsBackButton(onClick = { navController.popBackStack() })
                }
            )
        }
    ) { paddingValues ->
        val transactionItems = uiState.transactions
        if (transactionItems.isNullOrEmpty()) {
            Column(Modifier.padding(paddingValues)) {
                uiState.balanceViewItem?.let {
                    TokenBalanceHeader(balanceViewItem = it, navController = navController, viewModel = viewModel)
                }
                if (transactionItems == null) {
                    ListEmptyView(
                        text = stringResource(R.string.Transactions_WaitForSync),
                        icon = R.drawable.ic_clock
                    )
                } else {
                    ListEmptyView(
                        text = stringResource(R.string.Transactions_EmptyList),
                        icon = R.drawable.ic_outgoingraw
                    )
                }
            }
        } else {
            val listState = rememberLazyListState()
            LazyColumn(Modifier.padding(paddingValues), state = listState) {
                item {
                    uiState.balanceViewItem?.let {
                        TokenBalanceHeader(balanceViewItem = it, navController = navController, viewModel = viewModel)
                    }
                }

                transactionList(
                    transactionsMap = transactionItems,
                    willShow = { viewModel.willShow(it) },
                    onClick = { onTransactionClick(it, viewModel, transactionsViewModel, navController) },
                    onBottomReached = { viewModel.onBottomReached() }
                )
            }
        }
    }

}


private fun onTransactionClick(
    transactionViewItem: TransactionViewItem,
    tokenBalanceViewModel: TokenBalanceViewModel,
    transactionsViewModel: TransactionsViewModel,
    navController: NavController
) {
    val transactionItem = tokenBalanceViewModel.getTransactionItem(transactionViewItem) ?: return
    transactionsViewModel.tmpTransactionRecordToShow = transactionItem.record

    navController.slideFromBottom(R.id.transactionInfoFragment)

    stat(page = StatPage.TokenPage, event = StatEvent.Open(StatPage.TransactionInfo))
}

@Composable
private fun TokenBalanceHeader(
    balanceViewItem: BalanceViewItem,
    navController: NavController,
    viewModel: TokenBalanceViewModel,
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        VSpacer(height = (24.dp))
        WalletIcon(
            viewItem = balanceViewItem,
            viewModel = viewModel,
            navController = navController,
        )
        VSpacer(height = 12.dp)
        Text(
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        viewModel.toggleBalanceVisibility()
                        HudHelper.vibrate(context)

                        stat(page = StatPage.TokenPage, event = StatEvent.ToggleBalanceHidden)
                    }
                ),
            text = if (balanceViewItem.primaryValue.visible) balanceViewItem.primaryValue.value else "*****",
            color = if (balanceViewItem.primaryValue.dimmed) ComposeAppTheme.colors.grey else ComposeAppTheme.colors.leah,
            style = ComposeAppTheme.typography.title2R,
            textAlign = TextAlign.Center,
        )
        VSpacer(height = 6.dp)
        if (balanceViewItem.syncingTextValue != null) {
            body_grey(
                text = balanceViewItem.syncingTextValue + (balanceViewItem.syncedUntilTextValue?.let { " - $it" } ?: ""),
                maxLines = 1,
            )
        } else {
            Text(
                text = if (balanceViewItem.secondaryValue.visible) balanceViewItem.secondaryValue.value else "*****",
                color = if (balanceViewItem.secondaryValue.dimmed) ComposeAppTheme.colors.andy else ComposeAppTheme.colors.grey,
                style = ComposeAppTheme.typography.body,
                maxLines = 1,
            )
        }
        VSpacer(height = 24.dp)
        ButtonsRow(viewItem = balanceViewItem, navController = navController, viewModel = viewModel)
        LockedBalanceSection(balanceViewItem, navController)
        balanceViewItem.warning?.let {
            VSpacer(height = 8.dp)
            TextImportantWarning(
                icon = R.drawable.ic_attention_20,
                title = it.title.getString(),
                text = it.text.getString()
            )
        }
        VSpacer(height = 16.dp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LockedBalanceSection(balanceViewItem: BalanceViewItem, navController: NavController) {
    val infoModalBottomSheetState = androidx.compose.material3.rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var bottomSheetContent by remember { mutableStateOf<BottomSheetContent?>(null) }
    val coroutineScope = rememberCoroutineScope()

    if (balanceViewItem.lockedValues.isNotEmpty()) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .border(0.5.dp, ComposeAppTheme.colors.blade, RoundedCornerShape(12.dp))
        ) {
            balanceViewItem.lockedValues.forEach { lockedValue ->
                val infoTitle = lockedValue.infoTitle.getString()
                val infoText = lockedValue.info.getString()
                val actionButtonTitle: String?
                val onClickActionButton: (() -> Unit)?

                if (lockedValue is ZcashLockedValue) {
                    actionButtonTitle = null
                    onClickActionButton = null
                } else {
                    actionButtonTitle = null
                    onClickActionButton = null
                }

                LockedBalanceCell(
                    title = lockedValue.title.getString(),
                    lockedAmount = lockedValue.coinValue
                ) {
                    bottomSheetContent = BottomSheetContent(
                        icon = R.drawable.ic_info_24,
                        title = infoTitle,
                        description = infoText,
                        actionButtonTitle = actionButtonTitle,
                        onClickActionButton = onClickActionButton
                    )
                    coroutineScope.launch {
                        infoModalBottomSheetState.show()
                    }
                }
            }
        }
        bottomSheetContent?.let { info ->
            InfoBottomSheet(
                content = info,
                bottomSheetState = infoModalBottomSheetState,
                hideBottomSheet = {
                    coroutineScope.launch {
                        infoModalBottomSheetState.hide()
                    }
                    bottomSheetContent = null
                }
            )
        }
    }
}

data class BottomSheetContent(
    val icon: Int,
    val title: String,
    val description: String,
    val actionButtonTitle: String? = null,
    val onClickActionButton: (() -> Unit)? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoBottomSheet(
    content: BottomSheetContent,
    hideBottomSheet: () -> Unit,
    bottomSheetState: SheetState
) {
    ModalBottomSheet(
        onDismissRequest = hideBottomSheet,
        sheetState = bottomSheetState,
        containerColor = ComposeAppTheme.colors.transparent
    ) {
        BottomSheetHeader(
            iconPainter = painterResource(content.icon),
            title = content.title,
            titleColor = ComposeAppTheme.colors.leah,
            iconTint = ColorFilter.tint(ComposeAppTheme.colors.grey),
            onCloseClick = hideBottomSheet
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 12.dp, horizontal = 24.dp)
                    .fillMaxWidth()
            ) {
                body_bran(
                    text = content.description,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                )
                VSpacer(56.dp)
                content.actionButtonTitle?.let {
                    ButtonPrimaryYellow(
                        modifier = Modifier.fillMaxWidth(),
                        title = content.actionButtonTitle,
                        onClick = content.onClickActionButton ?: {}
                    )
                    VSpacer(32.dp)
                }

            }
        }
    }
}

@Composable
private fun LockedBalanceCell(
    title: String,
    lockedAmount: DeemedValue<String>,
    onClickInfo: () -> Unit
) {

    RowUniversal(
        modifier = Modifier
            .padding(horizontal = 16.dp),
    ) {
        subhead2_grey(
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        HSpacer(8.dp)
        HsIconButton(
            modifier = Modifier.size(20.dp),
            onClick = onClickInfo
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_info_20),
                contentDescription = "info button",
                tint = ComposeAppTheme.colors.grey
            )
        }
        Spacer(Modifier.weight(1f))
        Text(
            modifier = Modifier.padding(start = 6.dp),
            text = if (lockedAmount.visible) lockedAmount.value else "*****",
            color = if (lockedAmount.dimmed) ComposeAppTheme.colors.andy else ComposeAppTheme.colors.leah,
            style = ComposeAppTheme.typography.subheadR,
            maxLines = 1,
        )
    }
}

@Composable
private fun WalletIcon(
    viewItem: BalanceViewItem,
    viewModel: TokenBalanceViewModel,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .height(52.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        viewItem.syncingProgress.progress?.let { progress ->
            AndroidView(
                modifier = Modifier
                    .size(52.dp),
                factory = { context ->
                    RotatingCircleProgressView(context)
                },
                update = { view ->
                    val color = when (viewItem.syncingProgress.dimmed) {
                        true -> R.color.grey_50
                        false -> R.color.grey
                    }
                    view.setProgressColored(progress, view.context.getColor(color))
                }
            )
        }
        if (viewItem.failedIconVisible) {
            val view = LocalView.current
            Image(
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        onSyncErrorClicked(viewItem, viewModel, navController, view)
                    },
                painter = painterResource(id = R.drawable.ic_attention_24),
                contentDescription = "coin icon",
                colorFilter = ColorFilter.tint(ComposeAppTheme.colors.lucian)
            )
        } else {
            CoinImage(
                token = viewItem.wallet.token,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

private fun onSyncErrorClicked(viewItem: BalanceViewItem, viewModel: TokenBalanceViewModel, navController: NavController, view: View) {
    when (val syncErrorDetails = viewModel.getSyncErrorDetails(viewItem)) {
        is BalanceViewModel.SyncError.Dialog -> {
            val wallet = syncErrorDetails.wallet
            val errorMessage = syncErrorDetails.errorMessage

            navController.slideFromBottom(
                R.id.syncErrorDialog,
                SyncErrorDialog.Input(wallet, errorMessage)
            )
        }

        is BalanceViewModel.SyncError.NetworkNotAvailable -> {
            HudHelper.showErrorMessage(view, R.string.Hud_Text_NoInternet)
        }
    }
}


@Composable
private fun ButtonsRow(viewItem: BalanceViewItem, navController: NavController, viewModel: TokenBalanceViewModel) {
    val onClickReceive = {
        try {
            val wallet = viewModel.getWalletForReceive()
            navController.slideFromRight(R.id.receiveFragment, ReceiveFragment.Input(wallet))

            stat(page = StatPage.TokenPage, event = StatEvent.OpenReceive(wallet.token))
        } catch (e: BackupRequiredError) {
            val text = Translator.getString(
                R.string.ManageAccount_BackupRequired_Description,
                e.account.name,
                e.coinTitle
            )
            navController.slideFromBottom(
                R.id.backupRequiredDialog,
                BackupRequiredDialog.Input(e.account, text)
            )

            stat(page = StatPage.TokenPage, event = StatEvent.Open(StatPage.BackupRequired))
        }
    }

    Row(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 16.dp)
    ) {
        if (viewItem.isWatchAccount) {
            ButtonPrimaryDefault(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.Balance_Address),
                onClick = onClickReceive,
            )
        } else {
            ButtonPrimaryYellow(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.Balance_Send),
                onClick = {
                    val sendTitle = Translator.getString(R.string.Send_Title, viewItem.wallet.token.fullCoin.coin.code)
                    navController.slideFromRight(
                        R.id.sendXFragment,
                        SendFragment.Input(
                            wallet = viewItem.wallet,
                            title = sendTitle,
                            sendEntryPointDestId = R.id.tokenBalanceFragment,
                            address = io.fastpayd.wallet.entities.Address(""),
                            amount = null,
                            hideAddress = false
                        )
                    )

                    stat(
                        page = StatPage.TokenPage,
                        event = StatEvent.OpenSend(viewItem.wallet.token)
                    )
                },
                enabled = viewItem.sendEnabled
            )
            HSpacer(8.dp)
            if (!viewItem.swapVisible) {
                ButtonPrimaryDefault(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.Balance_Receive),
                    onClick = onClickReceive,
                )
            } else {
                ButtonPrimaryCircle(
                    icon = R.drawable.ic_arrow_down_left_24,
                    contentDescription = stringResource(R.string.Balance_Receive),
                    onClick = onClickReceive,
                )
            }
            if (viewItem.swapVisible) {
                HSpacer(8.dp)
            }
        }
        HSpacer(8.dp)
        ButtonPrimaryCircle(
            icon = R.drawable.ic_chart_24,
            contentDescription = stringResource(R.string.Coin_Info),
            enabled = !viewItem.wallet.token.isCustom,
            onClick = {
                val coinUid = viewItem.wallet.coin.uid
                val arguments = CoinFragment.Input(coinUid)

                navController.slideFromRight(R.id.coinFragment, arguments)

                stat(page = StatPage.TokenPage, event = StatEvent.OpenCoin(coinUid))
            },
        )
    }
}
