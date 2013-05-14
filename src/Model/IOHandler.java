package Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

public class IOHandler {
	
	public static void deleteFile(String strFileName) throws IOException{
		File f = new File("./output/" + strFileName);
		
		if (f.exists()){
			f.delete();
		}
	}
	
	public static InputImage readInput(String strFileName) throws IOException{
		try {
			//Reads the first file in the refered folder
			FileReader reader = new FileReader(new File("./input/simmulation_input/" + strFileName));
			
			BufferedReader buffReader = new BufferedReader(reader);
			String line = null;
			
			InputImage img = new InputImage();
			String prevLine = null;
			int i=1;
			
			while( (line = buffReader.readLine()) != null ) {
				
				if (prevLine != null){
					if (prevLine.equals("#Rate")) {
						img.setRate(Float.parseFloat(line));
					} else if (prevLine.equals("#Num MMFs")) {
						img.setMmfNum(Integer.parseInt(line));
					} else if (prevLine.equals("#MMFs")) {
						img.setMmfs(line);
					} else if (prevLine.equals("#Sucessors")) {
						while(! (line = buffReader.readLine()).equals("") ) {
							//System.out.println("Linha " + i + ": " + line);
							i++;
							
							img.getSucessors().add(line);
							i++;
						}
					} else if (prevLine.equals("#Periods")) {
						img.setPeriods(Integer.parseInt(line));						
					}else if (prevLine.equals("#Number of Scenarios")) {
						img.setScenariosNum(Integer.parseInt(line));
					} else if (prevLine.equals("#SimulationFlag")) {
						img.setSimulationType(line.toString());
					} else if (prevLine.equals("#Value Periodos")) {
						if (line.equals("")) { 
							img.getValuesThrougTime().add(line);
						}
						
						while(! (line = buffReader.readLine()).equals("") ) {
							//System.out.println("Linha " + i + ": " + line);
							i++;
							
							img.getValuesThrougTime().add(line);
							i++;
						}
					}
				}
				
				i++;
			    prevLine = line;
				
			}
			
			buffReader.close();  
			reader.close();
			
			return img;
			
		} catch (FileNotFoundException e) {
			// TODO Tratar ou logar exceção
			throw new java.lang.RuntimeException("Erro ao ler o arquivo!", e);
		}
	}
	
	public static void exportData(ArrayList<Model.Sequence> sequences, String fileName, String simulationType) {
		
		try {
			PrintStream out = new PrintStream(new FileOutputStream("./output/" + fileName));
			
			for (int i = 0; i < sequences.size(); i++) {
				
				String strSequence = "";
				String outputLine = "";
				
				for (int j = 0 ; j < sequences.get(i).getSequence().size() ; j++) {
					strSequence += " " + sequences.get(i).getSequence().get(j).getName();
				}
				
				if (simulationType.equals("CONSTANT")) {
					
					for (int j = 0 ; j < sequences.get(i).getNpvs().size() ; j++) {
						
						outputLine = (j+1) + "/" + strSequence.trim();
						outputLine += "/" + sequences.get(i).getNpvs().get(j).toString().replace(".", ",");
						out.println(outputLine);
					}
					
				} else if (simulationType.equals("TRIANGULAR")) {
					for (int j = 0 ; j < sequences.get(i).getNpvs().size() ; j++) {
						
						outputLine = (j+1) + "/" + strSequence.trim();
						outputLine += "/" + sequences.get(i).getNpvs().get(j).toString().replace(".", ",");
						out.println(outputLine);
					}
				} else if ((simulationType.equals("BROWNIAN_MOTION_G")) || (simulationType.equals("BROWNIAN_MOTION_A"))) {
					for (int j = 0 ; j < sequences.get(i).getNpvs().size() ; j++) {
						
						outputLine = (j+1) + "/" + strSequence.trim();
						outputLine += "/" + sequences.get(i).getNpvs().get(j).toString().replace(".", ",");
						out.println(outputLine);
					}
				} else {
					//TODO
				}				
			}
			
			out.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}