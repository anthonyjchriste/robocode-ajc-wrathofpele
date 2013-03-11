package ajc; 

import robocode.AdvancedRobot;
import robocode.Rules;
import java.awt.Color;

/**
 * Provides trigonometric and geometric methods for robots.
 * 
 * @author Anthony Christe
 * 
 */
public class RobotUtilities {
  /**
   * Instance of robot to provide methods to.
   */
  private AdvancedRobot robot;

  /**
   * Defines the four walls of the Robocode playing area.
   */
  public static enum Wall {
    /** The top of the playing area. */
    NORTH,

    /** The right side of the playing area. */
    EAST,

    /** The bottom side of the playing area. */
    SOUTH,

    /** The left side of the playing area. */
    WEST;
  }

  /**
   * Associates this utility class with a specific robot.
   * 
   * @param robot The robot to associate with this utilities class.
   */
  public RobotUtilities(AdvancedRobot robot) {
    if (robot == null) {
      throw new IllegalArgumentException("RobotUtilities expects instance of AdvancedRobot");
    }
    this.robot = robot;
  }

  /**
   * Sets the robot's colors to the team colors.
   */
  public void setTeamColors() {
    Color bodyColor = Color.GREEN;
    Color gunColor = Color.BLUE;
    Color radarColor = Color.RED;

    robot.setColors(bodyColor, gunColor, radarColor);
  }

  /**
   * Calculates the power for firing proportional to the distance of the opponent.
   * 
   * The max distance is is calculated as max(height, width). 
   * The firing power is then linear from [0 to width of robot] pixels away being full power to the
   * max distance away being the lowest power.
   *  
   * @param distance The distance of the object from the tank.
   * @return The linear proportional power based on the enemy's distance.
   */
  public double getProportionalFirePower(double distance) {
    // If we are up against a robot, fire at full power
    if (distance <= robot.getWidth()) {
      return Rules.MAX_BULLET_POWER;
    }
  
    double maxDistance = Math.max(robot.getBattleFieldWidth(), robot.getBattleFieldHeight());
    double deltaY = Rules.MAX_BULLET_POWER - Rules.MIN_BULLET_POWER;
    double deltaX = 0 - maxDistance;
    double slope = deltaY / deltaX;
    return Rules.MAX_BULLET_POWER - Math.abs(slope * distance);
  }

  // ------------------------ Trigonometry ---------------------------------------------------------
  /**
   * Returns the opposite angle to the angle passed in between [0 - 360) degrees.
   * 
   * Returns the angle 180 degrees opposite of this angle. If the angle is 0, 180 is returned. If
   * the angle is 90, 270 is returned. If the angle is 270, 90 is returned.
   * 
   * @param angle The angle to find the opposite of.
   * @return The 180 degrees opposite of this angle.
   */
  public static double getOppositeAngleDegrees(double angle) {
    double opposite;

    if (angle < 180.0) {
      opposite = angle + 180.0;
    }
    else {
      opposite = 180.0 - angle;
    }

    return Math.abs(opposite);
  }

  /**
   * Returns the angle that an item would need to turn in-order to be facing an enemy target.
   * 
   * @param heading The heading of the robot.
   * @param itemHeading The current heading of the item.
   * @param targetBearing The bearing stored in the onScannedRobot event object.
   * @return The angle an item would need to turn in-order to be facing an enemy target.
   */
  public static double getTargetTurnAngle(double heading, double itemHeading,
      double targetBearing) {
    return heading - itemHeading + targetBearing;
  }
}
