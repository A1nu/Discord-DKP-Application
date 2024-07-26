-- liquibase formatted sql

-- changeset ainu:1721051445502-1
CREATE TABLE encounter
(
    id                  UUID         NOT NULL,
    version             BIGINT,
    created_date        TIMESTAMP WITHOUT TIME ZONE,
    last_modified_date  TIMESTAMP WITHOUT TIME ZONE,
    creator_snowflake   BIGINT       NOT NULL,
    editor_snowflake    BIGINT,
    encounter_name      VARCHAR(255) NOT NULL,
    scheduled_encounter BOOLEAN,
    prime_encounter     BOOLEAN,
    encounter_weight    UUID         NOT NULL,
    guild_id            BIGINT       NOT NULL,
    CONSTRAINT pk_encounter PRIMARY KEY (id)
);

-- changeset ainu:1721051445502-2
CREATE TABLE encounter_spawn
(
    id                 UUID   NOT NULL,
    version            BIGINT,
    created_date       TIMESTAMP WITHOUT TIME ZONE,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
    creator_snowflake  BIGINT NOT NULL,
    editor_snowflake   BIGINT,
    encounter_id       UUID,
    day_of_week        VARCHAR(255),
    CONSTRAINT pk_encounter_spawn PRIMARY KEY (id)
);

-- changeset ainu:1721051445502-3
CREATE TABLE encounter_spawn_time
(
    id                 UUID   NOT NULL,
    version            BIGINT,
    created_date       TIMESTAMP WITHOUT TIME ZONE,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
    creator_snowflake  BIGINT NOT NULL,
    editor_snowflake   BIGINT,
    offset_time        time WITHOUT TIME ZONE NOT NULL,
    encounter_spawn_id UUID   NOT NULL,
    CONSTRAINT pk_encounter_spawn_time PRIMARY KEY (id)
);

-- changeset ainu:1721051445502-4
CREATE TABLE encounter_weight
(
    id                 UUID    NOT NULL,
    version            BIGINT,
    created_date       TIMESTAMP WITHOUT TIME ZONE,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
    creator_snowflake  BIGINT  NOT NULL,
    editor_snowflake   BIGINT,
    encounter_weight   INTEGER NOT NULL,
    CONSTRAINT pk_encounter_weight PRIMARY KEY (id)
);

-- changeset ainu:1721051445502-5
CREATE TABLE event_attendance
(
    id                 UUID   NOT NULL,
    version            BIGINT,
    created_date       TIMESTAMP WITHOUT TIME ZONE,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
    creator_snowflake  BIGINT NOT NULL,
    editor_snowflake   BIGINT,
    event_id           UUID   NOT NULL,
    guild_member_id    UUID   NOT NULL,
    CONSTRAINT pk_event_attendance PRIMARY KEY (id)
);

-- changeset ainu:1721051445502-6
CREATE TABLE guild
(
    snowflake              BIGINT NOT NULL,
    version                BIGINT,
    created_date           TIMESTAMP WITHOUT TIME ZONE,
    last_modified_date     TIMESTAMP WITHOUT TIME ZONE,
    creator_snowflake      BIGINT NOT NULL,
    editor_snowflake       BIGINT,
    guild_configuration_id UUID,
    CONSTRAINT pk_guild PRIMARY KEY (snowflake)
);

-- changeset ainu:1721051445502-7
CREATE TABLE guild_administrator_role
(
    id                     UUID   NOT NULL,
    version                BIGINT,
    created_date           TIMESTAMP WITHOUT TIME ZONE,
    last_modified_date     TIMESTAMP WITHOUT TIME ZONE,
    creator_snowflake      BIGINT NOT NULL,
    editor_snowflake       BIGINT,
    guild_configuration_id UUID   NOT NULL,
    role_snowflake         BIGINT NOT NULL,
    CONSTRAINT pk_guild_administrator_role PRIMARY KEY (id)
);

-- changeset ainu:1721051445502-8
CREATE TABLE guild_configuration
(
    id                         UUID   NOT NULL,
    version                    BIGINT,
    created_date               TIMESTAMP WITHOUT TIME ZONE,
    last_modified_date         TIMESTAMP WITHOUT TIME ZONE,
    creator_snowflake          BIGINT NOT NULL,
    editor_snowflake           BIGINT,
    guild_id                   BIGINT NOT NULL,
    admin_role_snowflake       BIGINT,
    moderator_role_snowflake   BIGINT,
    member_role_snowflake      BIGINT,
    stash_enabled              BOOLEAN,
    attendance_enabled         BOOLEAN,
    loot_pretending_days_delay INTEGER,
    CONSTRAINT pk_guild_configuration PRIMARY KEY (id)
);

-- changeset ainu:1721051445502-9
CREATE TABLE guild_event
(
    id                    UUID   NOT NULL,
    version               BIGINT,
    created_date          TIMESTAMP WITHOUT TIME ZONE,
    last_modified_date    TIMESTAMP WITHOUT TIME ZONE,
    creator_snowflake     BIGINT NOT NULL,
    editor_snowflake      BIGINT,
    guild_event_encounter UUID   NOT NULL,
    event_owner_guild     BIGINT NOT NULL,
    CONSTRAINT pk_guild_event PRIMARY KEY (id)
);

-- changeset ainu:1721051445502-10
CREATE TABLE guild_member
(
    id                 UUID   NOT NULL,
    version            BIGINT,
    created_date       TIMESTAMP WITHOUT TIME ZONE,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
    creator_snowflake  BIGINT NOT NULL,
    editor_snowflake   BIGINT,
    member_guild       BIGINT NOT NULL,
    member             BIGINT NOT NULL,
    member_join_date   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    member_suspended   BOOLEAN,
    CONSTRAINT pk_guild_member PRIMARY KEY (id)
);

-- changeset ainu:1721051445502-11
CREATE TABLE guild_member_note
(
    id                 UUID   NOT NULL,
    version            BIGINT,
    created_date       TIMESTAMP WITHOUT TIME ZONE,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
    creator_snowflake  BIGINT NOT NULL,
    editor_snowflake   BIGINT,
    note_text          VARCHAR(255),
    visible_to_member  BOOLEAN,
    member_id          UUID,
    CONSTRAINT pk_guild_member_note PRIMARY KEY (id)
);

-- changeset ainu:1721051445502-12
CREATE TABLE guild_member_role
(
    id                     UUID   NOT NULL,
    version                BIGINT,
    created_date           TIMESTAMP WITHOUT TIME ZONE,
    last_modified_date     TIMESTAMP WITHOUT TIME ZONE,
    creator_snowflake      BIGINT NOT NULL,
    editor_snowflake       BIGINT,
    guild_configuration_id UUID   NOT NULL,
    role_snowflake         BIGINT NOT NULL,
    CONSTRAINT pk_guild_member_role PRIMARY KEY (id)
);

-- changeset ainu:1721051445502-13
CREATE TABLE guild_moderator_role
(
    id                     UUID   NOT NULL,
    version                BIGINT,
    created_date           TIMESTAMP WITHOUT TIME ZONE,
    last_modified_date     TIMESTAMP WITHOUT TIME ZONE,
    creator_snowflake      BIGINT NOT NULL,
    editor_snowflake       BIGINT,
    guild_configuration_id UUID   NOT NULL,
    role_snowflake         BIGINT NOT NULL,
    CONSTRAINT pk_guild_moderator_role PRIMARY KEY (id)
);

-- changeset ainu:1721051445502-14
CREATE TABLE item_model
(
    id                 UUID         NOT NULL,
    version            BIGINT,
    created_date       TIMESTAMP WITHOUT TIME ZONE,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
    creator_snowflake  BIGINT       NOT NULL,
    editor_snowflake   BIGINT,
    item_name          VARCHAR(255) NOT NULL,
    item_price         INTEGER,
    synthesized_item   BOOLEAN,
    item_exp_needed    INTEGER,
    countable_item     BOOLEAN,
    item_quantity      INTEGER,
    guild_id           BIGINT,
    CONSTRAINT pk_item_model PRIMARY KEY (id)
);

-- changeset ainu:1721051445502-15
CREATE TABLE member
(
    snowflake            BIGINT NOT NULL,
    version              BIGINT,
    created_date         TIMESTAMP WITHOUT TIME ZONE,
    last_modified_date   TIMESTAMP WITHOUT TIME ZONE,
    creator_snowflake    BIGINT NOT NULL,
    editor_snowflake     BIGINT,
    member_configuration UUID,
    CONSTRAINT pk_member PRIMARY KEY (snowflake)
);

-- changeset ainu:1721051445502-16
CREATE TABLE member_configuration
(
    id                   UUID   NOT NULL,
    version              BIGINT,
    created_date         TIMESTAMP WITHOUT TIME ZONE,
    last_modified_date   TIMESTAMP WITHOUT TIME ZONE,
    creator_snowflake    BIGINT NOT NULL,
    editor_snowflake     BIGINT,
    member_default_guild BIGINT,
    CONSTRAINT pk_member_configuration PRIMARY KEY (id)
);

-- changeset ainu:1721051445502-17
CREATE TABLE personalized_item
(
    id                 UUID         NOT NULL,
    version            BIGINT,
    created_date       TIMESTAMP WITHOUT TIME ZONE,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
    creator_snowflake  BIGINT       NOT NULL,
    editor_snowflake   BIGINT,
    item_name          VARCHAR(255) NOT NULL,
    item_price         INTEGER,
    synthesized_item   BOOLEAN,
    item_exp_needed    INTEGER,
    countable_item     BOOLEAN,
    item_quantity      INTEGER,
    guild_id           BIGINT,
    item_available     BOOLEAN,
    item_owner         UUID,
    item_exp_inserted  INTEGER      NOT NULL,
    CONSTRAINT pk_personalized_item PRIMARY KEY (id)
);

-- changeset ainu:1721051445502-18
ALTER TABLE encounter
    ADD CONSTRAINT uc_encounter_encounter_weight UNIQUE (encounter_weight);

-- changeset ainu:1721051445502-19
ALTER TABLE encounter_weight
    ADD CONSTRAINT uc_encounter_weight_encounter_weight UNIQUE (encounter_weight);

-- changeset ainu:1721051445502-20
ALTER TABLE guild_configuration
    ADD CONSTRAINT uc_guild_configuration_guild UNIQUE (guild_id);

-- changeset ainu:1721051445502-21
ALTER TABLE guild
    ADD CONSTRAINT uc_guild_guild_configuration UNIQUE (guild_configuration_id);

-- changeset ainu:1721051445502-22
ALTER TABLE guild_member
    ADD CONSTRAINT uc_guild_member_member UNIQUE (member);

-- changeset ainu:1721051445502-23
ALTER TABLE member_configuration
    ADD CONSTRAINT uc_member_configuration_member_default_guild UNIQUE (member_default_guild);

-- changeset ainu:1721051445502-24
ALTER TABLE member
    ADD CONSTRAINT uc_member_member_configuration UNIQUE (member_configuration);

-- changeset ainu:1721051445502-25
ALTER TABLE encounter
    ADD CONSTRAINT FK_ENCOUNTER_ON_ENCOUNTER_WEIGHT FOREIGN KEY (encounter_weight) REFERENCES encounter_weight (id);

-- changeset ainu:1721051445502-26
ALTER TABLE encounter
    ADD CONSTRAINT FK_ENCOUNTER_ON_GUILD FOREIGN KEY (guild_id) REFERENCES guild (snowflake);

-- changeset ainu:1721051445502-27
ALTER TABLE encounter_spawn
    ADD CONSTRAINT FK_ENCOUNTER_SPAWN_ON_ENCOUNTER FOREIGN KEY (encounter_id) REFERENCES encounter (id);

-- changeset ainu:1721051445502-28
ALTER TABLE encounter_spawn_time
    ADD CONSTRAINT FK_ENCOUNTER_SPAWN_TIME_ON_ENCOUNTER_SPAWN FOREIGN KEY (encounter_spawn_id) REFERENCES encounter_spawn (id);

-- changeset ainu:1721051445502-29
ALTER TABLE event_attendance
    ADD CONSTRAINT FK_EVENT_ATTENDANCE_ON_EVENT FOREIGN KEY (event_id) REFERENCES guild_event (id);

-- changeset ainu:1721051445502-30
ALTER TABLE event_attendance
    ADD CONSTRAINT FK_EVENT_ATTENDANCE_ON_GUILD_MEMBER FOREIGN KEY (guild_member_id) REFERENCES guild_member (id);

-- changeset ainu:1721051445502-31
ALTER TABLE guild_administrator_role
    ADD CONSTRAINT FK_GUILD_ADMINISTRATOR_ROLE_ON_GUILD_CONFIGURATION FOREIGN KEY (guild_configuration_id) REFERENCES guild_configuration (id);

-- changeset ainu:1721051445502-32
ALTER TABLE guild_configuration
    ADD CONSTRAINT FK_GUILD_CONFIGURATION_ON_GUILD FOREIGN KEY (guild_id) REFERENCES guild (snowflake);

-- changeset ainu:1721051445502-33
ALTER TABLE guild_event
    ADD CONSTRAINT FK_GUILD_EVENT_ON_EVENT_OWNER_GUILD FOREIGN KEY (event_owner_guild) REFERENCES guild (snowflake);

-- changeset ainu:1721051445502-34
ALTER TABLE guild_event
    ADD CONSTRAINT FK_GUILD_EVENT_ON_GUILD_EVENT_ENCOUNTER FOREIGN KEY (guild_event_encounter) REFERENCES encounter (id);

-- changeset ainu:1721051445502-35
ALTER TABLE guild_member_note
    ADD CONSTRAINT FK_GUILD_MEMBER_NOTE_ON_MEMBER FOREIGN KEY (member_id) REFERENCES guild_member (id);

-- changeset ainu:1721051445502-36
ALTER TABLE guild_member
    ADD CONSTRAINT FK_GUILD_MEMBER_ON_MEMBER FOREIGN KEY (member) REFERENCES member (snowflake);

-- changeset ainu:1721051445502-37
ALTER TABLE guild_member
    ADD CONSTRAINT FK_GUILD_MEMBER_ON_MEMBER_GUILD FOREIGN KEY (member_guild) REFERENCES guild (snowflake);

-- changeset ainu:1721051445502-38
ALTER TABLE guild_member_role
    ADD CONSTRAINT FK_GUILD_MEMBER_ROLE_ON_GUILD_CONFIGURATION FOREIGN KEY (guild_configuration_id) REFERENCES guild_configuration (id);

-- changeset ainu:1721051445502-39
ALTER TABLE guild_moderator_role
    ADD CONSTRAINT FK_GUILD_MODERATOR_ROLE_ON_GUILD_CONFIGURATION FOREIGN KEY (guild_configuration_id) REFERENCES guild_configuration (id);

-- changeset ainu:1721051445502-40
ALTER TABLE guild
    ADD CONSTRAINT FK_GUILD_ON_GUILD_CONFIGURATION FOREIGN KEY (guild_configuration_id) REFERENCES guild_configuration (id);

-- changeset ainu:1721051445502-41
ALTER TABLE item_model
    ADD CONSTRAINT FK_ITEM_MODEL_ON_GUILD FOREIGN KEY (guild_id) REFERENCES guild (snowflake);

-- changeset ainu:1721051445502-42
ALTER TABLE member_configuration
    ADD CONSTRAINT FK_MEMBER_CONFIGURATION_ON_MEMBER_DEFAULT_GUILD FOREIGN KEY (member_default_guild) REFERENCES guild (snowflake);

-- changeset ainu:1721051445502-43
ALTER TABLE member
    ADD CONSTRAINT FK_MEMBER_ON_MEMBER_CONFIGURATION FOREIGN KEY (member_configuration) REFERENCES member_configuration (id);

-- changeset ainu:1721051445502-44
ALTER TABLE personalized_item
    ADD CONSTRAINT FK_PERSONALIZED_ITEM_ON_GUILD FOREIGN KEY (guild_id) REFERENCES guild (snowflake);

-- changeset ainu:1721051445502-45
ALTER TABLE personalized_item
    ADD CONSTRAINT FK_PERSONALIZED_ITEM_ON_ITEM_OWNER FOREIGN KEY (item_owner) REFERENCES guild_member (id);

