package com.proxym.sonarteamsnotifier;

import com.proxym.sonarteamsnotifier.constants.Constants;
import com.proxym.sonarteamsnotifier.extension.TeamsPostProjectAnalysisTask;
import com.proxym.sonarteamsnotifier.extension.TeamsSensor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        context.addExtensions(extensions);
    }

    /**
     * Gets this plugin's property definitions.
     *
     * @return A list of this plugin's property definitions.
     */
    private List<Object> pluginPropertyDefinitions() {
        List<Object> extensions = new ArrayList<>();
        extensions.add(getProperty(Constants.ENABLED, "Enabled",
                "Is Teams Notifier plugin enabled ?", "false", PropertyType.BOOLEAN));
        extensions.add(getProperty(Constants.SHOW_AUTHOR, "Show Author",
                "Post author name with notification",
                "false", PropertyType.BOOLEAN));
        extensions.add(getSelectProperty(Constants.METRICS, "Available metrics",
                "Choose metrics in order to be sent within the teams notification?",
                Arrays.stream(DataProvider.getProperty(Constants.REPORTS_METRICS)
                        .split(Constants.COMMA)).collect(Collectors.toList()), PropertyType.SINGLE_SELECT_LIST, true));
        extensions.add(getSelectProperty(Constants.POST_CONDITIONS, "Notification Conditions",
                "Choose quality gate condition for notification",
                List.of(Constants.BAD_QUALITY_GATEWAY, Constants.GOOD_QUALITY_GATEWAY,
                        Constants.ANYWAYS), PropertyType.SINGLE_SELECT_LIST, false));

        return extensions;
    }


    private PropertyDefinition getSelectProperty(
            String property,
            String name,
            String description,
            List<String> defaultValue,
            PropertyType type,
            boolean multiSelect
    ) {
        return PropertyDefinition.builder(property)
                .name(name)
                .description(description)
                .type(type)
                .multiValues(multiSelect)
                .options(defaultValue)
                .category(Constants.CATEGORY)
                .subCategory(Constants.SUBCATEGORY)
                .index(propertyIndex++)
                .build();
    }

    private PropertyDefinition getProperty(
            String property,
            String name,
            String description,
            String defaultValue,
            PropertyType type
    ) {
        return PropertyDefinition.builder(property)
                .name(name)
                .description(description)
                .type(type)
                .defaultValue(defaultValue)
                .category(Constants.CATEGORY)
                .subCategory(Constants.SUBCATEGORY)
                .index(propertyIndex++)
                .build();
    }
}
