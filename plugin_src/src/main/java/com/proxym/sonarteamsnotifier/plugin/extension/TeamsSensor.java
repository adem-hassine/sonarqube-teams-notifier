package com.proxym.sonarteamsnotifier.plugin.extension;

import com.proxym.sonarteamsnotifier.plugin.constants.Constants;

import java.util.Optional;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

/**
 * Plugin to check for this plugin's scanner properties.
 */
public class TeamsSensor implements Sensor {

  /**
   * Logger.
   */
  private static final Logger LOG = Loggers.get(TeamsSensor.class);

  /**
   * The Sensor Context.
   */
  private SensorContext sensorContext;

  /**
   * Describes this sensor.
   *
   * @param descriptor The SensorDescriptor
   */
  @Override
  public void describe(SensorDescriptor descriptor) {
    descriptor.name(getClass().getName());
  }

  /**
   * Executes this sensor.
   *
   * @param context The SensorContext
   */
  @Override
  public void execute(SensorContext context) {
    this.sensorContext = context;
    checkAndAddParam(Constants.HOOK);
    checkAndAddParam(Constants.TOKEN);
    checkAndAddParam(Constants.SERVER_URL);
  }

  /**
   * Checks if a scanner parameter is set and adds it to the context if so.
   *
   * @param paramName The name of the parameter to check.
   */
  private void checkAndAddParam(String paramName) {
    Optional<String> param = sensorContext.config().get(paramName);
    if (param.isPresent()) {
      LOG.info(String.format(
          "Sonar Teams Notifier %s found.",
          paramName.substring(paramName.lastIndexOf('.') + 1)
      ));
      sensorContext.addContextProperty(paramName, param.get());
    }
  }
}
