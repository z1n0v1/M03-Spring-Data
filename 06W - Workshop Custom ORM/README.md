## miniORM

 **miniOrm** is a simple Object-relational mapping framework 
 providing basic CRUD operations for the following data types :

  - **boolean** and **Boolean**
  - **int** and **Integer**
  - **long** and **Long**
  - **BigDecimal**
  - **LocalDate**
  - **Date**
  - **String**
  
You can add more data types by adding them to [*setEntityColumns*](https://github.com/z1n0v1/M03-Spring-Data/blob/b05351906036692860489aa628a6cd9aee004e77/06W%20-%20Workshop%20Custom%20ORM/src/main/java/miniOrm/core/EntityManagerImpl.java#L50) method in 
*EntityManagerImpl* class.

The supported operations are defined in [**EntityManager**](https://github.com/z1n0v1/M03-Spring-Data/blob/main/06W%20-%20Workshop%20Custom%20ORM/src/main/java/miniOrm/core/EntityManager.java) interface :
```java
 <T> T findById(long id, Class<T> tClass)
 <T> T finddst(Class<T> tClass)
 <T> T finFirst(Class<T> tClass, String where)
 <T> Iterable<T> find(Class<T> tClass)
 <T> Iterable<T> find(Class<T> tClass, String where)
 <T> boolean persist(T entity)
 <T> boolean delete(T entity)
```
Initialization of the framework happens using
*EntityManagerFactory*'s [**create**](https://github.com/z1n0v1/M03-Spring-Data/blob/2d556b28780181e706a56a50a544cf55d5eb9803/06W%20-%20Workshop%20Custom%20ORM/src/main/java/miniOrm/core/EntityManagerFactory.java#L24) method:
```java
public static EntityManager *create*(
  String dbType,
  String host,
  int port,
  String user,
  String pass,
  String dbName,
  Class<?> mainClass
)
```
Here is an example of the way one should anotate their classes for them to be processed by the framework:
```java
@Entity(tableName = simple_classes)
class SimpleClass {

  @Id
    private long id;
  
  @Column(name = "street", columnDefinition = "VARCHAR(255)")
    private String street;
  
  @Column(name = "date", columnDefinition = "DATE")
    private LocalDate date;

  @Column(name = "number", columnDefinition = "INT")
    private int number;
}
```



I've written my own ORM implementation as I was not satisfied by the skeletons provided as part of SoftUni's lecture and workshop you can find them at */src/main/java/archived/* subfolder.

