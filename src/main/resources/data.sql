insert into users (user_name) values ('John Deo');
insert into users (user_name) values ('Mary Kim');
insert into users (user_name) values ('Justin Denim');
insert into users (user_name) values ('Kristen John');
insert into users (user_name) values ('John Podesto');

insert into tasks (name, desc, status, user_id) values ('Code Review Dolphin', 'Code review for the project dolphin', 'Not Started', 1);
insert into tasks (name, desc, status, user_id) values ('Test Cases Review', 'Review the test cases and approve the check-in', 'Completed', 2);
insert into tasks (name, desc, status, user_id) values ('API Integration Swagger', 'Integrate the open source Swagger API', 'In Progress', 3);
insert into tasks (name, desc, status, user_id) values ('Build Resolve', 'Build resolve on project cake', 'In Progress', 1);
insert into tasks (name, desc, status, user_id) values ('Interview Developer', 'Interview developers for new position', 'Not Started', 4);
insert into tasks (name, desc, status, user_id) values ('Interview Developer', 'Interview developers for new position', 'In Progress', 3);
insert into tasks (name, desc, status, user_id) values ('Meet at Hackathon', 'Meet people during the hackthon event', 'Completed', 5);
insert into tasks (name, desc, status, user_id) values ('Meeting with QA', 'Meet with QA to discuss about some of development bugs', 'Not Started', 4);
insert into tasks (name, desc, status, user_id) values ('Research on MicroServices', 'Research the requirements and implementation of microservices', 'Not Started', 5);
insert into tasks (name, desc, status, user_id) values ('Research on MicroServices', 'Research the requirements and implementation of microservices', 'In Progress', 2);

insert into todolists (name) values ('Code Reviews ToDoList');
insert into todolists (name) values ('API ToDoList');
insert into todolists (name) values ('Interview ToDoList');
insert into todolists (name) values ('Managers ToDoList');
insert into todolists (name) values ('Developers ToDoList');

insert into todolist_task (todolist_id, task_id) values ('1', '1');
insert into todolist_task (todolist_id, task_id) values ('1', '3');
insert into todolist_task (todolist_id, task_id) values ('1', '6');
insert into todolist_task (todolist_id, task_id) values ('2', '9');
insert into todolist_task (todolist_id, task_id) values ('2', '4');
insert into todolist_task (todolist_id, task_id) values ('2', '3');
insert into todolist_task (todolist_id, task_id) values ('3', '6');
insert into todolist_task (todolist_id, task_id) values ('4', '10');
insert into todolist_task (todolist_id, task_id) values ('5', '8');