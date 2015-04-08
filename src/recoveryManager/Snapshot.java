package recoveryManager;



import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import com.vmware.vim25.InsufficientResourcesFault;
import com.vmware.vim25.InvalidState;
import com.vmware.vim25.NotFound;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.SnapshotFault;
import com.vmware.vim25.TaskInProgress;
import com.vmware.vim25.VmConfigFault;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;

public class Snapshot extends Thread
{
	
	
	public void snapshotforvm(VirtualMachine v) throws Exception
	{
		
		String snapshotname=v.getName()+"Snapshot";
		String desc="snapshot for vm" + v.getName() + "at time" + System.currentTimeMillis();
		System.out.println("Creating Snapshot for vm: "+v.getName());
	
		Task t1=v.removeAllSnapshots_Task();
		t1.waitForTask();
		System.out.println("t1 is" + t1);
		Task t=v.createSnapshot_Task(snapshotname, desc, false, true);
		t.waitForTask();
		System.out.println("t is" + t);
		
		System.out.println("vm:" + v.getName() + "has snapshot" + snapshotname);
		
		
	}
	
	public void run()
	{
		while(true)
		{
			try
			{
				ServiceInstance si = new ServiceInstance(new URL("https://130.65.132.116/sdk"), "administrator", "12!@qwQW", true);
				Folder rootFolder = si.getRootFolder();
	
				ManagedEntity[] vms = new InventoryNavigator(rootFolder).searchManagedEntities(new String[][] { {"VirtualMachine", "name" }, }, true);
				ManagedEntity[] hosts = new InventoryNavigator(rootFolder).searchManagedEntities(new String[][] { {"HostSystem", "name" }, }, true);
				for(int i=0;i<vms.length;i++)
				{
					
					VirtualMachine v= (VirtualMachine)vms[i];
					snapshotforvm(v);
			
				}
				for(int j=0;j<hosts.length;j++)
				{
					HostSystem h=(HostSystem) hosts[j];
					snapshotforhost(h);
				}
				Thread.sleep(600000);
				System.out.println("Thread goes to sleep for 10 min");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	
	
	}

	
	public void snapshotforhost(HostSystem h) throws RemoteException, MalformedURLException, InterruptedException
	{
		
		String ip=h.getName();
		String vhostnm="T16-vHost-"+ip;
		ServiceInstance si = new ServiceInstance(new URL("https://130.65.132.14/sdk"), "administrator", "12!@qwQW", true);
		Folder rootFolder = si.getRootFolder();
		
		ManagedEntity[] vms = new InventoryNavigator(rootFolder).searchManagedEntities(new String[][] { {"VirtualMachine", "name" }, }, true);
		for(int i=0; i<vms.length; i++)
		{
			VirtualMachine m=(VirtualMachine) vms[i];
		
			if(vms[i].getName().equals(vhostnm))
			{
				String snapshotname=m.getName()+"Snapshot";
				String desc="snapshot for vm" + m.getName() + "at time" + System.currentTimeMillis();
				System.out.println("Creating Snapshot for vm: "+m.getName());
			
				Task t1=m.removeAllSnapshots_Task();
				t1.waitForTask();
				System.out.println("t1 is" + t1);
				Task t=m.createSnapshot_Task(snapshotname, desc, false, true);
				t.waitForTask();
				System.out.println("t is" + t);
				
				System.out.println("vm:" + m.getName() + "has snapshot" + snapshotname);
			}
			
		}
	}
	
	public void revertsnapshotforvm(VirtualMachine v) throws Exception
	{
		System.out.println("reverting snapshot to vm has strated");
		v.revertToCurrentSnapshot_Task(null);
		
	}
	
	public void revertsnapshotforhost(HostSystem h) throws Exception 
	{
		String ip=h.getName();
		String vhostnm="T16-vHost-"+ip;
		ServiceInstance si = new ServiceInstance(new URL("https://130.65.132.14/sdk"), "administrator", "12!@qwQW", true);
		Folder rootFolder = si.getRootFolder();
		
		ManagedEntity[] vms = new InventoryNavigator(rootFolder).searchManagedEntities(new String[][] { {"VirtualMachine", "name" }, }, true);
		for(int i=0; i<vms.length; i++)
		{
			VirtualMachine m=(VirtualMachine) vms[i];
		
			if(vms[i].getName().equals(vhostnm))
			{
				m.revertToCurrentSnapshot_Task(null);
			}
			
		}
		
	}

}
