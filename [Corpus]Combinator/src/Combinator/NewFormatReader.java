package Combinator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NewFormatReader {
	public static ArrayList<Sentence> readNewFormatXml (String filePath) {
		ArrayList<Sentence> sentenceList = new ArrayList<Sentence>();
		
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(filePath);
			doc.getDocumentElement().normalize();
			NodeList sentenceNodeList = doc.getElementsByTagName("sentence");

			for (int temp = 0; temp < sentenceNodeList.getLength(); temp++) {
				Node sentence = sentenceNodeList.item(temp);
				if (sentence.getNodeType() == Node.ELEMENT_NODE) {
					Element sentenceElement = (Element) sentence;
					String text = sentenceElement.getElementsByTagName("text").item(0).getTextContent();
					text = text.replace("            ", "");
					text = text.replace("        ", "");
					text = text.replace("    ", "");
					Sentence s = new Sentence (sentenceElement.getAttribute("id"),
							text,
							sentenceElement.getAttribute("type"),
							null);



					Node entityListTag = sentenceElement.getElementsByTagName("entityList").item(0);
					if (entityListTag != null && entityListTag.getNodeType() == Node.ELEMENT_NODE) {
						Element entityListTagElement = (Element) entityListTag;
						NodeList entityList = entityListTagElement.getElementsByTagName("entity");
						for (int temp2 = 0; temp2 < entityList.getLength(); temp2++) {
							Node entity = entityList.item(temp2);
							if (entity.getNodeType() == Node.ELEMENT_NODE) {
								Element entityElement = (Element) entity;
								Entity e = new Entity(Integer.parseInt(entityElement.getAttribute("charOffset").split("-")[0]),
										Integer.parseInt(entityElement.getAttribute("charOffset").split("-")[1]),
										entityElement.getAttribute("id"),
										entityElement.getAttribute("name"),
										Integer.parseInt(entityElement.getAttribute("seqid")),
										entityElement.getAttribute("type"),
										entityElement.getAttribute("subtype"));
								s.entityList.add(e);
							}
						}
					}



					Node pairListTag = sentenceElement.getElementsByTagName("pairList").item(0);
					if (pairListTag != null && pairListTag.getNodeType() == Node.ELEMENT_NODE) {
						Element pairListTagElement = (Element) pairListTag;

						NodeList pairList = pairListTagElement.getElementsByTagName("pair");
						for (int temp2 = 0; temp2 < pairList.getLength(); temp2++) {
							Node pair = pairList.item(temp2);
							if (pair.getNodeType() == Node.ELEMENT_NODE) {
								Element pairElement = (Element) pair;
								Pair p = new Pair (pairElement.getAttribute("e1"),
										pairElement.getAttribute("e2"),
										pairElement.getAttribute("id"),
										pairElement.getAttribute("interaction"),
										"");
								s.pairList.add(p);
							}
						}
					}
					sentenceList.add(s);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sentenceList;
	}
	
	public static ArrayList<Sentence> readNewFormatXmlInFolder(String source){
		ArrayList<Sentence> result = new ArrayList<Sentence>();
		File dir = new File(source); 
		File[] fileList = dir.listFiles(); 
		try{
			for(int i = 0 ; i < fileList.length ; i++){
				File file = fileList[i]; 
				if(file.isFile()){
					// 파일이 있다면 파일 이름 출력
					System.out.println("\t 파일 이름 = " + file.getName());
					System.out.println(file.getPath());
					if (file.getName().endsWith(".xml") || file.getName().endsWith(".XML")) {
						result.addAll(NewFormatReader.readNewFormatXml(file.getPath()));
					}
				}else if(file.isDirectory()){
					System.out.println("디렉토리 이름 = " + file.getName());
					// 서브디렉토리가 존재하면 재귀적 방법으로 다시 탐색
					readNewFormatXmlInFolder(file.getCanonicalPath().toString()); 
				}
			}
		}catch(IOException e){

		}
		
		return result;
	}
}
