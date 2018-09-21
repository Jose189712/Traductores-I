/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author h
 */
public class Archivos {
    //ATRIBUTOS DE LA CLASE
    private static FileWriter fichero;
    //MÉTODOS DE LA CLASE PARA ABRIR Y ESCRIBIR EN EL ARCHIVO
    /**
     * Método utilzado para abrir el archivo o crearlo
     * @param F
     * @throws IOException 
     */
    protected void openFile(File F) throws IOException{
        if(F.exists() && !F.isFile()){
            throw new IOException (F.getName() + "No es un archivo");
        }else
        {//inicio del else para crear el archivo
            fichero = new FileWriter(F);        
        }//fin del else para abrir el archivo en forma de escritura
    }//Fin del método para abrir el archivo
   /**
    * Método utilizado para escribir el archivo temporal y el archivo tabsim
    * @param temporal
    * @throws IOException 
    */
    protected void escribirArchivo(ArrayList<String[]> temporal) throws IOException{
        try (BufferedWriter escribir = new BufferedWriter(fichero)) {//Inicio del try para declarar el objeto para escribir en el archivo
            String cadena="";
            for(int i = 0; i<temporal.size(); i++) 
            {//Inicio del for que recorre cada objeto del arraylist #1
                for (String get : temporal.get(i))
                {//Incio del for loop para recorrer cada dato del arreglo almacenado 
                    cadena += get + "|";
                }//Fin del for loop 
                escribir.write(cadena); 
                escribir.newLine();
                cadena = "";
            }//Fin del for que recorre cada objeto del arraylits #1
        }//Fin del try para evitar la instruccion de cerrar el bufer
    }//Fin del método para escribir en el archivo ya sea el tabsim o el temporal
    /**
     * Este método nos ayuda a cerrar el archivo creado en esta clase
     * @throws IOException 
     */
    protected void cerrarArchivo() throws IOException{
        fichero.close();
    }//Fin del método para cerrar el archivo
}//Fin de la clase Archivo
