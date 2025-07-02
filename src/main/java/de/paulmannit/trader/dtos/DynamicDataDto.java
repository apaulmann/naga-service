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
public class DynamicDataDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private double floatingProfit;
    private double marginLevel;
    private double margin;
    private double freeMargin;
    private double equity;
    private double balance;
    private double credit;
    private String currency;
    private Instant creationDate;

    public static DynamicDataDto fromJson(JsonObject json) {
        DynamicDataDto dto = new DynamicDataDto();
        dto.setFloatingProfit(json.getJsonNumber("floating_profit").doubleValue());
        dto.setMarginLevel(json.getJsonNumber("margin_level").doubleValue());
        dto.setMargin(json.getJsonNumber("margin").doubleValue());
        dto.setFreeMargin(json.getJsonNumber("free_margin").doubleValue());
        dto.setEquity(json.getJsonNumber("equity").doubleValue());
        dto.setBalance(json.getJsonNumber("balance").doubleValue());
        dto.setCredit(json.getJsonNumber("credit").doubleValue());
        dto.setCurrency(json.getString("currency"));
        dto.setCreationDate(Instant.parse(json.getString("creation_date")));
        return dto;
    }
}
