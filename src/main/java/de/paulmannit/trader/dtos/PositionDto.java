package de.paulmannit.trader.dtos;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.json.JsonObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RegisterForReflection
public class PositionDto {
    private String platformPositionId;
    private int quantity;
    private String symbol;
    private String side;
    private long creationTime;
    private long lastChangeTime;
    private double takeProfit;
    private double stopLoss;
    private double avgOpenPrice;
    private double openProfit;
    private double fees;
    private double swapFees;
    private double margin;
    private boolean isClosed;

    public static PositionDto fromJson(JsonObject obj) {
        PositionDto dto = new PositionDto();
        dto.platformPositionId = obj.getString("platform_position_id");
        dto.quantity = obj.getInt("quantity");
        dto.symbol = obj.getString("symbol");
        dto.side = obj.getString("side");
        dto.creationTime = obj.getJsonNumber("creation_time").longValue();
        dto.lastChangeTime = obj.getJsonNumber("last_change_time").longValue();
        if (obj.containsKey("take_profit")) {
            dto.takeProfit = obj.getJsonNumber("take_profit").doubleValue();
        }
        if (obj.containsKey("stop_loss")) {
            dto.stopLoss = obj.getJsonNumber("stop_loss").doubleValue();
        }
        dto.avgOpenPrice = obj.getJsonNumber("avg_open_price").doubleValue();
        dto.openProfit = obj.getJsonNumber("open_profit").doubleValue();
        dto.fees = obj.getJsonNumber("fees").doubleValue();
        dto.swapFees = obj.getJsonNumber("swap_fees").doubleValue();
        dto.margin = obj.getJsonNumber("margin").doubleValue();
        dto.isClosed = obj.getBoolean("is_closed");
        return dto;
    }
}