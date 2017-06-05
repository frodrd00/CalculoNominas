package es.SSII2.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.util.ArrayList;
import es.SSII2.entity.WorkersDni;

/**
 * 
 * @author Flavio, Alvaro
 */
public class ExcelManagerDni {
    
    String excel = ""; //cadena con la ruta del excel
    WorkersDni dni; // objeto WorkersDni
    
    //constructor que se le pasa la ruta del fichero excel y el objeto nuevo WorkersDni
    public ExcelManagerDni(String excel,WorkersDni dni) {
        this.excel = excel;
        this.dni = dni;
    }

    //metodo para leer excel
    public void LeerExcelDni() throws FileNotFoundException, IOException {
        
	FileInputStream file;
        file = new FileInputStream(new File(excel));
        /*
         * Obtenemos la primera pestaña a la que se quiera procesar indicando el indice.
         * Una vez obtenida la hoja excel con las filas que se quieren leer obtenemos el iterator
         * que nos permite recorrer cada una de las filas que contiene.
         */
        try ( // Crear el objeto que tendra el libro de Excel
                XSSFWorkbook workbook = new XSSFWorkbook(file)) {
            /*
            * Obtenemos la primera pestaña a la que se quiera procesar indicando el indice.
            * Una vez obtenida la hoja excel con las filas que se quieren leer obtenemos el iterator
            * que nos permite recorrer cada una de las filas que contiene.
            */
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            Row row;
            // Recorremos todas las filas para mostrar el contenido de cada celda
            while (rowIterator.hasNext()) {
                row = rowIterator.next();
                
                // Obtenemos el iterator que permite recorres todas las celdas de una fila
                Iterator<Cell> cellIterator = row.cellIterator();
                Cell celda;
                
                while (cellIterator.hasNext()){
                    celda = cellIterator.next();
                   if(celda.getRowIndex() >= 1 && celda.getColumnIndex() == 3 && celda.getCellType() != 3){
                        // Dependiendo del formato de la celda el valor se debe mostrar como String, Fecha, boolean, entero...
                       /* switch(celda.getCellType()) {
                            case Cell.CELL_TYPE_NUMERIC:
                                if( DateUtil.isCellDateFormatted(celda) ){
                                    System.out.println(celda.getDateCellValue());
                                }else{
                                    System.out.println(celda.getNumericCellValue());
                                }
                                break;
                            case Cell.CELL_TYPE_STRING:
                                System.out.println(celda.getStringCellValue());
                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
                                System.out.println(celda.getBooleanCellValue());
                                break;
                        }*/

                       //System.out.print(celda.getStringCellValue());
                      // System.out.println("("+celda.getRowIndex()+","+celda.getColumnIndex()+")");
                       
                      //se mete cada valor de los dni en un arraylist original de dnis
                       dni.anadirDniArray(celda.getStringCellValue());
                       //se mete la posicion el el arraylsit
                       dni.anadirPosArray(celda.getRowIndex()+"-"+celda.getColumnIndex());
                   }
                }
            }
            // cerramos el libro excel
        }
        
    }
    //metodo para actualizar el excel con los digitos de control calculados bien
    public void actualizarDnis(ArrayList<String> dnisOriginales,ArrayList<String> dnisCalculados, ArrayList<String> pos) throws IOException {
          
            int row,col;
            String nuevoDni,originalDni;
            boolean noCambios = true;
            
            FileInputStream file;
            file = new FileInputStream(new File(excel));  
              
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Cell cell;
            
            //sacar los dnis calculados
            for (int i = 0; i < dnisCalculados.size(); i++) {
            
                originalDni = dnisOriginales.get(i);
                nuevoDni = dnisCalculados.get(i);//se coje el dni
                
                //comprobar que el dni calculado no sea igual para cambiarlo
                if(!originalDni.equals(nuevoDni)){

                    String posicion = pos.get(i);//se coje la posicion

                    String[] a = posicion.split("-");//split del string "1-3" de las posiciones que estan el el arraylist

                    row = Integer.parseInt(a[0]);//fila "1"
                    col = Integer.parseInt(a[1]);//columna "3"

                    cell = sheet.getRow(row).getCell(col); //obtiene la fila y columna
                    cell.setCellValue(nuevoDni);//cambia la celda

                    file.close();//cierra el archivo

                    //escribe en el excel
                    try (FileOutputStream outFile = new FileOutputStream(new File(excel))) {
                        workbook.write(outFile);

                       /* System.out.println("Dni original: "+ originalDni +
                                ", Dni actualizado: " + nuevoDni + 
                                ", Pos: " + "("+row+"-"+col+")" );*/
                        
                        noCambios = false;
                    }
                }//if
            }//for
            
            //comprobar si se ha actualizado algun campo dni
            /*if(!noCambios){ 
                System.out.println();
                System.out.println("Documento excel actualizado con exito.");
            }else{
                System.out.println("No se han producido cambios en el documento excel.");
            }*/
            
    }

}

