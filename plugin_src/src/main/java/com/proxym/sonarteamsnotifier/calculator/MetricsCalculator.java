package com.proxym.sonarteamsnotifier.calculator;


import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.proxym.sonarteamsnotifier.constants.Constants;
import com.proxym.sonarteamsnotifier.exceptions.InitialMetricsFileNotFound;
import com.proxym.sonarteamsnotifier.exceptions.MetricNotFoundException;
import com.proxym.sonarteamsnotifier.jackson.ObjectMapperConfigurer;
import com.proxym.sonarteamsnotifier.model.History;
import com.proxym.sonarteamsnotifier.model.Measure;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MetricsCalculator {
    protected static final Logger LOGGER = Logger.getLogger(MetricsCalculator.class.getCanonicalName());

    private static final List<MetricDetails> metrics;
    private static final String NUMBERS_REGEX = "\\d+?\\.\\d+";

    static {
        try {
            List<?> metricObjects = new Gson().fromJson(JsonParser.parseReader(new FileReader("plugin_src/src/main/resources/metrics.json")), List.class);
            metrics = metricObjects.stream().map(obj -> ObjectMapperConfigurer.convertInstanceOfObject(obj, MetricDetails.class)).collect(Collectors.toList());
        } catch (FileNotFoundException e) {
            throw new InitialMetricsFileNotFound("Verify existence of metrics file!");
        }
    }

    public List<MeasureDto> calculate(List<Measure> measures) {
        int size = measures.get(0).getHistory().size();
       return measures.stream().filter(measure -> !measure.getHistory().isEmpty()).map(measure -> {
                    MeasureDto measureDto = new MeasureDto();
                    MetricDetails metricDetails = metrics.stream().filter(metricDetail -> metricDetail.getKey().equals(measure.getMetric())).findFirst().orElseThrow(MetricNotFoundException::new);
                    measureDto.setMetric(measure.getMetric());
                    measureDto.setDescription(measureDto.getDescription());
                    measureDto.setType(metricDetails.getType());
                    List<History> histories = measure.getHistory();
                    String recentValue = histories.get(0).getValue();
                    Optional<String> previousValue = Optional.empty();
                    double difference=0;
                    String measureDifference="";
                    if (size > 1) {
                        previousValue = Optional.of(histories.get(1).getValue());
                    }
                    if (isNumber(recentValue)) {
                        difference = convert(recentValue) - convert(previousValue.orElse("0"));
                        measureDifference = Double.toString(difference);
                    }
                    if (metricDetails.getType().equals(Type.PERCENT)) {
                    recentValue +="%";
                    measureDifference+="%";
                    }
                    measureDto.setActualValue(recentValue);
                    measureDto.setDifference(measureDifference);
                    assignColor(measureDto,difference, metricDetails.higherValuesAreBetter);
                return measureDto;
                }


        ).collect(Collectors.toList());
    }
    public static boolean isNumber(String string) {
        return string.matches(NUMBERS_REGEX);
    }
    private static void assignColor(MeasureDto measureDto,double difference,boolean higherValuesAreBetter){
        if (measureDto.getMetric().equals("alert_status")){
            measureDto.setOrder(100);
            if (measureDto.getActualValue().equals("ERROR")){
                measureDto.setColor(Color.RED.cssStyle);
            }else{
                measureDto.setColor(Color.GREEN.cssStyle);
            }
        }else if(difference < 0 && !higherValuesAreBetter){
            measureDto.setColor(Color.GREEN.cssStyle);
            measureDto.setOrder(10);
        }else if (difference == 0 ){
            measureDto.setColor(Color.BLACK.cssStyle);
            measureDto.setOrder(5);
        }else{
            measureDto.setOrder(0);
            measureDto.setColor(Color.RED.cssStyle);
        }
    }

    private static Double convert(String value) {
        Double number = null;
        try {
            number = Double.valueOf(value);
        } catch (Exception ex) {
            LOGGER.fine("measure value is not a double");
        }
        return number;
    }


    @Getter
    @Setter
    static class MetricDetails {
        private String key;
        private boolean higherValuesAreBetter;
        private Type type;
        private String description;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    static class MeasureDto {
        String metric;
        String description;
        Type type;
        String difference;
        String actualValue;
        String color;
        Integer order;

    }

    enum Type {
        DATA, INT, PERCENT, RATING, WORK_DUR, LEVEL
    }

    enum Color {
        GREEN(Constants.GREEN_COLOR, "green"), RED(Constants.RED_COLOR, "red"), BLACK("000000","black");

        Color(String reference, String cssStyle) {
            this.reference = reference;
            this.cssStyle = cssStyle;
        }

        private String cssStyle;
        private String reference;
    }


}
