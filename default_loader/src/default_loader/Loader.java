package default_loader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import string_operations.StrOps;

public class Loader {
	ArrayList<ArrayList<ArrayList<String>>> parameters_2d; 
	ArrayList<String> parameter_names_2d;
	ArrayList<ArrayList<String>> parameters_1d;
	ArrayList<String> parameter_names_1d;
	ArrayList<String> parameters_0d;
	ArrayList<String> parameter_names_0d;
	ArrayList<String> comments;
	
	public Loader(){
		parameters_2d = new ArrayList<ArrayList<ArrayList<String>>>();
		parameters_1d = new ArrayList<ArrayList<String>>();
		parameters_0d = new ArrayList<String>();
		parameter_names_2d = new ArrayList<String>();
		parameter_names_1d = new ArrayList<String>();
		parameter_names_0d = new ArrayList<String>();
		comments = new ArrayList<String>();
	}
	
	public void clear(){
		parameters_2d.clear();
		parameters_1d.clear();
		parameters_0d.clear();
		parameter_names_2d.clear();
		parameter_names_1d.clear();
		parameter_names_0d.clear();
		comments.clear();
	}
	
	public void loadFile(String filename){
		try{
			clear();
			BufferedReader br = new BufferedReader(new FileReader(filename));
			int line_number = 0;
			String line = br.readLine();
			while(line != null){
				line_number++;
				if(line.length() > 0){
					if(line.charAt(0) == '#'){
						comments.add(line);
					}
					else if(line.charAt(0) != '#'){
						String name = StrOps.trimString(StrOps.getDilineatedSubstring(line, "=", 0, false));
						if(name.equals("")){
							System.out.println("default_loader.Loader:: Invalid Default Parameter");
							System.exit(-1);
						}
		
						for(int ii = 0; ii < parameter_names_0d.size(); ii++){
							if(parameter_names_0d.get(ii).equals(name)){
								System.out.println("default_loader.Loader:: Duplicate Parameter");
								System.exit(-1);
							}
						}
						for(int ii = 0; ii < parameter_names_1d.size(); ii++){
							if(parameter_names_1d.get(ii).equals(name)){
								System.out.println("default_loader.Loader:: Duplicate Parameter");
								System.exit(-1);
							}
						}
						for(int ii = 0; ii < parameter_names_2d.size(); ii++){
							if(parameter_names_2d.get(ii).equals(name)){
								System.out.println("default_loader.Loader:: Duplicate Parameter");
								System.exit(-1);
							}
						}
						
						System.out.println("Loading parameter " + name);
						
						int index = StrOps.findPattern(line, "=");
						line = StrOps.trimString(line.substring(index+1));
						if(line.length() == 0){
							System.out.println("default_loader.Loader:: Invalid value for parameter " + name);
							System.exit(-1);
						}
						if(StrOps.findPattern(line, ";") != -1){
							if(line.charAt(0) != '{'){
								System.out.println("default_loader.Loader:: Invalid format for parameter " + name);
								System.exit(-1);
							}
							index = 1;
							ArrayList<ArrayList<String>> par_matrix = new ArrayList<ArrayList<String>>();
							par_matrix.add(new ArrayList<String>());
							while(line.charAt(index) != '}'){
								String cur_string = "";
								while(index < line.length() && line.charAt(index) != ',' && line.charAt(index) != ';' && line.charAt(index) != '}' && line.charAt(index) != '\"'){
									if(!(line.charAt(index) == ' ' && cur_string.equals("")))
										cur_string += line.charAt(index);
									index++;
								}
								if(line.charAt(index) == ','){
									if(cur_string.length() == 0){
										System.out.println("default_loader.Loader:: Invalid value for parameter " + name);
										System.exit(-1);
									}
									par_matrix.get(par_matrix.size()-1).add(StrOps.trimString(cur_string));
									cur_string = "";
									index++;
								}
								else if(line.charAt(index) == ';'){
									if(cur_string.length() == 0){
										System.out.println("default_loader.Loader:: Invalid value for parameter " + name);
										System.exit(-1);
									}
									par_matrix.get(par_matrix.size()-1).add(StrOps.trimString(cur_string));
									par_matrix.add(new ArrayList<String>());
									cur_string = "";
									index++;									
								}
								else if(line.charAt(index) == '\"'){
									if(cur_string.length() != 0){
										System.out.println("default_loader.Loader:: Invalid use of quotes " + name);
										System.exit(-1);
									}
									index++;
									while(index < line.length() && line.charAt(index) != '\"'){
										if(line.charAt(index) != '\\')
											cur_string += line.charAt(index);
										else{
											if(index + 1 < line.length() && line.charAt(index) == '\"'){
												cur_string += '\"';
												index++;
											} else
												cur_string += '\\';
										}
										index++;
									}
									index++;
									while(index < line.length() && line.charAt(index) == ' ')
										index++;
									if(index == line.length() || (line.charAt(index) != ',' && line.charAt(index) != ';' && line.charAt(index) != '}')){
										System.out.println("default_loader.Loader:: Invalid value for parameter " + name);
										System.exit(-1);
									} else{
										if(line.charAt(index) != ','){
											if(cur_string.length() == 0){
												System.out.println("default_loader.Loader:: Invalid value for parameter " + name);
												System.exit(-1);
											}
											par_matrix.get(par_matrix.size()-1).add(StrOps.trimString(cur_string));
											cur_string = "";
											index++;
										}
										else if(line.charAt(index) != ';'){
											if(cur_string.length() == 0){
												System.out.println("default_loader.Loader:: Invalid value for parameter " + name);
												System.exit(-1);
											}
											par_matrix.get(par_matrix.size()-1).add(StrOps.trimString(cur_string));
											par_matrix.add(new ArrayList<String>());
											cur_string = "";
											index++;	
										}
									}
								}
								else if(line.charAt(index) == '}'){
									if(cur_string.length() == 0){
										System.out.println("default_loader.Loader:: Invalid value for parameter " + name);
										System.exit(-1);
									}
									par_matrix.get(par_matrix.size()-1).add(StrOps.trimString(cur_string));
									cur_string = "";
								}
							}
							parameter_names_2d.add(name);
							parameters_2d.add(par_matrix);
						} else if(StrOps.findPattern(line, ",") != -1 || StrOps.findPattern(line, "{") != -1){
							if(line.charAt(0) != '{'){
								System.out.println("default_loader.Loader:: Invalid format for parameter " + name);
								System.exit(-1);
							}
							index = 1;
							ArrayList<String> par_array = new ArrayList<String>();
							while(line.charAt(index) != '}'){
								String cur_string = "";
								while(index < line.length() && line.charAt(index) != ',' && line.charAt(index) != '}' && line.charAt(index) != '\"'){
									if(!(line.charAt(index) == ' ' && cur_string.equals("")))
										cur_string += line.charAt(index);
									index++;
								}
								if(line.charAt(index) == ','){
									if(cur_string.length() == 0){
										System.out.println("default_loader.Loader:: Invalid value for parameter " + name);
										System.exit(-1);
									}
									par_array.add(StrOps.trimString(cur_string));
									cur_string = "";
									index++;
								}
								else if(line.charAt(index) == '\"'){
									if(cur_string.length() != 0){
										System.out.println("default_loader.Loader:: Invalid use of quotes " + name);
										System.exit(-1);
									}
									index++;
									while(index < line.length() && line.charAt(index) != '\"'){
										if(line.charAt(index) != '\\')
											cur_string += line.charAt(index);
										else{
											if(index + 1 < line.length() && line.charAt(index) == '\"'){
												cur_string += '\"';
												index++;
											} else
												cur_string += '\\';
										}
										index++;
									}
									index++;
									while(index < line.length() && line.charAt(index) == ' ')
										index++;
									if(index == line.length() || (line.charAt(index) != ',' && line.charAt(index) != ';' && line.charAt(index) != '}')){
										System.out.println("default_loader.Loader:: Invalid value for parameter " + name);
										System.exit(-1);
									} else{
										if(line.charAt(index) != ','){
											if(cur_string.length() == 0){
												System.out.println("default_loader.Loader:: Invalid value for parameter " + name);
												System.exit(-1);
											}
											par_array.add(StrOps.trimString(cur_string));
											cur_string = "";
											index++;
										}
										else if(line.charAt(index) != ';'){
											if(cur_string.length() == 0){
												System.out.println("default_loader.Loader:: Invalid value for parameter " + name);
												System.exit(-1);
											}
											par_array.add(StrOps.trimString(cur_string));
											cur_string = "";
											index++;	
										}
									}
								}
								else if(line.charAt(index) == '}'){
									if(cur_string.length() == 0){
										System.out.println("default_loader.Loader:: Invalid value for parameter " + name);
										System.exit(-1);
									}
									par_array.add(StrOps.trimString(cur_string));
									cur_string = "";
								}
							}
							parameter_names_1d.add(name);
							parameters_1d.add(par_array);						
						} else{
							String cur_str = "";
							index = 0;
							while(index < line.length())
								cur_str += line.charAt(index++);
							parameter_names_0d.add(name);
							parameters_0d.add(StrOps.trimString(cur_str));
						}
					}
				}
				line = br.readLine();
			}
			br.close();
		} catch(IOException e){
			System.out.println("default_loader.Loader:: Cannot open file " + filename);
			System.exit(-1);
		}
	}
	
	public void saveParameters(String filename, ArrayList<String> n_0d, ArrayList<String> n_1d, ArrayList<String> n_2d, ArrayList<String> p_0d, ArrayList<ArrayList<String>> p_1d, ArrayList<ArrayList<ArrayList<String>>> p_2d){
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename, false));
			for(int ii = 0; ii < n_0d.size(); ii++){
				bw.write(n_0d.get(ii) + " = " + p_0d.get(ii) + "\r\n");
			}
			for(int ii = 0; ii < n_1d.size(); ii++){
				bw.write(n_1d.get(ii) + " = {");
				for(int jj = 0; jj < p_1d.get(ii).size(); jj++){
					bw.write(p_1d.get(ii).get(jj));
					if(jj < p_1d.get(ii).size()-1)
						bw.write(", ");
				}
				bw.write("}\r\n");
			}
			for(int ii = 0; ii < n_2d.size(); ii++){
				bw.write(n_2d.get(ii) + " = {");
				for(int jj = 0; jj < p_2d.get(ii).size(); jj++){
					for(int kk = 0; kk < p_2d.get(ii).get(jj).size(); kk++){
						bw.write(p_2d.get(ii).get(jj).get(kk));
						if(kk < p_2d.get(ii).get(jj).size()-1)
							bw.write(", ");
					}
					if(jj < p_2d.get(ii).size()-1)
						bw.write("; ");
				}
				bw.write("}\r\n");
			}			
			bw.close();
		} catch(IOException e){
			System.out.println("default_loader.Loader:: Cannot open " + filename);
			System.exit(-1);
		}
	}
	
	public void print(){
		System.out.println("");
		for(int ii = 0; ii < parameter_names_0d.size(); ii++){
			System.out.println(parameter_names_0d.get(ii) + "=");
			System.out.println("\t" + parameters_0d.get(ii)+"\n");
		}
		for(int ii = 0; ii < parameter_names_1d.size(); ii++){
			System.out.println(parameter_names_1d.get(ii) + "=");
			System.out.print("\t");
			for(int jj = 0; jj < parameters_1d.get(ii).size(); jj++){
				System.out.print(parameters_1d.get(ii).get(jj));
				if(jj < parameters_1d.get(ii).size()-1)
					System.out.print(", ");
			}
			System.out.println("\n");
		}
		for(int ii = 0; ii < parameter_names_2d.size(); ii++){
			System.out.println(parameter_names_2d.get(ii) + "=");
			
			for(int jj = 0; jj < parameters_2d.get(ii).size(); jj++){
				System.out.print("\t");
				for(int kk = 0; kk < parameters_2d.get(ii).get(jj).size(); kk++){
					System.out.print(parameters_2d.get(ii).get(jj).get(kk));
					if(kk < parameters_2d.get(ii).get(jj).size()-1)
						System.out.print(", ");
				}
				if(jj < parameters_2d.get(ii).size()-1)
					System.out.println(";");
			}
			System.out.println("\n");
		}
		
		System.out.println("");
	}
	
	public ArrayList<ArrayList<String>> getNames(){
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		list.add(parameter_names_0d);
		list.add(parameter_names_1d);
		list.add(parameter_names_2d);
		return list;
	}
	
	public ArrayList<ArrayList<String>> get2dParameter(String name){
		for(int ii = 0; ii < parameter_names_2d.size(); ii++){
			if(parameter_names_2d.get(ii).equals(name))
				return parameters_2d.get(ii);
		}
		System.out.println("default_loader.Loader:: Error! parameter not found!");
		System.exit(-1);
		return null; 
	}
	
	public ArrayList<String> get1dParameter(String name){
		for(int ii = 0; ii < parameter_names_1d.size(); ii++){
			if(parameter_names_1d.get(ii).equals(name))
				return parameters_1d.get(ii);
		}
		System.out.println("default_loader.Loader:: Error! parameter not found!");
		System.exit(-1);
		return null; 
	}
	
	public String get0dParameter(String name){
		for(int ii = 0; ii < parameter_names_0d.size(); ii++){
			if(parameter_names_0d.get(ii).equals(name))
				return parameters_0d.get(ii);
		}
		System.out.println("default_loader.Loader:: Error! parameter not found!");
		System.exit(-1);
		return null; 
	}
}
