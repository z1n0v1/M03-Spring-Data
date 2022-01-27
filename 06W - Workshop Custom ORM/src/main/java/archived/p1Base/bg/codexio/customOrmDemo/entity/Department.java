package archived.p1Base.bg.codexio.customOrmDemo.entity;

import archived.p1Base.ormFramework.annotation.Column;
import archived.p1Base.ormFramework.annotation.Entity;
import archived.p1Base.ormFramework.annotation.Id;

@Entity(tableName = "departments")
public class Department {

    @Id
    private int id;

    @Column(name = "name", columnDefinition = "VARCHAR(211)")
    private String name; // "get" + "String"

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
