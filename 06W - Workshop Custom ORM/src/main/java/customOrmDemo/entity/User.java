package customOrmDemo.entity;

import miniOrm.anotation.Column;
import miniOrm.anotation.Entity;
import miniOrm.anotation.Id;

import java.time.LocalDate;

@Entity(tableName = "users")
public class User {

    @Id
    private long id;
    @Column(name = "username", columnDefinition = "VARCHAR(100)")
    private String username;
    @Column(name = "age", columnDefinition = "INT")
    private int age;
    @Column(name = "registration", columnDefinition = "DATE")
    private LocalDate registration;
//    @Column(name = "address", columnDefinition = "VARCHAR(255)")
//    private String address;


    public User() {
    }

    public User(String username, int age) {
        this.username = username;
        this.age = age;
        this.registration = LocalDate.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDate getRegistration() {
        return registration;
    }

    public void setRegistration(LocalDate registration) {
        this.registration = registration;
    }

//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
}
