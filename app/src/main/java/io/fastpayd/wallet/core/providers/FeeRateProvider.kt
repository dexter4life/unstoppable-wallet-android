package io.fastpayd.wallet.core.providers

import io.fastpayd.wallet.core.IFeeRateProvider
import io.reactivex.Single
import java.math.BigInteger

class FeeRateProvider(appConfig: AppConfigProvider) {
    data class RecommendedFees(val halfHourFee: Int, val minimumFee: Int)

    fun bitcoinFeeRate(): Single<RecommendedFees> {
        return Single.just(RecommendedFees(2, 1))
    }

    fun litecoinFeeRate(): Single<BigInteger> {
        return Single.just(BigInteger.valueOf(2))
    }

    fun bitcoinCashFeeRate(): Single<BigInteger> {
        return Single.just(BigInteger.valueOf(2))
    }

    fun dashFeeRate(): Single<BigInteger> {
        return Single.just(BigInteger.valueOf(2))
    }

}

class BitcoinFeeRateProvider(private val feeRateProvider: FeeRateProvider) : IFeeRateProvider {
    override val feeRateChangeable = true

    override suspend fun getFeeRates(): FeeRates {
        val bitcoinFeeRate = feeRateProvider.bitcoinFeeRate().blockingGet()
        return FeeRates(bitcoinFeeRate.halfHourFee, bitcoinFeeRate.minimumFee)
    }
}

class LitecoinFeeRateProvider(private val feeRateProvider: FeeRateProvider) : IFeeRateProvider {
    override suspend fun getFeeRates(): FeeRates {
        return FeeRates(2)
    }
}

class BitcoinCashFeeRateProvider(private val feeRateProvider: FeeRateProvider) : IFeeRateProvider {
    override suspend fun getFeeRates(): FeeRates {
        val feeRate = feeRateProvider.bitcoinCashFeeRate().blockingGet()
        return FeeRates(feeRate.toInt())
    }
}

class DashFeeRateProvider(private val feeRateProvider: FeeRateProvider) : IFeeRateProvider {
    override suspend fun getFeeRates(): FeeRates {
        val feeRate = feeRateProvider.dashFeeRate().blockingGet()
        return FeeRates(feeRate.toInt())
    }
}

class ECashFeeRateProvider : IFeeRateProvider {
    override suspend fun getFeeRates(): FeeRates {
        return FeeRates(2)
    }
}

data class FeeRates(
    val recommended: Int,
    val minimum: Int = 0,
)