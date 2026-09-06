# Transfers Microservice

## Описание

`Transfers Microservice` — микросервис для хранения банковских счетов (контрактов) и переводов между ними.

Микросервис отвечает за:
- создание нового пустого контракта (счёта);
- получение информации о контракте по имени;
- создание перевода между двумя счетами с проверкой блокировок пользователя во внешнем сервисе;
- получение истории транзакций по имени счёта.

---

## Модели данных

### Contract (Счёт)

| Поле           | Тип          | Описание                                     |
|----------------|--------------|----------------------------------------------|
| `id`           | `UUID`       | Уникальный идентификатор                     |
| `contractName` | `String`     | Уникальное имя счёта (генерируется сервисом) |
| `createdDate`  | `Instant`    | Дата создания                                |
| `balance`      | `BigDecimal` | Баланс счёта                                 |

### Transaction (Транзакция)

| Поле               | Тип          | Описание                 |
|--------------------|--------------|--------------------------|
| `id`               | `UUID`       | Уникальный идентификатор |
| `sourceContractId` | `UUID`       | ID счёта отправителя     |
| `targetContractId` | `UUID`       | ID счёта получателя      |
| `amount`           | `BigDecimal` | Сумма перевода           |
| `description`      | `String`     | Описание платежа         |
| `createdAt`        | `Instant`    | Дата создания транзакции |

---

## REST API

### 1. Создание нового контракта

**POST** `/api/v1/transfers/contract`

Параметры: не требуются.
Контракту автоматически присваивается имя, баланс устанавливается в `0`.

Пример ответа:

```json
{
  "contractName": "CN1234567890RU116",
  "createdDate": "2026-03-22T15:00:19.637Z",
  "balance": 0
}
```

---

### 2. Получение контракта по имени

**GET** `/api/v1/transfers/contract/{name}`

Параметры: String name - название контракта(счёта)

Пример:

`GET /api/v1/transfers/contract/CN1234567890RU116`

Пример ответа:

```json
{
  "contractName": "CN1234567890RU116",
  "createdDate": "2026-03-22T15:00:19.637Z",
  "balance": 160000
}
```

---

### 3. Создание перевода

**POST** `/api/v1/transfers/transactions`

Параметры: не требуются.

Перед созданием перевода сервис проверяет, не заблокированы ли пользователи
во внешнем `UserRestrictions` микросервисе (по `USER_RESTRICTIONS_URL`).

Пример запроса (TransactionRequest):

```json
{
  "sourceContractName": "CNDD98D151E9RU116",
  "targetContractName": "CN659C1C51F4RU116",
  "amount": 10000,
  "description": "Перевод за проезд"
}
```

Пример ответа:

```json
{
  "sourceContractId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "targetContractId": "4fa85f64-5717-4562-b3fc-2c963f66afa7",
  "amount": 10000,
  "description": "Перевод за проезд"
}
```

---

### 4. История транзакций по счёту

**GET** `/api/v1/transfers/transactions/{name}`

Параметры: String name - название контракта(счёта).

Пример:

`GET /api/v1/transfers/transactions/CN1234567890RU116`

Пример ответа:

```json
{
  "contractName": "CN1234567890RU116",
  "from": "2026-03-22T15:00:26.760Z",
  "to": "2026-03-22T15:00:26.760Z",
  "transactions": [
    {
      "sourceContractId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "targetContractId": "4fa85f64-5717-4562-b3fc-2c963f66afa7",
      "amount": 10000,
      "description": "Перевод за проезд"
    }
  ]
}
```

---

## Что должно работать после запуска

После запуска сервиса доступны:

- `POST /api/v1/transfers/contract` — создание счёта
- `GET /api/v1/transfers/contract/{name}` — получение счёта по имени
- `POST /api/v1/transfers/transactions` — создание перевода
- `GET /api/v1/transfers/transactions/{name}` — история транзакций по счёту

Внутри контейнера сервис слушает порт **8080**.
Снаружи можно пробросить, например, на **8083**:

```
-p 8083:8080
```

---

## Настройки подключения

Подключение к БД и адрес внешнего микросервиса задаются через переменные окружения в `application.properties`:

```properties
db.url=${DB_URL}
db.username=${DB_USERNAME}
db.password=${DB_PASSWORD}
db.hikari.max-pool-size=${DB_POOL_SIZE}
db.driver.classname=org.postgresql.Driver

user-restrictions.url=${USER_RESTRICTIONS_URL}
cards-service.url=${CARDS_SERVICE_URL}
```

### ENV-переменные

| Переменная              | Описание                        | Пример значения                                     |
|-------------------------|---------------------------------|-----------------------------------------------------|
| `DB_URL`                | JDBC URL базы данных            | `jdbc:postgresql://db:5432/transfer-db`             |
| `DB_USERNAME`           | Пользователь БД                 | `postgres`                                          |
| `DB_PASSWORD`           | Пароль БД                       | `secret`                                            |
| `DB_POOL_SIZE`          | Размер пула соединений HikariCP | `6`                                                 |
| `USER_RESTRICTIONS_URL` | URL микросервиса блокировок     | `http://user-restrictions:8081/api/v1/restrictions` |
| `CARDS_SERVICE_URL`     | URL микросервиса карт           | `http://cards-service:8084//api/v2/cards`           |

---

## Внешние зависимости

Сервис зависит от внешнего микросервиса блокировок пользователей `UserRestrictions` и `CardsService`.

Используемые эндпоинты:

`GET /api/v2/cards/by-contract/{contract}`

Параметры: String contract - название контракта(счёта).

Пример ответа от `CardsService`:

```json
{
  "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "plasticName": "Ivanov Ivan Ivanovich",
  "contractName": "CN1234567890RU116",
  "pan": "1111222233334444",
  "expDate": "3003",
  "cvv": "989",
  "cardProduct": {
    "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "cardName": "Visa Digital All In",
    "description": "Кешбек на все категории 120%",
    "cardImageLink": "https://img.freepik.com/premium-photo/credit-card-is-black-color-white-background_725455-354.jpg"
  },
  "userId": "3fa85f64-5717-4562-b3fc-2c963f66afa6"
}
```
Из этого ответа мы берем userId и дальше идем в endpoint `UserResctrictions` чтобы проверить не заблокированы ли пользователи.

---

`GET /api/v1/restrictions/{id}`

Параметры: String userId - id пользователя в формате UUID

Пример ответа от `UserRestrictions`:

```json
{
  "userId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "blockType": "PermanentBlock",
  "blocked": true
}
```

Если пользователь заблокирован (`blocked: true`), перевод не будет выполнен.

---

## Требования к базе данных

Имя базы данных: **transfer-db**
Тип: **PostgreSQL**

Если база данных не настроена или таблицы не созданы, сервис вернёт **500 Internal Server Error**.  
Схема автоматически инициализируется вместе с запуском docker-compose, но должна быть папка с файлом db-init/init.sql  
Схема БД:

```sql
CREATE TABLE IF NOT EXISTS contracts (
    id            UUID PRIMARY KEY,
    contract_name VARCHAR(255)   NOT NULL UNIQUE,
    created_date  TIMESTAMP    NOT NULL,
    balance       NUMERIC(19, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS transactions (
    id                  UUID PRIMARY KEY,
    source_contract_id  UUID           NOT NULL REFERENCES contracts(id),
    target_contract_id  UUID           NOT NULL REFERENCES contracts(id),
    amount              NUMERIC(19, 2) NOT NULL,
    description         TEXT,
    created_at          TIMESTAMP    NOT NULL
);
```

---

# Имя Docker image: transfers-service:1.0
### Ссылка на образ в dockerHub: https://hub.docker.com/repository/docker/shozha/transfers-service

## Запуск проекта через docker compose

```bash
# первый запуск инициализирует БД по init.sql который лежит в /db-init
docker compose up -d
```

Папка с схемой бд обязательно должна быть рядом с docker-compose.

Пример `.env` файла для compose:

```env
DB_URL=jdbc:postgresql://db:5432/transfer-db
DB_USERNAME=postgres
DB_PASSWORD=secret
DB_POOL_SIZE=6
USER_RESTRICTIONS_URL=http://user-restrictions:8081/api/v1/restrictions
CARDS_SERVICE_URL=http://cards-service:8080/api/v2/cards
```

После запуска REST API будет доступен по адресу:

`http://localhost:8083/api/v1/transfers/...`
