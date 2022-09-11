package com.proxym.sonarteamsnotifier.calculator;


import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.proxym.sonarteamsnotifier.constants.PayloadUtils;
import com.proxym.sonarteamsnotifier.exceptions.InitialMetricsFileNotFound;
import com.proxym.sonarteamsnotifier.exceptions.MetricNotFoundException;
import com.proxym.sonarteamsnotifier.jackson.ObjectMapperConfigurer;
import com.proxym.sonarteamsnotifier.metriccall.History;
import com.proxym.sonarteamsnotifier.metriccall.Measure;
import com.proxym.sonarteamsnotifier.metriccall.dto.CalculatorResponse;
import com.proxym.sonarteamsnotifier.metriccall.dto.Color;
import com.proxym.sonarteamsnotifier.metriccall.dto.MeasureDto;
import com.proxym.sonarteamsnotifier.metriccall.dto.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MetricsCalculator {
    private MetricsCalculator(){}
    protected static final Logger LOGGER = Logger.getLogger(MetricsCalculator.class.getCanonicalName());

    private static final List<MetricDetails> metrics;
    private static final String NUMBERS_REGEX = "\\d+?\\.\\d+";

    static {
        try {
            List<?> metricObjects = ObjectMapperConfigurer.objectMapper.readValue( MetricsCalculator.class.getResourceAsStream("/metrics.json"),List.class);
            metrics = metricObjects.stream().map(obj -> ObjectMapperConfigurer.convertInstanceOfObject(obj, MetricDetails.class)).collect(Collectors.toList());
        } catch (IOException e) {
            throw new InitialMetricsFileNotFound("Verify existence of metrics file!");
        }
    }

    public static CalculatorResponse calculate(List<Measure> measures) {
        boolean firstScan = measures.get(0).getHistory().size() > 1 ;
        return new CalculatorResponse(firstScan, measures.stream().filter(measure -> !measure.getHistory().isEmpty()).map(measure -> {
                    MeasureDto measureDto = new MeasureDto();
                    MetricDetails metricDetails = metrics.stream().filter(metricDetail -> metricDetail.getKey().equals(measure.getMetric())).findFirst().orElseThrow(MetricNotFoundException::new);
                    measureDto.setMetric(measure.getMetric());
                    measureDto.setDescription(measureDto.getDescription());
                    measureDto.setType(metricDetails.getType());
                    List<History> histories = measure.getHistory();
                    String recentValue = histories.get(0).getValue();
                    Optional<String> previousValue = Optional.empty();
                    double difference = 0;
                    String measureDifference = "";
                    if (firstScan) {
                        previousValue = Optional.of(histories.get(1).getValue());
                    }
                    if (isNumber(recentValue)) {
                        difference = convert(recentValue) - convert(previousValue.orElse("0"));
                        measureDifference = Double.toString(difference);
                    }
                    if (metricDetails.getType().equals(Type.PERCENT)) {
                        recentValue += "%";
                        measureDifference += "%";
                    }
                    measureDto.setActualValue(recentValue);
                    measureDto.setDifference(measureDifference);
                    assignColor(measureDto, difference, metricDetails.higherValuesAreBetter);
                    return measureDto;
                }


        ).sorted(Comparator.comparing(MeasureDto::getOrder)).collect(Collectors.toList()));
    }

    public static boolean isNumber(String string) {
        return string.matches(NUMBERS_REGEX);
    }

    private static void assignColor(MeasureDto measureDto, double difference, boolean higherValuesAreBetter) {
        if (measureDto.getMetric().equals("alert_status")) {
            measureDto.setOrder(100);
            if (measureDto.getActualValue().equals("ERROR")) {
                measureDto.setColor(Color.RED.getCssStyle());
            } else {
                measureDto.setColor(Color.GREEN.getCssStyle());
            }
        }else if (measureDto.getType().equals(Type.DATA)){
            measureDto.setOrder(-1);
            measureDto.setColor(Color.BLACK.getCssStyle());
        }else if (difference < 0 && !higherValuesAreBetter) {
            measureDto.setColor(Color.GREEN.getCssStyle());
            measureDto.setOrder(10);
        } else if (difference == 0) {
            measureDto.setColor(Color.BLACK.getCssStyle());
            measureDto.setOrder(5);
        } else {
            measureDto.setOrder(0);
            measureDto.setColor(Color.RED.getCssStyle());
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
}
