package de.solugo.oauthmock.token

import com.fasterxml.jackson.annotation.JsonView

enum class TokenError(@get:JsonView val value: String) {
    InvalidRequest("invalid_request"),
    UnauthorizedClient("unauthorized_client"),
    AccessDenied("access_denied"),
    UnsupportedResponseType("unsupported_response_type"),
    InvalidScope("invalid_scope"),
    ServerError("server_error"),
    TemporarilyUnavailable("temporarily_unavailable"),
}
