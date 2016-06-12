package Separator;
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
	
	public static String Tree2String (Tree t) {
		// Tree t를 받아 String의 형태로 출력해주는 method

		String s = "";
		Iterator<Tree> itr2 = t.getLeaves().iterator();
		while (itr2.hasNext()) {
			Tree tmp2 = itr2.next();
			String tmp3 = tmp2.nodeString();

			if (tmp3.equals(".")) {
				//문장 종결 부호로 사용되는 . 는 다른 함수에서 처리
				if (!itr2.hasNext()) continue;
			}

			if (!s.endsWith("``") && !tmp3.startsWith("''") && !tmp3.startsWith("\'") && !tmp3.equals(",") && !tmp3.startsWith(".")) {
				//콤마의 경우 띄어쓰기 없이 곧바로 결합  
				tmp3 = " " + tmp3;
			}

			s += tmp3;
		}

		while (s.startsWith(" ")) s = s.substring(1, s.length()); // 맨 처음 공백 제거 
		while (s.endsWith(" ")) s = s.substring(0, s.length()-1); // 맨 나중 공백 제
		while (s.contains("  ")) s = s.replace("  ", " "); // 더블 스페이스 제거 

		return s;
	}
	
	public static int indexOf (Tree child, Tree parent) {
		// return index of child in child's parent
	
		int index = 0; //parent node에서 SBAR node의 index

		//index 값을 찾는 과정 
		for (int i=0; i<parent.children().length; i++) {
			Tree tmp2 = parent.getChild(i);
			if (tmp2 == child) {
				index = i;
				break;
			}
		}
		
		return index;
	}
}
