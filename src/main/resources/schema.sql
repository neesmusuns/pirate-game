DROP TABLE IF EXISTS GAME_OBJECTS;
DROP TABLE IF EXISTS PLAYERS;

CREATE TABLE GAME_OBJECTS(
  id INT IDENTITY(0,1),
  positionX INT NOT NULL,
  positionY INT NOT NULL,
  scaleX INT NOT NULL,
  scaleY INT NOT NULL,
  sprite VARCHAR(250) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE PLAYERS(
  id INT PRIMARY KEY,
  drunk_level INT NOT NULL,
  FOREIGN KEY (id) REFERENCES GAME_OBJECTS(id)
);