package dev.greyferret.utils;

import dev.greyferret.exception.PathFileSyntaxException;
import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static dev.greyferret.utils.IpUtils.generateRandomIp;
import static dev.greyferret.utils.IpUtils.PART_OF_IP_MAX;

/***
 * Utility class for File Generation
 */
@UtilityClass
public class FilesUtils {
    Logger logger = LoggerFactory.getLogger(FilesUtils.class);

    public static Path generateFileWithRandomIps(String filePath, Long amountOfIps) throws IOException {
        Path path = filePath == null ?
                Files.createTempFile("ips_" + amountOfIps + "_", ".txt") :
                getPathOrThrow(filePath);
        File file = path.toFile();
        if (filePath != null && file.delete()) {
            logger.info("Already existing file {} deleted", path.toAbsolutePath());
        }
        boolean firstLine = true;
        logger.info("Creating file {}", path.toAbsolutePath());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writeRandomIp(writer, firstLine, amountOfIps);
        }
        return path;
    }

    /**
     * Generate File With All Ips. Warning: it would allocate about 64 Gb on your disk and take around 10 minutes!
     *
     * @param filePath nullable path to file; would create temp file if null
     * @return path to a file
     * @throws IOException IOException
     */
    public static Path generateFileWithAllIps(String filePath) throws IOException {
        Path path = filePath == null ?
                Files.createTempFile("ips_all_", ".txt") :
                getPathOrThrow(filePath);
        boolean firstLine = true;
        logger.info("Creating file {}", path.toAbsolutePath());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile(), true))) {
            for (int i1 = 0; i1 < PART_OF_IP_MAX; i1++) {
                for (int i2 = 0; i2 < PART_OF_IP_MAX; i2++) {
                    for (int i3 = 0; i3 < PART_OF_IP_MAX; i3++) {
                        for (int i4 = 0; i4 < PART_OF_IP_MAX; i4++) {
                            if (!firstLine) {
                                writer.newLine();
                            } else {
                                firstLine = false;
                            }
                            writer.write(i1 + "." + i2 + "." + i3 + "." + i4);
                        }
                    }
                }
            }
        }
        if (filePath == null) {
            // it's a temporary file
            path.toFile().deleteOnExit();
        }
        return path;
    }

    public static Path getPathOrThrow(String pathString) {
        try {
            return Paths.get(pathString);
        } catch (InvalidPathException ex) {
            throw new PathFileSyntaxException(pathString, ex);
        }
    }

    private static void writeRandomIp(BufferedWriter writer, boolean firstLine, Long amountOfIps) throws IOException {
        for (int i = 0; i < amountOfIps; i++) {
            if (!firstLine) {
                writer.newLine();
            } else {
                firstLine = false;
            }
            writer.write(generateRandomIp());
        }
    }
}
