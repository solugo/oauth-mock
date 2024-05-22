package de.solugo.oauthmock.token.grant

import de.solugo.oauthmock.model.User
import de.solugo.oauthmock.token.*
import de.solugo.oauthmock.util.uuid
import org.springframework.stereotype.Component

@Component
class PasswordGrant : TokenGrant {
    override val type = "password"

    override suspend fun process(context: TokenContext) {
        val username = context.username ?: return

        context.user = object : User {
            override val id = context.subject ?: uuid(username)
            override val username = username
        }
    }

}
