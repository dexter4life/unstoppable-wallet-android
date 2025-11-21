package io.horizontalsystems.bankwallet.modules.authentication.register

import androidx.lifecycle.ViewModel
import io.horizontalsystems.bankwallet.core.ILocalStorage

    class RegisterActivityModel (
    public val localStorage: ILocalStorage?
): ViewModel() {
    val countries = listOf("")
}
