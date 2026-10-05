alter table ks_person_01
    add column fnr_normalisert varchar(11);
alter table ks_stonad_20
    add column fnr_normalisert varchar(11);
alter table ks_utbet_hist_40
    add column fnr_normalisert varchar(11);
alter table ks_utbetaling_30
    add column fnr_normalisert varchar(11);
alter table sa_hendelse_20
    add column fnr_normalisert varchar(11);
alter table sa_person_01
    add column fnr_normalisert varchar(11);
alter table sa_sak_10
    add column fnr_normalisert varchar(11);
alter table sa_saksblokk_05
    add column fnr_normalisert varchar(11);
alter table sa_status_15
    add column fnr_normalisert varchar(11);