INSERT INTO users(name)
VALUES ('Тимур'),
       ('Артем'),
       ('Евгений'),
       ('Светлана'),
       ('Кристина');


INSERT INTO file(name, file_path)
VALUES ('sound', 'my_computer/sounds'),
       ('image_1', 'my_computer/images'),
       ('text', 'my_computer/texts'),
       ('java-code', 'my_computer/applications'),
       ('script', 'my_computer/scripts');


INSERT INTO event(user_id, file_id)
VALUES
    (1,2),
    (2,2),
    (4,3),
    (3,2),
    (5,1),
    (2,1),
    (3,4);
