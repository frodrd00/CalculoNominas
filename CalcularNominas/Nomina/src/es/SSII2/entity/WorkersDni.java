/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.SSII2.entity;

import java.util.ArrayList;

/**
 *
 * @author Flavio, Alvaro
 */
public class WorkersDni {
    
    ArrayList<String> dnis = new ArrayList<>();
    ArrayList<String> dnisPos = new ArrayList<>();
    ArrayList<String> dnisCalculados = new ArrayList<>();

    //constructor
    public WorkersDni() {

    }
    
    //anade un dni original al array
    public void anadirDniArray(String dni){
        dnis.add(dni);
    }
    //anade la posicion del dni al array
    public void anadirPosArray(String pos){
        dnisPos.add(pos);
    }
    //coje el array de dnis originales
    public ArrayList<String> getDnis() {
        return dnis;
    }
    //coje el array con las posiciones
    public ArrayList<String> getDnisPos() {
        return dnisPos;
    }
    //coje el array con los dnis calculados
    public ArrayList<String> getDnisCalculados() {
        return dnisCalculados;
    }
    //metodo para imprimir los arrays
    public void imprimir(){
        System.out.println(dnis);//dnis originales excel
        System.out.println(dnisPos);//posicion en el excel de los dnis
        System.out.println(dnisCalculados);//dnis con el digito de control comprobado y cambiado
    }
    //metodo para calcular al letra del dni
    public void calculatorLetterDni(){
        //lista con las letras para el digito de control
        String juegoCaracteres="TRWAGMYFPDXBNJZSQVHLCKE";    
        int modulo;
        char letra;
        int IntDni;
        boolean nie;
             
            for (String dni : dnis) {
               //si ha pillado un nie hay que ponerlo a false para comprobar el siguiente dni
                nie = false;
                //si se trada de un nie
                if(dni.toUpperCase().startsWith("X")){//si empieza por X
                    dni = "0"+dni.substring(1);//se substrae la letra y se le añade el 0
                    nie = true;
                }else if(dni.toUpperCase().startsWith("Y")){
                    dni = "1"+dni.substring(1);
                    nie = true;
                }else if(dni.toUpperCase().startsWith("Z")){
                    dni = "2"+dni.substring(1);
                    nie = true;
                }

                //calcular la letra
                dni = dni.substring(0, 8); //se sustrae el codigo de control
                IntDni = Integer.parseInt(dni);//se pasa el dni a entero
                modulo = IntDni % 23; //se coje el resto de 23
                letra = juegoCaracteres.charAt(modulo);//devulve la letra del string el la posicion del modulo
               
                //si se trada de un nie 
                //volver a cambiar el numero por su letra
                if(nie){ 
                    if(dni.toUpperCase().startsWith("0")){
                        dni = "X"+dni.substring(1);//se substrae el numero y se anade la letra;
                    }else if(dni.toUpperCase().startsWith("1")){
                        dni = "Y"+dni.substring(1);
                    }else if(dni.toUpperCase().startsWith("2")){
                        dni = "Z"+dni.substring(1);
                    }
                }
                
                //meterlo al nuevo array de dni
                dnisCalculados.add(dni+letra);//dni + el codigo de control calculado

        }

    }

}
