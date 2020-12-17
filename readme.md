# Tutor Chat BackEnd

Приложение служит backEnd частью для чата разработанного в рамках курсовой работы.<br/>
Приложение призвано ~~упростить~~ коммуникацию между студентами и тьюторами.

## Организация разработки проекта

Использвуется CD pipeline с использованием Jenkins (см. [Jenkinsfile](./Jenkinsfile)) и Docker (см. [Dockerfile](./Dockerfile)).<br/>
Для облегчения работы с Докером исползуется [Gradle](https://gradle.org/) Docker plugin (см. [build.gradle](./build.gradle)).

Ссылки на ресурсы:

- [Swagger Api документация][0] (ссылка ведет на запущенное backEnd приложение)
- [Консоль базы данных][1] (для входа нажать `Connect`)
- [Jenkins приложение][2]
- [Docker image][3]

## Функционал и технологии проекта

1. Для простоты разработки была использована in memory База данных - H2. В качестве ORM использован Hibernate.
2. Авторизация  посредствам JWT (см. [JwtAuthFilter][5]) (опционально можно использовать связку из двух токенов JWT + refresh token).
3. Функциоанал чата поддерживает следующие возможности (см. [ChatController][6], [UserController][7])
    - Отправлять сообщения в групповой чат или Direct
    - Оставлять реакции к сообщениям
    - Проводить голосования
    
    Для реализации уведомления клиентов о получении нового сообщения/реакции/голосования был использован протокол [STOMP][4] поверх WebSocket'ов.


[0]: http://168.119.101.26:8081/swagger-ui.html
[1]: http://168.119.101.26:8081/h2-console
[2]: http://168.119.101.26:8080/job/tutor-chat/
[3]: https://hub.docker.com/repository/docker/kilev/tutorchat-backend
[4]: http://stomp.github.io/stomp-specification-1.2.html

[5]: /src/main/java/com/kil/tutor/security/JwtAuthFilter.java
[6]: /src/main/java/com/kil/tutor/controller/ChatController.java
[7]: /src/main/java/com/kil/tutor/controller/UserController.java
