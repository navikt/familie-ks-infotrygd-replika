package no.nav.familie.ks.infotrygd.replika.exodus

import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

/**
 * Klient mot familie-ks-exodus sitt REST-API. /api/tellRader er kun ment til engangsbruk for
 * progresjonsvisning, og skal ikke kalles fra replikeringsløypa (se ExodusKlientService).
 */
@Component
class ExodusClient(
    private val exodusRestClient: RestClient,
) {
    fun hentUttrekk(
        tabell: ExodusTabell,
        iterator: String?,
        antallRader: Int,
    ): HentUttrekkResponse =
        try {
            exodusRestClient
                .post()
                .uri("/api/hentUttrekk")
                .body(HentUttrekkRequest(tabell.tabellNavn, iterator, antallRader.toLong()))
                .retrieve()
                .body<HentUttrekkResponse>() ?: HentUttrekkResponse(iterator = iterator.orEmpty())
        } catch (e: HttpClientErrorException.Conflict) {
            throw NyBaselineException(tabell)
        }

    fun tellRader(tabell: ExodusTabell): Long =
        exodusRestClient
            .post()
            .uri("/api/tellRader")
            .body(TellRaderRequest(tabell.tabellNavn))
            .retrieve()
            .body<TellRaderResponse>()
            ?.antall ?: 0
}
