package no.nav.familie.ks.infotrygd.replika.normalisering

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles

@JdbcTest
@Import(FnrNormaliseringRepository::class)
@ActiveProfiles("test")
class FnrNormaliseringRepositoryTest {
    @Autowired
    private lateinit var repository: FnrNormaliseringRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun rydd() {
        jdbcTemplate.update("delete from ks_barn_10")
    }

    @Test
    fun `normaliserer eksisterende ellevesifret verdi`() {
        settInn(id = 1, infotrygdFnr = "01020312345")

        val antallOppdatert = repository.normaliserBarn()

        assertThat(antallOppdatert).isEqualTo(1)
        assertThat(hentNormalisert(1)).isEqualTo("03020112345")
    }

    @Test
    fun `gjenoppretter ledende null for tisifret verdi`() {
        settInn(id = 1, infotrygdFnr = "1020312345")

        repository.normaliserBarn()

        assertThat(hentNormalisert(1)).isEqualTo("03020112345")
    }

    @Test
    fun `overskriver ikke allerede normalisert verdi`() {
        settInn(id = 1, infotrygdFnr = "01020312345", normalisertFnr = "eksisterer")

        val antallOppdatert = repository.normaliserBarn()

        assertThat(antallOppdatert).isZero()
        assertThat(hentNormalisert(1)).isEqualTo("eksisterer")
    }

    private fun settInn(
        id: Long,
        infotrygdFnr: String,
        normalisertFnr: String? = null,
    ) {
        jdbcTemplate.update(
            """
            insert into ks_barn_10 (id_barn, k10_barn_fnr, k10_barn_fnr_normalisert)
            values (?, cast(? as numeric), ?)
            """,
            id,
            infotrygdFnr,
            normalisertFnr,
        )
    }

    private fun hentNormalisert(id: Long): String? =
        jdbcTemplate.queryForObject(
            "select k10_barn_fnr_normalisert from ks_barn_10 where id_barn = ?",
            String::class.java,
            id,
        )
}
