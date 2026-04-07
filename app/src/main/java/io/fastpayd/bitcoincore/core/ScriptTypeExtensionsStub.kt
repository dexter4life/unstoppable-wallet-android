package io.fastpayd.bitcoincore.core

import io.fastpayd.bitcoincore.transactions.scripts.ScriptType
import io.fastpayd.hdwalletkit.HDWallet

val ScriptType.purpose: HDWallet.Purpose?
    get() = when (this) {
        ScriptType.P2PKH -> HDWallet.Purpose.BIP44
        ScriptType.P2SH -> HDWallet.Purpose.BIP49
        ScriptType.P2WPKH -> HDWallet.Purpose.BIP84
        ScriptType.P2TR -> HDWallet.Purpose.BIP86
    }