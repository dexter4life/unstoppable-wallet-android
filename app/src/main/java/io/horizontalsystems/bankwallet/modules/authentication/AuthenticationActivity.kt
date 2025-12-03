package io.horizontalsystems.bankwallet.modules.authentication

import AutoScrollingPager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.core.App
import io.horizontalsystems.bankwallet.core.BaseActivity
import io.horizontalsystems.bankwallet.core.composablePage
import io.horizontalsystems.bankwallet.modules.authentication.login.LoginModule
import io.horizontalsystems.bankwallet.modules.authentication.login.LoginScreen
import io.horizontalsystems.bankwallet.modules.authentication.register.AccountTypeView
import io.horizontalsystems.bankwallet.modules.authentication.register.CountryDetailFormScreen
import io.horizontalsystems.bankwallet.modules.authentication.register.RegisterActivityModel
import io.horizontalsystems.bankwallet.modules.authentication.register.RegisterForm
import io.horizontalsystems.bankwallet.modules.authentication.register.RegisterModule
import io.horizontalsystems.bankwallet.modules.authentication.register.RegisterView
import io.horizontalsystems.bankwallet.modules.authentication.register.SmsCodeScreen
import io.horizontalsystems.bankwallet.modules.authentication.register.VerifyEmailNotification
import io.horizontalsystems.bankwallet.modules.authentication.register.VerifyPhoneScreen
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import io.horizontalsystems.bankwallet.ui.compose.components.ButtonPrimary
import io.horizontalsystems.bankwallet.ui.compose.components.ButtonPrimaryDefaults
import io.horizontalsystems.bankwallet.ui.compose.components.RadialBackground
import io.horizontalsystems.bankwallet.ui.compose.components.SliderIndicator


// Navigation Routes
private const val LOGIN_INTRO_ROUTE = "login_intro"
private const val LOGIN_SCREEN_ROUTE = "login_screen"
private const val GETTING_STARTED_ROUTE = "getting_started"
private const val REGISTER_ROUTE = "register"
private const val ACCOUNT_TYPE_ROUTE = "account_type"
private const val COUNTRY_DETAIL_ROUTE = "country_detail"
private const val VERIFY_MAIL_NOTIFICATION_ROUTE = "verify-mail-notification"
private const val VERIFY_PHONE_ROUTE = "verify-phone-notification"
private const val CONFIRM_PHONE_ROUTE = "confirm-phone-number"


class AuthenticationActivity : BaseActivity() {

    val viewModel by viewModels<AuthActivityModel> { LoginModule.Factory() }

    private val nightMode by lazy {
        val uiMode =
            App.instance.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)
        uiMode == Configuration.UI_MODE_NIGHT_YES
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AuthIntro(viewModel, nightMode) { finish() }
        }
    }

    companion object {
        // Navigation Routes
        private const val LOGIN_INTRO_ROUTE = "login_intro"
        private const val LOGIN_SCREEN_ROUTE = "login_screen"
        private const val GETTING_STARTED_ROUTE = "getting_started"
        private const val REGISTER_ROUTE = "register"
        private const val ACCOUNT_TYPE_ROUTE = "account_type"
        private const val COUNTRY_DETAIL_ROUTE = "country_detail"
        private const val VERIFY_MAIL_NOTIFICATION_ROUTE = "verify-mail-notification"

        fun start(context: Context) {
            val intent = Intent(context, AuthenticationActivity::class.java)
            context.startActivity(intent)
        }
    }
}

@Composable
private fun AuthIntro(
    viewModel: AuthActivityModel,
    nightMode: Boolean = isSystemInDarkTheme(),
    closeActivity: () -> Unit
) {
    val currentPage = remember { mutableIntStateOf(0) }
    val pageCount = viewModel.slides.size
    val pagerState = rememberPagerState(initialPage = 0) { pageCount }
    val navController = rememberNavController() // Create and remember the NavController


    ComposeAppTheme {
        NavHost(navController = navController, startDestination = LOGIN_INTRO_ROUTE) {
            composable(LOGIN_INTRO_ROUTE) {
                Box(Modifier.fillMaxSize()) {
                    RadialBackground()
                    AutoScrollingPager(
                        viewModel = viewModel, pagerState = pagerState, nightMode = nightMode
                    ) { page ->
                        currentPage.intValue = page
                    }
                    StaticContent(navController, viewModel, pagerState, closeActivity, pageCount)
                }
            }

            composablePage(LOGIN_SCREEN_ROUTE) {
                LoginScreen(viewModel, navController = navController, nightMode = nightMode) {
                    closeActivity()
                }
            }

            navigation(startDestination = REGISTER_ROUTE, route = GETTING_STARTED_ROUTE) {
                composablePage(REGISTER_ROUTE) {
                    RegisterView({
                        navController.popBackStack()
                    }) {
                        RegisterForm(onContinue = {
                            navController.navigate(VERIFY_MAIL_NOTIFICATION_ROUTE)
                        })
                    }
                }

                composablePage(ACCOUNT_TYPE_ROUTE) {
                    RegisterView({
                        navController.popBackStack()
                    }) {
                        AccountTypeView(
                            nightMode = nightMode,
                            onAccountTypeChange = { accountType ->

                            }
                        )
                    }
                }
                composablePage(COUNTRY_DETAIL_ROUTE) { backStackEntry ->
                    val vm: RegisterActivityModel =
                        viewModel(backStackEntry, factory = RegisterModule.Factory())
                    RegisterView(
                        onBackClick = { navController.popBackStack() },
                        nightMode = nightMode
                    ) {
                        CountryDetailFormScreen(
                            viewModel = vm,
                            onNextClicked = {
                                navController.navigate(VERIFY_MAIL_NOTIFICATION_ROUTE)
                            }
                        )
                    }
                }

                composablePage(VERIFY_MAIL_NOTIFICATION_ROUTE) {
                    VerifyEmailNotification(onConfirmSuccess = {
                        navController.navigate(VERIFY_PHONE_ROUTE)
                    }, onBackClick = {
                        navController.popBackStack()
                    })
                }


                composablePage(VERIFY_PHONE_ROUTE) {
                    VerifyPhoneScreen(onBack = {
                        navController.popBackStack()
                    }, onCodeSend = { phone ->
                        navController.navigate(CONFIRM_PHONE_ROUTE)
                    })
                }

                composablePage(CONFIRM_PHONE_ROUTE) {
                    SmsCodeScreen(onClose = {
                        navController.popBackStack(REGISTER_ROUTE, false)
                    }, onDone = {
                        navController.navigate(ACCOUNT_TYPE_ROUTE)
                    }, onResendClick = {}, maskedPhone = "********5466")
                }
            }
        }


    }
}


@Composable
private fun StaticContent(
    navController: NavHostController,
    viewModel: AuthActivityModel,
    pagerState: PagerState,
    closeActivity: () -> Unit,
    pageCount: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(2f))
        Spacer(Modifier.height(326.dp))
        Spacer(Modifier.weight(1f))
        SliderIndicator(
            total = pageCount, current = pagerState.currentPage
        )
        Spacer(Modifier.weight(0.4f))
        //Text
        Column(
            modifier = Modifier
                .height(140.dp)
                .fillMaxWidth(),
        ) {
            val title = viewModel.slides[pagerState.currentPage].title
            Crossfade(targetState = title) { titleRes ->
                Text(
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    text = stringResource(titleRes),
                    textAlign = TextAlign.Center,
                    lineHeight = 32.sp,
                    color = colorResource(R.color.leah)
                )
            }
            Spacer(Modifier.height(16.dp))
            val subtitle = viewModel.slides[pagerState.currentPage].subtitle
            Crossfade(targetState = subtitle) { subtitleRes ->
                Text(
                    text = stringResource(subtitleRes),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp),
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Clip,
                    maxLines = Int.MAX_VALUE,
                    style = ComposeAppTheme.typography.body,
                    color = ComposeAppTheme.colors.grey,
                    fontSize = 17.sp,
                )
            }
        }
        Spacer(Modifier.weight(1f))
        ButtonPrimary(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(), onClick = {
                navController.navigate(GETTING_STARTED_ROUTE)
            }, buttonColors = ButtonPrimaryDefaults.textButtonColors(
                backgroundColor = ComposeAppTheme.colors.primary,
                contentColor = ComposeAppTheme.colors.dark,
                disabledBackgroundColor = ComposeAppTheme.colors.blade,
                disabledContentColor = ComposeAppTheme.colors.andy,
            ), content = {
                Text(
                    stringResource(R.string.Button_GettingStarted),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }, enabled = true
        )
        Spacer(Modifier.height(10.dp))
        ButtonPrimary(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(), onClick = {
                viewModel.onLoginClicked()
                navController.navigate("login_screen")
            }, border = BorderStroke(
                width = 1.dp, color = ComposeAppTheme.colors.primary
            ), buttonColors = ButtonPrimaryDefaults.textButtonColors(
                backgroundColor = ComposeAppTheme.colors.transparent,
                contentColor = ComposeAppTheme.colors.primary,
                disabledBackgroundColor = ComposeAppTheme.colors.blade,
                disabledContentColor = ComposeAppTheme.colors.andy,
            ), content = {
                Text(
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = stringResource(R.string.Button_Login),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }, enabled = true
        )
        Spacer(Modifier.height(60.dp))
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview_AuthenticationActivity_Dark() {
    ComposeAppTheme(darkTheme = false) {
        AuthIntro(AuthActivityModel(null), true) {}
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun Preview_AuthenticationActivity_Light() {
    ComposeAppTheme(darkTheme = false) {
        AuthIntro(AuthActivityModel(null), false) {}
    }
}
