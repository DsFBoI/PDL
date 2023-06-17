package src.code;

//import javafx.util.Pair;

import javax.security.auth.callback.TextInputCallback;
import java.io.*;

import java.util.*;

public class Analizador {
    private static String parse = "Des ";
    private static int[] saltos;
    private static Pair<String, String> oldPair, oldPair2;
    private static Pair<String, String> newPair;
    private static Stack<Pair<String, String>> pila = new Stack();
    private static Stack<Pair<String, String>> pilaAux = new Stack();
    private static Stack<Pair<String, String>> pilaAux_errores = new Stack();
    private static Pair<String,String> pilaArriba;
    private  static  int contTok;
    private static BufferedReader br;
    private static boolean error = false;
    private static int parentesis = 0;
    private static int llaves = 0;
    private static int idPos;
    private static boolean declaracion = true;
    private static boolean function = false;
    private static int continua = 0;
    private static Map<Integer,String> mapa_id_pos = new HashMap<>();
    private static int tamanoTablaG = 0;
    private static int tamanoTablaL = 0;
    private static String tokenAct;
    private static String tokenErr;
    private static String EstadoErr;
    private static  String funcAct;
    private static String tipoReturn;
    private static String TsActual;
    private static HashMap<String, Dato_Tabla> tablaGlobal = new HashMap<>();
    private static ArrayList<Dato_Tabla> tablaGlobalarrayList = new ArrayList<>();
    private static HashMap<String, Dato_Tabla> tablaLocal = new HashMap<>();
    private static HashMap<String, ArrayList<String>> func_dec = new HashMap<>();
    private static ArrayList<String> lista_params = new ArrayList<>();
    private static ArrayList<String> lista_params_L = new ArrayList<>();
    private static ArrayList<String> lista_params_aux = new ArrayList<>();
    private static ArrayList<String> lista_params_aux2 = new ArrayList<>();
    private static Pair<String, String> tok_comprobacion;


    private static boolean before =false;
    private static int nig2=0;
    private static int num_func =0;
    private static String casoTipoId = "";


    static ArrayList<String> casosErroresFin = new ArrayList<String>(){
        {
            add("akey");
            add("ckey");
            add("pcoma");
            add("apar");
            add("cpar");
        }
    };


    static List<String> terminales = new ArrayList<String>(){
        { /* Palabras reservadas */
            add("if");
            add("let");
            add("int");
            add("input");
            add("return");
            add("boolean");
            add("string");
            add("while");
            add("false");
            add("true");
            add("print");
            add("function");
            add("akey");
            add("ckey");
            add("id");
            add("entera");
            add("cad");
            add("apar");
            add("cpar");
            add("ig2");
            add("eq");add("asig");add("pcoma"); add("sum"); add("neg");
        }
    };

    static List<String> terminalesFin = new ArrayList<String>(){
        { /* Palabras reservadas */
            add("if");
            add("let");
            add("input");
            add("return");
            add("while");
            add("print");
            add("function");
            //add("id");
        }
    };


    static List<String> accsem = new ArrayList<String>(){
        { /* acciones semanticas */
            add("1.1");add("2.1");add("3.1");add("3.2");add("4.1");add("4.2"); add("4.3");add("5.1");add("5.2");
            add("6.1");add("7.1");add("8.1");add("9.1");add("10.1");add("10.2");add("10.3");add("10.4");add("10.5");
            add("11.1");add("12.1");add("12.2");add("13.1");add("13.2");add("14.1");add("14.2");add("15.1");add("16.1");add("17.1");
            add("18.1");add("19.1");add("20.1");add("20.2");add("21.1");add("21.2");add("22.1"); add("23.1");add("24.1");add("24.2");add("24.3");
            add("25.1");add("25.2"); add("26.1");add("27.1");add("28.1");add("29.1");add("30.1");
            add("31.1");add("31.2");add("32.1");add("33.1");add("34.1");add("35.1");add("36.1");add("36.2");add("37.1");
            add("38.1");add("38.2");
        }
    };




    public static void main(String[] args) throws IOException {
        Tokens toke = new Tokens(0,0);
        Tokens.main(null);
        TsActual = "Global";
        saltos = toke.saltos;
        String[] split = new String[0];

        Map<String,Integer> mapaTokens = toke.mapaid;
        for(String key: mapaTokens.keySet()){
            mapa_id_pos.put(mapaTokens.get(key), key);

        }

        String tokenCogido="";

        try(FileReader fr = new FileReader("C:\\Users\\esthe\\Desktop\\upm\\tercero\\primer cuatri\\pdL\\practica\\entrega_Julio\\PDL\\src\\grmatica\\prueba_if_token.txt")){

            br = new BufferedReader(fr);
            pila.push(new Pair<String,String>("$", "-"));
            pila.push(new Pair<String,String>("S", "-"));

            while(br != null){
                contTok++;

                Pair<String,String> pop;
                while(accsem.contains(pila.peek().getKey())){
                    pop = pilaAux.push(pila.pop());
                    acciones_sem(pop.getKey());

                    //pop = pila.peek();
                }
                pilaArriba = pila.peek();
                if(!terminales.contains(pilaArriba.getKey())){

                    pop = pilaAux.push(pila.pop());
                    String estado = pop.getKey();
                    if(!error){
                        tokenCogido = br.readLine();
                        if(tokenCogido == null){
                            parse += " 3";
                            pilaAux.pop();
                            break;
                        }

                        tokenCogido = tokenCogido.replace("<", "");
                        tokenCogido = tokenCogido.replace(">",  "");
                        split = tokenCogido.split(",");

                        tokenAct = split[0];
                        switch(tokenAct){
                            case "apar":
                                ++parentesis;
                                break;
                            case "cpar":
                                --parentesis;
                                break;
                            case "akey":
                                ++llaves;
                                break;
                            case "ckey":
                                --llaves;
                                break;
                        }
                        tok_comprobacion = new Pair<>(tokenAct, "");
                        pilaAux_errores.push(tok_comprobacion);

                        if(tokenAct.equals("id")){
                            idPos = Integer.parseInt(split[1]);
                            System.out.println(mapa_id_pos.get(idPos));
                        }
                    }
                    else {
                        if(!terminalesFin.contains(tokenAct)){
                            while(!terminalesFin.contains(tokenAct)){
                                tokenCogido = br.readLine();
                                if(tokenCogido == null){
                                    parse += " 3";
                                    pilaAux.pop();
                                    break;
                                }

                                tokenCogido = tokenCogido.replace("<", "");
                                tokenCogido = tokenCogido.replace(">",  "");
                                split = tokenCogido.split(",");

                                tokenAct = split[0];
                                switch(tokenAct){
                                    case "apar":
                                        ++parentesis;
                                        break;
                                    case "cpar":
                                        --parentesis;
                                        break;
                                    case "akey":
                                        ++llaves;
                                        break;
                                    case "ckey":
                                        --llaves;
                                        break;
                                }

                            }
                        }
                        error = false ;
                    }
                    if(tokenCogido != null)
                        readToken(tokenAct,estado);
                }else{
                    tokenCogido = br.readLine();
                    if(tokenCogido == null && pilaArriba.getKey().equals("$")){
                        parse += " 3";
                        break;
                    }else if(tokenCogido == null && !pilaArriba.getKey().equals("$")){
                        //error de que ha finalizado cuando no debia
                        parse += " 3";
                        break;
                    }

                    tokenCogido = tokenCogido.replace("<", "");
                    tokenCogido = tokenCogido.replace(">", "");
                    split = tokenCogido.split(",");

                    tokenAct = split[0];


                    if(tokenAct.equals("id")){
                        idPos = Integer.parseInt(split[1]);
                        System.out.println(mapa_id_pos.get(idPos));
                    }

                    equiparar(tokenAct,pila.peek().getKey());

                    switch(tokenAct){
                        case "apar":
                            ++parentesis;
                            break;
                        case "cpar":
                            --parentesis;
                            break;
                        case "akey":
                            ++llaves;
                            break;
                        case "ckey":
                            --llaves;
                            break;
                    }
                }


            }

            if(parentesis != 0){
                //error diferencia de parentesis
                errores(2);
            }
            if(llaves != 0 ){
                // error diferencia de llaves
                errores(3);
            }

            try (FileWriter fw = new FileWriter(new File("C:\\Users\\esthe\\Desktop\\upm\\tercero\\primer cuatri\\pdL\\practica\\entrega_Julio\\PDL\\src\\parse.txt"), true);){
                fw.write(parse);


            } catch (Exception e) {
                System.out.println("no funciona el parse");
            }
            System.out.println(parse);

            if(pila.size() > 1){
                if(terminales.contains(pila.peek()))
                    errores(4);
            }
            while(!pila.empty()&&accsem.contains(pila.peek().getKey())){
                acciones_sem(pilaAux.push(pila.pop()).getKey());
            }
            printTGlobal();

        }catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }


    }

    public static int linea_actu(int cont_token){
        int linea = 1;
        int tokens = 0;
        for(int i = 0 ; i < saltos.length ; i++ ){
            tokens += saltos[i];
            if(tokens < cont_token-1){
                linea = i+1;

            }else  if(tokens == cont_token-1){
                linea = i+1;
                break;
            }else if( i+1 != saltos.length && tokens < cont_token-1+ saltos[i]){
                linea = i+1;
                break;
            }

        }
        return linea;
    }


    private static void readToken(String tokenAct, String estado) {
        switch (estado){
            case "S":
                caseS(tokenAct);
                if(error)
                    break;
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            case "B":
                caseB(tokenAct);
                if(error)
                    break;
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            case "T":
                caseT(tokenAct);
                if(error)
                    break;
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            case "F":
                caseF(tokenAct);
                if(error)
                    break;
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            case "G":
                caseG(tokenAct);
                if(error)
                    break;
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            case "H":
                caseH(tokenAct);
                if(error)
                    break;
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            case "N":
                caseN(tokenAct);
                if(error)
                    break;
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            case "M":
                caseM(tokenAct);
                if(error)
                    break;
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            case "D":
                caseD(tokenAct);
                if(error)
                    break;
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            case "K":
                caseK(tokenAct);
                if(error)
                    break;
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            case "I":
                caseI(tokenAct);
                if(error)
                    break;
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            case "L":
                caseL(tokenAct);
                if(error)
                    break;
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            case "A":
                caseA(tokenAct);
                if(error)
                    break;
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            case "X":
                caseX(tokenAct);
                if(error)
                    break;
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            case "R":
                caseR(tokenAct);
                if(error)
                    break;
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            case "P":
                caseP(tokenAct);

                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;

                break;

            case "J":
                caseJ(tokenAct);
                if(error)
                    break;
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            case "Y":
                caseY(tokenAct);
                if(error)
                    break;
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            case "V":
                caseV(tokenAct);
                if(error)
                    break;
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            case "Z":
                caseZ(tokenAct);
                if(error)
                    break;
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            default:
                if(!accsem.contains(estado)) {
                    equiparar(tokenAct, estado);
                }
        }

    }
    private static void caseS(String tokenAct) {
        if ("function".equals(tokenAct)) {/* S -> F S {1.1}*/
            parse += " 2";
            pila.push(new Pair<>("1.1", "-"));
            pila.push(new Pair<>("S", "-"));
            pila.push(new Pair<>("F", "-"));
        } else {/*S -> B S*/
            parse += " 1";
            pila.push(new Pair<>("2.1", "-"));
            pila.push(new Pair<>("S", "-"));
            pila.push(new Pair<>("B", "-"));
        }
    }

    private static void caseB(String tokenAct) {
        switch (tokenAct) {
            case "if":
                /* B -> if ( R ) {3.1} D {3.2} */
                parse += " 5";
                pila.push(new Pair<>("3.2", "-"));
                pila.push(new Pair<>("D", "-"));
                pila.push(new Pair<>("3.1", "-"));
                pila.push(new Pair<>("cpar", "-"));
                pila.push(new Pair<>("R", "-"));
                pila.push(new Pair<>("apar", "-"));
                pila.push(new Pair<>("if", "-"));
                break;
            case "let":
                /* B -> {4.1} let id T {4.2} ; {4.3} */
                parse += " 6";
                pila.push(new Pair<>("4.3", "-"));
                pila.push(new Pair<>("pcoma", "-"));
                pila.push(new Pair<>("4.2", "-"));
                pila.push(new Pair<>("T", "-"));
                pila.push(new Pair<>("id", "-"));
                pila.push(new Pair<>("let", "-"));
                pila.push(new Pair<>("4.1", "-"));
                break;
            case "while":
                /*B ->  while ( R ) {5.1} { M } {5.2}*/
                parse += " 7";
                pila.push(new Pair<>("5.2", "-"));
                pila.push(new Pair<>("ckey", "-"));
                pila.push(new Pair<>("M", "-"));
                pila.push(new Pair<>("akey", "-"));
                pila.push(new Pair<>("5.1", "-"));
                pila.push(new Pair<>("cpar", "-"));
                pila.push(new Pair<>("R", "-"));
                pila.push(new Pair<>("apar", "-"));
                pila.push(new Pair<>("while", "-"));
                break;
            default :
                /* B-> K {6.1}*/
                parse += " 4";
                pila.push(new Pair<>("6.1", "-"));
                pila.push(new Pair<>("K", "-"));
                break;
        }
    }

    private static void caseT(String tokenAct) {
        switch (tokenAct) {
            case "int":
                /* T -> int {7.1}*/
                parse += " 8";
                pila.push(new Pair<>("7.1", "-"));
                pila.push(new Pair<>("int", "-"));
                break;
            case "string":
                /* T -> string {8.1}  */
                parse += " 9";
                pila.push(new Pair<>("8.1", "-"));
                pila.push(new Pair<>("string", "-"));
                break;
            case "boolean":
                /*T-> boolean {9.1}*/
                parse += " 10";
                pila.push(new Pair<>("9.1", "-"));
                pila.push(new Pair<>("boolean", "-"));
                break;

            default:
                tokenErr = tokenAct;
                EstadoErr = "boolean String Int";
                error = true;
                errores(1);
                break;
        }
    }

    private static void caseF(String tokenAct) {
        /* F -> {10.1} function id {10.2} G ( H ) {10.3} { M {10.4} } {10.5}*/
        if(tokenAct.equals("function")){
            pila.push(new Pair<>("10.5", "-"));
            pila.push(new Pair<>("ckey", "-"));
            pila.push(new Pair<>("10.4", "-"));
            pila.push(new Pair<>("M", "-"));
            pila.push(new Pair<>("akey", "-"));
            pila.push(new Pair<>("10.3", "-"));
            pila.push(new Pair<>("cpar", "-"));
            pila.push(new Pair<>("H", "-"));
            pila.push(new Pair<>("apar", "-"));
            pila.push(new Pair<>("G", "-"));
            pila.push(new Pair<>("10.2", "-"));
            pila.push(new Pair<>("id", "-"));
            pila.push(new Pair<>("function", "-"));
            pila.push(new Pair<>("10.1", "-"));
        }
        parse += " 11";
    }

    private static void caseG(String tokenAct) {
        if(tokenAct.equals("int") || tokenAct.equals("string") || tokenAct.equals("boolean")){
            /* G -> T {11.1}*/
            parse += " 12";
            pila.push(new Pair<>("11.1", "-"));
            pila.push(new Pair<>("T", "-"));
        }
        else {
            /*G -> labmda*/
            parse += " 13";
        }
    }

    private static void caseH(String tokenAct) {
        /* H -> T id {12.1} N {12.2} */
        if(tokenAct.equals("int") || tokenAct.equals("string") || tokenAct.equals("boolean")){
            parse += " 14";
            pila.push(new Pair<>("12.2", "-"));
            pila.push(new Pair<>("N", "-"));
            pila.push(new Pair<>("12.1", "-"));
            pila.push(new Pair<>("id", "-"));
            pila.push(new Pair<>("T", "-"));
        }
        else{
            parse += " 15";
        }
    }

    private static void caseN(String tokenAct) {
        switch (tokenAct){
            /* N -> , T id {13.1} K {13.2}*/
            case "coma":
                parse += " 17";
                pila.push(new Pair<>("13.2", "-"));
                pila.push(new Pair<>("N", "-"));
                pila.push(new Pair<>("13.1", "-"));
                pila.push(new Pair<>("id", "-"));
                pila.push(new Pair<>("T", "-"));
                pila.push(new Pair<>("coma", "-"));
                break;
            default:
                parse += " 16";
                break;
        }
    }

    private static void caseM(String tokenAct) {
        /* M -> B {14.1} M {14.2}*/
        if(tokenAct.equals("if")||tokenAct.equals("let")||tokenAct.equals("while")||tokenAct.equals("return")
                ||tokenAct.equals("print")||tokenAct.equals("input")||tokenAct.equals("id")){
            parse += " 18";
            pila.push(new Pair<>("14.2", "-"));
            pila.push(new Pair<>("M", "-"));
            pila.push(new Pair<>("14.1", "-"));
            pila.push(new Pair<>("B", "-"));
        }
        else{
            /* M -> lambda*/
            parse += " 19";
        }
    }

    private static void caseD(String tokenAct) {
        switch (tokenAct){
            /*D -> { M } {15.1} */
            case "akey":
                parse += " 20";
                pila.push(new Pair<>("15.1", "-"));
                pila.push(new Pair<>("ckey", "-"));
                pila.push(new Pair<>("M", "-"));
                pila.push(new Pair<>("akey", "-"));
                break;

            default:
                /*D -> K {16.1}*/
                parse += " 21";
                pila.push(new Pair<>("16.1", "-"));
                pila.push(new Pair<>("K", "-"));
                break;
        }
    }

    private static void caseK(String tokenAct) {
        switch (tokenAct){
            case "return":
                parse += " 22";
                /* K -> return X ; {17.1}*/
                pila.push(new Pair<>("17.1", "-"));
                pila.push(new Pair<>("pcoma", "-"));
                pila.push(new Pair<>("X", "-"));
                pila.push(new Pair<>("return", "-"));
                break;

            case "print":
                /* K -> print ( R ) ; {18.1}*/
                parse += " 23";
                pila.push(new Pair<>("18.1", "-"));
                pila.push(new Pair<>("pcoma", "-"));
                pila.push(new Pair<>("cpar", "-"));
                pila.push(new Pair<>("R", "-"));
                pila.push(new Pair<>("apar", "-"));
                pila.push(new Pair<>("print", "-"));
                break;

            case "input":
                /* K -> input id ; {19.1}*/
                parse += " 24";
                pila.push(new Pair<>("19.1", "-"));
                pila.push(new Pair<>("pcoma", "-"));
                pila.push(new Pair<>("id", "-"));
                pila.push(new Pair<>("input", "-"));
                break;
            /* K -> id {20.1} I {20.2}*/
            case "id":
                parse += " 25";
                pila.push(new Pair<>("20.2", "-"));
                pila.push(new Pair<>("I", "-"));
                pila.push(new Pair<>("20.1", "-"));
                pila.push(new Pair<>("id", "-"));
                break;
        }
    }

    private static void caseI(String tokenAct) {
        switch (tokenAct){
            case "apar":
                /* I -> {21.1} ( L ) ;  {21.2} */
                parse += " 26";
                pila.push(new Pair<>("21.2", "-"));
                pila.push(new Pair<>("pcoma", "-"));
                pila.push(new Pair<>("cpar", "-"));
                pila.push(new Pair<>("L", "-"));
                pila.push(new Pair<>("apar", "-"));
                pila.push(new Pair<>("21.1", "-"));
                break;
            case "asig":
                /* I -> %= R ; {22.1} */
                parse += " 28";
                pila.push(new Pair<>("22.1", "-"));
                pila.push(new Pair<>("pcoma", "-"));
                pila.push(new Pair<>("R", "-"));
                pila.push(new Pair<>("asig", "-"));
                break;
            case "eq":
                /* I -> = R ; {23.1} */
                parse += " 27";
                pila.push(new Pair<>("23.1", "-"));
                pila.push(new Pair<>("pcoma", "-"));
                pila.push(new Pair<>("R", "-"));
                pila.push(new Pair<>("eq", "-"));
                break;

        }
    }

    private static void caseL(String tokenAct) {
        /*L -> {24.1} R {24.2} A {24.3}*/
        if ("id".equals(tokenAct)||"apar".equals(tokenAct)||"entera".equals(tokenAct)||"true".equals(tokenAct)||"false".equals(tokenAct)||"cad".equals(tokenAct)||"neg".equals(tokenAct)) {
            parse += " 29";
            pila.push(new Pair<>("24.3", "-"));
            pila.push(new Pair<>("A", "-"));
            pila.push(new Pair<>("24.2", "-"));
            pila.push(new Pair<>("R", "-"));
            pila.push(new Pair<>("24.1", "-"));
        } else {
            parse += " 30";
        }
    }

    private static void caseA(String tokenAct) {
        /*A -> , R {25.1} A {25.2} */
        if ("coma".equals(tokenAct)){
            parse += " 31";
            pila.push(new Pair<>("25.2", "-"));
            pila.push(new Pair<>("A", "-"));
            pila.push(new Pair<>("25.1", "-"));
            pila.push(new Pair<>("R", "-"));
            pila.push(new Pair<>("coma", "-"));
        }else {
            parse += " 32";
        }
    }

    private static void caseX(String tokenAct) {
        /*X -> R {26.1}*/
        if ("id".equals(tokenAct)||"apar".equals(tokenAct)||"entera".equals(tokenAct)||"true".equals(tokenAct)||"false".equals(tokenAct)||"cad".equals(tokenAct)||"neg".equals(tokenAct)) {
            parse += " 33";
            pila.push(new Pair<>("26.1", "-"));
            pila.push(new Pair<>("R", "-"));
        } else {
            parse += " 34";
        }
    }

    private static void caseR(String tokenAct){
        /*R -> J P {27.1}*/
        parse += " 35";
        pila.push(new Pair<>("27.1", "-"));
        pila.push(new Pair<>("P", "-"));
        pila.push(new Pair<>("J", "-"));
    }

    private static void caseP(String tokenAct) {
        /*P -> == J P {28.1} */
        if ("ig2".equals(tokenAct)) {

            parse += " 36";
            pila.push(new Pair<>("28.1", "-"));
            pila.push(new Pair<>("P", "-"));
            pila.push(new Pair<>("J", "-"));
            pila.push(new Pair<>("ig2", "-"));
        } else {
            while(accsem.contains(pila.peek().getKey())){
                pilaAux.push(pila.pop());
            }
            parse += " 37";
        }
    }

    private static void caseJ(String tokenAct) {
        /*J -> V Y {29.1} */
        parse += " 38";
        pila.push(new Pair<>("29.1", "-"));
        pila.push(new Pair<>("Y", "-"));
        pila.push(new Pair<>("V", "-"));
    }

    private static void caseY(String tokenAct) {
        /*Y -> + V {30.1} Y {30.2}*/
        if ("sum".equals(tokenAct)) {
            parse += " 39";
            pila.push(new Pair<>("30.2", "-"));
            pila.push(new Pair<>("Y", "-"));
            pila.push(new Pair<>("30.1", "-"));
            pila.push(new Pair<>("V", "-"));
            pila.push(new Pair<>("sum", "-"));
        } else {
            parse += " 40";
        }
    }

    private static void caseV(String tokenAct) {
        switch (tokenAct) {
            /*V -> id {31.1} Z {31.2}*/
            case "id":
                parse += " 41";
                pila.push(new Pair<>("31.2", "-"));
                pila.push(new Pair<>("Z", "-"));
                pila.push(new Pair<>("31.1", "-"));
                pila.push(new Pair<>("id", "-"));
                break;
            case "apar":
                /*V -> ( R ) {32.2}*/
                parse += " 42";
                pila.push(new Pair<>("32.1", "-"));
                pila.push(new Pair<>("cpar", "-"));
                pila.push(new Pair<>("R", "-"));
                pila.push(new Pair<>("apar", "-"));
                break;

            case "entera":
                /*V->entera*/
                parse += " 43";
                pila.push(new Pair<>("33.1", "-"));
                pila.push(new Pair<>("entera", "-"));
                break;
            case "true":
                parse += " 44";
                pila.push(new Pair<>("34.1", "-"));
                pila.push(new Pair<>("true", "-"));
                break;
            case "false":
                parse += " 45";
                pila.push(new Pair<>("35.1", "-"));
                pila.push(new Pair<>("false", "-"));
                break;
            case "neg":
                /*V -> ! id {36.1} Z {36.2}*/
                parse += " 46";
                pila.push(new Pair<>("36.2", "-"));
                pila.push(new Pair<>("Z", "-"));
                pila.push(new Pair<>("36.1", "-"));
                pila.push(new Pair<>("id", "-"));
                pila.push(new Pair<>("neg", "-"));
                break;
            case "cad":
                parse += " 47";
                pila.push(new Pair<>("37.1", "-"));
                pila.push(new Pair<>("cad", "-"));
                break;
            default:
                errores(5);
        }
    }

    private static void caseZ(String tokenAct) {
        /* Z -> ( {38.1} L ) {38.2} */
        if ("apar".equals(tokenAct)) {
            parse += " 48";
            pila.push(new Pair<>("38.2", "-"));
            pila.push(new Pair<>("cpar", "-"));
            pila.push(new Pair<>("L", "-"));
            pila.push(new Pair<>("38.1", "-"));
            pila.push(new Pair<>("apar", "-"));
        } else {
            while(accsem.contains(pila.peek().getKey())){
                String acc = pilaAux.push(pila.pop()).getKey();
                acciones_sem(acc);
            }
            parse += " 49";
        }
    }

    private static void continuar(String estado){
        while(accsem.contains(pila.peek().getKey())){
            acciones_sem(pilaAux.push(pila.pop()).getKey());
        }
        if(!terminales.contains(pila.peek().getKey())&&!accsem.contains(pila.peek().getKey())){
            continua++;
            estado = pilaAux.push(pila.pop()).getKey();
            readToken(tokenAct, estado);
        }


    }

    private static void equiparar(String token, String estado) {
        if (continua == 0 ) {
            if (token.equals(estado)) {
                if(token.equals("id")){
                    pilaAux.push(new Pair<>(pila.pop().getKey(),""+idPos));
                }
                else{
                    pilaAux.push(pila.pop());
                }

            } else {

                if (!accsem.contains(estado) && !accsem.contains(token)) {

                    tokenErr = token;
                    EstadoErr = estado;
                    errores(1);
                }

            }
        }
    }
 private static void errores(int coderror){
        try(FileWriter fw = new FileWriter(new File("C:\\Users\\esthe\\Desktop\\upm\\tercero\\primer cuatri\\pdL\\practica\\entrega_Julio\\PDL\\src\\erroresSin.txt"), true);){

            PrintWriter writer2 = new PrintWriter(fw);

            switch(coderror){
                case 1:
                    writer2.write("Error en la linea " + linea_actu(contTok)+" : " + "Se esperaba "+ EstadoErr  + " se obtuvo " + tokenErr +"\n" );

                    while(!pila.peek().getKey().equals("2.1") ){
                        pila.pop();
                    }
                    pila.pop();
                    pila.push(new Pair<>("S","-"));
                    while(!pilaAux.peek().getKey().equals("S")){
                        pilaAux.pop();
                    }
                    pilaAux.pop();

                    error =true;
                    break;
                case 2:
                    if(parentesis<0)
                        writer2.write("Error:  mayor numero de cierre de parentesis" );
                    else
                        writer2.write("Error:  mayor numero de apertura de parentesis  " );
                    break;

                case 3:
                    if(llaves<0)
                        writer2.write("Error:  mayor numero de cierre de llaves" );
                    else
                        writer2.write("Error:  mayor numero de apertura de llaves  " );
                    break;

                case 4:
                    if(pila.size()!= 1){
                        writer2.write("Falta el token : "+ pila.pop().getKey() + "\n" );
                    }
                    break;
                case 5:
                    error = true;
                    writer2.write("Error en la linea: "  + linea_actu(contTok)+ " necesario una declaracion entre parentesis"  + "\n" );
                    break ;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    public static void  printTGlobal(){

        try(FileWriter fw = new FileWriter(new File("C:\\Users\\esthe\\Desktop\\upm\\tercero\\primer cuatri\\pdL\\practica\\entrega_Julio\\PDL\\src\\TSimbolos.txt"), true);){

            for (Dato_Tabla dt: tablaGlobalarrayList) {
                String entrada = dt.getLexema() + " " +dt.getTipo() + " " + dt.getDirec()+"\n";
                fw.write(entrada);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

    }
    private static void acciones_sem(String token){
        switch(token) {
            case "1.1":
                oldPair = pilaAux.get(pilaAux.size() - 3);
                if (pilaAux.get(pilaAux.size() - 2).getValue().equals("tipo_error")) {
                    errores_sem(1);
                    newPair = new Pair<>(oldPair.getKey(), "tipo_error");
                } else {
                    newPair = new Pair<>(oldPair.getKey(), "tipo_OK");
                }
                pilaAux.set(pilaAux.size() - 3, newPair);
                destroyPilaAux(3);
                break;

            case "2.1":
                oldPair = pilaAux.get(pilaAux.size() - 3);
                if (pilaAux.get(pilaAux.size() - 2).getValue().equals("tipo_error")) {
                    errores_sem(1);
                    newPair = new Pair<>(oldPair.getKey(), "tipo_error");
                } else {
                    newPair = new Pair<>(oldPair.getKey(), "tipo_OK");
                }
                pilaAux.set(pilaAux.size() - 3, newPair);
                destroyPilaAux(3);
                break;

            case "3.1":
                /* B -> if ( R ) {3.1} D {3.2} */
                oldPair = pilaAux.get(pilaAux.size() - 6);
                if (pilaAux.get(pilaAux.size() - 3).getValue().equals("boolean")) {
                    newPair = new Pair<>(oldPair.getKey(), "tipo_ok");

                } else {
                    errores_sem(3);
                    newPair = new Pair<>(oldPair.getKey(), "tipo_error");
                }
                pilaAux.set(pilaAux.size() - 6, newPair);
                break;

            case "3.2":
                if (pilaAux.get(pilaAux.size() - 3).getValue().equals("tipo_error")) {
                    errores_sem(1);
                    oldPair = pilaAux.get(pilaAux.size() - 8);
                    newPair = new Pair<>(oldPair.getKey(), "tipo_error");
                    pilaAux.set(pilaAux.size() - 8, newPair);
                }
                destroyPilaAux(7);
                break;

            case "4.1":
                declaracion = true;
                break;

            case "4.2":
                declaracion = false;
                String value = pilaAux.get(pilaAux.size() - 3).getValue();
                if (value.equals("entera")) {
                    value = "int";
                } else if (value.equals("cad")) {
                    value = "string";
                }
                if (TsActual.equals("Global")) {
                    if (!tablaGlobal.containsKey(mapa_id_pos.get(idPos))) {
                        Dato_Tabla add = new Dato_Tabla(mapa_id_pos.get(idPos), value, tamanoTablaG);
                        tablaGlobal.put(mapa_id_pos.get(idPos), add);
                        tablaGlobalarrayList.add(add);
                        anadirDesp(pilaAux.get(pilaAux.size() - 3).getValue());
                    }
                } else {
                    if (!tablaLocal.containsKey(mapa_id_pos.get(idPos))) {
                        Dato_Tabla add = new Dato_Tabla(mapa_id_pos.get(idPos), value, tamanoTablaG);
                        tablaGlobal.put(mapa_id_pos.get(idPos), add);
                        tablaGlobalarrayList.add(add);
                        anadirDesp(pilaAux.get(pilaAux.size() - 3).getValue());
                    }
                }
                break;

            case "4.3":
                destroyPilaAux(8);
                break;

            /*B -> while ( E ) {5.1} { M } {5.2}*/
            case "5.1":     //
                oldPair = pilaAux.get(pilaAux.size() - 6);
                if (!pilaAux.get(pilaAux.size() - 3).getValue().equals("boolean")) {
                    errores_sem(3);
                    newPair = new Pair<>(oldPair.getKey(), "tipo_error");
                } else {
                    newPair = new Pair<>(oldPair.getKey(), "tipo_ok");
                }
                pilaAux.set(pilaAux.size() - 6, newPair);
                break;

            case "5.2":     //
                destroyPilaAux(9);
                break;


            case "6.1":     //* B->  K {6.1} */
                if (pilaAux.get(pilaAux.size() - 2).getValue().equals("tipo_error")) {  // si tipo_K = tipo_error
                    errores_sem(4);
                    newPair = new Pair<>(pilaAux.get(pilaAux.size() - 3).getKey(), "tipo_error"); // ponemos B a tipo_error
                } else {  // si k_tipo no es igual a tipo_error
                    newPair = new Pair<>(pilaAux.get(pilaAux.size() - 3).getKey(), "tipo_ok");    // ponemos B a tipo_ok
                }
                pilaAux.set(pilaAux.size() - 3, newPair);
                destroyPilaAux(2);
                break;

            case "7.1":
                oldPair = pilaAux.get(pilaAux.size() - 3);
                newPair = new Pair<>(oldPair.getKey(), "int");
                pilaAux.set(pilaAux.size() - 3, newPair);
                destroyPilaAux(1);
                break;

            case "8.1":
                oldPair = pilaAux.get(pilaAux.size() - 3);
                newPair = new Pair<>(oldPair.getKey(), "string");
                pilaAux.set(pilaAux.size() - 3, newPair);
                destroyPilaAux(1);
                break;

            case "9.1":
                /*T-> boolean {9.1}*/
                oldPair = pilaAux.get(pilaAux.size() - 3);
                newPair = new Pair<>(oldPair.getKey(), "boolean");
                pilaAux.set(pilaAux.size() - 3, newPair);
                destroyPilaAux(1);
                break;
            //  F -> {10.1} function id {10.2} G ( H ) {10.3} {  M {10.4} } {10.5}
            case "10.1":
                declaracion = true;
                num_func++;
                break;
            case "10.2":
                TsActual = "Local";
                tablaLocal = new HashMap<>();
                tamanoTablaL = 0;
                funcAct = mapa_id_pos.get(idPos);
                break;

            case "10.3":
                declaracion = false;
                func_dec.put(funcAct, lista_params);

                tipoReturn = pilaAux.get(pilaAux.size() - 5).getValue();
                if (tipoReturn.equals("-")) {
                    tipoReturn = "void";
                }

                // añade la funcion en la TS
                tablaGlobal.put(funcAct, new Dato_Tabla(funcAct, "function", lista_params.size(), lista_params.toString(), tipoReturn, "func" + num_func));
                tablaGlobalarrayList.add(new Dato_Tabla(funcAct, "function", lista_params.size(), lista_params.toString(), tipoReturn, "func" + num_func));
                lista_params.clear();
                break;

            case "10.4":
                // tenemos que ver si el tipo ret  de M es igual al tipo de G
                String m_tipo = pilaAux.get(pilaAux.size() - 2).getValue();
                oldPair = pilaAux.get(pila.size() - 11);
                if (!tipoReturn.equals("void") && !tipoReturn.equals(m_tipo)) {
                    newPair = new Pair<>(pilaAux.get(pila.size() - 13).getKey(), "tipo_error");
                } else {
                    newPair = new Pair<>(pilaAux.get(pila.size() - 13).getKey(), "tipo_ok");
                }
                pilaAux.set(pilaAux.size() - 13, newPair);

                break;
            case "10.5":
                // pop de las acciones
                TsActual = "Global";
                // destruir tabla de simbolos:
                tablaLocal.clear();
                tamanoTablaL = 0;

                destroyPilaAux(14);
                break;

            case "11.1":    // G -> T 11.1
                oldPair = pilaAux.get(pila.size() - 2);
                String val = oldPair.getValue();
                newPair = new Pair<>(pilaAux.get(pila.size() - 3).getKey(), val);
                pilaAux.set(pilaAux.size() - 3, newPair);
                destroyPilaAux(1);
                break;

            // T id 12.1  N 12.2
            case "12.1":
                String tipoParam = pilaAux.get(pilaAux.size() - 2).getValue();
                lista_params.add(tipoParam);
                break;

            case "12.2":
                destroyPilaAux(5);
                break;

            // , T id N
            case "13.1":
                String tipoParam_rec = pilaAux.get(pilaAux.size() - 2).getValue();
                lista_params.add(tipoParam_rec);
                break;

            case "13.2":
                destroyPilaAux(6);
                break;

            case "14.1":
                /* M -> B {14.1} M {14.2}*/
                oldPair = pilaAux.get(pila.size() - 2);
                if (pilaAux.get(pilaAux.size() - 2).getValue().equals("tipo_error")) {
                    newPair = new Pair<>(pilaAux.get(pila.size() - 3).getKey(), "tipo_error");
                } else {
                    newPair = new Pair<>(pilaAux.get(pila.size() - 3).getKey(), oldPair.getValue());
                }
                pilaAux.set(pilaAux.size() - 3, newPair);
                break;
            case "14.2":
                // { E.tipo = (if (I.tipo != void) Then I.tipo Else R.tipo); Pop.Aux(2) }
                /* M -> B {14.1} M {14.2}*/
                oldPair = pilaAux.get(pilaAux.size() - 2);
                if (!pilaAux.get(pilaAux.size() - 2).getValue().equals("void")) {
                    newPair = new Pair<>(pilaAux.get(pila.size() - 5).getKey(), oldPair.getValue());
                } else {
                    oldPair = pilaAux.get(pilaAux.size() - 4);
                    newPair = new Pair<>(pilaAux.get(pila.size() - 5).getKey(), oldPair.getValue());
                }
                pilaAux.set(pilaAux.size() - 3, newPair);
                destroyPilaAux(4);
                break;

            case "15.1":
                /*D -> { M } {15.1} */
                oldPair = pilaAux.get(pila.size() - 3);
                if (pilaAux.get(pilaAux.size() - 3).getValue().equals("tipo_error")) {
                    newPair = new Pair<>(pilaAux.get(pila.size() - 5).getKey(), "tipo_error");
                    errores_sem(1);
                } else {
                    newPair = new Pair<>(pilaAux.get(pila.size() - 5).getKey(), oldPair.getValue());
                }
                pilaAux.set(pilaAux.size() - 5, newPair);
                destroyPilaAux(4);
                break;

            case "16.1":
                /*D -> K {16.1} */
                oldPair = pilaAux.get(pila.size() - 2);
                if (pilaAux.get(pilaAux.size() - 2).getValue().equals("tipo_error")) {
                    newPair = new Pair<>(pilaAux.get(pila.size() - 5).getKey(), "tipo_error");
                    errores_sem(1);
                } else {
                    newPair = new Pair<>(pilaAux.get(pila.size() - 3).getKey(), oldPair.getValue());
                }
                pilaAux.set(pilaAux.size() - 3, newPair);
                destroyPilaAux(2);
                break;

            // casos de K:
            //
            case "17.1":
                /* K -> return X  ; {17.1}*/
                oldPair = pilaAux.get(pila.size() - 3);     // X
                oldPair2 = pilaAux.get(pila.size() - 5);    // K

                idPos = Integer.parseInt(pilaAux.get(pilaAux.size() - 3).getValue());
                tipoReturn = tablaGlobal.get(mapa_id_pos.get(idPos)).getTipoDev();

                if (oldPair.getValue().equals("tipo_error")) {
                    errores_sem(2);
                    newPair = new Pair<>(oldPair2.getKey(), "tipo_error");
                } else if (function) {
                    if (oldPair.getValue().equals(tipoReturn)) {
                        newPair = new Pair<>(oldPair2.getKey(), "tipo_ok");
                    } else {
                        newPair = new Pair<>(oldPair2.getKey(), "tipo_error");

                    }
                } else {
                    newPair = new Pair<>(oldPair2.getKey(), "tipo_ok");
                }
                pilaAux.set(pilaAux.size() - 5, newPair);   // ponemos K a
                destroyPilaAux(4);
                break;
            case "18.1":
                // K -> print ( R ) ; {18.1}
                String r_tipo = pilaAux.get(pilaAux.size() - 4).getValue();
                oldPair = pilaAux.get(pila.size() - 7); // par de K
                if (r_tipo.equals("tipo_error")) {
                    newPair = new Pair<>(pilaAux.get(pila.size() - 3).getKey(), "tipo_error");
                } else {
                    newPair = new Pair<>(pilaAux.get(pila.size() - 3).getKey(), "tipo_ok");
                }
                pilaAux.set(pilaAux.size() - 7, newPair);
                destroyPilaAux(7);
                break;

            case "19.1":
                // K -> input id ; {19.1}
                oldPair = pilaAux.get(pila.size() - 5); // par de K
                newPair = new Pair<>(pilaAux.get(pila.size() - 3).getKey(), "tipo_ok");
                pilaAux.set(pilaAux.size() - 5, newPair);
                destroyPilaAux(5);

                break;

            // K ->  id {20.1} I {20.2}
            case "20.1":
                // si no existe poner a int y añadir a la tabla de simbolos
                oldPair = pilaAux.get(pilaAux.size() - 3); // K
                //buscamos el tipo de id que se encuebtra en idpos
                String id_actual = mapa_id_pos.get(idPos);
                if (!tablaGlobal.containsKey(id_actual) && !tablaLocal.containsKey(id_actual)) {
                    newPair = new Pair<>(oldPair.getKey(), "int");
                    // hay que poner el tipo de I a int
                    casoTipoId = "int";
                    if (TsActual.equals("Global")) {
                        Dato_Tabla nuevoDato = new Dato_Tabla(id_actual, "int", tamanoTablaG);
                        tamanoTablaG += 1;
                        tablaGlobal.put(id_actual, nuevoDato);
                        tablaGlobalarrayList.add(nuevoDato);

                    } else {
                        Dato_Tabla nuevoDato = new Dato_Tabla(id_actual, "int", tamanoTablaL);
                        tamanoTablaL += 1;
                        tablaGlobal.put(id_actual, nuevoDato);
                        tablaGlobalarrayList.add(nuevoDato);
                    }
                } else {
                    String tipo_id = "";
                    if (TsActual.equals("Global")) {
                        if (tablaGlobal.get(id_actual).getTipo().equals("function")) {
                            // si id es una funcion
                            function = true;
                            tipo_id = tablaGlobal.get(id_actual).getTipoDev();
                            // pongo el tipo de I a funcion
                            casoTipoId = "function";
                            //SACAMOS LA LISTA DE PARAMETROS ASOCIADA A ID
                            // Y LA COMPARAMOS CON LA LISTA DE L

                        } else {
                            tipo_id = tablaGlobal.get(id_actual).getTipo();
                            casoTipoId = tipo_id;

                        }
                    } else {
                        tipo_id = tablaLocal.get(id_actual).getTipo();
                        casoTipoId = tipo_id;

                    }
                    // newPair = new Pair<>(oldPair.getKey(), tipo_id);
                    // hay que piner el tipo de I a el tipo de id
                }
                pilaAux.set(pilaAux.size() - 3, newPair); // seteamos el tipo de K al tipo del id

                break;
            case "20.2":
                //  miro lo que me devuelve I y lo comparo con el tipo de id
                oldPair = pilaAux.get(pilaAux.size() - 2); // I
                if (oldPair.getValue().equals(casoTipoId)) {
                    newPair = new Pair<>(oldPair.getKey(), casoTipoId);
                    pilaAux.set(pilaAux.size() - 2, newPair); // seteamos el tipo de I al tipo del id
                    oldPair = pilaAux.get(pilaAux.size() - 5); // K
                    newPair = new Pair<>(oldPair.getKey(), casoTipoId);
                    pilaAux.set(pilaAux.size() - 5, newPair); // seteamos el tipo de K al tipo del id
                } else {
                    newPair = new Pair<>(oldPair.getKey(), "tipo_error");
                    pilaAux.set(pilaAux.size() - 2, newPair); // seteamos el tipo de I al tipo del id
                    oldPair = pilaAux.get(pilaAux.size() - 5); // K
                    newPair = new Pair<>(oldPair.getKey(), "tipo_error");
                    pilaAux.set(pilaAux.size() - 5, newPair); // seteamos el tipo de K al tipo del id
                }
                destroyPilaAux(2);
                break;

            case "21.1":    // I -> {21.1} (L) {21.2}
                // miramos si estamos en una funcion si es el caso creamos una lista de tipo parametros
                // que iremos comparando con la lista de tipos de param de la func y si no osn iguales lanza errorç
                oldPair = pilaAux.get(pilaAux.size() - 2); // I
                newPair = new Pair<>(oldPair.getKey(), "function");
                pilaAux.set(pilaAux.size() - 2, newPair); // seteamos el tipo de I

                break;
            case "21.2":    // I -> (L)
                oldPair = pilaAux.get(pilaAux.size() - 3); // L
                if (!oldPair.getValue().equals("tipo_ok")) {
                    //error
                    oldPair = pilaAux.get(pilaAux.size() - 6); // I
                    newPair = new Pair<>(oldPair.getKey(), "tipo_error");
                    pilaAux.set(pilaAux.size() - 6, newPair); // seteamos el tipo de I a tipo_error si los parametros no estan bien
                }

                destroyPilaAux(6);
                break;

            case "22.1":    // I -> %= R ; {22.1}
                String r_tipo_22_1 = pilaAux.get(pilaAux.size() - 2).getValue(); // tipo de R
                if (!r_tipo_22_1.equals("int")) {
                    // lanza error
                    oldPair = pilaAux.get(pilaAux.size() - 5); // I
                    newPair = new Pair<>(oldPair.getKey(), "tipo_error");
                    pilaAux.set(pilaAux.size() - 5, newPair); //
                } else {
                    oldPair = pilaAux.get(pilaAux.size() - 4); // I
                    newPair = new Pair<>(oldPair.getKey(), "int");
                    pilaAux.set(pilaAux.size() - 4, newPair); // seteamos el tipo de I a tipo_error si los parametros no estan bien
                }

                destroyPilaAux(4);
                break;


            case "23.1":    // I -> = R ; {23.1}
                String r_tipo_23_1 = pilaAux.get(pilaAux.size() - 2).getValue(); // tipo de R
                oldPair = pilaAux.get(pilaAux.size() - 5); // I
                newPair = new Pair<>(oldPair.getKey(), r_tipo_23_1);
                pilaAux.set(pilaAux.size() - 4, newPair); //
                destroyPilaAux(4);
                break;

            /*L -> {24.1} R {24.2} A {24.3}*/
            case "24.1":
                // crear lista de tipo de params cuando llamo a la funcion
                lista_params = new ArrayList<>();
                break;

            case "24.2":
                // añado a la lista L el tipo de param de R
                String r_tipo_24_2 = pilaAux.get(pilaAux.size() - 2).getValue(); // tipo de R
                lista_params_L.add(r_tipo_24_2);
                // lista aux = lista L
                break;

            case "24.3":
                // lista L = lista aux
                lista_params_L = lista_params_aux;
                destroyPilaAux(5);
                break;

            /*A -> , R {25.1 } A{25.2} */
            case "25.1":
                // lista aux le añadimos el tipo de R
                // lista aux 2 = lista aux
                oldPair = pilaAux.get(pilaAux.size() - 2); // R
                lista_params_aux.add(oldPair.getValue());
                lista_params_aux2 = lista_params_aux;
                break;
            case "25.2":
                // si lista aux2 es dif de null entonces lista aux = lista aux 2
                if (lista_params_aux2 != null) {
                    lista_params_aux = lista_params_aux2;
                }
                destroyPilaAux(5);
                break;

            case "26.1":
                /*X -> R {26.1}*/
                oldPair = pilaAux.get(pilaAux.size() - 2); // R
                oldPair2 = pilaAux.get(pilaAux.size() - 3); // X
                newPair = new Pair<>(oldPair2.getKey(), oldPair.getValue());
                pilaAux.set(pilaAux.size() - 3, newPair); //
                destroyPilaAux(2);
                break;

            case "27.1":
                /*R -> J P {27.1}*/
                // si tipo_P no es error y tipo_J tampoco
                if (!pilaAux.get(pilaAux.size() - 2).getValue().equals("tipo_error") && !pilaAux.get(pilaAux.size() - 3).getValue().equals("tipo_error")) {
                    oldPair = pilaAux.get(pilaAux.size() - 3); // J

                    if (pilaAux.get(pilaAux.size() - 2).getValue().equals("-")) {
                        // R sera del tipo de J
                        newPair = new Pair<>(pilaAux.get(pilaAux.size() - 4).getKey(), "boolean");
                    }
                    // si tipo_P no es de tipo vacio y tipo_J es igual a tipo_P
                    else if (!pilaAux.get(pilaAux.size() - 2).getValue().equals("-") && oldPair.getValue().equals(pilaAux.get(pilaAux.size() - 2).getValue())) {
                        // R sera del tipo de J
                        newPair = new Pair<>(pilaAux.get(pilaAux.size() - 4).getKey(), "boolean");
                    } else {
                        errores_sem(5);
                        newPair = new Pair<>(pilaAux.get(pilaAux.size() - 4).getKey(), "tipo_error");
                    }

                } else {
                    newPair = new Pair<>(pilaAux.get(pilaAux.size() - 4).getKey(), "tipo_error");
                }


                destroyPilaAux(3);
                break;


            case "28.1":  /*P -> == J P1 {28.1} */
                // si tipo_p1 es dif de tipo_error y  tipo_j tambien
                if (!pilaAux.get(pilaAux.size() - 2).getValue().equals("tipo_error") && !pilaAux.get(pilaAux.size() - 3).getValue().equals("tipo_error")) {
                    oldPair = pilaAux.get(pilaAux.size() - 3); // J

                    if (pilaAux.get(pilaAux.size() - 2).getValue().equals("-")) {
                        newPair = new Pair<>(pilaAux.get(pilaAux.size() - 5).getKey(), oldPair.getValue());
                    }
                    // si tipo_p1 es dif de tipo_vacio y tipo_j es igual a tipo_p1
                    else if (!pilaAux.get(pilaAux.size() - 2).getValue().equals("-") && oldPair.getValue().equals(pilaAux.get(pilaAux.size() - 2).getValue())) {
                        // ponemos a P el tipo de J
                        newPair = new Pair<>(pilaAux.get(pilaAux.size() - 6).getKey(), oldPair.getValue());
                    } else {
                        newPair = new Pair<>(pilaAux.get(pilaAux.size() - 5).getKey(), "tipo_error");
                        errores_sem(5);
                    }

                } else {
                    newPair = new Pair<>(pilaAux.get(pilaAux.size() - 5).getKey(), "tipo_error");
                }
                pilaAux.set(pilaAux.size() - 5, newPair);
                destroyPilaAux(4);
                break;


            case "29.1": /* J -> V Y {29.1} */
                // si tipo_y es dif de tipo_error y  tipo_v tambien
                if (!pilaAux.get(pilaAux.size() - 2).getValue().equals("tipo_error") && !pilaAux.get(pilaAux.size() - 3).getValue().equals("tipo_error")) {
                    oldPair = pilaAux.get(pilaAux.size() - 3); // V
                    // miramos si y es de tipo vacio
                    if (pilaAux.get(pilaAux.size() - 2).getValue().equals("-")) {
                        // j sera del tipo de V
                        newPair = new Pair<>(pilaAux.get(pilaAux.size() - 4).getKey(), oldPair.getValue());
                    }
                    // si v es del mismo tipo que Y y que sean de tipo int
                    else if (oldPair.getValue().equals(pilaAux.get(pilaAux.size() - 2).getValue()) && (pilaAux.get(pilaAux.size() - 2).getValue().equals("int"))) {
                        // ponemos J al tipo de V ( que tendria que ser int)
                        newPair = new Pair<>(pilaAux.get(pilaAux.size() - 4).getKey(), oldPair.getValue());
                    } else {
                        newPair = new Pair<>(pilaAux.get(pilaAux.size() - 4).getKey(), "tipo_error");
                        errores_sem(5);
                    }

                } else {
                    newPair = new Pair<>(pilaAux.get(pilaAux.size() - 4).getKey(), "tipo_error");
                    errores_sem(1);
                }
                pilaAux.set(pilaAux.size() - 4, newPair);
                destroyPilaAux(3);
                break;

            case "30.1":
                /*Y -> + V {30.1} Y1 {30.2*} */
                // si v_tipo es dif de int
                if (pilaAux.get(pilaAux.size() - 2).getValue().equals("boolean")) {
                    oldPair = pilaAux.get(pilaAux.size() - 4); // Y
                    newPair = new Pair<>(oldPair.getKey(), "tipo_error");
                }
                pilaAux.set(pilaAux.size() - 4, newPair);
                break;

            case "30.2":
                // si y1_tipo es dif de tipo_error y tipo_v tambien
                String y1_tipo = pilaAux.get(pilaAux.size() - 2).getValue(); // Y1

                if (!y1_tipo.equals("tipo_error") && !pilaAux.get(pilaAux.size() - 4).getValue().equals("tipo_error")) {
                    oldPair = pilaAux.get(pilaAux.size() - 4); // V

                    // si y1 es dif de tipo_vacio y tipo_v es igual al tipo de y1 y si son dif de tipo boolean
                    if (!y1_tipo.equals("-") && oldPair.getValue().equals(y1_tipo) && !y1_tipo.equals("boolean")) {
                        newPair = new Pair<>(pilaAux.get(pilaAux.size() - 6).getKey(), oldPair.getValue());
                    }
                    if (y1_tipo.equals("-")) {
                        newPair = new Pair<>(pilaAux.get(pilaAux.size() - 6).getKey(), oldPair.getValue());
                    }
                } else {
                    newPair = new Pair<>(pilaAux.get(pilaAux.size() - 6).getKey(), "tipo_error");
                }
                pilaAux.set(pilaAux.size() - 6, newPair);
                destroyPilaAux(5);
                break;
            case "31.1":
                oldPair = pilaAux.get(pilaAux.size() - 3); // V
                //buscamos el tipo de id que se encuebtra en idpos
                String id_actual2 = mapa_id_pos.get(idPos);
                if (!tablaGlobal.containsKey(id_actual2) && !tablaLocal.containsKey(id_actual2)) {
                    newPair = new Pair<>(oldPair.getKey(), "int");
                    if (TsActual.equals("Global")) {
                        Dato_Tabla nuevoDato = new Dato_Tabla(id_actual2, "int", tamanoTablaG);
                        tamanoTablaG += 1;
                        tablaGlobal.put(id_actual2, nuevoDato);
                        tablaGlobalarrayList.add(nuevoDato);
                    } else {
                        Dato_Tabla nuevoDato = new Dato_Tabla(id_actual2, "int", tamanoTablaL);
                        tamanoTablaL += 1;
                        tablaGlobal.put(id_actual2, nuevoDato);
                        tablaGlobalarrayList.add(nuevoDato);
                    }
                } else {
                    String tipo_id = "";
                    if (TsActual.equals("Global")) {
                        if (tablaGlobal.get(id_actual2).getTipo().equals("function")) {
                            function = true;
                            tipo_id = tablaGlobal.get(id_actual2).getTipoDev();
                        } else {
                            tipo_id = tablaGlobal.get(id_actual2).getTipo();
                        }
                    } else {
                        tipo_id = tablaLocal.get(id_actual2).getTipo();
                    }
                    newPair = new Pair<>(oldPair.getKey(), tipo_id);
                }
                pilaAux.set(pilaAux.size() - 3, newPair); // seteamos el tipo de V al tipo del id
                break;

            case "31.2":
                /*V -> id {31.1} Z {31.2}*/
                // mira el tipo de Z

                if (pilaAux.get(pilaAux.size() - 2).getValue().equals("tipo_error")) {
                    errores_sem(1);
                    oldPair = pilaAux.get(pila.size() - 5);
                    newPair = new Pair<>(oldPair.getKey(), "tipo_error");
                }
                else if (function && pilaAux.get(pilaAux.size() - 2).getValue().equals("tipo_ok")) {
                    tipoReturn = tablaGlobal.get(mapa_id_pos.get(idPos)).getTipoDev();
                    oldPair = pilaAux.get(pila.size() - 5);
                    newPair = new Pair<>(oldPair.getKey(), tipoReturn);
                }
                else if (pilaAux.get(pilaAux.size() - 2).getValue().equals("-")){

                    String tipoId = tablaGlobal.get(mapa_id_pos.get(idPos)).getTipo();
                    oldPair = pilaAux.get(pilaAux.size() - 5);
                    newPair = new Pair<>(oldPair.getKey(), tipoId);
                }
                pilaAux.set(pilaAux.size() - 5, newPair);   // seteamos V
                destroyPilaAux(4);
                break;

            case "32.1": /*V -> ( R ) {32.2}*/
                oldPair = pilaAux.get(pila.size() - 2);
                newPair = new Pair<>(pilaAux.get(pila.size() - 5).getValue(), oldPair.getValue());
                pilaAux.set(pilaAux.size() - 5, newPair);
                destroyPilaAux(4);
                break;

            case "33.1":
                /*V->entera {33.1}*/
                newPair = new Pair<>(pilaAux.get(pilaAux.size() - 3).getKey(), "int");
                pilaAux.set(pilaAux.size() - 3, newPair);
                destroyPilaAux(2);
                break;

            case "34.1":
                pilaAux.set(pilaAux.size()-2,new Pair<>(pilaAux.get(pilaAux.size()-2).getKey(),"boolean"));
                destroyPilaAux(2);
                break;
            case "35.1":
                pilaAux.set(pilaAux.size()-2,new Pair<>(pilaAux.get(pilaAux.size()-2).getKey(),"boolean"));
                destroyPilaAux(2);

                break;
            case "36.1":
                /*V -> ! id {36.1} Z {36.2}*/
                //buscar en la TS el id y comprobar que sea boolean
                idPos = Integer.parseInt(pilaAux.get(pilaAux.size()-2).getValue());
                tipoReturn = tablaGlobal.get(mapa_id_pos.get(idPos)).getTipoDev();
                if((function && !tipoReturn.equals("boolean")) || !pilaAux.get(pilaAux.size()-2).getValue().equals("boolean")){
                    errores_sem(4);
                }
                else{
                    pilaAux.set(pilaAux.size()-2,new Pair<>(pilaAux.get(pilaAux.size()-4).getKey(),"boolean"));
                    pilaAux.set(pilaAux.size() - 4, newPair);
                }
                break;

            case "36.2":
                if(pilaAux.get(pilaAux.size()-2).getValue().equals("tipo_error")) {
                    errores_sem(1);
                    newPair = new Pair<>(pilaAux.get(pilaAux.size()-6).getKey(), "tipo_error");
                    pilaAux.set(pilaAux.size() - 6, newPair);
                }
                destroyPilaAux(5);

                break;

            case "37.1":
                pilaAux.set(pilaAux.size()-2,new Pair<>(pilaAux.get(pilaAux.size()-2).getKey(),"cad"));
                destroyPilaAux(2);

                break;
            /* Z -> ( {38.1} L ) {38.2} */
            case "38.1":
                pilaAux.set(pilaAux.size()-2,new Pair<>(pilaAux.get(pilaAux.size()-2).getKey(),"function"));

                break;
            case "38.2":

                if(!pila.get(pilaAux.size()-2).getValue().equals("tipo_ok")){
                    //error
                    pilaAux.set(pilaAux.size()-5,new Pair<>(pilaAux.get(pilaAux.size()-2).getKey(),"tipo_error"));
                }
                destroyPilaAux(5);
                break;

        }
    }

    private static void anadirDesp(String value) {

        if (TsActual.equals("Global")) {
            switch (value) {
                case "string":
                    tamanoTablaG += 32;
                    break;

                case "int":
                    tamanoTablaG += 1;
                    break;

                case "boolean":
                    tamanoTablaG += 1;
                    break;
            }
        } else {
            switch (value) {
                case "string":
                    tamanoTablaL += 32;
                    break;

                case "int":
                    tamanoTablaL += 1;
                    break;

                case "boolean":
                    tamanoTablaL += 1;
                    break;

            }
        }
    }

    private static void destroyPilaAux(int i){
        for (int j = 0; j < i; j++) {
            pilaAux.pop();
        }
    }
    private static void errores_sem(int i){
        int linea = linea_actu(contTok);
        try(FileWriter fw = new FileWriter(new File("C:\\Users\\esthe\\Desktop\\upm\\tercero\\primer cuatri\\pdL\\practica\\entrega_Julio\\PDL\\src\\errores_Sem.txt"), true);){

            PrintWriter writer2 = new PrintWriter(fw);

            switch(i){
                case 1:
                    writer2.write("Error de tipos en línea " + linea);
                    break;
                case 3:
                    writer2.write("Error if mal declarado en la línea " + linea + "se esperaba tipo boolean y se obtuvo " + pilaAux.get(pilaAux.size()-3).getValue());
                    break;
                case 4:
                    writer2.write("Error tipo RETURN no válido en la línea " + linea + "se esperaba tipo " + tipoReturn +  " y se obtuvo " + pilaAux.get(pilaAux.size()-3).getValue());
                    break;

                case 5:
                    writer2.write("Error tipo tipos incopatibles " +oldPair.getValue() + "con " + pilaAux.get(pilaAux.size()-2).getValue()  + " en linea "  + linea );
                    break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}