package Combinator;
import java.util.ArrayList;

public class Combinator {
	public static Sentence entityPairClear(Sentence s1, Sentence s2, Sentence result) {
		ArrayList<Entity> entityList = new ArrayList<Entity>();
		ArrayList<Entity> allEntityList = new ArrayList<Entity>();

		for (int j=0; j<s1.entityList.size(); j++) {
			Entity e = s1.entityList.get(j).copy();
			e.charOffsetStart = result.text.toLowerCase().indexOf(e.name.toLowerCase());
			e.charOffsetEnd = e.charOffsetStart + e.name.length()-1;
			if (e.charOffsetStart != -1) {
				allEntityList.add(e);
			}
		}
		for (int j=0; j<s2.entityList.size(); j++) {
			Entity e = s2.entityList.get(j).copy();
			e.charOffsetStart = result.text.toLowerCase().indexOf(e.name.toLowerCase());
			e.charOffsetEnd = e.charOffsetStart + e.name.length()-1;
			if (e.charOffsetStart != -1) {
				allEntityList.add(e);
			}
		}
		entityList = Entity.removeRedundantEntity(allEntityList);
		result.entityList = entityList;

		ArrayList<Pair> pairList = new ArrayList<Pair>();
		ArrayList<Pair> allPairList = new ArrayList<Pair>();
		for (int j=0; j<s1.pairList.size(); j++) {
			allPairList.add(s1.pairList.get(j).copy());
		}
		for (int j=0; j<s2.pairList.size(); j++) {
			allPairList.add(s2.pairList.get(j).copy());
		}

		for (int j=0; j<allPairList.size(); j++) {
			Pair p = allPairList.get(j);
			boolean tmp1 = false;
			boolean tmp2 = false;
			for (Entity e : allEntityList) {
				String name = null;
				if (p.e1.equals(e.id)) {
					name = e.name;
					for (Entity e1 : entityList) {
						if (e1.name.toLowerCase().equals(name.toLowerCase())) {
							p.e1 = e1.id;
							tmp1 = true;
						}
					}
				}
				else if (p.e2.equals(e.id)) {
					name = e.name;
					for (Entity e1 : entityList) {
						if (e1.name.toLowerCase().equals(name.toLowerCase())) {
							p.e2 = e1.id;
							tmp2 = true;
						}
					}
				}
			}
			if (tmp1 && tmp2) {
				pairList.add(p);
			}
		}

		pairList = Pair.removeRedundantPair(pairList);
		result.pairList = pairList;

		return result;
	}
	
	public static boolean isTargetSentence (Sentence s) {
		boolean isValidType = true;
		for (Pair p : s.pairList) {
			if (!Constant.validTypes.contains(p.interaction)) {
				isValidType = false;
				break;
			}
		}
		if (!isValidType) return false;
		if (!s.type.equals("simple")) return false;
		
		return true;
	}
}
