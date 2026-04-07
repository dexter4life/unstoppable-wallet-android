package io.fastpayd.wallet.core.managers

import android.content.Context

class ZcashBirthdayProvider(
    private val context: Context,
) {
    fun getLatestCheckpointBlockHeight(): Long {
        return 0L
    }
}
