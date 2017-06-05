/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.SSII2.manager;

import es.SSII2.entity.WorkersBank;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Flavio, Alvaro
 */
public class ExcelManagerAccount {
    
    WorkersBank account;
    String excel;

    public ExcelManagerAccount(String excel,WorkersBank account) {
        this.account = account;
        this.excel = excel;
    }

   
    //leer las cuentas del excel
    public void readAccountExcel() throws FileNotFoundException, IOException{
    
        FileInputStream file;
        file = new FileInputStream(new File(excel));

        try ( 
            XSSFWorkbook workbook = new XSSFWorkbook(file)) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            Row row;
            // Recorremos todas las filas para mostrar el contenido de cada celda
            while (rowIterator.hasNext()) {
                row = rowIterator.next();
                
                // Obtenemos el iterator que permite recorres todas las celdas de una fila
                Iterator<Cell> cellIterator = row.cellIterator();
                Cell celda;
                
                double val;
                DecimalFormat df = new DecimalFormat("#");

                while (cellIterator.hasNext()){
                    
                    celda = cellIterator.next();
                    
                   if(celda.getRowIndex() >= 1 && celda.getColumnIndex() == 8 && celda.getCellType() != 3){
                    
                      val = celda.getNumericCellValue();
                      String stringPOI = NumberToTextConverter.toText(val);
                      
                      //anadir la cuenta al arraylist y las posiciones
                      account.addAccount(stringPOI);
                      account.addAccountPos(celda.getRowIndex()+"-"+celda.getColumnIndex());
                    
                   }
                }
            }
            
        }
    }
    
    //actualizar cuentas y poner el iban en el excel
    public void actualizarCuentas(ArrayList<String> cuentas,
                                 ArrayList<String> cuentasCorrectas,
                                 ArrayList<String> arrayIban,
                                 ArrayList<String> pos) throws IOException, ParseException {
          
            int row,col;
            String originalCuenta,nuevaCuenta,iban,posicion,entidad,oficina,dc,numCuenta;
            String[] a;
            
            FileInputStream file;
            file = new FileInputStream(new File(excel));  
            
            FileOutputStream outFile;
                 
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            XSSFRow rowIban;
            XSSFCell cellIban;
            Cell cellCuenta;

            //sacar las cuentas
            for (int i = 0; i < cuentas.size(); i++) {
            
                originalCuenta = cuentas.get(i);//se coje la cuenta original
                nuevaCuenta = cuentasCorrectas.get(i);//se coje la cuenta actualizada   
                iban = arrayIban.get(i);// cojer el iban

                posicion = pos.get(i);//se coje la posicion
                a = posicion.split("-");//split del string "1-3" de las posiciones que estan el el arraylist
                row = Integer.parseInt(a[0]);//fila "1"
                col = Integer.parseInt(a[1]);//columna "3"
                
                //insertar el iban
                rowIban = sheet.getRow(row);//coje la fila
                cellIban = rowIban.createCell(1+col);//crea la celda
                cellIban.setCellValue(iban);
                
                //2096 0056 16 3231500000
                entidad = nuevaCuenta.substring(0, 4);
                oficina = nuevaCuenta.substring(4, 8);
                dc = nuevaCuenta.substring(8,10);
                numCuenta = nuevaCuenta.substring(10);
                  
                //actualizar la cuenta si esta mal el cc
                if(!originalCuenta.equals(nuevaCuenta)){

                    cellCuenta = sheet.getRow(row).getCell(col); //obtiene la fila y columna
                    DecimalFormat df = new DecimalFormat("#");
                    Number cuenta = df.parse(nuevaCuenta);
                    cellCuenta.setCellValue(cuenta.doubleValue());

                    /*System.out.println("Cuenta actualizada: " + iban + 
                                       "-" + entidad +
                                       "-" + oficina +
                                       "-" + dc + 
                                       "-" + numCuenta); */
                                       

                }else{

                     /*System.out.println("Cuenta correcta:    " + iban + 
                                       "-" + entidad +
                                       "-" + oficina +
                                       "-" + dc + 
                                       "-" + numCuenta); */
                        
                }
                

            }//for
         
          outFile = new FileOutputStream(new File(excel));
          //escribe en el excel
          workbook.write(outFile);
          outFile.close();
            
          file.close();//cierra el archivo  
         
            
    }
    
}
