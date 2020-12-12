/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;
import java.util.Stack;
import javax.swing.JOptionPane;

/**
 *
 * @author spart
 */
public class Sintactico {
    private  ArrayList<String> listaTokens;
    private Stack<String> pila;
    private Stack<Integer>contadorPila;
    private int contadorRenglon;
    private ArrayList<String> historiaPila;
    boolean fParametro;
    boolean fParametro2;
    boolean f2;
    private ArrayList<Renglon> listaSalida;
    private ArrayList<String> listaIdyNum;
    
    public Sintactico(ArrayList<String> listaTokens) {
        contadorPila = new Stack<>();
        listaIdyNum = new ArrayList<String>();
        contadorRenglon=0;
        this.listaTokens=acortarLista(listaTokens);
        pila= new Stack<String>();
        fParametro = false;
        fParametro2 = false;
        f2=false;
        historiaPila = new ArrayList<String>();
        listaSalida = new ArrayList<Renglon>();
        generarPila();
        asginarValores();
    }
    public void asginarValores(){
        int conteo = 0;
        for (int i = 0; i < listaSalida.size(); i++) {
            if (listaSalida.get(i).getLexema().equals("tk_id") && listaSalida.get(i).isFid()==false || listaSalida.get(i).getLexema().equals("tk_num") && listaSalida.get(i).isFid()==false) {
                listaSalida.get(i).setLexema(listaSalida.get(i).getLexema()+"("+listaIdyNum.get(conteo)+")");
                listaSalida.get(i).setFid(true);
                conteo++;
            }
        }
    }
    
    public void verPila(){
        Stack<String> pilaTemporal = (Stack<String>) pila.clone();
        Stack<String> pila3 = new Stack<String>();
        int tamTemporal = pilaTemporal.size();
        for (int i = 0; i < tamTemporal; i++) {
            pila3.push(pilaTemporal.pop());
        }
        String par = "";
        int max = pila3.size();
        for (int i = 0; i < max; i++) {
            par= par +" "+ pila3.pop();
        }
        historiaPila.add(par);
    }
    
    public void generarPila(){
        int numRenglon = 0;
        String temporal = "";
        String anterior = "";
        boolean fprimerDiferente = false;
        boolean fprimerCodigo = true;
        int tope = listaTokens.size(); //numero de tokens
        tope = tope+5;
        for (int cont = 0; cont < tope; cont++) {
            String listaToken="";
            if (cont<listaTokens.size()) {
                listaToken = listaTokens.get(cont);
                pila.push(listaToken);
                contadorPila.push(contadorRenglon);
                if (numRenglon>0) {
                    aumentarRenglon(anterior);
                }
                verPila();
            }
           anterior=listaToken;
        //}
        //for (String listaToken : listaTokens) {
             
           
             if (esInicio()) { //verifica si se aplica la regla de produccion inicio
                 temporal =pila.pop(); // saca de pila el ultimo tk_{ y se guarda en temporal
                 numRenglon++; //aumenta el contador de numero de renglon
                 listaSalida.add(generarRenglon(numRenglon, temporal)); // Como salio de pila se agrega a lista
                 temporal =pila.pop(); //saca de pila el ultimo tk_Begin y se guarda en temporal
                 numRenglon++;//aumenta el contador de numero de renglon
                 listaSalida.add(generarRenglon(numRenglon, temporal)); // Como salio de pila se agrega a lista
                 contadorPila.pop();
                 contadorPila.pop();
                 contadorPila.push(contadorRenglon);
                 pila.push("INICIO"); //se agrega a la pila
             verPila();
            }
             if (esTipo()) {//verifica si se aplica la regla de produccion Tipo
                temporal = pila.pop();// saca de pila el ultimo tk_intenger o tk_Real y se guarda en temporal
                numRenglon++;//aumenta el contador de numero de renglon
                listaSalida.add(generarRenglon(numRenglon, temporal));// Como salio de pila se agrega a lista
                contadorPila.pop();
                contadorPila.push(contadorRenglon);
                pila.push("TIPO");//se agrega a la pila
                
                verPila();
            }  
             /*
             if (esValores()) {
                pila.pop();
                pila.pop();
                pila.pop();
                pila.push("VALORES");
                System.out.println("entro if3"+pila.peek());
            }
             */
             if (esValor()) {//verifica si se aplica la regla de produccion Valor
               temporal = pila.pop();// saca de pila el ultimo tk_num y se guarda en temporal
               numRenglon++;//aumenta el contador de numero de renglon
               listaSalida.add(generarRenglon(numRenglon, temporal));// Como salio de pila se agrega a lista
                pila.push("VALOR");//se agrega a la pila
                contadorPila.pop();
                contadorPila.push(contadorRenglon);
                verPila();
            }
             
             for (int i = 0; i < 10; i++) {
                
               if (esParametro()) {//verifica si se aplica la regla de produccion Valor
                 if (fParametro) {//verifica que la bandera fparametro este apagada
               temporal = pila.pop();// saca de pila el ultimo y se guarda en temporal
               numRenglon++;
               listaSalida.add(generarRenglon(numRenglon, temporal));
                     generarPadre("tk_)", numRenglon);
                     generarPadreTresOpciones("IDASIG", "VALOR", "OPERACION", numRenglon);
                temporal = pila.pop();
                numRenglon++;
                listaSalida.add(generarRenglon(numRenglon, temporal));
                temporal = pila.pop();
                numRenglon++;
                listaSalida.add(generarRenglon(numRenglon, temporal));
                     generarPadreTresOpciones("tk_id", "ASIG", "tk_num", numRenglon);
                pila.push("PARAMETRO");
                contadorPila.pop();
                contadorPila.pop();
                contadorPila.pop();
                contadorPila.push(contadorRenglon);
                verPila();
                 }else{
                     if (!fParametro2) {
                        temporal = pila.pop();
                        numRenglon++;
                        listaSalida.add(generarRenglon(numRenglon, temporal));
                        temporal = pila.pop();
                        numRenglon++;
                        listaSalida.add(generarRenglon(numRenglon, temporal));
                         if (temporal.equals("IDASIG")) {
                             generarPadreDosOpciones("tk_id", "ASIG", numRenglon);
                         }
                         if (temporal.equals("VALOR")) {
                             generarPadre("tk_num", numRenglon);
                         }
                         if (temporal.equals("OPERACION")) {
                             generarPadre("PARAMETRO", numRenglon);
                             generarPadre("TIPOOPE", numRenglon);
                         }
                        pila.push("PARAMETRO");
                        contadorPila.pop();
                        contadorPila.pop();
                        contadorPila.push(contadorRenglon);
                        verPila();
                       
                     }else{
                      temporal = pila.pop();
                      numRenglon++;
                      listaSalida.add(generarRenglon(numRenglon, temporal));
                         
                         if (getPosicionLexema("tk_)")>getPosicionLexema("PARAMETRO")) {
                             
                             generarPadre("tk_)", numRenglon);
                             generarPadreTresOpciones("IDASIG","VALOR", "OPERACION", numRenglon);
                         }else{
                             
                             generarPadre("PARAMETRO", numRenglon);
                             generarPadre("tk_,", numRenglon);
                             generarPadreDosOpciones("IDASIG", "VALOR", numRenglon);
                     }
                     
                       temporal = pila.pop();
                       numRenglon++;
                       listaSalida.add(generarRenglon(numRenglon, temporal));
                       pila.push("PARAMETRO");
                       contadorPila.pop();
                       contadorPila.pop();
                       contadorPila.push(contadorRenglon);
                       verPila();  
                       fParametro2=false;
                     }
                 }
                 
            } 
            }
             /*
             if (esParenteseis()) {
                pila.pop();
                pila.pop();
                pila.pop();
                pila.push("PARENTESIS");
                System.out.println("entro if6"+pila.peek());
            }
             */
             if (esTipoOpe()) {
                temporal = pila.pop();
                numRenglon++;
                listaSalida.add(generarRenglon(numRenglon, temporal));
                pila.push("TIPOOPE");
                contadorPila.pop();
                contadorPila.push(contadorRenglon);
                verPila();
            }
             if (esOperacion()) {
               temporal = pila.pop();
               numRenglon++;
               listaSalida.add(generarRenglon(numRenglon, temporal));
                 generarPadre("PARAMETRO", numRenglon);
                 generarPadre("tk_(", numRenglon);
                temporal = pila.pop();
                numRenglon++;
                listaSalida.add(generarRenglon(numRenglon, temporal));
                 generarPadreCuatroOpciones("tk_ADD", "tk_MUL", "tk_DIV", "tk_SUB", numRenglon);
                 contadorPila.pop();
                 contadorPila.pop();
                 contadorPila.push(contadorRenglon);
                pila.push("OPERACION");
                
                verPila();
            }
             /*
             if (esListaOpe()) {
                pila.pop();
                pila.pop();
                pila.push("LISTAOPE");
                System.out.println("entro if9"+pila.peek());
            }
             */
             /*
             if (esAr()) {
                pila.pop();
                pila.pop();
                pila.pop();
                pila.push("AR");
                System.out.println("entro if10"+pila.peek());
            }*/
             if (esAsig()) {
                temporal = pila.pop();
                numRenglon++;
                listaSalida.add(generarRenglon(numRenglon, temporal));
                generarPadre("tk_num", numRenglon);
                temporal = pila.pop();
                numRenglon++;
                listaSalida.add(generarRenglon(numRenglon, temporal));
                temporal = pila.pop();
                numRenglon++;
                listaSalida.add(generarRenglon(numRenglon, temporal));
                generarPadre("tk_id", numRenglon);
                pila.push("ASIG");
                contadorPila.pop();
                contadorPila.pop();
                contadorPila.pop();
                contadorPila.push(contadorRenglon);
                verPila();
            }
             if (esIdasig()) {
                temporal = pila.pop();
                numRenglon++;
                listaSalida.add(generarRenglon(numRenglon, temporal));
                 if (f2) {
                     generarPadre("VALOR", numRenglon);
                     generarPadre("tk_=", numRenglon);
                     generarPadre("IDASIG", numRenglon);
                     f2=false;
                 }
                pila.push("IDASIG");
                contadorPila.pop();
                contadorPila.push(contadorRenglon);
                verPila();
            }
             
             if (esFuncion()) {
                temporal = pila.pop();
                numRenglon++;
                listaSalida.add(generarRenglon(numRenglon, temporal));
                
                pila.push("FUNCION");
                contadorPila.pop();
                contadorPila.push(contadorRenglon);
                
                verPila();
            }
             if (esLW()) {
                temporal = pila.pop();
                numRenglon++;
                listaSalida.add(generarRenglon(numRenglon, temporal));
                temporal = pila.pop();
                numRenglon++;
                listaSalida.add(generarRenglon(numRenglon, temporal));
                 generarPadre("PARAMETRO", numRenglon);
                 generarPadre("tk_(", numRenglon);
                temporal = pila.pop();
                numRenglon++;
                listaSalida.add(generarRenglon(numRenglon, temporal));
                 generarPadreDosOpciones("tk_WRITE", "tk_READ", numRenglon);
                pila.push("LW");
                 contadorPila.pop();
                 contadorPila.pop();
                 contadorPila.pop();
                 contadorPila.push(contadorRenglon);
                 verPila();
            }
             
             for (int i = 0; i < 10; i++) {
                if (esLista()) {
                 if (fParametro) {
                temporal = pila.pop();
                numRenglon++;
                listaSalida.add(generarRenglon(numRenglon, temporal));
                     if (fprimerDiferente) {
                         generarPadre("tk_;", numRenglon);
                         generarPadre("IDASIG", numRenglon);
                         fprimerDiferente=false;
                     }else{
                         generarPadre("LISTA", numRenglon);
                         generarPadre("tk_,", numRenglon);
                         generarPadre("IDASIG", numRenglon);
                     }
                temporal = pila.pop();
                numRenglon++;
                listaSalida.add(generarRenglon(numRenglon, temporal));
                temporal = pila.pop();
                numRenglon++;
                listaSalida.add(generarRenglon(numRenglon, temporal));
                     generarPadreDosOpciones("tk_id","ASIG", numRenglon);
                
                pila.push("LISTA");
                contadorPila.pop();
                contadorPila.pop();
                contadorPila.pop();
                contadorPila.push(contadorRenglon);
                verPila();
                 }else{
                temporal = pila.pop();
                numRenglon++;
                listaSalida.add(generarRenglon(numRenglon, temporal));
                temporal = pila.pop();
                numRenglon++;
                listaSalida.add(generarRenglon(numRenglon, temporal));
                     generarPadreDosOpciones("tk_id", "ASIG", numRenglon);
                //generarPadre("tk_id", numRenglon);
                pila.push("LISTA");
                contadorPila.pop();
                contadorPila.pop();
                contadorPila.push(contadorRenglon);
                fprimerDiferente = true;
                 
                 verPila();
                 }
            }
                
            }
             if (esDeclaracion()) {
                temporal = pila.pop();
                numRenglon++;
                listaSalida.add(generarRenglon(numRenglon, temporal));
                 generarPadre("LISTA", numRenglon);
                 generarPadre("tk_,", numRenglon);
                 generarPadre("IDASIG", numRenglon);
                 temporal = pila.pop();
                 numRenglon++;
                 listaSalida.add(generarRenglon(numRenglon, temporal));
                 generarPadreDosOpciones("tk_INTEGER", "tk_REAL", numRenglon);
                 pila.push("DECLARACION");
                contadorPila.pop();
                contadorPila.pop();
                contadorPila.push(contadorRenglon);
                verPila();
            }
             
            
                if (esIdope()) {
                // if (fParametro) {
                    temporal = pila.pop();
                    numRenglon++;
                    listaSalida.add(generarRenglon(numRenglon, temporal));
                    temporal = pila.pop();
                    numRenglon++;
                    listaSalida.add(generarRenglon(numRenglon, temporal));
                    generarPadre("PARAMETRO", numRenglon);
                    generarPadre("TIPOOPE", numRenglon);
                    temporal = pila.pop();
                    numRenglon++;
                    listaSalida.add(generarRenglon(numRenglon, temporal));
                    temporal = pila.pop();
                    numRenglon++;
                    listaSalida.add(generarRenglon(numRenglon, temporal));
                    generarPadre("tk_id", numRenglon);
                    pila.push("IDOPE");
                 contadorPila.pop();
                 contadorPila.pop();
                 contadorPila.pop();
                 contadorPila.pop();
                 contadorPila.push(contadorRenglon);
                verPila();
                /* }else{
                    pila.pop();
                    pila.pop();
                    pila.push("IDOPE");
                System.out.println("entro if16"+pila.peek());  
                verPila();
                 }*/
                
            }
            
             if (esFin()) {
                temporal = pila.pop();
                numRenglon++;
                listaSalida.add(generarRenglon(numRenglon, temporal));
                temporal = pila.pop();
                numRenglon++;
                listaSalida.add(generarRenglon(numRenglon, temporal));
                pila.push("FIN");
                contadorPila.pop();
                contadorPila.pop();
                contadorPila.push(contadorRenglon);
                verPila();
            }
            for (int i = 0; i < 10; i++) {
                if (esCodigo()) {
                    if (fParametro) {
                        temporal = pila.pop();
                        numRenglon++;
                        listaSalida.add(generarRenglon(numRenglon, temporal));
                        if (fprimerCodigo) {
                            generarPadreTresOpciones("IDOPE", "LW", "DECLARACION", numRenglon);
                            temporal = pila.pop();
                            numRenglon++;
                            listaSalida.add(generarRenglon(numRenglon, temporal));
                            generarPadreTresOpciones("IDOPE", "LW", "DECLARACION", numRenglon);
                            fprimerCodigo=false;
                        }else{
                            generarPadre("CODIGO", numRenglon+1);
                            generarPadre("CODIGO", numRenglon+1);
                            generarPadreTresOpciones("IDOPE", "LW", "DECLARACION", numRenglon);
                            temporal = pila.pop();
                            numRenglon++;
                            listaSalida.add(generarRenglon(numRenglon, temporal));
                        }
                        fParametro=false;
                        pila.push("CODIGO");
                        contadorPila.pop();
                        contadorPila.pop();
                        contadorPila.push(contadorRenglon);
                        
                    }else{
                        temporal = pila.pop();
                        numRenglon++;
                        listaSalida.add(generarRenglon(numRenglon, temporal));
                        if (temporal.equals("DECLARACION")) {
                            generarPadre("LISTA", numRenglon);
                             generarPadre("TIPO", numRenglon);
                        }
                        if (temporal.equals("LW")) {
                            generarPadre("tk_;", numRenglon);
                             generarPadre("PARAMETRO", numRenglon);
                             generarPadre("FUNCION", numRenglon);
                        }
                        if (temporal.equals("IDOPE")) {
                            generarPadre("tk_;", numRenglon);
                            generarPadre("OPERACION", numRenglon);
                            generarPadre("tk_=", numRenglon);
                            generarPadre("IDASIG", numRenglon);
                            
                        }
                        pila.push("CODIGO");
                        contadorPila.pop();
                        contadorPila.push(contadorRenglon);
                        
                }
                  verPila();  
                }
            }
             if (esPrograma()) {
                temporal = pila.pop();
                numRenglon++;
                listaSalida.add(generarRenglon(numRenglon, temporal));
                 generarPadre("tk_}", numRenglon);
                 generarPadre("tk_END", cont);
                temporal = pila.pop();
                numRenglon++;
                listaSalida.add(generarRenglon(numRenglon, temporal));
                 generarPadre("CODIGO", numRenglon);
                 generarPadre("CODIGO", numRenglon);
                 
                 temporal=pila.pop();
                 numRenglon++;
                 listaSalida.add(generarRenglon(numRenglon, temporal));
                 generarPadre("tk_{", numRenglon);
                 generarPadre("tk_BEGIN", numRenglon);
                pila.push("PROGRAMA");
                contadorPila.pop();
                contadorPila.pop();
                contadorPila.pop();
                contadorPila.push(contadorRenglon);
                verPila();
            }
        }
    }
    public boolean esIdope(){
        if (pila.peek().equals("tk_;")) {
            String z = pila.pop();
        if (pila.peek().equals("OPERACION")) {
            String a = pila.pop();
            if (pila.peek().equals("tk_=")) {
               String b = pila.pop();
                if (pila.peek().equals("IDASIG")) {
                    pila.push(b);
                    pila.push(a);
                    pila.push(z);
                    //fParametro=true;
                return true;
                }else{
                    pila.push(b);
                    pila.push(a);
                    pila.push(z);
                }
            }else{
                pila.push(a);
                pila.push(z);
            }
        }else{
            pila.push(z);
        }
        }/*
        if (pila.peek().equals("tk_;")) {
            String a = pila.pop();
            if (pila.peek().equals("IDOPE")) {
                pila.push(a);
                fParametro=false;
                return true;
            }else{
                pila.push(a);
            }
        }else{
            
        }*/
        return false;
    }
    public boolean esCodigo(){
        if (pila.peek().equals("CODIGO")) {
            String a = pila.pop();
            if (pila.peek().equals("CODIGO")) {
                pila.push(a);
                fParametro=true;
                return true;
            }else{
                pila.push(a);
            }
        }
        if (pila.peek().equals("DECLARACION")) {
            fParametro = false;
            return true;
        }
        if (pila.peek().equals("IDOPE")) {
            fParametro = false;
            return true;
        }
        if (pila.peek().equals("LW")) {
            fParametro = false;
            return true;
        }
        
        return false;
    }
    public boolean esPrograma(){
        if (pila.peek().equals("FIN")) {
            String a = pila.pop();
            if (pila.peek().equals("CODIGO")) {
                String b = pila.pop();
                if (pila.peek().equals("INICIO")) {
                    pila.push(b);
                    pila.push(a);
                    fParametro = true;
                    return true;
                }else{
                    pila.push(b);
                    pila.push(a);
                }
            }else{
                 pila.push(a);
            }
        }
        return false;
    }
    public boolean esLista(){
        if (pila.peek().equals("LISTA")) {
            String a = pila.pop();
            if (pila.peek().equals("tk_,")) {
                String b = pila.pop();
                if (pila.peek().equals("IDASIG")) {
                    pila.push(b);
                    pila.push(a);
                    fParametro = true;
                    return true;
                }else{
                    pila.push(b);
                    pila.push(a);
                }
            }else{
                 pila.push(a);
            }
        }
        if (pila.peek().equals("tk_;")) {
            String a = pila.pop();
             if (pila.peek().equals("IDASIG")) {
                  pila.push(a);
                  fParametro = false;
                  
            return true;
        }else{
                  pila.push(a);
             }
        }
        return false;
    }
    
    public boolean esIdasig(){
        if (pila.peek().equals("tk_id")) {
            return true;
        }
         if (pila.peek().equals("ASIG")) {
             f2 = true;
            return true;
        }
         return false;
    }
    public boolean esAsig(){
        if (pila.peek().equals("VALOR")) {
            String a = pila.pop();
            if (pila.peek().equals("tk_=")) {
                String b = pila.pop();
                if (pila.peek().equals("IDASIG")) {
                    pila.push(b);
                    pila.push(a);
                   
                    return true;
                }else{
                    pila.push(b);
                    pila.push(a);
                }
            }else{
                 pila.push(a);
            }
        }
         if (pila.peek().equals("OPERACION")) {
            String a = pila.pop();
            if (pila.peek().equals("tk_=")) {
                String b = pila.pop();
                if (pila.peek().equals("tk_id")) {
                    pila.push(b);
                    pila.push(a);
                    
                    return true;
                }else{
                    pila.push(b);
                    pila.push(a);
                }
            }else{
                 pila.push(a);
            }
        }
        return false;
    }
    public boolean esAr(){
        if (pila.peek().equals("tk_;")) {
            String a = pila.pop();
            if (pila.peek().equals("LISTAOPE")) {
                String b = pila.pop();
                if (pila.peek().equals("tk_id")) {
                    pila.push(b);
                    pila.push(a);
                    
                    return true;
                }else{
                    pila.push(b);
                    pila.push(a);
                }
            }else{
                 pila.push(a);
            }
        }
        return false;
    }
    public boolean esListaOpe(){
        if (pila.peek().equals("OPERACION")) {
            String a = pila.pop();
            if (pila.peek().equals("tk_=")) {
                pila.push(a);
                return true;
            }else{
                pila.push(a);
                return false;
            }
        }else{
            return false;
        }
    }
    public boolean esOperacion(){
        /*
        if (pila.peek().equals("PARENTESIS")) {
            String a = pila.pop();
            if (pila.peek().equals("TIPOOPE")) {
                pila.push(a);
                return true;
            }else{
                pila.push(a);
                return false;
            }
        }else{
            return false;
        }
        */
        if (pila.peek().equals("PARAMETRO")) {
            String a = pila.pop();
            if (pila.peek().equals("TIPOOPE")) {
                pila.push(a);
                return true;
            }else{
                pila.push(a);
            }
        }else{
            
        }
        if (pila.peek().equals("tk_;")) {
            String a = pila.pop();
            if (pila.peek().equals("PARAMETRO")) {
                String b = pila.pop();
                if (pila.peek().equals("TIPOOPE")) {
                    pila.push(b);
                    pila.push(a);
                return true;
                }else{
                    pila.push(b);
                    pila.push(a);
                }
            }else{
                pila.push(a);
            }
        }else{
            
        }
        return false;
    }
    public boolean esTipoOpe(){
        if (pila.peek().equals("tk_ADD")) {
            return true;
        }
         if (pila.peek().equals("tk_SUB")) {
            return true;
        }
          if (pila.peek().equals("tk_MUL")) {
            return true;
        }
           if (pila.peek().equals("tk_DIV")) {
            return true;
        }
           return false;
    }
    public boolean esLW(){
        if (pila.peek().equals("tk_;")) {
            String a = pila.pop();
            if (pila.peek().equals("PARAMETRO")) {
                String b = pila.pop();
                if (pila.peek().equals("FUNCION")) {
                    pila.push(b);
                    pila.push(a);
                    
                    return true;
                }else{
                    pila.push(b);
                    pila.push(a);
                }
            }else{
                 pila.push(a);
            }
        }
        return false;
    }
    public boolean esParenteseis(){   //POSIBLE ERROR PORQUE PODRIA FALTAR (LISTA)
        if (pila.peek().equals("tk_)")) {
            String a = pila.pop();
            if (pila.peek().equals("PARAMETRO")) {
                String b = pila.pop();
                if (pila.peek().equals("tk_(")) {
                    pila.push(b);
                    pila.push(a);
                    
                    return true;
                }else{
                    pila.push(b);
                    pila.push(a);
                }
            }else{
                 pila.push(a);
            }
        }
        
        if (pila.peek().equals("tk_)")) {
            String a = pila.pop();
            if (pila.peek().equals("IDASIG")) {
                String b = pila.pop();
                if (pila.peek().equals("tk_(")) {
                    pila.push(b);
                    pila.push(a);
                    
                    return true;
                }else{
                    pila.push(b);
                    pila.push(a);
                }
            }else{
                 pila.push(a);
            }
        }
        return false;
    }
    
    public boolean esParametro(){
        if (pila.peek().equals("tk_)")) {
            String a = pila.pop();
            if (pila.peek().equals("IDASIG")) {
                pila.push(a);
                fParametro = false;
                fParametro2 =false;
                return true;
            }else{
                pila.push(a);
            }
        }else{
            
        }
        if (pila.peek().equals("tk_)")) {
            String a = pila.pop();
            if (pila.peek().equals("VALOR")) {
                pila.push(a);
                fParametro = false;
                fParametro2=false;
                return true;
            }else{
                pila.push(a);
            }
        }else{
            
        }
        if (pila.peek().equals("PARAMETRO")) {
            String a = pila.pop();
            if (pila.peek().equals("tk_,")) {
                String b = pila.pop();
                if (pila.peek().equals("IDASIG")) {
                    pila.push(b);
                    pila.push(a);
                    fParametro = true;
                    fParametro2=false;
                return true;
                }else{
                    pila.push(b);
                    pila.push(a);
                }
            }else{
                pila.push(a);
            }
        }else{
            
        }
        
        if (pila.peek().equals("PARAMETRO")) {
            String a = pila.pop();
            if (pila.peek().equals("tk_,")) {
                String b = pila.pop();
                if (pila.peek().equals("VALOR")) {
                    pila.push(b);
                    pila.push(a);
                    fParametro = true;
                    fParametro2=false;
                return true;
                }else{
                    pila.push(b);
                    pila.push(a);
                }
            }else{
                pila.push(a);
            }
        }else{
            
        }
        if (pila.peek().equals("PARAMETRO")) {
            String a = pila.pop();
            if (pila.peek().equals("tk_(")) {
                pila.push(a);
                fParametro=false;
                fParametro2=true;
                return true;
            }else{
                pila.push(a);
            }
        }else{
            
        }
        
        if (pila.peek().equals("tk_)")) {
            String a = pila.pop();
            if (pila.peek().equals("OPERACION")) {
                pila.push(a);
                fParametro = false;
                fParametro2=false;
                return true;
            }else{
                pila.push(a);
            }
        }else{
            
        }
        return false;
        
    }
    public boolean esValores(){
        if (pila.peek().equals("VALOR")) {
            String a = pila.pop();
            if (pila.peek().equals("tk_,")) {
                String b = pila.pop();
                if (pila.peek().equals("tk_id")) {
                    pila.push(b);
                    pila.push(a);
                    return true;
                }else{
                    pila.push(b);
                    pila.push(a);
                    
                }
            }else{
                pila.push(a);
                
            }
        }
        if (pila.peek().equals("tk_id")) {
            String a = pila.pop();
            if (pila.peek().equals("tk_,")) {
                String b = pila.pop();
                if (pila.peek().equals("VALOR")) {
                    pila.push(b);
                    pila.push(a);
                    return true;
                }else{
                    pila.push(b);
                    pila.push(a);
                    
                }
            }else{
                pila.push(a);
                
            }
        }
        
        return false;
    }
    public boolean esValor(){
        if (pila.peek().length()>=5) {
            //if (pila.peek().substring(0,5).equals("tk_num")) {
            if (pila.peek().equals("tk_num")) {
            return true;
        }
        }
        
        return false;
    }
    public boolean esTipo(){
        if (pila.peek().equals("tk_INTEGER")) {
            return true;
        }
        if (pila.peek().equals("tk_REAL")) {
            return true;
        }
       return false;
    }
    public boolean esFuncion(){
        if (pila.peek().equals("tk_READ")) {
            return true;
        }
        if (pila.peek().equals("tk_WRITE")) {
            return true;
        }
       return false;
    }
    public boolean esDeclaracion(){
        if (pila.peek().equals("LISTA")) {
            String a = pila.pop();
            if (pila.peek().equals("TIPO")) {
                pila.push(a);
                return true;
            }else{
                pila.push(a);
                return false;
            }
        }else{
            return false;
        }
    }
    public boolean esInicio(){
        if (pila.peek().equals("tk_{")) {
            String a = pila.pop();
            if (pila.peek().equals("tk_BEGIN")) {
                pila.push(a);
                return true;
            }else{
                pila.push(a);
                return false;
            }
        }else{
            return false;
        }
        
    }
    public boolean esFin(){
        if (pila.peek().equals("tk_END")) {
            String a = pila.pop();
            if (pila.peek().equals("tk_}")) {
                pila.push(a);
                return true;
            }else{
                pila.push(a);
                return false;
            }
        }else{
            return false;
        }
        
    }
    
    
    
    public ArrayList<String> acortarLista(ArrayList<String> a){
        
        int conta=0;
        int contb=0;
        ArrayList<String> listaAcortada = new ArrayList<String>();
        for (String palabra : a) {
            
            for (int i = palabra.length()-3; i > 0; i--) {
              
                if (palabra.charAt(i)==' ') {
                    listaAcortada.add(palabra.substring(i+1, palabra.length()-2));
                    
                    if (palabra.substring(i+1, palabra.length()-2).equals("tk_id") || palabra.substring(i+1, palabra.length()-2).equals("tk_num")) {
                        for (int j = 2; j < palabra.length(); j++) {
                            if (palabra.charAt(j)==' ') {
                            listaIdyNum.add(palabra.substring(2,j));
                                j=palabra.length();
                            }
                        }
                    }
                    
                    
                    
                    i=0;
                }
            }
        }
        return listaAcortada;
    }
    public String MostrarLista(ArrayList lista){
      String a="";
      if (!lista.isEmpty()) {
          for (int i = 0; i < lista.size(); i++) {
              a+=lista.get(i).toString()+"\n";
          }
      }
      return a;
  }

    public ArrayList<String> getListaTokens() {
        return listaTokens;
    }

    public Stack<String> getPila() {
        return pila;
    }

    public ArrayList<String> getHistoriaPila() {
        return historiaPila;
    }

    public boolean isfParametro() {
        return fParametro;
    }
    
    public Renglon generarRenglon(int num,String lexema){
        Renglon renglon = new Renglon(num, lexema);
        return renglon;
    }
    public void generarPadre(String palabra,int num){
        for (int i = listaSalida.size()-2; i >= 0; i--) {
            if (listaSalida.get(i).getLexema().equals(palabra) && listaSalida.get(i).isFpadre()==false) {
                listaSalida.get(i).setPadre(num);
                listaSalida.get(i).setFpadre(true);
                break;
            }
        }
    }
    
    public void generarPadreDosOpciones(String palabra,String text,int num){
        for (int i = listaSalida.size()-2; i > 0; i--) {
            if (listaSalida.get(i).getLexema().equals(palabra) && listaSalida.get(i).isFpadre()==false ||   listaSalida.get(i).getLexema().equals(text) && listaSalida.get(i).isFpadre()==false  ) {
                listaSalida.get(i).setPadre(num);
                listaSalida.get(i).setFpadre(true);
                break;
            }
        }
    }
public void generarPadreTresOpciones(String palabra,String text,String text2,int num){
        for (int i = listaSalida.size()-2; i > 0; i--) {
            if (listaSalida.get(i).getLexema().equals(palabra) && listaSalida.get(i).isFpadre()==false ||   listaSalida.get(i).getLexema().equals(text) && listaSalida.get(i).isFpadre()==false ||   listaSalida.get(i).getLexema().equals(text2) && listaSalida.get(i).isFpadre()==false ) {
                listaSalida.get(i).setPadre(num);
                listaSalida.get(i).setFpadre(true);
                break;
            }
        }
    }
public void generarPadreCuatroOpciones(String palabra,String text,String text2,String text3,int num){
        for (int i = listaSalida.size()-2; i > 0; i--) {
            if (listaSalida.get(i).getLexema().equals(palabra) && listaSalida.get(i).isFpadre()==false ||   listaSalida.get(i).getLexema().equals(text) && listaSalida.get(i).isFpadre()==false ||   listaSalida.get(i).getLexema().equals(text2) && listaSalida.get(i).isFpadre()==false ||   listaSalida.get(i).getLexema().equals(text3) && listaSalida.get(i).isFpadre()==false) {
                listaSalida.get(i).setPadre(num);
                listaSalida.get(i).setFpadre(true);
                break;
            }
        }
    }
public int getSumaPosicionLexema(String palabra, String text){
            int a = 0;
            int x = 0;
        for (int i = listaSalida.size()-2; i > 0; i--) {
            
            if (listaSalida.get(i).getLexema().equals(palabra) && listaSalida.get(i).isFpadre()==false) {
                a=i;
            }
        }
        for (int i = listaSalida.size()-2; i > 0; i--) {
            if (listaSalida.get(i).getLexema().equals(text) && listaSalida.get(i).isFpadre()==false) {
                x=i;
            }
        }
        return a+x;
    }

public int getPosicionLexema(String palabra){
        for (int i = listaSalida.size()-2; i > 0; i--) {
            
            if (listaSalida.get(i).getLexema().equals(palabra) && listaSalida.get(i).isFpadre()==false) {
               return i;
            }
        }
       
        return 0;
    }

    public ArrayList<Renglon> getListaSalida() {
        return listaSalida;
    }
    public void aumentarRenglon(String text){
        if (text.equals("tk_{")) {
            contadorRenglon++;
        }
         if (text.equals("tk_;")) {
            contadorRenglon++;
        }
    }
   public String buscarErrores(){
        String res="";
        int resto = 0;
        int anterior = 0;
        int tamanoPila = pila.size();
        if (pila.size()>6) {
           resto=1;
       }
       for (int i = 0; i < tamanoPila; i++) {
           if (!pila.peek().equals("INICIO") && !pila.peek().equals("CODIGO") && !pila.peek().equals("FIN")) {
               if (!(anterior==contadorPila.peek())) {
                    res+="L"+(contadorPila.peek()-resto)+" Error de sintaxis\n";
                    anterior=contadorPila.peek();
               }
           }
           pila.pop();
           System.out.println(contadorPila.pop());
       }
       System.out.println("errores"+res);
       return res;
   }
}

