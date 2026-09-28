alter table ks_stonad_20
    add column k20_iverfom_seq_dato timestamp(3),
    add column k20_virkfom_seq_dato timestamp(3);

alter table ks_utbet_hist_40
    add column k40_utbet_dato_seq_dato timestamp(3);

alter table ks_utbetaling_30
    add column k30_start_utbet_mnd_seq_dato timestamp(3),
    add column k30_vfom_seq_dato            timestamp(3);

alter table sa_hendelse_20
add column s20_aksjonsdato_seq_dato timestamp(3);
