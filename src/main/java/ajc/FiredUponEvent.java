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

import java.util.ArrayList;
import java.util.Map;
import robocode.Condition;

/**
 * This event is fired when our detects an energy drop in an enemy robot being scanned.
 *
 * @author Anthony Christe
 * 
 */
public class FiredUponEvent extends Condition {
  /**
   * Reference to scanned robots with energy history. 
   */
  private Map<String, ArrayList<Double>> scannedRobots;

  /**
   * Adds the FiredUponEvent to the event queue with the relevant robot histories.
   * 
   * @param scannedRobots Map containing list of scanned robots and energy histories.
   */
  public FiredUponEvent(Map<String, ArrayList<Double>> scannedRobots) {
    super("FiredUponEvent");
    this.scannedRobots = scannedRobots;
  }

  /**
   * Checks all scanned robots to see if any of their energies have lowered since the last time 
   * they were scanned.
   * 
   * This method compares the last two scanned energies of a robot to determine if there has been
   * an energy drop. 
   * 
   * We also attempt to determine if the energy drop was due to one of our bullets hitting the 
   * enemy.
   * 
   * @return <i>true</i> if the energy dropped in a scanned robot and <i> false otherwise.
   */
  @Override
  public boolean test() {
    int size;
    ArrayList<Double> energies;

    for (String enemy : scannedRobots.keySet()) {
      energies = scannedRobots.get(enemy);
      size = energies.size();

      if (size > 2 && ((energies.get(size - 2) - energies.get(size - 1)) > 0)) {
        energies.clear();
        return true;
      }
    }
    return false;
  }

}
