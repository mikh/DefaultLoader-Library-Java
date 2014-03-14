package debug;

import java.util.ArrayList;

import default_loader.Loader;

public class Test {
	public static void main(String[] args){
		Loader ld = new Loader();
		ld.loadFile("test.txt");
		ld.print();
		ArrayList<ArrayList<String>> names = ld.getNames();
		ArrayList<String> p_0d = new ArrayList<String>();
		ArrayList<ArrayList<String>> p_1d = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<ArrayList<String>>> p_2d = new ArrayList<ArrayList<ArrayList<String>>>();
		for(int ii = 0; ii < names.get(0).size(); ii++)
			p_0d.add(ld.get0dParameter(names.get(0).get(ii)));
		for(int ii = 0; ii < names.get(1).size(); ii++)
			p_1d.add(ld.get1dParameter(names.get(1).get(ii)));
		for(int ii = 0; ii < names.get(2).size(); ii++)
			p_2d.add(ld.get2dParameter(names.get(2).get(ii)));
		ld.saveParameters("test_new.txt", names.get(0), names.get(1), names.get(2), p_0d, p_1d, p_2d);
	}
}
