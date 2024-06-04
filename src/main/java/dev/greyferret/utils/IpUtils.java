package dev.greyferret.utils;

import dev.greyferret.structure.CountResult;
import dev.greyferret.structure.IntRepresentationCollector;
import lombok.experimental.UtilityClass;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

@UtilityClass
public class IpUtils {
    public static final int PART_OF_IP_MAX = Byte.SIZE * 32; //256

    /***
     * Method calculates and returns amount of unique ips in the input Stream
     * For the task purposes, there is no validation for each line, implying that every line contains one single valid ip
     * @param linesStream input Stream of String
     * @return amount of unique ips in the Stream
     */
    public static CountResult countUniques(Stream<String> linesStream) {
        try {
            IntRepresentationCollector intRepresentationCollector = linesStream
                    .mapToInt(IpUtils::toIntRepresentation)
                    .collect(IntRepresentationCollector::new, IntRepresentationCollector::accumulator, IntRepresentationCollector::combiner);
            return new CountResult(intRepresentationCollector.getAmountOfUniqueIps(), intRepresentationCollector.getAmountOfIps());
        } finally {
            if (linesStream != null) {
                linesStream.close();
            }
        }
    }

    /***
     * Transformation of an ip address into it's int representation
     * @param cs Char Sequence of a line, containing ip
     * @return int representation of an ip
     */
    private static int toIntRepresentation(CharSequence cs) {
        // result int
        int res = 0;
        // temp variable to hold current part of the ip
        int curr = 0;
        for (int i = 0; i < cs.length(); ++i) {
            char ch = cs.charAt(i);
            if (ch != '.') {
                // collect int value of a single part of the ip
                curr = curr * 10 + ch - '0';
            } else {
                // when dot is met, part of an ip is ended; Shifting the already collected data for 1 byte
                res = (res << Byte.SIZE);
                // and using OR operator to store the currently collected part of the ip on fresh zeroes from shift
                res = res | curr;
                // reset current
                curr = 0;
            }
        }
        // The last part of the ip doesn't end with dot, but with the end of a char sequence.
        // Collecting data the same way as above
        res = (res << Byte.SIZE);
        res = res | curr;
        return res;
    }

    public static String generateRandomIp() {
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        return threadLocalRandom.nextInt(PART_OF_IP_MAX) + "." +
                threadLocalRandom.nextInt(PART_OF_IP_MAX) + "." +
                threadLocalRandom.nextInt(PART_OF_IP_MAX) + "." +
                threadLocalRandom.nextInt(PART_OF_IP_MAX);
    }
}
