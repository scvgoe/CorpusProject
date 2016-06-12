package FormatTransformer;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CorpusReader {
	private static ArrayList<Sentence> corpusXmlToSentenceList (String filePath) {
		ArrayList<Sentence> sentenceList = new ArrayList<Sentence>();

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(filePath);
			doc.getDocumentElement().normalize();

			Element sourceFile = (Element) doc.getElementsByTagName("source_file").item(0);
			String sourceURI = sourceFile.getAttribute("URI").toUpperCase();
			sourceURI = sourceURI.substring(0, sourceURI.length()-4).replace(".", "_");
			sourceURI += ".SGM";
			String[] splitFilePath = filePath.split("/");
			String sourceURIPath = "";
			for (int i=0; i<splitFilePath.length-1; i++) {
				sourceURIPath = sourceURIPath + "/" + splitFilePath[i];
			}
			sourceURI = sourceURIPath + "/" + sourceURI;

			NodeList entityNodeList = doc.getElementsByTagName("entity");
			NodeList relationNodeList = doc.getElementsByTagName("relation");

			for (int i=0; i<relationNodeList.getLength(); i++) {
				String text = null;
				Node relationNode = relationNodeList.item(i);
				if (relationNode.getNodeType() == Node.ELEMENT_NODE) {
					Element relationElement = (Element) relationNode;
					NodeList relationMentionNodeList = relationElement.getElementsByTagName("relation_mention");
					for (int j=0; j<relationMentionNodeList.getLength(); j++) {
						Node relationMentionNode = relationMentionNodeList.item(j);
						if (relationMentionNode.getNodeType() == Node.ELEMENT_NODE) {
							Element relationMentionElement = (Element) relationMentionNode;
							if (relationMentionElement.getElementsByTagName("ldc_extent").item(0).getNodeType() == Node.ELEMENT_NODE) {
								Element ldcExtentElement = (Element) relationMentionElement.getElementsByTagName("ldc_extent").item(0);
								if (ldcExtentElement.getElementsByTagName("charseq").item(0).getNodeType() == Node.ELEMENT_NODE) {
									Element charseq = (Element) ldcExtentElement.getElementsByTagName("charseq").item(0);
									text = SGMParser.getSentenceText(Integer.parseInt(charseq.getAttribute("START")),
											Integer.parseInt(charseq.getAttribute("END")),
											sourceURI);
									if (text == null) continue;

									Sentence s = new Sentence ("0", text);
									sentenceList.add(s);
									String e1 = null;
									String e2 = null;
									NodeList relEntityArgNodeList = relationElement.getElementsByTagName("rel_entity_arg");
									for (int k=0; k<relEntityArgNodeList.getLength(); k++) {
										if (relEntityArgNodeList.item(k).getNodeType() == Node.ELEMENT_NODE) {
											Element relEntityArg = (Element) relEntityArgNodeList.item(k);
											if (relEntityArg.getAttribute("ARGNUM").equals("1")) {
												e1 = relEntityArg.getAttribute("ENTITYID");
											}
											else {
												e2 = relEntityArg.getAttribute("ENTITYID");
											}
										}
									}

									String type = relationElement.getAttribute("TYPE");
									String subtype = "";
									if (relationElement.hasAttribute("SUBTYPE")) {
										subtype = relationElement.getAttribute("SUBTYPE");
									}
									Pair p = new Pair (e1, e2,
											relationElement.getAttribute("ID"),
											type, subtype);
									s.pairList.add(p);

									NodeList relMentionArgNodeList = relationMentionElement.getElementsByTagName("rel_mention_arg");
									for (int k=0; k<relMentionArgNodeList.getLength(); k++) {
										Node relMentionArgNode = relMentionArgNodeList.item(k);
										if (relMentionArgNode.getNodeType() == Node.ELEMENT_NODE) {
											Element relMentionArgElement = (Element) relMentionArgNode;
											Element charseqElement = ((Element) 
													((Element) relMentionArgElement
															.getElementsByTagName("extent").item(0))
													.getElementsByTagName("charseq")
													.item(0));
											String id = (relMentionArgElement.getAttribute("ARGNUM").equals("1")) ?
													e1 : e2;
											int entityStart = Integer.parseInt(charseqElement.getAttribute("START"));
											int entityEnd = Integer.parseInt(charseqElement.getAttribute("END"));
											String name = charseqElement.getTextContent().replace("\n", " ");
											int sentenceStart = SGMParser.sgmParse(sourceURI).indexOf(s.text);
											String entityType = "";
											String entitySubtype = "";
											for (int l=0; l<entityNodeList.getLength(); l++) {
												Element entityElement = ((Element) entityNodeList.item(l));
												if (entityElement.getAttribute("ID").equals(id)) {
													entityType = entityElement.getAttribute("TYPE");
													if (entityElement.hasAttribute("SUBTYPE")) {
														entitySubtype = entityElement.getAttribute("SUBTYPE");
													}
													break;
												}
											}
											Entity e = new Entity (entityStart - sentenceStart,
													entityEnd - sentenceStart,
													id,
													name,
													Integer.parseInt(relMentionArgElement.getAttribute("ARGNUM")) - 1,
													entityType,
													entitySubtype);
											s.entityList.add(e);
											if (!e.validationCheck()) {
												sentenceList.remove(s);
												break;
											}
										}
									}

								}
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return sentenceList;
	}

	public static ArrayList<Sentence> corpusXmlInFolderToSentenceList (String folderPath) {
		ArrayList<Sentence> result = new ArrayList<Sentence>();
		File dir = new File(folderPath); 
		File[] fileList = dir.listFiles(); 
		try{
			for(int i = 0 ; i < fileList.length ; i++){
				File file = fileList[i]; 
				if(file.isFile()){
					if (file.getName().endsWith("_apf.xml") || file.getName().endsWith("_APF.XML") ||
							file.getName().endsWith("ENG_A.XML")) {
						result.addAll(corpusXmlToSentenceList(file.getPath()));
					}
				}else if(file.isDirectory()){
					result.addAll(corpusXmlInFolderToSentenceList(file.getCanonicalPath().toString())); 
				}
			}
		}catch(IOException e){

		}

		return result;
	}
}
