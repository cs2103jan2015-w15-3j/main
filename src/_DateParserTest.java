import static org.junit.Assert.*;

import org.junit.Test;


public class _DateParserTest {

	@Test
	public void test() {
		DateParser dateParser = new DateParser("today");
		System.out.println(dateParser.parseDate("today"));
	}

}
