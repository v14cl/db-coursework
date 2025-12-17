CREATE INDEX idx_book_title ON book(title);
CREATE INDEX idx_checkout_client_id ON checkout(client_id);
CREATE INDEX idx_checkout_book_id ON checkout(book_id);
CREATE INDEX idx_checkout_active ON checkout(client_id, date_returned);
