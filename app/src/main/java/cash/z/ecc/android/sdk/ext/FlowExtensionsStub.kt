package cash.z.ecc.android.sdk.ext

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

fun <T> Flow<T>.collectWith(scope: CoroutineScope, action: suspend (T) -> Unit) {
    scope.launch {
        collect { action(it) }
    }
}

fun <T> Flow<T>.onFirstWith(scope: CoroutineScope, action: suspend (T) -> Unit) {
    scope.launch {
        firstOrNull()?.let { action(it) }
    }
}