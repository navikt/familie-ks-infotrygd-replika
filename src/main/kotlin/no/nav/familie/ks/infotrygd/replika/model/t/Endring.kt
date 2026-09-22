package no.nav.familie.ks.infotrygd.replika.model.t

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table
import java.io.Serializable

/**
 * t_endring har sammensatt primærnøkkel (vedtak_id, kode) - samme vedtak kan ha flere endringskoder.
 */
@Suppress("unused") // brukes av hibernate for å generere hvilke tabeller som brukes
@Entity
@Table(name = "T_ENDRING")
@IdClass(EndringId::class)
data class Endring(
    @Id
    @Column(name = "VEDTAK_ID")
    val vedtakId: Long,
    @Id
    @Column(name = "KODE")
    val kode: String,
)

data class EndringId(
    val vedtakId: Long = 0,
    val kode: String = "",
) : Serializable
