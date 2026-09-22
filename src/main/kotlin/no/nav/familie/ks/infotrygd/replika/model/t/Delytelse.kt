package no.nav.familie.ks.infotrygd.replika.model.t

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * t_delytelse har sammensatt primærnøkkel (vedtak_id, type_delytelse, tidspunkt_reg), siden det
 * finnes historikk-rader for samme vedtak/delytelse med ulik tidspunkt_reg.
 */
@Suppress("unused") // brukes av hibernate for å generere hvilke tabeller som brukes
@Entity
@Table(name = "T_DELYTELSE")
@IdClass(DelytelseId::class)
data class Delytelse(
    @Id
    @Column(name = "VEDTAK_ID")
    val vedtakId: Long,
    @Id
    @Column(name = "TYPE_DELYTELSE", columnDefinition = "CHAR")
    val typeDelytelse: String,
    @Id
    @Column(name = "TIDSPUNKT_REG")
    val tidspunktReg: LocalDateTime,
    @Column(name = "FOM")
    val fom: LocalDate?,
    @Column(name = "TOM")
    val tom: LocalDate?,
    @Column(name = "BELOP")
    val belop: BigDecimal?,
)

data class DelytelseId(
    val vedtakId: Long = 0,
    val typeDelytelse: String = "",
    val tidspunktReg: LocalDateTime = LocalDateTime.MIN,
) : Serializable
