package recoveryManager;


import java.net.URL;

import com.vmware.vim25.VirtualMachinePowerState;
import com.vmware.vim25.VirtualMachineRuntimeInfo;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;

public class VM 
{
	
	public void Poweron(VirtualMachine vm) throws Exception
	{
			
		System.out.println((vm.getName()));
		VirtualMachineRuntimeInfo vmri = (VirtualMachineRuntimeInfo) vm.getRuntime();
						
		if(vmri.getPowerState() == VirtualMachinePowerState.poweredOff)
		{
			Task t=vm.powerOnVM_Task(null);
			t.waitForTask();
			System.out.println("vm:" + vm.getName() + " powered on");
		}
		
	}

	public boolean poweronhost(String host)
	{
		try{
		
			String vhostnm="T16-vHost-"+host;
			//System.out.println("vhostname:" + vhostnm);
			ServiceInstance si = new ServiceInstance(new URL("https://130.65.132.14/sdk"), "administrator", "12!@qwQW", true);
			Folder rootFolder = si.getRootFolder();
			
			ManagedEntity[] vms = new InventoryNavigator(rootFolder).searchManagedEntities(new String[][] { {"VirtualMachine", "name" }, }, true);
			for(int i=0; i<vms.length; i++)
			{
				VirtualMachine m=(VirtualMachine) vms[i];
			
				if(vms[i].getName().equals(vhostnm))
				{
					System.out.println("task to power on starts");
					Task t=m.powerOnVM_Task(null);
					System.out.println("Vhost is on:" + m.getName());
					t.waitForTask();
					return true;
				}
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		
		}
		return false;
		
		
	}

}
