package aed.tp2;

public class ABB<T extends Comparable<T>> {
    private Nodo raiz; //Nodo raiz. Es el principio del ABB
    private int cantidad; //Cantidad de nodos en total. Se usa para el iterador

    private class Nodo {
        T valor; //Puede ser cualquier tipo de dato siempre y cuando sea comparable
        Nodo padre; //Padre del nodo. Si es la raiz, es null
        Nodo hijoi; //Hijo izquierdo
        Nodo hijod; //Hijo derecho

        Nodo (T data) { //Creo un nodo nuevo que contiene un dato T. Su padre e hijos son nulos hasta ubicarlo en el arbol
            this.valor = data;
            this.hijod = null;
            this.hijoi = null;
            this.padre = null;
        }
    }


    public ABB() { //Lo inicializo vacío. Las variables que guardan usuarios son null
        raiz = null;
        cantidad = 0;
    }

    public int cardinal() { //Devuelve la cantidad de usuarios. Solo la uso en el iterador. Podría usarla para verificar si la raiz es null, quedaría más prolijo. Después veo
        return cantidad;
    }

    public void insertar(T nuevo) { //Agrega un usuario nuevo al ABB. Se basa en su id para decidir su posición 
        if (raiz == null) {
            raiz = new Nodo(nuevo);
        } else {
            boolean romper = false;
            Nodo actual = raiz;
            while (!romper) {
                if (nuevo.compareTo(actual.valor) < 0) {
                    if (actual.hijod != null) {
                        actual = actual.hijod;
                    } else {
                        actual.hijod = new Nodo(nuevo);
                        actual.hijod.padre = actual;
                        romper = true;
                    }
                } else {
                    if (actual.hijoi != null) {
                        actual = actual.hijoi;
                    } else {
                        actual.hijoi = new Nodo(nuevo);
                        actual.hijoi.padre = actual;
                        romper = true;
                    }
                }
            }
        }
        cantidad = cantidad + 1;
    }

    public T devolver(T id) { //Busca el nodo dentro del ABB y devuelve el dato T
        Nodo actual = raiz;
        while (true) {
            //if (actual.identificador == id) {
            if (actual.valor.compareTo(id) == 0){
                return actual.valor;
            } else {
                //if (id > actual.identificador) {
                if (actual.valor.compareTo(id) > 0) {
                    actual = actual.hijod;                    
                } else {
                    actual = actual.hijoi;
                }
            }
        }
    }



    public class ABB_Iterador {
        private Nodo _actual;
        private int contadoriterador;

        public ABB_Iterador() {
            _actual = raiz;
            contadoriterador = 0;
            if (_actual != null) {
                while (_actual.hijoi != null) {
                    _actual = _actual.hijoi;
                }
            }
        }

        public boolean haySiguiente() {            
            return contadoriterador < cantidad;
        }
    
        public T siguiente() {
            Nodo sig;
            if (_actual == null) {
                return null;
            }
            sig = _actual;
            if (_actual.hijod != null) {
                _actual = _actual.hijod;
                while (_actual.hijoi != null) {
                    _actual = _actual.hijoi;
                }
            } else {
                while (_actual != raiz && _actual == _actual.padre.hijod) {
                    _actual = _actual.padre;
                }
                if (_actual != raiz && _actual.padre.hijoi == _actual) {
                    _actual = _actual.padre;
                }
            }
            contadoriterador = contadoriterador + 1;
            return sig.valor;       
        }
    }

    public ABB_Iterador iterador() {
        return new ABB_Iterador();
    }

}