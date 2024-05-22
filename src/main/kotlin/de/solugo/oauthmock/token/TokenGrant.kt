package de.solugo.oauthmock.token

interface TokenGrant {
    val type: String
    suspend fun process(context: TokenContext)
}
