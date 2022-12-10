package com.proxym.sonarteamsnotifier.metriccall.dto;

import com.proxym.sonarteamsnotifier.constants.PayloadUtils;
import lombok.Getter;

@Getter
public enum Color {
    GREEN(PayloadUtils.GREEN_COLOR, "green"), RED(PayloadUtils.RED_COLOR, "red"), BLACK("000000","black");

    Color(String reference, String cssStyle) {
        this.reference = reference;
        this.cssStyle = cssStyle;
    }

    private final String cssStyle;
    private final String reference;
}
