/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.SSII2.entity;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 *
 * @author Flavio, Alvaro
 */
public class WorkersBank {

   
   ArrayList<String> accounts = new ArrayList<>();
   ArrayList<String> accountsPossitions = new ArrayList<>();
   ArrayList<String> newsAccounts = new ArrayList<>();
   ArrayList<String> arrayIban = new ArrayList<>();


    public ArrayList<String> getAccounts() {
        return accounts;
    }

    public ArrayList<String> getAccountsPossitions() {
        return accountsPossitions;
    }

    public ArrayList<String> getNewsAccounts() {
        return newsAccounts;
    }

    public ArrayList<String> getArrayIban() {
        return arrayIban;
    }

    public void addAccount(String a){

            //completar con ceros a la izquierda para las cuentas menores a 20
            int tam  = a.length();
            if(tam < 20){
                do{
                    a = "0" + a;
                    tam++;
                }while(tam<20);//mientras el tamano de la cuenta no sea 10 anade ceros
            }
        accounts.add(a);
    }
    
    public void addAccountPos(String a){
        accountsPossitions.add(a);
    }
    
    public void addNewAccounts(String a){
        newsAccounts.add(a);
    }
    
    public void addIban(String a){
        arrayIban.add(a);
    }
    
    public void print(){
        System.out.println(accounts);
        System.out.println(accountsPossitions);
        System.out.println(newsAccounts);
        System.out.println(arrayIban);
    }
    

    public void calculoDigitoControl(){
    
        //bucle que recorre el arraylist con las cuentas
        for(int j=0;j<accounts.size();j++){
           
            //primeros ocho numeros
            String primeraParte = accounts.get(j).substring(0,8);  
            primeraParte = "00" + primeraParte;
            int[] arrayUno = new int[10];

            //10 ultimos numeros
            String segundaParte = accounts.get(j).substring(10, 20);
            int[] arrayDos = new int[10];

            //numeros para multilicar 
            int[] numParaMultiplicar = new int[]{1,2,4,8,5,10,9,7,3,6};

            //multiplica cada numero de la cuenta por los numeros del array anterior
            //y lo guarda en el mismo array utilizado antes
            for(int i=0; i< arrayUno.length; i++){
              char c = primeraParte.charAt(i);
              arrayUno[i] = Integer.parseInt(String.valueOf(c)) * numParaMultiplicar[i];
            }

            for(int i=0; i< arrayDos.length; i++){
              char c = segundaParte.charAt(i);
              arrayDos[i] = Integer.parseInt(String.valueOf(c))  * numParaMultiplicar[i];
            }

            //sumatorio del contenido del array
            int sum1 = 0;
            int sum2 = 0;
            for(int i=0;i<10;i++){

               sum1 = sum1 + arrayUno[i];
               sum2 = sum2 + arrayDos[i];
            }

           //calcular el digito de control
           int codigoControlUno = codigoControl(sum1);
           int codigoControlDos = codigoControl(sum2);

          /* System.out.print(codigoControlUno);
           System.out.print(codigoControlDos);
           System.out.println(",");*/
          
          String dCP = String.valueOf(codigoControlUno);//digito de control primero
          String dCS = String.valueOf(codigoControlDos);//digito de control segundo
        
          String nuevaCuenta = primeraParte + dCP + dCS + segundaParte;
          
          nuevaCuenta = nuevaCuenta.substring(2);
          
          addNewAccounts(nuevaCuenta);
          
          calculoIban(nuevaCuenta);
          
        }
      
    }
    
    //devuelve el numero de control 
    public  int codigoControl(int s){
        
        int resto = s % 11;
        int digitoControl = 11 - resto;
        if(digitoControl==11)
            digitoControl = 0;
        if(digitoControl==10)
            digitoControl = 1;
        
        return digitoControl;
    }
    
    //calculo de IBAN 
    public void calculoIban(String cuenta){
    
        cuenta = cuenta + "142800";//para ES00
        
        BigInteger bd = new BigInteger(cuenta);
        
        BigInteger resto = bd.mod(new BigInteger("97"));
        
        int r = 98 - resto.intValue();
        
        String iban;
        if(r<10){
            iban = "ES0" + String.valueOf(r);
        }else{
         iban = "ES" + String.valueOf(r);
        }

        addIban(iban);
    
    }
    
  
}
