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
public class PositionClosedDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private long orderId;
    private String ticket;
    private String type;
    private String generated;
    private double profit;
    private double takeProfit;
    private double stopLoss;
    private double margin;
    private double lots;
    private double openPrice;
    private double closePrice;
    private String openPlatform;
    private long signalUserId;
    private long terminalId;
    private String terminalCurrency;
    private String symbol;
    private String symbolName;
    private String baseSymbol;
    private int symbolDigits;
    private Instant closeTime;

    public static PositionClosedDto fromJson(JsonObject json) {
        PositionClosedDto dto = new PositionClosedDto();
        dto.setOrderId(json.getJsonNumber("order_id").longValue());
        dto.setTicket(json.getString("ticket"));
        dto.setType(json.getString("type"));
        dto.setGenerated(json.getString("generated"));
        dto.setProfit(json.getJsonNumber("profit").doubleValue());
        dto.setTakeProfit(json.getJsonNumber("take_profit").doubleValue());
        dto.setStopLoss(json.getJsonNumber("stop_loss").doubleValue());
        dto.setMargin(json.getJsonNumber("margin").doubleValue());
        dto.setLots(json.getJsonNumber("lots").doubleValue());
        dto.setOpenPrice(json.getJsonNumber("open_price").doubleValue());
        dto.setClosePrice(json.getJsonNumber("close_price").doubleValue());
        dto.setOpenPlatform(json.getString("open_platform"));
        dto.setSignalUserId(json.getJsonNumber("signal_user_id").longValue());
        dto.setTerminalId(json.getJsonNumber("terminal_id").longValue());
        dto.setTerminalCurrency(json.getString("terminal_currency"));
        dto.setSymbol(json.getString("symbol"));
        dto.setSymbolName(json.getString("symbol_name"));
        dto.setBaseSymbol(json.getString("base_symbol"));
        dto.setSymbolDigits(json.getInt("symbol_digits"));
        dto.setCloseTime(Instant.parse(json.getString("close_time")));

        return dto;
    }
}
