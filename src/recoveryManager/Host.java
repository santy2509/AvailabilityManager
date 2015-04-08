package recoveryManager;


import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import com.vmware.vim25.GuestInfo;
import com.vmware.vim25.VirtualMachineRuntimeInfo;
import com.vmware.vim25.mo.Folder;
//import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;

public class Host 
{
	public String gethoststate(String host) throws RemoteException, MalformedURLException
	{
		String vhostnm="T16-vHost-"+host;
		
		ServiceInstance si = new ServiceInstance(new URL("https://130.65.132.14/sdk"), "administrator", "12!@qwQW", true);
		Folder rootFolder = si.getRootFolder();
		
		ManagedEntity[] vms = new InventoryNavigator(rootFolder).searchManagedEntities(new String[][] { {"VirtualMachine", "name" }, }, true);
		
		for(int i=0; i<vms.length; i++)
		{
			VirtualMachine m=(VirtualMachine) vms[i];
		
			if(vms[i].getName().equals(vhostnm))
			{
				VirtualMachineRuntimeInfo vmri = (VirtualMachineRuntimeInfo) m.getRuntime();
				
				String state=vmri.getPowerState().toString();
				return state;
			}
			
		}
		
		return "unknown";
	}

}
