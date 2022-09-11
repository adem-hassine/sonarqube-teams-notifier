package com.proxym.sonarteamsnotifier.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Action {
  @JsonProperty("@type")
  private String type;
  private String name;
  private List<ActionTarget> targets;
}
