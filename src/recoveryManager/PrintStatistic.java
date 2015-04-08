package recoveryManager;


import java.net.URL;
import com.vmware.vim25.VirtualMachineQuickStats;
import com.vmware.vim25.VirtualMachineRuntimeInfo;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

public class PrintStatistic 
{
	
	public void print() throws Exception
	{
		ServiceInstance si = new ServiceInstance(new URL("https://130.65.132.116/sdk"), "administrator", "12!@qwQW", true);
		Folder rootFolder = si.getRootFolder();
		ManagedEntity[] hosts = new InventoryNavigator(rootFolder).searchManagedEntities(
				new String[][] { {"HostSystem", "name" }, }, true);
		
		for(int i=0; i<hosts.length; i++)
		{
			HostSystem hs = (HostSystem) new InventoryNavigator(rootFolder).searchManagedEntity("HostSystem", hosts[i].getName());
			hs.getHostAutoStartManager();
			System.out.println(hosts[i].getValues());
			System.out.println("host["+i+"]=" + hosts[i].getName());
		}
		
		ManagedEntity[] vms = new InventoryNavigator(rootFolder).searchManagedEntities(
				new String[][] { {"VirtualMachine", "name" }, }, true);
		
		for(int i=0;i<vms.length;i++)
		{	
			VirtualMachine vm=(VirtualMachine)vms[i];
			System.out.println("\nName: " + vm.getName());
			System.out.println("Guest OS: "	+ vm.getSummary().getConfig().guestFullName);
			System.out.println("VM Version: " + vm.getConfig().version);
			System.out.println("CPU: " + vm.getConfig().getHardware().numCPU + " vCPU");
			System.out.println("Memory: " + vm.getConfig().getHardware().memoryMB+ " MB");
			System.out.println("IP Addresses: " + vm.getGuest().getIpAddress());
			System.out.println("State: " + vm.getGuest().guestState);

			if (!vm.getGuest().guestState.equals("notRunning")) 
			{
			
				System.out.println("Data from VirtualMachineQuickStats: ");
				VirtualMachineQuickStats qs = vm.getSummary().getQuickStats();
				System.out.println(String.format("%-25s%s", "OverallCpuUsage: ",qs.getOverallCpuUsage() + " MHz"));
				System.out.println(String.format("%-25s%s", "GuestMemoryUsage: ",qs.getGuestMemoryUsage() + " MB"));
				System.out.println(String.format("%-25s%s","ConsumedOverheadMemory: ", qs.getConsumedOverheadMemory()+ " MB"));
				System.out.println(String.format("%-25s%s", "FtLatencyStatus: ",qs.getFtLatencyStatus()));
				System.out.println(String.format("%-25s%s","GuestHeartbeatStatus: ", qs.getGuestHeartbeatStatus()));
		
		
	}
		}
	}
}
