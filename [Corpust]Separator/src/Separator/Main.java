package Separator;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		Constant.initialize();

		ArrayList<Sentence> targetSentence = NewFormatReader.readNewFormatXmlInFolder(Constant.ROOT_DIR);
		ArrayList<Sentence> separatorResult = new ArrayList<Sentence>();

		for (int i=0; i<targetSentence.size(); i++) {
			System.out.println(i + "/" + targetSentence.size());
			Sentence s = targetSentence.get(i);
			ArrayList<Sentence> separatedSentences = Separator.separateComplex(s);
			if (separatedSentences == null || separatedSentences.size() == 0) continue;
			separatorResult.addAll(separatedSentences);
		}
		
		System.out.println("# of total result sentences: " + separatorResult.size());
		NewFormatWriter.newFormatWrite(separatorResult);
	}
}
