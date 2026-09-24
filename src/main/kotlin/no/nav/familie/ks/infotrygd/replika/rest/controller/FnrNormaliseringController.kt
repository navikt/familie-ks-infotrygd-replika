package no.nav.familie.ks.infotrygd.replika.rest.controller

import no.nav.familie.ks.infotrygd.replika.normalisering.FnrKolonne
import no.nav.familie.ks.infotrygd.replika.normalisering.FnrNormaliseringRepository
import no.nav.familie.ks.infotrygd.replika.normalisering.SeqDatoKolonne
import no.nav.familie.ks.infotrygd.replika.normalisering.SeqDatoRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class FnrNormaliseringRespons(
    val antallOppdatert: Map<String, Int>,
)

@PreAuthorize("hasRole('FORVALTER')")
@RestController
@RequestMapping("/api/admin/fnr-normalisering")
class FnrNormaliseringController(
    private val fnrNormaliseringRepository: FnrNormaliseringRepository,
    private val seqDatoRepository: SeqDatoRepository,
) {
    @PostMapping
    @Transactional
    fun normaliser(): FnrNormaliseringRespons {
        val fnrResultater = FnrKolonne.entries.associate { it.målkolonne to fnrNormaliseringRepository.normaliser(it) }
        val seqDatoResultater = SeqDatoKolonne.entries.associate { it.målkolonne to seqDatoRepository.konverter(it) }
        return FnrNormaliseringRespons(fnrResultater + seqDatoResultater)
    }
}
