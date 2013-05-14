package Support;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import Distribuicoes.Normal;
import Distribuicoes.Triangular;
import Model.CFE;
import Model.CashFlowStream;
import Model.InputImage;
import Model.LinhaOutput;
import Model.MMF;
import Model.Rate;

public class Graph {
	
	public static Stack<String> myStack = new Stack<String>();
	public static int sizeGraph;
	public static ArrayList<String> orders = new ArrayList<String>();

	public ArrayList<String> topologicalOrder(HashMap<String, LinkedList> graph) {
			
		for (Iterator iterator = graph.keySet().iterator(); iterator.hasNext();) {
			String vertice = (String) iterator.next();
			List vizinhos = graph.get(vertice);
		
	     	if (vizinhos.isEmpty()) { // sumidouro
					myStack.add(vertice);
					
					if (graph.size() == 1) {
						//printStack();
						orders.add(getOrder());
					} else {
						
						HashMap<String, LinkedList> graphCopy = new HashMap<String, LinkedList>(); 
						cloneGraph(graph, graphCopy);
						
						//retirando o vertice do grafo
						graphCopy.remove(vertice);
						
						// tirar o vertice das listas
						for (Iterator iteratorCp = graphCopy.values().iterator(); iteratorCp.hasNext();) {
							List vizinhosCp = (List) iteratorCp.next();
							
							vizinhosCp.remove(vertice);
				
					}
					topologicalOrder(graphCopy);
				}
					
				myStack.pop();
			}
		}
		
		return orders;
	}
	
	private String getOrder() {
		StringBuffer ordemStr = new StringBuffer();
		
		for (int i = myStack.size()-1; i >= 0; i--) {
			ordemStr.append(myStack.get(i)).append(" ");
		}
		
		return ordemStr.toString();
	}

	private static void printStack() {
		
		StringBuffer ordemStr = new StringBuffer();
		
		for (int i = myStack.size()-1; i >= 0; i--) {
			ordemStr.append(myStack.get(i)).append(" ");
		}
		
		System.out.println(ordemStr.toString());
	}

	private static void cloneGraph(HashMap<String, LinkedList> graphSrc, HashMap<String, LinkedList> graphCopy) {
		
		for (Iterator iterator = graphSrc.keySet().iterator(); iterator.hasNext();) {
			String vertice = (String) iterator.next();
			List vizinhos = graphSrc.get(vertice);
			
			graphCopy.put(vertice, new LinkedList<String>(vizinhos));
			
		}
	}
	
	public static ArrayList<ArrayList<MMF>> formatOrders(ArrayList<String> sequences, InputImage img, String strFileName) throws FileNotFoundException {
		
		PrintStream out = new PrintStream(new FileOutputStream("./output/Sequences_" + strFileName));
		
		ArrayList<String> valuesThrougTime = img.getValuesThrougTime();
		int periods = img.getPeriods();
		Rate r = new Rate(img.getRate());
		String simulationType = img.getSimulationType();
		float npv = (float) 0.0;
		
		Integer scenariosNum;
		if (img.getSimulationType().equals("CONSTANT")) {
			scenariosNum = 1;
		} else {
			scenariosNum = img.getScenariosNum();
		}
		
		ArrayList<ArrayList<MMF>> sequencesNew = new ArrayList<ArrayList<MMF>>();
		HashMap<String, ArrayList<String>> valuesMap = new HashMap<String, ArrayList<String>>(); 
		MMF myMMF;
		
		// colocar todos os flows na memoria, dps iterar sobre as sequencias e atribuí-los na ordem
		String arrMMFs[];
		String arrValuesThroughTime[];
		String arrValuesThroughTimeMaxMedMin[];
		String arrValsVarDrift[];
		
		float v0 = (float) 0.0;
		float v1 = (float) 0.0;
		float var = (float) 0.0;
		float drift = (float) 0.0;
		
		float valorNormal = (float) 0.0;
		
		Normal myNormal = new Normal();
		
		//Mapping the values through time
		for (int i = 0; i < valuesThrougTime.size(); i++) {
			arrValuesThroughTime = valuesThrougTime.get(i).split(" ");
			ArrayList<String> values = new ArrayList<String>();
			
			//Starting with 1 cause we don't want the label
			for (int k = 1; k < arrValuesThroughTime.length; k++) {
				values.add(arrValuesThroughTime[k]);
			}
			
			valuesMap.put(arrValuesThroughTime[0].replace(":", ""), values);
		}
		
		//For each sequence
		for (int iSeq = 0; iSeq < sequences.size(); iSeq++) {
			String element = sequences.get(iSeq);
			//System.out.println(iSeq + ")" + (new Date()).toString() + ": " + element);

			//For each Scenario
			for (int  iScenarios = 0; iScenarios < scenariosNum; iScenarios++) {
				
				//Making the npv = 0, since we have 1 npv per period
				npv = (float) 0.0;
				
				arrMMFs = element.split(" ");
				float[][] cashflowStreams = new float [img.getMmfNum()][img.getPeriods()];
				
				//For each MMF
				for (int iMMFs = 0; iMMFs < arrMMFs.length; iMMFs++) {
					//System.out.println("immf " + iMMFs);
					//For each Period
					for (int j = 0 ; j < img.getPeriods() ; j++) {
						//System.out.println("j " + j);
						if (simulationType.equals("CONSTANT")) {
							if ((j + iMMFs) < img.getPeriods()) {
								cashflowStreams[iMMFs][j+iMMFs] = Float.valueOf(valuesMap.get(arrMMFs[iMMFs]).get(j));
								npv += cashflowStreams[iMMFs][j+iMMFs] / Math.pow((r.getR() + 1), j+iMMFs+1);
							}
						} else if (simulationType.equals("TRIANGULAR")) {
							arrValuesThroughTimeMaxMedMin = valuesMap.get(arrMMFs[iMMFs]).get(j).split(",");
							Triangular myTriangular = new Triangular(new Float(arrValuesThroughTimeMaxMedMin[0]),
									new Float(arrValuesThroughTimeMaxMedMin[1]),
									new Float(arrValuesThroughTimeMaxMedMin[2]));
							
							if ((j + iMMFs) < img.getPeriods()) {								
								cashflowStreams[iMMFs][j+iMMFs] = Float.valueOf(String.valueOf(myTriangular.getSample()));
								npv += cashflowStreams[iMMFs][j+iMMFs] / Math.pow((r.getR() + 1), j+iMMFs+1);
							}
							
						} else if (simulationType.equals("BROWNIAN_MOTION_G") ||
								   simulationType.equals("BROWNIAN_MOTION_A")) {
							
							/*
							 * In the brownian motion, it always has 4 parameters:
							 * 1st cost, 2nd earning, 3rd var, 4th drift 
							 * 
							 */
							
							v0 = Float.valueOf(valuesMap.get(arrMMFs[iMMFs]).get(0).replace(",", ""));
							v1 = Float.valueOf(valuesMap.get(arrMMFs[iMMFs]).get(1).replace(",", ""));
							var = Float.valueOf(valuesMap.get(arrMMFs[iMMFs]).get(2).replace(",", ""));
							drift =Float.valueOf(valuesMap.get(arrMMFs[iMMFs]).get(3).replace(",", ""));
							
							//if there is the first period
							if (j == 0) {
								//First the cost - v0
								if ((j + iMMFs) < img.getPeriods()) {	
									cashflowStreams[iMMFs][j+iMMFs] = v0;

									//calculating the npv
									npv += v0 / Math.pow((r.getR() + 1), j+iMMFs+1);
								}
								
								j++;
								//now we have to check the first earn - v1								
								if ((j + iMMFs) < img.getPeriods()) {	
									cashflowStreams[iMMFs][j+iMMFs] = v1;
									
									//calculating the npv
									npv += v1 / Math.pow((r.getR() + 1), j+iMMFs+1);
								}
							} else {								
								if (simulationType.equals("BROWNIAN_MOTION_G")) {
									if ((j + iMMFs) < img.getPeriods()) {
									
										valorNormal = (float) myNormal.getSample();
										//V(t+1) = V(t) + (<sigma>*V(t)*N(0,1)) + <Drift>*V(t)
										cashflowStreams[iMMFs][j+iMMFs] = // V(t+1) = 
											cashflowStreams[iMMFs][j+iMMFs-1] + // V(t) + 
											(var*cashflowStreams[iMMFs][j+iMMFs-1])*valorNormal + // var*v(t)*N(0,1) +										
											drift*cashflowStreams[iMMFs][j+iMMFs-1]; // drift*V(t)
										
										//System.out.println(valorNormal);
										
										if (cashflowStreams[iMMFs][j+iMMFs] < 0){
											cashflowStreams[iMMFs][j+iMMFs] = (float) 1;
										}
									}
								} else { //BROWNIAN_MOTION_A
									if ((j + iMMFs) < img.getPeriods()) {
										//V(t+1) = V(t) + (<sigma>*V(t)*N(0,1)) + <drift>
										cashflowStreams[iMMFs][j+iMMFs] = // V(t+1) = 
											cashflowStreams[iMMFs][j+iMMFs-1] + // V(t) + 
											(var*cashflowStreams[iMMFs][j+iMMFs-1]) 
												*(Float.valueOf(String.valueOf(myNormal.getSample()))) + // var*v(t)*N(0,1) + 
											drift;// drift
										if (cashflowStreams[iMMFs][j+iMMFs] < 0){
											cashflowStreams[iMMFs][j+iMMFs] = (float) 0;
										}
									}
								}
								
								//calculating the npv
								if ((j + iMMFs) < img.getPeriods()) {
									npv += cashflowStreams[iMMFs][j+iMMFs] / Math.pow((r.getR() + 1), j+iMMFs+1);
								}
							}
						}//If brownian motion
					}//j
					//System.out.println(" ");
				}//iMMFs
				
				//Outputs the NPV 
				out.println(iScenarios + "/" + element + "/" + BigDecimal.valueOf(npv).toPlainString());
			}
		}
		out.close();
		return null;
		
	}
	
	public static ArrayList<ArrayList<MMF>> formatOrders_old(ArrayList<String> sequences, InputImage img, String strFileName) throws FileNotFoundException {
		
		ArrayList<LinhaOutput> output = new ArrayList<LinhaOutput>();
		
		PrintStream out = new PrintStream(new FileOutputStream("./output/Sequences_" + strFileName));
		
		ArrayList<String> valuesThrougTime = img.getValuesThrougTime();
		int periods = img.getPeriods();
		Rate r = new Rate(img.getRate());
		String simulationType = img.getSimulationType();
		double npv = 0.0;
		int iSeq=1;
		
		Integer scenariosNum;
		if (img.getSimulationType().equals("CONSTANT")) {
			scenariosNum = 1;
		} else {
			scenariosNum = img.getScenariosNum();
		}
		
		ArrayList<ArrayList<MMF>> sequencesNew = new ArrayList<ArrayList<MMF>>();
		HashMap<String, ArrayList<String>> valuesMap = new HashMap<String, ArrayList<String>>(); 
		MMF myMMF;
		
		// colocar todos os flows na memoria, dps iterar sobre as sequencias e atribuí-los na ordem
		Iterator<String> it = sequences.iterator();
		String arrMMFs[];
		String arrValuesThroughTime[];
		String arrValuesThroughTimeMaxMedMin[];
		String arrValsVarDrift[];
		
		//Mapping the values through time
		for (int i = 0; i < valuesThrougTime.size(); i++) {
			arrValuesThroughTime = valuesThrougTime.get(i).split(" ");
			ArrayList<String> values = new ArrayList<String>();
			
			//Starting with 1 cause we don't want the label
			for (int k = 1; k < arrValuesThroughTime.length; k++) {
				values.add(arrValuesThroughTime[k]);
			}
			
			valuesMap.put(arrValuesThroughTime[0].replace(":", ""), values);
		}
		
		//para cada sequencia
		while (it.hasNext()) {
			String element = it.next().trim();
			
			System.out.println(iSeq + ")" + (new Date()).toString() + ": " + element);
//			System.out.println("1º total de memoria ? :"+Runtime.getRuntime().maxMemory());
//            System.out.println("2º quanto estou usando de memoria ?:"+Runtime.getRuntime().totalMemory());       
//            System.out.println("3º quanto ainda tenho de memoria ?:"+Runtime.getRuntime().freeMemory());  
			
			arrMMFs = element.split(" ");
			
			ArrayList<MMF> sequence = new ArrayList<MMF>();
	
			//para cada mmf
			for (int iMMFs = 0; iMMFs < arrMMFs.length; iMMFs++) {
				myMMF = new MMF(arrMMFs[iMMFs]);
				
				ArrayList<CashFlowStream> cashFlowStreams = new ArrayList<CashFlowStream>();
				
				//para cada cenário
				for (int  iScenarios = 0; iScenarios < scenariosNum; iScenarios++) {
					
					CashFlowStream cashFlowStream = new CashFlowStream(new ArrayList<CFE>());
					
					//Zerando o npv, pois temos 1 npv para cada cenario
					npv = 0.0;
					
					if (simulationType.equals("CONSTANT") || simulationType.equals("TRIANGULAR")) {
						
						//para cada período
						for (int j = 0 ; j < valuesMap.get(arrMMFs[iMMFs]).size() ; j++) {
							CFE e;
							
							if (simulationType.equals("CONSTANT")) {
								
								e  = new CFE(new Float(valuesMap.get(arrMMFs[iMMFs]).get(j)), simulationType);
								
							} else if (simulationType.equals("TRIANGULAR")) {
								
								arrValuesThroughTimeMaxMedMin = valuesMap.get(arrMMFs[iMMFs]).get(j).split(",");
								e  = new CFE(new Float(arrValuesThroughTimeMaxMedMin[0]), 
										new Float(arrValuesThroughTimeMaxMedMin[1]),
										new Float(arrValuesThroughTimeMaxMedMin[2]));
								
							} else {
								//ERROR
								e = new CFE(null, null);
							}
							
							//Calculando o NPV de forma iterativa
							if (iMMFs > j) {
								npv += 0 / Math.pow((r.getR() + 1), j+1);
							} else {
								npv += e.getValue() / Math.pow((r.getR() + 1), j+1);
							}
							
							//cashFlowStream.getCashFlowStream().add(e);
							
							e = null;
						}
					} else if ((simulationType.equals("BROWNIAN_MOTION_G"))|| (simulationType.equals("BROWNIAN_MOTION_A"))) {
						
						//para cada período
						for (int i = 0 ; i < periods ; i++){
							CFE e;
							
							//In the brownian motion, it always has 4 parameters:
							//1st cost, 2nd earning, 3rd var, 4th drift
							Float v0 = Float.valueOf(valuesMap.get(arrMMFs[iMMFs]).get(0).replace(",", ""));
							Float v1 = Float.valueOf(valuesMap.get(arrMMFs[iMMFs]).get(1).replace(",", ""));
							
							Float var = Float.valueOf(valuesMap.get(arrMMFs[iMMFs]).get(2).replace(",", ""));
							Float drift = Float.valueOf(valuesMap.get(arrMMFs[iMMFs]).get(3).replace(",", ""));
							
							if ( i == 0 ) {
								e = new CFE(v0, v0, v0);
							} else if (i == 1) {
								e = new CFE(v1, v1, v1);
							} else {
								e = new CFE(cashFlowStream.getCashFlowStream().get(i-1).getValue(), var ,drift, img.getSimulationType());
							}
							
							//Calculando o NPV de forma iterativa
							if (iMMFs > i) {
								npv += 0 / Math.pow((r.getR() + 1), +1);
							} else {
								npv += e.getValue() / Math.pow((r.getR() + 1), i+1);
							}
							
							//cashFlowStream.getCashFlowStream().add(e);
							
							e = null;
						}
					}
					
					//out.println(iScenarios+1 +"/"+ element + "/" + String.valueOf(npv).replace(".", ","));
					
					//IOHandler.exportData(sequences, "Sequences_"+ strFileName, img.getSimulationType());
					
					
					cashFlowStream.setNpv(npv);
					cashFlowStreams.add(cashFlowStream);
				}
				//myMMF.setCashFlowStreams(cashFlowStreams);
				//sequence.add(myMMF);
			}
			
			//sequencesNew.add(sequence);
			
			iSeq++;
			System.gc();
		}
		
		out.close();
		
		return sequencesNew;
	}

	public static ArrayList<String> getOrders() {
		return orders;
	}

	public static void setOrders(ArrayList<String> orders) {
		Graph.orders = orders;
	}
	
	
}
