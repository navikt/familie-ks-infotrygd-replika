package no.nav.familie.ks.infotrygd.replika.model.ks

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal

@Suppress("unused") // brukes av hibernate for å generere hvilke tabeller som brukes
@Entity
@Table(name = "KS_UTBET_HIST_40")
data class UtbetalingHistorikk(
    @Id
    @Column(name = "ID_UHIST", nullable = false)
    val id: BigDecimal,
    @Column(name = "REGION", columnDefinition = "CHAR")
    val region: String?,
    @Column(name = "K01_PERSONKEY")
    val personkey: BigDecimal?,
    @Column(name = "K40_UTBET_DATO_SEQ", columnDefinition = "CHAR")
    val utbetDatoSeq: String?,
    @Column(name = "K40_NETTO_UTBET")
    val nettoUtbet: BigDecimal?,
)
