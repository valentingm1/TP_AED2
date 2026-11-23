package aed.tp2;

public class Promocion {
        boolean esPromo; //Si existe una promoción en esta posición del trie, es true
        double factor;
        boolean conTope; //Si es true, la promocion tiene tope y se tiene en cuenta el valor de tope
        double tope;
        Promocion[] hijos;
        Promocion padre;

            Promocion() {
                hijos = new Promocion[58]; //Mayusculas, minusculas y los caracteres entre ambas. Sin ñ ni acentos
                esPromo = false;
                factor = 0;
                conTope = false;
                tope = 0;
                padre = null;
            }
}