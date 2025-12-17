CREATE TABLE publisher (
    publisher_id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE book (
    book_id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    language VARCHAR(20) NOT NULL CHECK (language IN ('English', 'Ukrainian', 'French')),
    publisher_id INT NOT NULL REFERENCES publisher(publisher_id)
);

CREATE TABLE client (
    client_id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE author (
    author_id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL
);

CREATE TABLE genre (
    genre_id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE author_book (
    book_id INT REFERENCES book(book_id) ON DELETE CASCADE,
    author_id INT REFERENCES author(author_id) ON DELETE CASCADE,
    PRIMARY KEY(book_id, author_id)
);

CREATE TABLE genre_book (
    book_id INT REFERENCES book(book_id) ON DELETE CASCADE,
    genre_id INT REFERENCES genre(genre_id) ON DELETE CASCADE,
    PRIMARY KEY(book_id, genre_id)
);

CREATE TABLE checkout (
    checkout_id SERIAL PRIMARY KEY,
    client_id INT REFERENCES client(client_id),
    book_id INT REFERENCES book(book_id),
    date_taken DATE NOT NULL,
    deadline DATE NOT NULL,
    date_returned DATE
);

CREATE TABLE fine (
    fine_id BIGSERIAL PRIMARY KEY,
    checkout_id INT REFERENCES checkout(checkout_id) ON DELETE CASCADE,
    amount NUMERIC NOT NULL,
    reason VARCHAR(255),
    created_at TIMESTAMP DEFAULT NOW(),
    is_paid BOOLEAN DEFAULT FALSE
);
