/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica1;

import java.util.regex.Pattern;

/**
 *
 * @author 213709513
 */
public class ModoDireccionamiento {
    
    //DECLARACION DE ATRIBUTOS 
    private String tamaño;
    private String modo;
    private boolean error;
    //FIN DE LA DECLARACION DE ATRIBUTOS
    
    public ModoDireccionamiento(){//Constructor de la clase
        tamaño = "";
        modo = "";
        error = true;
    }//Fin del constructor de la clase
//----------------------------------------------Métodos para la obtencion de los atributos------------------------------------------
    /**
     * Método para obtener el valor de tamaño
     * @return 
     */
    public String getTamaño() {
        return tamaño;
    }//Fin del método get para tamaño
    /**
     * Método para asignar un valor a tamaño
     * @param tamaño 
     */
    public void setTamaño(String tamaño) {
        this.tamaño = tamaño;
    }//Fin del método set para tamaño
    /**
     * Método para obtener el valor de modo
     * @return 
     */
    public String getModo() {
        return modo;
    }//Fin del método get para modo
    /**
     * Método para asignar un valor a modo
     * @param modo 
     */
    public void setModo(String modo) {
        this.modo = modo;
    }//Fin del  método set para modo
    /**
     * Método para obtener el valor de error
     * @return 
     */
    public boolean isError() {
        return error;
    }//fin del método get de error
    /**
     * Método para asignar un valor booleano a error
     * @param error 
     */
    public void setError(boolean error) {
        this.error = error;
    }//fin del método set del atributo error
//---------------------------------------------Fin de los métodos para la obtencion de los atributos--------------------------------
                
    /**
     * Método para identificar modo de direccionamiento
     * @param codigos
     * @return 
     */
    protected String detectarDireccionamiento(String[] codigos){
        String idenDireccionamiento = "";
            if(codigos[2].substring(0, 1).equals("#")){//Aqui sabremos si el operando es de un inmediato
                idenDireccionamiento = "#";
            }else if(Pattern.compile("[,]").matcher(codigos[2]).find() || registrosComputadora(codigos[2])==true){//Aqui sabremos si el codigo es indexado
                idenDireccionamiento = "IND";
            }else if(dirExt(codigos[2])){//Con esta condicion sabemos si sera directo o extendido
                idenDireccionamiento = "DEX";
            }else if(isEtiqueta(codigos[2])){//Uso de expresion regular para saber si sera relativo                
                idenDireccionamiento = "REL";
            }else{              //Aqui sino se cumple unas de las condiciones anteriores manda error en operando                
               idenDireccionamiento = "ERROR";
            }//Fin de las condiciones para saber en que direccionamiento entraran                        
        return idenDireccionamiento;        //Retorno de la variable con su respectivo modo
    }//Fin del método para identificar el modo de direccionamiento
    /**
     * Método para validar etiqueta del modo de direccionamiento relativo
     * @param etiqueta
     * @return 
     */
    protected boolean isEtiqueta(String etiqueta){
        boolean bandera = true;//Variable para comprobar si se cumple la condicion de que si es una cadena
        int idenasci = etiqueta.codePointAt(0);
        if((idenasci >= 65 && idenasci <= 90) || (idenasci >= 97 && idenasci <= 122) && etiqueta.length() <= 8)
        {//Inicio de if para comprobar el caracter inicial y la longitud de la cadena
            for(int i  = 0; i<etiqueta.length(); i++)
            {//Inicio del for para leer caracter por caracter y validar
                if((etiqueta.codePointAt(i) < 65 || etiqueta.codePointAt(i) > 90) && (etiqueta.codePointAt(i) < 97 || etiqueta.codePointAt(i) > 122) && etiqueta.codePointAt(i)!=95 && (etiqueta.codePointAt(i)<48 || etiqueta.codePointAt(i)>57))
                {
                    bandera = false;
                    break;
                }//fin del if para detectar caracteres invalidos
            }
        }//Fin del if para comprobar el caracter incial
        return bandera;
    }//Fin del método para detectar las etiquetas 
    /**
     * Método que nos ayuda a identificar si el operando es directo o extendido
     * @param simbolo
     * @return 
     */    
    protected boolean dirExt(String simbolo){
        boolean bandera = false;
        if(simbolo.substring(0, 1).equals("%") || simbolo.substring(0, 1).equals("@") || simbolo.substring(0, 1).equals("$") || (simbolo.codePointAt(0) >= 48 && simbolo.codePointAt(0) <= 57))
        {
            bandera = true;
        }
        return bandera;
    }//Fin del método para identificar si es directo
    /**
     * Método que nos ayuda a convertir un numero decimal a hexadecimal
     * @param x
     * @return 
     */        
    protected String converHexa(int x){
        //converHexa(Integer.parseInt("4233", 8));
        return Integer.toHexString(x);
    }//Fin del método para convertir de entero a hexadecimal
    /**
     * Método para controlar hacia donde se ira el programa dependiendo su modo de direccionamiento
     * @param abreviatura 
     * @param codigos 
     */
    protected void opcionesModos(String abreviatura, String[] codigos){        
        switch(abreviatura){        //Recibe como variable el modo que identifico
            case "#":
                //Método para verificar los inmediatos
                if(!"null".equals(idenBase(codigos[2].substring(1))))
                {
                    setModo("IMM");                      
                }//Fin del if
                break;
            case "IND":
                //Método para identificar los indexados
                modosIndexados(codigos[2]);
                break;
            case "DEX":
                //Método para verificar si sera directo o extendido
                if(!"null".equals(idenBase(codigos[2])))
                {
                    if(getTamaño().equals("8"))
                    {
                        setModo("DIR");
                    }//Fin del if para saber si el tamaño de instruccion es de 8
                    else if(getTamaño().equals("16"))
                    {
                        setModo("EXT");
                    }//Fin del if para saber si el tamaño de instruccion es de 16
                }//Fin del if para identificar errores en las bases
                break;
            case "REL":
                //Método para identificar relativos
                setModo("REL");
                break;
            case "ERROR":
                setError(false);//Asignacion de flase a la variable error para identificar que hubo un error
                System.out.println("Hubo un error en el codigo de operacion no se pudo identificar el modo de direccionamiento\n");
                break;
        }//Fin del switch                
    }//Fin del método para separar modos de direccionamiento
    /**
     * 
     * @param identificador
     * @return 
     */
    protected String idenBase(String identificador){
        String base = "null";       
        switch(identificador.substring(0, 1)){      //Inicio del switch
            case "@":
                if(baseOctal(identificador.substring(1)))
                {
                    base = converHexa(Integer.parseInt(identificador.substring(1),8));
                    nBits(base);
                }//fin del if
                else
                {
                    setError(false);
                    System.out.println("Error el numero representado en octal no lo es\n");
                }//fin del else
                break;
            case "$":
                if(baseHexa(identificador.substring(1)))
                {
                    base = identificador.substring(1);
                    nBits(base);
                }//fin del if
                else
                {
                    setError(error);
                    System.out.println("Error el numero representado en hexadecimal no lo es\n");
                }//Fin del else
                break;
            case "%":
                if(baseBinaria(identificador.substring(1)))
                {
                    base = converHexa(Integer.parseInt(identificador.substring(1),2));
                    nBits(base);                    
                }//fin del if 
                else
                {
                    setError(error);
                    System.out.println("Error el numero representado como binario no lo es\n");
                }//Fin del else
                break;
            default:
                if(baseDecimal(identificador.substring(0)))
                {
                    setError(error);
                    base = converHexa(Integer.parseInt(identificador.substring(0),10));
                    nBits(base);
                }//fin del if
                else
                {
                    setError(error);
                    System.out.println("Error el numero representado como decimal no lo es\n");
                }//fin del else
                break;
        }//Fin del switch           
        return base;        
    }//fin del método para identificar en que base se encuentra el operando
    /**
     * 
     * @param octal
     * @return 
     */
     protected static boolean baseOctal(String octal){
        return Pattern.compile("[0-7]+").matcher(octal).matches();
    }//Fin del método base octal
     /**
      * 
      * @param binario
      * @return 
      */
    protected static boolean baseBinaria(String binario){
        return Pattern.compile("[0-1]+").matcher(binario).matches();
    }//Fin del método base binaria
    /**
     * 
     * @param decimal
     * @return 
     */
    protected static boolean baseDecimal(String decimal){               
        return Pattern.compile("[0-9[-]]+").matcher(decimal).matches();
    }//Fin del método base decimal
    /**
     * 
     * @param hexa
     * @return 
     */
    protected static boolean baseHexa(String hexa){
        return Pattern.compile("[0-9A-F]+").matcher(hexa).matches();
    }//Fin del método base hexadecimal
    /**
     * 
     * @param cifra 
     */
    protected void nBits(String cifra){
           String rellenar = "0";           
           for(int i = 0; i<cifra.length(); i++)
           {//  Inicio del for para ignorar los ceros a la izquierda que se puedan tener #1
               if(!"0".equals(cifra.substring(i, i+1)))
               {//Inicio del if para verificar cuando comienza a haber numeros diferentes a 0 #1.1
                   cifra = cifra.substring(i);
                   break;
               }//Fin del if #1.1
           }//fin del for #1
           int longitud = cifra.length();                     
           switch(longitud)
           {
               case 1:
                   cifra = rellenar + cifra;
                   setTamaño("8");
                   break;
               case 2:
                   setTamaño("8");
                   break;
               case 3:
                   cifra = rellenar + cifra;
                   setTamaño("16");
                   break;
               case 4:
                   setTamaño("16");
                   break;
               default:
                   setError(false);
                   System.out.println("Error se excedio el tamaño con el que se puede trabajar\n");
                   setTamaño("EXCEDIDO");
                   break;
           }//Fin del switch          
      }//Fin del método

//-----------------------------------------Modos de direccionamiento indexado------------------------------------------------
    /**
     * Método que nos ayuda a comprobar si la cadena de entrada corresponde a un registro de computadora
     * @param registro
     * @return 
     */
    protected boolean registrosComputadora(String registro){ 
        boolean bandera=false;
        registro = registro.toLowerCase();
        if(registro.equals("x") || registro.equals("y") || registro.equals("sp") || registro.equals("pc"))
        {//Fin del if para identificar los registros de computadora 
            bandera = true;
        }//Fin del if para validar los registros de computadora
        return bandera;
    }//fin del método para identificar los registros
    /**
     * Método que nos ayuda a separar las palabras de una cadena correspondiente a un direccionamiento indexado
     * @param operando
     * @param inicial
     * @param valor
     * @return 
     */
    protected String separarCadena(String operando,String inicial,int valor){
        String retorno="";
        int fori=operando.indexOf(",");                
        if(inicial.equals(""))
        {
            if(valor>0)
            {
                retorno = operando.substring(fori+1);
            }
            else
            {               
                retorno = operando.substring(0,fori);                
            }
        }
        else
        {
            if(valor>0)
            {
                retorno = operando.substring(fori+1,operando.length()-1);
            }
            else
            {
                retorno = operando.substring(1,fori);
            }
        }                       
        return retorno;//Retorna la cadena ya separada como se requiere respectivamente
    }//Fin del método
    /**
     * 
     * @param operando 
     */
    protected void modosIndexados(String operando){
        String separador;
        int conversion;
        if(operando.substring(0,1).equals(",") || registrosComputadora(operando)==true)
        {//Incio del if para validar la peculiaridad del indexado de 5 bits
            if(registrosComputadora(operando.substring(1))==true && operando.substring(0,1).equals(","))
            {
                setModo("IDX");
                setTamaño("5");                
            }//fin del if para validar los tipos de registros
            else if(registrosComputadora(operando)==true) 
            {
                setModo("IDX");
                setTamaño("5");
            }
        }//Fin del if para validar el indexado de 5 bits                        
        else
        {//Inicio del else 1°     
            separador = separarCadena(operando, "", 0);            
            if(baseDecimal(separador))
            {                
                conversion = Integer.parseInt(separador);
                if(conversion >= -16 && conversion <= 15 && registrosComputadora(separarCadena(operando, "", 1)))
                {
                    setModo("IDX");
                    setTamaño("5");
                }//Fin del if para el modo de direccionamiento indexado de 5 bits
                else if(conversion < -16 && conversion >= -256 || conversion > 15 && conversion <= 255 && registrosComputadora(separarCadena(operando, "", 1)))
                {
                    setModo("IDX1");
                    setTamaño("9");
                }
                else if(conversion > 255 && conversion <= 65535 && registrosComputadora(separarCadena(operando, "", 1)))
                {
                    setModo("IDX2");
                    setTamaño("16");
                }
                else if(conversion >= 1 && conversion <=8 && indizadoAuto(separarCadena(operando, "", 1)))
                {                                  
                    System.out.println("Correcto");
                }                
                else
                {
                    setError(false);
                    System.out.println("ERROR en el operando\n");
                }
            }//Fin del if que revisa si la primer cantidad de los numeros indexados son decimales
            else if(operando.substring(0, 1).equals("[") && operando.substring(operando.length()-1).equals("]"))
            {
                if(indeIndirectos(operando).equals("[IDX2]"))
                {
                    setModo("[IDX2]");
                }
                else if(indeIndirectos(operando).equals("[D,IDX]"))
                {
                    setModo("[D,IDX]");
                }
                else
                {
                    setError(false);
                    System.out.println("ERROR en el operando\n");
                }
            }
            else if(operando.substring(0, 1).toLowerCase().equals("a") || operando.substring(0, 1).toLowerCase().equals("d") || operando.substring(0, 1).toLowerCase().equals("b"))
            {
                if(registrosComputadora(separarCadena(operando, "", 1)))
                {
                    setModo("IDXABD");
                }
                else
                {
                    setError(false);
                    System.out.println("ERROR operando incorrecto\n");
                }
            }
            else 
            {
                setError(false);
                System.out.println("ERROR en el operando\n");
            }
        }//fin del else 1° para las instrucciones para identificar los modos indexados
    }//fin del método 
    /**
     * Método que nos ayuda a detectar los modos de direccionamiento indirectos 
     * @param operando
     * @return 
     */
    protected String indeIndirectos(String operando){
        String separador = separarCadena(operando, "[", 0);
        String retorno = "null";        
        if(baseDecimal(separador))
        {
            if(Integer.parseInt(separador) >= 0 && Integer.parseInt(separador) <= 65535 && registrosComputadora(separarCadena(operando, "[", 1)))
            {//Inicio del if para validar los modos indizados tipo indirectos
                retorno = "[IDX2]";
            }//Fin del if para la validacion de los indirectos         
        }
        else if(separador.toLowerCase().equals("d") && registrosComputadora(separarCadena(operando, "[", 1)))
        {
            retorno = "[D,IDX]";
        }
        return retorno;
    }//Fin del  método
    /**
     * Método para identificar los direccionamientos indiexados de incremento o decremento
     * @param registro
     * @return 
     */
    protected boolean indizadoAuto(String registro){
        String validarRegistro;  //Variable para almacenar solo las letras que conforman el registro      
        boolean retorno = false;//Variable booleana que se retornara
        if(registro.substring(0, 1).equals("-") || registro.subSequence(0, 1).equals("+"))
        {//If que identifica los signos - y + al iniciar el registro #1
            validarRegistro = registro.substring(1);
            if(registrosComputadora(validarRegistro))
            {//Fin del if para validar que si haya un correcto nombre de registro #1.1
                retorno = true;
                setModo(registro.substring(0,1)+"IDX");
            }//Fin del if #1.1
        }//Fin del if #1
        else if(registro.substring(registro.length()-1).equals("-") || registro.substring(registro.length()-1).equals("+"))
        {//else if que identifica que el registro termina con - o + #2
            validarRegistro = registro.substring(0, registro.length()-1);
            if(registrosComputadora(validarRegistro))
            {//if para validar que sea un correcto registro de computadora #2.1
                retorno = true;
                setModo("IDX"+registro.substring((registro.length()-1)));
            }//Fin del if #2.1
        }//Fin del if #2
        return retorno;
    }//Fin del método de los operandos de pre/post incremento/decremento
}//Fin de la clase para los detectar los modos de direccionamiento
