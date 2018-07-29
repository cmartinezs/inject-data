package cl.smartware.machali;

import java.text.ParseException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cl.smartware.machali.utils.DateUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InjectDataApplicationTests {

	@Test
	public void contextLoads() {
		try {
			System.out.println(DateUtils.toDate("04/07/2018 21:12"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
