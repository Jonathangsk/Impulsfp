CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50),
    password VARCHAR(50),
    role VARCHAR(20)
);

DELETE FROM users; --per no tenir dades duplicades cada vegada que executeu el script

INSERT INTO users (username, password, role) VALUES
     ('Jonathan', '1234', 'ADMIN'),
     ('Alba', '1234', 'STUDENT'),
     ('Josep', '1234', 'COMPANY')
;