import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;
@SuppressWarnings("BusyWait")

public class Menu {
    public static Scanner sc = new Scanner(System.in);

    public static volatile boolean continuar = true;

    public static int cobre = 0;
    public static int plata = 0;
    public static int oro = 0;
    public static int diamante = 0;
    
    public static int[] minas = {5, 4, 3, 2};

    public static int mejora = 30000; 
    public static int[] mejoras = {1, 1, 1, 1};

    public static int joyeria_normal = 0;
    public static int joyeria_deluxe = 0;

    public static int dinero = 0;

    public static void main(String[] args) throws Exception {
        vaciar_consola();

        Thread incrementador = new Thread(() -> {
            try {
                while(continuar) {
                    Thread.sleep(100);
                    cobre += minas[0];
                    plata += minas[1];
                    oro += minas[2];
                    diamante += minas[3];
                }
            } catch (InterruptedException e) {
                System.err.println("ERR0R: " + e);
            }
        });

        Thread terminal = new Thread(() -> {
            String input;

            vaciar_consola();
            slow_print("Cobre: 0");
            slow_print("Plata: 0");
            slow_print("Oro: 0");
            slow_print("Diamante: 0");
            System.out.println();
            slow_print("Joyería Normal: 0");
            slow_print("Joyería Deluxe: 0");
            System.out.println();
            slow_print("Dinero: 0$");
            System.out.println("");
            try {
                do {
                    System.out.print(">>> ");
                    input = sc.nextLine();
    
                    vaciar_consola();
                    switch (input) {
                        case "show", "mostrar", "s", "" -> {
                            mostrar_recursos();
                        }
                        case "create", "c", "crear", "craft" -> {
                            crear(cobre, plata, oro, diamante);
                        }
                        case "sell", "vender", "v" -> {
                            vender();
                        }
                        case "mejorar", "upgrade", "m" -> {
                            mejorar();
                        }
                        case "cerrar", "close", "stop", "p", "parar" -> {
                            continuar = false;
                        }
                        default -> {
                            mostrar_recursos();
                            slow_print("Comando no reconocido. Consulte el manual README.");
                        }
                    }
                } while (continuar);
            } catch (Exception e) {
                System.err.println("ERR0R: " + e);
            }

        });
        
        incrementador.start();
        terminal.start();
        
        incrementador.join();
        terminal.join();

        // Mensaje al terminar el programa.
        slow_print("Cerrando fábrica", false, 40);
        slow_print(".....", true, 100);
        Thread.sleep(500);
        slow_print("Cerrando minas", false, 40);
        slow_print("........", true, 100);
        Thread.sleep(200);
        slow_print("Fue un buen día", false, 30);
        Thread.sleep(400);
        slow_print(", muy productivo.", true, 35);
        Thread.sleep(800);
        slow_print("Hasta el siguiente día.", false, 45);
    }
    
    public static void mostrar_recursos(String recurso) {
        switch (recurso) {
            case "materiales", "mat" -> {
                slow_print("Cobre: " + formatear(cobre));
                slow_print("Plata: " + formatear(plata));
                slow_print("Oro: " + formatear(oro));
                slow_print("Diamante: " + formatear(diamante));        
            }
            case "joyerias", "joy" -> {
                slow_print("Joyería Normal: " + formatear(joyeria_normal));
                slow_print("Joyería Deluxe: " + formatear(joyeria_deluxe));        
            }
            case "dinero", "din" -> {
                slow_print("Dinero: " + formatear(dinero) + "$");
            }
            case "todo", "all" -> {
                slow_print("Cobre: " + formatear(cobre));
                slow_print("Plata: " + formatear(plata));
                slow_print("Oro: " + formatear(oro));
                slow_print("Diamante: " + formatear(diamante));
                System.out.println();
                slow_print("Joyería Normal: " + formatear(joyeria_normal));
                slow_print("Joyería Deluxe: " + formatear(joyeria_deluxe));
                System.out.println();
                slow_print("Dinero: " + formatear(dinero) + "$");
            }
        }
        System.out.println();
    }
    public static void mostrar_recursos() {
        mostrar_recursos("todo");
    }
    
    public static void crear(int a, int b, int c, int d) throws Exception {
        boolean repetir;
        do {
            mostrar_recursos("mat");
            slow_print("¿Qué tipo de joyería va a producir?", true, 10);
            slow_print("1. Joyería Normal (400 - 300 - 200 - 100)", true, 5);
            slow_print("2. Joyería Deluxe (100 - 300 - 300 - 400)", true, 5);
            slow_print("--> ", false);
            String opcion = sc.nextLine();
            opcion = opcion.toUpperCase();
    
    
            int max_productos;
            int cantidad;
    
            switch (opcion) {
                case "1", "1.", "NORMAL", "JOYERIA NORMAL", "N" -> {
                    max_productos = Math.min(a/400, Math.min(b/300, Math.min(c/200, d/100)));
    
                    if (max_productos > 0) {
                        System.out.println();
                        slow_print("Sólo puede producir " + max_productos + " lotes de Joyería Normal.");
                        slow_print("¿Cuántos va a fabricar?: ", false);
                        cantidad = sc.nextInt();
    
                        while (cantidad > max_productos) {
                            vaciar_consola();
                            mostrar_recursos("mat");
                            a = cobre;
                            b = plata;
                            c = oro;
                            d = diamante;
                            max_productos = Math.min(a/400, Math.min(b/300, Math.min(c/200, d/100)));
                            System.out.println();
                            slow_print("Sólo puede producir " + max_productos + " lotes de Joyería Normal.");
                            slow_print("¿Cuántos va a fabricar?: ", false);
                            cantidad = sc.nextInt();
                        }
        
                        for (int i = 0; i < cantidad; i++) {
                            joyeria_normal++;
                            cobre -= 400;
                            plata -= 300;
                            oro -= 200;
                            diamante -= 100;
                        }
                    } else {
                        vaciar_consola();
                        mostrar_recursos("mat");
                        slow_print("Materiales insuficientes.");
                    }
                    repetir = false;
                }
                case "2", "2.", "JOYERIA DELUXE", "DELUXE", "D" -> {
                    max_productos = Math.min(d/400, Math.min(c/300, Math.min(b/300, a/100)));
    
                    if (max_productos > 0) {
                        System.out.println();
                        slow_print("Sólo puede producir " + max_productos + " lotes de Joyería Deluxe.");
                        slow_print("¿Cuántos va a fabricar?: ", false);
                        cantidad = sc.nextInt();
                        while (cantidad > max_productos) {
                            vaciar_consola();
                            mostrar_recursos("mat");
                            a = cobre;
                            b = plata;
                            c = oro;
                            d = diamante;
                            max_productos = Math.min(d/400, Math.min(c/300, Math.min(b/300, a/100)));
                            System.out.println();
                            slow_print("Sólo puede producir " + max_productos + " lotes de Joyería Deluxe.");
                            slow_print("¿Cuántos va a fabricar?: ", false);
                            cantidad = sc.nextInt();
                        }
        
                        for (int i = 0; i < cantidad; i++) {
                            joyeria_deluxe++;
                            cobre -= 100;
                            plata -= 300;
                            oro -= 300;
                            diamante -= 400;
                        }
                        repetir = false;
                    } else {
                        vaciar_consola();
                        mostrar_recursos("mat");
                        slow_print("Materiales insuficientes.");
                        repetir = false;
                    }
                }
                default -> {
                    vaciar_consola();
                    repetir = true;
                }
            }
        } while (repetir);
    }

    public static void vender() {
        mostrar_recursos("joyerias");
        if (joyeria_normal > 0 || joyeria_deluxe > 0) {
            if (joyeria_normal > 0) {
                slow_print("Joyería Normal: +" + formatear(joyeria_normal * 1000) + "$");
                dinero += joyeria_normal * 1000;
                joyeria_normal = 0;
            } else {
                slow_print("Joyería Normal: N/A");
            }
            if (joyeria_deluxe > 0) {
                slow_print("Joyería Deluxe: +" + formatear(joyeria_deluxe * 5000) + "$");
                dinero += joyeria_deluxe * 5000;
                joyeria_deluxe = 0;
            } else {
                slow_print("Joyería Deluxe: N/A");
            }
            System.out.println("");
            slow_print("Dinero: " + formatear(dinero) + "$");
        } else {
            slow_print("No hay ningún producto para vender.");
        }
        System.out.println("");
    }

    public static void mejorar() {
        String opcion;
        boolean seguir_mejorando = true;
        int material = 5;
        boolean repetir;
        int coste = 1000^cobre; //Precio predeterminado por error de coste no inicializado
        do {
            mostrar_recursos("din");
    
            slow_print("¿Qué mina desea mejorar?");
            slow_print("1. Mina de Cobre (" + minas[0]*10 + "/s)");
            slow_print("2. Mina de Plata (" + minas[1]*10 + "/s)");
            slow_print("3. Mina de Oro (" + minas[2]*10 + "/s)");
            slow_print("4. Mina de Diamante (" + minas[3]*10 + "/s)");
            slow_print("5. SALIR (de inmediato)");
            
            do { 
                slow_print("--> ", false);
                opcion = sc.nextLine();
                opcion = opcion.toUpperCase();            
                vaciar_consola();
                switch (opcion) {
                    case "1", "COBRE", "C" -> {
                        material = 0;
                        repetir = false;
                    }
                    case "2", "PLATA", "P" -> {
                        material = 1;
                        repetir = false;
                    }
                    case "3", "ORO", "O" -> {
                        material = 2;
                        repetir = false;
                    }
                    case "4", "DIAMANTE", "D" -> {
                        material = 3;
                        repetir = false;
                    }
                    case "5", "SALIR", "EXIT" -> {
                        repetir = false;
                        seguir_mejorando = false;
                    }
                    default -> {
                        slow_print("No se reconoció su opción.");
                        repetir = true;
                    }
                }
                if (material != 5) {
                    coste = mejora * mejoras[material] / minas[material];
                    slow_print("Mejora de la mina: " + formatear(coste)+"$");
                }
            } while (repetir);
            if (dinero >= coste && seguir_mejorando == true) {
                slow_print("¿Desea comprar esta mejora? (SI/NO)");
                opcion = sc.nextLine();
                opcion = opcion.toUpperCase();
                switch (opcion) {
                    case "SI", "S" -> {
                        dinero -= coste;
                        mejora += mejora/2;
                        mejoras[material] += 1;
                        minas[material] += 1;
                        seguir_mejorando = false;
                    }
                    case "NO", "N" -> {
                        seguir_mejorando = true;
                    }
                    default -> {
                        slow_print("No se reconoció el comando.");
                        seguir_mejorando = true;
                    }
                }
            } else if (material != 5) {
                slow_print("No tiene dinero suficiente para esta mejora.");
                seguir_mejorando = true;
            }
        } while (seguir_mejorando);
        vaciar_consola();
        mostrar_recursos();
    }

    public static String formatear(int numero) {
        NumberFormat formato = NumberFormat.getInstance(Locale.US);
        return formato.format(numero);
    }

    public static void slow_print(String frase, boolean ln, int ms) throws InterruptedException {
        try { // Imprimiendo en la consola carácter por carácter para dar efecto lento. 
            for (int i = 0; i < frase.length(); i++) {
                System.out.print(frase.charAt(i));
                Thread.sleep(ms);
            }
            if (ln) {
                System.out.println();
            }
        } catch (InterruptedException e) {
            System.err.println("ERR0R: " + e);
        }
    }
    public static void slow_print(String frase, boolean ln) {
        try {
            slow_print(frase, ln, 15);
        } catch (InterruptedException e) {
            System.err.println("ERR0R: " + e);
        }
    }
    public static void slow_print(String frase) {
        try {
            slow_print(frase, true, 15);
        } catch (InterruptedException e) {
            System.err.println("ERR0R: " + e);
        }
    }

    public static void vaciar_consola() {
        System.out.println("\033c");
    }
    
}