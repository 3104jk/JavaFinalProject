package edu.handong.csee.utils;

import java.util.ArrayList;

import org.apache.commons.csv.CSVPrinter;

import java.io.*;

import org.apache.commons.csv.CSVFormat;


public class Utils {


	public static void writeAFile(ArrayList<String> lines, String targetFileName) {

		try (CSVPrinter printer = new CSVPrinter(new FileWriter(targetFileName), CSVFormat.DEFAULT)) {
			for(String line:lines) {


				String[] a = line.split("///");

				if(a.length < 5) {
					String aline2 = "///";
					aline2 +=line;
					String alines2 [] = aline2.split("///");
					printer.printRecord(alines2);
				}
				else {
					printer.printRecord(a);
				}

			}
			System.out.println("Finish!");
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}
}
