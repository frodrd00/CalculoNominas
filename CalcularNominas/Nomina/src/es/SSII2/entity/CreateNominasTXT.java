/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.SSII2.entity;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Windows
 */
public class CreateNominasTXT {

    String mesAnho;
    ArrayList<WorkersDatosNomina> datos;
    String rutaTxt;

    public String getRutaTxt() {
        return rutaTxt;
    }

    public void setRutaTxt(String rutaTxt) {
        this.rutaTxt = rutaTxt;
    }
    
    public CreateNominasTXT(String mA,ArrayList<WorkersDatosNomina> d){
        this.mesAnho = mA;
        this.datos = d;
    }
    
    public String crearTXT(){
    
        //coje el mes y el anho de la entrada estandar
        String a[] = mesAnho.split("/");
        String mes = a[0];
        int mesInt = Integer.parseInt(mes);
        String anho = a[1];
        String mesCadena;
        
        //cambia el anho en numero por su nombre
        switch (mesInt) {
            case 1:  mesCadena = "enero"; break;
            case 2:  mesCadena = "febrero"; break;
            case 3:  mesCadena = "marzo"; break;
            case 4:  mesCadena = "abril"; break;
            case 5:  mesCadena = "mayo"; break;
            case 6:  mesCadena = "junio"; break;
            case 7:  mesCadena = "julio"; break;
            case 8:  mesCadena = "agosto"; break;
            case 9:  mesCadena = "septiembre"; break;
            case 10: mesCadena = "otubre"; break;
            case 11: mesCadena = "noviembre"; break;
            case 12: mesCadena = "dicimebre"; break;
            default: mesCadena = "MES"; break;
        }
        
        rutaTxt = "src/es/SSII2/nominasTXT/"+mesCadena+anho+".txt";
        
        setRutaTxt(rutaTxt);
        
        //crea el archivo txt para guardar los datos de los trabajadores
        try (PrintWriter writer = new PrintWriter(rutaTxt, "UTF-8")) {
            
            for (int i = 0; i < datos.size(); i++) {
                //formato de fecha dd/MM/yyy
                String fecha = new SimpleDateFormat("dd/MM/yyyy").format(datos.get(i).getFechaAlta());
                //meses de antiguedad
                String meses=String.valueOf(datos.get(i).getMesesAntiguedad());
               
                DecimalFormat df = new DecimalFormat("0.00");
                
                //escribe una linea en el txt por trabajador
                writer.println(datos.get(i).getNombreEmpresa()+";"+
                        datos.get(i).getCifEmpresa()+";"+
                        datos.get(i).getNombre()+";"+
                        datos.get(i).getApellido1()+";"+
                        datos.get(i).getApellido2()+";"+
                        datos.get(i).getNif()+";"+
                        fecha+";"+
                        meses+";"+
                        datos.get(i).getCategoriaPuestoTrabajo()+";"+
                        df.format(datos.get(i).getIRPFPorcentaje())+";"+
                        datos.get(i).getNumContrato()+";"+
                        df.format(datos.get(i).getSalarioBase()/14)+";"+
                        df.format(datos.get(i).getComplemento()/14)+";"+
                        df.format(datos.get(i).getAntiguedad())+";"+
                        df.format(datos.get(i).getImporteCalculoContingencias())+";"+
                        df.format(datos.get(i).getIRPFPorcentaje())+";"+
                        df.format(datos.get(i).getCuotaObreraGeneral())+";"+
                        df.format(datos.get(i).getCuotaDesempleo())+";"+
                        df.format(datos.get(i).getCuotaFormacion())+";"+
                        df.format(datos.get(i).getParteProporcionalExtra())+";"+
                        df.format(datos.get(i).getImporteIRPF())+";"+
                        df.format(datos.get(i).getBrutoTotalNomina())+";"+
                        df.format(datos.get(i).getLiquidoAPercibirNomina())+";"+
                        datos.get(i).getMesNomina()+";"+
                        datos.get(i).getAnoNomina()+";"+
                        datos.get(i).getEmail()+";"+
                        datos.get(i).getIBAN()+";"+
                        df.format(datos.get(i).getBaseCalculoContingenciasEmpresario())+";"+
                        df.format(datos.get(i).getImporteContingenciasComunesEmpresario())+";"+
                        df.format(datos.get(i).getImporteFogasa())+";"+
                        df.format(datos.get(i).getDesempleoEmpresario())+";"+
                        df.format(datos.get(i).getFormacionEmpresa())+";"+
                        df.format(datos.get(i).getAccidentesTrabajoEnfermedadProfesional())+";"+
                        datos.get(i).getEsExtra()
                );
                
                
                
             }  
            
           // System.out.println("El txt con las nominas del " + mesAnho + " ha sido creado con exito.");
            
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(CreateNominasTXT.class.getName()).log(Level.SEVERE, null, ex);
        }
     
    return mesCadena+anho;    
        
    }
    
    
}
