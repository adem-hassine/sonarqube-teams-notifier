package com.proxym.sonarteamsnotifier.constants;


/**
 * Constants to be used across the plugin.
 */
public class Constants {

    /**
     * The plugin's category.
     */
    public static final String CATEGORY = "Teams";
    /**
     * The plugin's metric list.
     */
    public static final String METRICS="Metrics";
    /**
     * The plugin's sub category.
     */
    public static final String SUBCATEGORY = "Teams Notifier Plugin";

    /**
     * The name of the enabled property.
     */
    public static final String ENABLED = "sonar.teams.enabled";

    /**
     * The name of the webhook property, supplied to sonar-scanner.
     */
    public static final String HOOK = "sonar.teams.hook";
    public static final String TOKEN = "sonar.login";
    public static final String PROJECT_ID = "sonar.analysis.projectId";
    public static final String SERVER_URL = "sonar.host.url";


    private Constants() {
    }
}
