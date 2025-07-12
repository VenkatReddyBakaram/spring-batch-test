package com.vtechstorms.vems.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class EmployeeDTO {


    private Long empId;

    private String empName;
    private Double salary;

}