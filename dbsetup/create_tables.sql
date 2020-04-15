USE traininig_akka_java_1_preparation;

CREATE TABLE ticket_stocks (
  `ticket_id` INT NOT NULL,
  `quantity` INT NOT NULL,
  PRIMARY KEY(`ticket_id`)
  -- FOREIGN KEY(`ticket_id`) REFERENCES tickets(`id`),
) ENGINE=InnoDB;

CREATE TABLE orders (
  `id` INT NOT NULL AUTO_INCREMENT,
  `ticket_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  `quantity` INT NOT NULL,
  PRIMARY KEY(`id`),
  FOREIGN KEY(`ticket_id`) REFERENCES ticket_stocks(`ticket_id`),
  -- 本来ならticketsテーブルがあるはずで、以下のようになる
  -- FOREIGN KEY(`ticket_id`) REFERENCES tickets(`id`)
  -- また、usersテーブルもあるはず
  -- FOREIGN KEY(`user_id`) REFERENCES users(`id`)
) ENGINE=InnoDB;
