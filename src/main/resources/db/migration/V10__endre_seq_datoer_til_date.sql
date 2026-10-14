alter table ks_barn_10
    alter column k10_ba_iver_seq_dato type date using k10_ba_iver_seq_dato::date,
    alter column k10_ba_vfom_seq_dato type date using k10_ba_vfom_seq_dato::date,
    alter column k10_ba_tom_seq_dato type date using k10_ba_tom_seq_dato::date;

alter table ks_stonad_20
    alter column k20_iverfom_seq_dato type date using k20_iverfom_seq_dato::date,
    alter column k20_virkfom_seq_dato type date using k20_virkfom_seq_dato::date;

alter table ks_utbet_hist_40
    alter column k40_utbet_dato_seq_dato type date using k40_utbet_dato_seq_dato::date;

alter table ks_utbetaling_30
    alter column k30_start_utbet_mnd_seq_dato type date using k30_start_utbet_mnd_seq_dato::date,
    alter column k30_vfom_seq_dato type date using k30_vfom_seq_dato::date;

alter table sa_hendelse_20
    alter column s20_aksjonsdato_seq_dato type date using s20_aksjonsdato_seq_dato::date;
