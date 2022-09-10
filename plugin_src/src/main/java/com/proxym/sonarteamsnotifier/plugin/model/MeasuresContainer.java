package com.proxym.sonarteamsnotifier.plugin.model;


import java.util.List;

public class MeasuresContainer {
  private List<Measure> measures;

  public List<Measure> getMeasures() {
    return measures;
  }

  public void setMeasures(List<Measure> measures) {
    this.measures = measures;
  }
}
