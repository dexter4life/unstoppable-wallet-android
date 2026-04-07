package io.fastpayd.bitcoincore

class BitcoinCore {
    sealed class SyncMode {
        class Blockchair : SyncMode()
        class Api : SyncMode()
        class Full : SyncMode()
    }
}