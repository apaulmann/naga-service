# naga-service

Unofficial Java API service for accessing the NAGA.com trading and social platform.  
This library is implemented using [Quarkus](https://quarkus.io/) and provides programmatic access to NAGA’s private (undocumented) API, reverse-engineered from the official web interface.

> ⚠️ **Disclaimer**  
> I am not associated with NAGA.com. This project is unofficial and uses private APIs which may change at any time without notice.  
> Use this service at your own risk. I am not responsible for any financial losses resulting from the use of this software.

---

## Features

- Login with hashed credentials (see below)
- Access to linked broker accounts (DEMO and REAL)
- Trading actions: open, update, close positions
- Market data retrieval
- Session handling per trading account
- JSON-based REST API

---

## Requirements

- Java 21+
- Maven 3.9+
- Quarkus 3.23+
- NAGA account

---

## Installation

Build the library using Maven:

```bash
./mvnw clean install
```
---
## Dependency

Add it to your own project via Maven:
```code
<dependency>
    <groupId>de.paulmannit.trader</groupId>
    <artifactId>naga-service</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```
---
## Authentication
To use authenticated features (e.g. trading), you must manually extract login credentials from NAGA's web interface.

1. Log in via https://naga.com

2. Open Developer Tools → Network Tab

3. Look for the login request to:
https://api-v2.swipestox.com/user/login

4. Copy the hashed password and put it in your own project

---

## Example

In your application.properties:
```code
naga.userName=<account-email>
naga.passwordHash=<hash>
naga.accountName=NAGA - EUR
naga.accountIsDemo=true
naga.cron.reconnect=0 1 1 * * ?
```

Example for Login and get closed positions for the last two month

```code
    @Inject
    NagaService nagaService;

    private NagaSession nagaSession;

    @Startup
    public void start() {
        NagaSession nagaSession = nagaService.login();
        Log.info("Session Token: " + nagaSession.getToken());

        List<AccountDto> accountDtos = new ArrayList<>();
        try {
            accountDtos = nagaService.getLinkedAccounts(nagaSession);

            AccountDto accountDto = getAccount(accountDtos);
            if (accountDto == null) {
                Log.errorf("Account <%s> Type <%s> not found", accountName, isDemo ? "Demo" : "Live");
                System.exit(-1);
            }
            this.nagaSession = nagaService.createSession(nagaSession, String.valueOf(accountDto.getTerminalId()));
        }
        catch (Exception e) {
            Log.error(e);
        }
    }
    
    public void getClosedPositions() {
        if (nagaSession != null) {
            try {
                PositionsClosedDto positionsClosedDto = new PositionsClosedDto();
    
                List<PositionClosedDto> positionClosedDtos = nagaService.getClosedOrders(nagaSession);
                Map<String, String> map = nagaService.getProfit(nagaSession);
                positionsClosedDto.setProfit(Double.valueOf(map.get("profit")));
                positionsClosedDto.setCount(Integer.valueOf(map.get("count")));
    
                positionsClosedDto.setPositionClosedDtoList(positionClosedDtos);
                String json = objectMapper.writeValueAsString(positionsClosedDto);
                Log.info("Getting Positions: " + json);
            } catch (Exception e) {
                Log.error("Error getting positions: " + e.getMessage(), e);
            }
        }
    }

```