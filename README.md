# IWanit

A reverse marketplace: a buyer publishes an Article describing something they want to buy, and
sellers answer with Offers. The buyer accepts one offer and pays it.

- `iwanit/` – backend: Spring Boot 4, Java 21, PostgreSQL, JWT authentication
- `iwanit_front/` – frontend: Vue 3, Vite, Tailwind CSS
- `docker/` – database init scripts used by Docker Compose

## Domain rules

### Users, buyers and sellers

- Every user registers with a single role: `BUYER`, `SELLER` or `ADMIN`.
- A Buyer or a Seller is the profile of a User, in a one-to-one relation. A user with the `BUYER`
  role has a Buyer profile and a user with the `SELLER` role has a Seller profile.
- Users can only read, update or delete their own Buyer or Seller profile. Only admins can list
  all buyers or all sellers.

### Articles and offers

- A User doesn't create Articles or Offers directly:
  - Buyer -> creates an Article (`ArticleService`)
  - Seller -> creates an Offer related to one Article (`OfferService`)
- A Buyer can have multiple Articles. A Seller can have multiple Offers, but only one per Article.
- A Buyer can update or delete their Articles while they are `AVAILABLE`.
- A Seller can update their Offers while they are `PENDING`. An Offer can be deleted by its Seller
  or by the Buyer of the Article.
- Sellers can only make offers on `AVAILABLE` articles.

### Accepting an offer

- The Buyer of an Article accepts one of its Offers (`AcceptedOfferService`). An Article can only
  have one accepted Offer.
- When an Offer is accepted it goes to `ACCEPTED`, the Article goes to `BOUGHT` and the rest of
  the pending Offers of the Article go to `REJECTED`.

### Payments

- The Buyer pays the Offer they accepted (`PaymentService`). The paid amount is copied from the
  Offer when the payment is created.
- A new Payment is created for every attempt, so a buyer can retry after a failed one, but an
  Offer can only have one `COMPLETED` payment.
- Payments go through Stripe (work in progress): a payment is created as `PENDING` and the Stripe
  webhook will move it to `COMPLETED` or `FAILED`.

## Running the project

### With Docker Compose

1. Copy `.env.example` to `.env` and fill in the values (see [Environment variables](#environment-variables)).
2. Start everything:

   ```bash
   docker compose up --build
   ```

| Service  | URL                     |
| -------- | ----------------------- |
| Frontend | http://localhost:3000   |
| Backend  | http://localhost:8080   |
| Postgres | `localhost:5433`        |

Hibernate creates and updates the tables (`ddl-auto=update`), but it doesn't change the type of
columns that already exist. To start again from an empty database:

```bash
docker compose down -v && docker compose up --build
```

This deletes all the data, users included.

### Backend outside Docker

`application.properties` points to Postgres on `localhost:5432`, while Docker Compose exposes it on
`5433`. Change one of the two ports if you run the backend with Docker's database.

```bash
cd iwanit
./mvnw spring-boot:run
```

### Frontend outside Docker

```bash
cd iwanit_front
npm install
npm run dev
```

## Environment variables

Docker Compose reads them from a `.env` file in the project root. `.env` is ignored by git and
must never be committed; `.env.example` lists every variable with safe placeholder values.

| Variable                                          | Description                                    |
| ------------------------------------------------- | ---------------------------------------------- |
| `POSTGRES_DB`, `POSTGRES_USER`, `POSTGRES_PASSWORD` | Database name and credentials                |
| `POSTGRES_HOST_PORT`                              | Host port for Postgres (default `5433`)        |
| `FRONTEND_HOST_PORT`                              | Host port for the frontend (default `3000`)    |
| `JWT_SECRET`                                      | Key used to sign JWTs                          |
| `STRIPE_API_KEY`                                  | Stripe secret key (`sk_test_...` locally)      |
| `STRIPE_PUBLIC_KEY`                               | Stripe publishable key (`pk_test_...` locally) |

### Stripe keys

- Use the test keys from a Stripe sandbox (Dashboard > Sandboxes > Developers > API keys) for local
  development. Live keys (`sk_live_...`, `rk_live_...`) must never be used locally or shared.
- Never put the real values in `application.properties`: it only references the environment
  variables (`stripe.api.key=${STRIPE_API_KEY}`).
- If you run the backend outside Docker (with `./mvnw spring-boot:run` or from the IDE), the
  variables from `.env` are not loaded automatically. Add them to the IDE run configuration or
  export them in the terminal before starting the backend. For now nothing breaks without them,
  because no code uses these properties yet.
