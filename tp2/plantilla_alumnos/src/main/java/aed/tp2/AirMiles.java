package aed.tp2;

public class AirMiles implements SistemaAirMiles {
    /*COMPLETAR*/
    //Los usuarios es un ABB para agregarlos, con un heap para tenerlos en orden?
    //las promocioes son ???
    //seria conveniente guardar a los mas acumuladores y canjeadores como un atributo aparte? considerando que nos piden complejidad 1 por lo que no los podemos busacar en un arbol
    ABB<Usuario> arbol;
    Trie<Promocion> trie;
    Usuario masAcumulador;
    Usuario masCanjeador;

    public AirMiles() {
        arbol = new ABB();
        trie = new Trie();
        masAcumulador = null;
        masCanjeador = null;
        Promocion promo = new Promocion(1, false, 0);
        trie.agregarPalabra("NoPromo", promo);
    }

    private double factorUsuario(Usuario actual) {
        double millas = actual.devolverMillasAcumuladas() - actual.devolverMillasCanjeadas() - actual.devolverMillasTransferidas();
        if (0 <= millas && millas < 10000) {
            return 1;
        }
        if (10000 <= millas && millas < 50000) {
            return 1.5;
        }
        if (50000 <= millas && millas < 1000000) {
            return 2;
        }
        return 0;
    }


    public void registrarUsuario(int id) {
        Usuario nuevo = new Usuario(id);
        if (arbol.cardinal() == 0) { //Si nuevo es el primer usuario del sistema, lo asigna como mas acumulador y mas canjeador
            masAcumulador = nuevo;
            masCanjeador = nuevo;
        }
        arbol.insertar(nuevo);
    }

    public void registrarUsuarioConReferidor(int id, int ref) {
        Usuario nuevo = new Usuario(id);
        arbol.insertar(nuevo);
        Usuario referidor = new Usuario(ref); //Creo un usuario temporal con el mismo id del referidor para poder buscarlo en el arbol. Es la única manera que encontré de buscar un usuario por id int en una clase genérica
        referidor = arbol.devolver(referidor);
        nuevo = arbol.devolver(nuevo);

        referidor.sumarMillasAcumuladas(500);
        nuevo.sumarMillasAcumuladas(id);
        if (referidor.devolverMillasAcumuladas() > masAcumulador.devolverMillasAcumuladas()) {
            masAcumulador = referidor;
        }
    }

    public void acumularMillas(int id, double distancia, String nombrePromo) {
        Usuario actual = new Usuario(id);
        actual = arbol.devolver(actual);
        Promocion promo = trie.devolverPalabra(nombrePromo);
        double factorU = factorUsuario(actual);
        double millasAGanar = distancia * factorU * promo.devovlerFactor();
        if (promo.devolverConTope() == true) {
            if (millasAGanar <= promo.devovlerTope()) {
                actual.sumarMillasAcumuladas(millasAGanar);
                promo.bajarTope(millasAGanar);
            } else {
                actual.sumarMillasAcumuladas(distancia * factorU);
            }
        } else {
            actual.sumarMillasAcumuladas(millasAGanar);
        }
        if (actual.devolverMillasAcumuladas() > masAcumulador.devolverMillasAcumuladas()) {
            masAcumulador = actual;
        }
    }

    public void canjearMillas(int id, double cantidad) {
        Usuario actual = new Usuario(id);
        actual = arbol.devolver(actual);
        actual.sumarMillasCanjeadas(cantidad);
        if (actual.devolverMillasCanjeadas() + actual.devolverMillasTransferidas() > masCanjeador.devolverMillasCanjeadas() + masCanjeador.devolverMillasTransferidas()) {
            masCanjeador = actual;
        }
    }

    public void transferirMillas(int origen, int destino, double cantidad) {
        Usuario transferidor = new Usuario(origen);
        Usuario transferido = new Usuario(destino);
        transferidor = arbol.devolver(transferidor);
        transferido = arbol.devolver(transferido);
        transferidor.sumarMillasTransferidas(cantidad);
        transferido.sumarMillasAcumuladas(cantidad);        
        if (transferidor.devolverExclusividad() == null || transferidor.devolverExclusividad() == transferido) {
            transferidor.exclusivo(transferido);
        } else {
            transferidor.noExclusivo();
        }
        if (transferido.devolverExclusividad() == null || transferido.devolverExclusividad() == transferidor) {
            transferido.exclusivo(transferidor);
        }
        if (transferidor.devolverMillasCanjeadas() + transferidor.devolverMillasTransferidas() > masCanjeador.devolverMillasCanjeadas() + masCanjeador.devolverMillasTransferidas()) {
            masCanjeador = transferidor;
        }
    }

    public double consultarSaldo(int id) {
        Usuario actual = new Usuario(id);
        actual = arbol.devolver(actual);
        return (actual.devolverMillasAcumuladas() - actual.devolverMillasCanjeadas() - actual.devolverMillasTransferidas());
    }

    public String consultarCategoria(int id) {
        Usuario actual = new Usuario(id);
        actual = arbol.devolver(actual);
        double millas = (actual.devolverMillasAcumuladas() - actual.devolverMillasCanjeadas() - actual.devolverMillasTransferidas());
        if (0 <= millas && millas < 10000) {
            return "Bronce";
        }
        if (10000 <= millas && millas < 50000) {
            return "Plata";
        }
        if (50000 <= millas && millas < 1000000) {
            return "Oro";
        }
        return "Platino";        
    }

    public int[] rankingMillas(int n) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public void iniciarPromocion(String nombre, double factor) {
        Promocion promo = new Promocion(factor, false, 0);
        trie.agregarPalabra(nombre, promo);
    }

    public void iniciarPromocionConTope(String nombre, double factor, double tope) {
        Promocion promo = new Promocion(factor, true, tope);
        trie.agregarPalabra(nombre, promo);
    }

    public void finalizarPromocion(String nombre) {
        trie.eliminarPalabra(nombre);
    }

    public int elMasAcumulador() {
        return masAcumulador.devolverIdentificador();
    }

    public int elMasCanjeador() {
        return masCanjeador.devolverIdentificador();
    }

    private boolean hayExclusividad(Usuario actual) {
        if (actual.devolverExclusividad() != null && actual.devolverExclusividad() != actual) {
            if (actual.devolverExclusividad().devolverExclusividad() == actual && actual.devolverIdentificador() < actual.devolverExclusividad().devolverIdentificador()) {
                return true;
            }
        }
        return false;
    }

    public int[][] paresTransferidoresExclusivos() {
        //Recorro todo el arbol y reviso que puntero de exclusividad. Si ambos nodos se apuntan entre sí, los agrego al array solo si el nodo A tiene un id menor al nodo B
        Usuario actual;
        int cantidadtotal = arbol.cardinal() / 2;
        int[][] temp = new int[cantidadtotal][2];
        int[][] res;
        int cantidadpares = 0;
        ABB<Usuario>.ABB_Iterador iter = arbol.iterador();
        if (arbol.cardinal() != 0) {
            while (iter.haySiguiente() == true) {
                actual = iter.siguiente();
                if (hayExclusividad(actual) == true) {
                    temp[cantidadpares][0] = actual.devolverIdentificador();
                    temp[cantidadpares][1] = actual.devolverExclusividad().devolverIdentificador();
                    cantidadpares = cantidadpares + 1;
                }
            }
        }
        res = new int[cantidadpares][2];
        for (int i = 0; i < cantidadpares; i++) {
            res[i][0] = temp[i][0];
            res[i][1] = temp[i][1];
        }
        return res;
    }
}
