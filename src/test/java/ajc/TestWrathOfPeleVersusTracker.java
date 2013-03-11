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
        (double) wrathOfPeleResults.getFirsts() / (double) getNumRounds() >= .70);
  }
}
