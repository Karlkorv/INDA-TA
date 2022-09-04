import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    List<String> students;
    BufferedWriter writer;
    BufferedReader inputReader;
    final String FILE_NAME = "attendance.csv";

    public Scheduler(String fileName) throws IOException {
        inputReader = new BufferedReader(new FileReader("attendance.csv"));
        String firstLine = inputReader.readLine();

        if (firstLine.isBlank()) {
            throw new IOException("First line was blank, exiting");
        }

        firstLine = firstLine.trim();
        String[] splitLine = firstLine.split(",");
        students = new ArrayList<>(splitLine.length);

        for (String string : splitLine) {
            students.add(string);
        }

        inputReader.close();
    }

    public String[] getAttendance() throws IOException {
        String[] returnArr = new String[students.size()];
        inputReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Är följande student här? y/n");

        for (int i = 0; i < students.size(); i++) {
            System.out.println(students.get(i));
            String readLine = inputReader.readLine().trim().toLowerCase();

            while (readLine != "y" || readLine != "n") {
                System.out.println("Fel input, svara y/n");
            }
            returnArr[i] = readLine;
        }

        return returnArr;
    }

    public void writeAttendance(String[] attendance) throws IOException {
        writer = new BufferedWriter(new FileWriter(FILE_NAME, true)); // Append flag = true

        writer.newLine();

        for (int i = 0; i < attendance.length; i++) {
            writer.write(attendance[i]);
            if (i + 1 < attendance.length)
                writer.write(',');

        }
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
