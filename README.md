# RabbitMQ Messaging System - Студенческий проект

## Что это?
Простая система обмена сообщениями между двумя сервисами через RabbitMQ.
- **Order Service** - принимает заказы и отправляет их в RabbitMQ
- **Notification Service** - получает заказы из RabbitMQ и выводит в консоль

**Структура проекта (очень простая!):**
```
rabbitmq-system/
├── docker-compose.yml
├── test-requests.http
├── order-service/
│   ├── pom.xml
│   └── rabbitmq/orderservice/
│       ├── OrderServiceApplication.java      ← главный класс
│       ├── OrderController.java              ← принимает HTTP запросы
│       ├── OrderPublisher.java               ← отправляет в RabbitMQ
│       ├── OrderDTO.java                     ← данные заказа
│       └── application.properties            ← настройки
└── notification-service/
    ├── pom.xml
    └── rabbitmq/notificationservice/
        ├── NotificationServiceApplication.java   ← главный класс
        ├── OrderNotificationListener.java        ← слушает RabbitMQ
        ├── OrderDTO.java                         ← данные заказа
        └── application.properties                ← настройки
```

---

## Как запустить (3 шага)

### ⚠️ Требования: Maven

Если при запуске видите ошибку `mvn: command not found`, установите Maven:

```bash
# 1. Установить Homebrew (если еще нет)
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# 2. Установить Maven
brew install maven

# 3. Проверить установку
mvn -version
```

---

### ШАГ 1: Запустить RabbitMQ
Откройте терминал и выполните:
```bash
cd /Users/nurseit.shukenovapple.kz/Desktop/rabbitmq-system
docker-compose up
```
**Оставьте этот терминал открытым!**

Проверить: откройте http://localhost:15672 (логин/пароль: guest/guest)

---

### ШАГ 2: Запустить Notification Service
Откройте **НОВЫЙ терминал** и выполните:
```bash
cd /Users/nurseit.shukenovapple.kz/Desktop/rabbitmq-system/notification-service
mvn spring-boot:run
```
**Оставьте этот терминал открытым!**

Подождите, пока увидите: `Started NotificationServiceApplication`

---

### ШАГ 3: Запустить Order Service
Откройте **ЕЩЁ ОДИН новый терминал** и выполните:
```bash
cd /Users/nurseit.shukenovapple.kz/Desktop/rabbitmq-system/order-service
mvn spring-boot:run
```

Подождите, пока увидите: `Started OrderServiceApplication`

---

## Как протестировать

Откройте **ещё один терминал** и отправьте тестовый заказ:

```bash
curl -X POST http://localhost:8080/order/almaty \
  -H "Content-Type: application/json" \
  -d '{
    "restaurant": "KFC",
    "courier": "Иван",
    "foods": ["Пицца", "Бургер"],
    "status": "Новый"
  }'
```

**Что должно произойти:**
1. В терминале Order Service увидите: `Order sent to region almaty`
2. В терминале Notification Service увидите:
   ```
   Received Almaty order: OrderDTO(...)
   Received (common listener) order: OrderDTO(...)
   ```

---

## Как это работает (для защиты)

### 1. Order Service (порт 8080)
- Получает POST-запрос с данными заказа
- Отправляет заказ в RabbitMQ Exchange `order-topic-exchange`
- Использует routing key `order.{регион}`

**Основные классы:**
- `OrderController` - принимает HTTP запросы
- `OrderPublisher` - отправляет в RabbitMQ
- `OrderDTO` - данные заказа

### 2. Notification Service (порт 8081)
- Слушает 3 очереди:
  - `almaty_orders_queue` → заказы из Алматы
  - `astana_orders_queue` → заказы из Астаны
  - `common_orders_queue` → ВСЕ заказы (routing key: `order.#`)
- Выводит полученные заказы в консоль

**Основные классы:**
- `OrderNotificationListener` - слушает RabbitMQ и обрабатывает сообщения
- `OrderDTO` - данные заказа

### 3. RabbitMQ
- **Topic Exchange** - маршрутизирует сообщения по ключам
- **Routing Keys**: `order.almaty`, `order.astana`, `order.#` (все)

---

## Примеры для тестирования

### Заказ в Алматы:
```bash
curl -X POST http://localhost:8080/order/almaty \
  -H "Content-Type: application/json" \
  -d '{"restaurant": "KFC", "courier": "Иван", "foods": ["Пицца"], "status": "Новый"}'
```

### Заказ в Астане:
```bash
curl -X POST http://localhost:8080/order/astana \
  -H "Content-Type: application/json" \
  -d '{"restaurant": "Burger King", "courier": "Петр", "foods": ["Бургер"], "status": "Новый"}'
```

### Заказ в Шымкенте:
```bash
curl -X POST http://localhost:8080/order/shymkent \
  -H "Content-Type: application/json" \
  -d '{"restaurant": "Dodo Pizza", "courier": "Алекс", "foods": ["Лагман"], "status": "Новый"}'
```

---

## Технологии
- **Java 17**
- **Spring Boot 3.1.5**
- **Spring AMQP** (для работы с RabbitMQ)
- **RabbitMQ** (брокер сообщений)
- **Docker** (для запуска RabbitMQ)
- **Maven** (сборка проекта)

---

## Вопросы преподавателя - готовые ответы

**Q: Что такое RabbitMQ?**
- Брокер сообщений для асинхронного обмена данными между сервисами

**Q: Что такое Topic Exchange?**
- Тип exchange, который маршрутизирует сообщения по routing key (как DNS)

**Q: Зачем нужны очереди?**
- Для хранения сообщений до их обработки получателем

**Q: Что такое routing key?**
- Ключ маршрутизации сообщения. Например: `order.almaty`

**Q: Что означает `order.#`?**
- Wildcаrd паттерн, который ловит ВСЕ сообщения, начинающиеся с `order.`

**Q: Почему микросервисы?**
- Разделение ответственности: один сервис создаёт заказы, другой обрабатывает

---

## Остановка проекта

1. В терминалах с сервисами нажмите `Ctrl+C`
2. Остановите RabbitMQ:
   ```bash
   docker-compose down
   ```
