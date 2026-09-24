package no.nav.familie.ks.infotrygd.replika.normalisering

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class FnrNormaliseringRepository(
    private val jdbcTemplate: JdbcTemplate,
) {
    fun normaliserBarn(): Int =
        jdbcTemplate.update(
            """
            update ks_barn_10
            set k10_barn_fnr_normalisert =
                substring(lpad(k10_barn_fnr::text, 11, '0'), 5, 2) ||
                substring(lpad(k10_barn_fnr::text, 11, '0'), 3, 2) ||
                substring(lpad(k10_barn_fnr::text, 11, '0'), 1, 2) ||
                substring(lpad(k10_barn_fnr::text, 11, '0'), 7, 5)
            where k10_barn_fnr is not null
              and k10_barn_fnr_normalisert is null
              and k10_barn_fnr::text ~ '^[0-9]{10,11}$'
            """,
        )
}
