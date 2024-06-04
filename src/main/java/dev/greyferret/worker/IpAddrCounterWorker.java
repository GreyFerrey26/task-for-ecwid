package dev.greyferret.worker;

import dev.greyferret.exception.FileAccessException;
import dev.greyferret.structure.CountResult;
import dev.greyferret.utils.FilesUtils;
import dev.greyferret.utils.IpUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IpAddrCounterWorker {
    private static final Logger logger = LoggerFactory.getLogger(IpAddrCounterWorker.class);

    /**
     * Generate temporary file with specified amount of ips and count uniques; Deletes files afterward
     *
     * @param amountOfIpsForFiles amount of ips to generate
     * @throws IOException IOException
     */
    public static void generateTempFileAndCount(List<Long> amountOfIpsForFiles) throws IOException {
        for (Long amountOfIps : amountOfIpsForFiles) {
            Path path = generateFileWithRandomIps(amountOfIps);
            readFileAndCount(path);

            try {
                Files.delete(path);
                logger.info("File '{}' deleted", path.toAbsolutePath());
            } catch (IOException ex) {
                logger.error("Error deleting temp file '{}'", path.toAbsolutePath(), ex);
            }
            if (amountOfIpsForFiles.size() > 1) {
                //empty line for easier read
                logger.info("");
            }
        }
    }

    /**
     * Reads existing file and counts unique ips; Does not delete file afterward
     *
     * @param path path to the file
     * @return CountResult, containing amount of unique ips and the amount of all ips in the file
     * @throws IOException IOException
     */
    public static CountResult readFileAndCount(Path path) throws IOException {
        if (!path.toFile().isFile()) {
            throw new FileAccessException("File not found: " + path.toAbsolutePath());
        }
        LocalDateTime countFrom = LocalDateTime.now();
        logger.info("Started reading file '{}'", path.toAbsolutePath());
        CountResult countResult = IpUtils.countUniques(Files.lines(path, StandardCharsets.UTF_8));
        logger.info("Time to count uniques in {} ips: {} ms",
                countResult.getAmount(), ChronoUnit.MILLIS.between(countFrom, LocalDateTime.now()));

        logger.info("{}/{} uniques found", countResult.getUnique(), countResult.getAmount());
        return countResult;
    }

    /**
     * Simple generation of a file with specified number of random ips.
     * Will attempt to override file if exists
     *
     * @param amountOfIps amount of ips to create
     * @return CountResult, containing amount of unique ips and the amount of all ips in the file
     * @throws IOException IOException
     */
    public static Path generateFileWithRandomIps(Long amountOfIps) throws IOException {
        return generateFileWithRandomIps(null, amountOfIps);
    }

    /**
     * Simple generation of a file with specified number of random ips.
     * Will attempt to override file if exists
     *
     * @param filePath    file path for file to create; if null, the temporary file would be created
     * @param amountOfIps amount of ips to create
     * @return CountResult, containing amount of unique ips and the amount of all ips in the file
     * @throws IOException IOException
     */
    public static Path generateFileWithRandomIps(String filePath, Long amountOfIps) throws IOException {
        LocalDateTime fileGenFrom = LocalDateTime.now();
        Path path = FilesUtils.generateFileWithRandomIps(filePath, amountOfIps);
        logger.info("File '{}' with {} ips generated in {} ms", path.toAbsolutePath(), amountOfIps, ChronoUnit.MILLIS.between(fileGenFrom, LocalDateTime.now()));
        return path;
    }

    /**
     * Simple generation of a file with specified number of all possible ips.
     * Will attempt to override file if exists
     *
     * @param filePath    file path for file to create; if not specified, the temporary file would be created
     * @return CountResult, containing amount of unique ips and the amount of all ips in the file
     * @throws IOException IOException
     */
    public static Path generateFileWithAllIps(String filePath) throws IOException {
        LocalDateTime fileGenFrom = LocalDateTime.now();
        Path path = FilesUtils.generateFileWithAllIps(filePath);
        logger.info("File '{}' with all possible ips generated in {} ms", path.toAbsolutePath(), ChronoUnit.MILLIS.between(fileGenFrom, LocalDateTime.now()));
        return path;
    }
}
