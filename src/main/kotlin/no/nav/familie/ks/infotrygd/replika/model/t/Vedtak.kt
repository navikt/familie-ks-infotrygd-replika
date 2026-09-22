package no.nav.familie.ks.infotrygd.replika.model.t

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate

@Suppress("unused") // brukes av hibernate for å generere hvilke tabeller som brukes
@Entity
@Table(name = "T_VEDTAK")
data class Vedtak(
    @Id
    @Column(name = "VEDTAK_ID")
    val id: Long,
    @Column(name = "PERSON_LOPENR")
    val personKey: Long,
    @Column(name = "STONAD_ID")
    val stonadId: Long?,
    @Column(name = "KODE_RUTINE")
    val kodeRutine: String?,
    @Column(name = "TYPE_SAK")
    val sakstype: String?,
    @Column(name = "KODE_RESULTAT")
    val kodeResultat: String?,
    @Column(name = "DATO_INNV_FOM")
    val datoInnvFom: LocalDate?,
    @Column(name = "DATO_INNV_TOM")
    val datoInnvTom: LocalDate?,
)
