package de.solugo.oauthmock.token.grant

import de.solugo.oauthmock.service.TokenService
import de.solugo.oauthmock.token.*
import de.solugo.oauthmock.util.clientId
import de.solugo.oauthmock.util.put
import de.solugo.oauthmock.util.scopes
import org.springframework.stereotype.Component

@Component
class RefreshTokenGrant(
    private val tokenService: TokenService,

    ) : TokenGrant {
    override val type: String = "refresh_token"

    override suspend fun process(context: TokenContext) {
        val client = context.client ?: throw TokenException(
            error = TokenError.UnauthorizedClient,
            description = "Client resolution failed",
        )
        val refreshToken = context.refreshToken ?: throw TokenException(
            error = TokenError.InvalidRequest,
            description = "Request is missing refresh_token parameter",
        )

        val refreshTokenClaims = tokenService.decodeJwt(refreshToken).jwtClaims

        if (refreshTokenClaims.clientId != client.id) throw TokenException(
            error = TokenError.AccessDenied,
            description = "Client is not allowed to use this refresh token",
        )

        val refreshScopes = refreshTokenClaims.scopes ?: emptySet()

        (refreshTokenClaims.getClaimValue("common_claims") as? Map<*, *>)?.also { claims ->
            context.commonClaims.put(claims)
        }

        (refreshTokenClaims.getClaimValue("refresh_claims") as? Map<*, *>)?.also { claims ->
            context.refreshClaims.put(claims)
        }

        (refreshTokenClaims.getClaimValue("access_claims") as? Map<*, *>)?.also { claims ->
            context.accessClaims.put(claims)
        }

        (refreshTokenClaims.getClaimValue("id_claims") as? Map<*, *>)?.also { claims ->
            context.idClaims.put(claims)
        }

        context.scopes = context.scopes?.filter { refreshScopes.contains(it) }?.toSet() ?: refreshScopes
    }
}
