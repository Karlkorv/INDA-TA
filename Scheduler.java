import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private File file;
    List<Person> students;
    Person.attendance[][] doubleArr;
    BufferedReader reader;
    BufferedWriter writer;
    boolean parsedList = false;

    public Scheduler(String fileName) throws IOException {
        file = new File(fileName);
        if (!file.exists()) {
            throw new IOException("File does not exist");
        }

        students = new ArrayList<>(20);
        String curLine;
        reader = new BufferedReader(new FileReader(file));

        curLine = reader.readLine();

        String[] studentList = curLine.split(",");
        for (String curStr : studentList) {
            students.add(new Person(curStr));
        }

        doubleArr = new Person.attendance[students.size()][52];

        while (reader.ready()) {
            curLine = reader.readLine();
            String[] splitLine = curLine.split(",");
            for (int i = 0; i < splitLine.length; i++) {
                students.get(i).setAttendence(i, convertToEnum(splitLine[i]));
            }
        }

        reader.close();
    }

    public List<Person> getStudents() {
        return List.copyOf(students);
    }

    Person.attendance[][] getAttendance() {
        return doubleArr;
    }

    private Person.attendance convertToEnum(String string) {
        switch (string.toLowerCase()) {
            case "y":
                return Person.attendance.Y;
            case "m":
                return Person.attendance.M;
            case "n":
                return Person.attendance.N;
            default:
                throw new IllegalArgumentException("Input string could not be parsed");
        }
    }

}
