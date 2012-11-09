CREATE TABLE authentication(
	auth_token varchar(128) PRIMARY KEY,
	user_id char(9) NOT NULL
);

CREATE TABLE checkpoint(
	checkpoint_id INT PRIMARY KEY,
	latitude REAL NOT NULL,
	longitude REAL NOT NULL,
	challenge INT
);

CREATE TABLE player(
	player_id INT PRIMARY KEY,
	first_name varchar(64) NOT NULL,
	last_name varchar(64) NOT NULL,
	student_number char(9) NOT NULL
);

CREATE TABLE team(
	team_id INT PRIMARY KEY,
	players VARCHAR(10)
);

CREATE TABLE scavengerhunt(
	scavenger_hunt_id INT PRIMARY KEY,
	path INT NOT NULL,
	team INT NOT NULL,
	start_time TIMESTAMP,
	finish_time TIMESTAMP
);

CREATE TABLE path(
	path_id INT PRIMARY KEY,
	checkpoints VARCHAR(64)
);

CREATE TABLE challenge(
	challenge_id INT PRIMARY KEY,
	challenge_text VARCHAR(1024)
);

INSERT INTO player VALUES
	(1, 'Jason', 'Recillo', '100726948'),
	(2, 'Peter', 'Le', '100714258'),
	(3, 'Mellicent', 'Dres', '100726767');

INSERT INTO team VALUES
	(1, '1,2');

INSERT INTO checkpoint VALUES
	(1, 43.675854, -79.71069, NULL);
