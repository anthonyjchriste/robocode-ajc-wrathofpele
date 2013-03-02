package ajc;

import static org.junit.Assert.assertTrue;
import robocode.control.events.BattleCompletedEvent;
import robocode.control.events.TurnEndedEvent;
import robocode.control.snapshot.IBulletSnapshot;
import robocode.control.testing.RobotTestBed;

/**
 * Tests that WrathOfPele's bullet avoidance is working.
 * 
 * @author Anthony Christe
 */
public class TestWrathOfPeleBulletAvoidance extends RobotTestBed {
  private double totalPower = 0;

  /**
   * Specifies that Crazy and WrathOfPele are to be matched up in this test case.
   * 
   * @return The comma-delimited list of robots in this match.
   */
  @Override
  public String getRobotNames() {
    return "sample.Crazy,ajc.WrathOfPele";
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
   * Keep track of how the amount of power enemy expended against WrathOfPele in bullets.
   * 
   * @param event Info about the current state of the battle.
   */
  @Override
  public void onTurnEnded(TurnEndedEvent event) {
    IBulletSnapshot bullets[] = event.getTurnSnapshot().getBullets();

    for (int i = 0; i < bullets.length; i++) {
      if (bullets[i].getOwnerIndex() == 0) {
        totalPower += bullets[i].getPower();
      }
    }
  }

  /**
   * After running all matches, determine if WrathOfPele has had variability in its bullet power.
   * 
   * @param event Details about the completed battle.
   */
  @Override
  public void onBattleCompleted(BattleCompletedEvent event) {
    assertTrue("Test that enemy only hit WrathOfPele < 25% of attempted power", 
        event.getIndexedResults()[0].getBulletDamage() / totalPower < .25);
  }
}