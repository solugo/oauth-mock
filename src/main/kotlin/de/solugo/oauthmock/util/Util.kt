package de.solugo.oauthmock.util

import org.springframework.web.util.UriComponentsBuilder
import java.util.*

fun uuid(value: String? = null) = when {
    value != null -> UUID.nameUUIDFromBytes(value.toByteArray()).toString()
    else -> UUID.randomUUID().toString()
}

fun uri(uri: String, block: UriComponentsBuilder.() -> Unit) = UriComponentsBuilder.fromUriString(uri).run {
    block()
    toUriString()
}


fun String.removePrefixOrNull(prefix: String) = when {
    startsWith(prefix) -> substring(prefix.length)
    else -> null
}
