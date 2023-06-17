package src.code;
public class Dato_Tabla {
    String Lexema,Tipo,TipoParam,TipoDev,Etiq;
    int Direc,Nparam;
    //constructor general
    public Dato_Tabla(String Lexema, String Tipo,int Direc,int Nparam,String TipoParam,String TipoDev, String Etiq){
        this.Lexema= Lexema;
        this.Tipo = Tipo;
        this.Direc = Direc;
        this.Nparam = Nparam;
        this.TipoParam = TipoParam;
        this.TipoDev = TipoDev;
        this.Etiq = Etiq;

    }

    //para las funcciones
    public Dato_Tabla(String Lexema, String Tipo,int Nparam,String TipoParam,String TipoDev, String Etiq){
        this.Lexema= Lexema;
        this.Tipo = Tipo;
        this.Nparam = Nparam;
        this.TipoParam = TipoParam;
        this.TipoDev = TipoDev;
        this.Etiq = Etiq;

    }

    //para las variables
    public Dato_Tabla(String Lexema, String Tipo,int Direc){
        this.Lexema= Lexema;
        this.Tipo = Tipo;
        this.Direc = Direc;
    }

    public String getLexema(){

        return this.Lexema;
    }

    public String getTipo(){

        return this.Tipo;
    }

    public String getTipoParam(){

        return this.TipoParam;
    }

    public String getTipoDev(){

        return this.TipoDev;
    }

    public String getEtiq(){

        return this.Etiq;
    }

    public int getDirec(){

        return this.Direc;
    }

    public int getNparam(){

        return this.Nparam;
    }


}