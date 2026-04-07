package io.fastpayd.tronkit

import java.math.BigInteger

fun ByteArray.toBigInteger(): BigInteger = if (isEmpty()) BigInteger.ZERO else BigInteger(1, this)