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
  
You can add more data types by adding them to *setEntityColumns* method in 
*EntityManagerImpl* class.

The supported operations are defined in **EntityManager** interface :
```java
 <T> T findById(long id, Class<T> tClass)
 <T> T finddst(Class<T> tClass)
<T> T finFirstFir(Class<T> tClass, String where)
 <T> Iterable<T> find(Class<T> tClass)
 <T> Iterable<T> find(Class<T> tClass, String where)
 <T> boolean persist(T entity)
 <T> boolean delete(T entity)
```
Initialization of the framework happens using
*EntityManagerFactory*'s **create** method:
```java
public static EntityManager *create*(
  String *dbType*,
  String *host*,
  int *port*,
  String *user*,
  String *pass*,
  String *dbName*,
  Class<?> *mainClass*
)
```

I've written my own ORM implementation as I was not satisfied by the skeletons provided as part of SoftUni's lecture and workshop you can find them at */src/main/java/archived/* subfolder.

