package com.test.excel;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.test.util.ReflectUtil;

public class ExcelImport {

	private final String XLS_EXTNAME = "xls";
	private final String XLSX_EXTNAME = "xlsx";

	private Workbook wb;
	private Sheet sheet;
	private Row row;
	private int sheetNum = 0; // 默认读取第一列
	private int cellCount = 0;

	/**
	 * 创建wb
	 * 
	 * @param is
	 * @param extName
	 */
	public void createWorkBook(InputStream is, String extName) {
		try {
			if (XLS_EXTNAME.equals(extName)) {
				wb = new HSSFWorkbook(is);
			} else if (XLSX_EXTNAME.equals(extName)) {
				wb = new XSSFWorkbook(is);
			} else {
				throw new Exception("当前文件不是excel文件");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建wb, 根据文件头判断类型
	 * 
	 * @param is
	 * @throws Exception
	 */
	public void createWorkBook(InputStream is) throws Exception {
		try {
			wb = WorkbookFactory.create(is);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("当前文件不是excel文件");
		}
	}

	/**
	 * 返回总行数
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getRowCount() throws Exception {
		if (null == wb) {
			throw new Exception("excel.impot.wbIsNull");
		}
		Sheet sheet = wb.getSheetAt(this.sheetNum);
		int rowCount = -1;
		rowCount = sheet.getLastRowNum();
		return rowCount;
	}

	/**
	 * 获取总列数
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getCellCount() throws Exception {
		if (null == wb) {
			throw new Exception("excel.impot.wbIsNull");
		}
		Sheet sheet = wb.getSheetAt(0);
		Row tRow = sheet.getRow(0);
		cellCount = tRow.getPhysicalNumberOfCells();
		return cellCount;
	}

	/**
	 * 获取某行的内容，返回行数据数组
	 * 
	 * @param rowNum
	 * @return
	 * @throws Exception
	 */
	public String[] readExcelRow(int rowNum) throws Exception {
		if (this.sheetNum < 0 || rowNum < 0) {
			return null;
		}
		String[] strContent = null;
		sheet = wb.getSheetAt(this.sheetNum);
		row = sheet.getRow(rowNum);
		if (null == row) {
			return null;
		}
		// row.getPhysicalNumberOfCells() 遇到空行 -1
		// row.getLastCellNum() 统计到最后不为空的行
		// 由于以上两种方式皆不能完整的统计到完整的行数所以根据标题行获取行数
		int c = this.cellCount;
		strContent = new String[c];
		for (int i = 0; i < c; i++) {
			Cell cell = row.getCell(i);
			strContent[i] = getCellValue(cell);
		}
		return strContent;
	}

	
	/**
	 * 获取某行的内容，返回行数据数组
	 * @param <T>
	 * 
	 * @param rowNum
	 * @return
	 * @throws Exception
	 */
	public <T> T readExcelRow(Class<T> clazz, String[] fields, int rowNum) throws Exception {
		if (this.sheetNum < 0 || rowNum < 0) {
			return null;
		}
		sheet = wb.getSheetAt(this.sheetNum);
		row = sheet.getRow(rowNum);
		if (null == row) {
			return null;
		}
		T target = clazz.newInstance();
		for (int i = 0; i < fields.length; i++) {
			Cell cell = row.getCell(i);
			String fieldName = fields[i];
			String v = getCellValue(cell);
			Field field = clazz.getDeclaredField(fieldName);
			ReflectUtil.invokeSetter(target, fieldName, parseValueByType(v, field.getType()));
		}
		return target;
	}
	/**
	 * 根据Bean 字段属性处理数据
	 * @param v 值
	 * @param o 字段类型
	 * @return
	 */
	public static Object parseValueByType(String v, Object o) {
		String oToS = o.toString();
		try {
			if (oToS.endsWith("String")) {
				return checkValue(v);
			} else if (oToS.endsWith("int") || oToS.endsWith("Integer")) {
				return checkValue(v) == "" ? 0 : Integer.parseInt(v);
			} else if (oToS.endsWith("Date")) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if (checkValue(v) == "") {
					return sdf.parse(sdf.format(new Date()));
				}
				return sdf.parse(v);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static Object checkValue(Object o){
		if(null == o || "".equals(o) || "null".equals(o)){
			return "";
		}
		return o;
	}
	
	/**
	 * 根据cell 类型处理数据
	 * @param cell
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getCellValue(Cell cell) {
		String cellValue = "";
		if (cell == null) {
			return null;
		}
		// 把数字当成String来读，避免出现1读成1.0的情况, 排除为日期类型的数字
		if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC
				&& !DateUtil.isCellDateFormatted(cell)) {
			cell.setCellType(Cell.CELL_TYPE_STRING);
		}
		// 判断数据的类型
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC: // 数字
			if (DateUtil.isCellDateFormatted(cell)) {
				// 方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
				cellValue = cell.getDateCellValue().toLocaleString();
				// 方法2：这样子的data格式是不带带时分秒的：2011-10-12
				// Date date = cell.getDateCellValue();
				// SimpleDateFormat sdf = new
				// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				// cellValue = sdf.format(date);
			} else
				cellValue = String.valueOf(cell.getNumericCellValue());
			break;
		case Cell.CELL_TYPE_STRING: // 字符串
			String v = cell.getStringCellValue();
			if (null == v || "" == v)
				cellValue = null;
			else
				cellValue = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_BOOLEAN: // Boolean
			cellValue = String.valueOf(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_FORMULA: // 公式
		{
			if (DateUtil.isCellDateFormatted(cell)) {
				Date date = cell.getDateCellValue();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				cellValue = sdf.format(date);
			} else {
				cellValue = String.valueOf(cell.getCellFormula());
			}
			break;
		}
		case Cell.CELL_TYPE_BLANK: // 空值
			cellValue = "null";
			break;
		case Cell.CELL_TYPE_ERROR: // 故障
			cellValue = "非法字符";
			break;
		default:
			cellValue = "null";
			break;
		}
		return cellValue;
	}

	public int getSheetNum() {
		return sheetNum;
	}

	public void setSheetNum(int sheetNum) {
		this.sheetNum = sheetNum;
	}

	public static void main(String[] args) {
		ExcelImport excelImp = new ExcelImport();
		try {
			InputStream is = new FileInputStream("d:\\11.xls");
			excelImp.createWorkBook(is);
			excelImp.setSheetNum(0); // 设置读取的sheet页数
			excelImp.getCellCount();
			int rowCount = excelImp.getRowCount();
			for (int i = 1; i <= rowCount; i++) {
				String[] cellContent = excelImp.readExcelRow(i);
				if (null == cellContent || cellContent.length == 0)
					continue;
				for(int j =0;j<cellContent.length;j++) {
					System.out.println(cellContent.length +"  " +cellContent[j]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
