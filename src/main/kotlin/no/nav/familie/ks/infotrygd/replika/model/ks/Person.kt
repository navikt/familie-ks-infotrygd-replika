package no.nav.familie.ks.infotrygd.replika.model.ks

import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import no.nav.commons.foedselsnummer.Foedselsnummer
import no.nav.familie.ks.infotrygd.replika.model.converters.ReversedFoedselNrConverter
import java.math.BigDecimal

/** ks_person_01. Eksplisitt entitetsnavn fordi sa_person_01 også mapper til en klasse som heter Person. */
@Suppress("unused") // brukes av hibernate for å generere hvilke tabeller som brukes
@Entity(name = "KsPerson")
@Table(name = "KS_PERSON_01")
data class Person(
    @Id
    @Column(name = "ID_PERS", nullable = false)
    val id: BigDecimal,
    @Column(name = "REGION", columnDefinition = "CHAR")
    val region: String?,
    @Column(name = "K01_PERSONKEY")
    val personkey: BigDecimal?,
    @Column(name = "F_NR", columnDefinition = "CHAR")
    @Convert(converter = ReversedFoedselNrConverter::class)
    val fnr: Foedselsnummer?,
)
