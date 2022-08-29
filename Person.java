public class Person {
    public enum attendance {
        Y,
        N,
        M
    }

    private String name;
    attendance[] beenHere;

    public Person(String name) {
        this.name = name;
        beenHere = new attendance[52];
    }

    public String getName() {
        return name;
    }

    public attendance[] getAttendance() {
        return beenHere;
    }

    public void setAttendence(int day, Person.attendance hasAttended) {
        if (day >= 52)
            return;
        beenHere[day] = hasAttended;
    }

    public String getAttendance(int day) {
        if (day > beenHere.length || day < 0) {
            throw new IndexOutOfBoundsException("Day is not between 0 and length");
        }
        return beenHere[day].name();
    }
}
