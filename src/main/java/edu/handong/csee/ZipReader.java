package edu.handong.csee;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

import edu.handong.csee.utils.NotEnoughArgumentException;
//import org.apache.poi.ss.usermodel.CellType;
import edu.handong.csee.utils.Utils;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;


public class ZipReader extends Thread {
	private String[] argu;
	private File[] resultList;

	private String input; //-i
	private String output; // -o
	private String output2; // -another output
	boolean help;	//-h	

	ArrayList<String> save = new ArrayList<String>();
	ArrayList<String> save2 = new ArrayList<String>();


	public void setArg(String[] args) {
		argu = args;
	}


	public static void main(String[] args) {
		int numThreads = 5;
		Thread[] t = new Thread[numThreads];

		for(int i=0;i<numThreads;i++) {
			t[i] = new Thread(new ZipReader());
			t[i].start();
		}

		ZipReader zipReader = new ZipReader();
		zipReader.run(args);
	}

	class Files <T>{
		private T t;
		
		public void set(T t) {
			this.t = t;
		}
		
		public T get() {
			return t;
		}
	}

	private void run(String[] args) {
		try {	
			// when there are not enough arguments from CLI, it throws the NotEnoughArgmentException which must be defined by you.
			if(args.length<1)
				throw new NotEnoughArgumentException();	
			Options options = createOptions();

			if(parseOptions(options, args)){
				if (help){
					printHelp(options);
					return;
				}
				else {
					
					Files<String> myFile = new Files();
					
					myFile.set(input);
					
					getZipFileList(myFile.get());
					
					//getZipFileList(input);

					for(File file: resultList) {
						if(file.getName().contains("zip")){
							save.add(file.getName());
							save2.add(file.getName());
							readFileInZip(input + file.getName());
						}

					}
					Utils.writeAFile(save, output);
					Utils.writeAFile(save2,output2);

				}
			}
		} catch (NotEnoughArgumentException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}




	private boolean parseOptions(Options options, String[] args) {
		CommandLineParser parser = new DefaultParser();

		try {

			CommandLine cmd = parser.parse(options, args);

			input = cmd.getOptionValue("i");
			output = cmd.getOptionValue("o");
			output2 = cmd.getOptionValue("a");
			help = cmd.hasOption("h");


		} catch (Exception e) {
			printHelp(options);
			return false;
		}
		return true;

	}


	private Options createOptions() {
		Options options = new Options();

		// add options by using OptionBuilder
		options.addOption(Option.builder("i").longOpt("input")
				.desc("Set an input file path")
				.hasArg()
				.argName("input path")
				.required()
				.build());

		options.addOption(Option.builder("o").longOpt("output")
				.desc("Set an output file path")
				.hasArg()
				.argName("output path")
				.required()
				.build());

		options.addOption(Option.builder("a").longOpt("output2")
				.desc("Set an another output file path")
				.hasArg()
				.argName("output path")
				.required()
				.build());

		// add options by using OptionBuilder
		options.addOption(Option.builder("h").longOpt("help")
				.desc("Help")
				.build());

		return options;

	}

	private void printHelp(Options options) {
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
		String header = "JavaFinalProject";
		String footer ="";
		formatter.printHelp("Java Final Project", header, options, footer, true);
	}

	public void readFileInZip(String path) {
		ZipFile zipFile;

		int count =0;
		String result = output ;
		String result2 = output2;

		try {
			zipFile = new ZipFile(path);
			Enumeration<? extends ZipArchiveEntry> entries = zipFile.getEntries();

			while(entries.hasMoreElements()){
				ZipArchiveEntry entry = entries.nextElement();
				InputStream stream = zipFile.getInputStream(entry);

				ExcelReader myReader = new ExcelReader();

				for(String value:myReader.getData(stream)) {
					if(count ==0)
						save.add(value);
					else if(count ==1)
						save2.add(value);	

				}
	
				count++;
				save.add("");
				save2.add("");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public File[] getZipFileList(String path) {
		File file = new File(path);
		resultList = file.listFiles();

		return resultList;
	}
	
	

}
