package Combinator;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Constant.initialize();
		ArrayList<Sentence> combinatorResult = new ArrayList<Sentence>();
		int compound_1 = 0;
		int relative_1 = 0;
		int relative_2 = 0;
		int relative_3 = 0;
		int relative_4 = 0;
		
		ArrayList<Sentence> targetSentence = NewFormatReader.readNewFormatXmlInFolder(Constant.ROOT_DIR);

		for (int i=0; i<targetSentence.size(); i++) {
			System.out.println(i + "/" + targetSentence.size());
			
			Sentence s1 = targetSentence.get(i);

			if (!Combinator.isTargetSentence(s1)) continue;

			for (int j=i+1; j<targetSentence.size(); j++) {
				Sentence s2 = targetSentence.get(j);

				if (!Combinator.isTargetSentence(s2)) continue;

				Sentence sc1 = Compound1Combinator.combinate (s1, s2);
				if (sc1 != null) {
					combinatorResult.add(sc1);
					compound_1++;
				}
				else {
					sc1 = Compound1Combinator.combinate (s2, s1);
					if (sc1 != null) {
						combinatorResult.add(sc1);
						compound_1++;
					}
				}

				Sentence sr1 = Relative1Combinator.combinate (s1, s2);
				if (sr1 != null) {
					combinatorResult.add(sr1);
					relative_1++;
				}
				else {
					sr1 = Relative1Combinator.combinate (s2, s1);
					if (sr1 != null) {
						combinatorResult.add(sr1);
						relative_1++;
					}
				}


				Sentence sr2 = Relative2Combinator.combinate (s1, s2);
				if (sr2 != null) {
					combinatorResult.add(sr2);
					relative_2++;
				}
				else {
					sr2 = Relative2Combinator.combinate (s2, s1);
					if (sr2 != null) {
						combinatorResult.add(sr2);
						relative_2++;
					}
				}

				Sentence sr3 = Relative3Combinator.combinate (s1, s2);
				if (sr3 != null) {
					combinatorResult.add(sr3);
					relative_3++;
				}
				else {
					sr3 = Relative3Combinator.combinate (s2, s1);
					if (sr3 != null) {
						combinatorResult.add(sr3);
						relative_3++;
					}
				}


				Sentence sr4 = Relative4Combinator.combinate (s1, s2);
				if (sr4 != null) {
					combinatorResult.add(sr4);
					relative_4++;
				}
				else {
					sr4= Relative4Combinator.combinate (s2, s1);
					if (sr4 != null) {
						combinatorResult.add(sr4);
						relative_4++;
					}
				}
			}
		}

		System.out.println("# of compound_1 sentences : " + compound_1);
		System.out.println("# of relative_1 sentences : " + relative_1);
		System.out.println("# of relative_2 sentences : " + relative_2);
		System.out.println("# of relative_3 sentences : " + relative_3);
		System.out.println("# of relative_4 sentences : " + relative_4);
		System.out.println("# of total result sentences : " + combinatorResult.size());

		NewFormatWriter.newFormatWrite(combinatorResult);
	}	
}
