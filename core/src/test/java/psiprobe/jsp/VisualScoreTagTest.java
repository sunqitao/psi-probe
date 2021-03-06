/**
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe.jsp;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class VisualScoreTagTest.
 */
public class VisualScoreTagTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(VisualScoreTag.class).loadData().test();
  }

  /**
   * Test range scan.
   */
  @Test
  public void testRangeScan() {
    // As used in appRuntimeInfo.jsp
    doTestRangeScan(8, 5, false);
    doTestRangeScan(8, 5, true);

    /*
     * As used in memory_pools.jsp, application.jsp, datasourcegroup.jsp, datasources_table.jsp,
     * resources.jsp and sysinfo.jsp
     */
    doTestRangeScan(10, 5, false);
    doTestRangeScan(10, 5, true);
  }

  /**
   * Do test range scan.
   *
   * @param fullBlocks the full blocks
   * @param partialBlocks the partial blocks
   * @param invertLoopIndexes the invert loop indexes
   */
  private static void doTestRangeScan(int fullBlocks, int partialBlocks, boolean invertLoopIndexes) {
    int value;
    int value2;
    int count = 0;
    for (int i = 0; i <= 100; i++) {
      for (int j = 0; j <= 100; j++) {
        if (invertLoopIndexes) {
          value = j;
          value2 = i;
        } else {
          value = i;
          value2 = j;
        }
        String[] split = callCalculateSuffix(value, value2, fullBlocks, partialBlocks);
        for (String suffix : split) {
          // System.out.println(split[k]);
          String[] values = suffix.split("\\+");
          if (values.length > 1) {
            value = Integer.parseInt(values[0]);
            value2 = Integer.parseInt(values[1]);
            if (value > 5 || value2 > 5) {
              count++;
              StringBuilder msg = new StringBuilder();
              msg.append("Found incorrect value ");
              msg.append(suffix);
              msg.append(". value = ");
              msg.append(invertLoopIndexes ? j : i);
              msg.append(" value2 = ");
              msg.append(invertLoopIndexes ? i : j);
              msg.append(" fullBlocks = ");
              msg.append(fullBlocks);
              msg.append(" partialBlocks = ");
              msg.append(partialBlocks);
              // System.out.println(msg.toString());
            }
          }
        }
      }
    }
    if (count > 0) {
      fail("Incorrect values were founded " + count + " times");
    }
  }

  /**
   * Call calculate suffix.
   *
   * @param value the value
   * @param value2 the value2
   * @param fullBlocks the full blocks
   * @param partialBlocks the partial blocks
   * @return the string[]
   */
  private static String[] callCalculateSuffix(int value, int value2, int fullBlocks, int partialBlocks) {
    String body = "{0} ";

    VisualScoreTag visualScoreTag = new VisualScoreTag();

    // Values are based on datasources_table.jsp
    visualScoreTag.setFullBlocks(fullBlocks);
    visualScoreTag.setPartialBlocks(partialBlocks);
    visualScoreTag.setShowEmptyBlocks(true);
    visualScoreTag.setShowA(true);
    visualScoreTag.setShowB(true);

    visualScoreTag.setValue(value);
    visualScoreTag.setValue2(value2);

    String buf = visualScoreTag.calculateSuffix(body);

    return buf.split(" ");
  }

}
