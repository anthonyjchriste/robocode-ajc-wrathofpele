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
 * An acceptance test for WrathOfPele versus SpinBot.
 * 
 * This class is based off of Philip Johnson's DaCruzer test class.
 * https://github.com/philipmjohnson/robocode-pmj-dacruzer/tree/quality-assurance
 * 
 * @author Anthony Christe
 */
public class TestWrathOfPeleVersusSpinBot extends RobotTestBed {
  /**
   * Specifies that SpinBot and WrathOfPele are to be matched up in this test case.
   * 
   * @return The comma-delimited list of robots in this match.
   */
  @Override 
  public String getRobotNames() {
    return "sample.SpinBot,ajc.WrathOfPele";
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
   * The actual test, which asserts that WrathOfPele has won 60% of rounds versus SpinBot.
   * 
   * @param event Details about the completed battle.
   */
  @Override
  public void onBattleCompleted(BattleCompletedEvent event) {
    // Return the results in order of getRobotNames.
    BattleResults[] battleResults = event.getIndexedResults();
    // Sanity check that results[0] is WrathOfPele
    BattleResults wrathOfPeleResults = battleResults[0];
    String robotName = wrathOfPeleResults.getTeamLeaderName();
    assertEquals("Check that results[1] is WrathOfPele", "ajc.WrathOfPele*", robotName);

    // Check to make sure WrathOfPele wins 60% percent of the rounds.
    assertTrue("Check WrathOfPele winner", 
        (double) wrathOfPeleResults.getFirsts() / (double) getNumRounds() >= .60);
  }
}
