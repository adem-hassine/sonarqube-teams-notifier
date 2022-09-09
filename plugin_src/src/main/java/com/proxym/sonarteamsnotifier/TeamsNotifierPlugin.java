package com.proxym.sonarteamsnotifier;

import com.proxym.sonarteamsnotifier.constants.Constants;
import com.proxym.sonarteamsnotifier.extension.TeamsPostProjectAnalysisTask;
import com.proxym.sonarteamsnotifier.extension.TeamsSensor;

import java.util.ArrayList;
import java.util.List;

import com.proxym.sonarteamsnotifier.web.TeamsNotifierPageDefinition;
import org.sonar.api.Plugin;
import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;


/**
 * Base Plugin class.
 */
public class TeamsNotifierPlugin implements Plugin {

    private int propertyIndex = 0;

  @Override
  public void define(Context context) {
    List<Object> extensions = pluginPropertyDefinitions();
    extensions.add(TeamsSensor.class);
    extensions.add(TeamsPostProjectAnalysisTask.class);
    extensions.add(TeamsNotifierPageDefinition.class);
    context.addExtensions(extensions);
  }

  /**
   * Gets this plugin's property definitions.
   *
   * @return A list of this plugin's property definitions.
   */
  private List<Object> pluginPropertyDefinitions() {
    List<Object> extensions = new ArrayList<>();
    extensions.add(getProperty(Constants.ENABLED, "Plugin enabled",
        "Are Teams notifications enabled in general?",
        "true", PropertyType.BOOLEAN));
    return extensions;
  }

  /**
   * Gets a single property to add to Sonar plugins.
   *
   * @param property     Property ID.
   * @param name         Property name.
   * @param description  Property description.
   * @param defaultValue Default value of the property.
   * @param type         Property type.
   *
   * @return The property to add.
   */
  private PropertyDefinition  getProperty(
      String property,
      String name,
      String description,
      String defaultValue,
      PropertyType type
  ) {
    return PropertyDefinition.builder(property)
      .name(name)
      .description(description)
      .defaultValue(defaultValue)
      .type(type)
      .category(Constants.CATEGORY)
      .subCategory(Constants.SUBCATEGORY)
      .index(propertyIndex++)
      .build();
  }
}
