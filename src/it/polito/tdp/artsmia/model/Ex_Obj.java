package it.polito.tdp.artsmia.model;

public class Ex_Obj {
	private int o1;
	private int o2;
	
	private int cont;
	public Ex_Obj(int o1, int o2,  int cont) {
		super();
		this.o1 = o1;
		this.o2 = o2;
		
		this.cont = cont;
	}
	public int getO1() {
		return o1;
	}
	public void setO1(int o1) {
		this.o1 = o1;
	}
	public int getO2() {
		return o2;
	}
	public void setO2(int o2) {
		this.o2 = o2;
	}
	
	public int getCont() {
		return cont;
	}
	public void setCont(int cont) {
		this.cont = cont;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + o1;
		result = prime * result + o2;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ex_Obj other = (Ex_Obj) obj;
		if (o1 != other.o1)
			return false;
		if (o2 != other.o2)
			return false;
		return true;
	}
	
	

}
