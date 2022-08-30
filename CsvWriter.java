import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CsvWriter {
    private File file;
    List<Person> students;
    BufferedReader reader;

    public CsvWriter(String fileName) {
        file = new File(fileName);
        students = new ArrayList<>(20);
        String curLine;
        boolean isFirstLine = true;

        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        try {
            while (reader.ready()) {
                curLine = reader.readLine();
                if (isFirstLine) {
                    String[] studentList = curLine.split(",");
                    for (String curStr : studentList) {
                        students.add(new Person(curStr));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
