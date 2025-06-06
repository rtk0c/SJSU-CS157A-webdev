INSERT INTO Books (title, author, page_count, publisher, date_published, library_copies, available_copies) VALUES
('The Great Gatsby', 'F. Scott Fitzgerald', 180, 'Scribner', '1925-04-10', 10, 7),
('1984', 'George Orwell', 328, 'Secker & Warburg', '1949-06-08', 8, 5),
('To Kill a Mockingbird', 'Harper Lee', 281, 'J.B. Lippincott & Co.', '1960-07-11', 12, 10),
('Pride and Prejudice', 'Jane Austen', 279, 'T. Egerton', '1813-01-28', 6, 4),
('Moby-Dick', 'Herman Melville', 635, 'Harper & Brothers', '1851-10-18', 5, 2),
('War and Peace', 'Leo Tolstoy', 1225, 'The Russian Messenger', '1869-01-01', 4, 2),
('Hamlet', 'William Shakespeare', 400, 'Simon & Schuster', '1603-01-01', 7, 5),
('The Catcher in the Rye', 'J.D. Salinger', 214, 'Little, Brown and Company', '1951-07-16', 9, 6),
('Brave New World', 'Aldous Huxley', 268, 'Chatto & Windus', '1932-08-30', 6, 3),
('Crime and Punishment', 'Fyodor Dostoevsky', 671, 'The Russian Messenger', '1866-01-01', 3, 1),
('The Hobbit', 'J.R.R. Tolkien', 310, 'George Allen & Unwin', '1937-09-21', 10, 8),
('Fahrenheit 451', 'Ray Bradbury', 158, 'Ballantine Books', '1953-10-19', 7, 6),
('Jane Eyre', 'Charlotte Brontë', 500, 'Smith, Elder & Co.', '1847-10-16', 5, 3),
('Frankenstein', 'Mary Shelley', 280, 'Lackington, Hughes, Harding, Mavor & Jones', '1818-01-01', 6, 4),
('The Odyssey', 'Homer', 541, 'Ancient Greece', '0800-01-01', 5, 2);

INSERT INTO Members (first_name, last_name, email, membership_date) VALUES
('Alice', 'Johnson', 'alice.johnson@example.com', '2023-01-15'),
('Bob', 'Smith', 'bob.smith@example.com', '2022-06-20'),
('Carol', 'Davis', 'carol.davis@example.com', '2021-03-30'),
('David', 'Wilson', 'david.wilson@example.com', '2023-07-12'),
('Eva', 'Martinez', 'eva.martinez@example.com', '2022-11-25'),
('Frank', 'Brown', 'frank.brown@example.com', '2021-08-03'),
('Grace', 'Clark', 'grace.clark@example.com', '2023-02-17'),
('Henry', 'Lewis', 'henry.lewis@example.com', '2020-10-10'),
('Isabel', 'Lee', 'isabel.lee@example.com', '2022-01-01'),
('Jack', 'Walker', 'jack.walker@example.com', '2023-09-09'),
('Karen', 'Hall', 'karen.hall@example.com', '2023-03-27'),
('Liam', 'Allen', 'liam.allen@example.com', '2021-05-05'),
('Mia', 'Young', 'mia.young@example.com', '2023-06-16'),
('Nathan', 'Hernandez', 'nathan.hernandez@example.com', '2022-04-04'),
('Olivia', 'King', 'olivia.king@example.com', '2021-12-21');

INSERT INTO Borrow_Returns (book_id, member_id, borrow_date, return_date, due_date) VALUES
(1, 1, '2024-12-01', '2024-12-10', '2024-12-09'),
(2, 2, '2025-01-05', '2025-01-15', '2025-01-12'),
(3, 3, '2025-03-01', '2025-03-10', '2025-03-08'),
(4, 4, '2025-02-10', '2025-02-25', '2025-02-20'),
(5, 5, '2025-03-15', '2025-03-20', '2025-03-18'),
(6, 6, '2025-04-01', '2025-04-10', '2025-04-07'),
(7, 7, '2025-04-05', '2025-04-20', '2025-04-15'),
(8, 8, '2025-03-20', '2025-03-27', '2025-03-25'),
(9, 9, '2025-01-15', '2025-01-25', '2025-01-22'),
(10, 10, '2025-02-01', '2025-02-05', '2025-02-04'),
(11, 11, '2025-03-01', '2025-03-09', '2025-03-07'),
(12, 12, '2025-04-10', '2025-04-17', '2025-04-16'),
(13, 13, '2025-02-20', '2025-03-01', '2025-02-28'),
(14, 14, '2025-03-25', '2025-03-30', '2025-03-29'),
(15, 15, '2025-04-15', '2025-04-20', '2025-04-18');

INSERT INTO Fines (br_id, fine_total, fine_status) VALUES
(4, 3.00, true),
(7, 5.00, false),
(9, 2.50, true),
(13, 4.00, false),
(2, 0.00, false),
(3, 0.00, false),
(5, 0.00, false),
(6, 0.00, false),
(8, 0.00, false),
(1, 0.00, false),
(10, 0.00, false),
(11, 0.00, false),
(12, 0.00, false),
(14, 0.00, false),
(15, 0.00, false);
