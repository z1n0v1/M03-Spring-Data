public class Minion {
    private final String name;
    private final int age;

    public Minion(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return name + " " + age;
    }
}
