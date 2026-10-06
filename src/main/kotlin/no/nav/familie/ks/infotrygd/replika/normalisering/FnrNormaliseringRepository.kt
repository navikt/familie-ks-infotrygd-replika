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
    KS_PERSON_01(
        tabellnavn = "ks_person_01",
        kildekolonne = "F_NR",
        målkolonne = "fnr_normalisert",
    ),
    KS_STONAD_20(
        tabellnavn = "ks_stonad_20",
        kildekolonne = "F_NR",
        målkolonne = "fnr_normalisert",
    ),
    KS_UTBET_HIST_40(
        tabellnavn = "ks_utbet_hist_40",
        kildekolonne = "F_NR",
        målkolonne = "fnr_normalisert",
    ),
    KS_UTBETALING_30(
        tabellnavn = "ks_utbetaling_30",
        kildekolonne = "F_NR",
        målkolonne = "fnr_normalisert",
    ),
    SA_HENDELSE_20(
        tabellnavn = "sa_hendelse_20",
        kildekolonne = "F_NR",
        målkolonne = "fnr_normalisert",
    ),
    SA_PERSON_01(
        tabellnavn = "sa_person_01",
        kildekolonne = "F_NR",
        målkolonne = "fnr_normalisert",
    ),
    SA_SAK_10(
        tabellnavn = "sa_sak_10",
        kildekolonne = "F_NR",
        målkolonne = "fnr_normalisert",
    ),
    SA_SAKSBLOKK_05(
        tabellnavn = "sa_saksblokk_05",
        kildekolonne = "F_NR",
        målkolonne = "fnr_normalisert",
    ),
    SA_STATUS_15(
        tabellnavn = "sa_status_15",
        kildekolonne = "F_NR",
        målkolonne = "fnr_normalisert",
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
