package View;

import java.util.HashMap;
import java.util.LinkedList;

import Model.InputImage;

public class GraphIn {
	public HashMap<String, LinkedList> inputGraph(InputImage img) {
		
		String arrVertices[];
		String dependencies;
		String arrSucessors[];
		String mmfAndSucessors[];
		String sucessors;
		String mmf;
		
		HashMap<String, LinkedList> graph = new HashMap<String, LinkedList>();
		
		arrVertices = img.getMmfs().split(" ");
		
		for (int i=0 ; i < img.getSucessors().size() ; i++) {
			mmfAndSucessors = img.getSucessors().get(i).split(":");
			mmf = mmfAndSucessors[0];
			
			if (mmfAndSucessors.length > 0) {
				arrSucessors = mmfAndSucessors[1].split(" ");
				
				LinkedList<String> sucessorsList = new LinkedList<String>();
				
				for (String sucessor : arrSucessors) {
					if (! sucessor.equals("")) {
						sucessorsList.add(sucessor);	
					}
				}
				
				graph.put(mmf, sucessorsList);
				
			} else {
				graph.put(mmf, null);
			}
				
		}
		
		return graph;
	}
}
