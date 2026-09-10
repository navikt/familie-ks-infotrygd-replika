package no.nav.familie.ks.infotrygd.replika.rest.controller

import io.micrometer.core.annotation.Timed
import no.nav.familie.ks.infotrygd.replika.service.InnsynService
import no.nav.familie.ks.infotrygd.replika.service.SøkerOgBarn
import no.nav.infotrygd.kontantstotte.dto.InnsynRequest
import no.nav.infotrygd.kontantstotte.dto.InnsynResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@PreAuthorize("hasRole('FORVALTER') or hasRole('APPLICATION')")
@RestController
@RequestMapping("/api")
@Timed(value = "infotrygd_kontantstottev2_innsyn_controller", percentiles = [0.5, 0.95])
class InnsynController(
    private val innsynService: InnsynService,
) {
    @PostMapping("/hentPerioderMedKontantstøtteIInfotrygd", "/hentPerioderMedKontantstotteIInfotrygd")
    fun hentPerioder(
        @RequestBody req: InnsynRequest,
    ): InnsynResponse = innsynService.hentDataForSøker(req)

    @PostMapping("/hentPerioderMedKontantstøtteIInfotrygdByBarn", "/hentPerioderMedKontantstotteIInfotrygdByBarn")
    fun hentPerioderForBarn(
        @RequestBody req: InnsynRequest,
    ): InnsynResponse = innsynService.hentDataForBarn(req)

    @GetMapping("/hentidentertilbarnmedlopendesaker")
    fun harKontantstotteIInfotrygd(): List<String> = innsynService.hentbarnmedløpendekontantstøtte()

    @GetMapping("/hent-soekere-og-barn-med-loepende-kontantstoette")
    fun hentSøkereOgBarnMedLøpendeKontantstøtteIInfotrygd(): List<SøkerOgBarn> = innsynService.hentSøkerOgBarnMedLøpendeKontantstøtte()
}
