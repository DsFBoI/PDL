package src.code;


import javafx.util.Pair;

import java.io.*;

import java.util.*;

public class Analizador {
    private static String parse = "";
    private static int[] saltos;
    private static Stack<Pair<String, String>> pila = new Stack();
    private static Stack<Pair<String, String>> pilaAux = new Stack();
    private static Pair<String,String> pilaArriba;
    private  static  int contTok;
    private static boolean error = false;
    private static int parentesis = 0;
    private static int llaves = 0;
    private static int idPos;

    static Map<Integer,String> mapa_id_pos = new HashMap<>();

    private static String tokenAct;

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
                    String estado = pop.getKey();
                    if(!error){
                        tokenCogido = br.readLine();
                        if(tokenCogido == null){
                            parse+=" 9";
                            break;
                        }
                        tokenCogido = tokenCogido.replace("<", "");
                        tokenCogido = tokenCogido.replace(">", "");
                        split = tokenCogido.split(",");

                        tokenAct = split[0];


                    }else{
                        error = false;

                    }
                    readToken(tokenAct,estado);
                }else{
                    tokenCogido = br.readLine();
                    if(tokenCogido == null && pilaArriba.getKey().equals("$")){
                        parse += " 9";
                        break;
                    }else if(tokenCogido == null && !pilaArriba.getKey().equals("$")){
                        //error de que ha finalizado cuando no debia
                        parse += " 9";
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
                try (FileWriter fw = new FileWriter(new File("C:\\Users\\esthe\\Desktop\\upm\\tercero\\primer cuatri\\pdL\\practica\\primera entrega\\pruebas\\pruebas\\parse.txt"), true);){
                    fw.write(parse);

                } catch (Exception e) {
                    System.out.println("no funciona el parse");
                }
                System.out.println(parse);





            }

        }catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }


    }

    private static void readToken(String tokenAct, String estado) {

        switch (estado){

        }


    }

}