package FormatTransformer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Locale;

public class SGMParser {
	public static String sgmParse (String sgmPath) {	
		String xml = "";

		try {
			BufferedReader br = new BufferedReader(new FileReader(sgmPath));
			while(true) {
				String line = br.readLine();
				if (line==null) break;

				boolean tmp = false;
				String line2 = "";
				for (int i=0; i<line.length(); i++) {
					String c = line.substring(i, i+1);

					if (c.equals(">")) {
						tmp = false;
					}
					else if (c.equals("<")) {
						tmp = true;
					}
					else {
						if (!tmp) {
							line2 += c;
						}
					}
				}

				xml += line2;
				xml += "\n";
			}
			br.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		xml = xml.replace("-\n\n", "..!");
		xml = xml.replace("\n", " ");

		return xml;
	}

	public static String getSentenceText (int indexStart, int indexEnd, String sgmPath) {
		ArrayList<String> parsedSGM = new ArrayList<String>();
		String sgm = sgmParse(sgmPath);
		String tmpSgm = sgm.toUpperCase();

		BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
		iterator.setText(tmpSgm);
		int start = iterator.first();
		for (int end = iterator.next();
				end != BreakIterator.DONE;
				start = end, end = iterator.next()) {
			parsedSGM.add(sgm.substring(start, end));
		}

		int totalIndex = 0;
		for (String s : parsedSGM) {
			totalIndex += s.length();
			if (indexStart < totalIndex) {
				if (indexEnd < totalIndex) {
					if (s.toLowerCase().contains("abc news") ||
						s.toLowerCase().contains("cnn") ||
						s.toLowerCase().contains("nbc") ||
						s.toLowerCase().contains("voa news") ||
						s.toLowerCase().contains("npr news")) return null;
					
					if (s.contains("''")) return null;
					if (s.contains("``")) return null;
					if (s.contains("\"")) return null;
					if (s.contains(":")) return null;
					if (s.contains("&")) return null;
					if (s.contains("..!")) return null;
					
					return s;
				}
				else {
					return null;
				}
			}
		}

		return null;
	}
}
