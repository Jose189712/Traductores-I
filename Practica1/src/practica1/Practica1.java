//Practica 1
/*Identificacion de las partes de una linea de lenguaje ensamblador. Implementar el algoritmo para separar las partes de una linea
de lenguaje ensamblador.*/


package practica1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *Jose Rodriguez Sanchez
 *Luis Enrique Vazquez Lopez 
 */
public class Practica1 {

    int Nlinea=0;//Variable global para saber en que linea vamos
    public static void main(String[] args) throws FileNotFoundException, IOException {
        //DECLARACION DE VARIABLES
       String[] arregloCodigos = new String[3];//Arreglo para guardar los 3 codigos ETIQUETA-CODOPS-OPERANDOS  
       int opcion=59;//Varaible para las opciones del switch
       String guardarLinea;//String para guardar cada linea
       Practica1 practica1 = new Practica1();//objeto de la clase, para instanciar y mandar llamar metodos de la misma clase
       int posCodigo=0,Nlinea=0;       
       //Instrucciones para abrir el archivo
       File archivo = new File("P1ASM.txt");
                    
       //if para buscar si el archivo existe
       if((archivo.exists())!=true){
           System.out.println("El archivo no existe");
       }else{
            //Instrucciones para manejar el contenido del archivo
            FileReader practica = new FileReader(archivo);
            BufferedReader asm = new BufferedReader(practica);
                     
       while((guardarLinea = asm.readLine())!=null){                //While para leer linea por linea
           Nlinea++;//Contador para conocer el numero de linea en el que estamos
        
           if(guardarLinea.length()>0){//if para ignorar los saltos de linea sin instrucciones   
               
              if((opcion=practica1.leerCadena(guardarLinea))!=0){//if para ignorar los saltos de linea pero que tengan espacios o tabuladores                        
                opcion = guardarLinea.codePointAt(0);//Se obtiene el identificador en codigo ascii del primer caracter
                posCodigo = 0;//Se reinicia la variable del posicion ya que sera otra linea la evaluada           
                System.out.println("Linea: "+Nlinea);//Imprimir el numero de en que se encuentra          
              }//fin del if 
            
            switch(opcion){         //switch el cual representa el estado inicial de un automata                     
               case 59:// Caso para comentarios si la linea comienza con un ;
                   practica1.analizarComentario(guardarLinea);//aqui  se manda llamar el metodo
                   break;
               case 32://Caso para cuando la linea comienza con espacios 
                   practica1.recorrerEspacios(arregloCodigos, guardarLinea, posCodigo);   
                   practica1.imprimir(arregloCodigos);
                   break;
               case 9://Caso para cuando la linea comienza con tabuladores
                   practica1.recorrerEspacios(arregloCodigos, guardarLinea, posCodigo);
                   practica1.imprimir(arregloCodigos);
                   break;                                                  
               case 0://Caso para cuando la linea solo es espacios y/o tabuladores con un enter
                   opcion = 59;//Asignacion para controlar que no se imprima nadaya que no existen instrucciones
                   break;
               default://El caso por default es cuando la linea comienza con cualquier caracter que no sea espacio o tabulador
                   practica1.etiqueta(arregloCodigos, guardarLinea, posCodigo);
                   practica1.imprimir(arregloCodigos);
                   break;
           }//Fin del switch
            
            //If que hara salir del ciclo si se encontro la palabra end
            if(practica1.comparar(arregloCodigos, "end", 1)==true){
                break;
            }//Fin del if para salir del ciclo
                                
        }//fin del if que condiciona e ignora el que haya un salto de linea
        
           practica1.limpiarArreglo(arregloCodigos);//Instruccion para limpiar el arreglo
           
       }//Fin del while
            practica.close();//Instruccion para cerrar el archivo
       }//fin del if else para saber si el archivo existe
    }//Fin del main
    
    
    
    
    /**
     * 
     * @param codigos
     * @param x
     * @param posicion
     * @return 
     */
    public boolean comparar(String[] codigos, String x, int posicion){
        if(codigos[posicion]!=null && codigos[posicion].toLowerCase().equals(x)){
            return true;
        }else{
            return false;
        }//Fin del if else para saber si el objeto es igual o no
    }//Fin del método comparar
    /**
     * Método para mandar imprimir lo que contiene el arreglo
     * @param codigos 
     */
    public void imprimir(String[] codigos){
        if(codigos[1].equals("null")){
            System.out.println("!!!Error¡¡¡ No tiene codigo de operacion o contiene un error");
        }//Fin del if que revisa y manda un mensaje si no hay codigo de operacion
        else{
            //Instrucciones para imprimir lo que se pide
            System.out.println("\nEtiqueta:"+codigos[0]);
            System.out.println("CODOP:"+codigos[1]);
            System.out.println("Operando:"+codigos[2]+"\n");
        }//fin del else
    }//Fin del método para imprimir
    /**
     * 
     * @param revision
     * @return 
     */
    public int leerCadena(String revision){
       int retorno=0;
        for(int i = 0; i<revision.length(); i++){
            if(revision.codePointAt(i)!=32 && revision.codePointAt(i)!=9){
                retorno=1;
            }//fin del if para identificar si la linea solo tiene espacios
        }//fin del for         
        return retorno;
    }//Fin del método
    /**
     * 
     * @param codigos 
     */
    public void limpiarArreglo(String[] codigos){        
        for(int i = 0; i<codigos.length; i++){
            codigos[i] = "null";
        }//fin del for
    }//fin del método    
    //MÉTODOS QUE REPRESENTAN EL AUTOMATA
    /**
     * Este es nuestro metodo para analizar los comentarios
     * @param comentario 
     */
    public void analizarComentario(String comentario){        
        int numeroError=0;
        System.out.println("COMENTARIO\n");//Este print nos imprimira cuando detecte un comentario
        
        if(comentario.length()>80){   //Este if detecta si excede el limite el comenatio
            System.out.println("!!!Comentario excedio el limite¡¡¡¡"); //Esto se imprimira cuando el comenatrio exceda el limite
        }//Fin del if
        
        
        for(int i=1; i<comentario.length(); i++){// este for recorre posicion por posicion, para analizar cada caracter dle comentario
            if(comentario.substring(i, i+1).equals(";")){//Este if contara por cuantas palabras se sobrepasa el comentario
                numeroError++;
            }//fin del if
        }//fin del for
        
        if(numeroError!=0){//Este if evalua cuantos ; tiene de mas
            System.out.println("!!!ERROR, El comentario tiene "+numeroError+" ; de mas");// Este print te dice por cuanto se paso el comentario
        }//fin del if
    }//Fin del matodo analizarComentario
    /**
     * 
     * @param codigos
     * @param lineaArchivo
     * @param posCodigo 
     */
    public void recorrerEspacios(String[] codigos, String lineaArchivo, int posCodigo){//Metodo para recorrer espacios       
        String reducirLinea;        
        posCodigo++;//indica si es CODIGO,ETIQUETA,OPERANDO
            for(int i = 0; i<lineaArchivo.length(); i++){//Recorre las posiciones de la cadena
                if(lineaArchivo.codePointAt(i)!=32 && lineaArchivo.codePointAt(i)!=9){//codepointat convierte un caracter a codigo ASCII, el 32 es espacio.
                    reducirLinea = lineaArchivo.substring(i);//cuando dectecto un caracter diferente a espacio, almacena de hay en adelante
                        if(posCodigo==1){//Este if se activa cuando es un codigo de operacion
                            //Mandar llamar método de CODOP
                            codigoOperacion(codigos, reducirLinea, posCodigo);//
                        }else if(posCodigo==2){//Se activa cuando es un operando
                            //Mandar llamar método de Operando                            
                            operando(codigos, reducirLinea, posCodigo);
                        }else{
                            System.out.println("Ha escrito instrucciones de mas");
                        }//Fin del if else
                    break;
                }//Fin del if                                 
                
                if((lineaArchivo.length()-1)==i){//Como dice el documento despues de la ultima instruccion solo debe de haber retorno de carro
                    System.out.println("¡¡¡ERROR!!! Existen espacios despues de la ultima instruccion de la linea");
                }//fin del if para validar cuando haya espacios y se haya terminado la linea                                 
            }//Fin del for 
            
    }//Fin del método para recorrer los espacios
    /**
     * 
     * @param codigos
     * @param lineaArchivo
     * @param posCodigo 
     */
    public void etiqueta(String[] codigos, String lineaArchivo, int posCodigo){        
        int primerLetra = lineaArchivo.codePointAt(0);
        String etiqueta="",reducirLinea;//Variable para almacenar la etiqueta
        int caracterInvalido = 0;//variable para identificar los errores de caracteres en la cadena
        
        //If para saber si el caracter es una letra mayuscula o minuscula
            if((primerLetra < 65 || primerLetra > 90) && (primerLetra < 97 || primerLetra > 122)){
                System.out.println("El caracter inicial de la etiqueta es incorrecto");
            }//Fin del if     
            
        for(int i = 0; i<lineaArchivo.length(); i++){//inicio del for para recorrer caracter por caracter
            
            if((lineaArchivo.codePointAt(i)==32 || lineaArchivo.codePointAt(i)==9) || (lineaArchivo.length()-1)==i){//para salirnos del método y del ciclo cuando se detecte un espacio o el fin de la linea
                
                if(etiqueta.length()>8){//if para determinar si se paso del limite de longitud
                    System.out.println("La etiqueta ha sobre pasado el limite de caracteres");
                }//fin del if etiqueta>8
                
                if(caracterInvalido>0){//if que toma el contador "caracterInvalido" para mandar uun mensaje si fue aumentado
                    System.out.println("La etiqueta tiene caracteres invalidos");
                }//fin del if caracterinvalido > 0
                
                if((lineaArchivo.length()-1)==i){//
                    etiqueta += lineaArchivo.substring(i);
                }//fin del if 
                else{
                    reducirLinea = lineaArchivo.substring(i);
                    recorrerEspacios(codigos, reducirLinea, posCodigo);
                }//fin del else
                
                codigos[posCodigo] = etiqueta;
                break;                                
            }//fin del if lineaArchivo codigo ascii == 32
            
            etiqueta += lineaArchivo.substring(i, i+1);//Instruccion para ir concatenando caracter por caracter
            
            //if utilizado que aumenta contador para validar que cuando no sea letra, numero o guion bajo
            if((lineaArchivo.codePointAt(i) < 65 || lineaArchivo.codePointAt(i) > 90) && (lineaArchivo.codePointAt(i) < 97 || lineaArchivo.codePointAt(i) > 122) && lineaArchivo.codePointAt(i)!=95 && (lineaArchivo.codePointAt(i)<48 || lineaArchivo.codePointAt(i)>57)){
                caracterInvalido++;
            }//fin del if para detectar caracteres invalidos
            
        }//Fin del for para recorrer los caracteres
        
    }//Fin del método pára analizar etiquetas
    /**
     * 
     * @param codigos
     * @param lineaArchivo
     * @param posCodigo 
     */
    public void codigoOperacion(String[] codigos, String lineaArchivo, int posCodigo){        
       int Npuntos=0, caracterDistinto=0;
        int primerLetra = lineaArchivo.codePointAt(0); 
        String codigoOperacion="", reducirLinea;
                                               
            for(int i = 0; i<lineaArchivo.length(); i++){                 
                //if para salir del programa cuando se haya detectado la palabra "END"
                if(codigoOperacion.toLowerCase().equals("end")){                                                               
                        codigos[posCodigo]=codigoOperacion;                        
                        return;
                    }//fin del if de palabra end                                
                    
                if((lineaArchivo.codePointAt(i)==32 || lineaArchivo.codePointAt(i)==9) || (lineaArchivo.length()-1)==i){//If para revisar si ya se termino la palabra que corresponde al CODOP                    
                                                                               
                    //If para saber si el caracter es una letra mayuscula o minuscula
                    if((primerLetra < 65 || primerLetra > 90) && (primerLetra < 97 || primerLetra > 122)){
                        System.out.println("El caracter inicial del codigo de opercaion es incorrecto");
                        // codigos[posCodigo] = "null";
                        return;//Instruccion para salir del método ya que no tiene caso buscarlo en el tabop porque no se encontrara
                    }//Fin del if 
                    
                    else if(Npuntos>1){//If para mandar un mensaje si se excedio del numero de puntos
                        System.out.println("Codigo de operacion incorrecto por exceso de puntos");
                        return;
                    }//Fin del if que condiciona el numero de puntos                    
                    
                    else if(caracterDistinto!=0){//if para mandar un mensaje si existe un caracter diferente a una letra o un punto
                        System.out.println("A ingresado un caracter incorrecto en el codigo de operacion");
                        return;
                    }//Fin del if
                    
                    else if(codigoOperacion.length()>5){//if para saber si se excedio del limite de caracteres
                        System.out.println("Se excedio en los caracteres del codigo de operacion"); 
                        return;
                    }//Fin del if                 
                            
                    if((lineaArchivo.length()-1)==i){//if para concatenar la cadena completa que cuando esta sea la utlima de la linea
                        System.out.println("Entro aqui porque es el fin de la linea");
                        codigoOperacion += lineaArchivo.substring(i); 
                        codigos[posCodigo] = codigoOperacion;//Se almacena el codigo de operacion en el arreglo                        
                    }//Fin del if para 
                    else{
                        System.out.println("Entro aqui porque aun hay algo en la linea");
                        codigos[posCodigo] = codigoOperacion;//Se almacena el codigo de operacion en el arreglo
                        reducirLinea = lineaArchivo.substring(i);//Asignacion de lo que resta de la cadena una vez leido el codigo de operacion                    
                        recorrerEspacios(codigos, reducirLinea, posCodigo);//Se manda llamar el método de recorrer espacios en caso de que no haya sido el final de linea                                               
                    }//fin del else para no llamar el metodo de recorrer espacios en caso de que sea el final de la linea
                  break;                                 
                }//Fin del if
                    
                    codigoOperacion += lineaArchivo.substring(i, i+1);//Instrucciones para ir concatenando la cadena correspondiente a codigo de operacion
                    
                    if(lineaArchivo.codePointAt(i)==46 ){//If para identificar cuantos puntos se han puesto en el CODOP                       
                        Npuntos++;
                    }//fin del if 
                    
                    //If para identificar si el el CODOP tiene un caracter que no sea una letra o punto
                    if((lineaArchivo.codePointAt(i) < 65 || lineaArchivo.codePointAt(i) > 90) && (lineaArchivo.codePointAt(i) < 97 || lineaArchivo.codePointAt(i) > 122) && lineaArchivo.codePointAt(i)!=46){
                        caracterDistinto++;
                    }//Fin del if
                                        
                                        
            }//Fin del for            
    }//Fin del método que detecta codigos de operacion
    /**
     * 
     * @param codigos
     * @param lineaArchivo
     * @param posCodigo 
     */
    public void operando(String[] codigos, String lineaArchivo, int posCodigo){        
        String operando="", reducirLinea;//variable donde se guardara el operando
        
        for(int i = 0; i<lineaArchivo.length(); i++){            
            
            if((lineaArchivo.codePointAt(i)==32 || lineaArchivo.codePointAt(i)==9) || (lineaArchivo.length()-1) == i){                                
                
                if((lineaArchivo.length()-1)==i){//Sirve para concatenar de manera correcta todo el codigo
                    operando += lineaArchivo.substring(i);
                }//Fin del if 
                else{
                reducirLinea = lineaArchivo.substring(i);
                recorrerEspacios(codigos, reducirLinea, posCodigo);
                }//fin del else
                
                codigos[posCodigo] = operando;
                break;
            }//fin del if
            operando += lineaArchivo.substring(i, i+1);
        }//fin del for para guardar el operando
        
    }//Fin del método que detecta el operando de la linea
    
}//Fin de la clase practica 1
