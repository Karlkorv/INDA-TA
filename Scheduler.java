import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Scheduler {
    private List<String> students;
    private List<String> attendingStudents;
    private String[] attendance;
    private BufferedWriter writer;
    private BufferedReader inputReader;

    private static final String FILE_NAME = "attendance.csv";
    private static final String GROUP_ARG = "-group";
    private static final String GET_ARG = "-get";
    private static final String READ_ARG = "-read";
    private static final String TEMP_ARG = "-temp";

    /**
     * 
     * Creates a new instance of Scheduler. Reads in the students from a CSV file.
     * 
     * @throws IOException thrown if there is a problem reading the CSV file
     */
    public Scheduler() throws IOException {
        attendingStudents = new ArrayList<>();
        inputReader = new BufferedReader(new FileReader(FILE_NAME));
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

    /**
     * 
     * Lets the user input attendance for each student.
     * 
     * @throws IOException thrown if there is a problem reading user input
     */
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

    /**
     * 
     * Reads in attendance from a CSV file.
     */
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

        if (splitLine.length != students.size() + 1) // + 1 due to date
            throw new RuntimeException("Split attendance line length not equal to student amount");

        for (int i = 1; i < students.size(); i++) { // i = 1 since first index is date
            if (splitLine[i].equals("y")) {
                attendingStudents.add(students.get(i - 1)); // Add all students from last recorded attendance
            }
        }
    }

    /**
     * 
     * Writes attendance to the CSV file.
     * 
     * @throws IOException thrown if there is a problem writing to the CSV file
     */
    public void writeAttendance() throws IOException {
        writer = new BufferedWriter(new FileWriter(FILE_NAME, true)); // Append flag = true

        writer.newLine();

        // write the current date in MM-DD format
        writer.write(new Date().toString().substring(4, 10) + ",");

        for (int i = 0; i < attendance.length; i++) {
            writer.write(attendance[i]);
            if (i + 1 < attendance.length)
                writer.write(',');
        }
        writer.close();
    }

    /**
     * 
     * Prints a random student from the list of all students.
     */
    public void randomStudent() {
        List<String> listCopy = new ArrayList<>(students); // Copy of, not reference to
        Collections.shuffle(listCopy);
        for (String string : listCopy) {
            System.out.println(string);
        }
    }

    /**
     * 
     * Divides attending students into random groups and prints the groups.
     * 
     * @param studentPerGroup the number of students per group
     */
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

    /**
     * 
     * Handles the arguments sent to the program and calls the appropriate methods.
     * 
     * @param args the arguments sent to the program
     * @throws IOException thrown if there is a problem reading or writing to the
     *                     CSV file
     */
    private void parseCommands(String[] args) throws IOException {
        if (args.length == 0) {
            printHelp();
            return;
        }

        boolean gotAttendance = false;

        // Command precedence:
        // -get, -temp, -read/-r, -group

        List<String> commands = new ArrayList<>(Arrays.asList(args));

        if (commands.contains(GET_ARG)) {
            getAttendance();
            gotAttendance = true;
            writeAttendance();
        }

        if (commands.contains(TEMP_ARG)) {
            int index = commands.indexOf(TEMP_ARG);
            String nextArg = commands.get(++index);
            while (nextArg.charAt(0) != '-') {
                attendingStudents.add(nextArg);
                if (index + 1 != args.length) {
                    nextArg = commands.get(++index);
                } else {
                    break;
                }
            }
        }

        if (!gotAttendance && (commands.contains(READ_ARG))) {
            readAttendance();
            gotAttendance = true;
        }

        if (commands.contains(GROUP_ARG)) {
            if (!gotAttendance) {
                System.out.println("No attendance method specified, reading last");
                readAttendance();
                gotAttendance = true;
            }
            int groupAmount = Integer.parseInt(commands.get(commands.indexOf(GROUP_ARG) + 1));
            randomStudent(groupAmount);
        }

    }

    /**
     * 
     * Prints out help messages for the user.
     */
    private void printHelp() {
        System.out.println("Här borde det finnas lite vettig info");
    }

    /**
     * 
     * Main method for the program. Creates an instance of Scheduler and calls
     * parseCommands with the arguments sent to the program.
     * 
     * @param args the arguments sent to the program
     */
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