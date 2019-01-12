import org.junit.Test;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesTest {
    @Test
    public void test() {
        String resource = "simontConfig.properties";
        Properties properties = new Properties();

        try {
            InputStream inputStream = PropertiesTest.class.getResourceAsStream(resource);
            properties.load(inputStream);
            inputStream.close();

            System.out.println(properties.getProperty("simont.cpu.threshold"));
            System.out.println(properties.getProperty("simont.memory.threshold"));
            System.out.println(properties.getProperty("slack.notify"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
