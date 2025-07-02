package de.paulmannit.trader.naga;

import de.paulmannit.trader.dtos.HistoryDataDto;

import java.util.ArrayList;
import java.util.List;

public class TechnicalIndicators {

    public static void calculateMacd(List<HistoryDataDto> data) {
        final int shortPeriod = 12;
        final int longPeriod = 26;
        final int signalPeriod = 9;
        final int ema200Period = 200;

        List<Double> emaShort = calculateEmaList(data, shortPeriod);
        List<Double> emaLong = calculateEmaList(data, longPeriod);
        List<Double> ema200 = calculateEmaList(data, ema200Period);

        List<Double> macdList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            Double shortEma = emaShort.get(i);
            Double longEma = emaLong.get(i);

            if (shortEma != null && longEma != null) {
                Double macd = shortEma - longEma;
                macdList.add(macd);
                data.get(i).setMacd(macd);
            } else {
                macdList.add(null);
            }

            // set EMA200
            Double ema200Value = ema200.get(i);
            if (ema200Value != null) {
                data.get(i).setEma200(ema200Value);
            }
        }

        // Step 1: Get only valid (not-null) MACD-Values
        List<Double> validMacdValues = new ArrayList<>();
        List<Integer> validMacdIndexes = new ArrayList<>();
        for (int i = 0; i < macdList.size(); i++) {
            Double macd = macdList.get(i);
            if (macd != null) {
                validMacdValues.add(macd);
                validMacdIndexes.add(i);
            }
        }

        // Step 2: calculate Signal-EMA
        List<Double> signalValues = calculateEma(validMacdValues, signalPeriod);

        // Step 3: add Signal-Value on correct places in data[]
        for (int i = 0; i < signalValues.size(); i++) {
            Double signal = signalValues.get(i);
            if (signal != null) {
                int dataIndex = validMacdIndexes.get(i);
                data.get(dataIndex).setSignalLine(signal);
            }
        }
    }

    private static List<Double> calculateEmaList(List<HistoryDataDto> data, int period) {
        List<Double> closes = data.stream()
                .map(HistoryDataDto::getClose)
                .toList();
        return calculateEma(closes, period);
    }

    private static List<Double> calculateEma(List<Double> values, int period) {
        List<Double> result = new ArrayList<>();
        double multiplier = 2.0 / (period + 1);
        Double ema = null;

        for (int i = 0; i < values.size(); i++) {
            Double value = values.get(i);
            if (value == null) {
                result.add(null);
                continue;
            }

            if (i < period - 1) {
                result.add(null); // Not enough values
                continue;
            }

            if (i == period - 1) {
                double sum = 0.0;
                for (int j = i - period + 1; j <= i; j++) {
                    sum += values.get(j);
                }
                ema = sum / period;
            } else {
                if (ema != null) {
                    ema = ((value - ema) * multiplier) + ema;
                }
            }

            result.add(ema);
        }

        return result;
    }
}