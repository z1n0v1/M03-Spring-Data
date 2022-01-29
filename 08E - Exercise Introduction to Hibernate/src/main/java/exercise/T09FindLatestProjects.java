package exercise;

import entities.Project;

import javax.persistence.EntityManager;

public class T09FindLatestProjects implements Runnable {
    private final EntityManager entityManager;

    public T09FindLatestProjects(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void run() {
        entityManager.createQuery("SELECT p FROM Project p ORDER BY p.name", Project.class).setMaxResults(10)
                .getResultList().forEach(project -> {
                    System.out.printf(
                            "Project name: %s%n" +
                            "\t Project Description: %s%n" +
                            "\t Project Start Date: %s%n" +
                            "Project End Date: %s%n",
                            project.getName(), project.getDescription(), project.getStartDate(), project.getEndDate());
                });
    }
}
