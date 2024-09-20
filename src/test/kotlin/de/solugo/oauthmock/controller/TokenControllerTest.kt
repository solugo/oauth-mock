package de.solugo.oauthmock.controller

import IntegrationTest
import com.fasterxml.jackson.databind.node.ObjectNode
import de.solugo.oauthmock.util.clientId
import de.solugo.oauthmock.util.scopes
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import org.jose4j.jwt.consumer.JwtConsumerBuilder
import org.junit.jupiter.api.Test

class TokenControllerTest : IntegrationTest() {

    private val consumer = JwtConsumerBuilder().setSkipSignatureVerification().setSkipAllValidators().build()

    @Test
    fun `Get openid configuration`() = runTest {
        rest.get(".well-known/openid-configuration").apply {
            status shouldBe HttpStatusCode.OK
            body<ObjectNode>().apply {
                at("/issuer").textValue() shouldBe "https://my_issuer/"
            }
        }
    }

    @Test
    fun `Create only access token using password grant`() = runTest {
        val parameters = parametersOf(
            "grant_type" to listOf("password"),
            "client_id" to listOf("client_test"),
            "username" to listOf("test"),
        )

        rest.post("token") {
            setBody(FormDataContent(parameters))
        }.apply {
            status shouldBe HttpStatusCode.OK
            body<ObjectNode>().apply {
                at("/token_type").textValue() shouldBe "Bearer"
                at("/access_token").textValue() shouldNotBe null
                at("/id_token").textValue() shouldBe null
                at("/refresh_token").textValue() shouldBe null
            }
        }
    }


    @Test
    fun `Create token using password grant`() = runTest {
        val parameters = parametersOf(
            "grant_type" to listOf("password"),
            "client_id" to listOf("client_test"),
            "username" to listOf("test"),
            "scope" to listOf("openid offline_access"),
            "claim_qcc_simple" to listOf("common"),
            "idClaim_qic_simple" to listOf("id"),
            "accessClaim_qac_simple" to listOf("access"),
            "refreshClaim_qrc_simple" to listOf("refresh"),
            "claims" to listOf("""{"qcc": "common"}"""),
            "idClaims" to listOf("""{"qic": "id"}"""),
            "accessClaims" to listOf("""{"qac": "access"}"""),
            "refreshClaims" to listOf("""{"qrc": "refresh"}"""),
        )

        rest.post("token") {
            setBody(FormDataContent(parameters))
        }.apply {
            status shouldBe HttpStatusCode.OK
            body<ObjectNode>().apply {
                at("/token_type").textValue() shouldBe "Bearer"
                at("/access_token").textValue().also { token ->
                    val claims = consumer.processToClaims(token)
                    claims.subject shouldNotBe null
                    claims.clientId shouldBe "client_test"
                    claims.scopes shouldBe setOf("openid", "offline_access")
                    claims.getClaimValueAsString("qcc") shouldBe "common"
                    claims.getClaimValueAsString("qcc_simple") shouldBe "common"
                    claims.getClaimValueAsString("qac") shouldBe "access"
                    claims.getClaimValueAsString("qac_simple") shouldBe "access"
                    claims.hasClaim("qic") shouldBe false
                    claims.hasClaim("qic_simple") shouldBe false
                    claims.hasClaim("qrc") shouldBe false
                    claims.hasClaim("qrc_simple") shouldBe false
                }
                at("/id_token").textValue().also { token ->
                    val claims = consumer.processToClaims(token)
                    claims.subject shouldNotBe null
                    claims.clientId shouldBe "client_test"
                    claims.scopes shouldBe setOf("openid", "offline_access")
                    claims.getClaimValueAsString("qcc") shouldBe "common"
                    claims.getClaimValueAsString("qcc_simple") shouldBe "common"
                    claims.getClaimValueAsString("qic") shouldBe "id"
                    claims.getClaimValueAsString("qic_simple") shouldBe "id"
                    claims.hasClaim("qac") shouldBe false
                    claims.hasClaim("qac_simple") shouldBe false
                    claims.hasClaim("qrc") shouldBe false
                    claims.hasClaim("qrc_simple") shouldBe false
                }
                at("/refresh_token").textValue().also { token ->
                    val claims = consumer.processToClaims(token)
                    claims.subject shouldNotBe null
                    claims.clientId shouldBe "client_test"
                    claims.scopes shouldBe setOf("openid", "offline_access")
                    claims.getClaimValueAsString("qcc") shouldBe "common"
                    claims.getClaimValueAsString("qcc_simple") shouldBe "common"
                    claims.getClaimValueAsString("qrc") shouldBe "refresh"
                    claims.getClaimValueAsString("qrc_simple") shouldBe "refresh"
                    claims.hasClaim("qac") shouldBe false
                    claims.hasClaim("qac_simple") shouldBe false
                    claims.hasClaim("qic") shouldBe false
                    claims.hasClaim("qic_simple") shouldBe false
                }
            }
        }
    }

    @Test
    fun `Create token using client credentials grant`() = runTest {
        val parameters = parametersOf(
            "grant_type" to listOf("client_credentials"),
            "client_id" to listOf("client_test"),
            "scope" to listOf("custom"),
            "claim_qcc_simple" to listOf("common"),
            "idClaim_qic_simple" to listOf("id"),
            "accessClaim_qac_simple" to listOf("access"),
            "refreshClaim_qrc_simple" to listOf("refresh"),
            "claims" to listOf("""{"qcc": "common"}"""),
            "idClaims" to listOf("""{"qic": "id"}"""),
            "accessClaims" to listOf("""{"qac": "access"}"""),
            "refreshClaims" to listOf("""{"qrc": "refresh"}"""),
        )

        rest.post("token") {
            setBody(FormDataContent(parameters))
        }.apply {
            status shouldBe HttpStatusCode.OK
            body<ObjectNode>().apply {
                at("/token_type").textValue() shouldBe "Bearer"
                at("/access_token").textValue().also { token ->
                    val claims = consumer.processToClaims(token)
                    claims.subject shouldNotBe null
                    claims.clientId shouldBe "client_test"
                    claims.scopes shouldBe setOf("custom")
                    claims.getClaimValueAsString("qcc") shouldBe "common"
                    claims.getClaimValueAsString("qcc_simple") shouldBe "common"
                    claims.getClaimValueAsString("qac") shouldBe "access"
                    claims.getClaimValueAsString("qac_simple") shouldBe "access"
                    claims.hasClaim("qic") shouldBe false
                    claims.hasClaim("qic_simple") shouldBe false
                    claims.hasClaim("qrc") shouldBe false
                    claims.hasClaim("qrc_simple") shouldBe false
                }
                at("/id_token").textValue() shouldBe null
                at("/refresh_token").textValue() shouldBe null
            }
        }
    }

    @Test
    fun `Create token using refresh token`() = runTest {
        val passwordParameters = parametersOf(
            "grant_type" to listOf("password"),
            "client_id" to listOf("client_test"),
            "username" to listOf("test"),
            "scope" to listOf("openid offline_access"),
            "claim_qcc_simple" to listOf("common"),
            "idClaim_qic_simple" to listOf("id"),
            "accessClaim_qac_simple" to listOf("access"),
            "refreshClaim_qrc_simple" to listOf("refresh"),
            "claims" to listOf("""{"qcc": "common"}"""),
            "idClaims" to listOf("""{"qic": "id"}"""),
            "accessClaims" to listOf("""{"qac": "access"}"""),
            "refreshClaims" to listOf("""{"qrc": "refresh"}"""),
        )

        val refreshToken = rest.post("token") {
            setBody(FormDataContent(passwordParameters))
        }.run {
            body<ObjectNode>().at("/refresh_token").textValue()
        }

        val refreshParameters = parametersOf(
            "grant_type" to listOf("refresh_token"),
            "client_id" to listOf("client_test"),
            "refresh_token" to listOf(refreshToken),
        )

        rest.post("token") {
            setBody(FormDataContent(refreshParameters))
        }.apply {
            status shouldBe HttpStatusCode.OK
            body<ObjectNode>().apply {
                at("/token_type").textValue() shouldBe "Bearer"
                at("/access_token").textValue().also { token ->
                    val claims = consumer.processToClaims(token)
                    claims.subject shouldNotBe null
                    claims.clientId shouldBe "client_test"
                    claims.scopes shouldBe setOf("openid", "offline_access")
                    claims.getClaimValueAsString("qcc") shouldBe "common"
                    claims.getClaimValueAsString("qcc_simple") shouldBe "common"
                    claims.getClaimValueAsString("qac") shouldBe "access"
                    claims.getClaimValueAsString("qac_simple") shouldBe "access"
                    claims.hasClaim("qic") shouldBe false
                    claims.hasClaim("qic_simple") shouldBe false
                    claims.hasClaim("qrc") shouldBe false
                    claims.hasClaim("qrc_simple") shouldBe false
                }
                at("/id_token").textValue().also { token ->
                    val claims = consumer.processToClaims(token)
                    claims.subject shouldNotBe null
                    claims.clientId shouldBe "client_test"
                    claims.scopes shouldBe setOf("openid", "offline_access")
                    claims.getClaimValueAsString("qcc") shouldBe "common"
                    claims.getClaimValueAsString("qcc_simple") shouldBe "common"
                    claims.getClaimValueAsString("qic") shouldBe "id"
                    claims.getClaimValueAsString("qic_simple") shouldBe "id"
                    claims.hasClaim("qac") shouldBe false
                    claims.hasClaim("qac_simple") shouldBe false
                    claims.hasClaim("qrc") shouldBe false
                    claims.hasClaim("qrc_simple") shouldBe false
                }
                at("/refresh_token").textValue().also { token ->
                    val claims = consumer.processToClaims(token)
                    claims.subject shouldNotBe null
                    claims.clientId shouldBe "client_test"
                    claims.scopes shouldBe setOf("openid", "offline_access")
                    claims.getClaimValueAsString("qcc") shouldBe "common"
                    claims.getClaimValueAsString("qcc_simple") shouldBe "common"
                    claims.getClaimValueAsString("qrc") shouldBe "refresh"
                    claims.getClaimValueAsString("qrc_simple") shouldBe "refresh"
                    claims.hasClaim("qac") shouldBe false
                    claims.hasClaim("qac_simple") shouldBe false
                    claims.hasClaim("qic") shouldBe false
                    claims.hasClaim("qic_simple") shouldBe false
                }
            }
        }
    }
}
