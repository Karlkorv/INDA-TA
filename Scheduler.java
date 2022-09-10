import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Scheduler {
    private List<String> students;
    private List<String> attendingStudents;
    private String[] attendance;
    private BufferedWriter writer;
    private BufferedReader inputReader;
    private final String FILE_NAME = "attendance.csv";

    public Scheduler() throws IOException {
        attendingStudents = new ArrayList<>();
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
        attendance = new String[students.size()];
        inputReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Är följande student här? y/n");

        for (int i = 0; i < students.size(); i++) {
            System.out.println(students.get(i));
            String readLine = inputReader.readLine().trim().toLowerCase();
            while (!(readLine.equals("y") || readLine.equals("n"))) {
                System.out.println("Fel input, svara y/n");
                readLine = inputReader.readLine().trim().toLowerCase();
            }
            attendance[i] = readLine;
            if (readLine.equals("y")) {
                attendingStudents.add(students.get(i));
            }
        }
    }

    public void writeAttendance() throws IOException {
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

    public void randomStudent(int studentPerGroup) {
        Collections.shuffle(attendingStudents);
        int nrOfGroups = attendingStudents.size() / studentPerGroup;
        int leftOvers = attendingStudents.size() % studentPerGroup;
        List<List<String>> groups = new ArrayList<>(nrOfGroups);

        for (int i = 0; i < nrOfGroups; i++) {
            groups.add(new ArrayList<String>());
        }

        for (int i = 0, j = 0; i < attendingStudents.size(); i++, j++) {
            j = j % nrOfGroups;
            groups.get(j).add(attendingStudents.get(i));
        }

        System.out.println();
        System.out.println("Grupper:");
        for (List<String> list : groups) {
            for (String student : list) {
                System.out.println(student);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        try {
            Scheduler scheduler = new Scheduler();
            scheduler.getAttendance();
            scheduler.randomStudent(3);
            scheduler.writeAttendance();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}