package no.nav.familie.ks.infotrygd.replika.model.sa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal

@Suppress("unused") // brukes av hibernate for å generere hvilke tabeller som brukes
@Entity
@Table(name = "SA_STATUS_15")
data class Status(
    @Id
    @Column(name = "ID_STATUS", nullable = false)
    val id: BigDecimal,
    @Column(name = "REGION", columnDefinition = "CHAR")
    val region: String?,
    @Column(name = "S01_PERSONKEY")
    val personkey: BigDecimal?,
    @Column(name = "S05_SAKSBLOKK", columnDefinition = "CHAR")
    val saksblokk: String?,
    @Column(name = "S10_SAKSNR", columnDefinition = "CHAR")
    val saksnummer: String?,
    @Column(name = "S15_LOPENR", columnDefinition = "CHAR")
    val lopenr: String?,
    @Column(name = "S15_STATUS", columnDefinition = "CHAR")
    val status: String?,
)
