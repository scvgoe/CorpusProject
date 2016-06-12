package Separator;
import java.util.ArrayList;
import java.util.Arrays;


public class Constant {
	public static String ROOT_DIR = "/Users/daesungkim/Desktop/transformerResult_Module2_Done/"; 
	public static String RESULT_DIR = "/Users/daesungkim/Desktop/separatorResult/";
	
	public static String INDENTATION1 = "    "; //4 spaces
	public static String INDENTATION2 = "        "; //8 spaces
	public static String INDENTATION3 = "            "; //12 spaces

	// 관계대명사 List
	public static ArrayList<String> RELATIVE_PRONOUN;
	public static String relativePronounTmp[] = {
			"that",
			"who",
			"whom",
			"which"
	};

	public static ArrayList<String> RELATIVE_ADVERB;
	// 관계부사 List
	public static String relativeAdverbTmp[] = {
			"where",
			"when"
	};
	
	public static ArrayList<String> WH_PHRASE;
	// 관계사 phrase tag명들
	public static String whPhraseTmp[] = {
			"WHADJP",
			"WHAVP",
			"WHADVP",
			"WHNP",
			"WHPP"
	};
	
	public static ArrayList<String> WH_WORD;
	public static String whWordTmp[] = {
			"WDT",
			"WP",
			"WRB"
	};

	public static void initialize () {
		RELATIVE_PRONOUN = new ArrayList<String>(Arrays.asList(relativePronounTmp));
		RELATIVE_ADVERB = new ArrayList<String>(Arrays.asList(relativeAdverbTmp));
		WH_PHRASE = new ArrayList<String>(Arrays.asList(whPhraseTmp));
		WH_WORD = new ArrayList<String>(Arrays.asList(whWordTmp));
	}
}
