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
import org.junit.Test;

/**
 * Tests the static methods in the utilities class.
 * @author Anthony Christe
 *
 */
public class TestRobotUtilities {

  /**
   * Tests that passing null to the constructor throws an exception.
   */
  @SuppressWarnings("unused")
  @Test(expected = IllegalArgumentException.class)
  public void testNullConstructor() {
    RobotUtilities utils = new RobotUtilities(null);
  }
  
  /**
   * Test boundary conditions to make sure we always received the opposite angle.
   */
  @Test
  public void testOppositeAngle() {
    assertEquals(180, RobotUtilities.getOppositeAngleDegrees(0), 0.1);
    assertEquals(181, RobotUtilities.getOppositeAngleDegrees(1), 0.1);
    assertEquals(0, RobotUtilities.getOppositeAngleDegrees(180), 0.1);
    assertEquals(1, RobotUtilities.getOppositeAngleDegrees(181), 0.1);
    assertEquals(270, RobotUtilities.getOppositeAngleDegrees(90), 0.1);
    assertEquals(90, RobotUtilities.getOppositeAngleDegrees(270), 0.1);
  }

}
