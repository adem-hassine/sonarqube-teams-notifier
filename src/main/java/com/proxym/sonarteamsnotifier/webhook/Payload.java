package com.proxym.sonarteamsnotifier.webhook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payload {
  private String type;
  private String context;
  private String themeColor;
  private String summary;
  private List<Section> sections;
  private List<Action> potentialAction;
}
