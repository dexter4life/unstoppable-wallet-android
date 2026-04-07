package io.fastpayd.wallet.core.factories

import io.fastpayd.wallet.core.App
import io.fastpayd.wallet.modules.send.address.BitcoinAddressValidator
import io.fastpayd.wallet.modules.send.address.EnterAddressValidator
import io.fastpayd.wallet.modules.send.address.EvmAddressValidator
import io.fastpayd.wallet.modules.send.address.SolanaAddressValidator
import io.fastpayd.wallet.modules.send.address.StellarAddressValidator
import io.fastpayd.wallet.modules.send.address.TonAddressValidator
import io.fastpayd.wallet.modules.send.address.TronAddressValidator
import io.fastpayd.wallet.modules.send.address.ZcashAddressValidator
import io.horizontalsystems.marketkit.models.BlockchainType
import io.horizontalsystems.marketkit.models.Token

object AddressValidatorFactory {

    fun get(token: Token): EnterAddressValidator {
        return when (token.blockchainType) {
            BlockchainType.Bitcoin,
            BlockchainType.BitcoinCash,
            BlockchainType.ECash,
            BlockchainType.Litecoin,
            BlockchainType.Dash -> {
                BitcoinAddressValidator(token, App.adapterManager)
            }

            BlockchainType.Zcash -> {
                ZcashAddressValidator(token, App.adapterManager)
            }

            BlockchainType.Ethereum,
            BlockchainType.BinanceSmartChain,
            BlockchainType.Polygon,
            BlockchainType.Avalanche,
            BlockchainType.Optimism,
            BlockchainType.Base,
            BlockchainType.ZkSync,
            BlockchainType.Gnosis,
            BlockchainType.Fantom,
            BlockchainType.ArbitrumOne -> {
                EvmAddressValidator()
            }

            BlockchainType.Solana -> {
                SolanaAddressValidator()
            }

            BlockchainType.Tron -> {
                TronAddressValidator(token, App.adapterManager)
            }

            BlockchainType.Ton -> {
                TonAddressValidator()
            }

            is BlockchainType.Stellar -> {
                StellarAddressValidator(token)
            }

            is BlockchainType.Unsupported -> throw IllegalStateException("Unsupported blockchain type: ${token.blockchainType}")
        }
    }

}
