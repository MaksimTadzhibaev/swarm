--
-- ���� ������������ � ������� SQLiteStudio v3.3.3 � �� ��� 31 14:43:27 2021
--
-- �������������� ��������� ������: System
--
PRAGMA foreign_keys = off;
BEGIN TRANSACTION;

-- �������: users
CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, login TEXT, password TEXT);
INSERT INTO users (id, login, password) VALUES (1, 'qwe', 'qwe');
INSERT INTO users (id, login, password) VALUES (2, '123', '123');

COMMIT TRANSACTION;
PRAGMA foreign_keys = on;
