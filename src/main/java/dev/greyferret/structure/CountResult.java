package dev.greyferret.structure;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Counting result holder. Contains number of the unique ips and the amount of all the ips
 *
 * Could've used Pair<Long,Long>, but it's not the part of the default Java Core
 */
@AllArgsConstructor
@Getter
public class CountResult {
    private long unique;
    private long amount;
}
