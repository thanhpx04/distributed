package net.persistance.db;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.User;
import service.UserDaoImpl;

import static org.junit.Assert.*;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.dataset.xml.FlatDtdDataSet;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.dbunit.operation.DatabaseOperation;

public class UserTest {

	private static final Logger log = LoggerFactory.getLogger(UserTest.class);

	private static UserDaoImpl userDAO;
	private static EntityManager em;
	private static EntityTransaction tx;

	@BeforeClass
	public static void initEntityManager() throws Exception {
		userDAO = new UserDaoImpl();
		em = userDAO.createEntityManager();
	}

	@Before
	public void initTransaction() throws Exception {
		tx = em.getTransaction();
		seedData();
	}

	protected void seedData() throws Exception {
		tx.begin();
		Connection connection = em.unwrap(java.sql.Connection.class);
		try {
			IDatabaseConnection dbUnitCon = new DatabaseConnection(connection);
			dbUnitCon.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new H2DataTypeFactory());
			IDataSet dataset;
			FlatXmlDataSetBuilder flatXmlDataSetBuilder = new FlatXmlDataSetBuilder();
			flatXmlDataSetBuilder.setColumnSensing(true);
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("data/dataset.xml");
			if (in != null) {
				log.warn("DataSet found");
				dataset = flatXmlDataSetBuilder.build(in);
			} else {
				log.warn("DataSet not found");
				dataset = new DefaultDataSet();
			}
			DatabaseOperation.REFRESH.execute(dbUnitCon, dataset);
		} finally {
			tx.commit();
		}
	}

	@Test
	public final void testFindAll() {
		log.info("testFindAll");
		try {
			tx.begin();
			@SuppressWarnings("unchecked")
			List<User> list = em.createQuery("select u from User u").getResultList();
			log.debug("find by example successful, result size: " + list.size());
			assertEquals("did not get expected number of entities ", 5, list.size());
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		} finally {
			tx.commit();
		}
	}

	@Rule
	public TestName name = new TestName();

	@After
	public void afterTests() throws Exception {
		@SuppressWarnings({ "unused", "rawtypes" })
		Class driverClass = Class.forName("org.h2.Driver");
		Connection jdbcConnection = DriverManager.getConnection("jdbc:h2:mem://localhost:9101/dbunit", "sa", "");
		IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);
		connection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new H2DataTypeFactory());
		// full database export
		IDataSet fullDataSet = connection.createDataSet();
		FlatXmlDataSet.write(fullDataSet, new FileOutputStream("target/" + name.getMethodName() + ".xml"));
		FlatDtdDataSet.write(connection.createDataSet(), new FileOutputStream("target/" + "test.dtd"));
	}

	@AfterClass
	public static void closeEntityManager() throws Exception {

	}

}
