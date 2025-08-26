package de.solugo.oauthmock.controller


import de.solugo.oauthmock.ConfigurationProvider
import de.solugo.oauthmock.ServerProperties
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.reactive.result.view.Rendering
import org.springframework.web.server.ServerWebExchange

@Controller
class AuthorizationController(
    private val properties: ServerProperties,
) : ConfigurationProvider {

    override fun provide(exchange: ServerWebExchange) = with(properties) {
        mapOf(
            "authorization_endpoint" to exchange.issuerUri {
                replacePath("/auth")
            }
        )
    }

    @GetMapping("/auth")
    fun authForm(
        @RequestParam("redirect_url") redirectUrl: String? = null,
    ) = run {
        val model = mapOf(
            "redirectUrl" to redirectUrl,
        )

        Rendering.view("authForm").model(model).build()
    }

    @PostMapping("/auth")
    fun authProcess(
        @RequestParam("redirect_url") redirectUrl: String,
    ) = run {
        Rendering.redirectTo(redirectUrl)
    }
}
