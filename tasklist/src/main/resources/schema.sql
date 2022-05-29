CREATE TABLE IF NOT EXISTS tasklist(
    id VARCHAR(8) PRIMARY KEY,
    task VARCHAR(30),
    note VARCHAR(250),
    deadline VARCHAR(10),
    done BOOLEAN
);