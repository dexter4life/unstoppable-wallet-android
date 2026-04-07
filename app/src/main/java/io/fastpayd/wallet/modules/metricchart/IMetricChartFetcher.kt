package io.fastpayd.wallet.modules.metricchart

import io.fastpayd.wallet.ui.compose.TranslatableString

interface IMetricChartFetcher {
    val title: Int
    val description: TranslatableString
    val poweredBy: TranslatableString
}