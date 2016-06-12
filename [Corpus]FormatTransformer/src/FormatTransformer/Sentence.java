package FormatTransformer;

import java.util.ArrayList;

public class Sentence {
	public String id;
	public String text;
	public ArrayList<Entity> entityList;
	public ArrayList<Pair> pairList;

	public Sentence(String id, String text) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.text = text;

		entityList = new ArrayList<>();
		pairList = new ArrayList<>();
	}
	
	public Sentence copy () {
		Sentence result = new Sentence (id, text);
		
		ArrayList<Entity> el = new ArrayList<Entity>();
		ArrayList<Pair> pl = new ArrayList<Pair>();
		
		for (Entity e : entityList) {
			el.add(e.copy());
		}
		
		for (Pair p : pairList) {
			pl.add(p.copy());
		}
		
		result.entityList = el;
		result.pairList = pl;
		
		return result;
	}

	public String toXmlString() {
		String result = "";

		result += String.format(Constant.INDENTATION1 + "<sentence id=\"%s\"", this.id);
		result += ">\n";
		result += String.format(Constant.INDENTATION2 + "<text><![CDATA[\n%s%s\n%s]]></text>\n", Constant.INDENTATION3, this.text, Constant.INDENTATION2);
		if (this.entityList.size()>0) {
			result += String.format(Constant.INDENTATION2 + "<entityList>\n");
			for (int i=0; i<this.entityList.size(); i++) {
				result += this.entityList.get(i).toXmlString();
			}
			result += String.format(Constant.INDENTATION2 + "</entityList>\n");
		}
		if (this.pairList.size()>0) {
			result += String.format(Constant.INDENTATION2 + "<pairList>\n");
			for (int i=0; i<this.pairList.size(); i++) {
				result += this.pairList.get(i).toXmlString();
			}
			result += String.format(Constant.INDENTATION2 + "</pairList>\n");
		}
		result += String.format(Constant.INDENTATION2 + "<chunkList/>\n");
		result += String.format(Constant.INDENTATION1 + "</sentence>\n");

		return result;
	}

	public void plus (Sentence s2) {
		Sentence s1 = this;
		
		ArrayList<Entity> el = new ArrayList<Entity>();
		for (int i=0; i<s1.entityList.size(); i++) {
			el.add(s1.entityList.get(i));
		}
		for (int i=0; i<s2.entityList.size(); i++) {
			el.add(s2.entityList.get(i));
		}
		s1.entityList = Entity.removeRedundantEntity(el);
		
		ArrayList<Pair> pl = new ArrayList<Pair>();
		for (int i=0; i<s1.pairList.size(); i++) {
			pl.add(s1.pairList.get(i));
		}
		for (int i=0; i<s2.pairList.size(); i++) {
			pl.add(s2.pairList.get(i));
		}
		s1.pairList = Pair.removeRedundantPair(pl);
	}
}