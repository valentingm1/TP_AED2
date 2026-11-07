package aed.tp2;

public class AirMiles implements SistemaAirMiles {
    /*COMPLETAR*/
    //Los usuarios es un ABB para agregarlos, con un heap para tenerlos en orden?
    //las promocioes son ???
    //seria conveniente guardar a los mas acumuladores y canjeadores como un atributo aparte? considerando que nos piden complejidad 1 por lo que no los podemos busacar en un arbol
    
    // final para no crear otro heap ni modificarlo
    private final MaxHeap<Node> ranking = new MaxHeap<>((a, b) -> {
        // La lambda compara usuarios ya disponibles
        int orden = Double.compare(saldo(a), saldo(b));
        // Mayor saldo tiene prioridad, ante empate, el menor ID va primero
        return orden != 0 ? orden : Integer.compare(b.identificador, a.identificador);
    });

    // Comparte la formula con consultarSaldo. Recibe el usuario ya encontrado, por lo que calcula en O(1), sin la busqueda por ID que hace consultarSaldo
    private static double saldo(Node u) {
        return u.millasAcumuladas + u.millasRecibidas
            - u.millasCanjeadas - u.millasTransferidas;
    }
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
    // Las recibidas aumentan el saldo, pero no el historico para categoria y maximo, arranca en 0
    double millasRecibidas;
    // Acceso directo a la entrada del heap, para no recorrer el arreglo para localizar al usuario cuando cambia su saldo.
    Handle<Node> posicionHeap;
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
        // El factor depende del historico, gastar no baja categoria y recibir no la sube
        double millas = usuario.millasAcumuladas;
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
        ultimo.posicionHeap = ranking.insertar(ultimo);
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

        ultimo.posicionHeap = ranking.insertar(ultimo);
        referidor.millasAcumuladas = referidor.millasAcumuladas + 500;
        // El bono aumenta su saldo: reordenar al referidor que ya estaba en el heap
        referidor.posicionHeap.modificar(referidor);
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
        // Reordenar tras acreditar el vuelo
        usuario.posicionHeap.modificar(usuario);
        if (usuario.millasAcumuladas > masAcumulador.millasAcumuladas) {
            masAcumulador = usuario;
        }
    }

    public void canjearMillas(int id, double cantidad) {
        //throw new UnsupportedOperationException("Sin implementar");
        Node actual = encontrarUsuario(raiz, id);
        actual.millasCanjeadas = actual.millasCanjeadas + cantidad;
        // El canje reduce el saldo: el usuario puede necesitar bajar en el heap
        actual.posicionHeap.modificar(actual);
        // El maximo gastador incluye canjes y transferencias enviadas
        if (actual.millasCanjeadas + actual.millasTransferidas > masCanjeador.millasCanjeadas + masCanjeador.millasTransferidas) {
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
        //El origen tiene menos saldo y puede necesitar bajar
        transferidor.posicionHeap.modificar(transferidor);
        // Enviar tambien cuenta como gasto, revisar el maximo historico en O(1)
        if (transferidor.millasCanjeadas + transferidor.millasTransferidas
                > masCanjeador.millasCanjeadas + masCanjeador.millasTransferidas)
            masCanjeador = transferidor;
        // Antes se sumaba a acumuladas, ahora se separa para no aumentar su categoria(Más cambios en consultarCategoria)i su acumulado historico, la formula de saldo si incluye estas recibidas
        transferido.millasRecibidas = transferido.millasRecibidas + cantidad;
        // El destino tiene mas saldo y puede necesitar subir, el handle evita buscarlo
        transferido.posicionHeap.modificar(transferido);
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
        // La busqueda por ID ya se hizo arriba, reutilizar la cuenta incluye recibidas
        return saldo(actual);
    }

    public String consultarCategoria(int id) {
        //throw new UnsupportedOperationException("Sin implementar");
        Node actual = encontrarUsuario(raiz, id);
        // Este me genera dudas, recuerdo que la categoria usa el historico, hago el cambio porque es sencillo para volver a la version anterior si no es asi
        double millas = actual.millasAcumuladas;
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
        if (n < 0) throw new IllegalArgumentException("Cantidad negativa");
        // Si se piden mas usuarios que los existentes, devolver solo los existentes
        int cantidad = Math.min(n, ranking.tamanio());
        int[] resultado = new int[cantidad];
        // Guardar referencias permite restaurar usuarios sin buscarlos por ID en el ABB
        Node[] extraidos = new Node[cantidad];
        // Extraer TODOS antes de reinsertar, para no seleccionar repetidamente el maximo
        for (int i = 0; i < cantidad; i++) {
            extraidos[i] = ranking.extraerMaximo();
            resultado[i] = extraidos[i].identificador;
        }
        for (Node usuario : extraidos)
            usuario.posicionHeap = ranking.insertar(usuario);
        return resultado;
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
