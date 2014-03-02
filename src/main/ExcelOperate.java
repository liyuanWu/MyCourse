package main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExcelOperate {

	static int startRow=0;
	static HSSFWorkbook workbook;
	static HSSFSheet sheet;
    
	public static void writeKlassXls(String name,String[][] data){
		
		if(startRow==0){
			// 声明一个工作薄
	        workbook = new HSSFWorkbook();
	        // 生成一个表格
	        sheet = workbook.createSheet("KlassTable");
	        // 设置表格默认列宽度为15个字节
	        sheet.setDefaultColumnWidth((short) 15);
	        
	     // 产生表格标题行
	        HSSFRow row = sheet.createRow(0);
	        HSSFCell cell = row.createCell(1);
	        HSSFRichTextString text = new HSSFRichTextString("KlassTable");
	        cell.setCellValue(text);
	            
	         row = sheet.createRow(1);
	        for(int i=1;i<=5;i++){
	        	 cell = row.createCell(1+(i-1)*6);
	             text = new HSSFRichTextString("星期"+i);
	            cell.setCellValue(text);
	        }
	        
	        
	         row = sheet.createRow(2);
	        for(int i=1;i<=5;i++){
	        	for(int j=1;j<=6;j++){
	                 cell = row.createCell((i-1)*6+j);
	                 text = new HSSFRichTextString(String.valueOf(j));
	                cell.setCellValue(text);
	        	}
	        }
		}
       
        
		BufferedOutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(new File("klass13.xls")));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		HSSFRow row = sheet.createRow(startRow+3);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue(name);
		for(int i=1;i<=5;i++){
        	for(int j=1;j<=6;j++){
     	        cell = row.createCell((i-1)*6+j);
     	      HSSFRichTextString richString = new HSSFRichTextString(
     	    		 data[i-1][j-1]);
              HSSFFont font3 = workbook.createFont();
              font3.setColor(HSSFColor.BLUE.index);
              richString.applyFont(font3);
              cell.setCellValue(richString);
        	}
        }
            
            
        
        startRow+=1;
        try {
            workbook.write(out);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	static int kls_index =0;
	public static void writeKlassesXls(String name,String[][] data){
		
		if(kls_index==0){
			// 声明一个工作薄
	        workbook = new HSSFWorkbook();
	        // 生成一个表格
	        sheet = workbook.createSheet("KlassTable");
	        // 设置表格默认列宽度为15个字节
	        sheet.setDefaultColumnWidth((short) 15);
		
	     // 产生表格标题行
	        HSSFRow row = sheet.createRow(0);
	        HSSFCell cell = row.createCell(1);
	        HSSFRichTextString text = new HSSFRichTextString("KlassTable");
	        cell.setCellValue(text);
		}
		HSSFRow row;
        HSSFCell cell;
        HSSFRichTextString text;
        
    	row = sheet.createRow(kls_index + 1);
    	cell = row.createCell(0);
        text = new HSSFRichTextString(name);
        cell.setCellValue(text);
        
    	row = sheet.createRow(kls_index + 2);
        for(int i=1;i<=6;i++){
        	 cell = row.createCell(i);
             text = new HSSFRichTextString("第"+ i +"节");
             cell.setCellValue(text);
        }

       
		BufferedOutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(new File("klasses13.xls")));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for(int i=1;i<=5;i++){
			row = sheet.createRow(kls_index + 2 +i);
			cell = row.createCell(0);
            text = new HSSFRichTextString("星期"+i);
            cell.setCellValue(text);
        	for(int j=1;j<=6;j++){
     	        cell = row.createCell(j);
     	      HSSFRichTextString richString = new HSSFRichTextString(
     	    		 data[i-1][j-1]);
              HSSFFont font3 = workbook.createFont();
              font3.setColor(HSSFColor.BLUE.index);
              richString.applyFont(font3);
              cell.setCellValue(richString);
        	}
        }
            
            
        
		kls_index+=7;
        try {
            workbook.write(out);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	static int tea_index=0;
		public static void writeTeachersXls(String name,String[][] data){
		
		if(tea_index==0){
			// 声明一个工作薄
	        workbook = new HSSFWorkbook();
	        // 生成一个表格
	        sheet = workbook.createSheet("TeacherTable");
	        // 设置表格默认列宽度为15个字节
	        sheet.setDefaultColumnWidth((short) 15);
		
	     // 产生表格标题行
	        HSSFRow row = sheet.createRow(0);
	        HSSFCell cell = row.createCell(1);
	        HSSFRichTextString text = new HSSFRichTextString("TeacherTable");
	        cell.setCellValue(text);
		}
		HSSFRow row;
        HSSFCell cell;
        HSSFRichTextString text;
        
    	row = sheet.createRow(tea_index + 1);
    	cell = row.createCell(0);
        text = new HSSFRichTextString(name);
        cell.setCellValue(text);
        
    	row = sheet.createRow(tea_index + 2);
        for(int i=1;i<=6;i++){
        	 cell = row.createCell(i);
             text = new HSSFRichTextString("第"+ i +"节");
             cell.setCellValue(text);
        }

       
		BufferedOutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(new File("Teachers13.xls")));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for(int i=1;i<=5;i++){
			row = sheet.createRow(tea_index + 2 +i);
			cell = row.createCell(0);
            text = new HSSFRichTextString("星期"+i);
            cell.setCellValue(text);
        	for(int j=1;j<=6;j++){
     	        cell = row.createCell(j);
     	      HSSFRichTextString richString = new HSSFRichTextString(
     	    		 data[i-1][j-1]);
              HSSFFont font3 = workbook.createFont();
              font3.setColor(HSSFColor.BLUE.index);
              richString.applyFont(font3);
              cell.setCellValue(richString);
        	}
        }
            
            
        
		tea_index+=7;
        try {
            workbook.write(out);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static int startRowTeacher=0;
	public static void writeTeacherXls(String name,String[][] data){
		
		if(startRowTeacher==0){
			// 声明一个工作薄
	        workbook = new HSSFWorkbook();
	        // 生成一个表格
	        sheet = workbook.createSheet("TeacherTable");
	        // 设置表格默认列宽度为15个字节
	        sheet.setDefaultColumnWidth((short) 15);
	        
	     // 产生表格标题行
	        HSSFRow row = sheet.createRow(0);
	        HSSFCell cell = row.createCell(1);
	        HSSFRichTextString text = new HSSFRichTextString("TeacherTable");
	        cell.setCellValue(text);
	            
	         row = sheet.createRow(1);
	        for(int i=1;i<=5;i++){
	        	 cell = row.createCell(1+(i-1)*6);
	             text = new HSSFRichTextString("星期"+i);
	            cell.setCellValue(text);
	        }
	        
	        
	         row = sheet.createRow(2);
	        for(int i=1;i<=5;i++){
	        	for(int j=1;j<=6;j++){
	                 cell = row.createCell((i-1)*6+j);
	                 text = new HSSFRichTextString(String.valueOf(j));
	                cell.setCellValue(text);
	        	}
	        }
		}
       
        
		BufferedOutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(new File("teacher13.xls")));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		HSSFRow row = sheet.createRow(startRowTeacher+3);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue(name);
		for(int i=1;i<=5;i++){
        	for(int j=1;j<=6;j++){
     	        cell = row.createCell((i-1)*6+j);
     	      HSSFRichTextString richString = new HSSFRichTextString(
     	    		 data[i-1][j-1]);
              HSSFFont font3 = workbook.createFont();
              font3.setColor(HSSFColor.BLUE.index);
              richString.applyFont(font3);
              cell.setCellValue(richString);
        	}
        }
            
            
        
		startRowTeacher+=1;
        try {
            workbook.write(out);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String[][] readXls(String args){

		File file = new File(args);
		String[][] result = null;
		try{
			result = getData(file, 0);
	
			int rowLength = result.length;
	
			for (int i = 0; i < rowLength; i++) {
	
				for (int j = 0; j < result[i].length; j++) {
	
					System.out.print(result[i][j] + "\t\t");
	
				}
	
				System.out.println();
	
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		return result;
	}

	/**
	 * 
	 * 读取Excel的内容，第一维数组存储的是一行中格列的值，二维数组存储的是多少个行
	 * 
	 * @param file
	 *            读取数据的源Excel
	 * 
	 * @param ignoreRows
	 *            读取数据忽略的行数，比喻行头不需要读入 忽略的行数为1
	 * 
	 * @return 读出的Excel中数据的内容
	 * 
	 * @throws FileNotFoundException
	 * 
	 * @throws IOException
	 */

	public static String[][] getData(File file, int ignoreRows)

	throws FileNotFoundException, IOException {

		List<String[]> result = new ArrayList<String[]>();

		int rowSize = 0;

		BufferedInputStream in = new BufferedInputStream(new FileInputStream(

		file));

		// 打开HSSFWorkbook

		POIFSFileSystem fs = new POIFSFileSystem(in);

		HSSFWorkbook wb = new HSSFWorkbook(fs);

		HSSFCell cell = null;


			HSSFSheet st = wb.getSheetAt(0);
			;// wb.getSheetAt(sheetIndex);

			// 第一行为标题，不取

			for (int rowIndex = ignoreRows; rowIndex <= st.getLastRowNum(); rowIndex++) {

				HSSFRow row = st.getRow(rowIndex);

				if (row == null) {

					continue;

				}

				int tempRowSize = 33;// row.getLastCellNum() + 1;

				if (tempRowSize > rowSize) {

					rowSize = tempRowSize;

				}

				String[] values = new String[rowSize];

				Arrays.fill(values, "");

				boolean hasValue = false;

				for (short columnIndex = 0; columnIndex <= 32; columnIndex++) {

					String value = "";

					// HSSFComment comment = st.getCellComment(rowIndex,
					// columnIndex);
					cell = row.getCell(columnIndex);

					if (cell != null) {

						// 注意：一定要设成这个，否则可能会出现乱码
						// if(comment!=null)
						// value = comment.getString().getString();
						switch (cell.getCellType()) {

						case HSSFCell.CELL_TYPE_STRING:

							value = cell.getStringCellValue();

							break;

						case HSSFCell.CELL_TYPE_NUMERIC:

							if (HSSFDateUtil.isCellDateFormatted(cell)) {

								Date date = cell.getDateCellValue();

								if (date != null) {

									value = new SimpleDateFormat("yyyy-MM-dd")

									.format(date);

								} else {

									value = "";

								}

							} else {

								value = new DecimalFormat("0").format(cell

								.getNumericCellValue());

							}

							break;

						case HSSFCell.CELL_TYPE_FORMULA:

							// 导入时如果为公式生成的数据则无值

							if (!Double.toString(cell.getNumericCellValue())
									.equals("")) {

								value = Double.toString(cell
										.getNumericCellValue());

							} else {

								value = Double.toString(cell
										.getNumericCellValue()) + "";

							}

							break;

						case HSSFCell.CELL_TYPE_BLANK:
							value = cell.getStringCellValue();

							break;

						case HSSFCell.CELL_TYPE_ERROR:

							value = "";

							break;

						case HSSFCell.CELL_TYPE_BOOLEAN:

							value = (cell.getBooleanCellValue() == true ? "Y"

							: "N");

							break;

						default:

							value = "";

						}

					}

					// if (columnIndex == 0 && value.trim().equals("")) {
					//
					// break;
					//
					// }

					values[columnIndex] = rightTrim(value);

					hasValue = true;

				}

				if (hasValue) {

					result.add(values);

				}

			}




		in.close();

		String[][] returnArray = new String[result.size()+1][];

		for (int i = 0; i < result.size(); i++) {

			returnArray[i] = (String[]) result.get(i);

		}
		
		int i = 0;
		CellRangeAddress rsa;
		while ((rsa = st.getMergedRegion(i++)) != null) {
			for(int j=rsa.getFirstRow();j<rsa.getLastRow()+1&&j<result.size();j++){
				for(int k=rsa.getFirstColumn();k<rsa.getLastColumn()+1&&k<rowSize;k++){
					returnArray[j][k] = returnArray[rsa.getFirstRow()][rsa.getFirstColumn()];
				}
			}
		}

		return returnArray;

	}

	/**
	 * 
	 * 去掉字符串右边的空格
	 * 
	 * @param str
	 *            要处理的字符串
	 * 
	 * @return 处理后的字符串
	 */

	public static String rightTrim(String str) {

		if (str == null) {

			return "";

		}

		int length = str.length();

		for (int i = length - 1; i >= 0; i--) {

			if (str.charAt(i) != 0x20) {

				break;

			}

			length--;

		}

		return str.substring(0, length);

	}

	public static void reset() {
		kls_index=0;
		tea_index=0;
		startRow=0;
		startRowTeacher=0;
		
	}

}