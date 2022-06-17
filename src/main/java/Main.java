import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> staff;
        try {
            CSVReader csvReader = new CSVReader(new FileReader(fileName));
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            staff = csv.parse();
            return staff;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private static String listToJSON(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        return gson.toJson(list, listType);
    }

    private static int getElementsCount(Document doc) {
        return doc.getElementsByTagName("employee").getLength();
    }

    private static String getElementByTag(Document doc, int index, String tag) {
        NodeList nl = doc.getElementsByTagName(tag);
        return nl.item(index).getChildNodes().item(0).getNodeValue();
    }

    private static Employee readElement(Document doc, int index, String[] columns) {
        long id = Long.parseLong(getElementByTag(doc, index, columns[0]));
        String firstName = getElementByTag(doc, index, columns[1]);
        String lastName = getElementByTag(doc, index, columns[2]);
        String country = getElementByTag(doc, index, columns[3]);
        int age = Integer.parseInt(getElementByTag(doc, index, columns[4]));
        return new Employee(id, firstName, lastName, country, age);
    }

    private static List<Employee> readDocumentToEmployeeList(Document doc) {
        List<Employee> employeesList = new ArrayList<>();
        int len = getElementsCount(doc);
        for (int i = 0; i < len; i++) {
            employeesList.add(readElement(doc, i, columnMapping));
        }
        return employeesList;
    }

    private static List<Employee> parseXML(String fileName) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document xmlDoc = builder.parse(new File(fileName));
            return readDocumentToEmployeeList(xmlDoc);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private static void writeString(String fileName, String jsonString) {
        try {
            FileWriter fw = new FileWriter(fileName);
            fw.write(jsonString);
            fw.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        //Задание 1
        String csvFileName = "data.csv";
        String fileCSVtoJSON = "dataFromCSV.json";
        writeString(fileCSVtoJSON, listToJSON(parseCSV(columnMapping, csvFileName)));

        //Задание 2
        String xmlFileName = "data.xml";
        String fileXMLtoJSON = "dataFromXML.json";
        writeString(fileXMLtoJSON, listToJSON(parseXML(xmlFileName)));
    }

}
