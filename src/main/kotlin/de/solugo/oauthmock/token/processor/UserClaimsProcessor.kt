package de.solugo.oauthmock.token.processor

import de.solugo.oauthmock.token.*
import de.solugo.oauthmock.util.preferredUsername
import org.springframework.stereotype.Component

@Component
class UserClaimsProcessor : TokenProcessor {

    override val step =  TokenProcessor.Step.CLAIMS

    override suspend fun process(context: TokenContext) {
        val user = context.user ?: return

        context.commonClaims.apply {
            subject = user.id
            preferredUsername = user.username
        }
    }

}
