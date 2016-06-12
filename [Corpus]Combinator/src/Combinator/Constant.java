package Combinator;

import java.util.ArrayList;
import java.util.Arrays;


public class Constant {
	public static String ROOT_DIR = "/Users/daesungkim/Desktop/transformerResult_Module2_Done/"; // 분리 대상이 되는 xml 파일들이 위치한 폴더 
	public static String RESULT_DIR = "/Users/daesungkim/Desktop/combinatorResult/"; // 분리 결과가 저장될 폴더
	
	public static String INDENTATION1 = "    "; //4 spaces
	public static String INDENTATION2 = "        "; //8 spaces
	public static String INDENTATION3 = "            "; //12 spaces

	public static ArrayList<String> validTypes;
	public static String tmpValidTypes[] = {
			"EMP-ORG/Employ-Executive",
			"EMP-ORG/Member-of-Group",
			"EMP-ORG/Employ-Staff",
			"EMP-ORG/Subsidiary",
			"PHYS/Located",
			"PHYS/Part-Whole",
			"PER-SOC/Family",
			"PER-SOC/Business",
			"GPE-AFF/Based-In",
			"GPE-AFF/Citizen-or-Resident"
	};

	public static void initialize () {
		validTypes = new ArrayList<String>(Arrays.asList(tmpValidTypes));
	}
}
