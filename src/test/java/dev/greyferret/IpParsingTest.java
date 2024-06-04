package dev.greyferret;

import dev.greyferret.structure.CountResult;
import dev.greyferret.utils.IpUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This is an important test, because the integer representation of a 127.255.255.255 and 128.0.0.0 equals
 * biggest and lowest integer possible, and they can't be fit in BitSet.
 * This set emulates 2 generic ips and those 2 special ips and expects them all be calculated right
 */
public class IpParsingTest {
    @Test
    public void testIpParsing() {
        Set<String> fourIpsWithBiggestAndLowestSet = new HashSet<>();
        fourIpsWithBiggestAndLowestSet.add("127.255.255.255");
        fourIpsWithBiggestAndLowestSet.add("128.0.0.0");
        while (fourIpsWithBiggestAndLowestSet.size() < 4) {
            fourIpsWithBiggestAndLowestSet.add(IpUtils.generateRandomIp());
        }

        CountResult countResult4Different = IpUtils.countUniques(fourIpsWithBiggestAndLowestSet.stream());
        Assert.assertEquals(4, countResult4Different.getAmount());
        Assert.assertEquals(4, countResult4Different.getUnique());

        List<String> fourIpsWithBiggestAndLowestList = new java.util.ArrayList<>(fourIpsWithBiggestAndLowestSet.stream().toList());
        fourIpsWithBiggestAndLowestList.add(fourIpsWithBiggestAndLowestList.get(ThreadLocalRandom.current().nextInt(4)));
        fourIpsWithBiggestAndLowestList.add(fourIpsWithBiggestAndLowestList.get(ThreadLocalRandom.current().nextInt(4)));
        CountResult countResult6With2Copies = IpUtils.countUniques(fourIpsWithBiggestAndLowestList.stream());
        Assert.assertEquals(6, countResult6With2Copies.getAmount());
        Assert.assertEquals(4, countResult6With2Copies.getUnique());
    }

    @Test
    public void testIpParsingManyDuplicates() {
        Set<String> duplicates = new HashSet<>();
        while (duplicates.size() < 100) {
            duplicates.add(IpUtils.generateRandomIp());
        }
        List<String> duplicatesAsList = duplicates.stream().toList();
        List<String> ips = new ArrayList<>();
        ips.addAll(duplicatesAsList);
        for (int i = 0; i < 100000; i++) {
            ips.add(duplicatesAsList.get(ThreadLocalRandom.current().nextInt(100)));
        }

        CountResult countResult = IpUtils.countUniques(ips.stream());
        Assert.assertEquals(100100, countResult.getAmount());
        Assert.assertEquals(100, countResult.getUnique());
    }
}
