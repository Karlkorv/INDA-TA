import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
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

    public void readAttendance() {
        String curLine = "";
        try {
            inputReader = new BufferedReader(new FileReader(FILE_NAME));
            curLine = inputReader.readLine();
            while (curLine != null) {
                String newLine = inputReader.readLine();

                if (newLine == null) {
                    break;
                } else {
                    curLine = newLine;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        String[] splitLine = curLine.split(",");

        for (int i = 0; i < students.size(); i++) {
            if (splitLine[i].equals("y")) {
                attendingStudents.add(students.get(i)); // Add all students from last recorded attendance
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
        List<List<String>> groups = new ArrayList<>(nrOfGroups);

        for (int i = 0; i < nrOfGroups; i++) {
            groups.add(new ArrayList<String>());
        }

        for (int i = 0, j = 0; i < attendingStudents.size(); i++, j++) {
            j = j % nrOfGroups;
            groups.get(j).add(attendingStudents.get(i));
        }

        System.out.println();
        System.out.format("Grupper a %d styck:\n", studentPerGroup);
        for (List<String> list : groups) {
            for (String student : list) {
                System.out.println(student);
            }
            System.out.println();
        }
    }

    private void parseCommands(String[] args) throws IOException {
        if (args.length == 0) {
            printHelp();
            return;
        }

        boolean gotAttendance = false;

        // Command precedence:
        // -get, -read/-r, -group

        List<String> commands = new ArrayList<>(Arrays.asList(args));

        if (commands.contains("get")) {
            getAttendance();
            gotAttendance = true;
        }

        if (!gotAttendance && (commands.contains("read") || commands.contains("r"))) {
            readAttendance();
            gotAttendance = true;
        }

        if (commands.contains("group")) {
            if (!gotAttendance) {
                System.out.println("No attendance method specified, reading last");
                readAttendance();
                gotAttendance = true;
            }
            int groupAmount = Integer.parseInt(commands.get(commands.indexOf("group") + 1));
            randomStudent(groupAmount);
        }
    }

    private void printHelp() {
    }

    public static void main(String[] args) {
        try {
            Scheduler scheduler = new Scheduler();
            scheduler.parseCommands(args);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

}