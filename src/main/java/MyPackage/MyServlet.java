package MyPackage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class MyServlet
 */
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String HttpURLConnection = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		Api setup
		String apiKey="8c75d85ce12d7fe71bc043fa9d35e177";
//		get city from input
		String city=request.getParameter("city");
		
//		create url for open weather api
		String apiURL="https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+apiKey;

		//		Api intergration
		URI uri = null;
		try {
			uri = new URI(apiURL);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        URL url = uri.toURL();
	
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        
//        reading the data from network
        InputStream inputStream=connection.getInputStream();
        InputStreamReader reader =new InputStreamReader(inputStream);
        
//        want to store in string
        StringBuilder responseContent= new StringBuilder();
        
//        input lene k liye from reader , will create scanner object
        Scanner sc=new Scanner(reader);
        
        while(sc.hasNext()) {
        	responseContent.append(sc.nextLine());
        }
        sc.close();
//        parsing data int json from string
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(responseContent.toString(),JsonObject.class);
      
        
//        date/time
        long dateTimeStamp= jsonObject.get("dt").getAsLong()*1000;
        String date =new Date(dateTimeStamp).toString();
        
//        temperature
        double temperaturekelvin =jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
        int temperatureCelcius =(int)(temperaturekelvin-273.15);
        
//        humidity
        int humidity=jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
        
//        Wind speed
        double windSpeed=jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
        
//        weather conditions
        String weatherCondition =jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
        
//        set the data as request attributes
        request.setAttribute("date", date);
        request.setAttribute("city", city);
        request.setAttribute("temperature", temperatureCelcius);
        request.setAttribute("weatherCondition", weatherCondition);
        request.setAttribute("windSpeed", windSpeed);
        request.setAttribute("weatherData", responseContent.toString());
        
        connection.disconnect();
        
//        forward the page to weather jsp page
        request.getRequestDispatcher("index.jsp").forward(request, response);
	}

}
