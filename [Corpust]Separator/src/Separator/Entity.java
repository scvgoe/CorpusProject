package Separator;

public class Entity {
	public String audit;
	public int charOffsetStart;
	public int charOffsetEnd;
	public String from;
	public String id;
	public String name;
	public String nn;
	public String rule;
	public int seqid;
	public String term_hood_score;
	public String term_hood;
	public String type;
	public String subtype;

	public Entity (int charOffsetStart, int charOffsetEnd, 
			String id, String name, int seqid, String type, String subtype) {
		// TODO Auto-generated constructor stub
		this.audit = "";
		this.charOffsetStart = charOffsetStart;
		this.charOffsetEnd = charOffsetEnd;
		this.from = "manual";
		this.id = id;
		this.name = name;
		this.nn = name;
		this.rule = "";
		this.seqid = seqid;
		this.term_hood_score = "";
		this.term_hood = "";
		this.type = type;
		this.subtype = subtype;
	}
	
	public Entity copy () {
		return new Entity (charOffsetStart, charOffsetEnd, id, name, seqid, type, subtype);
	}
	
	public String toXmlString() {
		String result = "";
		
		result += String.format(Constant.INDENTATION3 + "<entity audit=\"%s\" charOffset=\"%d-%d\" from=\"%s\" id=\"%s\" "
				+ "name=\"%s\" nn=\"%s\" rule=\"%s\" seqid=\"%d\" term_hood_score=\"%s\" termhood=\"%s\" type=\"%s\"/>\n",
				this.audit, this.charOffsetStart, this.charOffsetEnd, this.from, this.id, this.name, this.nn, this.rule,
				this.seqid, this.term_hood_score, this.term_hood, 
				(this.subtype.equals("")) ? (this.type) : (this.type + "/" + this.subtype));
		
		return result;
	}
}
