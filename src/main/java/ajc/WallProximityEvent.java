package ajc;

import java.util.HashSet;
import java.util.Set;
import ajc.RobotUtilities.Wall;
import robocode.AdvancedRobot;
import robocode.Condition;

/**
 * This event fires whenever a robot is too close to one of the four walls.
 *
 * @author Anthony Christe
 * 
 */
public class WallProximityEvent extends Condition {
  /**
   * Instance of robot associated with this event.
   */
  private AdvancedRobot robot;

  /**
   * Walls that that the robot are within <i>minSafeDistance</i> of.
   */
  private Set<Wall> wallsInViolation;

  /**
   * The minimum safe distance the robot needs to maintain from a wall before this event is fired.
   */
  private double minSafeDistance;

  /**
   * Determines when this event triggers.
   * 
   * @see EventTrigger
   */
  private EventTrigger triggerOn;

  /**
   * Affect the way the WallProximityEvent fires.
   * 
   * A value of WALL_PROXIMITY will cause the WallProximityEvent to fire when the robot comes to
   * close to one of the four walls.
   * 
   * A value of SAFE_PROXIMITY will cause the WallProximityEvent to fire when the robot is within a
   * safe distance from all four walls.
   * 
   * @author Anthony Christe
   * 
   */
  public static enum EventTrigger {
    /** This trigger is triggered when the robot is too close to one of the walls. */
    WALL_PROXIMITY,

    /** This trigger is triggered when the robot is within a safe distance from all of the walls. */
    SAFE_PROXIMITY;
  }

  /**
   * Creates a WallProximityEvent with a custom <i>minSafeDistance</i>.
   * 
   * @param robot The robot associated with this event.
   * @param triggerOn The trigger enum associated with causing this event to fire.
   * @param minSafeDistance The minimum distance from a wall that will cause this event to fire (in
   * pixels).
   * @see EventTrigger
   */
  public WallProximityEvent(AdvancedRobot robot, EventTrigger triggerOn, double minSafeDistance) {
    super("WallProximityEvent");
    this.robot = robot;
    this.triggerOn = triggerOn;
    this.minSafeDistance = minSafeDistance;
    this.wallsInViolation = new HashSet<>();
  }

  /**
   * Tests whether the robot is in danger of hitting a wall or within a safe distance from the wall
   * depending on the <i>EventTrigger</i> enumeration.
   * 
   * If the <i>EventTrigger</i> enumeration is <i>WALL_PROXIMITY</i>, then this event fires if the
   * robot is too close to any of the four walls. If the <i>EventTrigger</i> enumeration is
   * <i>SAFE_PROXIMITY</i>, then this event fires when the robot is within a safe distance from all
   * four walls.
   * 
   * @return <i>true</i> if <i>EventTrigger.WALL_PROXIMITIY</i> and robot is too close to any of the
   * four walls or <i>true</i> if <i>EventTrigger.SAFE_PROXIMITIY</i> and robot is within a safe
   * distance of all four walls or <i>false</i> otherwise.
   */
  @Override
  public boolean test() {
    boolean fireEvent = false;

    this.findWallsInViolation(robot.getX(), robot.getY(), robot.getBattleFieldHeight(),
        robot.getBattleFieldWidth());

    if (this.triggerOn.equals(EventTrigger.WALL_PROXIMITY)) {
      if (this.wallsInViolation.size() > 0) {
        fireEvent = true;
      }
    }
    else {
      if (this.wallsInViolation.size() <= 0) {
        fireEvent = true;
      }
    }
    
    return fireEvent;
  }

  /**
   * Obtain the minimum safe distance from a wall before this event fires.
   * 
   * @return The minimum safe distance in pixels from the walls.
   */
  public double getMinSafeDistance() {
    return this.minSafeDistance;
  }

  /**
   * Return the trigger type of this event.
   * 
   * @return Either WALL_PROXIMITY or SAFE_PROXIMITY.
   */
  public EventTrigger getEventTrigger() {
    return this.triggerOn;
  }

  /**
   * Find all walls that robot is within an unsafe distance of.
   * 
   * @param x The x-coordinate of the robot. 
   * @param y The y-coordinate of the robot.
   * @param height The height of the playing field.
   * @param width The width of the playing field.
   */
  private void findWallsInViolation(double x, double y, double height, double width) {
    // Make sure the list is empty from the last scan.
    this.wallsInViolation.clear();

    // Left wall
    if (x < minSafeDistance) {
      this.wallsInViolation.add(Wall.WEST);
    }

    // Right wall
    if (x > (width - minSafeDistance)) {
      this.wallsInViolation.add(Wall.EAST);
    }

    // Lower wall
    if (y < minSafeDistance) {
      this.wallsInViolation.add(Wall.SOUTH);
    }

    // Upper wall
    if (y > (height - minSafeDistance)) {
      this.wallsInViolation.add(Wall.NORTH);
    }
  }

}
