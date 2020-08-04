package com.peilian.dataplatform.util;

import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.ss.usermodel.*;

import java.util.List;

import static org.apache.poi.ss.usermodel.BorderStyle.THIN;

/**
 * excel单元格样式
 *
 * @author zhengshangchao
 */
public class CustomCellWriteHandler implements CellWriteHandler {


    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer columnIndex, Integer relativeRowIndex, Boolean isHead) {

    }

    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        Workbook workbook = writeSheetHolder.getSheet().getWorkbook();
        CellStyle cellStyle = workbook.createCellStyle();
        if (!isHead) {
            // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了FillPatternType所以可以不指定
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            //设置 自动换行
            cellStyle.setWrapText(true);
            //设置 水平居中
            cellStyle.setAlignment(HorizontalAlignment.LEFT);
            //设置边框样式
            cellStyle.setBorderLeft(THIN);
            cellStyle.setBorderTop(THIN);
            cellStyle.setBorderRight(THIN);
            cellStyle.setBorderBottom(THIN);
            Font cellFont = workbook.createFont();
            cellFont.setFontHeightInPoints((short) 12);
            cellFont.setBold(false);
            cellStyle.setFont(cellFont);
            cell.setCellStyle(cellStyle);
        }
    }

    @Override
    public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, CellData cellData, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {

    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {

    }
}
