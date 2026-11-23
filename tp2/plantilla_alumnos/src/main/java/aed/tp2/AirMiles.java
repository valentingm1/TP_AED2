package aed.tp2;

public class AirMiles implements SistemaAirMiles {
    /*COMPLETAR*/
    //Los usuarios es un ABB para agregarlos, con un heap para tenerlos en orden?
    //las promocioes son ???
    //seria conveniente guardar a los mas acumuladores y canjeadores como un atributo aparte? considerando que nos piden complejidad 1 por lo que no los podemos busacar en un arbol
    ABBUsuarios arbol;
    TriePromociones trie;

    public AirMiles() {
        arbol = new ABBUsuarios();
        trie = new TriePromociones();
    }

    private double factorUsuario(Usuario usuario) {
        double millas = usuario.millasAcumuladas - usuario.millasCanjeadas - usuario.millasTransferidas;
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
        arbol.agregarUsuario(id);
    }

    public void registrarUsuarioConReferidor(int id, int ref) {
        arbol.agregarUsuario(id);
        Usuario nuevo = arbol.devolverUsuario(id);
        Usuario referidor = arbol.devolverUsuario(ref);
        referidor.millasAcumuladas = referidor.millasAcumuladas + 500;
        nuevo.millasAcumuladas = 500;
        if (referidor.millasAcumuladas > arbol.millasMasAcumulador()) {
            arbol.nuevoMasAcumulador(referidor);
        }
    }

    public void acumularMillas(int id, double distancia, String nombrePromo) {
        Usuario actual = arbol.devolverUsuario(id);
        Promocion promo = trie.devolvePromocion(nombrePromo);
        double factorU = factorUsuario(actual);
        double millasAGanar = distancia * factorU * promo.factor;
        if (promo.conTope == true) {
            if (millasAGanar <= promo.tope) {
                actual.millasAcumuladas = actual.millasAcumuladas + millasAGanar;
                promo.tope = promo.tope - millasAGanar;
            } else {
                actual.millasAcumuladas = actual.millasAcumuladas + distancia * factorU;
            }
        } else {
            actual.millasAcumuladas = actual.millasAcumuladas + millasAGanar;
        }
        if (actual.millasAcumuladas > arbol.millasMasAcumulador()) {
            arbol.nuevoMasAcumulador(actual);
        }
    }

    public void canjearMillas(int id, double cantidad) {
        Usuario actual = arbol.devolverUsuario(id);
        actual.millasCanjeadas = actual.millasCanjeadas + cantidad;
        if (actual.millasCanjeadas + actual.millasTransferidas > arbol.millasMasCanjeador()) {
            arbol.nuevoMasCanjeador(actual);
        }
    }

    public void transferirMillas(int origen, int destino, double cantidad) {
        Usuario transferidor;
        Usuario transferido;
        transferidor = arbol.devolverUsuario(origen);
        transferido = arbol.devolverUsuario(destino);
        transferidor.millasTransferidas = transferidor.millasTransferidas + cantidad;
        transferido.millasAcumuladas = transferido.millasAcumuladas + cantidad;
        if (transferidor.exclusividad == null || transferidor.exclusividad == transferido) {
            transferidor.exclusividad = transferido;
        } else {
            transferidor.exclusividad = transferidor;
        }
        if (transferido.exclusividad == null || transferido.exclusividad == transferidor) {
            transferido.exclusividad = transferidor;
        } else {
            transferido.exclusividad = transferido;
        }
        if (transferidor.millasCanjeadas + transferidor.millasTransferidas > arbol.millasMasCanjeador()) {
            arbol.nuevoMasCanjeador(transferidor);
        }
    }

    public double consultarSaldo(int id) {
        Usuario actual = arbol.devolverUsuario(id);
        return (actual.millasAcumuladas - actual.millasCanjeadas - actual.millasTransferidas);
    }

    public String consultarCategoria(int id) {
        Usuario actual = arbol.devolverUsuario(id);
        double millas = actual.millasAcumuladas - actual.millasCanjeadas - actual.millasTransferidas;
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
        trie.agregarPromocionSinTope(nombre, factor);
    }

    public void iniciarPromocionConTope(String nombre, double factor, double tope) {
        trie.agregarPromocionConTope(nombre, factor, tope);
    }

    public void finalizarPromocion(String nombre) {
        trie.eliminarPromocion(nombre);
    }

    public int elMasAcumulador() {
        return arbol.idMasAcumulador();
    }

    public int elMasCanjeador() {
        return arbol.idMasCanjeador();
    }

    private boolean hayExclusividad(Usuario actual) {
        if (actual.exclusividad != null && actual.exclusividad != actual) {
            if (actual.exclusividad.exclusividad == actual && actual.identificador < actual.exclusividad.identificador) {
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
        ABBUsuarios.ABBUsuarios_Iterador iter = arbol.iterador();
        if (arbol.cardinal() != 0) {
            while (iter.haySiguiente() == true) {
                actual = iter.siguiente();
                if (hayExclusividad(actual) == true) {
                    temp[cantidadpares][0] = actual.identificador;
                    temp[cantidadpares][1] = actual.exclusividad.identificador;
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
