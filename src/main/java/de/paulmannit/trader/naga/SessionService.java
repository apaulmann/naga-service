package de.paulmannit.trader.naga;

import de.paulmannit.trader.dtos.AccountDto;
import io.quarkus.logging.Log;
import io.quarkus.runtime.Startup;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class SessionService {

    @Inject
    NagaService nagaService;

    public NagaSession nagaSession = null;

    @ConfigProperty(name = "naga.accountName")
    String accountName;

    @ConfigProperty(name = "naga.accountIsDemo", defaultValue = "true")
    boolean isDemo;

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

    public AccountDto getAccount(List<AccountDto> accountDtos) {
        String type = isDemo?"D":"R";
        return accountDtos.stream()
                .filter(a -> accountName.equalsIgnoreCase(a.getCustomName()) && type.equalsIgnoreCase(a.getType()))
                .findFirst()
                .orElse(null);
    }

    public NagaSession getSession() {
        return nagaSession;
    }

    @Scheduled(cron = "${naga.cron.reconnect}")
    public void reconnect() {
        Log.info("Reconnecting...");
        start();
    }
}
