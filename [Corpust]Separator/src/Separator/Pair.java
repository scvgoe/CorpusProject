package Separator;

public class Pair {
	public String audit;
	public String e1;
	public String e2;
	public String id;
	public String interaction;
	public String method;
	public String pattern;
	public String patternInteraction;
	public String probability;
	public String psv;
	
	public Pair(String e1, String e2, String id, String type, String subtype) {
		// TODO Auto-generated constructor stub
		this.audit = "";
		this.e1 = e1;
		this.e2 = e2;
		this.id = id;
		this.interaction = (subtype.equals("")) ? (type) : (type + "/" + subtype);
		this.method = "";
		this.pattern = "";
		this.patternInteraction = "";
		this.probability = "";
		this.psv = "";
	}
	
	public Pair copy() {
		String types[] = interaction.split("/");
		String type;
		String subtype;
		if (types.length > 1) {
			type = types[0];
			subtype = types[1];
		}
		else {
			type = interaction;
			subtype = "";
		}
		
		return new Pair (e1, e2, id, type, subtype);
	}

	public String toXmlString() {
		String result = "";
		
		result += String.format(Constant.INDENTATION3 + "<pair audit=\"%s\" e1=\"%s\" e2=\"%s\" id=\"%s\" "
				+ "interaction=\"%s\" method=\"%s\" pattern=\"%s\" pattern_interaction=\"%s\" probability=\"%s\" "
				+ "psv=\"%s\"/>\n", this.audit, this.e1, this.e2, this.id, this.interaction, this.method,
				this.pattern, this.patternInteraction, this.probability, this.psv);
		
		return result;
	}
}
