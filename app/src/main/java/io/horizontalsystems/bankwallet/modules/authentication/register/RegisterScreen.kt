package io.horizontalsystems.bankwallet.modules.authentication.register

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.horizontalsystems.bankwallet.core.App
import io.horizontalsystems.bankwallet.core.BaseActivity
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import kotlin.getValue
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.colorResource
import androidx.glance.appwidget.lazy.LazyListScope
import io.github.alexzhirkevich.qrose.options.roundCorners
import io.horizontalsystems.bankwallet.R

import kotlinx.coroutines.delay


class RegisterActivity(private val navController: NavHostController) : BaseActivity() {
    val viewModel by viewModels<RegisterActivityModel> { RegisterModule.Factory() }

    private val nightMode by lazy {
        val uiMode =
            App.instance.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)
        uiMode == Configuration.UI_MODE_NIGHT_YES
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RegisterScreen(viewModel, navController, nightMode) { finish() }
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, RegisterActivity::class.java)
            context.startActivity(intent)
        }
    }
}

@Composable
private fun RegisterScreen(viewModel: RegisterActivityModel, navController: NavHostController, nightMode: Boolean
                           , closeActivity: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(top = 30.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ){
       Column(modifier = Modifier.fillMaxWidth(),
           verticalArrangement = Arrangement.spacedBy(10.dp)) {
           Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 13.dp).height(50.dp),
               horizontalArrangement = Arrangement.spacedBy(8.dp),
               verticalAlignment = Alignment.CenterVertically,

               ){
               IconButton(onClick = { navController.popBackStack() },
                   modifier = Modifier
                       .clip(RoundedCornerShape(50.dp)).size(35.dp)) {
                   Icon(Icons.AutoMirrored.Filled.ArrowBack,
                       tint = ComposeAppTheme.colors.leah,
                       contentDescription = "go back",  modifier = Modifier.size(25.dp) )
               }
               Text(text = "Create Account", modifier = Modifier.padding(end = 26.dp),
                   fontSize = 18.sp,
                   color = colorResource(R.color.leah),
                   fontWeight = FontWeight.Bold)
           }
           DeterminateProgress()
           AvailableCountries()
       }
    }
}


@Composable
fun DeterminateProgress() {
    var currentProgress by remember { mutableStateOf(0.1f) }

    LaunchedEffect(Unit) {
//        while (currentProgress < 1f) {
//            delay(100) // update every 0.1 seconds
//            currentProgress += 0.01f
//        }
    }

    LinearProgressIndicator(
        progress = { currentProgress },
        modifier = Modifier.fillMaxWidth().height(10.dp).clip(RectangleShape),
        trackColor = ProgressIndicatorDefaults.linearTrackColor,
        strokeCap = StrokeCap.Butt,
    )
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun Preview_KeysInvalidatedDialog() {
    val navController = rememberNavController()
    ComposeAppTheme() {
        RegisterScreen(RegisterActivityModel(null), navController, nightMode = false) {

        }
    }
}
