package com.mip.application.utils;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import com.mip.application.constants.ReportKeys;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
import com.mip.framework.utils.MIPFont;

public class ReportUtil {

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			ReportUtil.class);


	/**
	 * This method writes the headings to the excel workbook
	 * 
	 * @param workbook
	 *            Excel workbook object
	 * 
	 * @param sheet
	 *            Excel worksheet in which headings will be written
	 * 
	 * @param headings
	 *            String array containing heading data to be written
	 * 
	 * @param headerRowNo
	 *            Header row number where headings will be written
	 */
	public static void writeHeadings(HSSFWorkbook workbook, HSSFSheet sheet,
			String[] headings, int headerRowNo) {
		Object[] params = { headings, headerRowNo };
		logger.entering("writeHeadings", params);

		// Style for headings
		HSSFCellStyle headerFormat = workbook.createCellStyle();

		headerFormat.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		headerFormat.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		headerFormat.setFillBackgroundColor(HSSFColor.BLACK.index);
		headerFormat.setBorderTop(HSSFCellStyle.BORDER_THIN);
		headerFormat.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		headerFormat.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		headerFormat.setBorderRight(HSSFCellStyle.BORDER_THIN);
		headerFormat.setWrapText(true);
		headerFormat.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		HSSFFont font = workbook.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeight((short) 220);
		headerFormat.setFont(font);

		HSSFRow row = sheet.createRow(headerRowNo);

		// Writes the headings
		for (int i = 0; i < headings.length; i++) {

			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(headerFormat);
			cell.setCellValue(headings[i]);

		}
		logger.exiting("writeHeadings");
	}

	/**
	 * This method returns the style for user total row
	 * 
	 * @param workbook
	 *            Excel workbook object for setting the style
	 * 
	 * @return HSSFCellStyle cell style object
	 */
	public static HSSFCellStyle getTotalRowCellStyle(HSSFWorkbook workbook) {
		logger.entering("getTotalRowCellStyle");

		HSSFCellStyle totalRowFormat = workbook.createCellStyle();
		totalRowFormat.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		totalRowFormat.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		totalRowFormat.setFillBackgroundColor(HSSFColor.BLACK.index);

		totalRowFormat.setBorderTop(HSSFCellStyle.BORDER_THIN);
		totalRowFormat.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		totalRowFormat.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		totalRowFormat.setBorderRight(HSSFCellStyle.BORDER_THIN);
		totalRowFormat.setWrapText(true);

		HSSFFont font = workbook.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeight((short) 180);
		totalRowFormat.setFont(font);

		logger.exiting("getTotalRowCellStyle");
		return totalRowFormat;
	}

	/**
	 * This method returns the style for user total row with text aligned at
	 * center
	 * 
	 * @param workbook
	 *            Excel workbook object for setting the style
	 * 
	 * @return HSSFCellStyle cell style object
	 */
	public static HSSFCellStyle getTotalRowCellStyleAlignCenter(
			HSSFWorkbook workbook) {
		logger.entering("getTotalRowCellStyleAlignCenter");

		HSSFCellStyle totalRowFormat = workbook.createCellStyle();
		totalRowFormat.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		totalRowFormat.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		totalRowFormat.setFillBackgroundColor(HSSFColor.BLACK.index);

		totalRowFormat.setBorderTop(HSSFCellStyle.BORDER_THIN);
		totalRowFormat.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		totalRowFormat.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		totalRowFormat.setBorderRight(HSSFCellStyle.BORDER_THIN);
		totalRowFormat.setWrapText(true);
		totalRowFormat.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		HSSFFont font = workbook.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeight((short) 180);
		totalRowFormat.setFont(font);

		logger.exiting("getTotalRowCellStyleAlignCenter");
		return totalRowFormat;
	}

	/**
	 * This method returns the style for a data row. The content text will be
	 * bold.
	 * 
	 * @param workbook
	 *            Excel workbook object for setting the style
	 * 
	 * @return HSSFCellStyle cell style object
	 */
	public static HSSFCellStyle getDataRowCellStyleBold(HSSFWorkbook workbook) {
		logger.entering("getDataRowCellStyleBold");

		HSSFCellStyle totalRowFormat = workbook.createCellStyle();
		totalRowFormat.setFillForegroundColor(HSSFColor.WHITE.index);
		totalRowFormat.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		totalRowFormat.setFillBackgroundColor(HSSFColor.BLACK.index);

		totalRowFormat.setBorderTop(HSSFCellStyle.BORDER_THIN);
		totalRowFormat.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		totalRowFormat.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		totalRowFormat.setBorderRight(HSSFCellStyle.BORDER_THIN);
		totalRowFormat.setWrapText(true);

		HSSFFont font = workbook.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeight((short) 180);
		totalRowFormat.setFont(font);

		logger.exiting("getDataRowCellStyleBold");
		return totalRowFormat;
	}

	/**
	 * This method returns the style for a header row.
	 * 
	 * @param workbook
	 *            Excel workbook object for setting the style
	 * 
	 * @return HSSFCellStyle cell style object
	 */
	public static HSSFCellStyle getHeaderRowCellStyle(HSSFWorkbook workbook) {
		logger.entering("getHeaderRowCellStyle");
		
		//creating a custom palette for the workbook
	    HSSFPalette palette = workbook.getCustomPalette();

	    //replacing the standard red with freebsd.org red
	    palette.setColorAtIndex(HSSFColor.AQUA.index,
	            (byte) 213,  //RGB red (0-255)
	            (byte) 222,    //RGB green
	            (byte) 237     //RGB blue
	    );
	   
		HSSFCellStyle headerRowFormat = workbook.createCellStyle();
		
		headerRowFormat.setFillForegroundColor(HSSFColor.AQUA.index);
		headerRowFormat.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		headerRowFormat.setFillBackgroundColor(HSSFColor.BLACK.index);

		headerRowFormat.setBorderTop(HSSFCellStyle.BORDER_THIN);
		headerRowFormat.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		headerRowFormat.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		headerRowFormat.setBorderRight(HSSFCellStyle.BORDER_THIN);
		headerRowFormat.setWrapText(true);
		headerRowFormat.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		headerRowFormat.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);

		HSSFFont font = workbook.createFont();
		font.setFontName(MIPFont.FONT_CALIBRI);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeight((short) 220);
		headerRowFormat.setFont(font);

		logger.exiting("getHeaderRowCellStyle");
		return headerRowFormat;
	}
	
	/**
	 * This method returns the style for a data row. The content text will not
	 * be bold.
	 * 
	 * @param workbook
	 *            Excel workbook object for setting the style
	 * 
	 * @return HSSFCellStyle cell style object
	 */
	public static HSSFCellStyle getDataRowCellStyle(HSSFWorkbook workbook) {
		logger.entering("getDataRowCellStyle");

		// Format for data
		HSSFCellStyle dataCellStyle = workbook.createCellStyle();
		dataCellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		dataCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		dataCellStyle.setFillBackgroundColor(HSSFColor.BLACK.index);

		dataCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		dataCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		dataCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		dataCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		dataCellStyle.setWrapText(true);

		HSSFFont fontNoBold = workbook.createFont();
		fontNoBold.setFontName(HSSFFont.FONT_ARIAL);
		fontNoBold.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		fontNoBold.setFontHeight((short) 180);
		dataCellStyle.setFont(fontNoBold);

		logger.exiting("getDataRowCellStyle");
		return dataCellStyle;
	}
	
	/**
	 * This method returns the style for a header row.
	 * 
	 * @param workbook
	 * 				Excel workbook object for setting the style.
	 * @param headerColor
	 * 				Index of the color.
	 * 
	 * @return HSSFCellStyle cell style object
	 */
	public static HSSFCellStyle getHeaderRowCellStyle(HSSFWorkbook workbook, short headerColor){
		Object[] params = { headerColor };
		logger.entering("getHeaderRowCellStyle", params);
		
		// Header Cell Format		
		HSSFCellStyle headerFormat = workbook.createCellStyle();
		headerFormat.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		
		headerFormat.setFillForegroundColor(headerColor);
		headerFormat.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		headerFormat.setFillBackgroundColor(HSSFColor.BLACK.index);
		headerFormat.setBorderTop(HSSFCellStyle.BORDER_THIN);
		headerFormat.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		headerFormat.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		headerFormat.setBorderRight(HSSFCellStyle.BORDER_THIN);
		headerFormat.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont headerFont = workbook.createFont();
		headerFont.setFontName(MIPFont.FONT_CALIBRI);
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerFont.setFontHeightInPoints((short) 10);
		headerFormat.setFont(headerFont);
		
		logger.exiting("getHeaderRowCellStyle");
		return headerFormat;		
	}
	
	/**
	 * This method writes the headings to the excel workbook for the 
	 * given style.
	 * 
	 * @param workbook
	 * 			Excel workbook object 
	 * @param sheet
	 * 			Excel worksheet in which headings will be written
	 * @param headings
	 * 			String array containing heading data to be written
	 * @param headerRowNo
	 * 			Header row number where headings will be written
	 * @param headerFormat
	 * 			Style to be applied on header row 
	 */
	public static void writeHeadings(HSSFWorkbook workbook, HSSFSheet sheet,
			String[] headings, int headerRowNo, HSSFCellStyle headerFormat) {
		Object[] params = { headings, headerRowNo, headerFormat };
		logger.entering("writeHeadings", params);

		// Creates row
		HSSFRow row = sheet.createRow(headerRowNo);
		
		// Writes the headings
		for (int i = 0; i < headings.length; i++) {

			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(headerFormat);
			cell.setCellValue(headings[i]);

		}
		logger.exiting("writeHeadings");
	}
	
	/**
	 * This method returns the column name.
	 * 
	 * @param columnIndex
	 * 			Column index
	 * @return
	 * 			Column name
	 */
	public static String getColumnName(int columnIndex) {
        StringBuffer columnName = new StringBuffer();
        int tempcellnum = columnIndex;
        do {
            columnName.insert(0, ReportKeys.A2Z[tempcellnum%26]);
            tempcellnum = (tempcellnum / 26) - 1;
        } while (tempcellnum >= 0);
        return columnName.toString();
    }

	/**
	 * This method will generate the report header for the weekly report of the 
	 * agent performance on per day basis.
	 * @param workbook
	 * @param sheet
	 * @param headings
	 * @param headerRowNo
	 * @param dateRange
	 * 
	 */
	public static void writeHeadings(HSSFWorkbook workbook, HSSFSheet sheet,   
			String[] headings, int headerRowNo, List<String> dateRange) {
		

		Object[] params = { headings, headerRowNo };
		logger.entering("writeHeadings", params);

		// Style for headings
		HSSFCellStyle headerFormat = workbook.createCellStyle();

		headerFormat.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		headerFormat.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		headerFormat.setFillBackgroundColor(HSSFColor.BLACK.index);
		headerFormat.setBorderTop(HSSFCellStyle.BORDER_THIN);
		headerFormat.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		headerFormat.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		headerFormat.setBorderRight(HSSFCellStyle.BORDER_THIN);
		headerFormat.setWrapText(true);
		headerFormat.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		HSSFFont font = workbook.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeight((short) 220);
		headerFormat.setFont(font);

		HSSFRow row = sheet.createRow(headerRowNo);
		HSSFCell cell = null;
		
		int j=0,week=0;
		
		for(String string : dateRange)
		{
			if(string.contains(ReportKeys.WEEK_ENDING_DAY))
			{
				week++;
			}
		}
		int length = (headings.length-1) + dateRange.size() + week;
		
		week=1;
		// Writes the headings
		for (int i = 0; i < length; i++) {

			 cell = row.createCell(i);
			if(i<=2)
			{
				cell.setCellStyle(headerFormat);
				cell.setCellValue(headings[i]);
			}
			else
			{
				if(dateRange.get(j).contains(ReportKeys.WEEK_ENDING_DAY))
				{
					cell.setCellStyle(headerFormat);
					cell.setCellValue(dateRange.get(j));
					i=i+1;
					cell = row.createCell(i);
					cell.setCellStyle(headerFormat);
					cell.setCellValue("Week "+week);
					week++;
					j++;
				}
				else
				{
					cell.setCellStyle(headerFormat);
					cell.setCellValue(dateRange.get(j));
					j++;
				}
			}
		}
		if(dateRange.get(dateRange.size() -1).contains(ReportKeys.WEEK_ENDING_DAY))
		{
			cell = row.createCell(length);
		
			cell.setCellStyle(headerFormat);
			
			cell.setCellValue(headings[3]); // direct index based fetch to mark the end of the report.
		}
		else
		{
			cell = row.createCell(length);
			
			cell.setCellStyle(headerFormat);
			
			cell.setCellValue("Week "+week);
			
			cell = row.createCell(length+1);
		
			cell.setCellStyle(headerFormat);
			
			cell.setCellValue(headings[3]); // direct index based fetch to mark the end of the report.
		}
		
		logger.exiting("writeHeadings");
		
	}
}
