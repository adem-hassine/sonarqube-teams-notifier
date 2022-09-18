package com.proxym.sonarteamsnotifier.metriccall;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SonarPage {
    private Integer pageIndex;
    private Integer pageSize;
    private Integer total;

}
