/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.SSII2.entity;

import java.util.ArrayList;
import java.util.Collections;
/**
 *
 * @author Alvaro, Flavio
 */
public class WorkersEmail {
    
    ArrayList<WorkersDatosNomina> arrayWorkers;
    ArrayList<String> arrayCorreos = new ArrayList<>();
    
    
    public WorkersEmail(ArrayList<WorkersDatosNomina> trabajador) {
        this.arrayWorkers = trabajador;
    }
    
    public void creacionCorreos(){
        
        for(int i=0; i<arrayWorkers.size();i++){
            
            //se cojen los datos alamacenados en el arraylist de tipo WorkersDatos
            String nombre = arrayWorkers.get(i).getNombre();
            String apellido1 = arrayWorkers.get(i).getApellido1();
            String apellido2 = arrayWorkers.get(i).getApellido2();
            String empresa = arrayWorkers.get(i).getNombreEmpresa();
            
            //se quitan los acentos y se pone a minusculas las 2 primeras letras del nombre,apellido1 y apellido2
            String iniNombre= quitarAcentos(nombre.substring(0,2)).toLowerCase();
            String iniApe1= quitarAcentos(apellido1.substring(0,2)).toLowerCase();
            String iniApe2= quitarAcentos(apellido2.substring(0,2)).toLowerCase();
            
            //se quitan espacion y ountos del nombre de la empresa
            String empresaN = empresa.replace(" ", "");
            empresaN = empresaN.replace(".", "").toLowerCase();
            
            //se guarda el nombre del correo ej:"frodrd"
            String nombreCorreo = iniNombre + iniApe1 + iniApe2;
            
            //busca cuantos nombres de correos ya hay iguales en el array al creado
            //cuando no encuentra ninguno devuelve 0 y el nombre del correo sera "frodrd00"
            //si ya hay uno dentro que sea igual al que se crea, develve 1, y el siguiente sera "frodrd01"
            int numCasos = Collections.frequency(arrayCorreos, nombreCorreo);
            
            //se alamacena el nombre en el array
            arrayCorreos.add(nombreCorreo);
            
            String num="";
            
            //se añade un 0 al string delante del num de nombres encontrados 
            if(numCasos<9)
                num = "0" + Integer.toString(numCasos);
            
            //se da formato al correo
            String correo = iniNombre+iniApe1 + iniApe2 + num + "@" + empresaN + ".es";
          
            //guarda el correo en el objeto trabajador
            arrayWorkers.get(i).setEmail(correo);
             
           /* System.out.println(arrayWorkers.get(i).getNombre() 
                    + " " + arrayWorkers.get(i).getApellido1()
                    + " " + arrayWorkers.get(i).getApellido2()
                    + ": " + arrayWorkers.get(i).getEmail());*/
              
        }
        
       
    }
    public String quitarAcentos(String aaa){
            // Cadena de caracteres original a sustituir.
        String original = "áàäéèëíìïóòöúùuÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
        // Cadena de caracteres ASCII que reemplazarán los originales.
        String ascii = "aaaeeeiiiooouuuAAAEEEIIIOOOUUUNcC";
        String output = aaa;
        for (int i=0; i<original.length(); i++) {
            // Reemplazamos los caracteres especiales.
            output = output.replace(original.charAt(i), ascii.charAt(i));
        }//for i
        return output;
    }
}
