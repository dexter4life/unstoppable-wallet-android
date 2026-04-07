package io.fastpayd.wallet.modules.usersubscription

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.fastpayd.wallet.core.BaseComposeFragment
import io.fastpayd.wallet.core.composablePopup
import io.fastpayd.wallet.core.setNavigationResultX
import io.fastpayd.wallet.modules.usersubscription.ui.PremiumSubscribedScreen
import io.fastpayd.wallet.modules.usersubscription.ui.SelectSubscriptionScreen
import io.horizontalsystems.subscriptions.core.IPaidAction
import kotlinx.parcelize.Parcelize

class BuySubscriptionFragment : BaseComposeFragment() {
    @Composable
    override fun GetContent(navController: NavController) {
        SubscriptionNavHost(
            navController = navController,
            onClose = { navController.popBackStack() })
    }

    @Parcelize
    data class Input(val action: IPaidAction) : Parcelable

    @Parcelize
    class Result : Parcelable
}

@Composable
fun SubscriptionNavHost(
    navController: NavController,
    onClose: () -> Unit
) {
    val navHostController = rememberNavController()
    NavHost(
        navController = navHostController,
        startDestination = "select_subscription",
    ) {
        composable("select_subscription") {
            SelectSubscriptionScreen(
                navHostController,
                onCloseClick = onClose
            )
        }
        composablePopup("premium_subscribed_page") {
            PremiumSubscribedScreen(
                onCloseClick = {
                    navController.setNavigationResultX(BuySubscriptionFragment.Result())
                    onClose()
                }
            )
        }
    }
}
