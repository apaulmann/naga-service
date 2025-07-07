package de.paulmannit.trader.dtos;

import de.paulmannit.trader.naga.Helper;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
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

        dto.setSymbol(Helper.getSafeString(obj, "symbol"));
        dto.setAsk(Helper.getSafeDouble(obj, "ask"));
        dto.setBid(Helper.getSafeDouble(obj, "bid"));
        dto.setSpread(Helper.getSafeDouble(obj, "spread"));
        dto.setMargin(Helper.getSafeDouble(obj, "margin"));
        dto.setTickValue(Helper.getSafeDouble(obj, "tick_value"));
        dto.setLotMax(Helper.getSafeDouble(obj, "lot_max"));
        dto.setCommission(Helper.getSafeDouble(obj, "commission"));
        dto.setSpreadValue(Helper.getSafeDouble(obj, "spread_value"));
        dto.setStopLevel(Helper.getSafeDouble(obj, "stop_level"));
        dto.setPoint(Helper.getSafeDouble(obj, "point"));
        dto.setLotSize(Helper.getSafeDouble(obj, "lot_size"));
        dto.setLotMin(Helper.getSafeDouble(obj, "lot_min"));
        dto.setLotStep(Helper.getSafeDouble(obj, "lot_step"));

        dto.setDigits(obj.containsKey("digits") ? obj.getInt("digits") : 0);
        dto.setSymbolLotMax(obj.containsKey("symbol_lot_max") ? obj.getInt("symbol_lot_max") : 0);

        dto.setSymbolName(Helper.getSafeString(obj, "symbol_name"));
        dto.setAssetType(Helper.getSafeString(obj, "asset_type"));

        dto.setFavourite(Helper.getSafeBoolean(obj, "is_favourite"));
        dto.setRealStocks(Helper.getSafeBoolean(obj, "is_real_stocks"));
        dto.setBuyOnly(Helper.getSafeBoolean(obj, "is_buy_only"));
        dto.setMarketOpen(Helper.getSafeBoolean(obj, "is_market_open"));
        dto.setShowCostWarning(Helper.getSafeBoolean(obj, "show_cost_warning"));

        dto.setSentiment(obj.containsKey("sentiment") && obj.get("sentiment").getValueType() == JsonValue.ValueType.OBJECT
                ? Sentiment.fromJson(obj.getJsonObject("sentiment"))
                : null);

        dto.setLastUpdateDate(Helper.parseInstant(obj, "last_update_date"));
        dto.setNearestMarketOpening(Helper.parseInstant(obj, "nearest_market_opening"));
        dto.setNearestMarketClosing(Helper.parseInstant(obj, "nearest_market_closing"));

        return dto;
    }

}
