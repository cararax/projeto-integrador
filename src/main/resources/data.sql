

INSERT INTO public.caregiver (city_id, id, description, email, firstname, lastname, password, role) VALUES (10000, 10003, 'pedro pedro pedro pedro pedro pedro', 'pedro@carara.com', 'pedro', 'carara', '1234', 'CAREGIVER');
INSERT INTO public.caregiver (city_id, id, description, email, firstname, lastname, password, role) VALUES (10001, 10004, 'enfermeiro enfermeiro enfermeiro enfermeiro enfermeiro enfermeiro', 'enfermeiro@enfermeiro.com', 'enfermeiro', 'enfermeiro', '1234''', 'CAREGIVER');
INSERT INTO public.caregiver (city_id, id, description, email, firstname, lastname, password, role) VALUES (10002, 10005, 'outro enfermeiro outro enfermeiro outro enfermeiro', 'outro@enfermeiro.com', 'outro', 'enfermeiro', '1234', 'CAREGIVER');


INSERT INTO public.carerecivier (eldery_bith_date, city_id, id, description, eldery_name, email, firstname, health_details, lastname, password, role) VALUES ('1955-05-11', 10000, 10006, 'responsavel responsavel responsavel responsavel responsavel', 'idoso', 'responsavel@responsavel.com', 'responsavel', 'alergia a dipirona', 'responsavel', '1234', 'CARERECIVIER');
INSERT INTO public.carerecivier (eldery_bith_date, city_id, id, description, eldery_name, email, firstname, health_details, lastname, password, role) VALUES ('9044-05-15', 10002, 10007, 'outro responsavel outro responsavel outro responsavel outro responsavel', 'outro idoso', 'outro@responsavel.com', 'outro', 'sem alergias, pouco lucido', 'responsavel', '1234', 'CARERECIVIER');

INSERT INTO public.experience (end_date, start_date, caregiver_id, id, company, description) VALUES ('2023-05-10', '2021-05-01', 10003, 10011, 'husm', 'fui enfermeiro tal tal tal');
INSERT INTO public.experience (end_date, start_date, caregiver_id, id, company, description) VALUES ('2023-05-18', '2021-04-28', 10005, 10012, 'enfermeiro diretor', 'fui enfermeiro e diretor de hospital....');
INSERT INTO public.experience (end_date, start_date, caregiver_id, id, company, description) VALUES ('2023-05-16', '2023-04-03', 10004, 10013, 'exemplo', 'descrição');

INSERT INTO public.service (duration, price, caregiver_id, id, description, name) VALUES (12, 100, 10003, 10008, 'plantao noturno plantao noturno', 'plantao noturno');
INSERT INTO public.service (duration, price, caregiver_id, id, description, name) VALUES (12, 120, 10004, 10009, 'plantao diurno plantao diurno plantao diurno', 'plantao diurno');
INSERT INTO public.service (duration, price, caregiver_id, id, description, name) VALUES (1, 200, 10005, 10010, 'outro serviço', 'outro serviço');

INSERT INTO public.booking (caregiver_id, carerecivier_id, end_date_time, id, services_id, start_date_time) VALUES (10003, 10006, '2023-05-18 12:00:00.000000', 10014, 10009, '2023-03-28 12:00:00.000000');
INSERT INTO public.booking (caregiver_id, carerecivier_id, end_date_time, id, services_id, start_date_time) VALUES (10005, 10006, '2023-05-25 12:00:00.000000', 10015, 10008, '2023-05-16 12:00:00.000000');


