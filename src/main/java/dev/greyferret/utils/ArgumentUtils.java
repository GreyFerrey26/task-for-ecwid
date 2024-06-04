package dev.greyferret.utils;

import dev.greyferret.exception.InputArgumentFormatException;
import dev.greyferret.structure.enumeric.RunningType;
import dev.greyferret.structure.CountResult;
import dev.greyferret.worker.IpAddrCounterWorker;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static dev.greyferret.utils.FilesUtils.getPathOrThrow;
import static dev.greyferret.utils.LongUtils.getListOfLongsOrThrow;

/***
 * Utility class for parsing arguments for an app
 */
@UtilityClass
public class ArgumentUtils {
    private static final List<Long> defaultAmountOfIps = Arrays.asList(
            1000L,
            1000000L,
            10000000L,
            100000000L
    );

    public static void parseArgumentSettingsAndRun(String[] args) throws IOException {
        if (args == null || args.length == 0) {
            // if no args provided, demonstrate this task on 1000, 1000000, 10000000 and 100000000 ips as a default
            runMode(defaultAmountOfIps);
        } else {
            // Collecting arg with prefix '-' as runningType and all other args as variables
            ArrayList<String> variables = new ArrayList<>();
            RunningType runningType = null;
            for (String s : args) {
                if (s.startsWith("-")) {
                    if (runningType != null) {
                        throw new InputArgumentFormatException("Only one mode allowed at the same time");
                    }
                    runningType = RunningType.extractOrThrow(s.substring(1));
                } else {
                    variables.add(s);
                }
            }
            if (runningType == null) {
                throw new InputArgumentFormatException("Running type argument is missing", null);
            } else {
                switch (runningType) {
                    case FILE -> fileMode(variables);
                    case GENERATE -> generateMode(variables);
                    case GENERATE_ALL -> generateAllMode(variables);
                    case RUN -> runMode(getListOfLongsOrThrow(variables)
                            .orElse(defaultAmountOfIps));
                }
            }
        }
    }

    /**
     * Validate variables for the single file path as in input and start reading and counting on it
     *
     * @param variables all variables
     * @return Count Result
     * @throws IOException IOException
     */
    private static CountResult fileMode(ArrayList<String> variables) throws IOException {
        if (variables.size() != 1) {
            throw new InputArgumentFormatException("Required single file path to read");
        } else {
            return IpAddrCounterWorker.readFileAndCount(getPathOrThrow(variables.get(0)));
        }
    }

    /**
     * Validate variables for path to the file and amount of ips to generate and start generating and counting on it
     * Will override the old file
     *
     * @param variables all args that didn't start with a '-'
     * @throws IOException IOException
     */
    private static void generateMode(ArrayList<String> variables) throws IOException {
        if (variables.size() != 2) {
            throw new InputArgumentFormatException("Required single file path to generate (1st) and amount of ips to generate (2nd)");
        } else {
            String amountOfIpsString = variables.get(1);
            long amountOfIps;
            try {
                amountOfIps = Long.parseLong(amountOfIpsString);
            } catch (NumberFormatException ex) {
                throw new InputArgumentFormatException("Can't parse amount of ips to generate. " + amountOfIpsString + " could not be parsed to Long", ex);
            }
            IpAddrCounterWorker.generateFileWithRandomIps(variables.get(0), amountOfIps);
        }
    }

    /**
     * Validate variables for path to the file and amount of ips to generate and start generating and counting on it
     * Will override the old file
     *
     * @param variables all args that didn't start with a '-'
     * @throws IOException IOException
     */
    private static void generateAllMode(ArrayList<String> variables) throws IOException {
        if (variables.size() != 1) {
            throw new InputArgumentFormatException("Required single file path to generate all ips");
        } else {
            IpAddrCounterWorker.generateFileWithAllIps(variables.get(0));
        }
    }

    private static void runMode(List<Long> variables) throws IOException {
        IpAddrCounterWorker.generateTempFileAndCount(variables);
    }
}
