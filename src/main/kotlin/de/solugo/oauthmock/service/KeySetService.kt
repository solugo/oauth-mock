package de.solugo.oauthmock.service

import de.solugo.oauthmock.util.uuid
import org.jose4j.jwk.RsaJwkGenerator
import org.springframework.stereotype.Service

@Service
class KeySetService {

    val keys by lazy {
        listOf(
            RsaJwkGenerator.generateJwk(2048).apply {
                keyId = uuid()
                use = "sig"
                algorithm = "RS256"
            }
        )
    }

}
