package de.paulmannit.trader.dtos;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.json.JsonObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@RegisterForReflection
@NoArgsConstructor
@Getter
@Setter
public class AccountDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private long terminalId;
    private int brokerId;
    private String logoSmall;
    private int brokerServerId;
    private String type;
    private String status;
    private Instant creationDate;
    private String currency;
    private String leverage;
    private double balance;
    private String credit;
    private double equity;
    private String netFunds;
    private String pendingWithdrawals;
    private String pendingAmount;
    private String currencySymbol;
    private int currencyDigits;
    private String groupName;
    private String ibGroup;
    private String symbolGroup;
    private String login;
    private String customName;
    private String company;
    private String cashierLink;
    private String memberAreaLink;
    private String serverName;
    private Instant firstLogin;
    private Instant lastLogin;
    private boolean validPassword;
    private boolean canGetPasswords;
    private String instrumentType;

    public static AccountDto fromJson(JsonObject json) {
        AccountDto dto = new AccountDto();
        dto.setTerminalId(json.getJsonNumber("terminal_id").longValue());
        dto.setBrokerId(json.getInt("broker_id"));
        dto.setLogoSmall(json.getString("logo_small", null));
        dto.setBrokerServerId(json.getInt("broker_server_id"));
        dto.setType(json.getString("type", null));
        dto.setStatus(json.getString("status", null));
        dto.setCreationDate(Instant.parse(json.getString("creation_date")));
        dto.setCurrency(json.getString("currency", null));
        dto.setLeverage(json.getString("leverage", null));
        dto.setBalance(json.getJsonNumber("balance").doubleValue());
        dto.setCredit(json.getString("credit", null));
        dto.setEquity(json.getJsonNumber("equity").doubleValue());
        dto.setNetFunds(json.getString("net_funds", null));
        dto.setPendingWithdrawals(json.isNull("pending_withdrawals") ? null : json.get("pending_withdrawals").toString());
        dto.setPendingAmount(json.isNull("pending_amount") ? null : json.get("pending_amount").toString());
        dto.setCurrencySymbol(json.getString("currency_symbol", null));
        dto.setCurrencyDigits(json.getInt("currency_digits"));
        dto.setGroupName(json.getString("group_name", null));
        dto.setIbGroup(json.getString("ib_group", null));
        dto.setSymbolGroup(json.getString("symbol_group", null));
        dto.setLogin(json.getString("login", null));
        dto.setCustomName(json.getString("custom_name", null));
        dto.setCompany(json.getString("company", null));
        dto.setCashierLink(json.getString("cashier_link", null));
        dto.setMemberAreaLink(json.getString("member_area_link", null));
        dto.setServerName(json.getString("server_name", null));
        dto.setFirstLogin(Instant.parse(json.getString("first_login")));
        dto.setLastLogin(Instant.parse(json.getString("last_login")));
        dto.setValidPassword(json.getBoolean("valid_password", false));
        dto.setCanGetPasswords(json.getBoolean("can_get_passwords", false));
        dto.setInstrumentType(json.getString("instrument_type", null));
        return dto;
    }
}
