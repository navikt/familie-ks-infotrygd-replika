package no.nav.familie.ks.infotrygd.replika.normalisering

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

/**
 * Seq-kolonnene fra Infotrygd er lagret som komplementverdi.
 * - [YYYYMM]: 999999 - verdi gir YYYYMM, f.eks. 799791 -> 200208.
 * - [YYYYMMDD]: 99999999 - verdi gir YYYYMMDD, f.eks. 79979184 -> 20020815.
 */
enum class SeqDatoFormat {
    YYYYMM,
    YYYYMMDD,
}

enum class SeqDatoKolonne(
    val tabellnavn: String,
    val primærnøkkel: String,
    val kildekolonne: String,
    val målkolonne: String,
    val format: SeqDatoFormat = SeqDatoFormat.YYYYMM,
) {
    KS_BARN_10_IVER(
        tabellnavn = "ks_barn_10",
        primærnøkkel = "id_barn",
        kildekolonne = "k10_ba_iver_seq",
        målkolonne = "k10_ba_iver_seq_dato",
    ),
    KS_BARN_10_VFOM(
        tabellnavn = "ks_barn_10",
        primærnøkkel = "id_barn",
        kildekolonne = "k10_ba_vfom_seq",
        målkolonne = "k10_ba_vfom_seq_dato",
    ),
    KS_BARN_10_TOM(
        tabellnavn = "ks_barn_10",
        primærnøkkel = "id_barn",
        kildekolonne = "k10_ba_tom_seq",
        målkolonne = "k10_ba_tom_seq_dato",
    ),
    KS_STONAD_20_IVERFOM(
        tabellnavn = "ks_stonad_20",
        primærnøkkel = "id_stnd",
        kildekolonne = "k20_iverfom_seq",
        målkolonne = "k20_iverfom_seq_dato",
    ),
    KS_STONAD_20_VIRKFOM(
        tabellnavn = "ks_stonad_20",
        primærnøkkel = "id_stnd",
        kildekolonne = "k20_virkfom_seq",
        målkolonne = "k20_virkfom_seq_dato",
    ),
    KS_UTBETALING_30_START_UTBET_MND(
        tabellnavn = "ks_utbetaling_30",
        primærnøkkel = "id_utbet",
        kildekolonne = "k30_start_utbet_mnd_seq",
        målkolonne = "k30_start_utbet_mnd_seq_dato",
    ),
    KS_UTBETALING_30_VFOM(
        tabellnavn = "ks_utbetaling_30",
        primærnøkkel = "id_utbet",
        kildekolonne = "k30_vfom_seq",
        målkolonne = "k30_vfom_seq_dato",
    ),
    KS_UTBET_HIST_40_UTBET_DATO(
        tabellnavn = "ks_utbet_hist_40",
        primærnøkkel = "id_uhist",
        kildekolonne = "k40_utbet_dato_seq",
        målkolonne = "k40_utbet_dato_seq_dato",
        format = SeqDatoFormat.YYYYMMDD,
    ),
    SA_HENDELSE_20_AKSJONSDATO(
        tabellnavn = "sa_hendelse_20",
        primærnøkkel = "id_hend",
        kildekolonne = "s20_aksjonsdato_seq",
        målkolonne = "s20_aksjonsdato_seq_dato",
        format = SeqDatoFormat.YYYYMMDD,
    ),
}

@Repository
class SeqDatoRepository(
    private val jdbcTemplate: JdbcTemplate,
) {
    fun konverter(seqDatoKolonne: SeqDatoKolonne): Int =
        when (seqDatoKolonne.format) {
            SeqDatoFormat.YYYYMM -> konverterÅrMåned(seqDatoKolonne)
            SeqDatoFormat.YYYYMMDD -> konverterÅrMånedDag(seqDatoKolonne)
        }

    private fun konverterÅrMåned(kolonne: SeqDatoKolonne): Int {
        val kilde = "trim(${kolonne.kildekolonne}::text)"

        return jdbcTemplate.update(
            """
            update ${kolonne.tabellnavn} mål
            set ${kolonne.målkolonne} = to_date(k.datotall::text, 'YYYYMM')::timestamp
            from (
                select ${kolonne.primærnøkkel},
                       case when $kilde ~ '^[0-9]{1,6}$'
                            then 999999 - $kilde::bigint
                       end as datotall
                from ${kolonne.tabellnavn}
                where ${kolonne.kildekolonne} is not null
                  and ${kolonne.målkolonne} is null
            ) k
            where mål.${kolonne.primærnøkkel} = k.${kolonne.primærnøkkel}
              and k.datotall between 190001 and 299912
              and k.datotall % 100 between 1 and 12
            """,
        )
    }

    private fun konverterÅrMånedDag(kolonne: SeqDatoKolonne): Int {
        val kilde = "trim(${kolonne.kildekolonne}::text)"
        val måned = "k.datotall / 100 % 100"
        val dag = "k.datotall % 100"

        return jdbcTemplate.update(
            """
            update ${kolonne.tabellnavn} mål
            set ${kolonne.målkolonne} = to_date(k.datotall::text, 'YYYYMMDD')::timestamp
            from (
                select ${kolonne.primærnøkkel},
                       case when $kilde ~ '^[0-9]{1,8}$'
                            then 99999999 - $kilde::bigint
                       end as datotall
                from ${kolonne.tabellnavn}
                where ${kolonne.kildekolonne} is not null
                  and ${kolonne.målkolonne} is null
            ) k
            where mål.${kolonne.primærnøkkel} = k.${kolonne.primærnøkkel}
              and k.datotall between 19000101 and 29991231
              and $måned between 1 and 12
              and $dag between 1 and 31
            """,
        )
    }
}
