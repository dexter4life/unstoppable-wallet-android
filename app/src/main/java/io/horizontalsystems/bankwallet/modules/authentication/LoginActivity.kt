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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.core.App
import io.horizontalsystems.bankwallet.core.BaseActivity
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import io.horizontalsystems.bankwallet.ui.compose.components.RadialBackground
import io.horizontalsystems.bankwallet.ui.compose.components.SliderIndicator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.horizontalsystems.bankwallet.core.composablePage
import io.horizontalsystems.bankwallet.modules.authentication.login.LoginActivityModel
import io.horizontalsystems.bankwallet.modules.authentication.login.LoginForm
import io.horizontalsystems.bankwallet.modules.authentication.login.LoginModule
import io.horizontalsystems.bankwallet.modules.authentication.login.LoginWithActionGroup
import io.horizontalsystems.bankwallet.modules.authentication.register.RegisterActivity
import io.horizontalsystems.bankwallet.modules.receive.sharedViewModel
import io.horizontalsystems.bankwallet.modules.receive.viewmodels.ReceiveSharedViewModel
import io.horizontalsystems.bankwallet.ui.compose.components.ButtonPrimary
import io.horizontalsystems.bankwallet.ui.compose.components.ButtonPrimaryDefaults
import kotlin.Boolean
import kotlin.Int


class LoginActivity : BaseActivity() {

    val viewModel by viewModels<LoginActivityModel> { LoginModule.Factory() }

    private val nightMode by lazy {
        val uiMode =
            App.instance.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)
        uiMode == Configuration.UI_MODE_NIGHT_YES
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LoginIntroScreen(viewModel, nightMode) { finish() }
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

}

@Composable
private fun LoginIntroScreen(viewModel: LoginActivityModel,
                             nightMode: Boolean, closeActivity: () -> Unit) {
    val currentPage = remember { mutableIntStateOf(0) }
    val pageCount = viewModel.slides.size
    val pagerState = rememberPagerState(initialPage = 0) { pageCount }
    val navController = rememberNavController()

    ComposeAppTheme {
        val navController = rememberNavController() // Create and remember the NavController
        NavHost(navController = navController, startDestination = "login_intro") {
            composable("login_intro") {
                Box(Modifier.fillMaxSize()) {
                    RadialBackground()
                    AutoScrollingPager(viewModel = viewModel,pagerState = pagerState,
                        nightMode = nightMode) {
                            page -> currentPage.intValue = page
                    }
                    StaticContent(navController, viewModel, pagerState, closeActivity, pageCount)
                }
            }
            composablePage("login_screen") {
                LoginScreen(viewModel, navController = navController, closeActivity = {})
            }

            composablePage("getting_started") {
                ComposeAppTheme(nightMode) {
                    RegisterActivity(navController = navController)
                }
            }
        }
    }
}


@Composable
private fun StaticContent(
    navController: NavHostController,
    viewModel: LoginActivityModel,
    pagerState: PagerState,
    closeActivity: () -> Unit,
    pageCount: Int
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(2f))
        Spacer(Modifier.height(326.dp))
        Spacer(Modifier.weight(1f))
        SliderIndicator(
            total = pageCount,
            current = pagerState.currentPage
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
                .fillMaxWidth(),
            onClick = {
                navController.navigate("getting_started")
            },
            buttonColors = ButtonPrimaryDefaults.textButtonColors(
                backgroundColor = ComposeAppTheme.colors.yellowD,
                contentColor = ComposeAppTheme.colors.white,
                disabledBackgroundColor = ComposeAppTheme.colors.blade,
                disabledContentColor = ComposeAppTheme.colors.andy,
            ),
            content = {
                Text(
                    stringResource(R.string.Button_GettingStarted),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            enabled = true
        )
        Spacer(Modifier.height(10.dp))
        ButtonPrimary(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            onClick = {
                viewModel.onLoginClicked()
                navController.navigate("login_screen")
            },
            border = BorderStroke(
                width = 1.dp,
                color = ComposeAppTheme.colors.andy
            ),
            buttonColors = ButtonPrimaryDefaults.textButtonColors(
                backgroundColor = ComposeAppTheme.colors.transparent,
                contentColor = ComposeAppTheme.colors.leah,
                disabledBackgroundColor = ComposeAppTheme.colors.blade,
                disabledContentColor = ComposeAppTheme.colors.andy,
            ),
            content = {
                Text(
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = stringResource(R.string.Button_Login),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            enabled = true
        )
        Spacer(Modifier.height(60.dp))
    }
}

@Composable
private fun LoginScreen(viewModel: LoginActivityModel, navController: NavHostController, closeActivity: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        RadialBackground()
        Column(
            modifier = Modifier.fillMaxSize().padding(top = 50.dp, bottom = 0.dp,
                start = 20.dp, end = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column() {
                IconButton(onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(ComposeAppTheme.colors.lightGrey)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack,
                        tint = ComposeAppTheme.colors.dark,
                        contentDescription = "go back")
                }
                Spacer(Modifier.height(40.dp))
                Text(
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(R.string.Login_Title),
                    textAlign = TextAlign.Left,
                    color = colorResource(R.color.leah)
                )

                Spacer(Modifier.height(30.dp))
                LoginForm(viewModel = viewModel)
            }
            Column() {
                Spacer(Modifier.height(20.dp))
                LoginWithActionGroup(viewModel = viewModel)
            }
        }
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun Preview_KeysInvalidatedDialog() {
    val navController = rememberNavController()
    ComposeAppTheme(darkTheme = false) {
        LoginIntroScreen(LoginActivityModel(null), false) {

        }
    }
}
