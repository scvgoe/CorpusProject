package Combinator;
import java.util.ArrayList;
import edu.stanford.nlp.trees.Tree;

public class Relative1Combinator extends Combinator {
	public static Sentence combinate (Sentence s1, Sentence s2) {
		// 복문 생성 함수
		// 관계 대명사 that 결합 
		// S1 (A_subject, that S2(A_subject ...), verb ....)
		// 주절 가운데 종속절이 포함되어 있는 경우

		// A_subject가 S1에서 주어여야 함
		// A_subject가 S2에서는 목적어여도 상관없음
		s1 = s1.copy();
		s2 = s2.copy();

		ArrayList<Entity> commonEntity1 = new ArrayList<Entity>();
		ArrayList<Entity> commonEntity2 = new ArrayList<Entity>();

		for (int i=0; i<s1.entityList.size(); i++) {
			for (int j=0; j<s2.entityList.size(); j++) {
				if (s1.entityList.get(i).name.equals(s2.entityList.get(j).name)) {
					commonEntity1.add(s1.entityList.get(i));
					commonEntity2.add(s2.entityList.get(j));
				}
			}
		}

		if (commonEntity1.size() == 0) return null;
		if (commonEntity2.size() == 0) return null;
		
		Tree t1 = Parser.String2Tree(s1.text);
		Tree t2 = Parser.String2Tree(s2.text);
		for (int i=0; i<commonEntity1.size(); i++) {
			Entity e1 = commonEntity1.get(i).copy();
			Entity e2 = commonEntity2.get(i).copy();
			if (Parser.isSubject(t1, e1.name) && (Parser.isSubject(t2, e2.name) || Parser.isObject(t2, e2.name))) {
				String text = s1.text.substring(0, e1.charOffsetEnd+1) + ", that " +
						s2.text.substring(0, e2.charOffsetStart) +
						s2.text.substring(e2.charOffsetEnd+1, s2.text.length()-1) + ", " +
						s1.text.substring(e1.charOffsetEnd+1, s1.text.length());
				while (text.contains("  ") || text.contains(",,") || text.contains(" ,")) {
					text = text.replaceAll("  ", " ");
					text = text.replaceAll(",,", ",");
					text = text.replaceAll(" ,", ",");
				}
				text = text.replaceAll("\n", "");

				Sentence result = new Sentence (s1.id + "/" + s2.id, text, null, null);

				result = entityPairClear(s1, s2, result);

				result.text = result.text.substring(0, 1).toUpperCase() + result.text.substring(1);

				return result;
			}
		}
		return null;
	}
}
