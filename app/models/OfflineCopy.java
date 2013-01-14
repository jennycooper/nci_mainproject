package models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
 
public class OfflineCopy {
	
	//method to download a copy of the reservations calendar to the user's computer
	public static void downloadCopy(String year) {
 
		URL url;
		BufferedReader br = null;	
		BufferedWriter bw = null;
		
		try {
				// get URL content
				url = new URL("https://damp-ravine-8097.herokuapp.com/download?year="+year);
				URLConnection conn = url.openConnection();
				// open the stream to the url and put it into BufferedReader
				br = new BufferedReader(
	                               new InputStreamReader(conn.getInputStream()));
				String inputLine;
				//create a new directory (if it doesn't already exist) to store the offline copy
				File dir = new File("/euaiki_offline_calendar");
				if (!dir.exists()) {
					dir.mkdir();
				}
				//save to this filename
				File file = new File("/euaiki_offline_calendar/calendar_"+year+".html");
				if (!file.exists()) {
					file.createNewFile();
				}
				
				//use FileWriter to write file
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				
				//write today's date to the start of the file
				DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
				// Get the date today using Calendar object.
				Date today = Calendar.getInstance().getTime();        
				String downloadDate = df.format(today);
				String data =("<h2>" + "Copy downloaded on: "+ downloadDate + "</h2>");
				bw.write(data);
	
				while ((inputLine = br.readLine()) != null) {
					bw.write(inputLine);				
				}

 
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
			finally{
				try {
					br.close();
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
 
	}
}
