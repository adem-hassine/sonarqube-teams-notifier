package com.proxym.sonarteamsnotifier.plugin.webhook;

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
  private String type;
  private String name;
  private List<ActionTarget> targets;
}
