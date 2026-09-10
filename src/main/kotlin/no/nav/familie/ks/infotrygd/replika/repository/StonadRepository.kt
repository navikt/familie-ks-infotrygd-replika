package no.nav.familie.ks.infotrygd.replika.repository

import no.nav.commons.foedselsnummer.Foedselsnummer
import no.nav.familie.ks.infotrygd.replika.model.ks.Barn
import no.nav.familie.ks.infotrygd.replika.model.ks.Stonad
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.math.BigDecimal

@Repository
interface StonadRepository : JpaRepository<Stonad, BigDecimal> {
    fun findByFnrIn(fnr: List<Foedselsnummer>): List<Stonad>

    fun findByBarnIn(barn: List<Barn>): List<Stonad>

    fun findByOpphoertVfomEquals(opphoertFom: String): List<Stonad>
}
