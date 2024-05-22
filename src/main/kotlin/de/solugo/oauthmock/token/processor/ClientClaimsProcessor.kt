package de.solugo.oauthmock.token.processor

import de.solugo.oauthmock.token.*
import de.solugo.oauthmock.util.clientId
import de.solugo.oauthmock.util.scopes
import org.jose4j.jwt.NumericDate
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class ClientClaimsProcessor : TokenProcessor {

    override val step = TokenProcessor.Step.CLAIMS

    override suspend fun process(context: TokenContext) {
        val client = context.client ?: return
        val allowedGrants = client.allowedGrants
        val scopes = context.scopes

        context.commonClaims.also {
            it.clientId = client.id
            it.scopes = scopes?.filter { allowedGrants.contains(it) || allowedGrants.contains("*") }?.toSet()
        }
        context.accessClaims.apply {
            client.accessTokenLifetime?.also {
                val now = Instant.now()
                issuedAt = NumericDate.fromSeconds(now.epochSecond)
                expirationTime = NumericDate.fromSeconds(now.plus(it).epochSecond)
            }
        }
    }

}
