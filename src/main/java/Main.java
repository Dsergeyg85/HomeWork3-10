import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public  static List<Employee> employees = new ArrayList<>();
    public static void main(String[] args) {
        String[] columnMapping = { "id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        String fileXMLName = "data.xml";
        String outputJsovFileForCSV = "new_data_csv.json";
        String outputJsovFileForXML = "new_data_xml.json";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString(json, outputJsovFileForCSV);
        list = parseXML(fileXMLName);
        json = listToJson(list);
        writeString(json, outputJsovFileForXML);

        System.out.println("For csv file:");
        json = readString(outputJsovFileForCSV);
        List<Employee> employeeList = jsonToList(json);
        employeeList.forEach(System.out::println);

        System.out.println("\n" + "For XML file:");
        json = readString(outputJsovFileForXML);
        employeeList = jsonToList(json);
        employeeList.forEach(System.out::println);



    }
    private static List<Employee> jsonToList (String json) {
        JSONParser jsonParser = new JSONParser();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        try {
            JSONArray jsonArray = (JSONArray) jsonParser.parse(json);
            List<Employee> employees = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                Employee employee = gson.fromJson(jsonArray.get(i).toString(), Employee.class);
                employees.add(employee);
            }
            //Второй вариант
        /*Type listType = new TypeToken<List<Employee>>() {}.getType();
        List<Employee> employeeList = gson.fromJson(json, listType);*/
            return employees;
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    private static String readString(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))){
            String line;
            while ((line = reader.readLine()) != null) {
                return line;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    private static List<Employee> parseCSV(String[] columnMapping, String fileName)  {
        CSVParser csvParser = new CSVParserBuilder().withSeparator(',').build();
        try(CSVReader reader = new CSVReaderBuilder(new FileReader(fileName)).withCSVParser(csvParser).build()) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            return csv.parse();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    private static String listToJson(List<Employee> list) {
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.toJson(list, listType);
    }
    private static void writeString(String json, String fileName) {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(json);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    private static List<Employee> parseXML(String fileXMLName) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(fileXMLName));
            Node root = document.getDocumentElement();
            read(root);
            } catch (ParserConfigurationException | IOException | SAXException e) {
            System.out.println(e.getMessage());
        }
        return employees;
    }
    private static void read(Node node) {
        String id = null;
        String firstName = null;
        String lastName = null;
        String country = null;
        String age = null;
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i ++) {
            Node node_ = nodeList.item(i);
             if (Node.ELEMENT_NODE == node_.getNodeType()) {
                 Element element = (Element) node_.getChildNodes();
                 if (element.getNodeName().equals("id")) {
                     id = element.getTextContent();
                 } else if (node_.getNodeName().equals("firstName")) {
                     firstName = node_.getFirstChild().getNodeValue();
                 } else if (node_.getNodeName().equals("lastName")) {
                     lastName = node_.getFirstChild().getNodeValue();
                 } else if (node_.getNodeName().equals("country")) {
                     country = node_.getFirstChild().getNodeValue();
                 } else if (node_.getNodeName().equals("age")) {
                     age = element.getFirstChild().getNodeValue();
                 }
                 if (id != null && firstName != null && lastName != null && country != null && age != null) {
                     employees.add(new Employee(Integer.parseInt(id), firstName, lastName, country, Integer.parseInt(age)));
                 }
                read(node_);
            }
        }
    }
}
