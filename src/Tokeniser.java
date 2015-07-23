import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

/**
 * Open a folder of documents and parse them into terms
 * @author Harry Scells
 */
public class Tokeniser {
	
	private HashMap<String, List<String>> documents = new HashMap<String, List<String>>();
	private HashMap<String, Integer> terms = new HashMap<String, Integer>();
	public String path;
	
	/**
	 * Create a new Tokeniser object
	 * @param path
	 */
	public Tokeniser(String path) {
		this.path = path;
	}
	
	/**
	 * Parse a folder of documents for terms
	 * @return HashMap<String, Integer> terms The list of terms that were parsed
	 * @throws IOException
	 */
	public void parse() throws IOException{
		
		Files.walk(Paths.get(this.path)).forEach(filePath -> {
			if (Files.isRegularFile(filePath)) {
				try {
					documents.put(filePath.toString(), Files.readAllLines(filePath));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});	
		
		documents.keySet().forEach(document-> {
			documents.get(document).forEach(lines -> {
				lines = lines.replaceAll("<.*>", "");
				lines = lines.replaceAll("[.,;:+=!@#$%^&*()`{}|\'\"<>]~?", "");
				lines = lines.replaceAll("[\t\n]+", "");
				lines = lines.replaceAll(" +", " ");
				lines = lines.toLowerCase();
				String[] tokens = lines.split(" ");
				for (int i = 0; i < tokens.length; i++) {
					if (!terms.containsKey(tokens[i])) {
						terms.put(tokens[i], 1);
					} else {
						terms.put(tokens[i], terms.get(tokens[i]) + 1);
					}
				}
			});
		});
	}

	/**
	 * Get the list of terms generated from parse()
	 * @see parse
	 * @return HashMap<String, Integer> terms List of terms
	 */
	public HashMap<String, Integer> getTerms() {
		return this.terms;
	}
	
	/**
	 * get the list of documents generated from parse()
	 * @see parse
	 * @return HashMap<String, List<String>> documents List of documents
	 */
	public HashMap<String, List<String>> getDocuments() {
		return this.documents;
	}
}