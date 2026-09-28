package no.nav.familie.ks.infotrygd.replika.normalisering

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

enum class SeqDatoKolonne(
    val tabellnavn: String,
    val kildekolonne: String,
    val målkolonne: String,
) {
    KS_BARN_10_IVER(
        tabellnavn = "ks_barn_10",
        kildekolonne = "k10_ba_iver_seq",
        målkolonne = "k10_ba_iver_seq_dato",
    ),
    KS_BARN_10_VFOM(
        tabellnavn = "ks_barn_10",
        kildekolonne = "k10_ba_vfom_seq",
        målkolonne = "k10_ba_vfom_seq_dato",
    ),
    KS_BARN_10_TOM(
        tabellnavn = "ks_barn_10",
        kildekolonne = "k10_ba_tom_seq",
        målkolonne = "k10_ba_tom_seq_dato",
    ),
}

/**
 * Seq-kolonnene fra Infotrygd er lagret som komplementverdi: 999999 - verdi gir YYYYMM.
 * Eksempel: 799791 -> 200208 -> 2002-08-01T00:00:00.
 */
@Repository
class SeqDatoRepository(
    private val jdbcTemplate: JdbcTemplate,
) {
    fun konverter(seqDatoKolonne: SeqDatoKolonne): Int {
        val yyyymm = "lpad((999999 - trim(${seqDatoKolonne.kildekolonne})::int)::text, 6, '0')"
        return jdbcTemplate.update(
            """
            update ${seqDatoKolonne.tabellnavn}
            set ${seqDatoKolonne.målkolonne} = to_date($yyyymm, 'YYYYMM')::timestamp
            where ${seqDatoKolonne.kildekolonne} is not null
              and ${seqDatoKolonne.målkolonne} is null
              and trim(${seqDatoKolonne.kildekolonne}) ~ '^[0-9]{1,6}$'
              and (999999 - trim(${seqDatoKolonne.kildekolonne})::int) between 190001 and 299912
              and substring($yyyymm, 5, 2) between '01' and '12'
            """,
        )
    }
}
