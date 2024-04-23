DROP DATABASE IF EXISTS accommodation;
create database if not exists accommodation;
use accommodation;

DROP TABLE IF EXISTS listing;

CREATE TABLE IF NOT EXISTS listing (
  id int PRIMARY KEY AUTO_INCREMENT,
  name varchar(255),
  email varchar(255),
  phone varchar(15),
  address varchar(255),
  eircode varchar(10),
  distance decimal(4,2),
  room_type varchar(50),
  duration_stay varchar(20),
  rent int,
  bills varchar(255),
  gender_preference varchar(20),
  add_message text,
  image varchar(255)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO listing VALUES
(null, 'John', 'john@gmail.com', '012345', '4 Dublin Road', 'N37000', '1.5', 'Double room', '7 days a week', '500', 'Not included', 'No preference', 'Be ok with pets', 'house2.png'),
(null, 'Mary', 'mary@gmail.com', '543210', '1 Dublin Road', 'N37000', '3', 'Double room', '5 days a week', '600', 'All included', 'No preference', 'Own transport needed', 'house3.png'),
(null, 'Lucy', 'lucy@gmail.com', '543210', 'Cypress Garden', 'N37000', '0.5', 'Double room', '7 days a week', '800', 'All included', 'No preference', 'Own transport needed', 'castle.png');
