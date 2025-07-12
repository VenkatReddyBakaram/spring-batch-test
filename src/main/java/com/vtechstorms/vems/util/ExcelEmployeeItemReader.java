package com.vtechstorms.vems.util;

import com.vtechstorms.vems.dtos.EmployeeDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.item.ItemReader;

import java.io.InputStream;
import java.util.Iterator;

public class ExcelEmployeeItemReader implements ItemReader<EmployeeDTO> {

    private final Iterator<Row> rowIterator;

    public ExcelEmployeeItemReader(InputStream inputStream) throws Exception {
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        this.rowIterator = sheet.iterator();
        rowIterator.next(); // skip header
    }

    @Override
    public EmployeeDTO read() {
        if (!rowIterator.hasNext()) return null;

        Row row = rowIterator.next();
        EmployeeDTO dto = new EmployeeDTO();
        Cell empIdCell = row.getCell(0);
        if (empIdCell != null && empIdCell.getCellType() == CellType.NUMERIC) {
            dto.setEmpId((long) empIdCell.getNumericCellValue());
        }
        dto.setEmpName(row.getCell(1).getStringCellValue());
        dto.setSalary(row.getCell(2).getNumericCellValue());

        return dto;
    }
}
