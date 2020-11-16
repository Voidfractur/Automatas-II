/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author spart
 */
public class NewClass {
    public static void main(String[] args) {
        /*char a = 'A';
        if (esNumero(a+"")) {
            System.out.println("correcto");
        }else{
            System.out.println("falso");
        }*/
        String s1 = "java";
        String s2 = "java";
        System.out.println(s1 == s2);
        String var1 = "ja".concat("va");
        System.out.println(var1.intern());
        
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
    public static boolean esNumero(String text){
      if (text.matches("[0-9]+")){
          return true;
      }
      return false;
  }
}
