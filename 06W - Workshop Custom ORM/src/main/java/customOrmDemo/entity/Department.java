package customOrmDemo.entity;

import miniOrm.anotation.Column;
import miniOrm.anotation.Entity;
import miniOrm.anotation.Id;

@Entity(tableName = "departments")
public class Department {

    @Id
    private long id;

    @Column(name = "name", columnDefinition = "VARCHAR(211)")
    private String name; // "get" + "String"

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
