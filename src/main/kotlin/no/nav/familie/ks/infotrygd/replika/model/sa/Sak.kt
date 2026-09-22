package no.nav.familie.ks.infotrygd.replika.model.sa

import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import no.nav.commons.foedselsnummer.Foedselsnummer
import no.nav.familie.ks.infotrygd.replika.model.converters.ReversedFoedselNrConverter
import java.math.BigDecimal

@Suppress("unused") // brukes av hibernate for å generere hvilke tabeller som brukes
@Entity
@Table(name = "SA_SAK_10")
data class Sak(
    @Id
    @Column(name = "ID_SAK", nullable = false)
    val id: BigDecimal,
    @Column(name = "REGION", columnDefinition = "CHAR")
    val region: String?,
    @Column(name = "S01_PERSONKEY")
    val personkey: BigDecimal?,
    @Column(name = "S05_SAKSBLOKK", columnDefinition = "CHAR")
    val saksblokk: String?,
    @Column(name = "S10_SAKSNR", columnDefinition = "CHAR")
    val saksnummer: String?,
    @Column(name = "S10_KAPITTELNR", columnDefinition = "CHAR")
    val kapittelNr: String?,
    @Column(name = "S10_VALG", columnDefinition = "CHAR")
    val valg: String?,
    @Column(name = "S10_TYPE", columnDefinition = "CHAR")
    val type: String?,
    @Column(name = "F_NR", columnDefinition = "CHAR")
    @Convert(converter = ReversedFoedselNrConverter::class)
    val fnr: Foedselsnummer?,
)
