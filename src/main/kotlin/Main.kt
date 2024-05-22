@file:JvmName("OAuthMock")

import de.solugo.oauthmock.Server
import org.springframework.boot.runApplication


fun main(args: Array<String>) {
    runApplication<Server>(*args)
}
