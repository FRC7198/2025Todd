package frc.robot.subsystems;

import java.util.Objects;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.LEDPattern;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LEDSubsystem extends SubsystemBase {
  private static final int kPort = 0;
  private static final int kLength = 60;

  private final AddressableLED m_led;
  private final AddressableLEDBuffer m_buffer;

  // Create an LED pattern that sets the entire strip to solid red
  LEDPattern red = LEDPattern.solid(Color.kRed);
  LEDPattern blue = LEDPattern.solid(Color.kGreen); //wires are swapped so green is actually blue
  private Alliance currentAlliance;

  // https://docs.wpilib.org/en/stable/docs/software/hardware-apis/misc/addressable-leds.html
  public LEDSubsystem() {
    m_led = new AddressableLED(9);
    m_buffer = new AddressableLEDBuffer(kLength);
    m_led.setLength(kLength);
    m_led.start();

    checkAllianceColors(DriverStation.getAlliance().orElse(Alliance.Red));
    // Set the default command to turn the strip off, otherwise the last colors
    // written by
    // the last command to run will continue to be displayed.
    // Note: Other default patterns could be used instead!
    // setDefaultCommand(runPattern(LEDPattern.GradientType.kContinuous, Color.kRed,
    // Color.kBlue).withName("Off"));
  }

  @Override
  public void periodic() {


    // Periodically send the latest LED color data to the LED strip for it to
    // display
    m_led.setData(m_buffer);
  }

  private void checkAllianceColors(Alliance alliance) {

    if(Objects.nonNull(alliance) && alliance == currentAlliance) {
      return; //Alliance hasn't changed no need to set the colors;
    } else {
      currentAlliance = alliance;

      // Apply the LED pattern to the data buffer
      if (alliance == Alliance.Red) {
        red.applyTo(m_buffer);
      } else {
        blue.applyTo(m_buffer);
      }  
    }

  }

  /**
   * Creates a command that runs a pattern on the entire LED strip.
   *
   * @param pattern the LED pattern to run
   */
  public Command runPattern(LEDPattern pattern) {
    return run(() -> pattern.applyTo(m_buffer));
  }
}