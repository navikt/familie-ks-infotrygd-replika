-- Avledede datokolonner gjenopprettes uten tabellomskriving ved typeendring.
-- Verdiene må beregnes på nytt fra de uendrede originalkolonnene etter migreringen.
alter table ks_barn_10
    drop column k10_ba_iver_seq_dato,
    drop column k10_ba_vfom_seq_dato,
    drop column k10_ba_tom_seq_dato;

alter table ks_stonad_20
    drop column k20_iverfom_seq_dato,
    drop column k20_virkfom_seq_dato;

alter table ks_utbet_hist_40
    drop column k40_utbet_dato_seq_dato;

alter table ks_utbetaling_30
    drop column k30_start_utbet_mnd_seq_dato,
    drop column k30_vfom_seq_dato;

alter table sa_hendelse_20
    drop column s20_aksjonsdato_seq_dato;

alter table ks_barn_10
    add column k10_ba_iver_seq_dato date,
    add column k10_ba_vfom_seq_dato date,
    add column k10_ba_tom_seq_dato date;

alter table ks_stonad_20
    add column k20_iverfom_seq_dato date,
    add column k20_virkfom_seq_dato date;

alter table ks_utbet_hist_40
    add column k40_utbet_dato_seq_dato date;

alter table ks_utbetaling_30
    add column k30_start_utbet_mnd_seq_dato date,
    add column k30_vfom_seq_dato date;

alter table sa_hendelse_20
    add column s20_aksjonsdato_seq_dato date;
