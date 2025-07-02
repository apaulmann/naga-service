package de.paulmannit.trader.dtos;

import io.quarkus.runtime.annotations.RegisterForReflection;
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
public class HistoryDataDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Double open;
    private Double close;
    private Double high;
    private Double low;
    private Long volume;
    private Instant timeStamp;

    private Double macd;        // MACD-Linie (12-26 EMA)
    private Double signalLine;  // 9-Perioden-EMA der MACD-Linie
    private Double ema200;  // 9-Perioden-EMA der MACD-Linie

}
