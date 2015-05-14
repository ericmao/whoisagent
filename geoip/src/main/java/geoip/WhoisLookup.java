package geoip;

import java.io.File;

import org.apache.commons.net.whois.WhoisClient;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;


public class WhoisLookup {

	public static final String WHOIS_SERVER = "whois.cymru.com";
	public static final int WHOIS_PORT = 43;

	public static void main(String[] args) throws Exception {

		String nameToQuery = "216.90.108.31";
		WhoisClient whoisClient = new WhoisClient();
		whoisClient.connect(WHOIS_SERVER, WHOIS_PORT);
		String results = whoisClient.query(nameToQuery);

		System.out.println(results.split("\\|")[4].trim());
		
		File file = new File("geoipdb/GeoLiteCity.dat");
		LookupService lookup = new LookupService(file,LookupService.GEOIP_MEMORY_CACHE);
		Location locationServices = lookup.getLocation(nameToQuery);
		System.out.println(locationServices.countryName);
	}
}

