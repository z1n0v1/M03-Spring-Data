package entity.T03UniversitySystem;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "students")
public class Student extends Person {
    private Double averageGrade;
    private Set<Course> courses;

    public Student() {
        courses = new HashSet<>();
    }

    @Column(name = "average_grade")
    public Double getAverageGrade() {
        return averageGrade;
    }

    public void setAverageGrade(Double averageGrade) {
        this.averageGrade = averageGrade;
    }

    @ManyToMany(mappedBy = "students")
    public Set<Course> getAttendance() {
        return courses;
    }

    public void setAttendance(Set<Course> courses) {
        this.courses = courses;
    }
}
