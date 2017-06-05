/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.SSII2.manager;
import es.SSII2.entity.WorkersDatosNomina;
import es.SSII2.entity.WorkersEmail;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 *
 * @author Alvaro, Flavio
 */
public class ExcelManagerMail {

    WorkersDatosNomina workers;//clase para almacenar los datos de los trabajadores
    WorkersEmail email;//clase para manipular los datos de los trabajadores y crear el email
    String excel;
    ArrayList<WorkersDatosNomina> arrayWorkers= new ArrayList<>();//se alamacenan los trabajadores 
    
    public ExcelManagerMail(String excel) {
        this.excel = excel;
    }

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
                
                workers = new WorkersDatosNomina();
                row = rowIterator.next();
                
                // Obtenemos el iterator que permite recorres todas las celdas de una fila
                Iterator<Cell> cellIterator = row.cellIterator();
                Cell celda;
                
                
                String celdas;

                while (cellIterator.hasNext()){
                    
                    celda = cellIterator.next();
                    
                   if(celda.getRowIndex() >= 1 && celda.getColumnIndex() == 0 && celda.getCellType() != 3){
                    
                       celdas = celda.getStringCellValue();
                       workers.setNombre(celdas);//se coje el nombre del trabajador y se guarda en la clase WorkersDatos
                   }
                   if(celda.getRowIndex() >= 1 && celda.getColumnIndex() == 1 && celda.getCellType() != 3){
                    
                     celdas = celda.getStringCellValue();
                     workers.setApellido1(celdas);//se coje el apellido1 del trabajador y se guarda en la clase WorkersDatos
                   }
                   if(celda.getRowIndex() >= 1 && celda.getColumnIndex() == 2 && celda.getCellType() != 3){
                    
                      celdas = celda.getStringCellValue();
                     workers.setApellido2(celdas); //se coje el apellido2 del trabajador y se guarda en la clase WorkersDatos
                   }
                   if(celda.getRowIndex() >= 1 && celda.getColumnIndex() == 6 && celda.getCellType() != 3){
                    
                     celdas = celda.getStringCellValue();
                     workers.setNombreEmpresa(celdas);//se coje el apellido3 del trabajador y se guarda en la clase WorkersDatos
                   }
                }
              
                //se mete los datos de cada trabajador en el arraylist 
                if(workers.getNombre()!=null)
                    arrayWorkers.add(workers);
            }
            
        }
        
         email = new WorkersEmail(arrayWorkers);//clase para procesar el correo
         email.creacionCorreos();
    }
    public void actualizarEmails() throws IOException {
          
            int row;
            int col=15;//email col 15
 
            FileInputStream file;
            file = new FileInputStream(new File(excel));  
              
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            
            
                for (int i = 0; i < arrayWorkers.size(); i++) {
                    
                    row=i;
                    XSSFRow rowEmail = sheet.getRow(row+1); //coje la fila
                    XSSFCell cellEmail = rowEmail.createCell(col); //crea la celda
                    cellEmail.setCellValue(arrayWorkers.get(i).getEmail());//pone el email
                
                    //escribe en el excel
                    try (FileOutputStream outFile = new FileOutputStream(new File(excel))) {
                        workbook.write(outFile);
                    }
                
            }//for
    }
}
