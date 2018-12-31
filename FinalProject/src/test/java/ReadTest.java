import javafx.fxml.FXMLLoader;
import org.junit.Test;

import java.io.InputStream;
import java.util.Properties;
import org.junit.Assert;

public class ReadTest {
    @Test
    public void PropertiesReadTest() {
        Properties properties = new Properties();
        try {
            InputStream Is = ReadTest.class.getClassLoader().getResourceAsStream("HuLuBrothers.properties");
            properties.load(Is);
        } catch (Exception ex) {
            System.out.println("Read properties error!");
        }
        String creatureURL=properties.getProperty("CreatureUrl");
        Assert.assertNotNull(creatureURL);
        Assert.assertEquals(creatureURL,"/image/hulu.png");
    }

    @Test
    public void fxmlReadTest() {
        try {
            Assert.assertNotNull(FXMLLoader.load(getClass().getResource("/sample.fxml")));
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void RecordReadTest() {
        InputStream Is = ReadTest.class.getClassLoader().getResourceAsStream("record.txt");
        Assert.assertNotNull(Is);
    }
}