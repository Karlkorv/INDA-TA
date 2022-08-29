import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CsvWriter {
    private File file;
    List<Person> students;

    public CsvWriter(String fileName) {
        file = new File(fileName);
        students = new ArrayList<>(20);
    }
}
