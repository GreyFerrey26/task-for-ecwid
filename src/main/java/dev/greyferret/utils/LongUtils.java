package dev.greyferret.utils;

import dev.greyferret.exception.InputArgumentFormatException;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Utility class to parse Long or throw exception on the fly
 */
@UtilityClass
public class LongUtils {
    /**
     * Attempt to parse Strings as a viable Longs. If any of Strings fail parsing, throws InputArgumentFormatException
     * @param vars List of a variables for parsing
     * @return Optional of List of parsed Longs
     */
    public static Optional<List<Long>> getListOfLongsOrThrow(List<String> vars) {
        List<Long> amountOfIps = new ArrayList<>();
        if (vars.isEmpty()) {
            return Optional.empty();
        } else {
            for (String s : vars) {
                try {
                    amountOfIps.add(Long.parseLong(s));
                } catch (NumberFormatException ex) {
                    throw new InputArgumentFormatException("For -r option, only amounts of ips allowed. " + s + " could not be parsed to Long", ex);
                }
            }
        }
        return Optional.of(amountOfIps);
    }
}
