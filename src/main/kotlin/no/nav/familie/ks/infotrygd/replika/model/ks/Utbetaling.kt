package no.nav.infotrygd.kontantstotte.model.ks

import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.YearMonth

@Entity
@Table(name = "KS_UTBETALING_30")
data class Utbetaling(
    @Id
    @Column(name = "ID_UTBET", nullable = false)
    val id: BigDecimal,
    @Column(name = "REGION", columnDefinition = "CHAR")
    val region: String,
    @Column(name = "K01_PERSONKEY")
    val personkey: BigDecimal,
    @Column(name = "K30_START_UTBET_MND_SEQ")
    val startUtbetaltMndSeq: String,
    @Column(name = "K30_VFOM_SEQ")
    val virkfomSeq: String,
    @Column(name = "K30_UTBET_FOM")
    @Convert(converter = _root_ide_package_.no.nav.infotrygd.kontantstotte.model.converters.NavCharYearMonthConverter::class)
    val fom: YearMonth?,
    @Column(name = "K30_UTBET_TOM")
    @Convert(converter = _root_ide_package_.no.nav.infotrygd.kontantstotte.model.converters.NavCharYearMonthConverter::class)
    val tom: YearMonth?,
    @Column(name = "K30_BELOP", columnDefinition = "DECIMAL")
    val belop: Int,
    @Column(name = "K30_UTBET_TYPE", columnDefinition = "CHAR")
    val type: String,
)
