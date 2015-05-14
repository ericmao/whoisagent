package geoip;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.SocketException;
import java.util.HashSet;

import org.apache.commons.net.whois.WhoisClient;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;

/**
 * @author ericmao
 * */
public class FindingWhois {
	public static final String WHOIS_SERVER = "whois.cymru.com";
	public static final int WHOIS_PORT = 43;
	
	// please modify following address of file
	public static final String srcFolder = "/Users/ericmao/Downloads/new";
	public static final String outputFile4NC = "data/iplist4NC.txt";
	public static final String outputFile4result = "data/iplist4result.txt";
	
	public static void main(String[] args) throws IOException {
		HashSet<String> uniqueIP = new HashSet<String>();
		
		File folder = new File(srcFolder);
		File[] files=folder.listFiles();
		System.out.println(files.length);
		for(File file:files){
			BufferedReader br = new BufferedReader(new FileReader(file));
			String temp="";
			while((temp=br.readLine())!=null){
				uniqueIP.add(temp.split("\t")[0]);
			}
			System.out.println(uniqueIP.size());
		}
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile4NC));		
		BufferedWriter bw2 = new BufferedWriter(new FileWriter(outputFile4result));		

		bw.write("begin\n");
		bw.write("verbose\n");
		int count = 0;
		for(String ip:uniqueIP){
			ip = ip.trim();
			if(ip=="") continue;
			try{
				bw2.write(ip+","+getCountry(ip)+"\n");
			}catch(Exception e){
				continue;
			}
			if(count%10000==0) System.out.println(count);
			count++;
		}
		bw.write("end\n");
		bw2.close();
		bw.close();
		
	}
	
	/**
	 * by geoip
	 * @param query IP
	 * @return city of the query IP
	 * */
	public static String getCity(String ip) throws IOException{
		File file = new File("geoipdb/GeoLiteCity.dat");
		LookupService lookup = new LookupService(file,LookupService.GEOIP_MEMORY_CACHE);
		Location locationServices = lookup.getLocation(ip);
		return locationServices.city;
	}
	
	/**
	 * by geoip
	 * @param query IP
	 * @return country of the query IP
	 * */
	public static String getCountry(String ip) throws IOException{
		File file = new File("geoipdb/GeoLiteCity.dat");
		LookupService lookup = new LookupService(file,LookupService.GEOIP_MEMORY_CACHE);
		Location locationServices = lookup.getLocation(ip);
		return locationServices.countryName;
	}
	
	/**
	 * @deprecated very slow
	 * @param query IP
	 * @return ISP of the query IP
	 * */
	public static String getISP(String ip) throws SocketException, IOException{
		WhoisClient whoisClient = new WhoisClient();
		whoisClient.connect(WHOIS_SERVER, WHOIS_PORT);
		String results = whoisClient.query(ip);
		return results.split("\\|")[4].trim();
	}
}
