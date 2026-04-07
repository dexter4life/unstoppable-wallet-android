package io.fastpayd.hdwalletkit

import java.security.MessageDigest

enum class Language {
    English,
    Japanese,
    Korean,
    Spanish,
    SimplifiedChinese,
    TraditionalChinese,
    French,
    Italian,
    Czech,
    Portuguese
}

class Mnemonic {
    enum class EntropyStrength {
        Words12,
        Words15,
        Words18,
        Words21,
        Words24;

        companion object {
            fun fromWordCount(count: Int): EntropyStrength = when (count) {
                15 -> Words15
                18 -> Words18
                21 -> Words21
                24 -> Words24
                else -> Words12
            }
        }
    }

    fun validate(words: List<String>) {
        validateStrict(words)
    }

    fun validateStrict(words: List<String>) {
        if (words.isEmpty()) {
            throw IllegalArgumentException("Mnemonic is empty")
        }
    }

    fun isWordValid(word: String, partial: Boolean): Boolean {
        return if (partial) word.isNotEmpty() else word.isNotBlank()
    }

    fun generate(strength: EntropyStrength, language: Language): List<String> {
        val count = when (strength) {
            EntropyStrength.Words12 -> 12
            EntropyStrength.Words15 -> 15
            EntropyStrength.Words18 -> 18
            EntropyStrength.Words21 -> 21
            EntropyStrength.Words24 -> 24
        }
        return List(count) { index -> "word${index + 1}" }
    }

    fun toSeed(words: List<String>, passphrase: String): ByteArray {
        val payload = (words.joinToString(" ") + "|" + passphrase).toByteArray()
        return MessageDigest.getInstance("SHA-256").digest(payload)
    }
}

object Base58 {
    fun encode(data: ByteArray): String = data.joinToString(separator = "") { "%02x".format(it) }

    fun decode(value: String): ByteArray = value.chunked(2).mapNotNull { it.toIntOrNull(16)?.toByte() }.toByteArray()
}

class WordList private constructor() {
    fun validWords(words: List<String>): Boolean = words.all { it.isNotBlank() }

    fun validWord(word: String, partial: Boolean): Boolean {
        return if (partial) {
            word.isNotEmpty()
        } else {
            word.isNotBlank()
        }
    }

    fun fetchSuggestions(prefix: String): List<String> {
        return if (prefix.isBlank()) emptyList() else listOf(prefix)
    }

    companion object {
        fun wordList(language: Language): WordList = WordList()

        fun wordListStrict(language: Language): WordList = WordList()
    }
}

class HDWallet {
    enum class Purpose {
        BIP44,
        BIP49,
        BIP84,
        BIP86
    }
}

enum class ExtendedKeyCoinType {
    Bitcoin,
    Litecoin
}

class HDExtendedKey {
    enum class DerivedType {
        Bip32,
        Master,
        Account
    }

    sealed class ParsingError(message: String) : Exception(message) {
        object WrongVersion : ParsingError("Wrong version")
    }

    private val serializedValue: String
    val key: ByteArray
    val derivedType: DerivedType
    val isPublic: Boolean
    val purposes: List<HDWallet.Purpose>
    val coinTypes: List<ExtendedKeyCoinType>

    constructor(serialized: String) {
        if (serialized.isBlank()) {
            throw IllegalArgumentException("Extended key is blank")
        }

        serializedValue = serialized
        key = serialized.toByteArray()
        isPublic = serialized.startsWith("xpub") || serialized.startsWith("ypub") || serialized.startsWith("zpub")
        derivedType = when {
            serialized.startsWith("xprv") || serialized.startsWith("xpub") -> DerivedType.Account
            serialized.startsWith("m/") -> DerivedType.Master
            else -> DerivedType.Bip32
        }
        purposes = listOf(HDWallet.Purpose.BIP44, HDWallet.Purpose.BIP49, HDWallet.Purpose.BIP84, HDWallet.Purpose.BIP86)
        coinTypes = listOf(ExtendedKeyCoinType.Bitcoin, ExtendedKeyCoinType.Litecoin)
    }

    constructor(seed: ByteArray, purpose: HDWallet.Purpose) {
        serializedValue = seed.joinToString(separator = "") { "%02x".format(it) }
        key = seed
        isPublic = false
        derivedType = DerivedType.Master
        purposes = listOf(purpose)
        coinTypes = listOf(ExtendedKeyCoinType.Bitcoin, ExtendedKeyCoinType.Litecoin)
    }

    fun serialize(): String = serializedValue

    fun serializePrivate(): String = serializedValue

    fun serializePrivate(version: Int): String = serializedValue

    fun serializePublic(version: Int): String = serializedValue
}

class HDExtendedKeyVersion(val value: Int) {
    companion object {
        fun initFrom(purpose: HDWallet.Purpose, coinType: ExtendedKeyCoinType, isPrivate: Boolean): HDExtendedKeyVersion {
            return HDExtendedKeyVersion(0)
        }
    }
}

class HDKeychain(private val seed: ByteArray) {
    fun accountKey(purpose: HDWallet.Purpose, coinType: Int, account: Int): HDExtendedKey = HDExtendedKey(seed, purpose)
}