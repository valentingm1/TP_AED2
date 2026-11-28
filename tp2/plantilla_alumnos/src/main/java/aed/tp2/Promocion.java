package aed.tp2;

public class Promocion {
    private double factor;
    private boolean conTope; //Si es true, la promocion tiene tope y se tiene en cuenta el valor de tope
    private double tope;

    Promocion(double a, boolean b, double c) { //Me gustaría darles mejores nombres, pero soo se usan una vez y no quiero terminar con algo como "factor = factor"
        factor = a;
        conTope = b;
        tope = c;
    }

    public double devovlerFactor() {
        return factor;
    }

    public boolean devolverConTope() {
        return conTope;
    }
    
    public double devovlerTope() {
        return tope;
    }

    public void bajarTope(double millas) {
        tope = tope - millas;
    }


}