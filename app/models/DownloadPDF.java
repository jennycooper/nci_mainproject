package models;
 
/*
 * Name: DownloadPDF.java
 * Description: class to create a pdf invoice and download it to the user's local pc
 * Written On: 01/11/2012
 * @author Jenny Cooper, x12101303
 * 
 */


import java.io.ByteArrayOutputStream;

import java.io.FileOutputStream;
import java.io.IOException;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

 

public class DownloadPDF {
	public byte[] byteArrayPDF= null;
	
	//constructor
	public DownloadPDF(Reserve res){
		this.byteArrayPDF = createPDF(res);
	}

    /**
     * creates a pdf invoice, and returns it in the form of a byte array, which will be sent to the html view
     * @param res (a reservation instance, so costs can be calculated)
     * @return byte array
     */
    public byte[] createPDF(Reserve res){
    	//call methods to calculate the costs of the reservation
    	res.calcValues();
    	res.getCosts().addTotals(res);
    	
    	Document document = new Document();
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	try {
    		//create a pdf instance of the document, which writes to a byte output stream (baos)
			PdfWriter.getInstance(document, baos);
	    	document.open();

	    	// add paragraphs of details to the pdf
	        ArrayList<Paragraph> p = getDetails(res);
	        for (int i=0;i<p.size();i++){
	        	document.add(p.get(i));
	        }
	        //add a table of costs to the pdf
	        document.add(getTable(res));

    	} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	finally{
        	
        	try {
        		document.close();
				baos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    	byte[] byteArray = baos.toByteArray();
    	return byteArray;

    }
      

    /*
     * create a list of paragraphs, containing fields and values to be inserted in the pdf invoice
     */
    public static ArrayList<Paragraph> getDetails(Reserve res){
    	 ArrayList<Paragraph> p = new ArrayList<Paragraph>();
    	 
    	 //get today's date and convert it to a string, to add to pdf
    	 DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");
		 Date today = Calendar.getInstance().getTime();        

		 p.add(new Paragraph("Date: "+ df.format(today)));
    	 p.add(new Paragraph("Reservation for: "+res.getMyGuest().getName()));
         p.add(new Paragraph("Check in Date: "+df.format(res.getCheckin())));
         p.add(new Paragraph("Check out Date: "+df.format(res.getCheckout())));
         p.add(new Paragraph("Number of Rooms: "+res.getDetailList().size()));
         p.add(new Paragraph("Number of Nights: "+res.getLen()));
         p.add(new Paragraph("Number of Adults: "+res.getNumAdults()));
         if (res.getNumChild()!=0)
        	 p.add(new Paragraph("Number of Children: "+res.getNumChild()));

    	return p;
    }
    
    /*
     * create a table, containing rates and costs values to be inserted in the pdf invoice
     */
    public static PdfPTable getTable(Reserve res){
    	DecimalFormat df = new DecimalFormat("0.00");
    	float[] colsWidth ={2f, 1f};
    	
    	PdfPTable table = new PdfPTable(colsWidth); 
    	PdfPCell tableCell=new PdfPCell();
    	table.setWidthPercentage(90);
    	table.setHorizontalAlignment(Element.ALIGN_LEFT);
    	table.setSpacingBefore(30f);

    	
		table.addCell("Cost of accommodation at T$" + res.getRoomRate()+ " per night: ");
		tableCell=new PdfPCell(new Phrase(df.format(res.getCosts().getTotAccom())));
		tableCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(tableCell);
		
		table.addCell("Cost of Adult Meals at T$" + res.getMealAdult() + " per adult per day: ");
		tableCell=new PdfPCell(new Phrase(df.format(res.getCosts().getTotAdMeals())));
		tableCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(tableCell);
		
		if (res.getNumChild()!=0){
			table.addCell("Cost of Child Meals at T$" + res.getMealChild() + " per child per day: ");
			tableCell=new PdfPCell(new Phrase(df.format(res.getCosts().getTotChMeals())));
			tableCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(tableCell);
		}
		
		table.addCell("Cost of transfers: " );
		tableCell=new PdfPCell(new Phrase(df.format(res.getTransfer())));
		tableCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(tableCell);
		
		table.addCell("Total for Booking: ");
		tableCell=new PdfPCell(new Phrase(df.format(res.getCosts().getTotal())));
		tableCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(tableCell);
		
		table.addCell("30% Deposit Due: ");
		tableCell=new PdfPCell(new Phrase(df.format(res.getCosts().getDepositDue())));
		tableCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(tableCell);
		
		table.addCell("Balance Due: ");
		tableCell=new PdfPCell(new Phrase(df.format(res.getCosts().getBalance())));
		tableCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(tableCell);
		
    	return table;
    }
    
    
    /**
     * method that takes in a byte array and writes it to a file
     * this method is used for testing purposes only, it is not used by the application
     * a similar method is instead in the pdfdownloaded.html view
     * @param byteArray
     */
     public void testDownload(byte[] byteArray){
     	FileOutputStream out = null;
     	try {
     		String filePath = "/euaiki_offline_copy/testinvoice.txt";
     		out = new FileOutputStream(filePath);
     		out.write(byteArray);
     	}
     	catch ( IOException e) {
 		// TODO Auto-generated catch block
 		e.printStackTrace();
     	}
     	finally{
     		try {
 				out.flush();
 				out.close();
 				} catch (IOException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
     	}
 			
     }
 

}