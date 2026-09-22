package no.nav.familie.ks.infotrygd.replika.exodus

/**
 * Alle tabellene som skal repliseres fra familie-ks-exodus, med den naturlige nøkkelen fra Oracle
 * som brukes til å upserte rader i Postgres. Kolonnenavn er alltid lowercase siden det er slik
 * Postgres folder unquoted identifiers.
 */
enum class ExodusTabell(
    val tabellNavn: String,
    val primærnøkkel: List<String>,
) {
    KS_UTBETALING_30("ks_utbetaling_30", listOf("id_utbet")),
    KS_STONAD_20("ks_stonad_20", listOf("id_stnd")),
    KS_BARN_10("ks_barn_10", listOf("id_barn")),
    T_LOPENR_FNR("t_lopenr_fnr", listOf("person_lopenr")),
    T_STONAD("t_stonad", listOf("stonad_id")),
    T_VEDTAK("t_vedtak", listOf("vedtak_id")),
    T_ENDRING("t_endring", listOf("vedtak_id", "kode")),
    T_DELYTELSE("t_delytelse", listOf("vedtak_id", "type_delytelse", "tidspunkt_reg")),
    T_BESLUT("t_beslut", listOf("beslutning_id")),
    T_BEREGNINGSTYPE("t_beregningstype", listOf("type")),
    SA_SAK_10("sa_sak_10", listOf("id_sak")),
    SA_STATUS_15("sa_status_15", listOf("id_status")),
    SA_SAKSBLOKK_05("sa_saksblokk_05", listOf("id_sblk")),
    SA_PERSON_01("sa_person_01", listOf("id_pers")),
    SA_HENDELSE_20("sa_hendelse_20", listOf("id_hend")),
    KS_PERSON_01("ks_person_01", listOf("id_pers")),
    KS_UTBET_HIST_40("ks_utbet_hist_40", listOf("id_uhist")),
    ;

    companion object {
        fun fraTabellNavn(navn: String): ExodusTabell =
            entries.find { it.tabellNavn.equals(navn, ignoreCase = true) }
                ?: throw IllegalArgumentException("Ukjent exodus-tabell: $navn")
    }
}
