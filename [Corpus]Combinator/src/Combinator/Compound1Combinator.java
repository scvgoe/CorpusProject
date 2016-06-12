package Combinator;
import java.util.ArrayList;

public class Compound1Combinator extends Combinator{
	public static Sentence combinate (Sentence s1, Sentence s2) {
		// 중문 생성 함수 
		// and 결합
		// S1, and S2
		s1 = s1.copy();
		s2 = s2.copy();

		boolean isTarget = false;
		for (int i=0; i<s1.entityList.size(); i++) {
			for (int j=0; j<s2.entityList.size(); j++) {
				if (s1.entityList.get(i).name.equals(s2.entityList.get(j).name)) {
					isTarget = true;
					break;
				}
			}
			if (isTarget) break;
		}
		if (!isTarget) return null;
		
		String text = s1.text.substring(0, s1.text.length()-1) + ", and " + s2.text.substring(0, 1).toLowerCase() + s2.text.substring(1);
		text = text.substring(0, 1).toUpperCase() + text.substring(1);
		if (text.contains(".,")) return null;
		
		Sentence result = new Sentence (s1.id + "/" + s2.id, text, null, null);

		ArrayList<Entity> entityList = new ArrayList<Entity>();
		for (int i=0; i<s1.entityList.size(); i++) {
			entityList.add(s1.entityList.get(i).copy());
		}
		for (int i=0; i<s2.entityList.size(); i++) {
			Entity e = s2.entityList.get(i).copy();
			e.charOffsetStart += s1.text.length() + 5;
			e.charOffsetEnd += s1.text.length() + 5;
			entityList.add(e);
		}

		entityList = Entity.removeRedundantEntity(entityList);
		result.entityList = entityList;

		ArrayList<Pair> pairList = new ArrayList<Pair>();
		for (int j=0; j<s1.pairList.size(); j++) {
			pairList.add(s1.pairList.get(j).copy());
		}
		for (int j=0; j<s2.pairList.size(); j++) {
			pairList.add(s2.pairList.get(j).copy());
		}
		pairList = Pair.removeRedundantPair(pairList);
		result.pairList = pairList;

		return result;
	}
}
