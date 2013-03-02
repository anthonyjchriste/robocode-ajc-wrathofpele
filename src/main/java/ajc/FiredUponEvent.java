package ajc;

import java.util.ArrayList;
import java.util.HashMap;
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
  private HashMap<String, ArrayList<Double>> scannedRobots;

  /**
   * Adds the FiredUponEvent to the event queue with the relevant robot histories.
   * 
   * @param scannedRobots Map containing list of scanned robots and energy histories.
   */
  public FiredUponEvent(HashMap<String, ArrayList<Double>> scannedRobots) {
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
