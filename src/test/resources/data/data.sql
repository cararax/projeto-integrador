DELETE FROM booking;
DELETE FROM service;
DELETE FROM experience;
DELETE FROM users;
DELETE FROM city;



INSERT INTO city (id, name)
VALUES (1001, 'Rio de Janeiro'),
       (1002, 'São Paulo'),
       (1003, 'Brasília'),
       (1004, 'Salvador'),
       (1005, 'Porto Alegre');

INSERT INTO users (id, description, eldery_birth_date, eldery_name, email, firstname, health_details, lastname,
                   password, role, city_id)
VALUES (1001, 'Cuidador de idosos', '1945-07-12', 'Jose', 'jose@gmail.com', 'Joao', 'Sem detalhes', 'Silva', '$2a$10$JTjKRDP3HEqwC.JgOzNNiOuIZXFgZ/An97E3Y/BYciEbBlOti3BWW', 'CAREGIVER', 1001),
       (1002, 'Recebedor de cuidados', '1952-03-25', 'Maria', 'maria@gmail.com', 'Ana', 'Sem detalhes', 'Santos', '$2a$10$JTjKRDP3HEqwC.JgOzNNiOuIZXFgZ/An97E3Y/BYciEbBlOti3BWW', 'CARERECIVIER', 1002),
       (1003, 'Cuidador de idosos', '1947-12-02', 'Antonio', 'antonio@gmail.com', 'Carlos', 'Sem detalhes', 'Oliveira', '$2a$10$JTjKRDP3HEqwC.JgOzNNiOuIZXFgZ/An97E3Y/BYciEbBlOti3BWW', 'CAREGIVER', 1003),
       (1004, 'Recebedor de cuidados', '1943-05-20', 'Joana', 'joana@gmail.com', 'Marta', 'Sem detalhes', 'Costa', '$2a$10$JTjKRDP3HEqwC.JgOzNNiOuIZXFgZ/An97E3Y/BYciEbBlOti3BWW', 'CARERECIVIER', 1004),
       (1005, 'Cuidador de idosos', '1946-09-14', 'Francisco', 'francisco@gmail.com', 'Fernando', 'Sem detalhes', 'Pereira', '$2a$10$JTjKRDP3HEqwC.JgOzNNiOuIZXFgZ/An97E3Y/BYciEbBlOti3BWW', 'CAREGIVER', 1005);

INSERT INTO experience (id, company, description, end_date, start_date, caregiver_id)
VALUES (1001, 'Cuidador Cia', 'Cuidador principal', '2022-05-10', '2015-01-15', 1001),
       (1002, 'Cuidador Cia', 'Cuidador assistente', '2022-05-10', '2016-02-18', 1001),
       (1003, 'Cuidador S.A.', 'Cuidador principal', '2022-05-10', '2017-03-21', 1003),
       (1004, 'Cuidador S.A.', 'Cuidador assistente', '2022-05-10', '2018-04-24', 1003),
       (1005, 'Cuidador Ltda.', 'Cuidador principal', '2022-05-10', '2019-05-27', 1005);

INSERT INTO service (id, description, duration, name, price, caregiver_id)
VALUES (1001, 'Serviço de cuidados', 2.5, 'Cuidado básico', 50.0, 1001),
       (1002, 'Serviço de cuidados', 3.0, 'Cuidado intermediário', 75.0, 1001),
       (1003, 'Serviço de cuidados', 2.5, 'Cuidado básico', 50.0, 1003),
       (1004, 'Serviço de cuidados', 3.0, 'Cuidado intermediário', 75.0, 1003),
       (1005, 'Serviço de cuidados', 2.5, 'Cuidado básico', 50.0, 1005);

INSERT INTO booking (id, end_date_time, start_date_time, caregiver_id, carerecivier_id, services_id)
VALUES (1001, '2023-06-12 14:30:00', '2023-06-12 12:00:00', 1001, 1002, 1001),
       (1002, '2023-06-13 15:30:00', '2023-06-13 12:30:00', 1001, 1002, 1002),
       (1003, '2023-06-14 14:30:00', '2023-06-14 12:00:00', 1003, 1004, 1003),
       (1004, '2023-06-15 15:30:00', '2023-06-15 12:30:00', 1003, 1004, 1004),
       (1005, '2023-06-16 14:30:00', '2023-06-16 12:00:00', 1005, 1002, 1005);
