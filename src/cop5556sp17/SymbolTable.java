package cop5556sp17;

import cop5556sp17.AST.Dec;
import java.util.*;

public class SymbolTable {
	
	//TODO  add fields

	/** 
	 * to be called when block entered
	 */
	public class Ident_value
	{
		int scope_ident;
		Dec dec;
		public Ident_value(Dec dec,int  i)
		{
			this.dec = dec;
			this.scope_ident =i;
		}
		public Ident_value()
		{
			dec = null;
			scope_ident = 0;
		}
	}
	int  current_scope, next_scope ;
	HashMap<String,ArrayList<Ident_value>> hash;
	ArrayList<Integer> scope_stack;
	public void enterScope(){
		//TODO:  IMPLEMENT THIS
		current_scope = next_scope++; 
		scope_stack.add(new Integer(current_scope));
	}
	
	
	/**
	 * leaves scope
	 */
	public void leaveScope(){
		//TODO:  IMPLEMENT THIS
		int k = scope_stack.size();
		scope_stack.remove(k-1);
		current_scope = scope_stack.get(scope_stack.size()-1);
	}
	
	public boolean insert(String ident, Dec dec){
		//TODO:  IMPLEMENT THIS
		ArrayList<Ident_value> hs = hash.get(ident);
		if(hs!=null)
		{
			int k=scope_stack.size()-1;
			for(int j=0; j<hs.size(); j++)
			{
				if(hs.get(j).scope_ident==current_scope)
					return false;
			}
		}
		
//		ArrayList<Ident_value> hs = hash.get(ident);
		if(hs == null)
		{
			hs = new ArrayList<Ident_value>();
			hash.put(ident, hs);
		}
		hs.add(new Ident_value(dec,current_scope));
		return true;
	}
	
	public Dec lookup(String ident){
		//TODO:  IMPLEMENT THIS
		ArrayList<Ident_value> hs = hash.get(ident);
		if (hs == null)
			return null;
		int k=scope_stack.size()-1;
		for(int i=k; i>=0; i--)
		{
			for(int j=0; j<hs.size(); j++)
			{
				if(hs.get(j).scope_ident==scope_stack.get(i))
					return hs.get(j).dec;
			}
		}
		return null;
	}
		
	public SymbolTable() {
		//TODO:  IMPLEMENT THIS
		hash = new HashMap<>();
		scope_stack = new ArrayList<Integer>();
		scope_stack.add(0);
		current_scope=0;
		next_scope=1;
	}


	@Override
	public String toString() {
		//TODO:  IMPLEMENT THIS
		return "";
	}
	
	


}
