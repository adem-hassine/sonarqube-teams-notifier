package com.proxym.sonarteamsnotifier.extension;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.proxym.sonarteamsnotifier.DataProvider;
import com.proxym.sonarteamsnotifier.calculator.MetricsCalculator;
import com.proxym.sonarteamsnotifier.constants.Constants;
import com.proxym.sonarteamsnotifier.constants.PayloadUtils;
import com.proxym.sonarteamsnotifier.metriccall.dto.CalculatorResponse;
import com.proxym.sonarteamsnotifier.metriccall.dto.Color;
import com.proxym.sonarteamsnotifier.metriccall.dto.MeasureDto;
import com.proxym.sonarteamsnotifier.metriccall.dto.Type;
import com.proxym.sonarteamsnotifier.webhook.*;
import org.sonar.api.ce.posttask.Branch;
import org.sonar.api.ce.posttask.PostProjectAnalysisTask;
import org.sonar.api.ce.posttask.QualityGate;

/**
 * Builds a payload for a WebEx Teams message.
 */
class PayloadBuilder {

    /**
     * Metrics provided by administration settings.
     */
    private final String[] metrics;
    /**
     * Logger.
     */
    private final String token;

    /**
     * Project Analysis.
     */
    private final PostProjectAnalysisTask.ProjectAnalysis analysis;

    /**
     * Project URL.
     */
    private final String baseUrl;

    /**
     * Whether the overall QualityGate status is OK or not.
     */
    private final boolean qualityGateOk;

    private final String projectId;


    private PayloadBuilder(PostProjectAnalysisTask.ProjectAnalysis analysis, String baseUrl, boolean qualityGateOk, String token, String projectId, String[] metrics) {
        this.analysis = analysis;
        this.baseUrl = baseUrl;
        this.qualityGateOk = qualityGateOk;
        this.token = token;
        this.projectId = projectId;
        this.metrics = metrics;
    }

    /**
     * Static pattern PayloadBuilder constructor.
     */
    static PayloadBuilder of(PostProjectAnalysisTask.ProjectAnalysis analysis, String baseUrl, boolean qualityGateOk, String token, String projectId, String[] metrics) {
        return new PayloadBuilder(analysis, baseUrl, qualityGateOk, token, projectId, metrics);
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
            appendHeader(payload, projectName);
            appendCommit(payload, qualityGate, branch, projectName);
            appendConditions(payload);
            appendAction(payload);
        }

        return payload;
    }

    /**
     * Appends the header to the message.
     */
    private void appendHeader(Payload message, String projectName) {
        message.setType(PayloadUtils.MESSAGE_CARD);
        message.setThemeColor(qualityGateOk ? PayloadUtils.GREEN_COLOR : PayloadUtils.RED_COLOR);
        message.setSummary(PayloadUtils.SUMMARY.concat(projectName));
        message.setSections(new ArrayList<>());
    }

    /**
     * Appends commit information to the message.
     */
    private void appendCommit(Payload message, QualityGate qualityGate, Optional<Branch> optionalBranch, String projectName) {
        Section section = new Section();
        section.setActivityTitle(PayloadUtils.SUMMARY.concat(projectName));
        section.setActivitySubtitle(optionalBranch.filter(branch -> branch.getName().isPresent()).map(branch -> String.format(PayloadUtils.BRANCH, branch.getName().get())).orElse("") + String.format(PayloadUtils.STATUS, qualityGate.getStatus().toString()));
        section.setActivityImage(DataProvider.getProperty("LOGO_URL"));
        message.getSections().add(section);

    }

    /**
     * Appends Condition statuses to the message.
     */

    private void appendConditions(Payload message) {
        Section section = message.getSections().get(0);
        String noPageDefinitionUrl = String.format(DataProvider.getProperty(Constants.MEASURES_ENDPOINT), projectId, String.join(Constants.COMMA, metrics));
        CalculatorResponse response = MetricsCalculator.calculate(baseUrl, noPageDefinitionUrl, token);
        List<Fact> facts = new ArrayList<>();
        response.getMeasures().stream().filter(measure -> !response.isFirstScan() || !measure.getMetric().equals(PayloadUtils.ALERT_STATUS_METRIC)).forEach(measure -> {
            if (measure.getMetric().equals(PayloadUtils.ALERT_STATUS_METRIC)) {
                Fact fact = new Fact();
                fact.setName("Previous ".concat(measure.getDescription()));
                fact.setValue(appendMetricCondition( measure,true));
                facts.add(fact);
            } else {
                Fact codeFact = new Fact();
                codeFact.setName(measure.getDescription());
                codeFact.setValue(appendMetricCondition(measure,false));
                if (!response.isFirstScan()){
                    Fact differenceFact = new Fact();
                    differenceFact.setName("Previous ".concat(measure.getDescription()));
                    differenceFact.setValue(appendMetricCondition(measure,true));
                    facts.add(differenceFact);
                }
                facts.add(codeFact);
            }
        });
        section.setFacts(facts);

    }

    /**
     * Append single metric condition.
     */

    private String appendMetricCondition(MeasureDto measureDto,boolean difference) {
        String result ;
        if (measureDto.getType().equals(Type.LEVEL)) {
            result = String.format(PayloadUtils.HTML_ELEMENT_WITH_COLOR, measureDto.getColor(), measureDto.getDifference());
        } else{
            if (difference){
                result= String.format(PayloadUtils.HTML_ELEMENT_WITH_COLOR, measureDto.getColor(), measureDto.getDifference());
            }else{
                result= String.format(PayloadUtils.HTML_ELEMENT_WITH_COLOR, Color.BLACK.getCssStyle(), measureDto.getActualValue());

            }
        }
        return result;
    }


    /**
     * Append show button for webhook message .
     */

    private void appendAction(Payload message) {
        Action action = new Action();
        action.setType(PayloadUtils.OPEN_URI);
        action.setName("Show More");
        ActionTarget target = new ActionTarget();
        target.setOs("default");
        target.setUri(baseUrl.concat("/dashboard?id=".concat(projectId)));
        List<ActionTarget> targets = new ArrayList<>();
        targets.add(target);
        action.setTargets(targets);
        List<Action> potentialAction = new ArrayList<>();
        potentialAction.add(action);
        message.setPotentialAction(potentialAction);
    }

    private void assertNotNull(Object object, String objectName) {
        if (object == null) {
            throw new IllegalArgumentException("[Assertion failed] - " + objectName + " argument is required; it must not be null");
        }
    }
}
