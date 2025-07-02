package de.paulmannit.trader.dtos;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.json.JsonObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@RegisterForReflection
@NoArgsConstructor
@Getter
@Setter
public class Sentiment implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int buys;
    private int sells;

    public static Sentiment fromJson(JsonObject obj) {
        Sentiment s = new Sentiment();
        s.setBuys(obj.getInt("buys"));
        s.setSells(obj.getInt("sells"));
        return s;
    }
}
