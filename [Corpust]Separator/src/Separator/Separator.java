package Separator;

import java.util.ArrayList;
import java.util.Iterator;

import edu.stanford.nlp.trees.Tree;

public class Separator {
	public static boolean isAntecedentAfterVerb (Tree t) {
		// input tree t가 가리키는 관계사절에서 선행사가 삽입되어야 하는 위치가 동사 뒤인지를 판단한다.
		// 관계사와 관계사절의 동사 사이에 SBAR 태그 바로 아래에 있는 S 태그 바로 아래에 있는 NP 태그가 존재하는지의 유무를 판단한다.
		boolean result = false;
		Iterator<Tree> itr = t.iterator();
		boolean after_rel = false;
		while (itr.hasNext()) {
			Tree tmp = itr.next();

			if (Constant.RELATIVE_PRONOUN.contains(tmp.nodeString())) {
				after_rel = true;
				continue;
			}
			if (after_rel) {
				if (tmp.nodeString().split(" ")[0].equals("VP") && tmp.parent(t).nodeString().split(" ")[0].equals("S")) {
					if (tmp.parent(t).parent(t).nodeString().split(" ")[0].equals("SBAR")) {
						break;
					}
				}
				if (tmp.nodeString().split(" ")[0].equals("NP") && tmp.parent(t).nodeString().split(" ")[0].equals("S")) {
					if (tmp.parent(t).parent(t).nodeString().split(" ")[0].equals("SBAR")) {
						result = true;
						break;
					}
				}
			}
		}

		return result;
	}

	public static void insertAntecedentAfterVerb (Tree t, String sub) {
		// 관계사절의 동사의 위치를 찾고 그 뒤에 관계사의 선행사를 동사 뒤에 삽입한다.
		// 마찬가지로 SBAR 태그 바로 아래 S 태그 아래 첫번째 VP 태그 안에 가장 마지막으로 등장하는 VP 태그의 첫번째 leaf를 동사의 위치로 보았다.
		Iterator<Tree> itr = t.iterator();
		Tree first_VP = null;
		while (itr.hasNext()) {
			Tree tmp = itr.next();

			if (Constant.RELATIVE_PRONOUN.contains(tmp.nodeString())) {
				// 선행사를 제거한다.
				tmp.setValue("");
				continue;
			}

			if (tmp.nodeString().split(" ")[0].equals("VP") && tmp.parent(t).nodeString().split(" ")[0].equals("S")) {
				first_VP = tmp;
				break;
			}
		}

		Iterator<Tree> itr2 = first_VP.iterator();
		Tree last_VP = null;
		while (itr2.hasNext()) {
			Tree tmp = itr2.next();
			if (tmp.nodeString().split(" ")[0].equals("VP")) {
				last_VP = tmp;
			}
		}

		String origin = last_VP.getLeaves().get(0).nodeString();
		last_VP.getLeaves().get(0).setValue(origin + " " + sub);
	}

	public static int findDifferenceStartIndex (String s1, String s2) {
		// s1 (원래 문장)에서 s2 (종속절을 제한 주절) 부분을 제외한, 즉 s1에서 제거된 부분의 시작 index를 찾아 return 한다.
		int result = 0;

		int len = s1.length() - s2.length();
		if (len<0) return -1;

		while(true) {
			if (result == 0) {
				String tmp = s1.substring(len, s1.length()); 
				if (tmp.equals(s2)) {
					break;
				}
			}

			else if (result + len <= s1.length()) {
				String tmp = s1.substring(0, result) + s1.substring(result + len, s1.length());
				if (tmp.equals(s2)) {
					break;
				}
			}

			else {
				result = -1;
				break;
			}

			result++;
		}

		return result;
	}

	public static boolean entityIsInHere (String eid, Sentence s) {
		// entity의 id eid가 sentence s에 포함되어 있는지 확인 
		for (int i=0; i<s.entityList.size(); i++) {
			Entity e = s.entityList.get(i);
			if (e.id.equals(eid)) {
				return true;
			}
		}

		return false;
	}

	public static void resetEntityCharOffset (Sentence s, Sentence s1, Sentence s2) {
		int fd = findDifferenceStartIndex(s.text, s2.text);
		if (fd == -1) return; //분리된 주절과 원래 주절 사이의 관계가 확실하지 않을 때
		int len = s.text.length() - s2.text.length();

		// 원래 문장의 entity들의 charoffset을 새로 분리된 문장 내의 offset으로 지정하는 과정
		for (int i=0; i<s.entityList.size(); i++) {
			Entity e = s.entityList.get(i);
			if (e.charOffsetStart < fd + 1 && e.charOffsetEnd < fd + 1) {
				//Case 1
				e.seqid = s2.entityList.size();
				s2.entityList.add(e);
			}
			else if (e.charOffsetStart > fd + 1 && e.charOffsetStart < fd + len &&
					e.charOffsetEnd > fd + 1 && e.charOffsetEnd < fd + len) {
				//Case 2
				e.charOffsetStart = s1.text.indexOf(e.name);
				e.charOffsetEnd = e.charOffsetStart + e.name.length();
				e.seqid = s1.entityList.size();
				s1.entityList.add(e);
			}
			else if (e.charOffsetStart > fd + len && e.charOffsetEnd > fd + len) {
				//Case 3
				e.charOffsetStart = e.charOffsetStart - len;
				e.charOffsetEnd = e.charOffsetEnd - len;
				e.seqid = s2.entityList.size();
				s2.entityList.add(e);
			}
			else continue;
		}
	}

	public static void separatePairs (Sentence s, Sentence s1, Sentence s2) {
		// 원래 문장의 pair 들을 새로 분리된 문장 내로 지정하는 과
		for (int i=0; i<s.pairList.size(); i++) {
			Pair p = s.pairList.get(i);
			// p.e1, p.e2 check where it is.
			if (entityIsInHere(p.e1, s1) && entityIsInHere(p.e2, s1)) {
				s1.pairList.add(p);
			}
			else if (entityIsInHere(p.e1, s2) && entityIsInHere(p.e2, s2)) {
				s2.pairList.add(p);
			}
		}
	}

	public static ArrayList<Sentence> separateComplex (Sentence s) {
		//복문을 분리해주는 method

		// 분리 대상인 문장 s가 복문이 아니면 분리를 진행하지 않는다.
		if (!s.type.equals("complex")) {
			return null;
		}

		ArrayList<Sentence> result = new ArrayList<>(); //분리된 문장을 저장할 ArrayList
		Sentence s1 = new Sentence(s.id + "-1", null, null, null); //분리된 종속절 
		Sentence s2 = new Sentence(s.id + "-2", null, null, null); //분리된 주절 

		Tree t = Parser.String2Tree(s.text);
		Iterator<Tree> itr = t.iterator();
		while (itr.hasNext()) {
			Tree tmp = itr.next();
			if (!tmp.isLeaf()) {
				if (tmp.nodeString().split(" ")[0].equals("SBAR")) {
					// 종속절이 관계사절인지 아닌지 확인 
					if (!Constant.WH_PHRASE.contains(tmp.getChild(0).nodeString().split(" ")[0]) ||
							!Constant.WH_WORD.contains(tmp.getChild(0).getChild(0).nodeString().split(" ")[0])) {
						return null;
					}

					// Tree에서 태그를 탐색하며 SBAR 태그를 발견했을 때
					Tree parent = tmp.parent(t);					
					int index = Parser.indexOf(tmp, parent);
					if (index == 0) break;

					Tree prev = parent.getChild(index-1); //SBAR의 부모에서 SBAR node의 형제 node

					// 관계사의 위치가 문장과 문장 사이일 때 1
					if (prev.nodeString().split(" ")[0].equals("NP")) {
						// 선행사를 찾아 관계사의 값과 치환 
						String antecedent = Parser.Tree2String(prev);

						Iterator<Tree> itr2 = itr;
						while (itr2.hasNext()) {
							Tree tmp3 = itr2.next();
							if (Constant.RELATIVE_PRONOUN.contains(tmp3.nodeString())) {
								// 관계 대명사인 경우 
								if (isAntecedentAfterVerb(tmp)) {
									insertAntecedentAfterVerb(tmp, antecedent);
								}
								else {
									tmp3.setValue(antecedent);
									break;	
								}
							}
							else if (Constant.RELATIVE_ADVERB.contains(tmp3.nodeString())) {
								// 관계 부사인 경우 
								tmp3.setValue("in " + antecedent);
								break;
							}
						}

						s1.text = Parser.Tree2String(tmp); 

						// 주절에서 종속절 제거
						for (int i=0; i<parent.children().length; i++) {
							Tree tmp2 = parent.getChild(i);
							if (tmp2 == tmp) {
								parent.removeChild(i);
							}
						}

						s2.text = Parser.Tree2String(t); 

						break;
					}

					// 관계사의 위치가 주절의 가운데 일 때 (종속절이 , 사이에 끼어 있을 때)
					else if (prev.nodeString().split(" ")[0].equals(",")) {
						//prev node가 , (comma) 인 경우 다음 comma가 등장할 떄까지를 종속절로 판단
						if (index==1) break;
						Tree prev_prev = parent.getChild(index-2);

						// 선행사를 찾아 관계사에 치환 
						if (prev_prev.nodeString().split(" ")[0].equals("NP")) {							
							String sub = Parser.Tree2String(prev_prev);
							Iterator<Tree> itr2 = itr;
							while (itr2.hasNext()) {
								Tree tmp3 = itr2.next();
								if (Constant.RELATIVE_PRONOUN.contains(tmp3.nodeString())) {
									// 관계 대명사인 경우 
									if (isAntecedentAfterVerb(tmp)) {
										insertAntecedentAfterVerb(tmp, sub);
									}
									else {
										tmp3.setValue(sub);
										break;	
									}
								}
								else if (Constant.RELATIVE_ADVERB.contains(tmp3.nodeString())) {
									// 관계 부사인 경우
									tmp3.setValue("in " + sub);
									break;
								}
							}
						}

						else break;

						parent.removeChild(index-1); // SBAR 태그 이전 , 제거 

						// SBAR node 이전에 ,가 나왔으므로 다음 , 가 나올 때까지를 종속절로 봄 
						// Tree2String method와 동일하나 ,를 만날 때까지만 종속절의 범위로 판단 
						String subordinating = "";
						Iterator<Tree> itr3 = tmp.getLeaves().iterator();
						while (itr3.hasNext()) {
							Tree tmp4 = itr3.next();
							String nodestr = tmp4.nodeString();

							if (nodestr.equals(".")) continue;

							if (nodestr.equals(",")) {
								Tree comma_parent = tmp4.parent(t); // 두번째 ,의 부모 node
								Tree comma_parent_parent = comma_parent.parent(t); // 두번째 ,의 조부모 node

								// 주절에서 두번째 , 제거 
								for (int i=0; i<comma_parent_parent.children().length; i++) {
									Tree tmp5 = comma_parent_parent.getChild(i);
									if (tmp5 == comma_parent) {
										comma_parent_parent.removeChild(i);
									}
								}

								break;
							}
							else if (!nodestr.startsWith("\'")) nodestr = " " + nodestr;

							subordinating += nodestr;
						}

						subordinating.trim();  

						s1.text = subordinating;  // 종속절 추가 

						String main = Parser.Tree2String(t); // 주절 String
						main = main.replace(subordinating, ""); // 주절에서 종속절 제거 
						main = main.replace(" , ", " "); // SBAR에 포함되지 않은 두번째 comma 제거 
						s2.text = main; // 주절 추가 

						break;
					}
				}
			}
		}

		if (s1.text == null && s2.text == null) return null;

		s1.text = Sentence.sentenceTrim(s1.text);
		s2.text = Sentence.sentenceTrim(s2.text);

		resetEntityCharOffset (s, s1, s2); 
		separatePairs(s, s1, s2);

		result.add(s1);
		result.add(s2);

		return result;
	}

}
