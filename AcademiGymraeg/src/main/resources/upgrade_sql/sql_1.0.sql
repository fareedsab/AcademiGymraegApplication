CREATE SCHEMA `pos_sys` ;
CREATE TABLE users (
                       id int PRIMARY KEY AUTO_INCREMENT,
                       username VARCHAR(100) NOT NULL UNIQUE,
                       fullname VARCHAR(100) NOT NULL ,
                       password VARCHAR(100) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       roleId int NOT NULL,
                       isActive BOOLEAN DEFAULT TRUE,
                       createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE TABLE userRole (
                          id int PRIMARY KEY AUTO_INCREMENT,
                          roleName varchar(100) not null,
                          isActive BOOLEAN DEFAULT TRUE,
                          createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE TABLE questionType (
                              id int PRIMARY KEY AUTO_INCREMENT,
                              questionTypeName varchar(500) not null,
                              questionTypeValue varchar(500) not null,
                              isActive BOOLEAN DEFAULT TRUE,
                              createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE TABLE nounGender (
                            id int PRIMARY KEY AUTO_INCREMENT,
                            genderName varchar(100) not null,
                            created_by int NOT NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE nouns (
                       id int PRIMARY KEY AUTO_INCREMENT,
                       welsh_noun VARCHAR(100) NOT NULL,
                       english_noun VARCHAR(100) NOT NULL,
                       nounGenderId int not null,
                       isActive int not null default 0,
                       created_by int NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       FOREIGN KEY (nounGenderId) REFERENCES nounGender(id)
);
alter table nouns add column updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
alter table nouns add column updated_by int default 0;


CREATE TABLE testResult  (
                             id int PRIMARY KEY AUTO_INCREMENT,
                             userId int not null,
                             testScore int not Null default 0,
                             isActive int not null default 0,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             completed_at TIMESTAMP ,
                             isCompleted int default 0,
                             FOREIGN KEY (userId) REFERENCES users(id)
);


CREATE TABLE testResultDetails  (
                                    id int PRIMARY KEY AUTO_INCREMENT,
                                    questionText varchar(500),
                                    systemAnswer varchar(500),
                                    userAnswer varchar(500),
                                    isCorrect boolean default false,
                                    isActive boolean default false,
                                    isSubmitted boolean default false,
                                    questionTypeId int ,
                                    nounId int ,
                                    resultId int,
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    completed_at TIMESTAMP ,
                                    FOREIGN KEY (nounId) REFERENCES nouns(id),
                                    FOREIGN KEY (questionTypeId) REFERENCES questionType(id),
                                    FOREIGN KEY (resultId) REFERENCES testResult(id)
);
Alter table nouns add column updated_by int not null;