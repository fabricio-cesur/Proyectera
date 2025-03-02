import java.io.BufferedReader;
import java.io.BufferedWriter;
// import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
// import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;
import java.util.Map;

@SuppressWarnings("BusyWait")

public class Menu {
    public static Scanner sc = new Scanner(System.in);

    public static volatile boolean continuar = true;
    public static boolean vaciar = true;

    public static int cobre;
    public static int plata;
    public static int oro;
    public static int diamante;
    
    public static int[] minas = new int[4];

    public static int mejora; 
    public static int[] mejoras = new int[4];

    public static int joyeria_normal;
    public static int joyeria_deluxe;

    public static int valor_joyeria_normal;
    public static int valor_joyeria_deluxe;
    
    public static int[] costo_normal = new int[4];
    public static int[] costo_deluxe = new int[4];
    
    public static int dinero;

    
    public static void main(String[] args) throws Exception {
        cargar_datos();
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
                vaciar = false;
                System.err.println("ERR0R en el hilo de incrementador: " + e.getMessage());
                e.printStackTrace();
            }
        });
        
        Thread terminal = new Thread(() -> {
            String input;
            guardar_datos();
            
            vaciar_consola();
            mostrar_recursos();
            try {
                do {
                    System.out.print(">>> ");
                    input = sc.nextLine();
                    input = input.toLowerCase();
                    

                    vaciar_consola();
                    switch (input) {
                        case "actualizar", "a", "", " " -> {
                            mostrar_recursos();
                        }
                        case "fabricar", "crear", "f", "c" -> {
                            producir(cobre, plata, oro, diamante);
                        }
                        case "vender", "v" -> {
                            vender();
                        }
                        case "mejorar", "m" -> {
                            mejorar();
                        }
                        case "parar", "p" -> {
                            continuar = false;
                        }
                        default -> {
                            mostrar_recursos();
                            slow_print("Comando no reconocido. Consulte el manual README.");
                        }
                    }

                    guardar_datos();
                } while (continuar);
            } catch (Exception e) {
                vaciar = false;
                System.err.println("ERR0R en la terminal: " + e.getMessage());
                e.printStackTrace();
            }

        });
        
        incrementador.start();
        terminal.start();

        
        incrementador.join();
        terminal.join();

        // Mensaje al terminar el programa.
        slow_print("Guardando los recursos", false, 30);
        slow_print("...", true, 150);
        slow_print("Cerrando fabrica", false, 40);
        slow_print(".....", true, 100);
        Thread.sleep(500);
        slow_print("Hasta el siguiente dia.", false, 45);

    }
    
    
    public static void producir(int c, int p, int o, int d) throws Exception {
        boolean repetir;
        do {
            mostrar_recursos("mat");
            slow_print("¿Qué tipo de joyeria va a producir?", true, 10);
            slow_print("1. Joyería Normal (400 - 300 - 200 - 100)", true, 5);
            slow_print("2. Joyería Deluxe (100 - 300 - 300 - 400)", true, 5);
            slow_print("--> ", false);
            String opcion = sc.nextLine();
            opcion = opcion.toLowerCase();
            
            
            int max_productos;
            int cantidad;
            
            switch (opcion) {
                case "1", "1.", "normal", "joyeria normal", "n", "jn" -> {
                    max_productos = calcular_max_productos("normal");
                    
                    if (max_productos > 0) {
                        cantidad = pedir_cantidad("normal");
                        
                        max_productos = calcular_max_productos("normal");
                        while (cantidad > max_productos) {
                            max_productos = calcular_max_productos("normal");
                            cantidad = pedir_cantidad("normal");
                        }
                        
                        for (int i = 0; i < cantidad; i++) {
                            joyeria_normal++;
                            cobre -= costo_normal[0];
                            plata -= costo_normal[1];
                            oro -= costo_normal[2];
                            diamante -= costo_normal[3];
                        }
                    } else {
                        vaciar_consola();
                        mostrar_recursos("mat");
                        slow_print("Materiales insuficientes.");
                    }
                    repetir = false;
                }
                case "2", "2.", "joyeria deluxe", "deluxe", "d", "jd" -> {
                    max_productos = calcular_max_productos("deluxe");
                    
                    if (max_productos > 0) {
                        cantidad = pedir_cantidad("deluxe");
                        
                        max_productos = calcular_max_productos("deluxe");
                        while (cantidad > max_productos) {
                            max_productos = calcular_max_productos("deluxe");
                            cantidad = pedir_cantidad("deluxe");
                        }
                        
                        for (int i = 0; i < cantidad; i++) {
                            joyeria_deluxe++;
                            cobre -= costo_deluxe[0];
                            plata -= costo_deluxe[1];
                            oro -= costo_deluxe[2];
                            diamante -= costo_deluxe[3];
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
                    slow_print("No se reconoció esa opción.");
                    System.out.println();
                    
                    repetir = true;
                }
            }
        } while (repetir);
    }
    public static int calcular_max_productos(String tipo) {
        int max;
        switch (tipo) {
            case "1", "normal", "nor" -> {
                max = Math.min(cobre/costo_normal[0], Math.min(plata/costo_normal[1], Math.min(oro/costo_normal[2], diamante/costo_normal[3])));
            }
            case "2", "deluxe", "del" -> {
                max = Math.min(cobre/costo_deluxe[0], Math.min(plata/costo_deluxe[1], Math.min(oro/costo_deluxe[2], diamante/costo_deluxe[3])));
            }
            default -> {
                max = 0;
                System.err.println("ERR0R al calcular el máximo de productos disponible.");
            }
        }
        return max;
    }
    public static int pedir_cantidad(String tipo) {
        int cantidad;
        boolean input_correcto = false;
        int max_prod;

        do {
            try {
                System.out.println();
                max_prod = calcular_max_productos(tipo);
                slow_print("Puede producir " + max_prod + " lotes.");
                slow_print("¿Cuántos va a fabricar?: ", false);
                cantidad = sc.nextInt();

                max_prod = calcular_max_productos(tipo);
                if (cantidad < 0) {
                    slow_print("Debe ingresar un número entero positivo.");
                    input_correcto = false;

                    max_prod = calcular_max_productos(tipo);
                } else if (cantidad > max_prod) {
                    max_prod = calcular_max_productos(tipo);
                    slow_print("No puede fabricar más de " + max_prod + " lotes.");
                    input_correcto = false;
                } else {
                    input_correcto = true;
                }
            } catch (java.util.InputMismatchException e) {
                sc.nextLine();
                cantidad = -1;
                slow_print("Debe ingresar un número entero positivo.");
                input_correcto = false;
            }
        } while (!input_correcto);

        return cantidad;
    }
    
    
    
    public static void vender() {
        mostrar_recursos("joyerias");
        if (joyeria_normal > 0 || joyeria_deluxe > 0) {
            if (joyeria_normal > 0) {
                slow_print("Joyeria Normal: +" + "$" + formatear(joyeria_normal * valor_joyeria_normal));
                dinero += joyeria_normal * 1000;
                joyeria_normal = 0;
            } else {
                slow_print("Joyeria Normal: +$0");
            }
            if (joyeria_deluxe > 0) {
                slow_print("Joyeria Deluxe: +" + "$" + formatear(joyeria_deluxe * valor_joyeria_deluxe));
                dinero += joyeria_deluxe * 5000;
                joyeria_deluxe = 0;
            } else {
                slow_print("Joyeria Deluxe: +$0");
            }
            System.out.println("");
            slow_print("Dinero: " + "$" + formatear(dinero));
        } else {
            slow_print("No hay ningun producto para vender.");
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
                opcion = opcion.toLowerCase();            
                vaciar_consola();
                switch (opcion) {
                    case "1", "cobre", "c" -> {
                        material = 0;
                        repetir = false;
                    }
                    case "2", "plata", "p" -> {
                        material = 1;
                        repetir = false;
                    }
                    case "3", "oro", "o" -> {
                        material = 2;
                        repetir = false;
                    }
                    case "4", "diamante", "d" -> {
                        material = 3;
                        repetir = false;
                    }
                    case "5", "salir" -> {
                        material = 5;
                        repetir = false;
                        seguir_mejorando = false;
                    }
                    default -> {
                        slow_print("No se reconocio su opcion.");
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
                opcion = opcion.toLowerCase();
                switch (opcion) {
                    case "si", "s" -> {
                        dinero -= coste;
                        mejora += mejora/2;
                        mejoras[material] += 1;
                        minas[material] += 1;
                        seguir_mejorando = false;
                    }
                    case "no", "n" -> {
                        seguir_mejorando = true;
                    }
                    default -> {
                        slow_print("No se reconocio el comando.");
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
                slow_print("Dinero: " + "$" + formatear(dinero));
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
                slow_print("Dinero: " + "$" + formatear(dinero));
            }
            default -> {
                slow_print("ERR0R: No se especificó el recurso a mostrar correctamente.");
            }
        }
        System.out.println();
    }
    public static void mostrar_recursos() {
        mostrar_recursos("todo");
    }

    public static void cargar_datos() {
        Map<String, Integer> datos = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader("datos.txt"))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("=");
                if (partes.length == 2) {
                    try {
                        datos.put(partes[0].trim(), Integer.parseInt(partes[1].trim()));
                    } catch(NumberFormatException e) {
                        vaciar = false;
                        System.err.println("ERR0R al convertir '" + partes[1] + "' en entero.");
                    }
                }
            }

            cobre = datos.get("cobre");
            plata = datos.get("plata");
            oro = datos.get("oro");
            diamante = datos.get("diamante");
            
            minas[0] = datos.get("mina_cobre");
            minas[1] = datos.get("mina_plata");
            minas[2] = datos.get("mina_oro");
            minas[3] = datos.get("mina_diamante");
        
            mejora = datos.get("mejora"); 
            mejoras[0] = datos.get("mejora_cobre");
            mejoras[1] = datos.get("mejora_plata");
            mejoras[2] = datos.get("mejora_oro");
            mejoras[3] = datos.get("mejora_diamante");

            joyeria_normal = datos.get("joyeria_normal");
            joyeria_deluxe = datos.get("joyeria_deluxe");
        
            valor_joyeria_normal = datos.get("valor_normal");
            valor_joyeria_deluxe = datos.get("valor_deluxe");
            
            costo_normal[0] = datos.get("coste_normal_cobre");
            costo_normal[1] = datos.get("coste_normal_plata");
            costo_normal[2] = datos.get("coste_normal_oro");
            costo_normal[3] = datos.get("coste_normal_diamante");

            costo_deluxe[0] = datos.get("coste_deluxe_cobre");
            costo_deluxe[1] = datos.get("coste_deluxe_plata");
            costo_deluxe[2] = datos.get("coste_deluxe_oro");
            costo_deluxe[3] = datos.get("coste_deluxe_diamante");

            dinero = datos.get("dinero");

        } catch(IOException e) {
            vaciar = false;
            slow_print("ERR0R al cargar los datos: " + e.getMessage());
            e.printStackTrace();
        } catch(NullPointerException e) {
            vaciar = false;
            slow_print("ERR0R al cargar los datos debido a un dato nulo: " + e.getMessage());
            e.printStackTrace();

            for (String key : datos.keySet()) {
                if (datos.get(key) == null) {
                    slow_print("El valor de " + key + " es nulo." );
                }
            }
        }
    }

    public static void guardar_datos() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("datos.txt"))) {

            bw.write("cobre=" + cobre + "\n");
            bw.write("plata=" + plata + "\n");
            bw.write("oro=" + oro + "\n");
            bw.write("diamante=" + diamante + "\n\n");

            bw.write("mina_cobre=" + minas[0] + "\n");
            bw.write("mina_plata=" + minas[1] + "\n");
            bw.write("mina_oro=" + minas[2] + "\n");
            bw.write("mina_diamante=" + minas[3] + "\n\n");

            bw.write("mejora=" + mejora + "\n");
            bw.write("mejora_cobre=" + mejoras[0] + "\n");
            bw.write("mejora_plata=" + mejoras[1] + "\n");
            bw.write("mejora_oro=" + mejoras[2] + "\n");
            bw.write("mejora_diamante=" + mejoras[3] + "\n\n");

            bw.write("joyeria_normal=" + joyeria_normal + "\n");
            bw.write("joyeria_deluxe=" + joyeria_deluxe + "\n");
            bw.write("valor_normal=" + valor_joyeria_normal + "\n");
            bw.write("valor_deluxe=" + valor_joyeria_deluxe + "\n\n");

            bw.write("coste_normal_cobre=" + costo_normal[0] + "\n");
            bw.write("coste_normal_plata=" + costo_normal[1] + "\n");
            bw.write("coste_normal_oro=" + costo_normal[2] + "\n");
            bw.write("coste_normal_diamante=" + costo_normal[3] + "\n");

            bw.write("coste_deluxe_cobre=" + costo_deluxe[0] + "\n");
            bw.write("coste_deluxe_plata=" + costo_deluxe[1] + "\n");
            bw.write("coste_deluxe_oro=" + costo_deluxe[2] + "\n");
            bw.write("coste_deluxe_diamante=" + costo_deluxe[3] + "\n\n");

            bw.write("dinero=" + dinero);

        
        } catch (IOException e) {
            vaciar = false;
            slow_print("ERROR al guardar los datos: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public static String formatear(int numero) {
        NumberFormat formato = NumberFormat.getInstance(Locale.US);
        return formato.format(numero);
    }
    
    public static void vaciar_consola() {
        String os = System.getProperty("os.name");

        if (vaciar) {
            try {
                if (os.contains("Windows")) {
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                } else {
                    System.out.println("\033[H\033[2J");
                    System.out.flush();
                }
            } catch (Exception e) {
                vaciar = false;
                System.err.println("ERR0R al vaciar la consola: " + e.getMessage());;
            }
        }
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
            vaciar = false;
            System.err.println("ERR0R: " + e.getMessage());
        }
    }
    public static void slow_print(String frase, boolean ln) {
        try {
            slow_print(frase, ln, 15);
        } catch (InterruptedException e) {
            vaciar = false;
            System.err.println("ERR0R: " + e.getMessage());
        }
    }
    public static void slow_print(String frase) {
        try {
            slow_print(frase, true, 15);
        } catch (InterruptedException e) {
            vaciar = false;
            System.err.println("ERR0R: " + e.getMessage());
        }
    }   
}