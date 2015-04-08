package recoveryManager;


import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.VirtualMachine;

public class PingManager extends Thread
{
	Snapshot snapmgr=new Snapshot();
	VM vm=new VM();
	Host h=new Host();
	HostSystem host;
	String ip;
	
	public PingManager(HostSystem h, String ip) 
	{
		this.host=h;
		this.ip=ip;
	}

	public void run()
	{
		while(true)
		{
		try{

			if(ping(ip))
			{
			System.out.println("vhost is alive" + ip);
			VirtualMachine[] v=host.getVms();
			
				for(int j=0;j<v.length;j++)
				{
					if(ping(v[j].getGuest().getIpAddress()) && v[j].getGuest().guestState=="powerOn")
					{
						System.out.println(v[j].getName() + "vm is on working fine");
					}
					else if(!ping(v[j].getGuest().getIpAddress()) && v[j].getGuest().guestState=="powerOn")
					{
						//to do check nic
						//if nic is on then revert it
						snapmgr.revertsnapshotforvm(v[j]);
						vm.Poweron(v[j]);
					}
					else if(!ping(v[j].getGuest().getIpAddress()) && v[j].getGuest().guestState=="powerOff")
					{
						//to do alarm section
						//poweron 
					}
					else
					{
						System.out.println("Worst case scenario");
						//revert snapshot and poweron machine
						snapmgr.revertsnapshotforvm(v[j]);
						vm.Poweron(v[j]);
						
					}
				}
			}
			else
			{
				if(h.gethoststate(ip)=="poweredOff")
				{
					System.out.println("host is off trying to make it on");
					if(vm.poweronhost(ip))
					{
						System.out.println("Vm is on sucessfully");
						VirtualMachine[] vms=host.getVms();
						while(!ping(vms[0].getGuest().getIpAddress()))
						{
							if(vms[0].getGuest().getIpAddress()==null)
							{
								System.out.println("waiting for machine to start and thread is going to sleep for 10 sec");
								
								Thread.sleep(10000);
							}
							else
							{
								//System.out.println("ip address is : " + vms[0].getGuest().getIpAddress());
								System.out.println("Waiting for vhost to connect to MyVcenter");
								vm.Poweron(vms[0]);
								System.out.println("thread is going to sleep for 10 sec");
								Thread.sleep(1000);
							}
						
						}
					
					}
					else
					{
						
						System.out.println("unable to power it on");
						//find another vhost and migrate it on 
					}
				}
				else
				{
					//check nic
					System.out.println("checking nic");
					snapmgr.revertsnapshotforhost(host);
					
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		}
		
	}

	public static boolean ping(String ip)
	{
		//System.out.println("inside ping method");
		 String strCommand;
		
			  strCommand = "ping "+ ip;
			  Process myProcess;
			  if(ip==null)
			  {
				  return false;
			  }
			try {
				myProcess = Runtime.getRuntime().exec(strCommand);
				myProcess.waitFor();
		        if(myProcess.exitValue() == 0) 
		        {
		        	//System.out.println("ping from machine is coming" + ip);
		        	//System.out.println("============ ping success ============");
		        	return true;
		        	
		        } else {
		        	//System.out.println("============ ping failure ============" + ip);
		        	return false;
		        }
			} catch (Exception e) {
				
				e.printStackTrace();
				return false;
			} 
		
		
		
	}
}
