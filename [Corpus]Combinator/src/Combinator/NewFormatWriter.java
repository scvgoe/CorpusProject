package Combinator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class NewFormatWriter {
	public static void newFormatWrite (ArrayList<Sentence> combinatorResult) throws IOException {
		int total = 0;
		for (int i=0; i<combinatorResult.size(); i++) {
			Sentence s = combinatorResult.get(i);
			FileWriter fw;
			if (total%10 == 0) {
				fw = new FileWriter(Constant.RESULT_DIR + "combinatorResult" + "-" + (total/10+1) + ".xml");
				fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
				fw.write("<document id=\"combinatorResult" + "-" + (total/10+1) + "\" origId=\"combinater_result" + "-" + (total/10+1) + ".xml\">\n");
				fw.close();
			}
			
			fw = new FileWriter(Constant.RESULT_DIR + "combinatorResult" + "-" + (total/10+1) + ".xml", true);
			String result = "";
			result += s.toXmlString();
			fw.write(result);
			fw.close();

			if (total%10 == 9 || i == combinatorResult.size()-1) {
				fw = new FileWriter(Constant.RESULT_DIR + "combinatorResult" + "-" + (total/10+1) + ".xml", true);
				fw.write ("</document>\n");
				fw.close();
			}
			
			total++;
		}
	}
}
