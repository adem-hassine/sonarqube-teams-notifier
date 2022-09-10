package com.proxym.sonarteamsnotifier.plugin.extension;

import com.proxym.sonarteamsnotifier.plugin.constants.Constants;

import java.util.Map;

import org.sonar.api.ce.posttask.PostProjectAnalysisTask;
import org.sonar.api.ce.posttask.QualityGate;
import org.sonar.api.config.Configuration;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

/**
 * Post Project Analysis Task that sends the WebEx Teams notification message.
 */
public class TeamsPostProjectAnalysisTask implements PostProjectAnalysisTask {

  /**
   * Logger.
   */
  private static final Logger LOG = Loggers.get(TeamsPostProjectAnalysisTask.class);

  /**
   * SonarQube settings.
   */
  private final Configuration settings;

  /**
   * Constructor.
   *
   * @param settings The SonarQube Configuration settings.
   */
  public TeamsPostProjectAnalysisTask(Configuration settings) {
    this.settings = settings;
  }

  /**
   * Post analysis task.
   *
   */
  @Override
  public void finished(final Context context) {
    if (!isPluginEnabled()) {
      LOG.info("Teams Notifier Plugin disabled.");
      return;
    }

    Map<String, String> properties = context.getProjectAnalysis().getScannerContext().getProperties();
    if (!properties.containsKey(Constants.HOOK)) {
      LOG.info("No hook URL found for Teams Notifier Plugin.");
      return;
    }
    if(!properties.containsKey(Constants.TOKEN)){
      LOG.info("No token found for Teams Notifier Plugin");
      return ;
    }
    if (!properties.containsKey(Constants.PROJECT_ID)){
      LOG.info("No project id found for teams notifier plugin");
      return;
    }
    if(!properties.containsKey(Constants.SERVER_URL)){
      LOG.info("No server url found for teams notifier plugin");
      return;
    }
    String projectUrl = properties.get(Constants.SERVER_URL);
    String hook = properties.get(Constants.HOOK);
    String token = properties.get(Constants.TOKEN);
    String projectId= properties.get(Constants.PROJECT_ID);
    LOG.debug("Analysis ScannerContext: [{}]", properties);
    LOG.debug("Teams notification URL: " + hook);
    LOG.debug("Teams notification analysis: " + context);
    sendNotification(hook, context,token,projectId,projectUrl);
  }

  /**
   * Checks if the quality gate status is set and is OK.
   *
   *
   * @return True if quality gate is set and is OK. False if not.
   */
  private boolean qualityGateOk(Context context) {
    QualityGate qualityGate = context.getProjectAnalysis().getQualityGate();
    return (qualityGate != null && QualityGate.Status.OK.equals(qualityGate.getStatus()));
  }

  /**
   * Sends the WebEx teams notification.
   *
   * @param hook     The hook URL.
   */
  private void sendNotification(String hook,Context context,String token,String projectId,String serverUrl) {
    try {
      ProjectAnalysis analysis =  context.getProjectAnalysis();
      TeamsHttpClient httpClient = TeamsHttpClient
          .of(hook, PayloadBuilder.of(analysis,
                   serverUrl, qualityGateOk(context),token,projectId)
              .build())
          .build();
      if (httpClient.post()) {
        LOG.info("Teams message posted");
      } else {
        LOG.error("Teams message failed");
      }
    } catch (Exception e) {
      LOG.error("Failed to send teams message", e);
    }
  }


  private boolean isPluginEnabled() {
    return settings.getBoolean(Constants.ENABLED).orElse(false);
  }

}
