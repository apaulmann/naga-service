package de.paulmannit.trader;

import de.paulmannit.trader.dtos.AccountDto;
import de.paulmannit.trader.dtos.MarketInfoDto;
import de.paulmannit.trader.naga.NagaService;
import de.paulmannit.trader.naga.NagaSession;
import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.json.Json;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class NagaServiceTest {
    @Inject
    public NagaService nagaService;

    @ConfigProperty(name = "naga.userName")
    public String userName;

    @ConfigProperty(name = "naga.passwordHash")
    public String passwordHash;

    @ConfigProperty(name = "naga.accountName")
    String accountName;

    @ConfigProperty(name = "naga.accountIsDemo", defaultValue = "true")
    boolean isDemo;

    private NagaSession nagaSession;

    @Test
    void loginAndListAccounts() {
        NagaSession loginSession = nagaService.login(userName, passwordHash);
        assertNotNull(loginSession);

        List<AccountDto> accounts = nagaService.getLinkedAccounts(loginSession);
        assertNotNull(accounts);
        assertFalse(accounts.isEmpty());

        accounts.forEach(account ->
                System.out.printf("Account: %s (%s)%n", account.getCustomName(), account.getType())
        );

        AccountDto accountDto = getAccount(accounts);
        if (accountDto == null) {
            Log.errorf("Account <%s> Type <%s> not found", accountName, isDemo ? "Demo" : "Live");
        }
        assertNotNull(accountDto);
        nagaSession = nagaService.createSession(loginSession, String.valueOf(accountDto.getTerminalId()));

        assertNotNull(nagaSession);

        try {
            // MarketInfo for Gold/USD
            MarketInfoDto dto = nagaService.getMarketInfo("XAUUSD", nagaSession);
            if (dto != null) {
                Log.info("Sending XAUUSD Market Info: " + Json.encode(dto));
            }
        } catch (Exception e) {
            Log.error("Error sending <XAUUSD> data: " + e.getMessage(), e);
        }
    }

    private AccountDto getAccount(List<AccountDto> accountDtos) {
        String type = isDemo ? "D" : "R";
        return accountDtos.stream()
                .filter(a -> accountName.equalsIgnoreCase(a.getCustomName()) && type.equalsIgnoreCase(a.getType()))
                .findFirst()
                .orElse(null);
    }
}
