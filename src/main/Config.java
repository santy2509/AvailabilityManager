package main;

import java.net.URL;

import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.ServiceInstance;

public class Config
{
	static String VcenterUrlAdmin="130.65.132.14";
	static String VcenterUrl="130.65.132.116";
	static String Password="12!@qwQW";
	static String VcenterUser="administrator";
	static String VhostUser="root";
	
	public static ServiceInstance getvcentersi() throws Exception
	{
		ServiceInstance si = new ServiceInstance(new URL("https://"+VcenterUrl+"/sdk"), VcenterUser, Password, true);
		
		return si;
	}
	
	public static ServiceInstance getvhostsi(String VhostUrl) throws Exception
	{
		ServiceInstance si = new ServiceInstance(new URL("https://"+VhostUrl+"/sdk"), VhostUser, Password, true);
		
		
		return si;
	}
}
