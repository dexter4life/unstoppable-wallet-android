package io.horizontalsystems.bankwallet.modules.authentication.login

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.modules.authentication.AuthActivityModel
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme


@Composable
fun LoginScreen(
    viewModel: AuthActivityModel,
    navController: NavHostController,
    nightMode: Boolean,
    closeActivity: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = if (!nightMode) ComposeAppTheme.colors.gray100
                else ComposeAppTheme.colors.dark700
            ),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(
                    top = 40.dp, bottom = 30.dp, start = 10.dp, end = 10.dp
                )
                .clip(RoundedCornerShape(20.dp)),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = if (!nightMode) ComposeAppTheme.colors.white
                        else ComposeAppTheme.colors.dark
                    )
                    .clip(RoundedCornerShape(16.dp)),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.padding(top = 15.dp, start = 13.dp, end = 13.dp)) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(
                                if (nightMode) ComposeAppTheme.colors.dark500
                                else ComposeAppTheme.colors.gray100
                            )
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            tint = if (nightMode) ComposeAppTheme.colors.gray200
                            else ComposeAppTheme.colors.dark,
                            contentDescription = null
                        )
                    }
                    Spacer(Modifier.height(40.dp))
                    Text(
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.Login_Title),
                        textAlign = TextAlign.Center,
                        color = colorResource(R.color.leah)
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        fontSize = 16.sp,
                        modifier = Modifier.fillMaxWidth(),
                        text = "To sign in to your account, you need to provide an email and password.",
                        textAlign = TextAlign.Center,
                        color = ComposeAppTheme.colors.dark200
                    )
                    Spacer(Modifier.height(30.dp))
                    LoginForm(viewModel = viewModel, navController = navController)
                }
                Column(modifier = Modifier.padding(start = 13.dp, end = 13.dp)) {
                    Spacer(Modifier.height(20.dp))
                    LoginWithActionGroup(viewModel = viewModel, nightMode = nightMode)
                }
            }
        }
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun Preview_Login() {
    val navController = rememberNavController()
    ComposeAppTheme(darkTheme = false) {
        LoginScreen(
            AuthActivityModel(null), navController,
            isSystemInDarkTheme()
        ) {}
    }
}
