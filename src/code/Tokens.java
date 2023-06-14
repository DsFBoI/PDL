import java.io.*;
import java.util.*;

public class Tokens {
    /*DECLARACION DE VARIABLES */
	Object cod;
	Object atr; 
    /* Palabra(almacena la palabra que estamos leyendo)
     * Resultado(si es un numero es lo que almacena el resultado)
     * contador asigna los ids
     */
    static String palabra = "";
    static int EstadoAct = 0;
    static int resultado = 0;
    static int contador = 0;
    static int contadorcad= 0;
    static int lineas = 0;
    static int saltos[] = new int[100];
    static int contadortok = 0;
    static int totaltok = 0;
    static String rutaL = "C:\\Users\\esthe\\Desktop\\upm\\tercero\\primer cuatri\\pdL\\practica\\primera entrega\\pruebas\\pruebas\\prueba_if.txt";
    static String rutaW = "C:\\Users\\esthe\\Desktop\\upm\\tercero\\primer cuatri\\pdL\\practica\\primera entrega\\pruebas\\pruebas\\prueba_if_sem.txt";
    static boolean checkpalabra = false;
    static boolean error = false;
    static String[]hola;

    /*
     * hacemos listas para las palabras reservadas(palreserv) 
     * para las palabras que son ids(listaids) 
     * para las palrabras que ya han sido usadas(listacheck)
     */
    static List<String> listacheck = new ArrayList<>();
    
    static Map<String, Integer> mapaid = new HashMap<>();

    static List<String> palreserv = new ArrayList<String>(){
        { /* Palabras resrvadas */
            add("if");
            add("let");
            add("int");
            add("input");
            add("return");
            add("boolean");
            add("String");
            add("string");
            add("while");
            add("false");
            add("true");
            add("print");
            add("function");
        }
    };
   
    
	public Tokens(Object cod, Object atr) {
		this.cod = cod; 
        this.atr = atr;
        
	}
	  
    public static int estado(int estado,char car){
        final char eof = (char)-1;
        /* Switch que marcara los estados */
        switch(estado) {
        case 0: 
        	/* automata el caso O */
            if(car == ' ' ||car == '\n' ||car == '\t' ||car == '\r' ||car == '\b'|| car == '	' ){ 	//delimitadores 
                return 0;

            }
            if(car == '/'){
                /* Automata 0->1 */
                return 1;

            }
            if(car == '\''){ 
                /* Automata 0->3 */
                return 3;

            }
            if (car == '(') {
                /* Automata 0->116 */
                escToken("apar", "-");   
                return 0;

            }
            if (car == ')') {
                /* Automata 0->117 */
                escToken("cpar", "-");                
                return 0;

            }
            if (car == '{') {
                /* Automata 0->114 */
                escToken("akey", "-");
                return 0; 

            }
            if (car == '}') {
                /* Automata 0->115 */
                escToken("ckey", "-");
                return 0;
     
            }
            if (car == ';') {
                /* Automata 0->112 */
                escToken("pcoma", "-");
                return 0; 

            }
            if (car == ',') {
                /* Automata 0->111 */
                escToken("coma", "-");
                return 0; 

            }
            if (car == '!') {
                /* Automata 0->106 */
                escToken("neg", "-");
                return 0; 

            }
            if (car == '+') {
                /* Automata 0->107 */
                escToken("sum", "-");
                return 0; 
            }
            if (car == '=') {
                /* Automata 0->6 */
                return 6;

            }
            if (car == '%') {
                /* Automata 0->7 */
                return 7;

            }
            if (car == eof) {
                /* Automata 0->118 */
                escToken("EOF", "-");
                return 0;

            }
            if(Character.toString(car).matches("[0-9]")){ 
                /* Automata 0->4 */
            	resultado += car;
                return 4;

			}
            if(Character.toString(car).matches("[a-zA-Z]")){
                /* Automata 0->5 */
            	palabra += car;
                return 5;
			}
            break;
        case 1:
            if(car == '/'){
                /* Automata 1->2 */
            	return 2; 

            }
            break;
        case 2:
            if(car == '\n'){
                /* Automata 2->0 */
            	return 0; 

            } 
            else{
                /* Automata 2->2 */
                return 2;

            } 
         
        case 3:
            if(car != '\''&& car != '\n'){
                /* Automata 3->3 */
                contadorcad++;
                if(contadorcad>256){
                    errores(5); 
                }
                
            	return 3; 

            }
            else if(car == '\n' ){
                
                errores(3);
                return 0;
            }
            else{
                /* Automata 3->103 */
            	escToken("cad","lexema");
                return 0; 

            }
       
        case 4: 
        	if(Character.toString(car).matches("[0-9]")){ 
                /* Automata 4->4 */
        		resultado = resultado*10 + car;
                if(resultado > 32767){
                    errores(6);
                }
        		return 4;

			}
        	else if (car == ' ' ||car == '\n' ||car == '\t' ||car == '\r' ||car == '\b'|| car == ';' ){
                /* Automata 4->104 */
                escToken("entera", "valor");
                EstadoAct = estado(0,car);
        	    return EstadoAct; 

            }
            else if(Character.toString(car).matches("[a-zA-Z]")){
                errores(4);
                return -1;
            }
            else{
                escToken("entera", "valor");
                EstadoAct = estado(0,car);
        	    return EstadoAct; 
            }
            
        case 5:
        	if(Character.toString(car).matches("[a-zA-Z]")||car == '_'){
                /* Automata 5->5 */
                palabra += car;
                return 5;

			}
            else if(Character.toString(car).matches("[0-9]")){ 
                /* Automata 5->8 */
            	palabra += car;
                return 8;

			}
        	
        	else{
                /* Automata 5->105 */
                
                escToken("id", contador+""); 
                EstadoAct = estado(0,car);
                return EstadoAct;
            }
         
        case 6:
            if (car == '=') {
            /* Automata 6->109 */
            escToken("ig2", "-");
            EstadoAct = estado(0, car);
            return 0 ;  	 
            }   
        	else{
                /* Automata 6->108 */
                escToken("eq", "-");  
                EstadoAct = estado(0, car);
                return EstadoAct; 
            }
        	
     
        case 7:
        	if (car == '=') {
                /* Automata 7->110 */
                escToken("asig", "-");
                return 0;
            	
            }
            else{
                error = true;
                errores(7);
                
            }
            break;
        case 8:
            if(Character.toString(car).matches("[0-9]") || Character.toString(car).matches("[a-zA-Z]")||car == '_' ){
                /* Automata 8->8 */
                palabra += car;
                return 8;

            }
            else{
                /* Automata 8->102 */
                String hola = "";
                hola += contador;
                
                escToken("id", hola ); 
                EstadoAct = estado(0, car);
                return EstadoAct; 
               

            }
         
    }    
	return 0;  
   }
   
   
    private static void errores(int a) {
        error = true;
        try(FileWriter fw = new FileWriter(new File("C:\\Users\\esthe\\Desktop\\upm\\tercero\\primer cuatri\\pdL\\practica\\primera entrega\\pruebas\\pruebas\\errores_tok.txt"), true);){
            PrintWriter writer = new PrintWriter(fw);

         
            
           
            switch(a){
                case 3:
                    writer.write("Error linea "+ lineas +": cierre con ' para generar el token cadena");    
                    return;
                case 4:   
                    writer.write("Error linea " + lineas +": Identificadores no pueden empezar por numeros");    
                    return; 
                case 5:
                    writer.write("Error linea " + lineas + ": cadena supera el limite de caracteres");
                    return;
                case 6:
                    writer.write("Error linea" + lineas + ": int fuera de rango");
                    return;
                case 7:
                    writer.write("Error linea" + lineas + ": falta de = para completar el token %=");    
                    return;
            
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    /*funcion para comprobar si es una palbra reservada */
    public static boolean check(String pal){
        boolean res = false;
        if(palreserv.contains(pal)) {
        	res = true;
        }
        return res;
    }

    
 /*funcion escribir token */
  public static void escToken(String cod,String atributo){
    	PrintWriter writer = null;
        checkpalabra = check(palabra);
		try {
			File file = new File(rutaW);
            // Si el archivo no existe es creado
            if (!file.exists()) {
                file.createNewFile();
            }
			FileWriter fw = new FileWriter(file, true);
			writer = new PrintWriter(fw);
            if (checkpalabra){ 
                /*si es una palabra reservada */       
               
                    /*si no esta en la lista de palabras ya escritas se escribe y la almecena en esta lista */
                	writer.write("<" + palabra + ",->\n");
                	listacheck.add(palabra);
                    contadortok ++;
                    checkpalabra = false;
                    
                
                
            }
            else{   
                /*si no es una palabra reservada  */     
                if(cod.equals("id")){
                    /*si no esta en la lista de palabras ya escritas se escribe y la almecena en esta lista  */
                    if(!mapaid.containsKey(palabra)){
                        contador++;
                        writer.write("<" + cod  + "," + contador + ">\n"); 
                        contadortok ++;
                        mapaid.put(palabra, contador);
                    }
                    else{
                        writer.write("<" + cod  + "," + mapaid.get(palabra) + ">\n"); 
                        contadortok ++;
                    }
                    

                }
                else{
                
                    writer.write("<" + cod + "," + atributo + ">\n");
                    contadortok ++;
                    listacheck.add(cod);
                }
            
                
            }
            /*rinicia palabra y resultado */
            palabra = "";
            resultado = 0;
        }
        
        catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
		if(writer!= null) {
			writer.close();
		}
        
    }

    public static void main(String[] args) throws IOException{
    	
    	/*
    	Scanner reader = new Scanner(System.in);
        System.out.println("ingrese el camino donde tiene el txt que quiere enviar: ");
        String path = null; 
        path = reader.nextLine(); 
        */
    	System.out.println("ejutando");
		try (FileReader fileReader = new FileReader(rutaL)) {

			int caracterleido = fileReader.read();
			while(caracterleido != -1 && !error) {
                /* Lector de carcateres */
				char car = (char) caracterleido;
                if(car == '\n'){
                    
                    saltos[lineas] = contadortok;
                    lineas++;
                    totaltok += contadortok;
                    contadortok = 0;
                }
                 
                EstadoAct = estado(EstadoAct, car);

                /*leemos el siguiente caracter */
                caracterleido = fileReader.read();  
			}
            saltos[lineas] = contadortok;
            totaltok += contadortok;
            
			
		}
		catch (IOException e) {
			e.printStackTrace();
	        System.exit(1);
	    }
        
        
		
	}
    
    
}