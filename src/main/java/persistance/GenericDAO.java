package persistance;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public abstract class GenericDAO {

	   private static EntityManagerFactory factory = Persistence.createEntityManagerFactory("JPA2Tut");
	 
	    public EntityManager createEntityManager() {
	        return factory.createEntityManager();
	    }
	 
	    public static void closeEntityManager() {
	        factory.close();
	    }
}