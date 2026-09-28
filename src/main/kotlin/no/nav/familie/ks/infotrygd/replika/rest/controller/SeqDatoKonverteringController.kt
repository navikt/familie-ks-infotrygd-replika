package no.nav.familie.ks.infotrygd.replika.rest.controller

import no.nav.familie.ks.infotrygd.replika.normalisering.SeqDatoKolonne
import no.nav.familie.ks.infotrygd.replika.normalisering.SeqDatoRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class SeqDatoKonverteringRespons(
    val kolonne: String,
    val antallOppdatert: Int,
)

@PreAuthorize("hasRole('FORVALTER')")
@RestController
@RequestMapping("/api/admin/seq-dato-konvertering")
class SeqDatoKonverteringController(
    private val repository: SeqDatoRepository,
) {
    @PostMapping("/{kolonne}")
    @Transactional
    fun konverter(
        @PathVariable kolonne: SeqDatoKolonne,
    ): SeqDatoKonverteringRespons =
        SeqDatoKonverteringRespons(
            kolonne = kolonne.målkolonne,
            antallOppdatert = repository.konverter(kolonne),
        )
}
