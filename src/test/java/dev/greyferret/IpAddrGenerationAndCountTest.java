package dev.greyferret;

import dev.greyferret.structure.CountResult;
import dev.greyferret.worker.IpAddrCounterWorker;
import lombok.Getter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
@Getter
public class IpAddrGenerationAndCountTest {
    private final Long amount;

    public IpAddrGenerationAndCountTest(Long amount) {
        this.amount = amount;
    }

    @Parameterized.Parameters
    public static List amounts() {
        return Arrays.asList(
                1000L,
                10000L,
                100000L,
                1000000L,
                10000000L,
                100000000L
        );
    }

    @Test
    public void generateAndCount() {
        try {
            Path path = IpAddrCounterWorker.generateFileWithRandomIps(amount);
            Assert.assertTrue(path.toFile().exists());
            CountResult countResult = IpAddrCounterWorker.readFileAndCount(path);
            Assert.assertEquals(countResult.getAmount(), (long) amount);
            path.toFile().deleteOnExit();
        } catch (IOException e) {
            Assert.fail();
        }
    }
}
