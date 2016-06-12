package Separator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class NewFormatWriter {
	public static void newFormatWrite(ArrayList<Sentence> separatorResult) throws IOException {
		int total = 0;
		for (int i=0; i<separatorResult.size(); i++) {
			FileWriter fw;
			if (total%10 == 0) {
				fw = new FileWriter(Constant.RESULT_DIR + "separater_result" + "-" + (total/10+1) + ".xml");
				fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
				fw.write("<document id=\"separater_result" + "-" + (total/10+1) + "\" origId=\"separater_result" + "-" + (total/10+1) + ".xml\">\n");
				fw.close();
			}

			fw = new FileWriter(Constant.RESULT_DIR + "separater_result" + "-" + (total/10+1) + ".xml", true);
			String result = "";
			Sentence s = separatorResult.get(i);
			result += s.toXmlString();
			fw.write(result);
			fw.close();
			
			if (total%10 == 9 || i == separatorResult.size()-1) {
				fw = new FileWriter(Constant.RESULT_DIR + "separater_result" + "-" + (total/10+1) + ".xml", true);
				fw.write ("</document>\n");
				fw.close();
			}
			total++;
		}
	}
}
