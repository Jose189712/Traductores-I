/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica1;

import javax.swing.JOptionPane;
import static practica1.Practica1.error;

/**
 *
 * @author h
 */
public class Directivas {
    //ATRIBUTOS DE LA CLASE DIRECTIVAS
    private int DIR_INIC;    
    private boolean error;
    private int incremento;
    //CONSTRUCTOR DE LOS ATRIBUTOS DE LA CLASE
    protected Directivas(){
        DIR_INIC = 0; 
        incremento = 0;
        error = true;
    }//Fin del constructor
    //FIN DEL CONSTRUCTOR
    
    //MÉTODOS PARA LA OBTENCION Y ASIGNACION DE VALORES A LOS ATRIBUTOS DE LA CLASE
    /**
     * Método utilizado para obtener el valor de DIR_INIC
     * @return 
     */
    public int getDIR_INIC(){        
        return DIR_INIC;
    }//FIN DEL MÉTODO PARA LA OBTENCION DE DIR_INIC
    /**
     * Método utilizado para la asignacion de un valor al atributo DIR_INIC
     * @param DIR_INIC 
     */
    public void setDIR_INIC(int DIR_INIC) {
        this.DIR_INIC = DIR_INIC;
    }//FIN DEL MÉTODO PARA LA ASIGNACION DE UN VALOR A DIR_INIC   
    /**
     * Esta variable error nos ayuda a identificar en el programa principal si hubo error con las directivas
     * @return 
     */
    public boolean isError() {
        return error;
    }//Fin del método para obtener el valor de error
    /**
     * Esta variable error nos ayuda a identificar en el programa principal si hubo error con las directivas
     * @param error 
     */
    public void setError(boolean error) {
        this.error = error;
    }//Fin del método para asignar un valor a las variable error 
    /**
     * Método utilizado para obtener el valor que tiene el atributo incremento
     * @return 
     */
    public int getIncremento() {
        return incremento;
    }//Fin del método getIncremento
    /**
     * Método utilizado para asignar un numero al atributo que sera lo que se le incremente al contador de localidades
     * @param incremento 
     */
    public void setIncremento(int incremento) {
        this.incremento = incremento;
    }//Fin del método setIncremento       
    //FIN DE LOS MÉTODOS PARA LOS ATRIBUTOS DE LA CLASE
    //----------------------------------------------------------------------------------------------------------------------------------    
    /**
     * Método utilizado para identificar el tipo de directiva que se ha escrito
     * @param codigos
     * @return 
     */
    protected String directivas(String[] codigos) {
        String opcion = "";
        switch (codigos[1].toLowerCase()) 
        {//Inicio del switch
            case "org":
                opcion = "1";
                break;
            case "dc.b":
            case "db":
            case "fcb":
                opcion = "2";
                break;
            case "dw":
            case "dc.w":
            case "fdb":
            case "ds":
            case "ds.b":
            case "rmb":
            case "ds.w":
            case "rmw":
                opcion = "3";
                break;
            case "fcc":
                opcion = "4";
                break;                                        
            case "equ":
                opcion = "5";
                break;
            default:
                System.out.println("ERROR No es una directiva");
                break;
        }//Fin del switch para identicar los tipos de directivas
        
        return opcion;
    } //fin del método para identificar las directivas
   /**
    * 
    * @param numero
    * @param codigos 
    */
   protected void directivasConstantes(String numero, String[] codigos){
       String cifra; 
       ModoDireccionamiento operacion = new ModoDireccionamiento(); 
       switch(numero)
        {
            case "1"://Este caso es para la directiva ORG                            
                cifra = operacion.quitarCeros(operacion.idenBase(codigos[2]));//Método idenBase siempre regresa el valor en hexadecimal          
                if(!"null".equals(cifra) && operacion.isError())
                {                                       
                        if(cifra.length() > 4)
                        {
                            JOptionPane.showMessageDialog(null, "Error ¡Se excedio el tamaño! en la linea "+Practica1.Nlinea);
                            setError(operacion.isError());
                        }else if(!"null".equals(codigos[0]))
                        {
                            JOptionPane.showMessageDialog(null, "Error ¡ORG no debe de tener etiqueta! en la linea "+Practica1.Nlinea);
                            setError(operacion.isError());
                        }else
                        {
                            setDIR_INIC(Integer.parseInt(cifra,16));
                            Practica1.setCONTLOCK(getDIR_INIC());
                        }                        
                }//Fin del if para validar que el operando sea correcto en cualquiera de las bases en que este  
                else
                {//Inicio del else #2.2                    
                    setError(false);    //Aqui se le asigna este valor a la variable global para no guardar datos incorrectos
                }//Fin del else #2.2
                Practica1.setOrg(true);                
            break;
            case "2"://Caso para DB, DC.B, FCB
               cifra = operacion.quitarCeros(operacion.idenBase(codigos[2]));
               if (!"null".equals(cifra) && operacion.isError())
               {//Inicio del if para verificar que no haya errores en la base #3.3
                   if(Integer.parseInt(cifra,16) >= 0 && Integer.parseInt(cifra,16) <= 255)
                   {//If que revisa que no se pase del rango establecido
                       this.setIncremento(1);//Se manda al atributo incremento el valor en que se debe incrementar el contador de localidades
                   }else
                   {
                       setError(false);
                       JOptionPane.showMessageDialog(null, "ERROR ¡Se excedio el tamaño! linea: "+Practica1.Nlinea);
                   }//Fin del if (rango 0 a 255) else
               }else//Fin if #3.3 e inicio del else para mandar mensaje de error 
               {                   
                  JOptionPane.showMessageDialog(null, "ERROR ¡En operando! Linea: "+Practica1.Nlinea);
               }//Fin del else para mandar un mensaje de error en el operando
                break;
            case "3"://Caso para DW, DC.W, FDB
                cifra = operacion.quitarCeros(operacion.idenBase(codigos[2]));
               if (!"null".equals(cifra) && operacion.isError())
               {//Inicio del if para verificar qeu no haya errores en la base #3.3
                   if(Integer.parseInt(cifra,16) >= 0 && Integer.parseInt(cifra,16) <= 65535)
                   {//If que revisa que no se pase del rango establecido
                       switch(codigos[1].toLowerCase())
                       {//Inicio del switch para todos los tipos de directivas que aceptan valores de 0 a 65535 pero se incrementa en distinta forma
                           case "dw":
                           case "dc.w":
                           case "fdb":
                               setIncremento(2);//Se manda al atributo incremento el valor en que se debe incrementar el contador de localidades                               
                               break;
                           case "ds.b":
                           case "rmb":
                           case "ds":
                               setIncremento(Integer.parseInt(cifra,16)*1);//Se manda al atributo incremento el valor en que se debe incrementar el contador de localidades                               
                               break;    
                           case "ds.w":
                           case "rmw":
                               setIncremento(Integer.parseInt(cifra,16)*2);//Se manda al atributo incremento el valor en que se debe incrementar el contador de localidades                               
                               break;                                       
                       }//Fin del switch "switch(codigos[2].toLowerCase())"                 
                   }else//Fin del if que revisa el rango de valores permitidios
                   {//Inicio del else para poder mandar un mensaje de error si se escedio el tamño
                       setError(false);
                       JOptionPane.showMessageDialog(null, "ERROR ¡Se excedio el tamaño! linea: "+Practica1.Nlinea);
                   }//Fin del if (rango 0 a 255) else
               }else//Fin if #3.3 e inicio del else para mandar mensaje de error 
               {                   
                  JOptionPane.showMessageDialog(null, "ERROR ¡En operando! Linea: "+Practica1.Nlinea);
               }//Fin del else para mandar un mensaje de error en el operando                            
                break;
            case "4"://Caso para FCC
                String cadena;
                if(codigos[2].substring(0, 1).equals("\"") && codigos[2].substring(codigos[2].length()-1).equals("\""))
                {//Inicio del if para validar que la cadena contenga las dos comillas
                    cadena = codigos[2].substring(1, (codigos[2].length()-1));
                    if(revisarCadenaAscii(cadena))
                    {//If que llama método para validar los caracteres del ASCII
                        setIncremento(cadena.length());
                    }else
                    {//Fin del if e incio del else para validar los caracteres del ASCII
                        JOptionPane.showMessageDialog(null, "ERROR ¡Algun caracter de la cadena es incorrecto! linea "+Practica1.Nlinea);
                    }//Fin del else               
                }else//Fin e inicio del else resultante de la validacion de las comillas 
                {//Else
                    setError(false);
                    JOptionPane.showMessageDialog(null, "ERROR ¡Hacen falta comillas! linea "+Practica1.Nlinea);
                }//Fin del else para mandar el mensaje de error de que hacen falta comillas
                break;            
            case "5":                                
                cifra = operacion.quitarCeros(operacion.idenBase(codigos[2]));                
                if(!"null".equals(cifra) && operacion.isError())
                {                                                                                       
                        if(cifra.length() > 4)
                        {//Inicio del if para validar el tamaño del valor de EQU
                            JOptionPane.showMessageDialog(null, "Error en EQU ¡Excedio el tamaño! en la linea "+Practica1.Nlinea);
                            setError(false);
                        }else if("null".equals(codigos[0]))
                        {//Else if para validar si la posicion de la etiqueta no sea nula
                            JOptionPane.showMessageDialog(null, "Error en EQU ¡Le falta la etiqueta! en la linea "+Practica1.Nlinea);
                            setError(false);
                        }else if(codigos[2].equals("null"))
                        {//Else if para validar que la posicion del operando no sea nulo
                            JOptionPane.showMessageDialog(null, "Error en EQU ¡Debe de llevar operando! en la linea "+Practica1.Nlinea);
                            setError(false);
                        } else
                        {//Else qeu indica que no hay errores y almacena en la posicion del operando el numeor hexadecimal equivalente si es qeu no esta en decimal                                          
                            codigos[2] = Practica1.completarHexadecimal(cifra);
                        }
                }//Fin del if para validar que el operando sea correcto
                else
                {//Inicio del else #2.3
                    setError(false);    //Aqui se le asigna este valor a la variable global para no guardar datos incorrectos
                }//Fin del else #2.3
                break;
            default:
                this.setError(false);
                JOptionPane.showMessageDialog(null, "ERROR En directiva linea: "+Practica1.Nlinea);
                break;
        }//fin del switch "switch(numero)"
    }//fin del método para clasificar las directivas
    /**
     * Método que nos ayuda a identificar que todos los caracteres de una cadena esten en el codigo ascii
     * @param cadena
     * @return 
     */
    protected boolean revisarCadenaAscii(String cadena){
        boolean retorno = false;
        for(int i = 0; i<cadena.length(); i++)
        {//Inicio del for para leer los caracteres de la cadena
            if(cadena.codePointAt(i) >= 0 && cadena.codePointAt(i) <= 255)
            {//If para revisar que cada caracter que se esta leyendo este dentro del ASCII 
                retorno = true;
            }else
            {
                retorno = false;
                break;
            }//Fin del if else
        }//fin del for para leer la cadena
        return retorno;
    }//Fiin del método revisarCadenaAscii
}//fin de la clase directivas
