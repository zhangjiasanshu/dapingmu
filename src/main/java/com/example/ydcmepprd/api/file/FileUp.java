package com.example.ydcmepprd.api.file;

import com.example.ydcmepprd.contant.fileEnum.FileUpTypeEnum;
import com.example.ydcmepprd.exeption.BusinessExeption;
import com.example.ydcmepprd.exeption.BusinessExeptionCodeEnum;
import com.example.ydcmepprd.utils.fileEnumUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:excel文件上传解析类
 * @author: yaolewei
 * @date: 2019-09-18 10:19
 */
@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class FileUp {
    /**类型用于最后的中文头转换成字段名用于map转换成实体类*/
    private Integer type;
    /**最小有效列*/
    private Integer minLineNum;
    /**最大有效列*/
    private Integer maxLineNum;
    /**头信息所在的行号*/
    private Integer headRow;
    /**无用的行 格式为3,4,2,1*/
    private String unlessRow;
    /**不需要入库的列 格式为3,4,2,1*/
    private String unlessLine;
    /**特殊的表格里未标注的头信息*/
    private Map<Integer,String> headMap;


    /**
     * @description:excel获取头信息
     * @author: yaolewei
     * @date: 2019-09-18 10:19
     */
    public List<Map<String, String>> process(MultipartFile file) throws IOException {
        if (file.getOriginalFilename().contains(".xlsx")){
            XSSFWorkbook xssfSheets = new XSSFWorkbook(file.getInputStream());
            return sheetProcess(xssfSheets);
        }else if (file.getOriginalFilename().contains(".xls")){
            HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
            return sheetProcess(workbook);
        }else {
            log.error("excel数据导入,文件格式异常");
            throw new BusinessExeption(BusinessExeptionCodeEnum.FILE_TYPE_ERROR);
        }
    }


    /**
     * @description:sheet处理
     * @author: yaolewei
     * @date: 2019/9/19
     * param:xssfSheets
     * param: book
     * @return:
     */
    public List<Map<String, String>> sheetProcess(Object book) {
        List<String> headList = new ArrayList<>();
        List<String> cellList = new ArrayList<>();
        if (book instanceof HSSFWorkbook) {
            HSSFWorkbook workbook = (HSSFWorkbook)book;
            HSSFSheet sheetAt = workbook.getSheetAt(0);
            for (Row row : sheetAt) {
                StringBuilder thisRowNum = new StringBuilder();
                thisRowNum.append(",").append(row.getRowNum()).append(",");
                if (unlessRow.contains(thisRowNum.toString())){
                    //排除无用行
                    continue;
                }else if (row.getRowNum() == headRow){
                    //填充头信息
                    headList = getHead(row);
                }else {
                    //填充剩余数据
                    cellList.addAll(getCell(row));
                }
            }
        } else {
            XSSFWorkbook xworkbook = (XSSFWorkbook)book;
            XSSFSheet sheetAt = xworkbook.getSheetAt(0);
            for (Row row : sheetAt) {
                StringBuilder thisRowNum = new StringBuilder(",").append(row.getRowNum()).append(",");
                if (unlessRow.contains(thisRowNum.toString())){
                    //排除无用行
                    continue;
                }else if (row.getRowNum() == headRow){
                    //填充头信息
                    headList = getHead(row);
                }else {
                    //填充剩余数据
                    cellList.addAll(getCell(row));
                }
            }

        }
        return formatDate(headList, cellList);
    }

    /**
     * @description:excel获取头信息
     * @author: yaolewei
     * @date: 2019-09-18 10:19
     */
    public List<String> getHead(Row row) {
        ArrayList<String> headList = new ArrayList<>();
        for (int cellNo=minLineNum;cellNo<=maxLineNum;cellNo++){
            StringBuilder cellNoSB = new StringBuilder(",").append(cellNo).append(",");
            if (unlessLine !=null && unlessLine.contains(cellNoSB.toString())){
                continue;
            }else if (headMap.get(cellNo)!=null){
                headList.add(headMap.get(cellNo));
            }else {
                row.getCell(cellNo).setCellType(Cell.CELL_TYPE_STRING);
                headList.add(row.getCell(cellNo).getStringCellValue().trim());
            }
        }
        return headList;
    }

    /**
     * @description:excel获取所有表格信息
     * @author: yaolewei
     * @date: 2019-09-18 10:19
     */
    public List<String> getCell(Row row) {
        ArrayList<String> cellList = new ArrayList<>();
        for (int cellNo=minLineNum;cellNo<=maxLineNum;cellNo++){
            StringBuilder cellNoSB = new StringBuilder(",").append(cellNo).append(",");
            if (unlessLine !=null && unlessLine.contains(cellNoSB.toString())){
                continue;
            }
            row.getCell(cellNo).setCellType(Cell.CELL_TYPE_STRING);
            cellList.add(row.getCell(cellNo).getStringCellValue().trim());
        }
        return cellList;
    }

    /**
     * @description:根据头和表格数据进行数据拼接获得数据map
     * @author: yaolewei
     * @date: 2019-09-18 10:19
     */
    public List<Map<String,String>> formatDate(List<String> headList,List<String> cellList)

}
