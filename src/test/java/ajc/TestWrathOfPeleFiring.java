package ajc;

import static org.junit.Assert.assertTrue;
import java.util.TreeSet;
import robocode.control.events.BattleCompletedEvent;
import robocode.control.events.TurnEndedEvent;
import robocode.control.snapshot.IBulletSnapshot;
import robocode.control.testing.RobotTestBed;

/**
 * This tests that fire power is proportional to distance in WrathOfPele.
 * 
 * @author Anthony Christe
 */
public class TestWrathOfPeleFiring extends RobotTestBed {

  private TreeSet<Double> powers;

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
   * Adds all of the bullets' powers to a sorted set. This will allow us to see how many different
   * powers were used and also what the range of powers look like. 
   * 
   * @param event Info about the current state of the battle.
   */
  @Override
  public void onTurnEnded(TurnEndedEvent event) {
    // All active bullets belong to WrathOfPele since SittingDuck does not fire.
    IBulletSnapshot bullets[] = event.getTurnSnapshot().getBullets();

    for (int i = 0; i < bullets.length; i++) {
      powers.add(bullets[i].getPower());
    }
  }

  /**
   * Invoked before the test battle begins. 
   * 
   * Instantiates the powers sorted set object.
   */
  @Override
  protected void runSetup() {
    powers = new TreeSet<>();
  }

  /**
   * After running all matches, determine if WrathOfPele has had variability in its bullet power.
   * 
   * @param event Details about the completed battle.
   */
  @Override
  public void onBattleCompleted(BattleCompletedEvent event) {
    assertTrue("At least 10 different powers were used", powers.size() > 10);
    assertTrue("Assert that a range of powers were used", powers.last() - powers.first() > 2);
  }
}