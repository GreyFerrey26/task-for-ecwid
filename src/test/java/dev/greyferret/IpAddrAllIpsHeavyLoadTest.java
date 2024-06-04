package dev.greyferret;

import dev.greyferret.structure.CountResult;
import dev.greyferret.worker.IpAddrCounterWorker;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This test would run only if there is an Environment Variable 'IpAddrAllIpsHeavyLoadTest' = true
 */
public class IpAddrAllIpsHeavyLoadTest {
    Logger logger = LoggerFactory.getLogger(IpAddrAllIpsHeavyLoadTest.class);

    private final String envVarName = "IpAddrAllIpsHeavyLoadTest";

    @Test
    public void testIpAddrAllIpsHeavyLoad() throws IOException {
        String envValue;
        try {
            envValue = System.getenv(envVarName);
        } catch (SecurityException ex) {
            logger.warn("Could not get environment variable " + envVarName, ex);
            return;
        }
        if (envValue != null && envValue.equalsIgnoreCase("true")) {
            logger.warn("Warning: IpAddrAllIpsHeavyLoadTest has started. It'll create a 64Gb file, attempt to read it and delete it later");
            Path path = IpAddrCounterWorker.generateFileWithAllIps(null);
            path.toFile().deleteOnExit();
            CountResult countResult = IpAddrCounterWorker.readFileAndCount(path);
            Assert.assertEquals(0x100000000L, countResult.getAmount());
            Assert.assertEquals(0x100000000L, countResult.getUnique());
            try {
                boolean b = Files.deleteIfExists(path);
                if (b) {
                    logger.info("File {} successfully deleted", path.toAbsolutePath());
                    return;
                }
            } catch (IOException ex) {
                logger.warn("File {} could not be deleted", path.toAbsolutePath(), ex);
            }
            logger.warn("File {} could not be deleted", path.toAbsolutePath());
        }
    }
}
