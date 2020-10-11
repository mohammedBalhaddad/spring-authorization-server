package sample.entities;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

@DataJpaTest
@AutoConfigureDataJpa
public class ClientTest {

	@Autowired
	TestEntityManager entityManager ;

	@Test
	public void test (){
		Assertions.assertTrue(true);
	}
	@Test
	public void test2 (){
		Assertions.assertTrue(false);
	}

}
