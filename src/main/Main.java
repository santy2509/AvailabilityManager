package main;

import java.net.URL;

import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;

import recoveryManager.PingManager;
import recoveryManager.Snapshot;


public class Main 
{
	public static void main(String ar[]) throws Exception
	{
		ServiceInstance si = new ServiceInstance(new URL("https://130.65.132.116/sdk"), "administrator", "12!@qwQW", true);
		Folder rootFolder = si.getRootFolder();
		ManagedEntity[] hosts = new InventoryNavigator(rootFolder).searchManagedEntities(new String[][] { {"HostSystem", "name" }, }, true);
		
		
		/*thread for snapshot
		 * 
		 */
		Snapshot snapshot=new Snapshot();
		snapshot.start();
		
		/*
		 * thread for ping and so do recoverymanager
		 */
		for(int i=0;i<hosts.length;i++)
		{
			String ip=hosts[i].getName();
			HostSystem h=(HostSystem)hosts[i];
			PingManager pingmgr=new PingManager(h, ip);
			pingmgr.start();
		}		
	}
}
