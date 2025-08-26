plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.jib)
}

group = "de.solugo"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.bundles.kotlin)
    implementation(libs.bundles.kotlin.coroutines)
    implementation(libs.bundles.spring.boot)
    implementation(libs.bundles.jose4j)

    testImplementation(libs.bundles.kotlin.coroutines.test)
    testImplementation(libs.bundles.spring.boot.test)
    testImplementation(libs.bundles.junit)
    testImplementation(libs.bundles.ktor)
    testImplementation(libs.bundles.kotest)
    testRuntimeOnly(libs.bundles.junit.runtime)
}

kotlin {
    jvmToolchain(21)
}

springBoot {
    buildInfo()
}

jib {
    from {
        image = "openjdk:23-slim-bullseye"
    }
    container {
        mainClass = "OAuthMock"
    }
    to {
        image = "oauth-mock"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("failed", "passed")
        showCauses = true
    }
}
