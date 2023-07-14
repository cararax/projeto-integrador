DELETE
FROM booking;
DELETE
FROM service;
DELETE
FROM experience;
DELETE
FROM users;
DELETE
FROM city;

insert into public.city (id, name)
values  (1006, 'Manaus'),
        (1007, 'Belém'),
        (1008, 'Fortaleza'),
        (1009, 'Recife'),
        (1010, 'Natal'),
        (1011, 'João Pessoa'),
        (1012, 'Salvador'),
        (1013, 'Belo Horizonte'),
        (1014, 'Rio de Janeiro'),
        (1015, 'São Paulo');


insert into public.users (eldery_birth_date, city_id, id, description, eldery_name, email, firstname, health_details, lastname, password, role)
values  ('1944-04-03', 1009, 1009, 'Cuidador de idosos comprometido e empático', 'Marcelo', 'marcelo@gmail.com', 'Camila', 'Sem detalhes', 'Andrade', '$2a$10$JTjKRDP3HEqwC.JgOzNNiOuIZXFgZ/An97E3Y/BYciEbBlOti3BWW', 'CAREGIVER'),
        ('1947-12-10', 1010, 1010, 'Cuidador de idosos atencioso e paciente', 'Sandra', 'sandra@gmail.com', 'Gustavo', 'Sem detalhes', 'Santana', '$2a$10$JTjKRDP3HEqwC.JgOzNNiOuIZXFgZ/An97E3Y/BYciEbBlOti3BWW', 'CAREGIVER'),
        (null, 1006, 1006, 'Cuidador de idosos dedicado e atencioso', '', 'raimunda@gmail.com', 'Ricardo', '', 'Ferreira', '$2a$10$JTjKRDP3HEqwC.JgOzNNiOuIZXFgZ/An97E3Y/BYciEbBlOti3BWW', 'CAREGIVER'),
        ('1949-11-07', 1009, 1008, 'Cuidador de idosos responsável e prestativo', 'Lúcia', 'lucia@gmail.com', 'Rodrigo', 'Sem detalhes', 'Lima', '$2a$10$JTjKRDP3HEqwC.JgOzNNiOuIZXFgZ/An97E3Y/BYciEbBlOti3BWW', 'CAREGIVER'),
        ('1950-02-14', 1008, 1007, 'Cuidador de idosos experiente e carinhoso', 'Pedro', 'pedro@gmail.com', 'Fernanda', 'Sem detalhes', 'Cavalcante', '$2a$10$JTjKRDP3HEqwC.JgOzNNiOuIZXFgZ/An97E3Y/BYciEbBlOti3BWW', 'CAREGIVER'),
        ('1949-11-07', 1009, 1013, 'Recebedor de cuidados com doença crônica', 'Francisca Almeida', 'ana@gmail.com', 'Paulo', 'doença crônica', 'Barbosa', '$2a$10$JTjKRDP3HEqwC.JgOzNNiOuIZXFgZ/An97E3Y/BYciEbBlOti3BWW', 'CARERECIVIER'),
        ('1949-11-07', 1015, 1014, 'Recebedor de cuidados com deficiência visual', 'Ana Santos', 'isabela@gmail.com', 'Fábio', 'deficiência visual', 'Fernandes', '$2a$10$JTjKRDP3HEqwC.JgOzNNiOuIZXFgZ/An97E3Y/BYciEbBlOti3BWW', 'CARERECIVIER'),
        ('1943-05-05', 1007, 1011, 'Recebedor de cuidados com necessidades especiais', 'Carlos Antonio', 'carlos@gmail.com', 'Juliana', 'necessidades especiais', 'Rocha', '$2a$10$JTjKRDP3HEqwC.JgOzNNiOuIZXFgZ/An97E3Y/BYciEbBlOti3BWW', 'CARERECIVIER'),
        ('1949-11-07', 1015, 1015, 'Recebedor de cuidados com doença degenerativa', 'Manoel Pereira', 'fatima@gmail.com', 'Renato', 'doença degenerativa', 'Gomes', '$2a$10$JTjKRDP3HEqwC.JgOzNNiOuIZXFgZ/An97E3Y/BYciEbBlOti3BWW', 'CARERECIVIER'),
        ('1949-11-07', 1012, 1012, 'Recebedor de cuidados com limitações físicas', 'Antônio Oliveira', 'luisa@gmail.com', 'Mariana', 'limitações físicas', 'Almeida', '$2a$10$JTjKRDP3HEqwC.JgOzNNiOuIZXFgZ/An97E3Y/BYciEbBlOti3BWW', 'CARERECIVIER');

insert into public.experience (end_date, start_date, caregiver_id, id, company, description)
values  ('2022-05-10', '2017-03-21', 1009, 1013, 'Cuidador Sempre', 'Enfermeiro em centro de reabilitação, auxiliando na recuperação dos pacientes'),
        ('2022-05-10', '2017-03-21', 1007, 1008, 'Cuidador Sempre', 'Enfermeiro em hospital de trauma, fornecendo cuidados imediatos e de emergência'),
        ('2022-05-10', '2015-01-15', 1008, 1011, 'Cuidadores Unidos', 'Enfermeiro em centro cirúrgico, garantindo a segurança durante procedimentos'),
        ('2022-05-10', '2019-05-27', 1008, 1010, 'Amor e Cuidado', 'Enfermeiro em clínica pediátrica, atendendo crianças com carinho e dedicação'),
        ('2022-05-10', '2018-04-24', 1010, 1014, 'Cuidar e Companhia', 'Enfermeiro em casa de repouso, proporcionando cuidados e conforto aos idosos'),
        ('2022-05-10', '2016-02-18', 1009, 1012, 'Cuidador em Casa', 'Enfermeiro de saúde mental, prestando apoio emocional e cuidados terapêuticos'),
        ('2022-05-10', '2019-05-27', 1010, 1015, 'Amor e Cuidado', 'Enfermeiro em oncologia, acompanhando pacientes durante o tratamento contra o câncer'),
        ('2022-05-10', '2015-01-15', 1006, 1006, 'Cuidadores Unidos', 'Enfermeiro especializado em cuidados intensivos'),
        ('2022-05-10', '2018-04-24', 1007, 1009, 'Cuidar e Companhia', 'Enfermeiro de unidade de terapia intensiva, gerenciando pacientes críticos'),
        ('2022-05-10', '2016-02-18', 1006, 1007, 'Cuidador em Casa', 'Enfermeiro de saúde da família comprometido com o bem-estar dos pacientes');

insert into public.service (duration, price, caregiver_id, id, description, name)
values  (2.5, 60, 1006, 1006, 'Serviço de cuidados personalizados para pacientes com doença de Parkinson', 'Cuidado especializado para Parkinson'),
        (3, 80, 1006, 1007, 'Serviço de cuidados abrangentes para pacientes em recuperação pós-operatória', 'Cuidado pós-operatório'),
        (2, 40, 1008, 1011, 'Serviço de cuidados personalizados para pacientes com doença respiratória crônica', 'Cuidado respiratório'),
        (3, 80, 1009, 1012, 'Serviço de cuidados especializados para pacientes em cuidados paliativos', 'Cuidado paliativo'),
        (2.5, 60, 1009, 1013, 'Serviço de cuidados em casa para pacientes idosos', 'Cuidado domiciliar para idosos'),
        (2.5, 60, 1010, 1015, 'Serviço de cuidados personalizados para pacientes com doença cardíaca', 'Cuidado cardíaco'),
        (2.5, 60, 1008, 1010, 'Serviço de cuidados em casa para pacientes em reabilitação', 'Cuidado domiciliar de reabilitação'),
        (2.5, 60, 1007, 1008, 'Serviço de acompanhamento de consultas médicas para pacientes idosos', 'Acompanhamento de consultas'),
        (3, 80, 1007, 1009, 'Serviço de cuidados especializados para pacientes com diabetes', 'Cuidado para diabetes'),
        (3, 80, 1010, 1014, 'Serviço de acompanhamento de consultas médicas para pacientes com necessidades especiais', 'Acompanhamento de consultas para necessidades especiais');

insert into public.booking (caregiver_id, carerecivier_id, end_date_time, id, services_id, start_date_time)
values  (1006, 1011, '2023-07-10 14:30:00.000000', 1006, 1006, '2023-07-10 12:00:00.000000'),
        (1006, 1012, '2023-07-11 15:30:00.000000', 1007, 1007, '2023-07-11 12:30:00.000000'),
        (1008, 1013, '2023-07-12 14:30:00.000000', 1008, 1008, '2023-07-12 12:00:00.000000'),
        (1008, 1014, '2023-07-13 15:30:00.000000', 1009, 1009, '2023-07-13 12:30:00.000000'),
        (1010, 1015, '2023-07-14 14:30:00.000000', 1010, 1010, '2023-07-14 12:00:00.000000'),
        (1011, 1011, '2023-07-15 14:30:00.000000', 1011, 1011, '2023-07-15 12:00:00.000000'),
        (1011, 1012, '2023-07-16 15:30:00.000000', 1012, 1012, '2023-07-16 12:30:00.000000'),
        (1013, 1013, '2023-07-17 14:30:00.000000', 1013, 1013, '2023-07-17 12:00:00.000000'),
        (1013, 1014, '2023-07-18 15:30:00.000000', 1014, 1014, '2023-07-18 12:30:00.000000'),
        (1015, 1012, '2023-07-19 14:30:00.000000', 1015, 1015, '2023-07-19 12:00:00.000000'),
        (1006, 1011, '2023-07-20 12:30:00.000000', 1016, 1006, '2023-07-20 10:00:00.000000'),
        (1007, 1012, '2023-07-21 14:00:00.000000', 1017, 1007, '2023-07-21 11:00:00.000000'),
        (1008, 1013, '2023-07-22 15:30:00.000000', 1018, 1008, '2023-07-22 13:00:00.000000'),
        (1009, 1014, '2023-07-23 12:00:00.000000', 1019, 1009, '2023-07-23 09:30:00.000000'),
        (1010, 1015, '2023-07-24 13:00:00.000000', 1020, 1010, '2023-07-24 10:30:00.000000'),
        (1006, 1011, '2023-07-25 16:30:00.000000', 1021, 1011, '2023-07-25 14:00:00.000000'),
        (1007, 1012, '2023-07-26 14:00:00.000000', 1022, 1012, '2023-07-26 11:30:00.000000'),
        (1008, 1013, '2023-07-27 11:30:00.000000', 1023, 1013, '2023-07-27 09:00:00.000000'),
        (1009, 1014, '2023-07-28 16:00:00.000000', 1024, 1014, '2023-07-28 13:30:00.000000'),
        (1010, 1015, '2023-07-29 12:30:00.000000', 1025, 1015, '2023-07-29 10:00:00.000000');