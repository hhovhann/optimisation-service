CREATE TABLE CAMPAIGN_GROUP(
    ID BIGINT AUTO_INCREMENT,
    NAME VARCHAR(200) NOT NULL,
    primary key(ID)
);


CREATE TABLE CAMPAIGN(
    ID BIGINT AUTO_INCREMENT,
    NAME VARCHAR(200) NOT NULL,
    CAMPAIGN_GROUP_ID BIGINT,
    BUDGET NUMERIC,
    impressions BIGINT,
    REVENUE NUMERIC,
    primary key(ID),
    foreign key (CAMPAIGN_GROUP_ID) references CAMPAIGN_GROUP(ID) ON UPDATE CASCADE
);

CREATE TABLE OPTIMISATION(
    ID BIGINT AUTO_INCREMENT,
    CAMPAIGN_GROUP_ID BIGINT,
    STATUS VARCHAR(200),
    primary key(ID),
    foreign key (CAMPAIGN_GROUP_ID) references CAMPAIGN_GROUP(ID) ON UPDATE CASCADE
);

CREATE TABLE RECOMMENDATION(
    ID BIGINT AUTO_INCREMENT,
    RECOMMENDED_BUDGET NUMERIC,
    CAMPAIGN_ID BIGINT,
    OPTIMISATION_ID BIGINT,
    primary key(ID),
    foreign key (CAMPAIGN_ID) references CAMPAIGN(ID) ON UPDATE CASCADE,
    foreign key (OPTIMISATION_ID) references OPTIMISATION(ID) ON UPDATE CASCADE
);

