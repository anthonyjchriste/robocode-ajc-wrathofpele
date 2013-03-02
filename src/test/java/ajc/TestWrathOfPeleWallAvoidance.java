package ajc;

import static org.junit.Assert.assertTrue;
import robocode.control.events.BattleCompletedEvent;
import robocode.control.events.TurnEndedEvent;
import robocode.control.snapshot.IRobotSnapshot;
import robocode.control.testing.RobotTestBed;

/**
 * Tests that WrathOfPele avoids walls.
 * 
 * @author Anthony Christe
 */
public class TestWrathOfPeleWallAvoidance extends RobotTestBed {
  private int turnsHitWall = 0;
  private int totalTurns = 0;

  /**
   * Specifies that SittingDuck and WrathOfPele are to be matched up in this test case.
   * 
   * @return The comma-delimited list of robots in this match.
   */
  @Override
  public String getRobotNames() {
    return "sample.SittingDuck,ajc.WrathOfPele";
  }

  /**
   * This test runs for 20 rounds.
   * 
   * @return The number of rounds.
   */
  @Override
  public int getNumRounds() {
    return 20;
  }

  /**
   * Count the number of terms WrathOfPele hits a wall.
   * 
   * @param event Info about the current state of the battle.
   */
  @Override
  public void onTurnEnded(TurnEndedEvent event) {
    // All active bullets belong to WrathOfPele since SittingDuck does not fire.
    IRobotSnapshot robot = event.getTurnSnapshot().getRobots()[1];
    totalTurns++;
    if (robot.getState().isHitWall()) {
      turnsHitWall++;
    }
  }

  /**
   * Determine if the number of turns that WrathOfPele hits a wall / the total number of total 
   * terms is less than .01%.
   * 
   * @param event Details about the completed battle.
   */
  @Override
  public void onBattleCompleted(BattleCompletedEvent event) {
    assertTrue("WrathOfPele hit the wall < .01% of the time", 
        (float) turnsHitWall / (float) totalTurns < .01);
    
  }
}