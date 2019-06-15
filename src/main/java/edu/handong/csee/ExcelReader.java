package edu.handong.csee;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

//import org.apache.poi.hssf.usermodel.HSSFCell;
//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.xssf.usermodel.XSSFCell;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.apache.poi.ss.usermodel.CellType;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelReader {
	
	public ArrayList<String> getData(String path) {
		ArrayList<String> values = new ArrayList<String>();
		
		System.out.println(path);
		
		try (InputStream inp = new FileInputStream(path)) {
		    //InputStream inp = new FileInputStream("workbook.xlsx");
		    
		        Workbook wb = WorkbookFactory.create(inp);
//		        Sheet sheet = wb.getSheetAt(0);

		    } catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return values;
	}
	
	public ArrayList<String> getData(InputStream is) {
		ArrayList<String> values = new ArrayList<String>();		
		try (InputStream inp = is) {
		    //InputStream inp = new FileInputStream("workbook.xlsx");
		    	//int count = 1;
		        Workbook wb = WorkbookFactory.create(inp);
		        Sheet sheet = wb.getSheetAt(0);

		        Iterator<Row> i = sheet.iterator();
		        
		        while(i.hasNext()) {
		        	
		        	String value = "";
		        	String last = "";
		        	
		        	Row currRow = i.next();
		        	Iterator<Cell> c = currRow.iterator();
		        	
		        	while(c.hasNext()) {
		        		
		        		Cell currCell = c.next();
		        		switch (currCell.getCellType()){
                        case FORMULA:
                            value = currCell.getCellFormula();
                            break;
                        case NUMERIC:
                            value = currCell.getNumericCellValue()+"";
                            break;
                        case STRING:
                        	value = currCell.getStringCellValue()+"";
                            break;
                        case BLANK:
                        	value = "";
                            break;
                        case ERROR:
                            value = currCell.getErrorCellValue()+"";
                            break;
                        default:
                            value = new String();
                            break;
                        }
		        			
		        		last += value + "///";
		        	}
		        	
		        	values.add(last);

		        }
        
		        
		        
		    } catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return values;
	}
}
