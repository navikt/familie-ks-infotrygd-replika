package no.nav.familie.ks.infotrygd.replika.model.sa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal

@Suppress("unused") // brukes av hibernate for å generere hvilke tabeller som brukes
@Entity
@Table(name = "SA_SAKSBLOKK_05")
data class Saksblokk(
    @Id
    @Column(name = "ID_SBLK", nullable = false)
    val id: BigDecimal,
    @Column(name = "REGION", columnDefinition = "CHAR")
    val region: String?,
    @Column(name = "S01_PERSONKEY")
    val personkey: BigDecimal?,
    @Column(name = "S05_SAKSBLOKK", columnDefinition = "CHAR")
    val saksblokk: String?,
)
