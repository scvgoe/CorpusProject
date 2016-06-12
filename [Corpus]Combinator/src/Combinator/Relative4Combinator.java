package Combinator;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.trees.Tree;

public class Relative4Combinator extends Combinator{
	public static Sentence combinate (Sentence s1, Sentence s2) {
		// 복문 생성 함수
		// 관계 부사 where 결합
		// S1 where S2
		// 주절과 종속절이 순차적으로 연결된 경우

		// 개체가 장소를 나타내는 부사구여야 
		s1 = s1.copy();
		s2 = s2.copy();

		ArrayList<Entity> commonEntity1 = new ArrayList<Entity>();
		ArrayList<Entity> commonEntity2 = new ArrayList<Entity>();

		for (int i=0; i<s1.entityList.size(); i++) {
			for (int j=0; j<s2.entityList.size(); j++) {
				if (s1.entityList.get(i).name.toLowerCase().equals(s2.entityList.get(j).name.toLowerCase())) {
					commonEntity1.add(s1.entityList.get(i));
					commonEntity2.add(s2.entityList.get(j));
				}
			}
		}

		if (commonEntity1.size() == 0) return null;
		if (commonEntity2.size() == 0) return null;

		Tree t = Parser.String2Tree(s2.text);
		Entity targetEntity1 = null;
		String ppString = "";
		for (int i=0; i<commonEntity1.size(); i++) {
			Tree pp = Parser.isLocation(t, commonEntity1.get(i).name); 
			if (pp != null) {
				targetEntity1 = commonEntity1.get(i);
				List<Tree> ppLeaves = pp.getLeaves();
				for (int j=0; j<ppLeaves.size(); j++) {
					String[] tmpArray = ppLeaves.get(j).nodeString().split(" ");
					ppString += tmpArray[tmpArray.length-1] + " ";
				}
				break;
			}
		}

		if (ppString.equals("")) return null;
		if (targetEntity1.charOffsetEnd + 2 != s1.text.length()) return  null;
		
		ppString = ppString.substring(0, ppString.length()-1);
		String exceptPPString = s2.text.replace(ppString, "");
		String insertedString = exceptPPString.substring(0, exceptPPString.length()-1);
		String text = s1.text.substring(0, s1.text.length()-1) +
				" where " + insertedString + ".";

		while (text.contains("  ") || text.contains(",,") || text.contains(" ,") || text.contains(" .")) {
			text = text.replace("  ", " ");
			text = text.replace(",,", ",");
			text = text.replace(" ,", ",");
			text = text.replace(" .", ".");
		}
		text = text.replaceAll("\n", "");

		//entity and pair relocate
		Sentence result = new Sentence (s1.id + "/" + s2.id, text, null, null);

		entityPairClear(s1, s2, result);

		result.text = result.text.substring(0, 1).toUpperCase() + result.text.substring(1);

		return result;
	}
}
