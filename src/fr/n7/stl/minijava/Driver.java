package fr.n7.stl.minijava;

import java.util.Scanner;

class Driver {

	public static void main(String[] args) throws Exception {
		Parser parser = null;
		if (args.length == 0) {
			Scanner scanner = new Scanner(System.in);
			System.out.print("Fichier à compiler ? : ");
			String s = scanner.next();
			if (s.equals("tests_bloc")) {
				for (int i = 0; i <= 103; i++) {
					String i_string = String.valueOf(i);
					if (i < 10) i_string = "0"+i_string;
					parser = new Parser("tests_bloc/test" + i_string + ".bloc");
					try {
						parser.parse();
					} catch (Throwable e) {
						e.printStackTrace();
					}
					System.in.read();
				}
			} else {
				parser = new Parser( s );
				parser.parse();
			}
			scanner.close();
		} else {
			if (args[0].equals("tests_bloc")) {
				for (int i = 0; i <= 103; i++) {
					String i_string = String.valueOf(i);
					if (i < 10) i_string = "0"+i_string;
					parser = new Parser("tests_bloc/test" + i_string + ".bloc");
					try {
						parser.parse();
					} catch (Throwable e) {
						e.printStackTrace();
					}
					System.in.read();
				}
			}
			for (String name : args) {
				parser = new Parser( name );
				parser.parse();
			}
		}
	}
	
}