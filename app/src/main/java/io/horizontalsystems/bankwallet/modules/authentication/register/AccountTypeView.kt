package io.horizontalsystems.bankwallet.modules.authentication.register

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import io.horizontalsystems.bankwallet.ui.compose.components.HsIconButton
import kotlinx.coroutines.launch

@Composable
fun AccountTypeCard(
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit,
    nightMode: Boolean,
    @DrawableRes icon: Int
) {
    val borderColor = if (selected) ComposeAppTheme.colors.primary
    else if (nightMode) ComposeAppTheme.colors.dark500
    else ComposeAppTheme.colors.gray300

    val backgroundColor = if (selected) ComposeAppTheme.colors.primary.copy(alpha = 0.05f)
    else if (nightMode) ComposeAppTheme.colors.dark900
    else ComposeAppTheme.colors.white

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp, color = borderColor, shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick.invoke() }
            .padding(vertical = 10.dp, horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Image(
            modifier = Modifier.size(40.dp),
            painter = painterResource(icon),
            contentDescription = ""
        )
        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = if (nightMode) ComposeAppTheme.colors.gray100
                else ComposeAppTheme.colors.dark
            )
            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = if (nightMode) ComposeAppTheme.colors.dark100
                else ComposeAppTheme.colors.dark
            )
        }
    }
}

@Composable
fun AccountTypeView(
    nightMode: Boolean = isSystemInDarkTheme(), onAccountTypeChange: (accountType: String) -> Unit
) {
    val selectedType = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = if (nightMode) ComposeAppTheme.colors.dark900 else ComposeAppTheme.colors.white)
            .padding(horizontal = 20.dp, vertical = 25.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.AccountTypeView_Title),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = if (nightMode) ComposeAppTheme.colors.gray100
                else ComposeAppTheme.colors.dark600,
                lineHeight = 30.sp
            )
            Spacer(Modifier.height(20.dp))
            AccountTypeCard(
                title = stringResource(R.string.AccountTypeView_Personal_Title),
                subtitle = stringResource(R.string.AccountTypeView_Personal_subtitle),
                selected = selectedType.value == "personal",
                onClick = {
                    selectedType.value = "personal"
                    onAccountTypeChange.invoke(selectedType.value)
                },
                icon = R.drawable.account_type_personal,
                nightMode = nightMode
            )
            Spacer(Modifier.height(12.dp))
            AccountTypeCard(
                title = stringResource(R.string.AccountTypeView_Business_Title),
                subtitle = stringResource(R.string.AccountTypeView_Business_subtitle),
                selected = selectedType.value == "business",
                onClick = {
                    selectedType.value = "business"
                    onAccountTypeChange.invoke(selectedType.value)
                },
                icon = R.drawable.account_type_business,
                nightMode = nightMode
            )
        }
        Text(
            text = buildAnnotatedString() {
                append("You acknowledged that during your usage of FastPayd, you agree to our ")
                withStyle(
                    style = SpanStyle(
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Medium,
                        color = ComposeAppTheme.colors.issykBlue
                    )
                ) {
                    append("Usage Policy")
                }

                append(". You can not use Personal Account for business Purposes.")
            },
            modifier = Modifier.fillMaxWidth(),
            fontSize = 14.sp,
            textAlign = TextAlign.Left,
            color = if (nightMode) ComposeAppTheme.colors.gray500
            else ComposeAppTheme.colors.dark400
        )
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview_RegisterDark() {
    val nightMode = isSystemInDarkTheme()
    val navController = rememberNavController()
    ComposeAppTheme(darkTheme = false) {
        RegisterView(
            { navController.popBackStack() }, nightMode = nightMode
        ) {
            AccountTypeView(
                nightMode = nightMode, onAccountTypeChange = {})
        }

    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun Preview_RegisterLight() {
    val nightMode = isSystemInDarkTheme()
    val navController = rememberNavController()
    ComposeAppTheme(darkTheme = false) {
        RegisterView(
            { navController.popBackStack() }, nightMode = nightMode
        ) {
            AccountTypeView(
                nightMode = nightMode, onAccountTypeChange = {})
        }

    }
}
