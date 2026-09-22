package no.nav.familie.ks.infotrygd.replika.model.t

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Suppress("unused") // brukes av hibernate for å generere hvilke tabeller som brukes
@Entity
@Table(name = "T_BESLUT")
data class Beslutning(
    @Id
    @Column(name = "BESLUTNING_ID")
    val id: Long,
    @Column(name = "VEDTAK_ID")
    val vedtakId: Long?,
    @Column(name = "GODKJENT1", columnDefinition = "CHAR")
    val godkjent1: String?,
    @Column(name = "GODKJENT2", columnDefinition = "CHAR")
    val godkjent2: String?,
)
