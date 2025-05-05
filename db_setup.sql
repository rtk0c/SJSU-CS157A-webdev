CREATE TABLE Books (
  book_id SERIAL PRIMARY KEY,
  title VARCHAR(45) NOT NULL,
  author VARCHAR(45) NOT NULL,
  page_count INT NOT NULL,
  publisher VARCHAR(45) NOT NULL,
  date_published DATE NOT NULL,
  library_copies INT NOT NULL,
  available_copies INT NOT NULL
);

CREATE TABLE Members (
  member_id SERIAL PRIMARY KEY,
  first_name VARCHAR(45) NOT NULL,
  last_name VARCHAR(45) NOT NULL,
  email VARCHAR(45) NOT NULL,
  membership_date DATE NOT NULL
);

CREATE TABLE Borrow_Returns (
  br_id SERIAL PRIMARY KEY,
  book_id INT NOT NULL,
  member_id INT NOT NULL,
  borrow_date DATE NOT NULL,
  return_date DATE NOT NULL,
  due_date DATE NOT NULL,
  borrowed_book_status VARCHAR NOT NULL,
  FOREIGN KEY (book_id) REFERENCES Books(book_id),
  FOREIGN KEY (member_id) REFERENCES Members(member_id)
);

CREATE TABLE Fines (
  fine_id SERIAL PRIMARY KEY,
  br_id INT NOT NULL,
  fine_total DECIMAL NOT NULL,
  fine_status VARCHAR(45) NOT NULL,
  FOREIGN KEY (br_id) REFERENCES Borrow_Returns(br_id)
);
