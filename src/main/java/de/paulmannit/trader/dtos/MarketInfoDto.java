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
public class MarketInfoDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String symbol;
    private double ask;
    private double bid;
    private double spread;
    private double margin;
    private double tickValue;
    private double lotMax;
    private Instant lastUpdateDate;
    private double commission;
    private double spreadValue;
    private double stopLevel;
    private double point;
    private int digits;
    private double lotSize;
    private double lotMin;
    private double lotStep;
    private int symbolLotMax;
    private String symbolName;
    private boolean isFavourite;
    private boolean isRealStocks;
    private boolean isBuyOnly;
    private String assetType;
    private Sentiment sentiment;
    private boolean isMarketOpen;
    private Instant nearestMarketOpening;
    private Instant nearestMarketClosing;
    private boolean showCostWarning;

    public static MarketInfoDto fromJson(JsonObject obj) {
        MarketInfoDto dto = new MarketInfoDto();
        dto.setSymbol(obj.getString("symbol"));
        dto.setAsk(obj.getJsonNumber("ask").doubleValue());
        dto.setBid(obj.getJsonNumber("bid").doubleValue());
        dto.setSpread(obj.getJsonNumber("spread").doubleValue());
        dto.setMargin(obj.getJsonNumber("margin").doubleValue());
        dto.setTickValue(obj.getJsonNumber("tick_value").doubleValue());
        dto.setLotMax(obj.getJsonNumber("lot_max").doubleValue());
        dto.setLastUpdateDate(Instant.parse(obj.getString("last_update_date")));
        dto.setCommission(obj.getJsonNumber("commission").doubleValue());
        dto.setSpreadValue(obj.getJsonNumber("spread_value").doubleValue());
        dto.setStopLevel(obj.getJsonNumber("stop_level").doubleValue());
        dto.setPoint(obj.getJsonNumber("point").doubleValue());
        dto.setDigits(obj.getInt("digits"));
        dto.setLotSize(obj.getJsonNumber("lot_size").doubleValue());
        dto.setLotMin(obj.getJsonNumber("lot_min").doubleValue());
        dto.setLotStep(obj.getJsonNumber("lot_step").doubleValue());
        dto.setSymbolLotMax(obj.getInt("symbol_lot_max"));
        dto.setSymbolName(obj.getString("symbol_name"));
        dto.setFavourite(obj.getBoolean("is_favourite"));
        dto.setRealStocks(obj.getBoolean("is_real_stocks"));
        dto.setBuyOnly(obj.getBoolean("is_buy_only"));
        dto.setAssetType(obj.getString("asset_type"));
        dto.setSentiment(Sentiment.fromJson(obj.getJsonObject("sentiment")));
        dto.setMarketOpen(obj.getBoolean("is_market_open"));
        dto.setNearestMarketOpening(Instant.parse(obj.getString("nearest_market_opening")));
        dto.setNearestMarketClosing(Instant.parse(obj.getString("nearest_market_closing")));
        dto.setShowCostWarning(obj.getBoolean("show_cost_warning"));
        return dto;
    }
}
