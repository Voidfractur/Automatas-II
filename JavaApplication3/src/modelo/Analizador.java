/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.awt.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Leonardo
 */
public class Analizador {
    private String texto;
    private static String[] textoS;
    private ArrayList listaTokens;
    private int cont;
    private ArrayList<Simbolo> listaSimbolos;
    private ArrayList listaErrores;

    public Analizador(String texto) {
        cont=0;
        listaTokens = new ArrayList();
        listaSimbolos = new ArrayList<Simbolo>();
        listaErrores = new ArrayList();
        this.texto = reemplazar(texto);
        seperarTexto();
        AnalizarCodigo();
        
    }

    public ArrayList getListaTokens() {
        return listaTokens;
    }

    public ArrayList<Simbolo> getListaSimbolos() {
        return listaSimbolos;
    }

    public ArrayList getListaErrores() {
        return listaErrores;
    }
    
    
  public void seperarTexto(){
      textoS= texto.split("\r\n|\r|\n");
    /*  
for (String line : textoS) {
       System.out.println(line);
}
*/
  }
  public void AnalizarCodigo(){
      for (int i = 0; i < textoS.length; i++) {
          esPalabaraReservada(textoS[i], i);
      }
  }
  
  public void esPalabaraReservada(String palabra,int linea){
      boolean fm1 = false; boolean fn1=false; boolean fn2=false; boolean fn3 = false;boolean fn4 = false; boolean fErrorIdentificador = false;
      boolean fErrorPalabraReservada=false; boolean fErrorNumer=false; boolean ferrorSimbolo = false;
      String a = "("; String sim = "";
      
      for (int i = 0; i < palabra.length(); i++) {
          
          if (palabra.charAt(i)=='C') {         // Busca las palabras begin
          cont++;
          a ="( BEGIN , tk_BEGIN"+" )";
          listaTokens.add(a);                   // añade la palabra encontrada a la lista de toknes
          }
          
          if (palabra.charAt(i)=='Y') {         // Busca las palabras reservadas integer
          cont++;
          a ="( INTEGER , tk_INTEGER"+" )";
          listaTokens.add(a);                   // añade la palabra encontrada a la lista de toknes
          }
          
          if (palabra.charAt(i)=='F') {         // Busca las palabras reservadas end
              cont++;
          a ="( END , tk_END"+" )";
          listaTokens.add(a);                   // añade la palabra encontrada a la lista de toknes
          }
          
          if (palabra.charAt(i)=='H') {         // Busca las palabras reservadas real
              cont++;
           a ="( REAL , tk_REAL"+" )"; 
           listaTokens.add(a);                   // añade la palabra encontrada a la lista de toknes
          }
          
          if (palabra.charAt(i)=='J') {cont++;         // Busca las palabras reservadas read
          a ="( READ , tk_READ"+" )";
          listaTokens.add(a);                   // añade la palabra encontrada a la lista de toknes
          }
          
          if (palabra.charAt(i)=='K') {cont++;         // Busca las palabras reservadas write
          a ="( WRITE , tk_WRITE"+" )";
          listaTokens.add(a);                   // añade la palabra encontrada a la lista de toknes
          }
          
          if (palabra.charAt(i)=='X') {cont++;         // Busca las palabras reservadas add
          a ="( ADD , tk_ADD"+" )";    
          listaTokens.add(a);                   // añade la palabra encontrada a la lista de toknes
          }
          
          if (palabra.charAt(i)=='O') {cont++;         // Busca las palabras reservadas sub
          a ="( SUB , tk_SUB"+" )";
          listaTokens.add(a);                   // añade la palabra encontrada a la lista de toknes
          }
          
          if (palabra.charAt(i)=='P') {cont++;         // Busca las palabras reservadas mul
          a ="( MUL , tk_MUL"+" )";  
          listaTokens.add(a);                   // añade la palabra encontrada a la lista de toknes
          }
          
          if (palabra.charAt(i)=='Q') {cont++;         // Busca las palabras reservadas div
          a ="( DIV , tk_DIV"+" )"; 
          listaTokens.add(a);                   // añade la palabra encontrada a la lista de toknes
          }
          
          // If que detecta la primera letra de un id
          if (contieneSoloLetras(palabra.charAt(i)+"")) {
              if (fm1==true) {                      // si encuentra otra letra de identificador es porque hay error
                  fErrorIdentificador=true;         // se enciende la bandera de error en identificador
              }else{                                //si la bandera fm1 esta apagada es porque  no tiene error
                  fm1=true;                         // se enciende la bandera de que encontro una letra de un identificador
              }
          }
          //detecta los numeros despues de la letra del id
          if (esNumero(palabra.charAt(i)+"")) {     //if para saber si es un carcter numerico
              if (fn1==false && fn2==false && fn3==false && fm1==true) {   // busca si las banderas de los numero estan encendidas
                  fn1=true;                                                 //enciende la bandera de que encontro el primer numero de un identificador
              }else{
              if (fn1==true && fn2==false && fn3==false && fm1==true) {     //si la bandera de uno esta encedida encienda la bandera numero 2
                  fn2=true;
                  
              }else{
              if (fn1==true && fn2==true && fn3==false && fm1==true) {      //si la bandera de dos esta encedida encienda la bandera numero 3
                  fn3=true;
              }else{
                  if (fn1==true && fn2==true && fn3==true && fm1==true) {   //si la bandera 3 esta encendiad y encuentra otro numero es porque
                      fErrorIdentificador=true;                             //hay un error en el identificador y enciende la bandera de error
                 }
              }
            }
          }
        }
          //detecta si ya termino de ller el identificador
          if (esSimboloDespuesIdentificador(palabra.charAt(i))) {   //detecta el simbolo que hay despues de un identificador
              if (fn1==false && fn2==false && fn3==false && fm1==true) {
                  
              }else{
              if (fn1==true && fn2==false && fn3==false && fm1==true && fErrorIdentificador==false) {//entra cuando hay una letra y un numero y la
                                                                                                     // bandera de error esta apagada
                  if (!existeSimbolo(palabra.substring(i-2, i))) {//busca que no exista el simbolo
                      
                      Simbolo simbolo1 = new Simbolo();
                      simbolo1.setId(palabra.substring(i-2, i));//se regresa a la posicion del simbolo y lo corta
                      simbolo1.setValor("");    
                      listaSimbolos.add(simbolo1);
                      cont++;
                      listaTokens.add("( "+palabra.substring(i-2, i)+" , tk_id"+" )");//lo añade a la lista de simbolos   
                      
                      fn1=false;
                      fn2=false;                                //apaga banderas
                      fn3=false;
                      fm1=false;
                  }else{
                      fn1=false;
                      fn2=false;
                      fn3=false;                                 //apaga banderas
                      fm1=false;
                      cont++;
                      listaTokens.add("( "+palabra.substring(i-2, i)+" , tk_id"+" )");//lo añade a la lista de simbolos   
                      
                  }
              }else{
              if (fn1==true && fn2==true && fn3==false && fm1==true && fErrorIdentificador==false) {//entra cuando hay una letra y dos numeros y la
                                                                                                     // bandera de error esta apagada
                  if (!existeSimbolo(palabra.substring(i-3, i))) {//busca que no exista el simbolo
                      Simbolo simbolo1 = new Simbolo();
                      simbolo1.setId(palabra.substring(i-3, i));//se regresa a la posicion del simbolo y lo corta
                      simbolo1.setValor("");
                      listaSimbolos.add(simbolo1);//lo añade a la lista de simbolos   
                      
                      
                      cont++;
                      listaTokens.add("( "+palabra.substring(i-3, i)+" , tk_id"+" )");//lo añade a la lista de simbolos   
                      
                      fn1=false;
                      fn2=false;
                      fn3=false;    //apaga banderas
                      fm1=false;
                  }else{
                      fn1=false;
                      fn2=false;    //apaga banderas
                      fn3=false;
                      fm1=false;
                      cont++;
                      listaTokens.add("( "+palabra.substring(i-3, i)+" , tk_id"+" )");//lo añade a la lista de simbolos   
                      
                  }
              }else{
                  if (fn1==true && fn2==true && fn3==true && fm1==true && fErrorIdentificador==false) {//entra cuando hay una letra y tres numeros y la
                                                                                                     // bandera de error esta apagada
                      if (!existeSimbolo(palabra.substring(i-4, i))) {//busca que no exista el simbolo
                      Simbolo simbolo1 = new Simbolo();
                      simbolo1.setId(palabra.substring(i-4, i));//se regresa a la posicion del simbolo y lo corta
                      simbolo1.setValor("");
                      listaSimbolos.add(simbolo1);//lo añade a la lista de simbolos 
                      
                      
                      cont++; 
                      listaTokens.add("( "+palabra.substring(i-4, i)+" , tk_id"+" )");//lo añade a la lista de simbolos   
                      
                      fn1=false;
                      fn2=false;
                      fn3=false;   //apaga banderas
                      fm1=false;
                  }else{
                      fn1=false;
                      fn2=false;   //apaga banderas
                      fn3=false;
                      fm1=false;
                      
                      cont++;
                      listaTokens.add("( "+palabra.substring(i-4, i)+" , tk_id"+" )");//lo añade a la lista de simbolos   
                      
                      }
                 }else{
                      //Agregar Error Identificador
          if (fErrorIdentificador==true) {      //verfica si hubo error en identificadores
              fErrorIdentificador=false;
              fm1=false;
              fn1=false;
              fn2=false;                              //apaga banderas 
              fn3=false;
              String error = "L "+linea+" id no valido";
              listaErrores.add(error);      //agrega a la lista de errores
          }
                  }
              }
            }
          }
          }
          ///////////// busca error en palabra reservada
          if (hayErrorPalabraReservada(palabra.charAt(i))) {    //busca si hay una letra diferente a las reservadas
              fErrorPalabraReservada=true;      //enciende bandera de error en palabra reservada
              fm1=false;
              fn1=false;            //apaga banderas de identificador
              fn2=false;
              fn3=false;
          }
          
          ////////////// Detectar simbolo
          if (perteneceSimbolo(palabra.charAt(i))) {    // busca si el simbolo es valido
              a=escribirSimbolo(palabra.charAt(i));     // si es valido lo agrega a la lista de tokens
              listaTokens.add(a);                          //lo agrega a la lista de tokens
          }
          
          
          //////////// Asignacion de valores
          
          
          if (esIgual(palabra.charAt(i))) { //busca el simbolo de asignacion
              try {
                  
              
              if (esNumero(palabra.charAt(i+1)+"")) {       //verifica que el carcter despues del igual sea un numero
                  try {
                  if (esNumero(palabra.charAt(i-1)+"")) {   //verifica que sea numero
                      String number =  buscarNumero(palabra.substring(i+1,palabra.length()));   //busca el numero desúes del igual
                  fErrorNumer = hayerrorNumero(number);     //verfica que el numero sea valido
                      System.out.println("numero "+number);
                  if (fErrorNumer==false) { //verfica que el numero sea valido
                      listaSimbolos.get(listaSimbolos.size()-1).setValor(number);//añade el valor al simbolo
                      
                      //agregar el numero a la lista de tokens
                      cont++;
                      listaTokens.add("( "+number+" , tk_num"+" )");//lo añade a la lista de simbolos   
                      
                  }
                  }else{
                      fErrorNumer=true;            //si hubo un error en el numero enciende la bandera de error
                  }
                  
                } catch (StringIndexOutOfBoundsException e) {
                    fErrorNumer=true;
                }
              }
              } catch (StringIndexOutOfBoundsException e) {
                  fErrorNumer=true;
              }
          }
          
          //hay numero despues de parentecis
          if (esParentecis(palabra.charAt(i))) { //compara si es un parentecis (
              try {
                  
              
              if (esNumero(palabra.charAt(i+1)+"")) {       //verifica que el carcter despues del parentecis ( sea un numero
                  try {
                 
                        String number =  buscarNumero(palabra.substring(i+1,palabra.length()));   //busca el numero desúes del parentecis
                        fErrorNumer = hayerrorNumero(number);     //verfica que el numero sea valido
                  
                        if (fErrorNumer==false) { //verfica que la bandera este apagada
                      
                            //agregar el numero a la lista de tokens
                            cont++;
                            listaTokens.add("( "+number+" , tk_num"+" )");//lo añade a la lista de simbolos   
                      
                        }
                 
                  
                       } catch (StringIndexOutOfBoundsException e) {
                    fErrorNumer=true;
                }
              }
              } catch (StringIndexOutOfBoundsException e) {
                  fErrorNumer=true;
              }
          }
          //hay numero despues de coma
          if (esComa(palabra.charAt(i))) { //compara si es una coma
              try {
              if (esNumero(palabra.charAt(i+1)+"")) {       //verifica que el carcter despues de la coma , sea un numero
                  try {
                 
                        String number =  buscarNumero(palabra.substring(i+1,palabra.length()));   //busca el numero desúes de la coma
                        System.out.println("number coma "+number);
                        fErrorNumer = hayerrorNumero(number);     //verfica que el numero sea valido
                  
                        if (fErrorNumer==false) { //verfica que la bandera este apagada
                      
                            //agregar el numero a la lista de tokens
                            cont++;
                            listaTokens.add("( "+number+" , tk_num"+" )");//lo añade a la lista de simbolos   
                      
                        }
                 
                  
                       } catch (StringIndexOutOfBoundsException e) {
                    fErrorNumer=true;
                }
              }
              } catch (StringIndexOutOfBoundsException e) {
                  fErrorNumer=true;
              }
          }
          
          
          
          if (fErrorNumer==true) {      //verifica bandera de error en numero
          listaErrores.add("L"+linea+" Error en Numero");//añade el error a la lista de errores
      }
          if (hayErrorSimbolo(palabra.charAt(i))) {//verifica si es simbolo diferente a los permitidos
              ferrorSimbolo=true;                  //enciende la bandera de error
          }
          
      } 
      
      if (fErrorPalabraReservada==true) {          //verifica bandera de error en palabra reservada
          listaErrores.add("L"+linea+" Error en palabra reservada");//añade el error a la lista de errores
      }
      if (ferrorSimbolo==true) {                     //verifica bandera de error en simbolo reservada
          listaErrores.add("L"+linea+" Error de simbolo");  //añade el error a la lista de errores
      }
      
      
  } 
  public boolean hayerrorNumero(String numero){ //verifica que los numeros no tengan mas de un punto
      int cont1 =0; int cont2 =0; boolean punto = false;
     
      for (int i = 0; i < numero.length(); i++) {
          if (esNumero(numero.charAt(i)+"") && punto==false) {  //cuenta los numero antes del punto
              cont1++;
             
          }
          if (esPunto(numero.charAt(i)) && punto==true) {   //si encuentra otro punto y la bandera esta encendida hay un error
             
               return true;
               
          }
          if (esPunto(numero.charAt(i)) && punto==false) {  //encuentra un punto y enciende la bandera punto
              punto=true;
          }
          
              
          if (esNumero(numero.charAt(i)+"") && punto==true) {   //cuenta los numero despues del punto
              cont2++;
          }
      }
     
      if (cont1 >5) {       
          
          return true;
      }
      if (cont2 >2) {
          
          return true;
      }
          return false;
      
      
  }
  public String buscarNumero(String word){      //busca el numero despues de un identificador
     
      for (int i = 0; i <word.length(); i++) {
          if (esSimboloDespuesIdentificador(word.charAt(i))) {
             
              return word.substring(0,i);
          }
      }
      return"";
  }
  public boolean esParentecis(char letra){
      if(letra=='(') return true;
      
      return false;
  }
   public boolean esComa(char letra){
      if(letra==',') return true;
      
      return false;
  }
  
  public boolean esIgual(char letra){
      if(letra=='=') return true;
      
      return false;
  }
  
  public boolean esPunto(char letra){
      if(letra=='.') return true;
      return false;
  } 
  public boolean hayErrorSimbolo(char letra){
      if(letra=='|') return true;
      if(letra=='°') return true;
      if(letra=='¬') return true;
      if(letra=='!') return true;
      if(letra=='"') return true;
      if(letra=='#') return true;
      if(letra=='$') return true;
      if(letra=='%') return true;
      if(letra=='&') return true;
      if(letra=='/') return true;
      if(letra=='?') return true;
      if(letra=='¿') return true;
      if(letra=='¡') return true;
      if(letra=='¨') return true;
      if(letra=='´') return true;
      if(letra=='+') return true;
      if(letra=='*') return true;
      if(letra==']') return true;
      if(letra=='[') return true;
      if(letra=='-') return true;
      if(letra=='_') return true;
      if(letra==':') return true;
      if(letra=='<') return true;
      if(letra=='>') return true;
      return false;
  }
  
  public boolean hayErrorPalabraReservada(char letra){  //busca que no encuentre nunguna de estas letras
      if(letra=='A') return true;
      if(letra=='B') return true;
      if(letra=='D') return true;
      if(letra=='E') return true;
      if(letra=='G') return true;
      if(letra=='I') return true;
      if(letra=='L') return true;
      if(letra=='M') return true;
      if(letra=='N') return true;
      if(letra=='R') return true;
      if(letra=='S') return true;
      if(letra=='T') return true;
      if(letra=='U') return true;
      if(letra=='V') return true;
      if(letra=='W') return true;
      return false;
  }
  public boolean esNumero(String text){
      if (text.matches("[0-9]+")){
          return true;
      }
      return false;
  }
  public static boolean contieneSoloLetras(String cadena) {
        for (int x = 0; x < cadena.length(); x++) {
            char c = cadena.charAt(x);
            // Si no está entre a y z
            if (!((c >= 'a' && c <= 'z'))) {
                return false;
            }
        }
        return true;
    }
  public void MostrarLista(ArrayList lista){
      String a="";
      if (!lista.isEmpty()) {
          for (int i = 0; i < lista.size(); i++) {
              a+=lista.get(i).toString()+"\n";
          }
      }
      JOptionPane.showMessageDialog(null, a);
  }
  
  public boolean esSimboloDespuesIdentificador(char letra){
      if(letra==')') return true;
      if(letra==';') return true;
      if(letra==',') return true;
      if(letra=='=') return true;
      return false;
  }
  public boolean perteneceSimbolo(char letra){
      if(letra=='{') return true;
      if(letra=='}') return true;
      if(letra=='(') return true;
      if(letra==')') return true;
      if(letra==';') return true;
      if(letra==',') return true;
     // if(letra=='.') return true;
      if(letra=='=') return true;
      return false;
  }
  public String escribirSimbolo(char letra){
      cont++;
      if(letra=='{') return "( { , tk_{"+" )";
      if(letra=='}') return "( } , tk_}"+" )";;
      if(letra=='(') return "( ( , tk_("+" )";;
      if(letra==')') return "( ) , tk_)"+" )";;
      if(letra==';') return "( ; , tk_;"+" )";;
      if(letra==',') return "( , , tk_,"+" )";;
      if(letra=='.') return "( . , tk_."+" )";;
      if(letra=='=') return "( = , tk_="+" )";;
      return "";
  }
  public boolean existeSimbolo(String simbolo){
      if (listaSimbolos.isEmpty()) {
          return false;
      }else{
          String comparador="";
          for (int i = 0; i < listaSimbolos.size(); i++) {
              
              comparador=listaSimbolos.get(i).getId();
              
              if (comparador.compareTo(simbolo)==0) {
                  return true;
              }
          }
      }
      return false;
  }
  public String reemplazar(String tex){
      String //textoreemplazado = tex.replace("{", " {");
      textoreemplazado = tex.replace("INTEGER", "Y");
      textoreemplazado = textoreemplazado.replace("BEGIN", "C");
      textoreemplazado = textoreemplazado.replace("END", "F");
      textoreemplazado = textoreemplazado.replace("REAL", "H");
      textoreemplazado = textoreemplazado.replace("READ", "J");
      textoreemplazado = textoreemplazado.replace("WRITE ", "K");
      textoreemplazado = textoreemplazado.replace("ADD", "X");
      textoreemplazado = textoreemplazado.replace("SUB", "O");
      textoreemplazado = textoreemplazado.replace("MUL", "P");
      textoreemplazado = textoreemplazado.replace("DIV", "Q");
      //JOptionPane.showMessageDialog(null, textoreemplazado);
      return textoreemplazado;
  }
  
  
  
  
  
  
}
