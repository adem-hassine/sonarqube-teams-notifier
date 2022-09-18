package com.proxym.sonarteamsnotifier.metriccall;

import java.util.ArrayList;
import java.util.List;

public class Measure {
  private String metric;
  private List<History> history = new ArrayList<>();

  public String getMetric() {
    return metric;
  }

  public void setMetric(String metric) {
    this.metric = metric;
  }

  public List<History> getHistory() {
    return history;
  }

  public void setHistory(List<History> history) {
    this.history = history;
  }
}
