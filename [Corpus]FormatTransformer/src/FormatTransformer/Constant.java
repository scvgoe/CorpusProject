package FormatTransformer;

import java.util.ArrayList;
import java.util.Arrays;


public class Constant {
	public static String ROOT_DIR = "/Users/daesungkim/Desktop/ACE_2004/DATA/ENGLISH/";
	public static String RESULT_DIR = "/Users/daesungkim/Desktop/transformerResult/";
	public static String INDENTATION1 = "    "; //4 spaces
	public static String INDENTATION2 = "        "; //8 spaces
	public static String INDENTATION3 = "            "; //12 spaces
	public static ArrayList<String> PRONOUNS;
	public static String pronounsTmp[] = {"I", "me", "you", "he", "him", "she", "her", "it", "we", "us", "they", "them",
		"my", "mine", "your", "yours", "her", "hers", "his", "its", "our", "ours", "their", "theirs",
		"this", "that", "these", "those",
		"who", "whom", "whose", "which", "what", "that",
		"all", "another", "any", "anyone", "anybody", "anything", "both", "each", "either", "everybody", 
		"everyone", "everything", "few", "many", "neither", "nobody", "none", "no one", "nothing", "none",
		"one", "several", "some", "somebody", "someone", "something",
		"myself", "yourself", "himself", "herself", "itself", "ourselves", "yourselves", "themselves",
		"each other", "one another", "we\'re", "that\'s", "we\'ve", "it\'s"};
	
	
	
	public static void initialize () {
		PRONOUNS = new ArrayList<String>(Arrays.asList(pronounsTmp));
	}
}
