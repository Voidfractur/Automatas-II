/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author spart
 */
public class Renglon {
    
    private int numero;
    private String Lexema;
    private int padre;
    private boolean fpadre;
    private boolean fid;

    public Renglon(int numero, String Lexema) {
        this.numero = numero;
        this.Lexema = Lexema;
        fpadre=false;
        fid=false;
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

    @Override
    public String toString() {
        return numero+" "+Lexema+" "+padre; //To change body of generated methods, choose Tools | Templates.
    }

    
    
    
    
}
