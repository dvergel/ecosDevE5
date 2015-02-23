/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ecos.ecosdeve5.model;

import com.ecos.ecosdeve5.exceptions.ExceptionApp;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dev
 */
public class CalcularSimpsonRule {

    private List<BigDecimal> list = new ArrayList<BigDecimal>();
    private BigDecimal w = BigDecimal.ZERO;
    private BigDecimal e = BigDecimal.ZERO;
    private BigDecimal num_seg = BigDecimal.ZERO;
    private BigDecimal dof = BigDecimal.ZERO;
    private BigDecimal x = BigDecimal.ZERO;
    private BigDecimal p = BigDecimal.ZERO;
    private BigDecimal resultado = BigDecimal.ZERO;
    private BigDecimal fXestatico = BigDecimal.ZERO;
    private List<BigDecimal> fX = new ArrayList<BigDecimal>();
    private List<CalcularSimpsonRule> intentos = new ArrayList<CalcularSimpsonRule>();

    public CalcularSimpsonRule(BigDecimal e, BigDecimal x, BigDecimal num_seg, BigDecimal dof) {
        this.w = x.divide(num_seg, MathContext.DECIMAL64);
        this.x = x;
        this.e = e;
        this.num_seg = num_seg;
        this.dof = dof;
        this.intentos.clear();
        this.fX.clear();
        this.list.clear();
    }

    public void calcularP() throws Exception {
        try {
            calcularLista();
            calcularFdeX();
            BigDecimal formula1 = calcularSumaVector(0, 1, new BigDecimal(1));
            BigDecimal formula2 = calcularSumaVector(1, getNum_seg().intValue(), new BigDecimal(4));
            BigDecimal formula3 = calcularSumaVector(2, getNum_seg().subtract(new BigDecimal(1)).intValue(), new BigDecimal(2));
            BigDecimal formula4 = calcularSumaVector(getNum_seg().intValue(), getNum_seg().add(new BigDecimal(1)).intValue(), new BigDecimal(1));
            System.out.println("f1 :"+formula1);
            System.out.println("f2 :"+formula2);
            System.out.println("f3 :"+formula3);
            System.out.println("f4 :"+formula4);
            setP(formula1.add(formula2).add(formula3).add(formula4));
        } catch (Exception ex) {
            throw new ExceptionApp("Error al Calcular P :" + ex.getMessage());
        }
    }
    
    public void calcularLista(){
        getList().add(BigDecimal.ZERO);
        BigDecimal acumulador = getW();
        for (int index = 1; index <= num_seg.intValue(); index++) {
            getList().add(acumulador.setScale(5, RoundingMode.HALF_UP));
            acumulador = acumulador.add(w);
        }
    }

    public void calcular() throws Exception {
        try {
            CalcularSimpsonRule intento = new CalcularSimpsonRule(this.e, this.x, this.num_seg, this.dof);
            BigDecimal acumulador_num_seg = BigDecimal.ZERO;
            intento.calcularP();
            System.out.println("p :"+intento.getP());
            getIntentos().add(intento);
            do {
                acumulador_num_seg = acumulador_num_seg.add(this.num_seg.multiply(new BigDecimal(2)));
                intento = new CalcularSimpsonRule(this.e, this.x, acumulador_num_seg, this.dof);
                intento.calcularP();
                System.out.println("p :"+intento.getP());
                getIntentos().add(intento);
                System.out.println("intento ant"+intentos.get(intentos.size() - 2).getP());
                System.out.println("intento act"+intentos.get(intentos.size()-1).getP());
                System.out.println("intento resta"+intentos.get(intentos.size() - 2).getP().subtract(intentos.get(intentos.size()-1).getP()).abs().setScale(5, RoundingMode.HALF_UP));
                System.out.println("intento e"+getE().setScale(5, RoundingMode.HALF_UP));
            } while (intentos.get(intentos.size() - 2).getP().subtract(intentos.get(intentos.size()-1).getP()).abs().setScale(5, RoundingMode.HALF_UP).compareTo(getE().setScale(5, RoundingMode.HALF_UP)) >= 0);
            setResultado(intentos.get(intentos.size()-1).getP());
        } catch (Exception ex) {
            throw new ExceptionApp("Error al Calcular la variables:" + ex.getMessage());
        }
    }

    public BigDecimal calcularSumaVector(int inicio, int fin, BigDecimal multiplo) throws Exception {
        try {
            BigDecimal acumulador = BigDecimal.ZERO;
            for (int index = inicio; index < fin; index = index + 2) {
                acumulador = acumulador.add(getW().divide(new BigDecimal(3), MathContext.DECIMAL64).multiply(multiplo.multiply(getfX().get(index))).setScale(5, RoundingMode.HALF_UP));
            }
            return acumulador;
        } catch (Exception ex) {
            throw new ExceptionApp("Error al Calcular la suma del vector :" + ex.getMessage());
        }
    }

    public void calcularFdeXEstatico() throws Exception {
        try {
            BigDecimal formula1 = getDof().add(BigDecimal.ONE).divide(new BigDecimal(2), MathContext.DECIMAL64);
            BigDecimal formula2 = new BigDecimal(Math.sqrt(getDof().multiply(new BigDecimal(Math.PI)).doubleValue()));
            BigDecimal formula3 = getDof().divide(new BigDecimal(2), MathContext.DECIMAL64);
            if (formula1.doubleValue() % 1 == 0) {
                formula1 = calcularGammaEntero(formula1);
            } else {
                formula1 = calcularGammaDecimal(formula1);
            }
            if (formula3.doubleValue() % 1 == 0) {
                formula3 = calcularGammaEntero(formula3);
            } else {
                formula3 = calcularGammaDecimal(formula3);
            }
            setfXestatico(formula1.divide(formula2.multiply(formula3), MathContext.DECIMAL64));
        } catch (Exception ex) {
            throw new ExceptionApp("Error al Calcular el valor estatico de f(x) :" + ex.getMessage());
        }
    }

    public void calcularFdeX() throws Exception {
        try {
            calcularFdeXEstatico();
            System.out.println("f(x) estatico :"+getfXestatico());
            BigDecimal formula1;
            BigDecimal formula2;
            for (BigDecimal x : getList()) {
                formula1 = BigDecimal.ONE.add(x.pow(2).divide(dof, MathContext.DECIMAL64));
                formula2 = getDof().add(BigDecimal.ONE).divide(new BigDecimal(2), MathContext.DECIMAL64).multiply(new BigDecimal(-1));
                System.out.println("f(x) :"+new BigDecimal(Math.pow(formula1.doubleValue(), formula2.doubleValue())).multiply(getfXestatico()).setScale(5, RoundingMode.HALF_UP));
                getfX().add(new BigDecimal(Math.pow(formula1.doubleValue(), formula2.doubleValue())).multiply(getfXestatico()).setScale(5, RoundingMode.HALF_UP));
            }
        } catch (Exception ex) {
            throw new ExceptionApp("Error al Calcular f(x) :" + ex.getMessage());
        }
    }

    public BigDecimal calcularGammaEntero(BigDecimal xi) throws Exception {
        try {
            BigDecimal gamma = BigDecimal.ONE;
            for (int index = 2; index <= xi.intValue() - 1; index++) {
                gamma = gamma.multiply(new BigDecimal(index));
            }
            return gamma;
        } catch (Exception ex) {
            throw new ExceptionApp("Error al Calcular el valor de gamma cuando es un entero :" + ex.getMessage());
        }
    }

    public BigDecimal calcularGammaDecimal(BigDecimal xi) throws Exception {
        try {
            BigDecimal gamma = BigDecimal.ONE;
            BigDecimal xiMultiplo = xi;
            do {
                xiMultiplo = xiMultiplo.subtract(new BigDecimal(1));
                gamma = gamma.multiply(xiMultiplo);
            } while (xiMultiplo.subtract(new BigDecimal(1)).compareTo(new BigDecimal(0))>=0);
            gamma = gamma.multiply(new BigDecimal(Math.sqrt(Math.PI)));
            return gamma;
        } catch (Exception ex) {
            throw new ExceptionApp("Error al Calcular el valor de gamma cuando es un decimal :" + ex.getMessage());
        }
    }

    public BigDecimal getP() {
        return p;
    }

    public void setP(BigDecimal p) {
        this.p = p.setScale(5, RoundingMode.HALF_UP);
    }

    public List<BigDecimal> getList() {
        return list;
    }

    public void setList(List<BigDecimal> list) {
        this.list = list;
    }

    public BigDecimal getW() {
        return w;
    }

    public void setW(BigDecimal w) {
        this.w = w.setScale(5, RoundingMode.HALF_UP);
    }

    public BigDecimal getE() {
        return e;
    }

    public void setE(BigDecimal e) {
        this.e = e.setScale(5, RoundingMode.HALF_UP);
    }

    public BigDecimal getDof() {
        return dof;
    }

    public void setDof(BigDecimal dof) {
        this.dof = dof.setScale(5, RoundingMode.HALF_UP);
    }

    public BigDecimal getX() {
        return x;
    }

    public void setX(BigDecimal x) {
        this.x = x.setScale(5, RoundingMode.HALF_UP);
    }

    public BigDecimal getfXestatico() {
        return fXestatico;
    }

    public void setfXestatico(BigDecimal fXestatico) {
        this.fXestatico = fXestatico;
    }

    public List<BigDecimal> getfX() {
        return fX;
    }

    public void setfX(List<BigDecimal> fX) {
        this.fX = fX;
    }

    public BigDecimal getNum_seg() {
        return num_seg;
    }

    public void setNum_seg(BigDecimal num_seg) {
        this.num_seg = num_seg.setScale(5, RoundingMode.HALF_UP);
    }

    public List<CalcularSimpsonRule> getIntentos() {
        return intentos;
    }

    public void setIntentos(List<CalcularSimpsonRule> intentos) {
        this.intentos = intentos;
    }

    public BigDecimal getResultado() {
        return resultado;
    }

    public void setResultado(BigDecimal resultado) {
        this.resultado = resultado.setScale(5, RoundingMode.HALF_UP);
    }
}
