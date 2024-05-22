package de.solugo.oauthmock.model

import java.time.Duration


interface Client {
    val id: String
    val allowedGrants: Set<String>
    val accessTokenLifetime: Duration?
}
