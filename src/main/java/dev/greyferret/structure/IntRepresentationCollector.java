package dev.greyferret.structure;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.BitSet;

/***
 * Structure to collect and check ip's availability in input file.
 *
 * There are 2^32 ipv4 ips available. Because there is no UInt in Java, input int could be positive and negative.
 * To work around it, instead of single BitSet/Array w/ length 2^32,
 * two BitSets w/ size 2^31-1 are used: one for positive values and one for negatives.
 *
 * However, by using two 2^31-1, we are losing two elements, max and min. To work around it, we could create third BitSet, or,
 * alternately, use two booleans to keep a track of meeting max and min ints (ips 127.255.255.255 and 128.0.0.0)
 */
@NoArgsConstructor
@Getter
public class IntRepresentationCollector {
    BitSet positiveHalf = new BitSet(Integer.MAX_VALUE);
    BitSet negativeHalf = new BitSet(Integer.MAX_VALUE);
    boolean maxValueFound = false;
    boolean minValueFound = false;
    // Optional amount of counter ips, for better output
    long amountOfIps = 0;

    public void accumulator(int ip) {
        amountOfIps++;
        if (ip == Integer.MAX_VALUE) {
            maxValueFound = true;
        } else if (ip == Integer.MIN_VALUE) {
            minValueFound = true;
        } else if (ip >= 0) {
            positiveHalf.set(ip);
        } else {
            negativeHalf.set(~ip);
        }
    }

    /***
     * Method to combine two IntRepresentationCollector structures.
     * This method isn't used for task purposes and isn't tested
     * @param otherCollector other collector
     */
    public void combiner(IntRepresentationCollector otherCollector) {
        positiveHalf.or(otherCollector.positiveHalf);
        negativeHalf.or(otherCollector.negativeHalf);
        maxValueFound = maxValueFound || otherCollector.maxValueFound;
        minValueFound = minValueFound || otherCollector.minValueFound;
    }

    public long getAmountOfUniqueIps() {
        long positiveCardinality = this.getPositiveHalf().cardinality();
        long negativeCardinality = this.getNegativeHalf().cardinality();
        long result = positiveCardinality + negativeCardinality;
        if (maxValueFound) {
            result++;
        }
        if (minValueFound) {
            result++;
        }
        return result;
    }
}
