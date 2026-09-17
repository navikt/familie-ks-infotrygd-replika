package no.nav.familie.ks.infotrygd.replika.exodus

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.content
import org.springframework.test.web.client.match.MockRestRequestMatchers.method
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withStatus
import org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess
import org.springframework.web.client.RestClient

class ExodusClientTest {
    private val baseUrl = "https://familie-ks-exodus.dev-fss-pub.nais.io"

    private lateinit var server: MockRestServiceServer
    private lateinit var exodusClient: ExodusClient

    @BeforeEach
    fun setUp() {
        val builder = RestClient.builder()
        server = MockRestServiceServer.bindTo(builder).build()
        exodusClient =
            ExodusClient(
                exodusRestClient = builder.build(),
                exodusProperties = ExodusProperties(baseUrl = baseUrl),
            )
    }

    @Test
    fun `hentUttrekk skal kalle absolutt URL med json content-type`() {
        server
            .expect(requestTo("$baseUrl/api/hentUttrekk"))
            .andExpect(method(HttpMethod.POST))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andRespond(
                withSuccess(
                    """
                    {
                      "iterator": "42",
                      "schema": { "kolonner": [ { "navn": "ID_BARN" } ] },
                      "innhold": [ [ "1" ] ]
                    }
                    """.trimIndent(),
                    MediaType.APPLICATION_JSON,
                ),
            )

        val respons = exodusClient.hentUttrekk(ExodusTabell.KS_BARN_10, null, 1000)

        assertThat(respons.iterator).isEqualTo("42")
        assertThat(respons.tilRader()).containsExactly(mapOf("id_barn" to "1"))
        server.verify()
    }

    @Test
    fun `tellRader skal kalle absolutt URL`() {
        server
            .expect(requestTo("$baseUrl/api/tellRader"))
            .andExpect(method(HttpMethod.POST))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andRespond(withSuccess("""{ "antall": 7 }""", MediaType.APPLICATION_JSON))

        assertThat(exodusClient.tellRader(ExodusTabell.KS_BARN_10)).isEqualTo(7)
        server.verify()
    }

    @Test
    fun `hentUttrekk skal mappe 409 til NyBaselineException`() {
        server
            .expect(requestTo("$baseUrl/api/hentUttrekk"))
            .andRespond(withStatus(HttpStatus.CONFLICT))

        assertThatThrownBy { exodusClient.hentUttrekk(ExodusTabell.KS_BARN_10, "1", 1000) }
            .isInstanceOf(NyBaselineException::class.java)
        server.verify()
    }

    @Test
    fun `base-url med trailing slash skal ikke gi dobbel skraastrek`() {
        val builder = RestClient.builder()
        val lokalServer = MockRestServiceServer.bindTo(builder).build()
        val klient =
            ExodusClient(
                exodusRestClient = builder.build(),
                exodusProperties = ExodusProperties(baseUrl = "$baseUrl/"),
            )

        lokalServer
            .expect(requestTo("$baseUrl/api/tellRader"))
            .andRespond(withSuccess("""{ "antall": 0 }""", MediaType.APPLICATION_JSON))

        klient.tellRader(ExodusTabell.KS_BARN_10)
        lokalServer.verify()
    }
}
