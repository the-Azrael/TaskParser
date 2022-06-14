import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.List;

public class Main {
    public static String fileName = "data.csv";
    public static String fileNameJSON = "data.json";
    public static String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
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

    public static String listToJSON(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        String json = gson.toJson(list, listType);
        return json;
    }

    public static void writeString(String fileName, String jsonString) {
        try {
            FileWriter fw = new FileWriter(fileName);
            fw.write(jsonString);
            fw.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJSON(list);
        System.out.println(json);
        writeString(fileNameJSON, json);
    }

}
