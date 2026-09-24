package no.nav.familie.ks.infotrygd.replika.normalisering

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

@JdbcTest
@Import(SeqDatoRepository::class)
@ActiveProfiles("test")
class SeqDatoRepositoryTest {
    @Autowired
    private lateinit var repository: SeqDatoRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun rydd() {
        jdbcTemplate.update("delete from ks_barn_10")
    }

    @Test
    fun `konverterer komplementverdi til timestamp`() {
        settInn(id = 1, iverSeq = "799791")

        val antallOppdatert = repository.konverter(SeqDatoKolonne.KS_BARN_10_IVER)

        assertThat(antallOppdatert).isEqualTo(1)
        assertThat(hentDato(1, "k10_ba_iver_seq_dato")).isEqualTo(LocalDateTime.of(2002, 8, 1, 0, 0, 0))
    }

    @Test
    fun `konverterer alle tre kolonnene`() {
        settInn(id = 1, iverSeq = "799791", vfomSeq = "799792", tomSeq = "799787")

        SeqDatoKolonne.entries.forEach { repository.konverter(it) }

        assertThat(hentDato(1, "k10_ba_iver_seq_dato")).isEqualTo(LocalDateTime.of(2002, 8, 1, 0, 0, 0))
        assertThat(hentDato(1, "k10_ba_vfom_seq_dato")).isEqualTo(LocalDateTime.of(2002, 7, 1, 0, 0, 0))
        assertThat(hentDato(1, "k10_ba_tom_seq_dato")).isEqualTo(LocalDateTime.of(2002, 12, 1, 0, 0, 0))
    }

    @Test
    fun `setter ikke dato for ugyldig maaned`() {
        settInn(id = 1, iverSeq = "790086")

        val antallOppdatert = repository.konverter(SeqDatoKolonne.KS_BARN_10_IVER)

        assertThat(antallOppdatert).isZero()
        assertThat(hentDato(1, "k10_ba_iver_seq_dato")).isNull()
    }

    @Test
    fun `setter ikke dato for aar utenfor gyldig omraade`() {
        settInn(id = 1, iverSeq = "999999")

        val antallOppdatert = repository.konverter(SeqDatoKolonne.KS_BARN_10_IVER)

        assertThat(antallOppdatert).isZero()
        assertThat(hentDato(1, "k10_ba_iver_seq_dato")).isNull()
    }

    @Test
    fun `setter ikke dato for ikke-numerisk verdi`() {
        settInn(id = 1, iverSeq = "      ")

        val antallOppdatert = repository.konverter(SeqDatoKolonne.KS_BARN_10_IVER)

        assertThat(antallOppdatert).isZero()
        assertThat(hentDato(1, "k10_ba_iver_seq_dato")).isNull()
    }

    @Test
    fun `overskriver ikke allerede konvertert verdi`() {
        settInn(id = 1, iverSeq = "799791", iverSeqDato = LocalDateTime.of(1999, 1, 1, 0, 0, 0))

        val antallOppdatert = repository.konverter(SeqDatoKolonne.KS_BARN_10_IVER)

        assertThat(antallOppdatert).isZero()
        assertThat(hentDato(1, "k10_ba_iver_seq_dato")).isEqualTo(LocalDateTime.of(1999, 1, 1, 0, 0, 0))
    }

    private fun settInn(
        id: Long,
        iverSeq: String? = null,
        vfomSeq: String? = null,
        tomSeq: String? = null,
        iverSeqDato: LocalDateTime? = null,
    ) {
        jdbcTemplate.update(
            """
            insert into ks_barn_10 (id_barn, k10_ba_iver_seq, k10_ba_vfom_seq, k10_ba_tom_seq, k10_ba_iver_seq_dato)
            values (?, ?, ?, ?, ?)
            """,
            id,
            iverSeq,
            vfomSeq,
            tomSeq,
            iverSeqDato,
        )
    }

    private fun hentDato(
        id: Long,
        kolonne: String,
    ): LocalDateTime? =
        jdbcTemplate.queryForObject(
            "select $kolonne from ks_barn_10 where id_barn = ?",
            LocalDateTime::class.java,
            id,
        )
}
