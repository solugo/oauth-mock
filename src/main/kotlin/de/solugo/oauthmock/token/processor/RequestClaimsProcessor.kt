package de.solugo.oauthmock.token.processor

import de.solugo.oauthmock.token.*
import de.solugo.oauthmock.util.put
import de.solugo.oauthmock.util.removePrefixOrNull
import org.jose4j.jwt.JwtClaims
import org.springframework.stereotype.Component

@Component
class RequestClaimsProcessor : TokenProcessor {

    override val step = TokenProcessor.Step.CLAIMS

    override suspend fun process(context: TokenContext) {
        context.parameters["claims"]?.forEach {
            context.commonClaims.put(JwtClaims.parse(it))
        }
        context.parameters["idClaims"]?.forEach {
            context.idClaims.put(JwtClaims.parse(it))
        }
        context.parameters["accessClaims"]?.forEach {
            context.accessClaims.put(JwtClaims.parse(it))
        }
        context.parameters["refreshClaims"]?.forEach {
            context.refreshClaims.put(JwtClaims.parse(it))
        }
        context.parameters.entries.forEach { (key, values) ->
            key.removePrefixOrNull("claim_")?.also {
                context.commonClaims.setClaim(it, values.last())
            }
            key.removePrefixOrNull("accessClaim_")?.also {
                context.accessClaims.setClaim(it, values.last())
            }
            key.removePrefixOrNull("idClaim_")?.also {
                context.idClaims.setClaim(it, values.last())
            }
            key.removePrefixOrNull("refreshClaim_")?.also {
                context.refreshClaims.setClaim(it, values.last())
            }
        }
    }

}
