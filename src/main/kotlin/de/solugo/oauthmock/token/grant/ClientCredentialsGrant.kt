package de.solugo.oauthmock.token.grant

import de.solugo.oauthmock.model.User
import de.solugo.oauthmock.token.*
import de.solugo.oauthmock.util.uuid
import org.springframework.stereotype.Component

@Component
class ClientCredentialsGrant : TokenGrant {
    override val type = "client_credentials"

    override suspend fun process(context: TokenContext) {
        val client = context.client ?: throw TokenException(
            error = TokenError.InvalidRequest,
            description = "Client could not be found",
        )

        context.user = object : User {
            override val id = context.subject ?: uuid(client.id)
            override val username = client.id
        }
    }
}
