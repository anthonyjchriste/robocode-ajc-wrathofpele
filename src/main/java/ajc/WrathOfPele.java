/*
 * This file is part of WrathOfPele.
 *
 * WrathOfPele is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WrathOfPele is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WrathOfPele.  If not, see <http://www.gnu.org/licenses/>.
 */

package ajc;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import robocode.AdvancedRobot;
import robocode.BulletHitEvent;
import robocode.Condition;
import robocode.CustomEvent;
import robocode.HitRobotEvent;
import robocode.Rules;
import robocode.ScannedRobotEvent;
import robocode.WinEvent;
import robocode.util.Utils;
import ajc.WallProximityEvent.EventTrigger;

/**
 * A competitive Robocode robot that uses stop-and-go for defense, circular movements, and
 * back-as-front capabilities.
 * 
 * This robot changes which direction it thinks is forward. The robot's forward starts out as
 * normal, but it can then switch so that it's forward is from the back of the robot. This allows
 * the robot to switch directions, but always move from the "front" (even if it's moving with the
 * back of the robot first). Throughout this class, this robot's forward refers to the direction it
 * thinks is front.
 * 
 * @author Anthony Christe
 *
 */
public final class WrathOfPele extends AdvancedRobot {
  /**
   * Instance of utilities class associated with this robot.
   */
  private RobotUtilities utils;

  /**
   * private RobotUtilities utils = new RobotUtilities(this); Indicates what this robot believes if
   * forward in relation to the front or back of its body.
   * 
   * A value of true indicates that this robot believes that the front of its body is facing
   * forward. A value of false indicates that this robot believes that the back of its body is
   * facing forward.
   */
  private boolean frontAsForward = true;

  /**
   * Provides random numbers for random movements by this robot.
   */
  private Random random;

  /**
   * Stores all scanned robots along with their energy history.
   */
  private HashMap<String, ArrayList<Double>> scannedRobots;

  /**
   * Gets set by the wallProximityEvent.
   * 
   * This allows us to disable other events while we're too close to a wall.
   */
  private boolean nearWall = false;

  /**
   * Keeps track of the damage dealth by this robot's last bullet on an enemy robot.
   */
  private Double lastBulletHitEnergy = 0.0;
  
  /**
   * Setup, scan for, and attack enemy robots.
   */
  @Override
  public void run() {
    init();
    this.setAdjustRadarForGunTurn(true);

    while (true) {
      // If we've slowed down and not near a wall, move randomly
      if (this.getVelocity() < 3 && !nearWall) {
        this.moveInArc();
      }

      // The robot is "stuck" on a wall
      if (nearWall && this.getVelocity() == 0) {
        this.switchForward();
        this.moveInArc();
        //ahead(100);
      }
      // Scan for other robots
      this.setTurnRadarLeft(360);
      this.execute();
    }
  }

  /**
   * Return the current heading of the robot taking into account if this robot's forward.
   * 
   * If the robot's forward is front, simply return the heading. If the robot's forward is back,
   * then return the opposite angle of the heading.
   * 
   * @return The heading in degrees from this robot's forward.
   */
  @Override
  public double getHeading() {
    // Forward is front
    if (frontAsForward) {
      return super.getHeading();
    }
    // Forward is back
    return RobotUtilities.getOppositeAngleDegrees(super.getHeading());
  }

  /**
   * If the robot's forward is front, move ahead, otherwise move backwards.
   * 
   * @param distance {@inheritDoc}
   */
  @Override
  public void ahead(double distance) {
    // Forward is front
    if (frontAsForward) {
      super.ahead(distance);
    }
    // Forward is back
    else {
      super.back(distance);
    }
  }

  /**
   * If the robot's forward is front, move ahead, otherwise move backwards.
   * 
   * @param {@inheritDoc}
   */
  @Override
  public void setAhead(double distance) {
    // Forward is front
    if (frontAsForward) {
      super.setAhead(distance);
    }
    // Forward is back
    else {
      super.setBack(distance);
    }
  }

  /**
   * Performs basic robot initialization.
   * 
   * Sets the team colors, manages debugging, and sets up custom events.
   */
  private void init() {
    this.utils = new RobotUtilities(this);
    this.scannedRobots = new HashMap<>();
    this.random = new Random(System.currentTimeMillis());
    utils.setTeamColors();
    this.addCustomEvent(new WallProximityEvent(this,
        WallProximityEvent.EventTrigger.WALL_PROXIMITY, 40.0));
    this.addCustomEvent(new FiredUponEvent(this.scannedRobots, this.lastBulletHitEnergy));
  }

  /**
   * If this robot's forward is front, then switch to back, otherwise, switch to front.
   */
  private void switchForward() {
    this.frontAsForward = !this.frontAsForward;
  }

  /**
   * Causes this robot to move in arcs with a set distance.
   * 
   * @param distance The distance this robot will move during the arc.
   */
  private void moveInArc(double distance) {
    // Cause robot to turn between 30 and 75 degrees to left or right
    double randomDelta = random.nextInt(45) + 30;
    randomDelta = randomDelta * (random.nextBoolean() ? 1 : -1);
    setHeading(this.getHeading() + randomDelta);
    
    // If distance is negative, then choose a random distance
    if (distance < 0) {
      setAhead(random.nextInt(300) + 100); 
    } 
    // Otherwise, use the given distance
    else {
      setAhead(distance);
    }
    
    this.execute();
  }
  
  /**
   * Causes this robot to move in arcs with a random distance.
   */
  private void moveInArc() {
    moveInArc(-1);
  }

  /**
   * Turns the robot towards a new heading using the shortest arc distance to the new heading.
   * 
   * @param heading The new heading to adjust to.
   */
  private void setHeading(double heading) {
    setTurnRight(Utils.normalRelativeAngleDegrees(heading));
  }

  /**
   * If a robot hits another robot, switch which direction is forward to move in opposite direction.
   * 
   * @param evt The event that is fired when this robot hits another robot.
   */
  @Override
  public void onHitRobot(HitRobotEvent evt) {
    this.moveInArc();
  }

  /**
   * Determines if one of our bullets has hit an enemy robot. 
   * 
   * If one of our bullets does his an enemy robot, keep track of the damage it has done so the
   * firedUpon event doesn't fire from damage energy loss to our own bullets.
   * 
   * @param evt The event that is fired when one of this robots bullets hits an enemy robot.
   */
  @Override
  public void onBulletHit(BulletHitEvent evt) {
    this.lastBulletHitEnergy = evt.getEnergy();
  }
  
  /**
   * Tracks, records energy levels of, and fires upon enemy robots.
   * 
   * @param evt The event that is generated when this robot scans another robot.
   */
  @Override
  public void onScannedRobot(ScannedRobotEvent evt) {
    if (!nearWall) {
      // Square off against enemy
      // From http://mark.random-article.com/robocode/basic_movement.html
      this.setTurnRight(evt.getBearing() + 90);

      // If we havn't scanned this robot before, add it to known robots.
      if (!this.scannedRobots.containsKey(evt.getName())) {
        this.scannedRobots.put(evt.getName(), new ArrayList<Double>());
      }

      // Store the robots energy
      this.scannedRobots.get(evt.getName()).add(evt.getEnergy());

      // Determine relative angles needed to turn radar and gun, and then turn them.
      double radarTurnAngle =
          RobotUtilities.getTargetTurnAngle(super.getHeading(), this.getRadarHeading(),
              evt.getBearing());
      double gunTurnAngle =
          RobotUtilities.getTargetTurnAngle(super.getHeading(), this.getGunHeading(),
              evt.getBearing());

      this.setTurnRadarRight(Utils.normalRelativeAngleDegrees(radarTurnAngle));
      this.setTurnGunRight(Utils.normalRelativeAngleDegrees(gunTurnAngle));
      
      // Make sure our gun is actually pointing at the robot before firing
      // Also ensure that the gun is cool before firing
      // Inspired from http://mark.random-article.com/robocode/basic_targeting.html
      if (Math.abs(this.getGunTurnRemaining()) < 5 && this.getGunHeat() == 0) {
        this.setFire(utils.getProportionalFirePower(evt.getDistance()));
      }
      this.execute();
    }
  }

  /**
   * Causes this robot to spin in circles while cycling through colors of the rainbow.
   * 
   * @param evt The event fired when this robot wins a round.
   */
  @Override
  public void onWin(WinEvent evt) {
    Color[] rainbow =
        { Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA };
    int i = 0;
    this.setTurnRight(Double.POSITIVE_INFINITY);
    this.setTurnGunLeft(Double.POSITIVE_INFINITY);
    this.setTurnRadarRight(Double.POSITIVE_INFINITY);
    while (true) {
      this.setBodyColor(rainbow[i % rainbow.length]);
      this.setGunColor(rainbow[(i + 1) % rainbow.length]);
      this.setRadarColor(rainbow[(i + 2) % rainbow.length]);
      this.setBulletColor(rainbow[(i + 3) % rainbow.length]);
      this.setFire(Rules.MAX_BULLET_POWER);
      this.execute();
      i++;
    }

  }

  /**
   * Catches all custom events and forwards the events to the correct event handler.
   * 
   * @param evt The custom event that has been fired.
   */
  public void onCustomEvent(CustomEvent evt) {
    Condition condition = evt.getCondition();

    switch (condition.getName()) {
    case "WallProximityEvent":
      onWallProximityEvent((WallProximityEvent) condition);
      break;
    case "FiredUponEvent":
      onFiredUponEvent((FiredUponEvent) condition);
      break;
    default:
      // This should not occur
      break;
    }
  }

  /**
   * This event fires when this robot is too close to one of the walls.
   * 
   * This event is added at the start of a round. If this event fires, this event is removed, and a
   * <i>SafeProximityEvent</i> is added to alert us when we're at a safe distance from the wall.
   * 
   * @param evt The event object associated with this event.
   */
  private void onWallProximityEvent(WallProximityEvent evt) {
    this.removeCustomEvent(evt);

    if (evt.getEventTrigger().equals(EventTrigger.WALL_PROXIMITY)) {
      this.nearWall = true;
      
      // Make sure we now wait on robot returning to safe distance
      this.addCustomEvent(new WallProximityEvent(this,
          WallProximityEvent.EventTrigger.SAFE_PROXIMITY, evt.getMinSafeDistance()));
      
      // Move away from the wall
      this.switchForward();
      this.ahead(evt.getMinSafeDistance() * 1.5);
     
    }
    else {
      this.nearWall = false;
      // Robot has returned to a safe distance, re-enable wall priority
      this.addCustomEvent(new WallProximityEvent(this,
          WallProximityEvent.EventTrigger.WALL_PROXIMITY, evt.getMinSafeDistance()));
    }
  }

  /**
   * This event is fired when this robot detects an energy drop in the enemy robot.
   * 
   * This event causes this robot to switch direction and move a random distance as defensive
   * movement.
   * 
   * @param evt The event object associated with this event.
   */
  private void onFiredUponEvent(FiredUponEvent evt) {
    if (!nearWall) {
      this.switchForward();
      this.moveInArc(random.nextInt(200) + 50);
      this.execute();
    }
  }
}
