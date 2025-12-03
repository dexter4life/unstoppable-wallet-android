package io.horizontalsystems.bankwallet.modules.authentication.register.business

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.modules.authentication.register.AccountTypeView
import io.horizontalsystems.bankwallet.modules.authentication.register.RegisterActivityModel
import io.horizontalsystems.bankwallet.modules.authentication.register.RegisterView
import io.horizontalsystems.bankwallet.modules.settings.appearance.MenuItemWithDialog
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import io.horizontalsystems.bankwallet.ui.compose.components.ButtonPrimary
import io.horizontalsystems.bankwallet.ui.compose.components.ButtonPrimaryDefaults
import io.horizontalsystems.bankwallet.ui.compose.components.CellUniversalLawrenceSection


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RegistrationView(nightMode: Boolean) {
    val scrollState = rememberScrollState()
    var legalBusinessName by remember { mutableStateOf("") }
    var doingBusinessAs by remember { mutableStateOf("") }
    var ein by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var streetAddress by remember { mutableStateOf("") }
    var apartment by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var zipCode by remember { mutableStateOf("") }
    var industry by remember { mutableStateOf("") }
    var website by remember { mutableStateOf("") }

    val options = listOf("Option 1", "Option 2", "Option 3")
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(options[0]) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = if (!nightMode) ComposeAppTheme.colors.white else ComposeAppTheme.colors.dark)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Legal business name section
        Text(
            text = "Legal business name",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = legalBusinessName,
            onValueChange = { legalBusinessName = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textStyle = TextStyle(
                fontSize = 20.sp,
                color = colorResource(R.color.leah)
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ComposeAppTheme.colors.primary,
                unfocusedBorderColor = ComposeAppTheme.colors.gray500
            )
        )
        Text(
            text = "You must provide your official business registration number and legal business name exactly as shown on your government-issued documents. If unsure, check your registration or tax certificate.",
            fontSize = 12.sp,
            color = Color.Gray,
            lineHeight = 16.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text(
            text = "Business name (Doing Business As)",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = "The operating name of your company, if it's different than the legal name.",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = doingBusinessAs,
            onValueChange = { doingBusinessAs = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            textStyle = TextStyle(
                fontSize = 20.sp,
                color = colorResource(R.color.leah)
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ComposeAppTheme.colors.primary,
                unfocusedBorderColor = ComposeAppTheme.colors.gray500
            )
        )

        ButtonPrimary(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {},
            buttonColors = ButtonPrimaryDefaults.textButtonColors(
                backgroundColor = ComposeAppTheme.colors.primary,
                contentColor = ComposeAppTheme.colors.dark,
                disabledBackgroundColor = ComposeAppTheme.colors.blade,
                disabledContentColor = ComposeAppTheme.colors.andy,
            ),
            content = {
                Text(
                    text = "Create Account",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = ComposeAppTheme.colors.white
                )
            }
        )
    }
}


@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun Preview_KeysInvalidatedDialog() {
    val nightMode = isSystemInDarkTheme()
    ComposeAppTheme(darkTheme = false) {
        RegisterView(
            {},
            nightMode = nightMode
        ) {
            RegistrationView(nightMode)
        }
    }
}