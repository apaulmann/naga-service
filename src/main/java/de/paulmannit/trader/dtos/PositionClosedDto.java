package de.paulmannit.trader.dtos;

import de.paulmannit.trader.naga.Helper;
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
        dto.setOrderId(Helper.getSafeLong(json, "order_id"));
        dto.setTicket(Helper.getSafeString(json,"ticket"));
        dto.setType(Helper.getSafeString(json,"type"));
        dto.setGenerated(Helper.getSafeString(json,"generated"));
        dto.setProfit(Helper.getSafeDouble(json, "profit"));
        dto.setTakeProfit(Helper.getSafeDouble(json,"take_profit"));
        dto.setStopLoss(Helper.getSafeDouble(json,"stop_loss"));
        dto.setMargin(Helper.getSafeDouble(json,"margin"));
        dto.setLots(Helper.getSafeDouble(json,"lots"));
        dto.setOpenPrice(Helper.getSafeDouble(json,"open_price"));
        dto.setClosePrice(Helper.getSafeDouble(json,"close_price"));
        dto.setOpenPlatform(Helper.getSafeString(json,"open_platform"));
        dto.setSignalUserId(Helper.getSafeLong(json,"signal_user_id"));
        dto.setTerminalId(Helper.getSafeLong(json,"terminal_id"));
        dto.setTerminalCurrency(Helper.getSafeString(json,"terminal_currency"));
        dto.setSymbol(Helper.getSafeString(json,"symbol"));
        dto.setSymbolName(Helper.getSafeString(json,"symbol_name"));
        dto.setBaseSymbol(Helper.getSafeString(json,"base_symbol"));
        dto.setSymbolDigits(json.getInt("symbol_digits"));
        dto.setCloseTime(Instant.parse(json.getString("close_time")));

        return dto;
    }
}
