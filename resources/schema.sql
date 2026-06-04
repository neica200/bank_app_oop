DROP TABLE IF EXISTS tranzactii;
DROP TABLE IF EXISTS carduri;
DROP TABLE IF EXISTS conturi;
DROP TABLE IF EXISTS clienti;

CREATE TABLE clienti (
    id VARCHAR(50) PRIMARY KEY,
    nume VARCHAR(100) NOT NULL,
    prenume VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE conturi (
    iban VARCHAR(50) PRIMARY KEY,
    tip VARCHAR(20) NOT NULL, -- 'CURENT' sau 'ECONOMII'
    sold DOUBLE NOT NULL DEFAULT 0.0,
    titular_id VARCHAR(50) NOT NULL,
    limita_overdraft DOUBLE DEFAULT 0.0,
    rata_dobanda DOUBLE DEFAULT 0.0,
    FOREIGN KEY (titular_id) REFERENCES clienti(id) ON DELETE CASCADE
);

CREATE TABLE carduri (
    numar_card VARCHAR(20) PRIMARY KEY,
    pin VARCHAR(4) NOT NULL,
    iban_asociat VARCHAR(50) NOT NULL,
    este_blocat BOOLEAN DEFAULT FALSE,
    limita_zilnica DOUBLE DEFAULT 5000.0,
    data_expirarii DATE NOT NULL,
    FOREIGN KEY (iban_asociat) REFERENCES conturi(iban) ON DELETE CASCADE
);

CREATE TABLE tranzactii (
    id VARCHAR(50) PRIMARY KEY,
    tip VARCHAR(30) NOT NULL, -- 'PLATA', 'INTRA', 'INTER'
    suma DOUBLE NOT NULL,
    valuta VARCHAR(10) NOT NULL,
    data_executie TIMESTAMP NOT NULL,
    iban_sursa VARCHAR(50) NOT NULL,
    iban_destinatie VARCHAR(50),
    comerciant VARCHAR(100),
    swift VARCHAR(20),
    comision DOUBLE DEFAULT 0.0,
    FOREIGN KEY (iban_sursa) REFERENCES conturi(iban) ON DELETE CASCADE
);