package Combinator;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.Tree;

public class Parser {
	private final static String PCG_MODEL = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";        
	private final static LexicalizedParser parser = LexicalizedParser.loadModel(PCG_MODEL);
	
	public static Tree String2Tree (String s) {
		// String을 Tree로 parsing 해주는 method
		TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
		Tokenizer<CoreLabel> tok = tokenizerFactory.getTokenizer(new StringReader(s));
		List<CoreLabel> rawWords2 = tok.tokenize();
		Tree t = parser.apply(rawWords2);

		return t;
	}

	public static boolean isSubject (Tree t, String entity) {
		boolean result = false;

		Iterator<Tree> itr = t.iterator();
		while(itr.hasNext()) {
			Tree node = itr.next();
			if (node.nodeString().split(" ")[0].equals("NP")) {
				if (node.parent(t).nodeString().split(" ")[0].equals("S")) {
					if (node.parent(t).parent(t).nodeString().split(" ")[0].equals("ROOT")) {
						List<Tree> leaves = node.getLeaves();

						String cdt = "";
						for (int i=0; i<leaves.size(); i++) {
							cdt += leaves.get(i) + " ";
						}
						if (cdt.toLowerCase().contains(entity.toLowerCase())) {
							if (cdt.toLowerCase().indexOf(entity.toLowerCase()) + entity.length() + 1 == cdt.length()) {
								result = true;
								break;
							}
						}
					}
				}
			}
		}

		return result;
	}

	public static boolean isObject (Tree t, String entity) {
		boolean result = false;

		Iterator<Tree> itr = t.iterator();
		while(itr.hasNext()) {
			Tree node = itr.next();
			if (node.nodeString().split(" ")[0].equals("NP")) {
				if (node.parent(t).nodeString().split(" ")[0].equals("VP")) {
					if (node.parent(t).parent(t).nodeString().split(" ")[0].equals("S")) {
						if (node.parent(t).parent(t).parent(t).nodeString().split(" ")[0].equals("ROOT")) {
							List<Tree> leaves = node.getLeaves();

							String cdt = "";
							for (int i=0; i<leaves.size(); i++) {
								cdt += leaves.get(i) + " ";
							}
							if (cdt.toLowerCase().contains(entity.toLowerCase())) {
								if (cdt.toLowerCase().indexOf(entity.toLowerCase()) + entity.length() + 1 == cdt.length()) {
									result = true;
									break;
								}
							}
						}
					}
				}
			}
		}

		return result;
	}
	
	public static Tree isLocation (Tree t, String e) {
		Tree result = null;

		Iterator<Tree> itr = t.iterator();
		while (itr.hasNext()) {
			Tree n = itr.next();
			if (n.nodeString().split(" ")[n.nodeString().split(" ").length-1].toLowerCase().equals(e.toLowerCase())) {
				if(n.parent(t).parent(t).nodeString().split(" ")[0].equals("NP")){
					if(n.parent(t).parent(t).parent(t).nodeString().split(" ")[0].equals("PP")){
						if(n.parent(t).parent(t).parent(t).getChild(0).getLeaves().get(0).nodeString().split(" ")[0].equals("in") ||
								n.parent(t).parent(t).parent(t).getChild(0).getLeaves().get(0).nodeString().split(" ")[0].equals("at")){
							if(n.parent(t).parent(t).parent(t).parent(t).nodeString().split(" ")[0].equals("VP")){
								result = n.parent(t).parent(t).parent(t);
								break;
							}
						}
					}

				}
			}
		}

		return result;
	}
}
