package no.nav.familie.ks.infotrygd.replika.model.t

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Suppress("unused") // brukes av hibernate for å generere hvilke tabeller som brukes
@Entity
@Table(name = "T_BEREGNINGSTYPE")
data class Beregningstype(
    @Id
    @Column(name = "TYPE", columnDefinition = "CHAR")
    val type: String,
    @Column(name = "TEKST", columnDefinition = "CHAR")
    val tekst: String?,
)
