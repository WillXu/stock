package com.zhaijiong.stock;

import com.google.common.collect.Lists;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by xuqi.xq on 2014/6/8.
 */
public class ExcelReader {
    private Sheet sheet;
    private Workbook workbook;

    public void read(String path,int sheetNum) throws IOException, BiffException {
        workbook =  Workbook.getWorkbook(new File(path));
        sheet = workbook.getSheet(sheetNum);
    }

    public static List<Cell> readline(Sheet sheet ,int lineNum){
        List<Cell> list = Lists.<Cell>newArrayList();
        for(int i=0;i<sheet.getColumns();i++){
            Cell cell = sheet.getCell(i,lineNum);
            if(cell==null){
                break;
            }
            list.add(cell);
        }
        return list;
    }

    public static List<Cell> readHeader(Sheet sheet){
        return readline(sheet,0);
    }

    public static void printLine(List<Cell> line){
        for(Cell cell:line){
            System.out.print(cell.getContents() + "\t");
        }
    }

    public static void main(String[] args) throws IOException, BiffException {
        ExcelReader reader = new ExcelReader();
        reader.read("e:/stock/_600359_debtreport.xls",0);

        for(int i=1;i<reader.sheet.getRows();i++){
            System.out.println("line:"+i);
            printLine(readHeader(reader.sheet));
            System.out.println("");
            List<Cell> readline = reader.readline(reader.sheet,i);
            printLine(readline);
        }
    }
}
