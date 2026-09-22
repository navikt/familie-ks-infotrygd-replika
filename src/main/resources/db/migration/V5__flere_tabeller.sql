-- Utvidelse av replika-skjemaet med t_*- og sa_*-tabellene fra Infotrygd, samt de resterende
-- ks_*-tabellene. Samme konvensjoner som i V1__init.sql: Oracle NUMBER -> numeric,
-- CHAR/VARCHAR2 -> char/varchar, DATE/TIMESTAMP beholdt, alle identifikatorer i lowercase.

create table t_lopenr_fnr
(
    person_lopenr numeric   not null primary key,
    personnr      char(11)  not null,
    opprettet     timestamp not null default current_timestamp,
    oppdatert     timestamp          default current_timestamp,
    db_splitt     char(2)            default '  '
);

create table t_stonad
(
    stonad_id         numeric   not null primary key,
    person_lopenr     numeric,
    kode_rutine       char(2),
    dato_start        date,
    kode_opphor       char(2),
    dato_opphor       date,
    oppdrag_id        numeric,
    tidspunkt_opphort timestamp,
    tidspunkt_reg     timestamp,
    brukerid          char(8),
    opprettet         timestamp not null default current_timestamp,
    oppdatert         timestamp          default current_timestamp,
    db_splitt         char(2)            default '  '
);


create table t_vedtak
(
    vedtak_id           numeric   not null primary key,
    person_lopenr       numeric   not null,
    kode_rutine         char(2),
    dato_start          date,
    tknr                char(4),
    saksblokk           char(1),
    saksnr              numeric,
    type_sak            char(2),
    kode_resultat       char(2),
    dato_innv_fom       date,
    dato_innv_tom       date,
    dato_mottatt_sak    date,
    kode_vedtaksnivaa   char(3),
    type_beregning      char(3),
    tknr_beh            char(4),
    tidspunkt_reg       timestamp,
    brukerid            char(8),
    nokkel_dl1          char(30),
    alternativ_mottaker numeric(11),
    stonad_id           numeric,
    kidnr               varchar(25),
    faktnr              varchar(33),
    opprettet           timestamp not null default current_timestamp,
    oppdatert           timestamp          default current_timestamp,
    db_splitt           char(2)            default '  '
);


create table t_endring
(
    vedtak_id numeric    not null,
    kode      varchar(2) not null,
    opprettet timestamp  not null default current_timestamp,
    oppdatert timestamp           default current_timestamp,
    db_splitt char(2)             default '  ',
    primary key (vedtak_id, kode)
);

create table t_delytelse
(
    vedtak_id       numeric,
    type_delytelse  char(2),
    tidspunkt_reg   timestamp(6),
    fom             date,
    tom             date,
    belop           numeric(11, 2),
    oppgjorsordning char(1),
    mottaker_lopenr numeric(38),
    brukerid        char(8),
    type_sats       char(1),
    type_utbetaling char(1),
    linje_id        numeric(38),
    opprettet       timestamp(6) not null default current_timestamp,
    oppdatert       timestamp(6)          default current_timestamp,
    db_splitt       char(2)               default '  ',
    primary key (vedtak_id, type_delytelse, tidspunkt_reg)
);

create table t_beslut
(
    beslutning_id  numeric(38) primary key,
    vedtak_id      numeric,
    saksbehandler1 char(8),
    godkjent1      char(1),
    enhet1         char(4),
    tidspunkt_reg1 timestamp(6),
    saksbehandler2 char(8),
    godkjent2      char(1),
    enhet2         char(4),
    tidspunkt_reg2 timestamp(6),
    sendt_til_os   timestamp(6),
    mottatt_fra_os timestamp(6),
    godkjent_av_os char(1),
    opprettet      timestamp(6) not null default current_timestamp,
    oppdatert      timestamp(6)          default current_timestamp,
    db_splitt      char(2)               default '  '
);

create table t_beregningstype
(
    type      char(3) primary key,
    tekst     char(30),
    opprettet timestamp(6) not null default current_timestamp,
    oppdatert timestamp(6)          default current_timestamp,
    db_splitt char(2)               default '99'
);

create table sa_sak_10
(
    s01_personkey          numeric(15),
    s05_saksblokk          char(1),
    s10_saksnr             char(2),
    s10_reg_dato           numeric(8),
    s10_mottattdato        numeric(8),
    s10_kapittelnr         char(2),
    s10_valg               char(2),
    s10_undervalg          char(2),
    s10_dublett_feil       char(1),
    s10_type               char(2),
    s10_innstilling        char(2),
    s10_resultat           char(2),
    s10_nivaa              char(3),
    s10_innstilldato       numeric(8),
    s10_vedtaksdato        numeric(8),
    s10_iverksattdato      numeric(8),
    s10_grunnbl_dato       numeric(8),
    s10_aarsakskode        char(2),
    s10_tellepunkt         char(3),
    s10_telletype          char(1),
    s10_telledato          numeric(8),
    s10_eval_kode          char(4),
    s10_eval_tir           char(1),
    s10_fremlegg           char(3),
    s10_innstilling2       char(2),
    s10_innstilldato2      numeric(8),
    s10_annen_instans      char(1),
    s10_behen_type         char(3),
    s10_behen_enhet        char(4),
    s10_reg_av_type        char(3),
    s10_reg_av_enhet       char(4),
    s10_diff_framlegg      char(3),
    s10_innstillt_av_type  char(3),
    s10_innstillt_av_enhet char(4),
    s10_vedtatt_av_type    char(3),
    s10_vedtatt_av_enhet   char(4),
    s10_prio_tab           char(6),
    s10_aoe                char(3),
    s10_es_system          char(1),
    s10_es_gsak_oppdragsid numeric(10),
    s10_knyttet_til_sak    char(2),
    s10_vedtakstype        char(1),
    s10_reell_enhet        char(4),
    s10_mod_endret         char(1),
    tk_nr                  char(4),
    f_nr                   char(11),
    opprettet              timestamp(6) not null default current_timestamp,
    endret_i_kilde         timestamp(6)          default current_timestamp,
    kilde_is               varchar(12)           default ' ',
    region                 char(1)               default ' ',
    id_sak                 numeric primary key,
    oppdatert              timestamp(6)          default current_timestamp,
    db_splitt              char(2)               default '99'
);

create table sa_status_15
(
    s01_personkey       numeric(15),
    s05_saksblokk       char(1),
    s10_saksnr          char(2),
    s15_lopenr          char(2),
    s15_beh_enhet_type  char(3),
    s15_beh_enhet_enhet char(4),
    s15_status          char(2),
    s15_status_dato     numeric(8),
    s15_brukerid        char(7),
    s15_status_klokke   char(6),
    s15_status_brukerid char(7),
    s15_endrings_kode   char(1),
    s15_type_gml        char(2),
    s15_type_ny         char(2),
    s15_lovetsvar_dato  numeric(8),
    s15_ant_lofter      char(2),
    s15_gruppe          char(2),
    s15_sperr           char(1),
    tk_nr               char(4),
    f_nr                char(11),
    opprettet           timestamp(6) not null default current_timestamp,
    endret_i_kilde      timestamp(6)          default current_timestamp,
    kilde_is            varchar(12)           default ' ',
    region              char(1)               default ' ',
    id_status           numeric primary key,
    oppdatert           timestamp(6)          default current_timestamp,
    db_splitt           char(2)               default '99'
);

create table sa_saksblokk_05
(
    s01_personkey    numeric(15),
    s05_saksblokk    char(1),
    s05_gruppe       char(2),
    s05_brukerid     char(7),
    s05_mengdetellet char(1),
    tk_nr            char(4),
    f_nr             char(11),
    opprettet        timestamp(6) not null default current_timestamp,
    endret_i_kilde   timestamp(6)          default current_timestamp,
    kilde_is         varchar(12)           default ' ',
    region           char(1)               default ' ',
    id_sblk          numeric primary key,
    oppdatert        timestamp(6)          default current_timestamp,
    db_splitt        char(2)               default '99'
);

create table sa_person_01
(
    s01_personkey  numeric(15),
    tk_nr          char(4),
    f_nr           char(11),
    opprettet      timestamp(6) not null default current_timestamp,
    endret_i_kilde timestamp(6)          default current_timestamp,
    kilde_is       varchar(12)           default ' ',
    region         char(1)               default ' ',
    id_pers        numeric primary key,
    oppdatert      timestamp(6)          default current_timestamp,
    db_splitt      char(2)               default '99'
);

create table sa_hendelse_20
(
    s01_personkey       numeric(15),
    s05_saksblokk       char(1),
    s20_aksjonsdato_seq numeric(8),
    s20_s_b_kode        char(1),
    s20_brevnummer      char(2),
    s20_mottakerkode    char(3),
    s20_mottakernr      char(11),
    s20_fnr_anr_instnr  numeric(11),
    s20_opplysning      char(38),
    s20_ant_purreuker_1 char(2),
    s20_ant_purreuker_2 char(2),
    s20_ant_purreuker_3 char(2),
    s20_s_purrenr       char(1),
    s20_1_purredato     numeric(8),
    s20_2_purredato     numeric(8),
    s20_3_purredato     numeric(8),
    s20_returdato       numeric(8),
    s20_neste_purredato numeric(8),
    s20_saksnr          char(2),
    s20_kladd           char(1),
    s20_lopenr_fm01     char(1),
    s20_lopenr_print    char(3),
    s20_sentralprint    char(1),
    s20_brevtype        char(1),
    s20_tekstkode_1     char(4),
    s20_tekstkode_2     char(4),
    s20_tekstkode_3     char(4),
    s20_tekstkode_4     char(4),
    s20_tekstkode_5     char(4),
    s20_purretekst_1    char(4),
    s20_purretekst_2    char(4),
    s20_purretekst_3    char(4),
    s20_dato_dannet     numeric(8),
    s20_lager           char(1),
    s20_reg_av          char(3),
    s20_oppr_tknr       char(4),
    s20_oppr_brukerid   char(7),
    s20_ikke_avbryt     char(1),
    s20_maalform        char(1),
    tk_nr               char(4),
    f_nr                char(11),
    opprettet           timestamp(6) not null default current_timestamp,
    endret_i_kilde      timestamp(6)          default current_timestamp,
    kilde_is            varchar(12)           default ' ',
    region              char(1)               default ' ',
    id_hend             numeric primary key,
    oppdatert           timestamp(6)          default current_timestamp,
    db_splitt           char(2)               default '99'
);

create table ks_person_01
(
    k01_personkey    numeric(15),
    k01_akonto_belop numeric(7),
    tk_nr            char(4),
    f_nr             char(11),
    opprettet        timestamp(6) not null default current_timestamp,
    endret_i_kilde   timestamp(6)          default current_timestamp,
    kilde_is         varchar(12)           default ' ',
    region           char(1)               default ' ',
    id_pers          numeric primary key,
    oppdatert        timestamp(6)          default current_timestamp,
    db_splitt        char(2)               default 'KS'
);

create table ks_utbet_hist_40
(
    k01_personkey      numeric(15),
    k40_utbet_dato_seq char(8),
    k40_netto_utbet    numeric(7),
    k40_kort_fra       char(10),
    k40_kort_til       char(10),
    k40_gironr         char(11),
    tk_nr              char(4),
    f_nr               char(11),
    opprettet          timestamp(6) not null default current_timestamp,
    endret_i_kilde     timestamp(6)          default current_timestamp,
    kilde_is           varchar(12)           default ' ',
    region             char(1)               default ' ',
    id_uhist           numeric primary key,
    oppdatert          timestamp(6)          default current_timestamp,
    db_splitt          char(2)               default 'KS'
);
