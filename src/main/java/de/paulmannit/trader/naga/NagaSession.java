package de.paulmannit.trader.naga;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RegisterForReflection
@AllArgsConstructor
public class NagaSession {
    private String token;
    private String xsrf;
    private String terminalId;
}
