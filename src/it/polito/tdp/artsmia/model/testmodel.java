package it.polito.tdp.artsmia.model;

public class testmodel {

	public static void main(String[] args) {
		Model m = new Model();
		m.creaGrafo();
		System.out.println(m.componenteConnessa(44));
		System.out.println(m.camminoMax(44, 3));

	}

}
