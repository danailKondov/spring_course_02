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

Тесты можно реализовать с помощью spring-boot-starter-embedded-mongodb 

решение: см. ветку _migrate_to_mongodb_


## CRUD приложение с Web UI и хранением данных в БД

Создайте приложение с хранением сущностей в БД (можно взять DAOs из прошлых занятий)

Использовать классический View, предусмотреть страницу отображения всех сущностей и создания/редактирования.

View на Thymeleaf, classic Controllers. 

решение: см. ветку _crud_web_ui_


## Переписать приложение с использованием AJAX и REST-контроллеров

Переписать приложение с классических View на AJAX архитектуру и REST-контроллеры.

Сделать SPA приложение на любом из Web-фреймворков (React)

решение: см. _crud_rest_react_


## Использовать WebFlux

Вместо классического потока и embedded Web-сервера использован WebFlux.
 
Для избавления от блокирующих элементов база данных (снова) переведена на MongoDB.

решение: см. _migrate_to_web_flux_


## В CRUD Web-приложение добавить механизм аутентификации

В существующее CRUD-приложение добавлен Spring Security и механизм Form-based аутентификации.

решение: см. _add_form_based_auth_


## Настроить в приложении авторизацию на уровне URL и/или доменных сущностей. 

В приложение добавлен кастомный security expression

решение: см. _add_custom_security_expression_