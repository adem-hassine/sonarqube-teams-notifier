package com.proxym.sonarteamsnotifier.metriccall.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CalculatorResponse {
    private boolean firstScan;
    private List<MeasureDto> measures;
}
