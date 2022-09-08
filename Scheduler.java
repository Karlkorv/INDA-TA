import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Scheduler {
    private List<String> students;
    private String[] attendingStudents;
    private BufferedWriter writer;
    private BufferedReader inputReader;
    private final String FILE_NAME = "attendance.csv";

    public Scheduler() throws IOException {
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

    public void getAttendance() throws IOException {
        attendingStudents = new String[students.size()];
        inputReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Är följande student här? y/n");

        for (int i = 0; i < students.size(); i++) {
            System.out.println(students.get(i));
            String readLine = inputReader.readLine().trim().toLowerCase();
            while (!(readLine.equals("y") || readLine.equals("n"))) {
                System.out.println("Fel input, svara y/n");
                readLine = inputReader.readLine().trim().toLowerCase();
            }
            attendingStudents[i] = readLine;
        }
    }

    public void writeAttendance(String[] attendance) throws IOException {
        writer = new BufferedWriter(new FileWriter(FILE_NAME, true)); // Append flag = true

        writer.newLine();

        for (int i = 0; i < attendance.length; i++) {
            writer.write(attendance[i]);
            if (i + 1 < attendance.length)
                writer.write(',');
        }
        writer.close();
    }

    public void randomStudent() {
        List<String> listCopy = new ArrayList<>(students); // Copy of, not reference to
        Collections.shuffle(listCopy);
        for (String string : listCopy) {
            System.out.println(string);
        }
    }

    public void randomStudent(int groupAmount) {
        int studentsHere = attendingStudents.length;
        int groups = studentsHere / groupAmount;
        int leftovers = 0;
        if (studentsHere % groups > groupAmount - 1) {
            groups++;
        } else if (studentsHere % groups > groupAmount - 1) {
            leftovers = studentsHere % groups;
        }
        String[][] groupArr = new String[groups][groupAmount + 1];
        List<String> randomList = new ArrayList<>(students);
        Stack<String> randomStack = new Stack<>();
        for (String string : randomList) {
            randomStack.add(string);
        }

        Collections.shuffle(randomList);
        for (int i = 0; i < groupArr.length; i++) {
            for (int j = 0; j < groupAmount; j++) {
                if (leftovers > 0 && i + 1 == groupArr.length) {
                    groupArr[i][groupAmount] = randomStack.pop();
                } else {
                    groupArr[i][j] = randomStack.pop();
                }
                // TODO: Testa denna skeva setup
            }
        }

        for (String[] strings : groupArr) {
            System.out.println("grupp:");
            for (String string : strings) {
                System.out.println(string);
            }
            System.out.println("\n");
        }
    }

    public static void main(String[] args) {
        // TODO: Main
    }
}