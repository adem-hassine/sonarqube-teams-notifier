package com.proxym.sonarteamsnotifier.metriccall.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MeasureDto {
    private String metric;
    private String description;
    private Type type;
    private String difference;
    private String actualValue;
    private String color;
    private Integer order;

}