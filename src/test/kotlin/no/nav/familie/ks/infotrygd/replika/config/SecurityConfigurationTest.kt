package no.nav.familie.ks.infotrygd.replika.config

import no.nav.familie.ks.infotrygd.replika.testutil.AbstractStonadFactoryTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.actuate.info.InfoEndpoint
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.ApplicationContext
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.client.RestClient

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = [
        "exodus.base-url=http://localhost",
        "EXODUS_SCOPE=test-scope",
        "NAIS_TOKEN_ENDPOINT=http://localhost/api/v1/token",
        "NAIS_TOKEN_EXCHANGE_ENDPOINT=http://localhost/api/v1/token/exchange",
    ],
)
@ActiveProfiles("test")
class SecurityConfigurationTest : AbstractStonadFactoryTest() {
    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    private val restClient =
        RestClient
            .builder()
            .defaultStatusHandler(HttpStatusCode::isError) { _, _ -> }
            .build()

    @ParameterizedTest
    @ValueSource(strings = ["/internal", "/internal/health/liveness", "/internal/health/readiness", "/internal/prometheus"])
    fun `skal tillate offentlige internal-endepunkter uten token`(path: String) {
        val response =
            restClient
                .get()
                .uri("http://localhost:$port$path")
                .retrieve()
                .toBodilessEntity()

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "/internal/info",
            "/internal/mappings",
            "/internal/status/isAlive",
            "/internal/healthcheck",
            "/internal/prometheus/other",
            "/actuator/health",
        ],
    )
    fun `skal avvise øvrige interne endepunkter uten token`(path: String) {
        val response =
            restClient
                .get()
                .uri("http://localhost:$port$path")
                .retrieve()
                .toBodilessEntity()

        assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun `info-endepunktet er deaktivert`() {
        assertThat(applicationContext.getBeansOfType(InfoEndpoint::class.java)).isEmpty()
    }
}
