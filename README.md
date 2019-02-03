# spring_course_02

Домашние задания в рамках курса по Spring


## Создать приложение хранящее информацию о книгах в билиотеке

Использовать Spring JDBC и реляционную базу.

Опционально использовать настоящую реляционную БД, но можно использовать H2.

Предусмотреть таблицы авторов, книг и жанров.

Покрыть тестами, насколько это возможно.
 
решение: см. ветку _create_library_


## Переписать приложение для хранения книг на ORM

Использовать JPA, Hibernate только в качестве JPA-провайдера.

Добавить комментарии к книгам, и высокоуровневые сервисы, оставляющие комментарии к книгам.

Покрыть DAO тестами используя H2 базу данных и соответствующий H2 Hibernate-диалект 

решение: см. ветку _migrate-to-jpa_


## Билиотеку на Spring Data JPA

Реализовать весь функционал работы с БД в приложении книг с использованием spring-data-jpa репозиториев. 

решение: см. ветку _migrate_to_spring_data_repo_


## Использовать MongoDB и spring-data для хранения информации о книгах

Тесты можно реализовать с помощью spring-boot-strter-embedded-mongodb 

решение: см. ветку _migrate_to_mongodb_


## CRUD приложение с Web UI и хранением данных в БД

Создайте приложение с хранением сущностей в БД (можно взять DAOs из прошлых занятий)

Использовать классический View, предусмотреть страницу отображения всех сущностей и создания/редактирования.

View на Thymeleaf, classic Controllers. 

решение: см. ветку _crud_web_ui_
