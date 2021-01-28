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
    private int contadorDeRenglones;
    public Semantico(ArrayList<Renglon> nodosSintacticos, ArrayList<Simbolo> listaSimbolos) {
        this.nodosSintacticos = nodosSintacticos;
        this.listaSimbolos = listaSimbolos;
        listaHijos = new ArrayList<>();
        listaErrores = new ArrayList<>();
        contadorDeRenglones = 0;
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
            if (nodosSintactico.getLexema().equals("tk_BEGIN") || nodosSintactico.getLexema().equals("DECLARACION") || nodosSintactico.getLexema().equals("LW") || nodosSintactico.getLexema().equals("IDOPE")) {
                contadorDeRenglones++;
            }
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
            if (isIDOPE(nodosSintactico.getLexema())) {
                contadorDeRenglones--;
                decorarIDOPE(nodosSintactico);
                contadorDeRenglones++;
            }
            if (isLW(nodosSintactico.getLexema())) {
                verificarLW(nodosSintactico);
            }
        }
    }
    public void decorarIDOPE(Renglon renglon){
        String tipoIDOPE = "";
        for (int i = renglon.getNumero(); i >1; i--) {
            if (renglon.getNumero()==nodosSintacticos.get(i).getPadre()) {
                if (nodosSintacticos.get(i).getLexema().equals("IDASIG")) {
                    for (Renglon nodosSintactico1 : nodosSintacticos) {
                        if (nodosSintactico1.getPadre()==nodosSintacticos.get(i).getNumero()) {
                            
                            if (tieneTipo(buscarSimbolo(getInParentesis(nodosSintactico1.getLexema())))) {
                                
                                tipoIDOPE=buscarSimbolo(getInParentesis(nodosSintactico1.getLexema())).getTipo();
                                nodosSintacticos.get(i).getDecoracion().add(tipoIDOPE);
                                nodosSintacticos.get(i).getDecoracion().add(getInParentesis(nodosSintactico1.getLexema()));
                                nodosSintactico1.getDecoracion().add(tipoIDOPE);
                                nodosSintactico1.getDecoracion().add(getInParentesis(nodosSintactico1.getLexema()));
                            }else{
                                listaErrores.add("L "+contadorDeRenglones+" Error variable no declarada "+nodosSintactico1.getLexema());
                            }
                           
                            break;
                        }
                    }
                }
                if (nodosSintacticos.get(i).getLexema().equals("OPERACION")) {
                    decorarOperacion(buscarHijosOperacion(nodosSintacticos.get(i)), tipoIDOPE,1);
                    nodosSintacticos.get(i).getDecoracion().add(tipoIDOPE);
                }
            }
        }
        renglon.getDecoracion().add(tipoIDOPE);
    }
    public void verificarLW(Renglon renglon){
        for (int i = renglon.getNumero()-4; i < renglon.getNumero()+1; i++) {
            if (nodosSintacticos.get(i).getLexema().equals("PARAMETRO")) {
                buscaridasigParametro(nodosSintacticos.get(i));
            }
        }
    }
    public void buscaridasigParametro(Renglon renglon){
        for (int i = renglon.getNumero(); i > renglon.getNumero()-5; i--) {
            if (nodosSintacticos.get(i).getPadre()==renglon.getNumero() && nodosSintacticos.get(i).getLexema().equals("PARAMETRO")) {
                buscaridasigParametro(nodosSintacticos.get(i));
            }
            if (nodosSintacticos.get(i).getPadre()==renglon.getNumero() && nodosSintacticos.get(i).getLexema().equals("IDASIG")) {
                for (int j = nodosSintacticos.get(i).getNumero(); j > nodosSintacticos.get(i).getNumero()-4; j--) {
                    if (nodosSintacticos.get(j).getPadre()==nodosSintacticos.get(i).getNumero()) {
                        if (tieneTipo(buscarSimbolo(getInParentesis(nodosSintacticos.get(j).getLexema())))) {
                            
                        }else{
                             listaErrores.add("L "+contadorDeRenglones+" Error Semantico Variable no declarada"+" "+nodosSintacticos.get(j).getLexema()); //se agrega el error a la lista
                
                        }
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
                     listaErrores.add("L "+contadorDeRenglones+" Error Semantico doble declaracion"+" "+nodosSintactico.getLexema()); //como ya tiene un tipo añade el error a la lista de errores
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
                    listaErrores.add("L "+contadorDeRenglones+" Error Semantico en Lexema Sistemas numericos diferentes "+nodosSintactico.getLexema());
                }
                
                if (tipo.equals("REAL") && esEntero(nodosSintactico.getLexema())) {
                    listaErrores.add("L "+contadorDeRenglones+" Error Semantico en Lexema Sistemas numericos diferentes "+nodosSintactico.getLexema());
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
                     listaErrores.add("L "+contadorDeRenglones+" Error Semantico doble declaracion"+" "+nodosSintactico.getLexema());
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
    public String getPostBarraBaja(String lexema){
        int izq=0;
        for (int i = lexema.length()-1; i > 1 ; i--) {
           
            if (lexema.charAt(i)=='_') {
                izq = i;
                break;
            }
        }
        return lexema.substring(izq+1, lexema.length());
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
    
    public int decorarOperacion(ArrayList<Integer> listaHijos , String tipoIdope , int uno){
        ArrayList<String> listaTipos = new ArrayList<>();
        for (Integer listaHijo : listaHijos) {
            
            for (int i = listaHijo; i > 1; i--) {
                if (listaHijo.intValue()==nodosSintacticos.get(i).getNumero()) { //compara si los nodos corresponden a la lista de hijos obtenida
                    if (nodosSintacticos.get(i).getLexema().equals("IDASIG")) { //compara si se encontro un nodo hijo IDASIG
                        int position = getPosicionHijo(nodosSintacticos.get(i).getNumero()); // se obtiene la posicion en la lista del hijo
                        if (tieneTipo(buscarSimbolo(getInParentesis(nodosSintacticos.get(position).getLexema())))) { //se verifica que haya sido declarada la variable antes por si tiene asignado un tipo
                            nodosSintacticos.get(position).getDecoracion().add(buscarSimbolo(getInParentesis(nodosSintacticos.get(position).getLexema())).getTipo()); //decora el id agregandole el tipo
                            nodosSintacticos.get(position).getDecoracion().add(buscarSimbolo(getInParentesis(nodosSintacticos.get(position).getLexema())).getId()); //decora el id agregandole el id
                            
                            if (buscarSimbolo(getInParentesis(nodosSintacticos.get(position).getLexema())).getTipo().equals(tipoIdope)) { //compara el tipo del simbolo con el tipo de la primera variable
                                nodosSintacticos.get(i).getDecoracion().add(tipoIdope); //decora el nodo IDASIG
                                nodosSintacticos.get(i).getDecoracion().add(getInParentesis(nodosSintacticos.get(position).getLexema())); // agrega una segunda decoracion al nodo IDASIG
                                
                            }else{
                                listaTipos.add((buscarSimbolo(getInParentesis(nodosSintacticos.get(position).getLexema()))).getTipo());
                               // listaErrores.add("L "+contadorDeRenglones+" Error operacion con distintos tipos "+nodosSintacticos.get(position).getLexema()); //agrega el error a la lista de errores
                            
                            }
                            
                        }else{
                              listaErrores.add("L "+contadorDeRenglones+" Error variable no declarada "+nodosSintacticos.get(position).getLexema()); //agrega el error a la lista de errores
                        }
                    }
                    
                    //busqueda de valor
                    if (nodosSintacticos.get(i).getLexema().equals("VALOR")) { //busca si encontro un lexema valor
                        int position = getPosicionHijo(nodosSintacticos.get(i).getNumero()); //obtener la posicion de la lista del hijo en este caso de un num
                        boolean ferror = false; //bandera por si hay un error
                        if (esEntero(getInParentesis(nodosSintacticos.get(position).getLexema())) && tipoIdope.equals("REAL")) { //compara si es un numero entero y si el tipo de la primera variable es real
                            //listaErrores.add("L "+contadorDeRenglones+" Error operacion con distintos tipos "+nodosSintacticos.get(position).getLexema()); // agrega el error a la lista
                             listaTipos.add("INTEGER");
                            ferror = true; //enciende la bandera
                        }
                        if (!esEntero(getInParentesis(nodosSintacticos.get(position).getLexema())) && tipoIdope.equals("INTEGER")) { //compara si es un numero real y si el tipo de la primera variable es entero
                            //listaErrores.add("L "+contadorDeRenglones+" Error operacion con distintos tipos "+nodosSintacticos.get(position).getLexema());// agrega el error a la lista
                             listaTipos.add("REAL");
                            ferror = true; // enciende la bandera
                        }
                        if (!ferror) {
                            nodosSintacticos.get(i).getDecoracion().add(tipoIdope); //decora el nodo valor
                            nodosSintacticos.get(i).getDecoracion().add(getInParentesis(nodosSintacticos.get(position).getLexema())); // agrega una sefuna decoracion al nodo valor
                            
                            nodosSintacticos.get(position).getDecoracion().add(tipoIdope); //decora el nodo tk_num
                            nodosSintacticos.get(position).getDecoracion().add(getInParentesis(nodosSintacticos.get(position).getLexema())); // agrega una segunda decoracion al nodo tk_num
                        }
                    }
                    
                    //se encuentra un nodo operacion
                    if (nodosSintacticos.get(i).getLexema().equals("OPERACION")) { //compara si encontro un nodo operacion
                       uno = decorarOperacion(buscarHijosOperacion(nodosSintacticos.get(i)), tipoIdope,uno); // se vuelve a llamar a si misma para decorar los nodos de otra operacion
                        nodosSintacticos.get(i).getDecoracion().add(tipoIdope); // decora el nodo con el tipo
                    }
                    
                    //encuentra un tipoope
                    if (nodosSintacticos.get(i).getLexema().equals("TIPOOPE")) {  // compara si encotro un nodo tipoope
                        int position = getPosicionHijo(nodosSintacticos.get(i).getNumero()); //busca la posicion de tk_ADD o MUl o DIV o SUBB
                        
                        nodosSintacticos.get(i).getDecoracion().add(tipoIdope); //decora el nodo con el tipo de operacion
                        nodosSintacticos.get(i).getDecoracion().add(getPostBarraBaja(nodosSintacticos.get(position).getLexema())); // decora el nodo con la operacion
                        
                        nodosSintacticos.get(position).getDecoracion().add(tipoIdope); // decora el nodo con el tipo de operacion
                        nodosSintacticos.get(position).getDecoracion().add(getPostBarraBaja(nodosSintacticos.get(position).getLexema())); // decora el nodo con la operacion que se va a hacer
                    }
                    break;
                }
            }
        }
        if (!listaTipos.isEmpty() && uno==1) {
            boolean tiposiguales=true;
        for (String listaTipo : listaTipos) {
            if (!listaTipo.equals(listaTipos.get(0))) {
                tiposiguales=false;
            }
        }
        if (!listaTipos.get(listaTipos.size()-1).equals(tipoIdope) && tiposiguales ) {
            
            uno=0;
            listaErrores.add("L "+contadorDeRenglones+" Error Semantico operacion con distintos tipos "); //agrega el error a la lista de errores         
        }else{
            for (String listaTipo : listaTipos) {
                listaErrores.add("L "+contadorDeRenglones+" Error Semantico operacion con distintos tipos ");
            }
        }
        }
        return uno;
    }
    public int getPosicionHijo(int numero){
        for (int i = 0; i < nodosSintacticos.size()-1; i++) {
            if (nodosSintacticos.get(i).getPadre()==numero) {
                return i;
            }
        }
        return 0;
    }
    public ArrayList<Integer> buscarHijosOperacion(Renglon renglon){
        ArrayList<Integer> lista = new ArrayList<>();
        for (int i =  renglon.getNumero(); i > 0; i--) {
            if ( nodosSintacticos.get(i).getPadre()==renglon.getNumero()) {
                if (nodosSintacticos.get(i).getLexema().equals("PARAMETRO")) {
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
    public boolean isIDOPE(String text){
        return text.equals("IDOPE");
    }
    public boolean isLW(String text){
        return text.equals("LW");
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
    
