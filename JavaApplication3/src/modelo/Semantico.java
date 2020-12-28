package modelo;

import java.util.ArrayList;

/**
 *
 * @author spart
 */
public class Semantico {
 
    private ArrayList<Renglon> nodosSintacticos;
    private ArrayList<Simbolo> listaSimbolos;
    private ArrayList<Integer> listaHijos;
    private String tipo;
    private ArrayList<String> listaErrores;
    private ArrayList<String> lista;
    public Semantico(ArrayList<Renglon> nodosSintacticos, ArrayList<Simbolo> listaSimbolos) {
        this.nodosSintacticos = nodosSintacticos;
        this.listaSimbolos = listaSimbolos;
        listaHijos = new ArrayList<>();
        listaErrores = new ArrayList<>();
    }

    public ArrayList<Renglon> getNodosSintacticos() {
        return nodosSintacticos;
    }

    public void setNodosSintacticos(ArrayList<Renglon> nodosSintacticos) {
        this.nodosSintacticos = nodosSintacticos;
    }

    public ArrayList<Simbolo> getListaSimbolos() {
        return listaSimbolos;
    }

    public void setListaSimbolos(ArrayList<Simbolo> listaSimbolos) {
        this.listaSimbolos = listaSimbolos;
    }
    
    public void BuscarDeclaracion(){
        for (Renglon nodosSintactico : nodosSintacticos) {
            if (isDeclaracion(nodosSintactico.getLexema())) {
                if (isInteger(getTipoDeclaracion(nodosSintactico).getLexema())) {
                    tipo="INTEGER";
                    decorarHijosLista(buscarHijosLista(buscarPadreDeclaracion(nodosSintactico)));
                }else{
                    if (isReal(getTipoDeclaracion(nodosSintactico).getLexema())) {
                        tipo="REAL";
                        decorarHijosLista(buscarHijosLista(buscarPadreDeclaracion(nodosSintactico)));
                    }
                }
            }
        }
    }
    
    public void decorarHijosLista(ArrayList<Integer> listaHijos){
        for (Integer listaHijo : listaHijos) {
            
            for (int i = 0; i < nodosSintacticos.size(); i++) {
                if (nodosSintacticos.get(i).getNumero()==listaHijo.intValue()) {
                    if (nodosSintacticos.get(i).getLexema().equals("IDASIG")) {
                        for (Renglon nodosSintactico : nodosSintacticos) {
                            if (nodosSintactico.getPadre()==nodosSintacticos.get(i).getNumero()) {
                                
                                if (nodosSintactico.getLexema().equals("ASIG")) {
                                    decorarAsig(nodosSintactico, tipo);
                                    break;
                                }else{
                                   
                                    
                                   
                if (!tieneTipo(buscarSimbolo(getInParentesis(nodosSintactico.getLexema())))) { //verifica si ya tiene un tipo asignado si ya tiene uno es porque ya ha sido declarada antes
                    buscarSimbolo(getInParentesis(nodosSintactico.getLexema())).setTipo(tipo); //añade a la tabla de simbolos el tipo
                    nodosSintactico.getDecoracion().add(tipo); //decora el nodo añadiendo el tipo
                    nodosSintactico.getDecoracion().add(getInParentesis(nodosSintactico.getLexema())); //decora el nodo añadiendo la variable
                    nodosSintacticos.get(i).getDecoracion().add(tipo); //decora el idasig añadiendo el tipo
                    nodosSintacticos.get(i).getDecoracion().add(getInParentesis(nodosSintactico.getLexema())); // decora el idaig añadiendo la variable
                }else{
                     listaErrores.add("Error Semantico doble declaracion"+" "+nodosSintactico.getLexema()); //como ya tiene un tipo añade el error a la lista de errores
                }        
                                    break;
                                }
                            }
                        }
                    }
                }else{
                    if (i>listaHijo+5) {
                        break;
                    }
                }
            }
        }
    }
    public void decorarAsig(Renglon renglon,String tipo){
        renglon.getDecoracion().add(tipo);
        String valor = "";
        for (Renglon nodosSintactico : nodosSintacticos) {
            if (nodosSintactico.getPadre()==renglon.getNumero()) {
                if (nodosSintactico.getLexema().equals("VALOR")) {
                    valor = decorarValor(nodosSintactico);
                }else{
                    if (nodosSintactico.getLexema().equals("IDASIG")) {
                        decorarIdasig(nodosSintactico, valor);
                    }
                }
            }
        }
    }
    public String decorarValor(Renglon renglon){
       
        for (Renglon nodosSintactico : nodosSintacticos) {
            if (nodosSintactico.getPadre()==renglon.getNumero()) {
                nodosSintactico.getDecoracion().add(tipo);
                nodosSintactico.getDecoracion().add(getInParentesis(nodosSintactico.getLexema()));
                if (tipo.equals("INTEGER") && !esEntero(nodosSintactico.getLexema())) {
                    listaErrores.add("Error Semantico en Lexema Sistemas numericos diferentes"+nodosSintactico.getNumero()+" "+nodosSintactico.getLexema());
                }
                
                if (tipo.equals("REAL") && esEntero(nodosSintactico.getLexema())) {
                    listaErrores.add("Error Semantico en Lexema Sistemas numericos diferentes"+nodosSintactico.getNumero()+" "+nodosSintactico.getLexema());
                }
                renglon.getDecoracion().add(tipo);
                renglon.getDecoracion().add(getInParentesis(nodosSintactico.getLexema()));
                return getInParentesis(nodosSintactico.getLexema());
            }
        }
        return "";
    }
    public void decorarIdasig(Renglon renglon,String valor){
        for (Renglon nodosSintactico : nodosSintacticos) {
            if (nodosSintactico.getPadre()==renglon.getNumero()) {
                nodosSintactico.getDecoracion().add(tipo);
                nodosSintactico.getDecoracion().add(getInParentesis(nodosSintactico.getLexema()));
                if (!tieneTipo(buscarSimbolo(getInParentesis(nodosSintactico.getLexema())))) {
                    buscarSimbolo(getInParentesis(nodosSintactico.getLexema())).setTipo(tipo);
                    buscarSimbolo(getInParentesis(nodosSintactico.getLexema())).setValor(valor);
                }else{
                     listaErrores.add("Error Semantico doble declaracion"+" "+nodosSintactico.getLexema());
                }
                 renglon.getDecoracion().add(tipo);
                 renglon.getDecoracion().add(getInParentesis(nodosSintactico.getLexema()));
                break;
            }
        }
    }
    
    public boolean tieneTipo(Simbolo simbolo){       
        return simbolo.getTipo().length()>0;
    }
    public Simbolo buscarSimbolo(String id){
        for (Simbolo simbolo : listaSimbolos) {
            if (simbolo.getId().equals(id)) {
                return simbolo;
            }
        }
        return null;
    }
    
    
    public String getInParentesis(String lexema){
        int izq=0;
        for (int i = lexema.length()-1; i > 1 ; i--) {
           
            if (lexema.charAt(i)=='(') {
                izq = i;
                break;
            }
        }
        return lexema.substring(izq+1, lexema.length()-1);
    }
    
    public boolean esEntero(String lexema){
        for (int i = lexema.length()-1; i > 0 ; i--) {
            if (lexema.charAt(i)=='.') {
                return false;
            }
        }
        return true;
    }
    /*
    public int getPosicionRenglon(int numero){
        for (int i = 0; i < nodosSintacticos.size(); i++) {
            if (nodosSintacticos.get(i).getNumero()) {
                
            }
        }
    }*/
    public Renglon buscarPadreDeclaracion(Renglon renglon){
        for (Renglon nodosSintactico : nodosSintacticos) {
            if (nodosSintactico.getNumero()==renglon.getPadre()) {
                renglon=nodosSintactico;
                break;
            }
        }
        for (Renglon nodosSintactico : nodosSintacticos) {
            if (nodosSintactico.getPadre()==renglon.getPadre() && nodosSintactico.getLexema().equals("LISTA")) {
                return nodosSintactico;
            }
        }
        return null;
    }
    
    public ArrayList<Integer> buscarHijosLista(Renglon renglon){
        ArrayList<Integer> lista = new ArrayList<>();
        for (int i =  renglon.getNumero(); i > 0; i--) {
            if ( nodosSintacticos.get(i).getPadre()==renglon.getNumero()) {
                if (nodosSintacticos.get(i).getLexema().equals("LISTA")) {
                    renglon = nodosSintacticos.get(i);
                }
                lista.add(nodosSintacticos.get(i).getNumero());
            }
        }
        return lista;
    }
    
    public boolean isDeclaracion(String text){
        if (text.equals("tk_INTEGER") || text.equals("tk_REAL")) {
            return true;
        }
        return false;
    }
    public boolean isInteger(String text){
        if (text.equals("tk_INTEGER")) {
            return true;
        }
        return false;
    }
    public boolean isReal(String text){
        if (text.equals("tk_REAL")) {
            return true;
        }
        return false;
    }
    
    public Renglon getTipoDeclaracion(Renglon renglon){
        for (Renglon nodosSintactico : nodosSintacticos) {
            if (nodosSintactico.getPadre()==renglon.getNumero() && nodosSintactico.getLexema().equals("TIPO")) {
                renglon = nodosSintactico;
                break;
            }
        }
        
        for (Renglon nodosSintactico : nodosSintacticos) {
            if (nodosSintactico.getPadre()==renglon.getNumero()) {
                renglon = nodosSintactico;
                break;
            }
    }
         return renglon;
}

    public ArrayList<String> getLista() {
        return lista;
    }

    public void setLista(ArrayList<String> lista) {
        this.lista = lista;
    }

    public ArrayList<String> getListaErrores() {
        return listaErrores;
    }

    public void setListaErrores(ArrayList<String> listaErrores) {
        this.listaErrores = listaErrores;
    }
    
}
    
