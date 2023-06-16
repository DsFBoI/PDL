package src.code;

//import javafx.util.Pair;

import java.io.*;

import java.util.*;

public class Analizador {
    private static String parse = "Des ";
    private static int[] saltos;
    private static Stack<Pair<String, String>> pila = new Stack();
    private static Stack<Pair<String, String>> pilaAux = new Stack();
    private static Pair<String,String> pilaArriba;
    private  static  int contTok;
    private static boolean error = false;
    private static int parentesis = 0;
    private static int llaves = 0;
    private static int idPos;
    private static boolean declaracion = true;

    private static int continua = 0;

    static Map<Integer,String> mapa_id_pos = new HashMap<>();

    private static String tokenAct;
    private static String tokenErr;
    private static String EstadoErr;


    private static int nig2=0;

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
            add("eq");
            add("asig");
            add("pcoma");
            add("sum");
            add("neg");
            add("akey");
            add("ckey");
        }
    };


    public static void main(String[] args) throws IOException {
        Tokens toke = new Tokens(0,0);
        Tokens.main(null);
        saltos = toke.saltos;
        String[] split = new String[0];

        Map<String,Integer> mapaTokens = toke.mapaid;
        for(String key: mapaTokens.keySet()){
            mapa_id_pos.put(mapaTokens.get(key), key);
            
        }

        String tokenCogido;


        try(FileReader fr = new FileReader("C:\\Users\\danel\\Downloads\\calse\\PDL\\Trabajo julio\\PDL\\src\\grmatica\\prueba_if_token.txt")){
            BufferedReader br = new BufferedReader(fr);
            pila.push(new Pair<String,String>("$", "-"));
            pila.push(new Pair<String,String>("S", "-"));

            while(br != null){
                contTok++;
                pilaArriba = pila.peek();

                if(!terminales.contains(pilaArriba.getKey())){
                    Pair<String,String> pop = pilaAux.push(pila.pop());
                    while(contieneNumeros(pop.getKey())){
                        acciones_sem(pop.getKey());
                        pop = pilaAux.push(pila.pop());
                    }
                    String estado = pop.getKey();
                    if(!error){
                        tokenCogido = br.readLine();
                        if(tokenCogido == null){
                            parse += " 3";
                            break;
                        }
                        tokenCogido = tokenCogido.replace("<", "");
                        tokenCogido = tokenCogido.replace(">",  "");
                        split = tokenCogido.split(",");

                        tokenAct = split[0];
                        
                        if(tokenAct.equals("id")){
                            idPos = Integer.parseInt(split[1]);
                            System.out.println(mapa_id_pos.get(idPos));
                        }

                    }else{
                        error = false;

                    }
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

                    equiparar(pila.peek().getKey(), tokenAct);

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
                if(parentesis != 0){
                    //error diferencia de parentesis
                }
                if(llaves != 0 ){
                    // error diferencia de llaves
                }


            }
            try (FileWriter fw = new FileWriter(new File("C:\\Users\\danel\\Downloads\\calse\\PDL\\Trabajo julio\\PDL\\src\\grmatica\\parse.txt"), true);){
                fw.write(parse);


            } catch (Exception e) {
                System.out.println("no funciona el parse");
            }
            System.out.println(parse);

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
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            case "B":
                caseB(tokenAct);
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            case "T":
                caseT(tokenAct);
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            case "F":
                caseF(tokenAct);
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            case "G":
                caseG(tokenAct);
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            case "H":
                caseH(tokenAct);
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            case "N":
                caseN(tokenAct);
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            case "M":
                caseM(tokenAct);
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            case "D":
                caseD(tokenAct);
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            case "K":
                caseK(tokenAct);
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            case "I":
                caseI(tokenAct);
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            case "L":
                caseL(tokenAct);
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            case "A":
                caseA(tokenAct);
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            case "X":
                caseX(tokenAct);
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            case "R":
                caseR(tokenAct);
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
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            case "Y":
                caseY(tokenAct);
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            case "V":
                caseV(tokenAct);
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            case "Z":
                caseZ(tokenAct);
                continuar(estado);
                equiparar(tokenAct,pila.peek().getKey());
                if(continua>0)
                    continua--;
                break;

            default:
                if(!contieneNumeros(estado)) {
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
                /*B -> {5.1} while ( E ) { M } {5.1}*/ //NO ESTOY SEGURA DE ESTO!!!!
                parse += " 7";
                pila.push(new Pair<>("5.2", "-"));
                pila.push(new Pair<>("ckey", "-"));
                pila.push(new Pair<>("M", "-"));
                pila.push(new Pair<>("akey", "-"));
                pila.push(new Pair<>("cpar", "-"));
                pila.push(new Pair<>("R", "-"));
                pila.push(new Pair<>("apar", "-"));
                pila.push(new Pair<>("while", "-"));
                pila.push(new Pair<>("5.1", "-"));
                break;
            default :
                /* B-> {6.2} K {6.1}*/
                parse += " 4";
                pila.push(new Pair<>("6.1", "-"));
                pila.push(new Pair<>("K", "-"));
                pila.push(new Pair<>("6.2", "-"));
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
        }
    }

    private static void caseF(String tokenAct) {
        /* F -> {10.1} function id {10.2} G {10.3} ( H ) {10.4} { {10.5} S {10.6} } {10.7}*/
        if(tokenAct.equals("function")){
            pila.push(new Pair<>("10.7", "-"));
            pila.push(new Pair<>("ckey", "-"));
            pila.push(new Pair<>("10.6", "-"));
            pila.push(new Pair<>("M", "-"));
            pila.push(new Pair<>("10.5", "-"));
            pila.push(new Pair<>("akey", "-"));
            pila.push(new Pair<>("10.4", "-"));
            pila.push(new Pair<>("cpar", "-"));
            pila.push(new Pair<>("H", "-"));
            pila.push(new Pair<>("apar", "-"));
            pila.push(new Pair<>("10.3", "-"));
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
                /* I -> ( L ) ;  {21.1} */
                parse += " 26";
                pila.push(new Pair<>("cpar", "-"));
                pila.push(new Pair<>("L", "-"));
                pila.push(new Pair<>("apar", "-"));
                pila.push(new Pair<>("21.1", "-"));
                break;
            case "asig":
                /* I -> %= R ; {22.1} */
                parse += " 28";
                pila.push(new Pair<>("R", "-"));
                pila.push(new Pair<>("asig", "-"));
                pila.push(new Pair<>("22.1", "-"));
                break;
            case "eq":
                /* I -> %= R ; {23.1} */
                parse += " 27";
                pila.push(new Pair<>("R", "-"));
                pila.push(new Pair<>("eq", "-"));
                pila.push(new Pair<>("23.1", "-"));
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
        /*R -> J {27.1} P {27.2}*/
        parse += " 35";
        pila.push(new Pair<>("27.2", "-"));
        pila.push(new Pair<>("P", "-"));
        pila.push(new Pair<>("27.1", "-"));
        pila.push(new Pair<>("J", "-"));
    }

    private static void caseP(String tokenAct) {
        /*P -> == J {28.1} P {28.2} */
        if ("ig2".equals(tokenAct)) {

            parse += " 36";
            pila.push(new Pair<>("28.2", "-"));
            pila.push(new Pair<>("P", "-"));
            pila.push(new Pair<>("28.1", "-"));
            pila.push(new Pair<>("J", "-"));
            pila.push(new Pair<>("ig2", "-"));
        } else {
            while(contieneNumeros(pila.peek().getKey())){
                pila.pop();
            }
            parse += " 37";
        }
    }

    private static void caseJ(String tokenAct) {
        /*J -> V {29.1} Y {29.2} */
        parse += " 38";
        pila.push(new Pair<>("29.2", "-"));
        pila.push(new Pair<>("Y", "-"));
        pila.push(new Pair<>("29.1", "-"));
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
                /*V -> ( {32.1} R ) {32.2}*/
                parse += " 42";
                pila.push(new Pair<>("32.2", "-"));
                pila.push(new Pair<>("cpar", "-"));
                pila.push(new Pair<>("R", "-"));
                pila.push(new Pair<>("32.1", "-"));
                pila.push(new Pair<>("apar", "-"));
                break;

            case "entera":
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
        }
    }

    private static void caseZ(String tokenAct) {
        /* Z -> ( {38.1} L ) {38.2} */
        if ("apar".equals(tokenAct)) {
            parse += " 48";
            pila.push(new Pair<>("cpar", "-"));
            pila.push(new Pair<>("L", "-"));
            pila.push(new Pair<>("apar", "-"));
        } else {
            while(contieneNumeros(pila.peek().getKey())){
                pila.pop();
            }
            parse += " 49";
        }
    }

    private static void continuar(String estado){
        while(contieneNumeros(pila.peek().getKey())){
            pila.pop();
        }
        if(!terminales.contains(pila.peek().getKey())&&!contieneNumeros(pila.peek().getKey())){
            continua++;
            estado = pila.pop().getKey();
            readToken(tokenAct, estado);
        }

    }
    public static boolean contieneNumeros(String cadena) {
        for (int i = 0; i < cadena.length(); i++) {
            if (Character.isDigit(cadena.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private static void equiparar(String token, String estado) {
        if (continua == 0) {
            if (token.equals(estado)) {
                pilaAux.push(pila.pop());
            } else {

                if (!contieneNumeros(estado) && !contieneNumeros(token)) {
                    tokenErr = token;
                    EstadoErr = estado;
                    errores(1);
                }


            }
        }
    }

    private static void errores(int coderror){
        try(FileWriter fw = new FileWriter(new File("C:\\Users\\danel\\Downloads\\calse\\PDL\\Trabajo julio\\PDL\\src\\grmatica\\erroresSin.txt"), true);){

            PrintWriter writer2 = new PrintWriter(fw);

            switch(coderror){
                case 1:
                    writer2.write("Error en la linea " + linea_actu(contTok)+" : " + "Se esperaba "+ EstadoErr + " se obtuvo " + tokenErr +"\n" );
                    break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void acciones_sem(String token){
        switch(token){
            case "1.1":
                /* { if F.tipo == tipo_error then S.tipo = tipo_error }  */
                if(pila.peek().getValue().equals("tipo_error") || pila.get(pila.size()-2).getValue().equals("tipo_error")){
                    errores_sem(1);
                    Pair<String, String> oldPair = pila.get(pila.size()-3);
                    Pair<String, String> newPair = new Pair<>(oldPair.getKey(), "tipo_error");
                    pila.set(pila.size()-3, newPair);
                }
                else{
                    Pair<String, String> oldPair = pila.get(pila.size()-3);
                    Pair<String, String> newPair = new Pair<>(oldPair.getKey(), "tipo_OK");
                    pila.set(pila.size()-3, newPair);
                }
                break;

            case "2.1":
                /* { if B.tipo == tipo_error then S.tipo = tipo_error }   */
                if(pila.peek().getValue().equals("tipo_error") || pila.get(pila.size()-2).getValue().equals("tipo_error")){
                    errores_sem(1);
                    Pair<String, String> oldPair = pila.get(pila.size()-3);
                    Pair<String, String> newPair = new Pair<>(oldPair.getKey(), "tipo_error");
                    pila.set(pila.size()-3, newPair);
                }
                else{
                    Pair<String, String> oldPair = pila.get(pila.size()-3);
                    Pair<String, String> newPair = new Pair<>(oldPair.getKey(), "tipo_OK");
                    pila.set(pila.size()-3, newPair);
                }
                pilaAux.pop();
                break;

            case "3.1":
                /* B -> if ( R ) {3.1} D {3.2} */

                break;

            case "4.1":
                declaracion = true;
                break;

            case "4.2":
                //buscar en la TS TODO
                break;

            case "4.3":
                pilaAux.pop();
                pilaAux.pop();
                pilaAux.pop();
                pilaAux.pop();
                pilaAux.pop();
                pilaAux.pop();
                break;

            case "9.1":
                /*T-> boolean {9.1}*/
                Pair<String, String> oldPair = pilaAux.get(pilaAux.size()-3);
                Pair<String, String> newPair = new Pair<>(oldPair.getKey(), "boolean");
                pilaAux.set(pilaAux.size()-3, newPair);
                pilaAux.pop();
                break;

            case "36.1":
                //buscar en la TS el id y comprobar que sea boolean
                break;

            case "36.2":
                pilaAux.pop();
                pilaAux.pop();
                pilaAux.pop();
                pilaAux.pop();
                break;
        }
    }
    private static void errores_sem(int i) {
        int linea = linea_actu(contTok);
        try(FileWriter fw = new FileWriter(new File("C:\\Users\\esthe\\Desktop\\upm\\tercero\\primer cuatri\\pdL\\practica\\primera entrega\\pruebas\\pruebas\\errores_Sin.txt"), true);){

            PrintWriter writer2 = new PrintWriter(fw);

            switch(i){
                case 1:
                    writer2.write("Error de tipos en línea " + linea);
                    break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}