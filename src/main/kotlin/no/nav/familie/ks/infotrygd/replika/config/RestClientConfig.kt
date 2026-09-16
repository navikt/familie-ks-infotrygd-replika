package no.nav.familie.ks.infotrygd.replika.config

import no.nav.familie.felles.tokenklient.entraid.EntraIDClient
import no.nav.familie.felles.tokenklient.entraid.EntraIDRestClientFactory
import no.nav.familie.felles.tokenklient.tokenx.TokenXClient
import no.nav.familie.log.interceptor.ConsumerIdClientInterceptor
import no.nav.familie.log.interceptor.MdcValuesPropagatingClientInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.client.RestClient

@Configuration
@Import(
    EntraIDRestClientFactory::class,
    EntraIDClient::class,
)
class RestClientConfig(
    private val entraIDRestClientFactory: EntraIDRestClientFactory,
) {
    @Bean("exodusRestClient")
    fun exodusRestClient(
        @Value("\${EXODUS_SCOPE}") scope: String,
    ): RestClient = entraIDRestClientFactory.lagMaskinTilMaskinRestKlient(scope)
}
