package aed.tp2;

public class AirMiles implements SistemaAirMiles {
    /*COMPLETAR*/
    //Los usuarios es un ABB para agregarlos, con un heap para tenerlos en orden?
    //las promocioes son ???
    //seria conveniente guardar a los mas acumuladores y canjeadores como un atributo aparte? considerando que nos piden complejidad 1 por lo que no los podemos busacar en un arbol
    Node raiz;
    Node ultimo;
    Promos promoRaiz;
    Node masAcumulador;
    Node masCanjeador;

    private class Promos {
        boolean esPromo;
        double factor;
        boolean conTope;
        double tope;
        Promos[] hijos;
        Promos padre;

            Promos() {
                hijos = new Promos[58]; //Mayusculas, minusculas y los caracteres entre ambas (a quien se le ocurrió meter cosas en el medio?) sin ñ ni acentos
                esPromo = false;
                factor = 0;
                conTope = false;
                tope = 0;
                padre = null;
            }
    }


    private class Node {
    int identificador;
    double millasAcumuladas;
    double millasCanjeadas;
    double millasTransferidas;
    Node hijoi;
    Node hijod;
    Node siguiente;
    Node exclusividad;
        Node (int data){
            identificador = data;
            millasAcumuladas = 0;
            millasCanjeadas = 0;
            millasTransferidas = 0;
            hijoi = null;
            hijod = null;
            siguiente = null;
            exclusividad = null;
            //agregar los atributos que guardan informacion
        }    
    }

    private Node encontrarUsuario(Node actual, int id) {
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

    private Promos encontrarPromo(String nombre) {
        Promos actual = promoRaiz;
        for (int i = 0; i < nombre.length(); i++) {
            char letra = nombre.charAt(i);
            actual = actual.hijos[letra - 'A'];
        }
        return actual;
    }

    private void registrarUsuarioDesde(Node origen, int id) {
        boolean romper = false;
        Node actual = origen;
        while (!romper) {
                if (id > actual.identificador) {
                    if (actual.hijod != null) {
                        actual = actual.hijod;
                    } else {
                        actual.hijod = new Node(id);
                        ultimo.siguiente = actual.hijod;
                        ultimo = actual.hijod;
                        romper = true;
                    }
                } else {
                    if (actual.hijoi != null) {
                        actual = actual.hijoi;
                    } else {
                        actual.hijoi = new Node(id);
                        ultimo.siguiente = actual.hijoi;
                        ultimo = actual.hijoi;
                        romper = true;
                    }
                }
            }
    }

    private void registrarUsuarioDesdeConReferidor(Node origen, int id) {
        boolean romper = false;
        Node actual = origen;
        while (!romper) {
                if (id > actual.identificador) {
                    if (actual.hijod != null) {
                        actual = actual.hijod;
                    } else {
                        actual.hijod = new Node(id);
                        actual = actual.hijod;
                        romper = true;
                    }
                } else {
                    if (actual.hijoi != null) {
                        actual = actual.hijoi;
                    } else {
                        actual.hijoi = new Node(id);
                        actual = actual.hijoi;
                        romper = true;
                    }
                }
        }        
        ultimo.siguiente = actual;
        ultimo = actual;
        actual.millasAcumuladas = 500;
    }

    private double factorUsuario(Node usuario) {
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

    public AirMiles() {
        //throw new UnsupportedOperationException("Sin implementar");
        raiz = null;
        masAcumulador = null;
        masCanjeador = null;
        ultimo = null;
        promoRaiz = new Promos();
        iniciarPromocion("NoPromo", 1);
    }

    public void registrarUsuario(int id) {
        //throw new UnsupportedOperationException("Sin implementar");
        if (raiz == null) {
            raiz = new Node(id);
            masAcumulador = raiz;
            masCanjeador = raiz;
            ultimo = raiz;
        } else {
            registrarUsuarioDesde(raiz, id);
        }
    }

    public void registrarUsuarioConReferidor(int id, int ref) {
        //throw new UnsupportedOperationException("Sin implementar");
        //boolean romper = false;
        Node actual = raiz;
        Node referidor;
        registrarUsuarioDesdeConReferidor(actual, id);
        referidor = encontrarUsuario(actual, ref);        
        /*while (romper) {
            if (id > actual.identificador & ref > actual.identificador) {
                actual = actual.hijod;
            } else {
                if (id < actual.identificador & ref < actual.identificador) {
                    actual = actual.hijoi;
                } else {
                    registrarUsuarioDesdeConReferidor(actual, id);
                    referidor = encontrarUsuario(actual, ref);
                    romper = true;
                }
            }
        }*/
        referidor.millasAcumuladas = referidor.millasAcumuladas + 500;
        if (referidor.millasAcumuladas > masAcumulador.millasAcumuladas) {
            masAcumulador = referidor;
        }
    }

    public void acumularMillas(int id, double distancia, String nombrePromo) {
        //throw new UnsupportedOperationException("Sin implementar");
        Node usuario = encontrarUsuario(raiz, id);
        Promos promo = encontrarPromo(nombrePromo);
        double factorU = factorUsuario(usuario);
        double millasAGanar = distancia * factorU * promo.factor;
        if (promo.conTope == true) {
            if (millasAGanar <= promo.tope) {
                usuario.millasAcumuladas = usuario.millasAcumuladas + millasAGanar;
                promo.tope = promo.tope - millasAGanar;
            } else {
                usuario.millasAcumuladas = usuario.millasAcumuladas + distancia * factorU;
            }
        } else {
            usuario.millasAcumuladas = usuario.millasAcumuladas + millasAGanar;
        }
        if (usuario.millasAcumuladas > masAcumulador.millasAcumuladas) {
            masAcumulador = usuario;
        }
    }

    public void canjearMillas(int id, double cantidad) {
        //throw new UnsupportedOperationException("Sin implementar");
        Node actual = encontrarUsuario(raiz, id);
        actual.millasCanjeadas = actual.millasCanjeadas + cantidad;
        if (actual.millasCanjeadas > masCanjeador.millasCanjeadas) {
            masCanjeador = actual;
        }
    }

    public void transferirMillas(int origen, int destino, double cantidad) {
        //throw new UnsupportedOperationException("Sin implementar");
        //boolean romper = false;
        Node actual = raiz;
        Node transferidor;
        Node transferido;
        /*while (romper) {
            if (origen > actual.identificador & destino > actual.identificador) {
                actual = actual.hijod;
            } else {
                if (origen < actual.identificador & destino < actual.identificador) {
                    actual = actual.hijoi;
                } else {
                    transferidor = encontrarUsuario(actual, origen);
                    transferido = encontrarUsuario(actual, destino);
                    romper = true;
                }
            }
        }*/
        transferidor = encontrarUsuario(actual, origen);
        transferido = encontrarUsuario(actual, destino);
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
    }

    public double consultarSaldo(int id) {
        //throw new UnsupportedOperationException("Sin implementar");
        Node actual = encontrarUsuario(raiz, id);
        return (actual.millasAcumuladas - actual.millasCanjeadas - actual.millasTransferidas);
    }

    public String consultarCategoria(int id) {
        //throw new UnsupportedOperationException("Sin implementar");
        Node actual = encontrarUsuario(raiz, id);
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
        //throw new UnsupportedOperationException("Sin implementar");
        Promos actual = promoRaiz;
        for (int i = 0; i < nombre.length(); i++) {
            char letra = nombre.charAt(i);
            if (actual.hijos[letra - 'A'] == null) {
                actual.hijos[letra - 'A'] = new Promos();
                actual.hijos[letra - 'A'].padre = actual;
            }
            actual = actual.hijos[letra - 'A'];
        }
        actual.esPromo = true;
        actual.factor = factor;
        actual.conTope = false;
        actual.tope = 0;
    }

    public void iniciarPromocionConTope(String nombre, double factor, double tope) {
        //throw new UnsupportedOperationException("Sin implementar");
        Promos actual = promoRaiz;
        for (int i = 0; i < nombre.length(); i++) {
            char letra = nombre.charAt(i);
            if (actual.hijos[letra - 'A'] == null) {
                actual.hijos[letra - 'A'] = new Promos();
                actual.hijos[letra - 'A'].padre = actual;
            }
            actual = actual.hijos[letra - 'A'];
        }
        actual.esPromo = true;
        actual.factor = factor;
        actual.conTope = true;
        actual.tope = tope;
    }

    public void finalizarPromocion(String nombre) {
        //No hay un test para verificar esto
        Promos actual = promoRaiz;
        for (int i = 0; i < nombre.length(); i++) {
            char letra = nombre.charAt(i);
            actual = actual.hijos[letra - 'A'];
        }
        actual.esPromo = false;
        for (int i = 0; i < 58; i++) {
            if (actual.hijos[i] != null) {
                return;
            }
        }
        actual = actual.padre;
        for (int i = nombre.length() - 1; i > -1; i--) {
            for (int j = 0; i < 58; i++) {
                if (actual.hijos[j] != null && i != (nombre.charAt(i) - 'A')) {
                    char letra = nombre.charAt(i);
                    actual.hijos[letra - 'A'].padre = actual;
                    actual.hijos[letra - 'A'] = null;
                    return;
                }
            }
            actual = actual.padre;
            char letra = nombre.charAt(i);
            actual.hijos[letra - 'A'] = null;
        }
    }

    public int elMasAcumulador() {
        //throw new UnsupportedOperationException("Sin implementar");
        if (masAcumulador == null) {
            return -1;
        } else {
            return masAcumulador.identificador;
        }
    }

    public int elMasCanjeador() {
        //throw new UnsupportedOperationException("Sin implementar");
        if (masCanjeador == null) {
            return -1;
        } else {
            return masCanjeador.identificador;
        }
    }

    private boolean hayExclusividad(Node actual) {
        if (actual.exclusividad != null && actual.exclusividad != actual) {
            if (actual.exclusividad.exclusividad == actual && actual.identificador < actual.exclusividad.identificador) {
                return true;
            }
        }
        return false;
    }

    public int[][] paresTransferidoresExclusivos() {
        //Recorro todo el arbol y reviso que puntero de exclusividad. Si ambos nodos se apuntan entre sí, los agrego al array solo si el nodo A tiene un id menor al nodo B
        //throw new UnsupportedOperationException("Sin implementar");
        /*Node actual = raiz;
        boolean bajando = true;
        while (true) {
            while (bajando == true) {
                if (actual.hijoi != null) {
                    actual = actual.hijoi;
                }
                bajando = false;
                if (hayExclusividad(actual) == true) {
                    //agregar el coso
                }
            while (bajando == false) {
                if (actual.)
            }
        }
        }*/
        Node actual = raiz;
        int[][] res = new int[0][2];
        int[][] temp;
        int contador = 0;
        if (raiz == null) {
            return res;
        }
        while (actual.siguiente != null) {
            if (hayExclusividad(actual) == true) {
                //agregar
                temp = new int[contador + 1][2];
                for (int i = 0; i < contador; i++) {
                    temp[i][0] = res[i][0];
                    temp[i][1] = res[i][1];
                }
                res = temp;
                res[contador][0] = actual.identificador;
                res[contador][1] = actual.exclusividad.identificador;
                contador = contador + 1;
            }
            actual = actual.siguiente;
        } 
        return res;
    }
}
