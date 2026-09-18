package no.nav.familie.ks.infotrygd.replika.exodus

/**
 * Alle tabellene som skal repliseres fra familie-ks-exodus, med den naturlige nøkkelen fra Oracle
 * som brukes til å upserte rader i Postgres. Kolonnenavn er alltid lowercase siden det er slik
 * Postgres folder unquoted identifiers (se V1__init_schema.sql).
 *
 * t_lopenr_fnr, t_stonad, t_vedtak og t_ef har egen primærnøkkel i skjemaet fra før. t_endring,
 * t_rolle, t_beregn_grl og sa_sak_10 har fått en tilsvarende sammensatt/enkel nøkkel lagt til i
 * V1__init_schema.sql for å støtte upsert her.
 */
enum class ExodusTabell(
    val tabellNavn: String,
    val primærnøkkel: List<String>,
) {
    KS_UTBETALING_30("ks_utbetaling_30", listOf("id_utbet")),
    KS_STONAD_20("ks_stonad_20", listOf("id_stnd")),
    KS_BARN_10("ks_barn_10", listOf("id_barn")),
    ;

    companion object {
        fun fraTabellNavn(navn: String): ExodusTabell =
            entries.find { it.tabellNavn.equals(navn, ignoreCase = true) }
                ?: throw IllegalArgumentException("Ukjent exodus-tabell: $navn")
    }
}
