package de.paulmannit.trader.dtos;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@RegisterForReflection
@Getter
@Setter
public class HistoriesDataDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String symbol;

    private List<HistoryDataDto> historyDataDtoList;
}
