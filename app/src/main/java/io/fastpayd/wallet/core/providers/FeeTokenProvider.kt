package io.fastpayd.wallet.core.providers

import io.fastpayd.wallet.core.managers.MarketKitWrapper
import io.fastpayd.wallet.core.protocolType
import io.horizontalsystems.marketkit.models.Token
import io.horizontalsystems.marketkit.models.TokenQuery
import io.horizontalsystems.marketkit.models.TokenType

class FeeTokenProvider(
    private val marketKit: MarketKitWrapper
) {

    fun feeTokenData(token: Token): Pair<Token, String>? {
        val tokenQuery = when (token.type) {
            is TokenType.Eip20,
            is TokenType.Spl,
            is TokenType.Jetton,
            is TokenType.Asset,
            -> {
                TokenQuery(token.blockchainType, TokenType.Native)
            }
            TokenType.Native,
            is TokenType.Derived,
            is TokenType.AddressTyped,
            is TokenType.Unsupported -> null
        }

        return tokenQuery?.let {
            marketKit.token(it)?.let { feeToken ->
                Pair(feeToken, feeToken.protocolType!!)
            }
        }
    }
}
