-- Postgres-skjema for familie-ks-infotrygd-replika.
--
-- Tabellene er en replika av et utvalg av tabellene i Infotrygd (Oracle) som brukes av
-- ks-domenet. Navn på tabeller/kolonner er beholdt (i lowercase, siden Postgres
-- foldes unquoted identifiers til lowercase) slik at dette er kompatibelt med replikeringen
-- fra kildesystemet og med de eksisterende spørringene i koden.
--
-- Oracle NUMBER er konvertert til numeric, CHAR/VARCHAR2 til char/varchar, og DATE/TIMESTAMP
-- er beholdt som date/timestamp.

CREATE TABLE ks_stonad_20
(
    id_stnd            NUMERIC      NOT NULL,
    k01_personkey      NUMERIC(15, 0),
    k20_iverfom_seq    VARCHAR(6),
    k20_virkfom_seq    VARCHAR(6),
    k20_gruppe         CHAR(2),
    k20_brukerid       VARCHAR(7),
    k20_tknr           VARCHAR(4),
    k20_reg_dato       NUMERIC(8, 0),
    k20_sok_dato       NUMERIC(8, 0),
    k20_blokk          CHAR(1),
    k20_sak_nr         CHAR(2),
    k20_tekstkode      CHAR(2),
    k20_tot_ant_barn   CHAR(2),
    k20_ant_ks_barn    CHAR(2),
    k20_ebet_fom       VARCHAR(6),
    k20_ebet_tom       VARCHAR(6),
    k20_opphoert_iver  VARCHAR(6),
    k20_opphoert_vfom  VARCHAR(6),
    k20_opphorsgrunn   CHAR(1),
    k20_omregn         CHAR(1),
    k20_eos            CHAR(1),
    k20_adoptiv_sak    CHAR(1),
    k20_ant_adop_barn  CHAR(1),
    k20_opphor_adopsak VARCHAR(6),
    k20_status_x       CHAR(1),
    tk_nr              VARCHAR(4),
    f_nr               CHAR(11),
    opprettet          TIMESTAMP(6) NOT NULL DEFAULT current_timestamp,
    endret_i_kilde     TIMESTAMP(6)          DEFAULT current_timestamp,
    kilde_is           VARCHAR(12),
    region             CHAR(1),
    oppdatert          TIMESTAMP(6)          DEFAULT current_timestamp,
    db_splitt          CHAR(2)               DEFAULT 'KS',
    CONSTRAINT pk_ks_stonad_20 PRIMARY KEY (id_stnd)
);

CREATE TABLE ks_barn_10
(
    id_barn          NUMERIC      NOT NULL,
    k01_personkey    NUMERIC(11, 0),
    k10_barn_fnr     NUMERIC(11, 0),
    k10_ba_iver_seq  VARCHAR(6),
    k10_ba_vfom_seq  VARCHAR(6),
    k10_ba_tom_seq   VARCHAR(6),
    k10_timer_pr_uke CHAR(2),
    k10_stottetype   CHAR(2),
    tk_nr            VARCHAR(4),
    f_nr             CHAR(11),
    opprettet        TIMESTAMP(6) NOT NULL DEFAULT current_timestamp,
    endret_i_kilde   TIMESTAMP(6)          DEFAULT current_timestamp,
    kilde_is         VARCHAR(12)           DEFAULT ' ',
    region           CHAR(1)               DEFAULT ' ',
    oppdatert        TIMESTAMP(6)          DEFAULT current_timestamp,
    db_splitt        CHAR(2)               DEFAULT 'KS',
    CONSTRAINT pk_ks_barn_10 PRIMARY KEY (id_barn)
);

CREATE TABLE ks_utbetaling_30
(
    id_utbet                NUMERIC      NOT NULL,
    k01_personkey           NUMERIC(15, 0),
    k30_start_utbet_mnd_seq VARCHAR(6),
    k30_vfom_seq            VARCHAR(6),
    k30_kontonr             VARCHAR(8),
    k30_utbet_type          CHAR(1),
    k30_gruppe              CHAR(2),
    k30_brukerid            VARCHAR(7),
    k30_utbet_fom           VARCHAR(6),
    k30_utbet_tom           VARCHAR(6),
    k30_utbetalt            CHAR(1),
    k30_belop               NUMERIC(7, 0),
    k30_utbet_dato          NUMERIC(8, 0),
    tk_nr                   VARCHAR(4),
    f_nr                    CHAR(11),
    opprettet               TIMESTAMP(6) NOT NULL DEFAULT current_timestamp,
    endret_i_kilde          TIMESTAMP(6)          DEFAULT current_timestamp,
    kilde_is                VARCHAR(12)           DEFAULT ' ',
    region                  CHAR(1)               DEFAULT ' ',
    oppdatert               TIMESTAMP(6)          DEFAULT current_timestamp,
    db_splitt               CHAR(2)               DEFAULT 'KS',
    CONSTRAINT pk_ks_utbetaling_30 PRIMARY KEY (id_utbet)
);

-- Oppslag på fødselsnummer (Stonad.fnr er lagret reversert)
CREATE INDEX idx_ks_stonad_20_f_nr ON ks_stonad_20 (f_nr);

-- Join fra ks_stonad_20 til barn og utbetalinger
CREATE INDEX idx_ks_barn_10_stonad
    ON ks_barn_10 (region, k01_personkey, k10_ba_iver_seq, k10_ba_vfom_seq);

CREATE INDEX idx_ks_utbetaling_30_stonad
    ON ks_utbetaling_30 (region, k01_personkey, k30_start_utbet_mnd_seq, k30_vfom_seq);
