package Controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import Model.IOHandler;
import Model.InputImage;
import Model.MMF;
import Model.Rate;
import Support.Graph;
import Support.NPV;
import View.GraphIn;

public class Project {

	public static Rate r;
	public static Integer scenariosNum;
	
	public static void main(String[] args) throws IOException {	
		
		int iFiles;
		File dir = new File("./input/simmulation_input/");
		String filename[] = dir.list();
	
		for ( iFiles =0 ; iFiles < dir.list().length ; iFiles++ ) {
			InputImage img = new InputImage();
			HashMap<String, LinkedList> graph = new HashMap<String, LinkedList>();
			GraphIn gIn =  new GraphIn();
			Graph g = new Graph();
			int periods;
			String strFileName = filename[iFiles];
			
			System.out.println((new Date()).toString() + " - Simulação iniciada para o arquivo: " + strFileName);
			
			ArrayList<String> orders = new ArrayList<String>();
			ArrayList<ArrayList<MMF>> ordersNew = new ArrayList<ArrayList<MMF>>();
			ArrayList<ArrayList<Double>> npvs = new ArrayList<ArrayList<Double>>();
			ArrayList<Model.Sequence> sequences = new ArrayList<Model.Sequence>();
			
			IOHandler.deleteFile(strFileName);
			img = IOHandler.readInput(strFileName);
			
			if (img.getSimulationType().equals("CONSTANT")) {
				scenariosNum = 1;
			} else {
				scenariosNum = img.getScenariosNum();
			}
			graph.clear();
			graph = gIn.inputGraph(img);
			
			g.setOrders(new ArrayList<String>());
			orders.clear();
			orders = g.topologicalOrder(graph);
			
			periods = img.getPeriods();
			r = new Rate(img.getRate());
			
			NPV myNPV = new NPV(r);
			
			ordersNew.clear();
			ordersNew = Graph.formatOrders(orders, img, strFileName);
			
//			for (int i = 0; i < ordersNew.size(); i++) {
//				Model.Sequence sequence = new Model.Sequence();
//				sequence.setSequence(ordersNew.get(i));
//				
//				sequence.sortSequence();
//				sequence.calculateNPV(scenariosNum, img.getSimulationType());
//				
//				sequences.add(sequence);
//			}
//			
//			//Outputs
//			IOHandler.exportData(sequences, "Sequences_"+ strFileName, img.getSimulationType());
	
			System.out.println((new Date()).toString() + " - Simulação concluída para o arquivo: " + strFileName);
		}
	}
}
