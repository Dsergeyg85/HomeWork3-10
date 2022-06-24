import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
public class MainTest {
    final String CSVFILE = "./src/test/TestFiles/test_data.csv";
    final String XMLFILE = "./src/test/TestFiles/test_data.xml";
    @Test
    public void testFileNotFound () {
        try (FileReader fileReader = new FileReader(Main.fileName))  {
            Assertions.assertFalse(false);
        } catch (FileNotFoundException e) {
            Assertions.assertFalse(true);
        } catch (IOException e) {
            Assertions.assertFalse(false);
        }
    }
    @Test
    public void testParseCSV() {
        String[] columnMapping = Main.columnMapping;
        List<Employee> expectedObject = Main.parseCSV(columnMapping, CSVFILE);
        Assertions.assertNotNull(expectedObject);
        Assertions.assertFalse(expectedObject.isEmpty());
    }
    @Test
    public void testParseXML() {
        List<Employee> expectedObject = Main.parseXML(XMLFILE);
        Assertions.assertNotNull(expectedObject);
        Assertions.assertFalse(expectedObject.isEmpty());
    }
    @Test
    public void testListToJson() {
        String[] columnMapping = Main.columnMapping;
        List<Employee> list = Main.parseCSV(columnMapping, CSVFILE);
        String result = Main.listToJson(list);
        String expectedObject = "Test String";
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertSame(expectedObject.getClass(), result.getClass());
    }
    @AfterAll
    public static void testJsonCSVMessage() {
        String json = Main.readString(Main.outputJsonFileForCSV);
        String result1 = "{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Smith\",\"country\":\"USA\",\"age\":25}";
        String result2 = "{\"id\":2,\"firstName\":\"Inav\",\"lastName\":\"Petrov\",\"country\":\"RU\",\"age\":23}";
        assertThat(json, allOf(containsString(result1), containsString(result2)));
    }

    @AfterAll
    public static void testJsonXMLMessage() {
        String json = Main.readString(Main.outputJsonFileForXML);
        String result1 = "{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Smith\",\"country\":\"USA\",\"age\":25}";
        String result2 = "{\"id\":2,\"firstName\":\"Inav\",\"lastName\":\"Petrov\",\"country\":\"RU\",\"age\":23}";
        assertThat(json, allOf(containsString(result1), containsString(result2)));
    }
}
