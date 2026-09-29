package no.nav.familie.ks.infotrygd.replika.normalisering

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

/**
 * Seq-kolonnene fra Infotrygd er lagret som komplementverdi.
 * - [YYYYMM]: 999999 - verdi gir YYYYMM, f.eks. 799791 -> 200208.
 * - [YYYYMMDD]: 99999999 - verdi gir YYYYMMDD, f.eks. 79979184 -> 20020815.
 */
enum class SeqDatoFormat(
    val komplementbase: Long,
    val lengde: Int,
    val mønster: String,
    val laveste: Long,
    val høyeste: Long,
) {
    YYYYMM(
        komplementbase = 999999,
        lengde = 6,
        mønster = "YYYYMM",
        laveste = 190001,
        høyeste = 299912,
    ),
    YYYYMMDD(
        komplementbase = 99999999,
        lengde = 8,
        mønster = "YYYYMMDD",
        laveste = 19000101,
        høyeste = 29991231,
    ),
}

enum class SeqDatoKolonne(
    val tabellnavn: String,
    val kildekolonne: String,
    val målkolonne: String,
    val format: SeqDatoFormat = SeqDatoFormat.YYYYMM,
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
    KS_STONAD_20_IVERFOM(
        tabellnavn = "ks_stonad_20",
        kildekolonne = "k20_iverfom_seq",
        målkolonne = "k20_iverfom_seq_dato",
    ),
    KS_STONAD_20_VIRKFOM(
        tabellnavn = "ks_stonad_20",
        kildekolonne = "k20_virkfom_seq",
        målkolonne = "k20_virkfom_seq_dato",
    ),
    KS_UTBETALING_30_START_UTBET_MND(
        tabellnavn = "ks_utbetaling_30",
        kildekolonne = "k30_start_utbet_mnd_seq",
        målkolonne = "k30_start_utbet_mnd_seq_dato",
    ),
    KS_UTBETALING_30_VFOM(
        tabellnavn = "ks_utbetaling_30",
        kildekolonne = "k30_vfom_seq",
        målkolonne = "k30_vfom_seq_dato",
    ),
    KS_UTBET_HIST_40_UTBET_DATO(
        tabellnavn = "ks_utbet_hist_40",
        kildekolonne = "k40_utbet_dato_seq",
        målkolonne = "k40_utbet_dato_seq_dato",
        format = SeqDatoFormat.YYYYMMDD,
    ),
    SA_HENDELSE_20_AKSJONSDATO(
        tabellnavn = "sa_hendelse_20",
        kildekolonne = "s20_aksjonsdato_seq",
        målkolonne = "s20_aksjonsdato_seq_dato",
        format = SeqDatoFormat.YYYYMMDD,
    ),
}

@Repository
class SeqDatoRepository(
    private val jdbcTemplate: JdbcTemplate,
) {
    fun konverter(seqDatoKolonne: SeqDatoKolonne): Int {
        val format = seqDatoKolonne.format
        val kilde = "trim(${seqDatoKolonne.kildekolonne}::text)"
        val komplement = "(${format.komplementbase} - $kilde::bigint)"

        // Postgres garanterer ikke evalueringsrekkefølgen på predikatene i where, så et rent
        // `where <regex> and <cast>` kan feile på rader med ugyldig innhold. Nøstet case
        // garanterer rekkefølgen og gir null i stedet for feil.
        val datotall =
            """
            case when $kilde ~ '^[0-9]{1,${format.lengde}}$'
                 then case when $komplement between ${format.laveste} and ${format.høyeste}
                           then $komplement
                      end
            end
            """.trimIndent()

        // to_date kaster «date/time field value out of range» på f.eks. 20240230, så datoen må
        // valideres før den parses. Ren heltallsaritmetikk kan ikke kaste, og gir null videre
        // for rader der datotall er null. to_date brukes derfor bare i set, som Postgres kun
        // evaluerer for rader som allerede har passert where.
        val datovalidering =
            when (format) {
                SeqDatoFormat.YYYYMM -> {
                    "k.datotall % 100 between 1 and 12"
                }

                SeqDatoFormat.YYYYMMDD -> {
                    val år = "k.datotall / 10000"
                    val skuddår = "(case when ($år % 4 = 0 and $år % 100 <> 0) or $år % 400 = 0 then 1 else 0 end)"
                    val dagerIMåned =
                        "(case k.datotall / 100 % 100 when 2 then 28 + $skuddår " +
                            "when 4 then 30 when 6 then 30 when 9 then 30 when 11 then 30 else 31 end)"
                    "k.datotall / 100 % 100 between 1 and 12 and k.datotall % 100 between 1 and $dagerIMåned"
                }
            }

        // Subqueryen beregner datotall én gang per rad. Uten den må hele case-uttrykket
        // gjentas i både set og where, som for YYYYMMDD blir sju kopier.
        return jdbcTemplate.update(
            """
            update ${seqDatoKolonne.tabellnavn} m
            set ${seqDatoKolonne.målkolonne} =
                to_date(lpad(k.datotall::text, ${format.lengde}, '0'), '${format.mønster}')::timestamp
            from (
                select ctid,
                       $datotall as datotall
                from ${seqDatoKolonne.tabellnavn}
                where ${seqDatoKolonne.kildekolonne} is not null
                  and ${seqDatoKolonne.målkolonne} is null
            ) k
            where m.ctid = k.ctid
              and k.datotall is not null
              and $datovalidering
            """,
        )
    }
}
