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
    private val primærnøkler =
        mapOf(
            "ks_barn_10" to "id_barn",
            "ks_stonad_20" to "id_stnd",
            "ks_utbetaling_30" to "id_utbet",
            "ks_utbet_hist_40" to "id_uhist",
            "sa_hendelse_20" to "id_hend",
        )

    @Autowired
    private lateinit var repository: SeqDatoRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun rydd() {
        primærnøkler.keys.forEach { jdbcTemplate.update("delete from $it") }
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

    @Test
    fun `konverterer seks-sifret seq i ks_stonad_20`() {
        settInn(
            "ks_stonad_20",
            id = 1,
            "k20_iverfom_seq" to "799791",
            "k20_virkfom_seq" to "799792",
        )

        assertThat(repository.konverter(SeqDatoKolonne.KS_STONAD_20_IVERFOM)).isEqualTo(1)
        assertThat(repository.konverter(SeqDatoKolonne.KS_STONAD_20_VIRKFOM)).isEqualTo(1)

        assertThat(hentDato("ks_stonad_20", id = 1, kolonne = "k20_iverfom_seq_dato"))
            .isEqualTo(LocalDateTime.of(2002, 8, 1, 0, 0, 0))
        assertThat(hentDato("ks_stonad_20", id = 1, kolonne = "k20_virkfom_seq_dato"))
            .isEqualTo(LocalDateTime.of(2002, 7, 1, 0, 0, 0))
    }

    @Test
    fun `konverterer seks-sifret seq i ks_utbetaling_30`() {
        settInn(
            "ks_utbetaling_30",
            id = 1,
            "k30_start_utbet_mnd_seq" to "799791",
            "k30_vfom_seq" to "799792",
        )

        assertThat(repository.konverter(SeqDatoKolonne.KS_UTBETALING_30_START_UTBET_MND)).isEqualTo(1)
        assertThat(repository.konverter(SeqDatoKolonne.KS_UTBETALING_30_VFOM)).isEqualTo(1)

        assertThat(hentDato("ks_utbetaling_30", id = 1, kolonne = "k30_start_utbet_mnd_seq_dato"))
            .isEqualTo(LocalDateTime.of(2002, 8, 1, 0, 0, 0))
        assertThat(hentDato("ks_utbetaling_30", id = 1, kolonne = "k30_vfom_seq_dato"))
            .isEqualTo(LocalDateTime.of(2002, 7, 1, 0, 0, 0))
    }

    @Test
    fun `konverterer aatte-sifret seq lagret som char i ks_utbet_hist_40`() {
        // 99999999 - 20020815 = 79979184
        settInn("ks_utbet_hist_40", id = 1, "k40_utbet_dato_seq" to "79979184")

        assertThat(repository.konverter(SeqDatoKolonne.KS_UTBET_HIST_40_UTBET_DATO)).isEqualTo(1)
        assertThat(hentDato("ks_utbet_hist_40", id = 1, kolonne = "k40_utbet_dato_seq_dato"))
            .isEqualTo(LocalDateTime.of(2002, 8, 15, 0, 0, 0))
    }

    @Test
    fun `konverterer aatte-sifret seq lagret som numeric i sa_hendelse_20`() {
        settInn("sa_hendelse_20", id = 1, "s20_aksjonsdato_seq" to 79979184L)

        assertThat(repository.konverter(SeqDatoKolonne.SA_HENDELSE_20_AKSJONSDATO)).isEqualTo(1)
        assertThat(hentDato("sa_hendelse_20", id = 1, kolonne = "s20_aksjonsdato_seq_dato"))
            .isEqualTo(LocalDateTime.of(2002, 8, 15, 0, 0, 0))
    }

    @Test
    fun `setter ikke dato for dag som ikke finnes i maaneden`() {
        // 99999999 - 20240230 = 79759769, og 30. februar finnes ikke
        settInn("sa_hendelse_20", id = 1, "s20_aksjonsdato_seq" to 79759769L)

        assertThat(repository.konverter(SeqDatoKolonne.SA_HENDELSE_20_AKSJONSDATO)).isZero()
        assertThat(hentDato("sa_hendelse_20", id = 1, kolonne = "s20_aksjonsdato_seq_dato")).isNull()
    }

    private fun settInn(
        tabell: String,
        id: Long,
        vararg verdier: Pair<String, Any?>,
    ) {
        val kolonner = listOf(primærnøkler.getValue(tabell)) + verdier.map { it.first }
        jdbcTemplate.update(
            "insert into $tabell (${kolonner.joinToString()}) values (${kolonner.joinToString { "?" }})",
            id,
            *verdier.map { it.second }.toTypedArray(),
        )
    }

    private fun hentDato(
        tabell: String,
        id: Long,
        kolonne: String,
    ): LocalDateTime? =
        jdbcTemplate.queryForObject(
            "select $kolonne from $tabell where ${primærnøkler.getValue(tabell)} = ?",
            LocalDateTime::class.java,
            id,
        )

    private fun settInn(
        id: Long,
        iverSeq: String? = null,
        vfomSeq: String? = null,
        tomSeq: String? = null,
        iverSeqDato: LocalDateTime? = null,
    ) = settInn(
        "ks_barn_10",
        id = id,
        "k10_ba_iver_seq" to iverSeq,
        "k10_ba_vfom_seq" to vfomSeq,
        "k10_ba_tom_seq" to tomSeq,
        "k10_ba_iver_seq_dato" to iverSeqDato,
    )

    private fun hentDato(
        id: Long,
        kolonne: String,
    ): LocalDateTime? = hentDato("ks_barn_10", id = id, kolonne = kolonne)
}
