package de.paulmannit.trader.naga;

import de.paulmannit.trader.dtos.*;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.StringReader;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@ApplicationScoped
public class NagaService {
    private static final String BASE_URL = "https://api-v2.naga.com";
    private static final String PLATFORM = "web-trader";
    private static final String ORIGIN = "https://app.naga.com";
    private static final String REFERER = "https://app.naga.com";
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneOffset.UTC);

    private final HttpClient client = HttpClient.newHttpClient();
    private final String deviceUuid = UUID.randomUUID().toString().replace("-", "");

    @ConfigProperty(name = "naga.userName")
    public String userName;

    @ConfigProperty(name = "naga.passwordHash")
    public String passwordHash;

    /**
     * For Test-case
     */
    public NagaSession login(String userName, String passwordHash) {
        this.userName = userName;
        this.passwordHash = passwordHash;
        return login();
    }

    /**
     * Login
     * @return NagaSession
     */
    public NagaSession login() {
        try {
            this.userName = userName;
            this.passwordHash = passwordHash;
            String body = Json.createObjectBuilder()
                    .add("user_name", userName)
                    .add("password", passwordHash)
                    .add("device_uuid", deviceUuid)
                    .add("get_user_info", true)
                    .build().toString();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/user/login"))
                    .header("accept", "*/*")
                    .header("platform", "web-trader")
                    .header("origin", "https://naga.com")
                    .header("referer", "https://naga.com/login")
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject info = Json.createReader(new StringReader(response.body())).readObject().getJsonObject("info");
                return new NagaSession(info.getString("token"), info.getString("xsrf"), null, null);
            }
        } catch (Exception e) {
            throw new RuntimeException("Login failed", e);
        }
        return null;
    }

    /**
     * Get all linked Accounts
     */
    public List<AccountDto> getLinkedAccounts(NagaSession session) {
        try {
            String body = Json.createObjectBuilder()
                    .add("device_uuid", deviceUuid)
                    .add("terminal_id", "")
                    .build().toString();

            HttpRequest request = baseRequest("/broker/list_linked_accounts", session)
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonArray array = Json.createReader(new StringReader(response.body())).readObject().getJsonArray("data");
                List<AccountDto> accounts = new ArrayList<>();
                for (JsonValue val : array) {
                    accounts.add(AccountDto.fromJson(val.asJsonObject()));
                }
                return accounts;
            }
        } catch (Exception e) {
            Log.error("Get linked accounts failed", e);
        }
        return List.of();
    }

    /**
     * Create a session on the account
     * @param session
     * @param terminalId
     * @return
     */
    public NagaSession createSession(NagaSession session, String terminalId, String customName) {
        try {
            String body = Json.createObjectBuilder()
                    .add("device_uuid", deviceUuid)
                    .add("terminal_id", terminalId)
                    .build().toString();

            HttpRequest request = baseRequest("/broker/create_session", session)
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonObject info = Json.createReader(new StringReader(response.body())).readObject().getJsonObject("info");
                return new NagaSession(info.getString("token"), info.getString("xsrf"), terminalId, customName);
            }
        } catch (Exception e) {
            Log.error("Create session failed", e);
        }
        return null;
    }

    /**
     * Create a trade
     * @param session
     * @param symbol
     * @param price
     * @param lots
     */
    public void createPostition(NagaSession session, String symbol, double price, double lots, double takeProfit, double stopLoss, boolean isLong) {
        try {
            String body = Json.createObjectBuilder()
                    .add("duration", 60)
                    .add("lots", lots)
                    .add("price", price)
                    .add("private", "N")
                    .add("sl", stopLoss)
                    .add("tp", takeProfit)
                    .add("symbol", symbol)
                    .add("type", isLong ? "BUY":"SELL")
                    .build().toString();

            HttpRequest request = baseRequest("/broker/trade/create", session, "1.0.0")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                Log.info("Position Created");
            }
            else {
                Log.error("Position NOT Created");
            }
        } catch (Exception e) {
            Log.error("Create Position failed", e);
        }
    }

    /**
     * Close a trade
     * @param session
     * @param positionId
     */
    public void closePostition(NagaSession session, String positionId) {
        try {
            String body = Json.createObjectBuilder()
                    .add("platform_position_id", positionId)
                    .build().toString();

            HttpRequest request = baseRequest("/broker/position/close", session, "1.0.0")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                Log.info("Position Closed");
            }
            else {
                Log.error("Position NOT Closed");
            }
        } catch (Exception e) {
            Log.error("Close position failed", e);
        }
    }

    /**
     * Modify a trade
     * @param session
     * @param positionId
     * @param stop_loss
     * @param take_profit
     */
    public void modifyPostition(NagaSession session, String positionId, double stop_loss, double take_profit) {
        try {
            String body = Json.createObjectBuilder()
                    .add("platform_position_id", positionId)
                    .add("stop_loss", stop_loss)
                    .add("take_profit", take_profit)
                    .build().toString();

            HttpRequest request = baseRequest("/broker/position/modify", session)
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                Log.info("Position Modified");
            }
            else {
                Log.error("Position NOT Modified");
            }
        } catch (Exception e) {
            Log.error("Modify position failed", e);
        }
    }

    /**
     * Get the MarketInfomation for a symbol (e.g. XAUUSD)
     * @param symbol
     * @param session
     * @return
     */
    public MarketInfoDto getMarketInfo(String symbol, NagaSession session) {
        try {
            String encodedSymbol = URLEncoder.encode(symbol, StandardCharsets.UTF_8);
            HttpRequest request = baseRequest("/broker/symbol/" + encodedSymbol, session)
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject data = Json.createReader(new StringReader(response.body())).readObject().getJsonObject("data");
                return MarketInfoDto.fromJson(data);
            }
        } catch (Exception e) {
            Log.error("MarketInfo failed for symbol: " + symbol, e);
        }
        return null;
    }

    /**
     * Get all active Trades
     * @param session
     * @return
     */
    public List<PositionDto> getActivePositions(NagaSession session) {
        try {
            HttpRequest request = baseRequest("/broker/trades/active", session)
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonArray array = Json.createReader(new StringReader(response.body())).readObject().getJsonArray("data");
                List<PositionDto> result = new ArrayList<>();
                for (JsonValue val : array) {
                    result.add(PositionDto.fromJson(val.asJsonObject()));
                }
                return result;
            }
        } catch (Exception e) {
            Log.error("Get active positions failed", e);
        }
        return List.of();
    }

    /**
     * Get DynamicData from active account
     * @param session
     * @return
     */
    public DynamicDataDto getDynamicData(NagaSession session) {
        try {
            HttpRequest request = baseRequest("/user/get_dynamic_data/" + session.getTerminalId(), session)
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject data = Json.createReader(new StringReader(response.body())).readObject().getJsonObject("data");
                DynamicDataDto dynamicDataDto = DynamicDataDto.fromJson(data);
                dynamicDataDto.setTerminalId(session.getTerminalId());
                return dynamicDataDto;
            }
        } catch (Exception e) {
            Log.error("Getting dynamic data", e);
        }
        return null;
    }

    private Map<String, String> getCurrentMonthRangeUTC() {
        YearMonth currentMonth = YearMonth.now(ZoneOffset.UTC);
        YearMonth lastMonth = YearMonth.now(ZoneOffset.UTC).minusMonths(2);

        LocalDate startDate = lastMonth.atDay(1);
        LocalDate endDate = currentMonth.atEndOfMonth();

        // Start of day (00:00:00.000)
        Instant startInstant = startDate.atStartOfDay().toInstant(ZoneOffset.UTC);
        // End of day (23:59:59.999)
        Instant endInstant = endDate.atTime(23, 59, 59, 999_000_000).toInstant(ZoneOffset.UTC);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .withZone(ZoneOffset.UTC);

        return Map.of(
                "start", formatter.format(startInstant),
                "end", formatter.format(endInstant)
        );
    }

    /**
     * Get closed positions of last two month
     * @param session
     * @return
     */
    public PositionsClosedDto getClosedOrders(NagaSession session) {
        PositionsClosedDto  positionsClosedDto = new PositionsClosedDto();
        List<PositionClosedDto> positions = new ArrayList<>();
        Map<String, String> range = getCurrentMonthRangeUTC();

        int page = 0;
        while (true) {
            List<PositionClosedDto> part = fetchClosedOrders(session, page, range.get("start"), range.get("end"));
            positions.addAll(part);
            if (part.size() < 50) break;
            page++;
        }

        positionsClosedDto.setPositionClosedDtoList(positions);

        Map<String, String> map = getProfit(session);
        positionsClosedDto.setProfit(Double.parseDouble(map.get("profit")));
        positionsClosedDto.setCount(Integer.parseInt(map.get("count")));
        positionsClosedDto.setTerminalId(session.getTerminalId());
        positionsClosedDto.setCustomName(session.getCustomName());

        return positionsClosedDto;
    }

    private List<PositionClosedDto> fetchClosedOrders(NagaSession session, int page, String from, String to) {
        try {
            String path = String.format("/broker/order/closed?p_page_num=%d&p_page_size=50&p_date_from=%s&p_date_to=%s", page, from, to);
            HttpRequest request = baseRequest(path, session, "2.*")
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonArray array = Json.createReader(new StringReader(response.body())).readObject().getJsonArray("data");
                List<PositionClosedDto> result = new ArrayList<>();
                for (JsonValue val : array) {
                    result.add(PositionClosedDto.fromJson(val.asJsonObject()));
                }
                return result;
            }
        } catch (Exception e) {
            Log.error("Fetch closed orders failed", e);
        }
        return List.of();
    }

    /**
     * Get profit of positions
     * @param session
     * @return
     */
    public Map<String, String> getProfit(NagaSession session) {
        try {
            HttpRequest request = baseRequest("/broker/order/profit?p_page_num=0", session, "2.0")
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject data = Json.createReader(new StringReader(response.body())).readObject().getJsonObject("data");
                Map<String, String> result = new HashMap<>();
                if (data.containsKey("total_profit")) {
                    result.put("profit", data.getJsonNumber("total_profit").toString());
                }
                if (data.containsKey("order_count")) {
                    result.put("count", data.getJsonNumber("order_count").toString());
                }
                return result;
            }
        } catch (Exception e) {
            Log.error("Get profit failed", e);
        }
        return Map.of();
    }

    /**
     * Get historic data of a symbol.
     * Add MACD Indicator
     *
     * @param symbol
     * @param session
     * @return
     */
    public HistoriesDataDto getHistory(String symbol, NagaSession session) {
        try {
            HistoriesDataDto historiesDataDto = new HistoriesDataDto();

            Instant now = Instant.now();
            long from = now.minus(2, ChronoUnit.HOURS).getEpochSecond();
            long to = now.getEpochSecond();

            String path = String.format("/trading_view/history?symbol=%s&resolution=1&from=%d&to=%d", symbol, from, to);
            HttpRequest request = baseRequest(path, session, "1.0.0")
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject data = Json.createReader(new StringReader(response.body())).readObject().getJsonObject("data");

                JsonArray opens = data.getJsonArray("o");
                JsonArray closes = data.getJsonArray("c");
                JsonArray highs = data.getJsonArray("h");
                JsonArray lows = data.getJsonArray("l");
                JsonArray volumes = data.getJsonArray("v");
                JsonArray times = data.getJsonArray("t");

                List<HistoryDataDto> dtos = new ArrayList<>();
                for (int i = 0; i < closes.size(); i++) {
                    HistoryDataDto dto = new HistoryDataDto();
                    dto.setOpen(opens.getJsonNumber(i).doubleValue());
                    dto.setClose(closes.getJsonNumber(i).doubleValue());
                    dto.setHigh(highs.getJsonNumber(i).doubleValue());
                    dto.setLow(lows.getJsonNumber(i).doubleValue());
                    dto.setVolume(volumes.getJsonNumber(i).longValue());
                    dto.setTimeStamp(Instant.ofEpochSecond(times.getJsonNumber(i).longValue()));
                    dtos.add(dto);
                }

                dtos.sort(Comparator.comparing(HistoryDataDto::getTimeStamp));
                TechnicalIndicators.calculateMacd(dtos);

                historiesDataDto.setHistoryDataDtoList(dtos);
                historiesDataDto.setSymbol(symbol);
                return historiesDataDto;
            }
        } catch (Exception e) {
            Log.error("Get history failed", e);
        }
        return new HistoriesDataDto();
    }

    private HttpRequest.Builder baseRequest(String path, NagaSession session, String version) {
        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("accept", "*/*")
                .header("accept-version", version)
                .header("platform", PLATFORM)
                .header("origin", ORIGIN)
                .header("referer", REFERER)
                .header("Content-Type", "application/json")
                .header("Xsrf", session.getXsrf())
                .header("authorization", "JWT " + session.getToken());
    }
    private HttpRequest.Builder baseRequest(String path, NagaSession session) {
        return baseRequest(path, session, "1.*");
    }
}
