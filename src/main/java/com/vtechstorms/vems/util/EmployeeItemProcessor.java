package com.vtechstorms.vems.util;

import com.vtechstorms.vems.dtos.EmployeeDTO;
import com.vtechstorms.vems.entities.Employee;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class EmployeeItemProcessor implements ItemProcessor<EmployeeDTO, Employee> {

    @Override
    public Employee process(EmployeeDTO dto) {
        Employee entity = new Employee();
        entity.setEmpId(dto.getEmpId());
        entity.setEmpName(dto.getEmpName() != null ? dto.getEmpName().trim().toUpperCase() : null);
        entity.setSalary(dto.getSalary());
        return entity;
    }
}
