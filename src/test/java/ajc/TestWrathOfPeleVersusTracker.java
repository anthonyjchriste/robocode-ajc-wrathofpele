package ajc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import robocode.BattleResults;
import robocode.control.events.BattleCompletedEvent;
import robocode.control.testing.RobotTestBed;

/**
 * An acceptance test for WrathOfPele versus Tracker.
 * 
 * This class is based off of Philip Johnson's DaCruzer test class.
 * https://github.com/philipmjohnson/robocode-pmj-dacruzer/tree/quality-assurance
 * 
 * @author Anthony Christe
 */
public class TestWrathOfPeleVersusTracker extends RobotTestBed {
  /**
   * Specifies that Tracker and WrathOfPele are to be matched up in this test case.
   * 
   * @return The comma-delimited list of robots in this match.
   */
  @Override
  public String getRobotNames() {
    return "sample.Tracker,ajc.WrathOfPele";
  }

  /**
   * This test runs for 10 rounds.
   * 
   * @return The number of rounds.
   */
  @Override
  public int getNumRounds() {
    return 10;
  }

  /**
   * The actual test, which asserts that WrathOfPele has won 70% of rounds against Tracker.
   * 
   * @param event Details about the completed battle.
   */
  @Override
  public void onBattleCompleted(BattleCompletedEvent event) {
    // Return the results in order of getRobotNames.
    BattleResults[] battleResults = event.getIndexedResults();
    // Sanity check that results[0] is WrathOfPele.
    BattleResults wrathOfPeleResults = battleResults[0];
    String robotName = wrathOfPeleResults.getTeamLeaderName();
    assertEquals("Check that results[1] is WrathOfPele", "ajc.WrathOfPele*", robotName);

    // Check to make sure WrathOfPele wins 70% percent of the rounds.
    assertTrue("Check WrathOfPele winner", 
        (double) wrathOfPeleResults.getFirsts() / (double) getNumRounds() >= .70);;
  }
}
