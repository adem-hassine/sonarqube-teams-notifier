package com.proxym.sonarteamsnotifier.plugin;


import java.util.ArrayList;
import java.util.List;

import com.proxym.sonarteamsnotifier.plugin.web.TeamsNotifierPageDefinition;
import org.sonar.api.Plugin;


/**
 * Base Plugin class.
 */
public class TeamsNotifierPlugin implements Plugin {


  @Override
  public void define(Context context) {
    List<Object> extensions = new ArrayList<>();
    extensions.add(TeamsNotifierPageDefinition.class);
    context.addExtensions(extensions);
  }
}
