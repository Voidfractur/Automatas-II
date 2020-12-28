/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;

/**
 *
 * @author spart
 */
public class Renglon {
    
    private int numero;
    private String Lexema;
    private ArrayList<String> decoracion;
    private int padre;
    private boolean fpadre;
    private boolean fid;

    public Renglon(int numero, String Lexema) {
        this.numero = numero;
        this.Lexema = Lexema;
        fpadre=false;
        fid=false;
        decoracion = new ArrayList<>();
    }

    public boolean isFid() {
        return fid;
    }

    public void setFid(boolean fid) {
        this.fid = fid;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getLexema() {
        return Lexema;
    }

    public void setLexema(String Lexema) {
        this.Lexema = Lexema;
    }

    public int getPadre() {
        return padre;
    }

    public void setPadre(int padre) {
        this.padre = padre;
    }

    public boolean isFpadre() {
        return fpadre;
    }

    public void setFpadre(boolean fpadre) {
        this.fpadre = fpadre;
    }

    public ArrayList<String> getDecoracion() {
        return decoracion;
    }

    public void setDecoracion(ArrayList<String> decoracion) {
        this.decoracion = decoracion;
    }
    

    @Override
    public String toString() {
        return numero+" "+Lexema+" "+mostrarLista(decoracion)+padre; //To change body of generated methods, choose Tools | Templates.
    }

     public String mostrarLista(ArrayList lista){
      String a="";
      if (!lista.isEmpty()) {
          for (int i = 0; i < lista.size(); i++) {
              a+=lista.get(i).toString()+" ";
          }
      }
      return a;
  }
    
    
    
}
