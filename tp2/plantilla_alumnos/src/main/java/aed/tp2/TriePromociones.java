package aed.tp2;

public class TriePromociones {
    private Promocion raiz;

    public TriePromociones() {
        raiz = new Promocion();
        agregarPromocionSinTope("NoPromo", 1);
    }

    public Promocion devolvePromocion(String nombre) {
        Promocion actual = raiz;
        for (int i = 0; i < nombre.length(); i++) {
            char letra = nombre.charAt(i);
            if (actual.hijos[letra - 'A'] == null) { //Considero la posibilidad de que la promoción ya no exista
                return devolvePromocion("NoPromo"); //En caso de no existir promoción, devuelvo NoPromo, con factor 1
            }
            actual = actual.hijos[letra - 'A'];
        }
        if (actual.esPromo == false) {
            return devolvePromocion("NoPromo");
        }
        return actual;
    }


    private Promocion agregarPromocion(String nombre, double factor) { //Agregar promocion con y sin tope son prácticamente iguales. Creo un auxiliar para manejar los procesos idénticos así no tengo código repetido 
        Promocion actual = raiz;
        for (int i = 0; i < nombre.length(); i++) {
            char letra = nombre.charAt(i);
            if (actual.hijos[letra - 'A'] == null) {
                actual.hijos[letra - 'A'] = new Promocion();
                actual.hijos[letra - 'A'].padre = actual;
            }
            actual = actual.hijos[letra - 'A'];
        }
        actual.esPromo = true;
        actual.factor = factor;
        return actual;
    }


    public void agregarPromocionSinTope(String nombre, double factor) {
        Promocion actual = agregarPromocion(nombre, factor);
        actual.conTope = false;
        actual.tope = 0;
    }

    public void agregarPromocionConTope(String nombre, double factor, double tope){
        Promocion actual = agregarPromocion(nombre, factor);
        actual.conTope = true;
        actual.tope = tope;
    }

    public void eliminarPromocion(String nombre) {
        Promocion actual = raiz;
        for (int i = 0; i < nombre.length(); i++) { //Recorre el trie buscando la última letra de la promoción
            char letra = nombre.charAt(i);
            actual = actual.hijos[letra - 'A'];
        }
        actual.esPromo = false; //Desactiva la promoción. Todavía no sé si quiero borrar la rama entera, pero la promoción ya no se puede usar
        for (int i = 0; i < 58; i++) { //Revisa si la última letra de la promoción tiene un hijo. En ese caso, no se elimina nada
            if (actual.hijos[i] != null) {
                return;
            }
        }
        actual = actual.padre; //Va a la ante última letra
        for (int i = nombre.length() - 1; i > -1; i--) { //Recorre todas las letras. Actual siempre va a ser el padre de i
            for (int j = 0; j < 58; j++) { //Busca todos los posibles hijos de la letra
                if (actual.hijos[j] != null && i != (nombre.charAt(i) - 'A')) { //Si encuentra un hijo que no es i, borra i y se abandona el bucle para no borrar otras promociones
                    char letra = nombre.charAt(i);
                    actual.hijos[letra - 'A'].padre = actual;
                    actual.hijos[letra - 'A'] = null;
                    return;
                }
            }
            actual = actual.padre;
            char letra = nombre.charAt(i);
            actual.hijos[letra - 'A'] = null; //Borra el hijo y se sigue con la próxima letra
        }
    }

}