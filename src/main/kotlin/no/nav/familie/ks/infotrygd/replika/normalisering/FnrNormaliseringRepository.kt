package no.nav.familie.ks.infotrygd.replika.normalisering

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

enum class FnrKolonne(
    val tabellnavn: String,
    val kildekolonne: String,
    val målkolonne: String,
) {
    KS_BARN_10(
        tabellnavn = "ks_barn_10",
        kildekolonne = "k10_barn_fnr",
        målkolonne = "k10_barn_fnr_normalisert",
    ),
}

@Repository
class FnrNormaliseringRepository(
    private val jdbcTemplate: JdbcTemplate,
) {
    fun normaliser(fnrKolonne: FnrKolonne): Int =
        jdbcTemplate.update(
            """
            update ${fnrKolonne.tabellnavn}
            set ${fnrKolonne.målkolonne} =
                substring(lpad(${fnrKolonne.kildekolonne}::text, 11, '0'), 5, 2) ||
                substring(lpad(${fnrKolonne.kildekolonne}::text, 11, '0'), 3, 2) ||
                substring(lpad(${fnrKolonne.kildekolonne}::text, 11, '0'), 1, 2) ||
                substring(lpad(${fnrKolonne.kildekolonne}::text, 11, '0'), 7, 5)
            where ${fnrKolonne.kildekolonne} is not null
              and ${fnrKolonne.målkolonne} is null
              and ${fnrKolonne.kildekolonne}::text ~ '^[0-9]{8,11}$'
            """,
        )
}
