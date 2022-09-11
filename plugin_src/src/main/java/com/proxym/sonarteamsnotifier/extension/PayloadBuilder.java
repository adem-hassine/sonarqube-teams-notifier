package com.proxym.sonarteamsnotifier.extension;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.proxym.sonarteamsnotifier.DataProvider;
import com.proxym.sonarteamsnotifier.constants.Constants;
import com.proxym.sonarteamsnotifier.constants.PayloadUtils;
import com.proxym.sonarteamsnotifier.model.Measure;
import com.proxym.sonarteamsnotifier.model.MeasuresContainer;
import com.proxym.sonarteamsnotifier.webhook.*;
import lombok.Getter;
import org.sonar.api.ce.posttask.Branch;
import org.sonar.api.ce.posttask.PostProjectAnalysisTask;
import org.sonar.api.ce.posttask.QualityGate;

/**
 * Builds a payload for a WebEx Teams message.
 */
class PayloadBuilder {

  private final String[] metrics;
  private final boolean showAuthor;
  /**
   * Logger.
   */
  @Getter
  private final  String token;

  /**
   * Project Analysis.
   */
  private final PostProjectAnalysisTask.ProjectAnalysis analysis;

  /**
   * Project URL.
   */
  private final String baseUrl;

  /**
   * Whether to send notifications on only failures.
   */

  /**
   * Whether the overall QualityGate status is OK or not.
   */
  private final boolean qualityGateOk;

  /**
   * Decimal format for percentages.
   */
  private final DecimalFormat percentageFormat;
  private final String projectId;

  /**
   * Constructor.
   *
   * @param analysis      The Project's analysis.
   * @param baseUrl    The URL for the project.
   * @param qualityGateOk Whether the overall quality gate status is OK or not.
   */
  private PayloadBuilder(
      PostProjectAnalysisTask.ProjectAnalysis analysis,
      String baseUrl,
      boolean qualityGateOk,
      String token,
      String projectId,
      String[] metrics,
      boolean showAuthor) {
    this.analysis = analysis;
    this.baseUrl = baseUrl;
    this.qualityGateOk = qualityGateOk;
    this.token = token;
    // Round percentages to 2 decimal points.
    this.percentageFormat = new DecimalFormat();
    this.percentageFormat.setMaximumFractionDigits(2);
    this.projectId= projectId;
    this.metrics = metrics;
    this.showAuthor= showAuthor;
  }

  /**
   * Static pattern PayloadBuilder constructor.
   *
   * @param analysis      The Project's analysis.
   * @param baseUrl    The URL for the project.
   * @param qualityGateOk Whether the overall quality gate status is OK or not.
   *
   * @return The PayloadBuilder
   */
  static PayloadBuilder of(
      PostProjectAnalysisTask.ProjectAnalysis analysis,
      String baseUrl,
      boolean qualityGateOk,
      String token,
      String projectId,
      String[] metrics,
      boolean showAuthor

  ) {
    return new PayloadBuilder(analysis, baseUrl, qualityGateOk,token,projectId,metrics,showAuthor);
  }
  /**
   * Builds the payload.
   *
   * @return The payload as a JSON-encoded string.
   */
  Payload build() {
    assertNotNull(baseUrl, "baseUrl");
    assertNotNull(analysis, "analysis");
    QualityGate qualityGate = analysis.getQualityGate();
    Payload payload = new Payload();
    if (qualityGate != null) {
      Optional<Branch> branch = analysis.getBranch();
      String projectName = analysis.getProject().getName();
      appendHeader(payload,projectName);
      appendCommit(payload,qualityGate ,branch,projectName);
      appendConditions(payload);
      appendAction(payload);
    }

    return payload;
  }

  /**
   * Appends the header to the message.
   *
   * @param message     The StringBuilder being used to build the message.
   */
  private void appendHeader(
      Payload message,
      String projectName
  ) {
    message.setType(PayloadUtils.MESSAGE_CARD);
    message.setThemeColor(qualityGateOk ? Constants.GREEN_COLOR :Constants.RED_COLOR);
    message.setSummary(PayloadUtils.SUMMARY.concat(projectName));
    message.setSections(new ArrayList<>());
  }

  /**
   * Appends commit information to the message.
   *
   * @param message The StringBuilder being used to build the message.
   */
  private void  appendCommit(Payload message,QualityGate qualityGate,Optional<Branch> branch,String projectName) {
    Section section = new Section();
    section.setActivityTitle(PayloadUtils.SUMMARY.concat(projectName));
    String branchSubtitle="";
    if (branch.isPresent()){
      Branch present = branch.get();
      branchSubtitle  = present.getName().map(name -> "Branch " + name +",").orElse("");
    }
    section.setActivitySubtitle(branchSubtitle + "Status : [".concat(qualityGate.getStatus().toString()).concat("]"));
    section.setActivityImage(DataProvider.getProperty("LOGO_URL"));
    message.getSections().add(section);

  }

  /**
   * Appends Condition statuses to the message.
   *
   */

  private void appendConditions(Payload message) {
    Section section = message.getSections().get(0);
    section.setFacts(new ArrayList<>());
    String url =  String.format(DataProvider.getProperty(Constants.MEASURES_ENDPOINT),projectId, Arrays.stream(metrics).collect(Collectors.joining(Constants.COMMA)),2);
    MeasuresContainer measuresContainer =  SonarRequestSender.get(baseUrl,url,token);
    for (Measure measure : measuresContainer.getMeasures()) {
      Fact fact = new Fact();
      fact.setName(measure.getMetric());
      fact.setValue(measure.getHistory().get(measure.getHistory().size()-1).getValue());
      section.getFacts().add(fact);
    }
  }
  private void appendAction(Payload message){
    Action action = new Action();
    action.setType(PayloadUtils.OPEN_URI);
    action.setName("Show More");
    ActionTarget target = new ActionTarget();
    target.setOs("default");
    target.setUri(baseUrl.concat("dashboard?id=".concat(projectId)));
    List<ActionTarget> targets = new ArrayList<>();
    targets.add(target);
    action.setTargets(targets);
    List<Action> potentialAction = new ArrayList<>();
    potentialAction.add(action);
    message.setPotentialAction(potentialAction);
  }
  private void assertNotNull(Object object, String objectName) {
    if (object == null) {
      throw new IllegalArgumentException(
          "[Assertion failed] - " + objectName + " argument is required; it must not be null"
      );
    }
  }
}
