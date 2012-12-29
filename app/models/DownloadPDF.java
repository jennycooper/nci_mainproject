/*
 * Written By: Jenny Cooper
 *Description: Takes a reservation and writes it to a pdf (to folder euaiki_offline on user's computer)
 *
 */
 
package models;
 

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

 
import com.itextpdf.text.BadElementException;
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

    /**
     * Creates a PDF file: (NameOfGuest).pdf
     * @param    args    reservation
     */
    public static void createPDF(Reserve res){
    	//call methods to calculate the costs of the reservation
    	res.calcValues();
    	res.getCosts().addTotals(res);
    	
    	Image logo = null;
    	Document document = new Document();
    	//calculate room costs and add to reservation
    	res.calcValues();
    	res.getCosts().addTotals(res);
    	
    	//create a new directory (if it doens't already exist) to store the pdf copies
		File dir = new File("/euaiki_offline_calendar");
		if (!dir.exists()) {
			dir.mkdir();
		}

		/** Path to the resulting PDF file. */
	    String filePath = "/euaiki_offline_calendar/"+res.getMyGuest().getName()+".pdf";
	    //create a new Document and convert it to a pdf instance
        
        try {
			PdfWriter.getInstance(document, new FileOutputStream(filePath));
			document.open();
			
			//add the logo to the pdf
			logo = Image.getInstance("C:/play-2.0.4/firstApp/public/images/banner.jpg");
			logo.scaleAbsolute(200, 100);
	        logo.setAbsolutePosition(350, 700);
	        document.add(logo);
	        // add paragraphs of details to the pdf
	        ArrayList<Paragraph> p = getDetails(res);
	        for (int i=0;i<p.size();i++){
	        	document.add(p.get(i));
	        }
	        //add a table of costs to the pdf
	        document.add(getTable(res));
	        
		    } catch (BadElementException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		
        finally{
        	document.close();
        }
    }
    
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
 

}