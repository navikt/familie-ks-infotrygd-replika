package no.nav.familie.ks.infotrygd.replika.rest.controller

import no.nav.familie.ks.infotrygd.replika.normalisering.FnrNormaliseringRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class FnrNormaliseringRespons(
    val antallOppdatert: Int,
)

@PreAuthorize("hasRole('FORVALTER')")
@RestController
@RequestMapping("/api/admin/fnr-normalisering")
class FnrNormaliseringController(
    private val repository: FnrNormaliseringRepository,
) {
    @PostMapping
    @Transactional
    fun normaliser(): FnrNormaliseringRespons = FnrNormaliseringRespons(repository.normaliserBarn())
}
