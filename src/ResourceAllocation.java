import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResourceAllocation {
	static ArrayList<Resource> Resources;
	static ArrayList<Subtask> Subtasks;
	static Map<Subtask,ArrayList<Resource>> AllocationK; 
	static Map<Resource,ArrayList<Subtask>> AllocationR;
	public double computeUtility(double[][] b,int wt, int[][] turnaroundTime, int we, int[][] expense,int i)
	{
		double maxTT = turnaroundTime[i][0];
		double totalExp = expense[i][0];
		for(int j=1;j<Resources.size();j++)
		{
			double allocTT = (b[i][j])*(turnaroundTime[i][j]);
			if(maxTT<allocTT)
				maxTT = allocTT;
			totalExp+=(b[i][j])*(expense[i][j]);			
		}
		double cost = (we*totalExp)+(wt*maxTT);
		double utility = 1.0/cost;
		return utility;
	}
	
	public int[][] reallocate(int[][] b,int i,int p, int q)
	{
		int[][] c = b;
		c[i][p] = 0;
		c[i][q] = 1;
		return c;
	}

	public double computeSPELR(int[][] b,int i,int p,int q)
	{
		int[][] c;
		double utility1 = computeUtility(b, wt, turnaroundTime, we, expense, i);
		c = reallocate(b,i,p,q);
		double utility2 = computeUtility(c, wt, turnaroundTime, we, expense, i);
		double SPELR = utility1 - utility2;
		return SPELR;
	}
	
	public double computeGELR(int[][] b,int[][] c,ArrayList<Subtask> subtasks)
	{
		double utility1,utility2;
		utility1 = utility2 = 0;
		for(int k=0;k<subtasks.size();k++)
		{
			utility1 += computeUtility(b, wt, turnaroundTime, we, expense, k);
			utility2 += computeUtility(c, wt, turnaroundTime, we, expense, k);			
		}
		return (utility1-utility2);
	}
	
	public int MinSingle(int[][] b, int i,int p)
	{
		double currentSPELR,minSPELR;
		minSPELR = 0;
		int q= -1;
		for(int j=0;j<Resources.size();j++)
		{
			if(j!=p)
			{
				currentSPELR = computeSPELR(b, i, p, j);
				if(minSPELR==0)
					minSPELR = currentSPELR;
				else if(minSPELR>currentSPELR)
				{
					minSPELR = currentSPELR;
					if(minSPELR<0)
					 q = j;
				}
				
			}			
		}
		return q;
	}
	
	public int  MinGlobal(int[][] b,int j)
	{
		ArrayList<Subtask> subtasks = AllocationR.get(Resources.get(j));
		ArrayList<Integer> nsts;
		int i;
		double[] GELR;
		int q = -1;	int[][] c;
		for(int k=0;k<subtasks.size();k++)
		{
			q = MinSingle(b, k, j);
			if(q!=-1)
			{
				double utility1 = computeUtility(b, wt, turnaroundTime, we, expense, k);
				c = reallocate(b, k, j, q);
				double utility2 = computeUtility(c, wt, turnaroundTime, we, expense, k);
				if(utility1-utility2<0)
					nsts.add(k);
				GELR[k] = computeGELR(b,c,subtasks);
			}
		}
		if(nsts==null)
		 i = -1;
		else
		{
			double minGELR = 0;
			for(int n=0;n<nsts.size();n++)
			{
				if(minGELR == 0)
				{ 
					minGELR = GELR[nsts.get(n)];
					if(minGELR<0)
						i = n;
				}
				else if(minGELR>GELR[nsts.get(n)])
				{
					minGELR = GELR[nsts.get(n)];
					if(minGELR<0)
						i = n;
				}
			}
		}
		return i;		
	}
	
	
	public double EvolutionaryOptimize(int[][] b)
	{
		int i=1;
		Boolean flag = true;
		ArrayList<Resource> ms;
		int q=-1;
		int p=-1;
		while(flag)
		{
			if(i==1)
				flag = false;
			ms = AllocationK.get((Subtasks.get(i)));		
			for(int j=0;j<ms.size();j++)
			{
				//sort bi based on ti 
				if(b[i][j]==1)
				{
					q = MinGlobal(b, j);
				}
				if(q!=-1)
				{
					p = MinSingle(b, q, j);
					b = reallocate(b, q, j, p);
					flag = true;
				}				
			}
			if(i==Subtasks.size())
			{
				if(!flag)
					break;
				else
					i=1;
			}
			else
				i+=1;
		}		
	}
	
	public static void main(String[] args)
	{
		double wt,we;
		wt=we = 0.5;
		int n,m;
		n=m=0;
		Subtasks = new ArrayList<Subtask>();
		Resources = new ArrayList<Resource>();
		AllocationK = new HashMap<Subtask,ArrayList<Resource>>();
		AllocationR = new HashMap<Resource,ArrayList<Subtask>>();
		System.out.println("Enter number of tasks and resources.");
		try {
			n = System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			m = System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0;i<n;i++)
		{
			Subtask t = new Subtask();
			Subtasks.add(t);
		}
		for(int j=0;j<m;j++)
		{
			Resource r = new  Resource();
			r.setID(j+1);
			r.rt = ResourceType.CPU;
		}
		for(int i=0;i<n;i++)
		{
			System.out.println("Allocate Resource for subtask "+(i+1));
			for(int j=0;j<m;j++)
			{
				int in = System.in.read(); 
				
			}
			AllocationK.put((i+1), value)				
		}
		}
	}
}
