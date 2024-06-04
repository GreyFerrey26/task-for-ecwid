package dev.greyferret.structure.enumeric;

import dev.greyferret.exception.InputArgumentFormatException;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum RunningType {
    GENERATE("g"), // generate file with random ips (w/ override)
    GENERATE_ALL("ga"), // generate file with all available ips (w/ override)
    RUN("r"), // generate temporary files and run counting on them, delete afterward
    FILE("f"); // read file and count on it

    private final String variable;

    RunningType(String variable) {
        this.variable = variable;
    }

    public static RunningType extractOrThrow(String variable) {
        Optional<RunningType> runningTypeOptional = Arrays.stream(RunningType.values())
                .filter(rt -> rt.variable.equalsIgnoreCase(variable))
                .findFirst();
        if (runningTypeOptional.isEmpty()) {
            throw new InputArgumentFormatException("Only the following arguments allowed: " +
                    String.join(",", Arrays.stream(RunningType.values()).map(RunningType::getVariable).toList()));
        } else {
            return runningTypeOptional.get();
        }
    }
}
