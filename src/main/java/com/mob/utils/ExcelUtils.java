package com.mob.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.mob.pojo.Api;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtils {

    private static final String XLS = "xls";
    private static final String XLSX = "xlsx";
    private String fileName;
    private String url;
    private String method;
    private String[] argName;
    private String dependUrl;
    private String[] dependName;

    public static Workbook getWorkbook(InputStream inputStream,String fileType) throws IOException {
        Workbook workbook = null;
        if(fileType.equalsIgnoreCase(XLS)){
            workbook = new HSSFWorkbook(inputStream);
        }else if(fileType.equalsIgnoreCase(XLSX)){
            workbook = new XSSFWorkbook(inputStream);
        }
        return workbook;
    }


    public List<Api> getExcelContent(String sheetName){
        Workbook workbook = null;
        FileInputStream inputStream = null;
        List<Api> apiList = null;
        //读取excel文件
        fileName = System.getProperty("user.dir")+"\\api.xlsx";
        String fileType = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
        try {
            File excelFile = new File(fileName);
            if(!excelFile.exists()){
                System.out.println("excel文件不存在");
                return null;
            }
            inputStream = new FileInputStream(excelFile);
            workbook = getWorkbook(inputStream,fileType);


            //解析行
            apiList = parseExcel(workbook,sheetName);
            //转换为对象

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(workbook != null){
                    workbook.close();
                }
                if(inputStream != null){
                    inputStream.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return  apiList;
    }

    public List<Api> parseExcel(Workbook workbook,String sheetName){

        List<Api> apiList = new ArrayList<Api>();

//        for(int sheetNum = 0;sheetNum<workbook.getNumberOfSheets();sheetNum++){
//            Sheet sheet = workbook.getSheetAt(sheetNum);

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null){
                System.out.println("未找到相关sheet");
                return null;
            }

            int firstRowNum = sheet.getFirstRowNum();
            Row fistRow = sheet.getRow(firstRowNum);
            if(null == fistRow){
                System.out.println("解析excel失败，首行无数据");
            }
            url = fistRow.getCell(1).getStringCellValue();
            method = sheet.getRow(1).getCell(1).getStringCellValue().toLowerCase();
            argName = sheet.getRow(2).getCell(1).getStringCellValue().split(",");
            dependUrl = sheet.getRow(3).getCell(1).getStringCellValue();
            dependName = sheet.getRow(4).getCell(1).getStringCellValue().split(",");

            int rowStart = 6;
            int rowEnd = sheet.getPhysicalNumberOfRows();

            int colStart = 0;
            int colEnd = sheet.getRow(5).getPhysicalNumberOfCells();

            Api api = null;
            for(int rowNum = rowStart;rowNum<rowEnd;rowNum++){
                Row row = sheet.getRow(rowNum);
                Row menuRow = sheet.getRow(5);
                JSONObject reqBody = new JSONObject();
                JSONObject resBody_Exp = new JSONObject();
                api = new Api();
                for(int colNum = colStart;colNum<colEnd;colNum++){
                    Cell cell = row.getCell(colNum);

                    cell.setCellType(CellType.STRING);

                    if(menuRow.getCell(colNum).getStringCellValue().equals("CaseID")){
                        api.setCaseID(NumberUtils.toInt(cell.getStringCellValue()));
                    }
                    if(menuRow.getCell(colNum).getStringCellValue().equals("CaseName")){
                        api.setCaseName(cell.getStringCellValue());
                    }
                    if(menuRow.getCell(colNum).getStringCellValue().equals("Run")){
                        api.setRun(false);
                        if(cell.getStringCellValue().toUpperCase().equals("Y")){
                            api.setRun(true);
                        }else if(cell.getStringCellValue().toUpperCase().equals("N")){
                            api.setRun(false);
                        }
                    }

                    if(menuRow.getCell(colNum).getStringCellValue().endsWith("_Req")){
                        String str = menuRow.getCell(colNum).getStringCellValue();
                        reqBody.put(str.substring(0,str.lastIndexOf("_")),
                                NumberUtils.isNumber(cell.getStringCellValue())==true?Integer.parseInt(cell.getStringCellValue()):cell.getStringCellValue());
                    }

                    if(menuRow.getCell(colNum).getStringCellValue().endsWith("_Exp")){
                        String str = menuRow.getCell(colNum).getStringCellValue();
                        resBody_Exp.put(str.substring(0,str.lastIndexOf("_")),
                                NumberUtils.isNumber(cell.getStringCellValue())==true?Integer.parseInt(cell.getStringCellValue()):cell.getStringCellValue());
                    }
                }

                api.setUrl(url);
                api.setMethod(method);
                api.setArgName(argName);
                api.setDependUrl(dependUrl);
                api.setDependName(dependName);
                api.setReqBody(reqBody);
                api.setResBody_Exp(resBody_Exp);

                apiList.add(api);

            }
        return apiList;
    }


//    public static void main(String args[]) throws IOException {
//       List<Api> apis = new ExcelUtils().getExcelContent("login");
//
//       for(Api api:apis){
//           System.out.println(api.toString());
//       }
//    }

}
