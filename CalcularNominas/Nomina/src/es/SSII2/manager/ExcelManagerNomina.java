/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.SSII2.manager;
import es.SSII2.entity.WorkersDatosNomina;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Windows
 */
public class ExcelManagerNomina {

    String excel, nominaAPedir;
    float cuotaObreraGeneralTrabajador, cuotaDesempleoTrabajador, cuotaFormacionTrabajador,
          contingenciasComunesEmpresario,fogasaEmpresario,desempleoEmpresario,formacionEmpresario,
          accidentesTrabajoEmpresario;
    WorkersDatosNomina datos;
    ArrayList<WorkersDatosNomina> arrayWorkers = new ArrayList<>();//se alamacenan los trabajadores
    ArrayList<String> categorias = new ArrayList<>();
    ArrayList<Integer> salarioBase = new ArrayList<>();
    ArrayList<Integer> complementos = new ArrayList<>();
    ArrayList<Integer> trienio = new ArrayList<>();
    ArrayList<Integer> brutoArray = new ArrayList<>();
    ArrayList<Float> retencion = new ArrayList<>();
    
    
    public ExcelManagerNomina(String e, String nominaAPedir) {
        this.excel = e;
        this.nominaAPedir = nominaAPedir;
    }
    
    public void inicializarCalculosNomina() throws IOException{
      readExcel();
      read2Excel();
      calcularSalarioBase();
      calculoAntiguedadYBrutoAnual();
      calcularContingencias();
      prorrata();
      calculoIRPF();
      calculoLiquidoPercibir();
      calculosEmpresarios();
      reducionesBaja();
    }
    
    //se leen los datos necesarios de la pagina 1 del excel
     public void readExcel() throws FileNotFoundException, IOException{
    
        FileInputStream file;
        file = new FileInputStream(new File(excel));

        try ( 
            XSSFWorkbook workbook = new XSSFWorkbook(file)) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            Row row;
            // Recorremos todas las filas para mostrar el contenido de cada celda
                while (rowIterator.hasNext()) {
                    
                    datos = new WorkersDatosNomina();

                    row = rowIterator.next();

                    // Obtenemos el iterator que permite recorres todas las celdas de una fila
                    Iterator<Cell> cellIterator = row.cellIterator();
                    Cell celda;

                    if(row.getRowNum() >0 ){

                        String celdas;
                        Date celdaDate;
                        double cuenta;
                        DecimalFormat df = new DecimalFormat("#");

                        while (cellIterator.hasNext()){

                            celda = cellIterator.next();

                           //numero nomina
                           datos.setNumContrato(row.getRowNum());
                           //nombre
                           if(celda.getColumnIndex() == 0 && celda.getCellType() != 3){
                               celdas = celda.getStringCellValue();
                               datos.setNombre(celdas);//se coje el nombre del trabajador y se guarda en la clase WorkersDatos
                           }
                           //apellido
                           if(celda.getColumnIndex() == 1 && celda.getCellType() != 3){
                             celdas = celda.getStringCellValue();
                             datos.setApellido1(celdas);//se coje el apellido1 del trabajador y se guarda en la clase WorkersDatos
                           }
                           //apellido2
                           if(celda.getColumnIndex() == 2 && celda.getCellType() != 3){
                              celdas = celda.getStringCellValue();
                             datos.setApellido2(celdas); //se coje el apellido2 del trabajador y se guarda en la clase WorkersDatos
                           }
                           //nif
                           if(celda.getColumnIndex() == 3 && celda.getCellType() != 3){
                             celdas = celda.getStringCellValue();
                             datos.setNif(celdas);
                           }
                           //nombre empresa
                           if(celda.getColumnIndex() == 6 && celda.getCellType() != 3){
                             celdas = celda.getStringCellValue();
                             datos.setNombreEmpresa(celdas);
                            }
                           //cif empresa
                           if(celda.getColumnIndex() == 7 && celda.getCellType() != 3){
                             celdas = celda.getStringCellValue();
                             datos.setCifEmpresa(celdas);
                            }
                           //fecha alta empresa
                           if(celda.getColumnIndex() == 4 && celda.getCellType() != 3){
                             celdaDate = celda.getDateCellValue();
                             datos.setFechaAlta(celdaDate);
                            }
                           //categoria
                           if(celda.getColumnIndex() == 5 && celda.getCellType() != 3){
                             celdas = celda.getStringCellValue();
                             datos.setCategoriaPuestoTrabajo(celdas);
                            }
                           //cuenta
                           if(celda.getColumnIndex() == 8 && celda.getCellType() != 3){
                               cuenta = celda.getNumericCellValue();
                               String stringPOI = NumberToTextConverter.toText(cuenta);
                               datos.setIBAN(stringPOI);
                            }
                           //Iban
                           if(celda.getColumnIndex() == 9 && celda.getCellType() != 3){
                             celdas = celda.getStringCellValue();
                             datos.setIBAN(celdas+datos.getIBAN());
                            }
                           //prorrata
                           if(celda.getColumnIndex() == 10 && celda.getCellType() != 3){
                             celdas = celda.getStringCellValue();
                             datos.setProrrata(celdas);
                            }
                           //email
                            if(celda.getColumnIndex() == 15 && celda.getCellType() != 3){
                             celdas = celda.getStringCellValue();
                             datos.setEmail(celdas);
                            }
                            //fehaBajaLaboral
                            if(celda.getColumnIndex() == 11 && celda.getCellType() != 3){
                                celdaDate =  celda.getDateCellValue();
                                datos.setFechaBajaLaboral(celdaDate);
                            }
                            //fechaAltaLaboral
                            if(celda.getColumnIndex() == 12 && celda.getCellType() != 3){
                               celdaDate =  celda.getDateCellValue();
                               datos.setFechaAltaLaboral(celdaDate);
                            }
                            

                        }

                        calcularMesesAntiguedad();
                        
                        if(datos.getMesesAntiguedad()>0){
                            //se mete los datos de cada trabajador en el arrayWorkers
                            datos.setEsExtra("NoEsExtra");
                            arrayWorkers.add(datos);
                           
                            //Crear nuevo objeto nomina para las dos extras
                             if((datos.getMesNomina() == 6
                                || datos.getMesNomina() == 12)
                                && datos.getProrrata().equals("NO")){

                                 WorkersDatosNomina aux = new WorkersDatosNomina();
                                 aux.setEsExtra("SiEsExtra");
                                 aux.setNumContrato(datos.getNumContrato());
                                 aux.setNombre(datos.getNombre());
                                 aux.setApellido1(datos.getApellido1());
                                 aux.setApellido2(datos.getApellido2());
                                 aux.setNif(datos.getNif());
                                 aux.setNombreEmpresa(datos.getNombreEmpresa());
                                 aux.setCifEmpresa(datos.getCifEmpresa());
                                 aux.setCategoriaPuestoTrabajo(datos.getCategoriaPuestoTrabajo());
                                 aux.setEmail(datos.getEmail());
                                 aux.setIBAN(datos.getIBAN());
                                 aux.setMesesAntiguedad(datos.getMesesAntiguedad());
                                 aux.setProrrata(datos.getProrrata());
                                 aux.setFechaAlta(datos.getFechaAlta());
                                 aux.setMesNomina(datos.getMesNomina());
                                 aux.setAnoNomina(datos.getAnoNomina());

                                 //nomina extras
                                 arrayWorkers.add(aux);
                                 
                             }
                             
                        }
                    }

                }
 
            }
        
        // System.out.println(arrayWorkers.get(0).getMesesAntiguedad());
        
    }//fin readExcel()
     
    
    //se leen los datos necesarios de la pagina 2 del excel
     public void read2Excel() throws FileNotFoundException, IOException{
    
        FileInputStream file;
        file = new FileInputStream(new File(excel));

        try ( 
            XSSFWorkbook workbook = new XSSFWorkbook(file)) {
                XSSFSheet sheet = workbook.getSheetAt(1);//hoja 2
                Iterator<Row> rowIterator = sheet.iterator();
                Row row;
                // Recorremos todas las filas para mostrar el contenido de cada celda
                while (rowIterator.hasNext()) {

                    row = rowIterator.next();

                    // Obtenemos el iterator que permite recorres todas las celdas de una fila
                    Iterator<Cell> cellIterator = row.cellIterator();
                    Cell celda;

                    if(row.getRowNum() >0 ){
                        
                        String celdas;
                        int celdaNum;
                        float celdaFloat;

                        while (cellIterator.hasNext()){

                            celda = cellIterator.next();
                
                            //se guarda la categoria
                            if(celda.getColumnIndex() == 0 && row.getRowNum() < 15 && celda.getCellType() != 3){
                               celdas = celda.getStringCellValue();
                               categorias.add(celdas);
                            }
                            //se guarda los salarios base
                            if(celda.getColumnIndex() == 1 && celda.getCellType() != 3){
                               celdaNum = (int) celda.getNumericCellValue();
                               salarioBase.add(celdaNum);
                            }
                            //se guarda los complementos
                            if(celda.getColumnIndex() == 2 && celda.getCellType() != 3){
                               celdaNum = (int) celda.getNumericCellValue();
                               complementos.add(celdaNum);
                            }
                            
                            if(row.getRowNum() > 17 ){  
                                //trienio
                                 if(celda.getColumnIndex() == 4 && celda.getCellType() != 3){
                                   celdaNum = (int) celda.getNumericCellValue();
                                   trienio.add(celdaNum);
                                }
                             }
                            
                            if(row.getRowNum() == 17){  
                                //Cuota obrera general trabajador
                                if(celda.getColumnIndex() == 1 && celda.getCellType() != 3){       
                                    cuotaObreraGeneralTrabajador = (float) celda.getNumericCellValue();
                                }
                            }
                            
                            if(row.getRowNum() == 18){  
                                //Cuota desempleo trabajador
                                if(celda.getColumnIndex() == 1 && celda.getCellType() != 3){       
                                    cuotaDesempleoTrabajador = (float) celda.getNumericCellValue();
                                }
                            }
                             
                            if(row.getRowNum() == 19){  
                                //Cuota formación trabajador
                                if(celda.getColumnIndex() == 1 && celda.getCellType() != 3){       
                                    cuotaFormacionTrabajador = (float) celda.getNumericCellValue();
                                }
                            }
                            if(row.getRowNum() == 20){  
                                //contingencias Comunes Empresario
                                if(celda.getColumnIndex() == 1 && celda.getCellType() != 3){       
                                    contingenciasComunesEmpresario = (float) celda.getNumericCellValue();
                                }
                            }
                            if(row.getRowNum() == 21){  
                                //fogasa empresario
                                if(celda.getColumnIndex() == 1 && celda.getCellType() != 3){       
                                    fogasaEmpresario = (float) celda.getNumericCellValue();
                                }
                            }
                            if(row.getRowNum() == 22){  
                                //desempleo empresario
                                if(celda.getColumnIndex() == 1 && celda.getCellType() != 3){       
                                    desempleoEmpresario = (float) celda.getNumericCellValue();
                                }
                            }
                            if(row.getRowNum() == 23){  
                                //formacion empresario
                                if(celda.getColumnIndex() == 1 && celda.getCellType() != 3){       
                                    formacionEmpresario = (float) celda.getNumericCellValue();
                                }
                            }
                            if(row.getRowNum() == 24){  
                                //accidentes trabajo empresario
                                if(celda.getColumnIndex() == 1 && celda.getCellType() != 3){       
                                    accidentesTrabajoEmpresario = (float) celda.getNumericCellValue();
                                }
                            }
                           
                            //bruto anual
                            if(celda.getColumnIndex() == 5 && celda.getCellType() != 3){
                               celdaNum = (int) celda.getNumericCellValue();
                               brutoArray.add(celdaNum);
                            }
                             
                            //retencion
                            if(celda.getColumnIndex() == 6 && celda.getCellType() != 3){
                               celdaFloat = (float) celda.getNumericCellValue();
                               retencion.add(celdaFloat);
                            }
                             
                        }  
                    }
                }
            }
     }
     
     
       //calcula los meses de la fecha de alta hasta la fecha que le pasas por consola
    public void calcularMesesAntiguedad(){
    
        // Fecha inicio
        Calendar calendarInicio = Calendar.getInstance();
        calendarInicio.setTime(datos.getFechaAlta());

        //int diaInicio = calendarInicio.get(Calendar.DAY_OF_MONTH);
        int mesInicio = calendarInicio.get(Calendar.MONTH) + 1; // 0 Enero, 11 Diciembre
        int anioInicio = calendarInicio.get(Calendar.YEAR);

        // Fecha fin
        String s[] = nominaAPedir.split("/");
        int mesFin = Integer.parseInt(s[0]); 
        int anioFin = Integer.parseInt(s[1]);

        int anos = anioFin - anioInicio;
        int meses = 0;

        if(mesFin>mesInicio)
            meses = mesFin - mesInicio;

        int totalMeses = anos * 12 + meses;

        if(totalMeses < 0)
            totalMeses = -1;

        //guarda el mes nomina
        datos.setMesNomina(mesFin);
        //guarda el anho nomina
        datos.setAnoNomina(anioFin);
        //guardar la antiguedad de cada trabajador 
        datos.setMesesAntiguedad(totalMeses);
            
        
        
    }
    
    
     //asignacion de datos de la hoja2 a los trabaajdores
     public void calcularSalarioBase(){
     
         //recorro el array de trabajadores
         for (int i = 0; i < arrayWorkers.size(); i++) {
             
             //selecciono su puesto de trabajado
             String categoriaTrabajador = arrayWorkers.get(i).getCategoriaPuestoTrabajo();
             
             //recorro el array de categorias de la hoja2
             for (int j = 0; j < categorias.size(); j++) {
                 
                 //si el puesto es igual a la categoria se pone ese salario base al trabajador
                 if(categoriaTrabajador.equals(categorias.get(j))){
                     float sB = salarioBase.get(j);//se coje el salario base del array de salarios Base
                     arrayWorkers.get(i).setSalarioBase(sB);//se guarda el salario del trabajador
                     int c = complementos.get(j);//se coje el complemento de larray de complementos
                     arrayWorkers.get(i).setComplemento(c);//se guarda el complemento del trabajador
                 }
                 
             }
             
         }
     
     }
     
     
     //calculo antiguedad y bruto anual
     public void calculoAntiguedadYBrutoAnual(){
         
        float sB, comp, bruto;
      
         for (int i = 0; i < arrayWorkers.size(); i++) {
             
            int meses = arrayWorkers.get(i).getMesesAntiguedad();
            int anos = meses / 12;
            
            Calendar calendarInicio = Calendar.getInstance();
            calendarInicio.setTime(arrayWorkers.get(i).getFechaAlta());
            int mes = calendarInicio.get(Calendar.MONTH)+1;
            
            int mesPedido = arrayWorkers.get(i).getMesNomina();
            
            boolean aux = false;
            int t1=0,t2=0;
            int x=3;
            
             for (int j = 0; j < 18; j++) {
                if(x==anos){
                    int ant = anos / 3;
                    int antAnterior = ant - 1;
                    if(ant>0)
                         t1 = trienio.get(ant-1);
                    if(antAnterior>0)
                         t2 = trienio.get(antAnterior-1);
                    
                      if(mesPedido>=mes){
                         arrayWorkers.get(i).setAntiguedad(t1); 
                       }else{
                          arrayWorkers.get(i).setAntiguedad(t2);
                      }
                      
                  aux= true;
                  break;
                }
                
                x=x+3;
             }
             
             int t = 0;
             if(aux==false){
                int ant = anos / 3;
                if(ant>0)
                     t = trienio.get(ant-1);
                arrayWorkers.get(i).setAntiguedad(t);
           
             } 
             
             
            sB = arrayWorkers.get(i).getSalarioBase();
            comp = arrayWorkers.get(i).getComplemento();
            
            if(aux==true){
                float a = mes-1;
                float b = 12-a;
            
                bruto = sB + comp + (t2*a*14)/12 + (t1*b*14)/12;

            }else{
                bruto = sB + comp + t*14;
            }
             
             arrayWorkers.get(i).setBrutoTotalNomina(bruto); 
             
         }//for
  
     }
     
     //calculo contingencias
    public void calcularContingencias(){
        
        float importeCalculoContingencias, contingencias;
        
        for (int i = 0; i < arrayWorkers.size(); i++) {
            
            
            importeCalculoContingencias = arrayWorkers.get(i).getBrutoTotalNomina()/12;
            contingencias = importeCalculoContingencias*cuotaObreraGeneralTrabajador/100;
            
             if(arrayWorkers.get(i).getEsExtra().equals("SiEsExtra")){
                contingencias = 0;
                importeCalculoContingencias = 0;
             }
             
            arrayWorkers.get(i).setCuotaObreraGeneral(contingencias);   
            arrayWorkers.get(i).setImporteCalculoContingencias(importeCalculoContingencias);
        }
    
    
    }
    
    //prorrata extra
    public void prorrata(){
    
        float brutoAnual, parteProporcionalExtra;
      
        
         for (int i = 0; i < arrayWorkers.size(); i++) {
            if(arrayWorkers.get(i).getProrrata().equals("SI")){
                brutoAnual = arrayWorkers.get(i).getBrutoTotalNomina();
                parteProporcionalExtra = ((brutoAnual/14)*2)/12;
                arrayWorkers.get(i).setParteProporcionalExtra(parteProporcionalExtra);
            }
    
         }
    
    
    
    }
    
    //calculo irpf
    public void calculoIRPF(){
    
        float brutoAnual, bruto, ret = 0,aux, irpf;

         for (int i = 0; i < arrayWorkers.size(); i++) {
             
            brutoAnual = arrayWorkers.get(i).getBrutoTotalNomina();
             for (int j = 0; j < brutoArray.size() ; j++) {
                   bruto = brutoArray.get(j);
                   if(bruto>brutoAnual){
                       ret = retencion.get(j);
                        break;
                    }
                  
             }
             
           arrayWorkers.get(i).setIRPFPorcentaje(ret);
             
            if(arrayWorkers.get(i).getProrrata().equals("SI")){
               aux = brutoAnual/12;  
               irpf = aux*ret/100;  
           }else{
                aux = brutoAnual/14;   
                irpf = aux*ret/100;   
           }
           
           arrayWorkers.get(i).setImporteIRPF(irpf);
            
        }
    
    
    }
     
    //calculo liquido a percibir
    public void calculoLiquidoPercibir(){
    
             float liquido, prorrata;
        
          for (int i = 0; i < arrayWorkers.size(); i++) {
          
              float desempleo = (arrayWorkers.get(i).getBrutoTotalNomina()/12)*cuotaDesempleoTrabajador/100;
              float formacion = (arrayWorkers.get(i).getBrutoTotalNomina()/12)*cuotaFormacionTrabajador/100;
              
              prorrata = arrayWorkers.get(i).getParteProporcionalExtra();
              
               if(arrayWorkers.get(i).getProrrata().equals("NO")){
                   prorrata = 0;
               }
               if(arrayWorkers.get(i).getEsExtra().equals("SiEsExtra")){
                   desempleo = 0;
                   formacion = 0;
               }
               liquido = arrayWorkers.get(i).getBrutoTotalNomina()/14 
                            - arrayWorkers.get(i).getCuotaObreraGeneral()
                            - desempleo
                            - formacion
                            - arrayWorkers.get(i).getImporteIRPF()
                            + prorrata;
               
             arrayWorkers.get(i).setCuotaFormacion(formacion);
             arrayWorkers.get(i).setCuotaDesempleo(desempleo);
             arrayWorkers.get(i).setLiquidoAPercibirNomina(liquido);
              
          }
    
    
    }
    
    //reducciones de baja laboral
    //TODO
    public void reducionesBaja(){
    
        Calendar calendarBaja = Calendar.getInstance();
        Calendar calendarAlta = Calendar.getInstance();
        long fechaInicialMs = 0, fechaFinalMs = 0, diferencia;
        int mesBaja = 0, mesAlta = 0, diaBaja, diaAlta, anhoBaja, anhoAlta, mesPedido, anoPedido;
        double diasBaja;
        for (int i = 0; i < arrayWorkers.size(); i++) {
            
            if(arrayWorkers.get(i).getEsExtra().equals("NoEsExtra")){
                
                if(arrayWorkers.get(i).getFechaBajaLaboral()!=null){
                    calendarBaja.setTime(arrayWorkers.get(i).getFechaBajaLaboral());
                    fechaInicialMs =  calendarBaja.getTimeInMillis();
                    mesBaja = calendarBaja.get(Calendar.MONTH);
                    diaBaja = calendarBaja.get(Calendar.DAY_OF_MONTH);
                    anhoBaja = calendarBaja.get(Calendar.YEAR);
                
                    if(arrayWorkers.get(i).getFechaAltaLaboral()!=null){
                        calendarAlta.setTime(arrayWorkers.get(i).getFechaAltaLaboral());
                        fechaFinalMs =  calendarAlta.getTimeInMillis();
                        mesAlta = calendarAlta.get(Calendar.MONTH);
                        diaAlta = calendarAlta.get(Calendar.DAY_OF_MONTH);
                        anhoAlta = calendarAlta.get(Calendar.YEAR);
                    }else{
                        Calendar c = new GregorianCalendar(anhoBaja, mesBaja+1, 1);
                        fechaFinalMs =  c.getTimeInMillis();
                        mesAlta = c.get(Calendar.MONTH);
                        diaAlta = c.get(Calendar.DAY_OF_MONTH);
                        anhoAlta = calendarAlta.get(Calendar.YEAR);
                    }

                    diferencia = fechaFinalMs - fechaInicialMs;
                    diasBaja = Math.floor(diferencia / (1000 * 60 * 60 * 24));


                    float bruto = arrayWorkers.get(i).getSalarioBase()/14
                                     + arrayWorkers.get(i).getComplemento()/14
                                     + arrayWorkers.get(i).getAntiguedad()
                                     + arrayWorkers.get(i).getParteProporcionalExtra();

                    float brutoDia = bruto/30;

                    float[] arrayDiasBaja  = new float[31];

                    for (int x = 0; x < arrayDiasBaja.length; x++) {

                         if(x<3)
                             arrayDiasBaja[x]=brutoDia/2;
                         if(x>2&&x<=20)
                             arrayDiasBaja[x]=brutoDia/4;
                         if(x>20)
                             arrayDiasBaja[x]=(arrayWorkers.get(i).getBrutoTotalNomina()/14)/30;
                    }
              
                 
                    
                mesPedido = arrayWorkers.get(i).getMesNomina();
                anoPedido = arrayWorkers.get(i).getAnoNomina();
                float total = 0;
                int totalDiasMes, diasDeBajaMes;
                
                  if(mesPedido-1 == mesBaja && anoPedido == anhoBaja){
                  
                      totalDiasMes = calendarBaja.getMaximum(Calendar.DAY_OF_MONTH);
                      diasDeBajaMes = (totalDiasMes - diaBaja) + 1;
                      
                      for (int j = 0; j < arrayDiasBaja.length; j++) {
                          if(j<diasDeBajaMes)
                              total += arrayDiasBaja[j];
                      }
                  
                      arrayWorkers.get(i).setBrutoTotalNomina( arrayWorkers.get(i).getBrutoTotalNomina()-total);
                      
                  }
                  else if(mesPedido-1 == mesAlta && anoPedido == anhoBaja && anhoBaja == anhoAlta) {
                  
                        if(mesAlta-mesBaja==1){
                              totalDiasMes = calendarBaja.getMaximum(Calendar.DAY_OF_MONTH);
                              diasDeBajaMes = (totalDiasMes - diaBaja) + 1;
                              
                              int diaBajaMesAlta = diaAlta - diasDeBajaMes;
                              
                           for (int j = diasDeBajaMes; j < arrayDiasBaja.length; j++) {
                                if(j<=diaBajaMesAlta)
                                     total += arrayDiasBaja[j];
                            }    
                              
                        }
                        
                      arrayWorkers.get(i).setBrutoTotalNomina( arrayWorkers.get(i).getBrutoTotalNomina()-total);
                  
                  }else if(mesPedido-1 > mesBaja && mesPedido-1 < mesAlta){
                  
                      if(mesPedido-1-1==mesBaja){
                         totalDiasMes = calendarBaja.getMaximum(Calendar.DAY_OF_MONTH);
                         diasDeBajaMes = (totalDiasMes - diaBaja) + 1;
                         
                         if(diasDeBajaMes<=20){
                             int diasBajaPedido = 20 - diasDeBajaMes;
                             
                            for (int j = diasDeBajaMes; j < arrayDiasBaja.length; j++) {
                                if(j<=diasBajaPedido)
                                     total += arrayDiasBaja[j];
                            }  
                         }
                         
                      }
                      
                    arrayWorkers.get(i).setBrutoTotalNomina( arrayWorkers.get(i).getBrutoTotalNomina()-total);
                  
                  }
            }
             
          }   
             
        }//for
    
    
    }
    
    public void calculosEmpresarios(){
        for (int i = 0; i < arrayWorkers.size(); i++) {
        
          
        float baseCalculoContingenciasE = arrayWorkers.get(i).getBrutoTotalNomina()/12;
        
        float importeContingenciasE = baseCalculoContingenciasE*contingenciasComunesEmpresario/100;
        
        float importeFogasa = baseCalculoContingenciasE*fogasaEmpresario/100;
        
        float desempleoE = baseCalculoContingenciasE*desempleoEmpresario/100;
        
        float formacionE = baseCalculoContingenciasE*formacionEmpresario/100;
        
        float accidentesTrabajoE = baseCalculoContingenciasE*accidentesTrabajoEmpresario/100;
        
        
        
            
        arrayWorkers.get(i).setBaseCalculoContingenciasEmpresario(baseCalculoContingenciasE);
        arrayWorkers.get(i).setImporteContingenciasComunesEmpresario(importeContingenciasE);
        arrayWorkers.get(i).setImporteFogasa(importeFogasa);
        arrayWorkers.get(i).setDesempleoEmpresario(desempleoE);
        arrayWorkers.get(i).setFormacionEmpresa(formacionE);
        arrayWorkers.get(i).setAccidentesTrabajoEnfermedadProfesional(accidentesTrabajoE);
        
        
        }
    }
    
    //devuelve el array de los trabajadores
     public ArrayList<WorkersDatosNomina> getArrayWorkers(){
         
         return arrayWorkers;
     } 
     
     
    
}
