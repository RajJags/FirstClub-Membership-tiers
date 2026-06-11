# FirstClub Membership Service

Spring Boot backend for a subscription membership program with plans, configurable tiers, benefits, tier eligibility, and subscription lifecycle APIs.

## Run

```bash
mvn spring-boot:run
```

The app starts on `http://localhost:8080` and uses an in-memory H2 database seeded with:

- Plans: `MONTHLY`, `QUARTERLY`, `YEARLY`
- Tiers: `SILVER`, `GOLD`, `PLATINUM`

## Windows Local Setup Used Here

This workspace includes a portable toolchain under `.tools`:

- JDK: `.tools/jdk-17.0.19+10`
- Maven: `.tools/apache-maven-3.9.9`
- Maven cache: `.tools/.m2`

Build the runnable jar:

```cmd
build-local.cmd
```

Run the API:

```cmd
run-local.cmd
```

The frontend will be available at `http://127.0.0.1:8080/`.
The APIs remain available under `http://127.0.0.1:8080/api/memberships`.

## Demo APIs

Get plans and tiers:

```bash
curl http://localhost:8080/api/memberships/catalog
```

Subscribe a user:

```bash
curl -X POST http://localhost:8080/api/memberships/subscriptions \
  -H "Content-Type: application/json" \
  -d '{"userId":101,"billingPeriod":"MONTHLY","tierCode":"SILVER"}'
```

Get current membership:

```bash
curl http://localhost:8080/api/memberships/users/101/subscription
```

Upgrade or downgrade tier:

```bash
curl -X PUT http://localhost:8080/api/memberships/users/101/subscription/tier \
  -H "Content-Type: application/json" \
  -d '{"tierCode":"GOLD"}'
```

Evaluate the best eligible tier from order/cohort criteria:

```bash
curl -X POST http://localhost:8080/api/memberships/tiers/evaluate \
  -H "Content-Type: application/json" \
  -d '{"userId":101,"monthlyOrderCount":12,"monthlyOrderValue":20000,"cohorts":["VIP"]}'
```

Cancel membership:

```bash
curl -X DELETE http://localhost:8080/api/memberships/users/101/subscription
```

## Design Notes

- Plans, tiers, benefits, and tier criteria are separate entities so pricing, perks, and eligibility can evolve independently.
- `UserMembership` tracks the user's current plan, tier, status, start date, expiry date, and version.
- `@Version` plus repository locking protects upgrade, downgrade, and cancel flows from lost updates.
- One current membership row is enforced per user; cancelled or expired users can renew into a new plan/tier on the same row.
- Tier criteria are configurable with criterion types such as monthly order count, monthly order value, and cohort membership.
