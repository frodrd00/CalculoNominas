/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.SSII2.entity;

import com.itextpdf.text.Chunk;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.TabSettings;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 *
 * @author Windows
 */
public class PDFCreator {

    String rutaArchivoTxt;
    String rutaGuardarPDF;
    String nombreCarpeta;

    public PDFCreator(String rutaArchivoTxt, String rutaGuardarPDF, String nombreCarpeta) {
        this.rutaArchivoTxt = rutaArchivoTxt;
        this.rutaGuardarPDF = rutaGuardarPDF;
        this.nombreCarpeta = nombreCarpeta;
    }

    public void leerTxts() throws FileNotFoundException, IOException, DocumentException, ParseException{
    
        String cadena;
        String[] arrayDatos = null;
        FileReader f = new FileReader(rutaArchivoTxt);
        File carpetaNomina = new File(rutaGuardarPDF+nombreCarpeta);
        carpetaNomina.mkdir();
        try (BufferedReader b = new BufferedReader(f)) {
            while((cadena = b.readLine())!=null) {
                arrayDatos=cadena.split(";");
                generarPDF(arrayDatos);
            }
        }
    }
    
    public void generarPDF(String[] datos)  throws IOException, DocumentException, ParseException{
        
        String nombrePDF;
        
        if(datos[33].equals("NoEsExtra"))
            nombrePDF =  datos[2] + "_" + datos[23] + "_" + datos[24] + ".pdf";
        else
            nombrePDF = datos[2] + "_" + datos[23] + "_" + datos[24] + "_EXTRA.pdf";
        
        String rutaPDF = rutaGuardarPDF + nombreCarpeta + "/" + nombrePDF;
        
        
        Document document = new Document();
        
        PdfWriter.getInstance(document, new FileOutputStream(rutaPDF));
        document.setMargins(50, 50, 10, 0);
        document.open();
     
       //logo empresa
       Image img = Image.getInstance("src/es/SSII2/resources/logo.jpg");
       document.add(img);
       
       Paragraph espacio= new Paragraph("\n\n");
       document.add(espacio);
       
       Font font = new Font(Font.getFamily("TIMES_ROMAN"), 12, Font.BOLD);
       
       Chunk empresa = new Chunk("\n  Empresa:");
       empresa.setFont(font);
       
       //datos empresa
       Paragraph empresaNombre= new Paragraph("  "+datos[0]);
       Paragraph empresaCif= new Paragraph("  CIF: "+datos[1]);
       Paragraph empresaCalle= new Paragraph("  Avenida de la facultad-6");
       Paragraph empresaCP= new Paragraph("  24001 León\n\n");
                                       
       PdfPTable table = new PdfPTable(2);
       table.setHorizontalAlignment(Element.ALIGN_CENTER);
       
       PdfPCell cell = new PdfPCell();
       cell.addElement(empresa);
       cell.addElement(empresaNombre);
       cell.addElement(empresaCif);
       cell.addElement(empresaCalle);
       cell.addElement(empresaCP);
       
       table.addCell(cell);
       
       //datos trabajador
       cell = new PdfPCell();       
       Chunk destinatario = new Chunk("\n  Destinatario:");
       destinatario.setFont(font);
       
       Paragraph trabajadorNombreCompleto = new Paragraph("  "+datos[2]+" "+datos[3]+" "+datos[4]+" ("+datos[5]+")");
       Paragraph trabajadorDNI = new Paragraph("  Categoria: "+datos[8]);
       Paragraph tabajadorAntiguedad = new Paragraph("  Antigüedad: " + datos[6]);
       Paragraph tabajadorDirec = new Paragraph("  Avenida - 21001 León\n\n");
       
       cell.addElement(destinatario);
       cell.addElement(trabajadorNombreCompleto);
       cell.addElement(trabajadorDNI);
       cell.addElement(tabajadorAntiguedad);
       cell.addElement(tabajadorDirec);
       
       table.addCell(cell);
       table.setWidthPercentage(100);

       document.add(table);


       document.add(espacio);
       
       //datos calculos nomina
       int mesInt =  Integer.parseInt(datos[23]);
       String mesString=datos[23];
       if(mesInt<10){
           mesString = "0"+mesString;
       }
        
       Chunk periodoLiquidado = new Chunk("Periodo liquidado " + mesString + "/" + datos[24]);
       periodoLiquidado.setFont(font);
       
       document.add(periodoLiquidado);
       
       
        PdfPTable tableDatos = new PdfPTable(5);
        tableDatos.setWidthPercentage(100);
        
        //fila 1
        tableDatos.addCell(" ");
        tableDatos.addCell("Cant.");
        tableDatos.addCell("Imp. Unit.");
        tableDatos.addCell("Dev.");
        tableDatos.addCell("Deducciones");
        
        //fila2
        tableDatos.addCell("Salario Base");
        tableDatos.addCell("30 días");
        tableDatos.addCell(calculoDatos30Dias(datos[11]));
        tableDatos.addCell(datos[11]);
        tableDatos.addCell(" ");
        
        //fila3
        tableDatos.addCell("Complemento");
        tableDatos.addCell("30 días");
        tableDatos.addCell(calculoDatos30Dias(datos[12]));
        tableDatos.addCell(datos[12]);
        tableDatos.addCell(" ");
        
        //fila 4
        tableDatos.addCell("Antigüedad");
        tableDatos.addCell("30 días");
        tableDatos.addCell(calculoDatos30Dias(datos[13]));
        tableDatos.addCell(datos[13]);
        tableDatos.addCell(" ");
        
        //fila5
        String prorrata = "0,00";
        if(stringToFloat(datos[19])!=0){
            tableDatos.addCell("Prorrata");
            tableDatos.addCell("30 días");
            tableDatos.addCell(calculoDatos30Dias(datos[19]));
            tableDatos.addCell(datos[19]);
            tableDatos.addCell(" ");
            prorrata=datos[19];
        }
        
        //fila6
        tableDatos.addCell(" ");
        tableDatos.addCell(" ");
        tableDatos.addCell(" ");
        tableDatos.addCell(" ");
        tableDatos.addCell(" ");  
        
        //fila7
        tableDatos.addCell("Conting. Gener.");
        tableDatos.addCell("4,7%");
        tableDatos.addCell(" ");
        tableDatos.addCell(" ");
        tableDatos.addCell(datos[16]);
        
        //fila8
        tableDatos.addCell("Desempleo");
        tableDatos.addCell("1,6%");
        tableDatos.addCell(" ");
        tableDatos.addCell(" ");
        tableDatos.addCell(datos[17]);  
        
        //fila9
        tableDatos.addCell("Cuota formación");
        tableDatos.addCell("0,1%");
        tableDatos.addCell(" ");
        tableDatos.addCell(" ");
        tableDatos.addCell(datos[18]);
        
        //fila10
        tableDatos.addCell("IRPF");
        tableDatos.addCell(datos[9]+"%");
        tableDatos.addCell(" ");
        tableDatos.addCell(" ");
        tableDatos.addCell(datos[20]);
        
        //fila11
        tableDatos.addCell(" ");
        tableDatos.addCell(" ");
        tableDatos.addCell(" ");
        tableDatos.addCell(" ");
        tableDatos.addCell(" ");
        
        float totalDeducciones = stringToFloat(datos[20])
                                 +stringToFloat(datos[16])
                                 +stringToFloat(datos[17])
                                 +stringToFloat(datos[18]);
        
        
        //fila12
        tableDatos.addCell("Total Deducc.");
        tableDatos.addCell(" ");
        tableDatos.addCell(" ");
        tableDatos.addCell(" ");
        tableDatos.addCell(conDosDecimales(totalDeducciones));
        
        float totalDevengos =  stringToFloat(datos[11])
                               +stringToFloat(datos[12])
                               +stringToFloat(datos[13])
                               +stringToFloat(prorrata);
        
        //fila13
        tableDatos.addCell("Total Devengos");
        tableDatos.addCell(" ");
        tableDatos.addCell(" ");
        tableDatos.addCell(conDosDecimales(totalDevengos));
        tableDatos.addCell(" ");
     
        document.add(tableDatos);
        
        Font font2 = new Font(Font.getFamily("TIMES_ROMAN"), 12, Font.BOLD|Font.UNDERLINE);
        
        Paragraph p = new Paragraph();
        p.setTabSettings(new TabSettings(56f));
        p.add(Chunk.TABBING);
        p.add(Chunk.TABBING);
        p.add(Chunk.TABBING);
        p.add(Chunk.TABBING);
        p.add(Chunk.TABBING);
        Chunk liquidoAPercibir = new Chunk("Líquido a percibir:    " + datos[22]);
        liquidoAPercibir.setFont(font2);
        
        p.add(liquidoAPercibir);
        document.add(p);
        
        document.add(espacio);
        
        //cuenta banco
        String cuentaString = datos[26];
        String resultado="";
        int longitud = cuentaString.length();
        int j=0;
        for (int i = 0; i < longitud; i=j) {
            j=i+4;
            if(i!=20&&j!=23)
                    resultado = resultado + cuentaString.substring(i, j)+" ";
            else
                    resultado = resultado + "****";
        }
        
        Paragraph cuenta = new Paragraph("A ingesar en cuenta:    "+resultado);
        document.add(cuenta);
        
        document.add(espacio);
        
        //EMPRESARIO
        Chunk empresario = new Chunk("EMPRESARIO");
        empresario.setFont(font);
        document.add(empresario);
        
        
        PdfPTable tableEmpresario = new PdfPTable(2);
        tableEmpresario.setWidthPercentage(100);
        
        //fila1
        tableEmpresario.addCell("Contingencia comunes empresario");
        tableEmpresario.addCell(datos[28]);
        
        //fila2
        tableEmpresario.addCell("Desempleo empresario");
        tableEmpresario.addCell(datos[30]);
        
        //fila3
        tableEmpresario.addCell("Formación empresario");
        tableEmpresario.addCell(datos[31]);
        
        //fila4
        tableEmpresario.addCell("Accidentes trabajo empresario");
        tableEmpresario.addCell(datos[32]);
        
        //fila5
        tableEmpresario.addCell("FOGASA empresario");
        tableEmpresario.addCell(datos[29]);
        
        float totalEmpresario = stringToFloat(datos[28])
                                +stringToFloat(datos[29])
                                +stringToFloat(datos[30])
                                +stringToFloat(datos[31])
                                +stringToFloat(datos[32]);
        
        //fila6
        tableEmpresario.addCell("TOTAL empresario");
        tableEmpresario.addCell(conDosDecimales(totalEmpresario));
        
        document.add(tableEmpresario);
       
       document.close();
        
    }

    public String calculoDatos30Dias(String datoArray) throws ParseException{
    
        //formato 2 decimales
        DecimalFormat df = new DecimalFormat("0.00");
        
        float f = stringToFloat(datoArray);
        String resultado = df.format(f / 30);
        
        return resultado;
    
    }
    
    public float stringToFloat(String s) throws ParseException{
        
        NumberFormat nf = NumberFormat.getInstance(); 
        float f = nf.parse(s).floatValue();
    
        return f;
    }
    
    public String conDosDecimales(float f){
    
        DecimalFormat df = new DecimalFormat("0.00");
        String s = df.format(f);
        
        return s;
    }
   

    
}
