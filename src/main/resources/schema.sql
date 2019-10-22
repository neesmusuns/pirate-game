DROP TABLE IF EXISTS USERS;

CREATE TABLE USERS(
  username VARCHAR(250) NOT NULL,
  password VARCHAR(250) NOT NULL,
  money INT,
  headwear INT,
  shirt INT,
  pants INT,
  boat INT,
  drinks INT,
  treasure INT,
  map VARCHAR(1),
  PRIMARY KEY (username, password)
);