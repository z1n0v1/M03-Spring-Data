package entities;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "users", schema = "users_test", catalog = "")
public class UsersEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "username", nullable = false, length = 40)
    private String username;
    @Basic
    @Column(name = "password", nullable = false, length = 40)
    private String password;
    @Basic
    @Column(name = "age", nullable = false)
    private int age;
    @Basic
    @Column(name = "registration", nullable = false)
    private Date registration;

    public UsersEntity(String username, String password, int age, Date registration) {
        this.username = username;
        this.password = password;
        this.age = age;
        this.registration = registration;
    }

    public UsersEntity() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getRegistration() {
        return registration;
    }

    public void setRegistration(Date registration) {
        this.registration = registration;
    }
}
