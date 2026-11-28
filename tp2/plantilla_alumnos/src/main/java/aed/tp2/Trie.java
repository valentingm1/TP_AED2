package aed.tp2;

public class Trie<T> {
    private Nodo raiz; //El inicio del trie

    private class Nodo {
        T valor;
        Nodo padre;
        Nodo[] hijos;
        Boolean esPalabra;

        Nodo (T data) {
            this.valor = data;
            this.padre = null;
            this.hijos = new Nodo[58];//Todas las mayúsculas y minúsculas, sin contar acentos ni ñ
            this.esPalabra = false;
        }
    }

    public Trie() {
        raiz = new Nodo(null); //El null es el valor almacenado en el nodo, no el nodo en sí
    }

    public void agregarPalabra(String nombre, T nuevo) { //Agregar promocion con y sin tope son prácticamente iguales. Creo un auxiliar para manejar los procesos idénticos así no tengo código repetido 
        Nodo actual = raiz;
        for (int i = 0; i < nombre.length(); i++) {
            char letra = nombre.charAt(i);
            if (actual.hijos[letra - 'A'] == null) {
                actual.hijos[letra - 'A'] = new Nodo(null);
                actual.hijos[letra - 'A'].padre = actual;
            }
            actual = actual.hijos[letra - 'A'];
        }
        actual.valor = nuevo;
        actual.esPalabra = true;
    }

    public T devolverPalabra(String nombre) {
        Nodo actual = raiz;
        for (int i = 0; i < nombre.length(); i++) {
            char letra = nombre.charAt(i);
            if (actual.hijos[letra - 'A'] == null) {//Considero la posibilidad de que la palabra ya no exista
                return null; //En caso de no existir la palabra, devuelvo null
            }
            actual = actual.hijos[letra - 'A'];
        }
        if (actual.esPalabra == true) {
            return actual.valor;
        } else {
            return null;
        }
    }

    public void eliminarPalabra(String nombre) {
        Nodo actual = raiz;
        for (int i = 0; i < nombre.length(); i++) { //Recorre el trie buscando la última letra de la palabra
            char letra = nombre.charAt(i);
            actual = actual.hijos[letra - 'A'];
        }
        actual.esPalabra = false; //"Elimina" la palabra. Todavía no sé si quiero borrar la rama entera, pero la palabra ya no se reconoce
        for (int i = 0; i < 58; i++) { //Revisa si la última letra de la palabra tiene un hijo. En ese caso, no se elimina nada
            if (actual.hijos[i] != null) {
                return;
            }
        }
        actual = actual.padre; //Va a la ante última letra
        for (int i = nombre.length() - 1; i > -1; i--) { //Recorre todas las letras. Actual siempre va a ser el padre de i
            for (int j = 0; j < 58; j++) { //Busca todos los posibles hijos de la letra
                if (actual.hijos[j] != null && i != (nombre.charAt(i) - 'A')) { //Si encuentra un hijo que no es i, borra i y se abandona el bucle para no borrar otras palabras
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