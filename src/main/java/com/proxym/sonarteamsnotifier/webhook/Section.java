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
public class Section {
  private String activityTitle;
  private String activitySubtitle;
  private String activityImage;
  private List<Fact> facts;
  private boolean markdown;

}
