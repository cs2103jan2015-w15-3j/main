import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.joestelmach.natty.*;

public class DateParser {

	List<DateGroup> dateGroupList;
	Parser parser;
	String input;
	
	public DateParser(String input) {
		// Creates a parser object according to host timezone
		parser = new Parser(TimeZone.getDefault());
		
	}
	
	public Date parseDate() {
		dateGroupList = parser.parse(input);
		return dateGroupList.get(0).getDates().get(0);
	}

}
