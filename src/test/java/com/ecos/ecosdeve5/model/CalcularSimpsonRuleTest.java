/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ecos.ecosdeve5.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import junit.framework.TestCase;

/**
 *
 * @author Dev
 */
public class CalcularSimpsonRuleTest extends TestCase {

    public CalcularSimpsonRuleTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    
    /**
     * Test of testCalcularValoresDefecto method, of class CalcularSimpsonRule.
     */
    public void testCalcularValoresDefecto() throws Exception {
        BigDecimal expResult = new BigDecimal(0.35005).setScale(5, RoundingMode.HALF_UP);
        CalcularSimpsonRule ejercicio = new CalcularSimpsonRule(new BigDecimal(0.00001).setScale(5, RoundingMode.HALF_UP), new BigDecimal(1.1).setScale(1, RoundingMode.HALF_UP), new BigDecimal(10), new BigDecimal(9));
        ejercicio.calcular();
        assertEquals(expResult, ejercicio.getResultado());
        ejercicio = new CalcularSimpsonRule(new BigDecimal(0.00001).setScale(5, RoundingMode.HALF_UP), new BigDecimal(1.1812).setScale(4, RoundingMode.HALF_UP), new BigDecimal(10), new BigDecimal(10));
        ejercicio.calcular();
        expResult = new BigDecimal(0.36757).setScale(5, RoundingMode.HALF_UP);
        assertEquals(expResult, ejercicio.getResultado());
        ejercicio = new CalcularSimpsonRule(new BigDecimal(0.00001).setScale(5, RoundingMode.HALF_UP), new BigDecimal(2.750).setScale(3, RoundingMode.HALF_UP), new BigDecimal(10), new BigDecimal(30));
        ejercicio.calcular();
        expResult = new BigDecimal(0.49500).setScale(5, RoundingMode.HALF_UP);
        assertEquals(expResult, ejercicio.getResultado());
    }
}
