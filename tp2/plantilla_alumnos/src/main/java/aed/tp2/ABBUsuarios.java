package aed.tp2;

public class ABBUsuarios {
    private Usuario raiz; //El primer usuario en ser agregado al árbol. Si no hay usuarios, es null
    private int cantidad; //Cantidad de usuarios total. Se usa para el iterador
    private Usuario masAcumulador; //Guarda el usuario que más millas acumuló
    private Usuario masCanjeador; //Guarda el usuario que más millas canjeó. Ojo que son las canjeadas más las transferidas

    public ABBUsuarios() { //Lo inicializo vacío. Las variables que guardan usuarios son null
        raiz = null;
        cantidad = 0;
        masAcumulador = null;
        masCanjeador = null;
    }

    public int cardinal() { //Devuelve la cantidad de usuarios. Solo la uso en el iterador. Podría usarla para verificar si la raiz es null, quedaría más prolijo. Después veo
        return cantidad;
    }

    public double millasMasAcumulador() { //Devuelve la cantidad de millas del más acumulador. Si es null, devuelve -1. ¿Por qué? Si queremos saber las millas del más acumulador, es porque queremos ver si hay que reemplazarlo. Si devuelvo -1, no importa con quién lo compare, ese se va a convertir en el más acumulador
        if (masAcumulador != null) {
            return masAcumulador.millasAcumuladas;
        } else {
            return -1;
        }
    }

    public int idMasAcumulador() { //Devuelve el id del más acumulador. Mismo principio que el anterior
        if (masAcumulador != null) {
            return masAcumulador.identificador;
        } else {
            return -1;
        }
    }

    public void nuevoMasAcumulador(Usuario nuevo){ //Reemplaza al más acumulador por el parámetro
        masAcumulador = nuevo;
    }
    
    //Los siguientes 3 son iguales a los 3 anteriores, pero para el más canjeador
    public double millasMasCanjeador() {
        if (masCanjeador != null) {
            return (masCanjeador.millasCanjeadas + masCanjeador.millasTransferidas); //Ojo que se piden millas canjeadas y transferidas. Confuso
        } else {
            return -1;
        }
    }
    
    public int idMasCanjeador() {
        if (masCanjeador != null) {
            return masCanjeador.identificador;
        } else {
            return -1;
        }
    }

    public void nuevoMasCanjeador(Usuario nuevo){
        masCanjeador = nuevo;
    }

    public void agregarUsuario(int id) { //Agrega un usuario nuevo al ABB. Se basa en su id para decidir su posición 
        if (raiz == null) {
            raiz = new Usuario(id);
            masAcumulador = raiz;
            masCanjeador = raiz;
        } else {
            boolean romper = false;
            Usuario actual = raiz;
            while (!romper) {
                if (id > actual.identificador) {
                    if (actual.hijod != null) {
                        actual = actual.hijod;
                    } else {
                        actual.hijod = new Usuario(id);
                        actual.hijod.padre = actual;
                        romper = true;
                    }
                } else {
                    if (actual.hijoi != null) {
                        actual = actual.hijoi;
                    } else {
                        actual.hijoi = new Usuario(id);
                        actual.hijoi.padre = actual;
                        romper = true;
                    }
                }
            }
        }
        cantidad = cantidad + 1;
    }

    public Usuario devolverUsuario(int id) { //Busca el usuario dentro del ABB y lo devuelve
        Usuario actual = raiz;
        while (true) {
            if (actual.identificador == id) {
                return actual;
            } else {
                if (id > actual.identificador) {
                    actual = actual.hijod;                    
                } else {
                    actual = actual.hijoi;
                }
            }
        }
    }



    public class ABBUsuarios_Iterador {
        private Usuario _actual;
        private int contadoriterador;

        public ABBUsuarios_Iterador() {
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
    
        public Usuario siguiente() {
            Usuario sig;
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
            return sig;       
        }
    }

    public ABBUsuarios_Iterador iterador() {
        return new ABBUsuarios_Iterador();
    }

}