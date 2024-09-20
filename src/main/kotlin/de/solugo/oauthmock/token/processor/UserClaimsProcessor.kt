package de.solugo.oauthmock.token.processor

import de.solugo.oauthmock.token.TokenContext
import de.solugo.oauthmock.token.TokenProcessor
import de.solugo.oauthmock.token.commonClaims
import de.solugo.oauthmock.token.user
import de.solugo.oauthmock.util.preferredUsername
import org.springframework.stereotype.Component

@Component
class UserClaimsProcessor : TokenProcessor {

    override val step = TokenProcessor.Step.CLAIMS

    override suspend fun process(context: TokenContext) {
        val user = context.user ?: return

        context.commonClaims.apply {
            subject = subject ?: user.id
            preferredUsername = preferredUsername ?: user.username
        }
    }

}
