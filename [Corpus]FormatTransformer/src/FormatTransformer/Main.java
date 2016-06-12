package FormatTransformer;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Constant.initialize();
		ArrayList<Sentence> rawSentenceList = CorpusReader.corpusXmlInFolderToSentenceList(Constant.ROOT_DIR);
		ArrayList<Sentence> redundantSentenceRemoved = new ArrayList<Sentence>();
		
		int id = 0;
		for (Sentence s1 : rawSentenceList) {
			boolean tmp = false;
			for (Sentence s2 : redundantSentenceRemoved) {
				if (s1.text.equals(s2.text)){
					tmp = true;
					s2.plus(s1);
					break;
				}
			}
			if (!tmp) {
				s1.id = Integer.toString(id++);
				redundantSentenceRemoved.add(s1);
			}
		}
		
		System.out.println("Transformation is finished!");
		System.out.println("Totla number of sentence is " + redundantSentenceRemoved.size());
		NewFormatWriter.newFormatWrite(redundantSentenceRemoved);
		System.out.println("Writing to file is finished!");
	}
}
