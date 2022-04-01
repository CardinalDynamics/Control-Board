// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.PneumaticsControlModule;
import edu.wpi.first.networktables.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creating this project, you must also update the
 * build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  // This assigns the Cross The Road Electronics Pneumatics Control Module to an
  // easier to use variable.
  private final PneumaticsModuleType PCM = PneumaticsModuleType.CTREPCM;

  /**
   * Motor Controllers:
   * 1 Spark MAX, ran through PWM.
   * 1 Talon SRX, ran through CAN.
   */

  private final TalonSRX talon = new TalonSRX(0);

  private final PWMSparkMax sparkMax = new PWMSparkMax(0);

  /**
   * Pneumatics Objects:
   * 1 Double Solenoid, used for pneumatic pistons.
   * 1 Compressor, used to access the onboard compressor.
   */
  private final PneumaticsControlModule pneumatics = new PneumaticsControlModule(0);

  private final DoubleSolenoid solenoid = new DoubleSolenoid(PCM, 0, 0);

  private final Compressor compressor = new Compressor(0, PCM);

  // Construct the Cross The Road Electronics Power Distributon Panel for access
  // within Driver Station.
  private final PowerDistribution powerPanel = new PowerDistribution(0, ModuleType.kCTRE);

  // Autonomous Settings
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";

  // Other Things
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  // private final DifferentialDrive m_robotDrive = new
  // DifferentialDrive(m_leftMotor, m_rightMotor);
  private final XboxController m_controller = new XboxController(0);
  // private DifferentialDrive m_myRobot;

  boolean enabled = compressor.enabled();
  boolean pressureSwitch = compressor.getPressureSwitchValue();
  double compressorCurrent = compressor.getCurrent();

  /**
   * This function is run when the robot is first started up and should be used
   * for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    compressor.enableDigital();
    CameraServer.startAutomaticCapture("Camera 1", 0);
    CameraServer.startAutomaticCapture("Camera 2", 1);
    pneumatics.enableCompressorDigital();
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(1);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and
   * test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("Uptime", Timer.getFPGATimestamp());
    SmartDashboard.putNumber("Total Current", powerPanel.getTotalCurrent());
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different
   * autonomous modes using the dashboard. The sendable chooser code works with
   * the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the
   * chooser code and
   * uncomment the getString line to get the auto name from the text box below the
   * Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional comparisons to the
   * switch structure
   * below with additional strings. If using the SendableChooser make sure to add
   * them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    // Set the Spark MAX's speed to the position of the left stick,
    // and the Talon SRX's speed to the position of the right stick.
    sparkMax.set(m_controller.getLeftY());
    talon.set(ControlMode.PercentOutput, m_controller.getRightY());

  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
  }

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {
  }

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {
    // setSafetyEnabled( False);
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
  }

}