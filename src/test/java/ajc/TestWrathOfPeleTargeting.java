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

import static org.junit.Assert.assertTrue;
import robocode.control.events.BattleCompletedEvent;
import robocode.control.events.TurnEndedEvent;
import robocode.control.snapshot.BulletState;
import robocode.control.snapshot.IBulletSnapshot;
import robocode.control.testing.RobotTestBed;

/**
 * Tests that WrathOfPele can track and hit targets.
 * 
 * @author Anthony Christe
 */
public class TestWrathOfPeleTargeting extends RobotTestBed {
  private int bulletsHitWall = 0;
  private int bulletsHitEnemy = 0;

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
   * Keep track of how many of WrathOfPele's bullets actually hit a target versus not hitting
   * a target.
   * 
   * @param event Info about the current state of the battle.
   */
  @Override
  public void onTurnEnded(TurnEndedEvent event) {
    // All active bullets belong to WrathOfPele since SittingDuck does not fire.
    IBulletSnapshot bullets[] = event.getTurnSnapshot().getBullets();
    
    for (int i = 0; i < bullets.length; i++) {
      if (bullets[i].getState().equals(BulletState.HIT_WALL)) {
        bulletsHitWall++;
      }
      if (bullets[i].getState().equals(BulletState.HIT_VICTIM)) {
        bulletsHitEnemy++;
      }
    }
  }

  /**
   * After running all matches, determine if WrathOfPele has had variability in its bullet power.
   * 
   * 
   * @param event Details about the completed battle.
   */
  @Override
  public void onBattleCompleted(BattleCompletedEvent event) {
    System.out.format("%d %d %f\n", bulletsHitEnemy, bulletsHitWall, 
        (float) bulletsHitEnemy / (float) (bulletsHitEnemy + bulletsHitWall));
    assertTrue("At least 40% of our bullets hit a target",
        ((float) bulletsHitEnemy / (float) (bulletsHitEnemy + bulletsHitWall)) > 0.40);
  }
}