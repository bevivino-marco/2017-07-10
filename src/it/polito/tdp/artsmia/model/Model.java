package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	// definizione grafi;
	private SimpleWeightedGraph<Integer, DefaultWeightedEdge> grafo;
	// idMap e liste;
	private List <ArtObject> oggetti;
	private List <Ex_Obj> relazioni;
	// dao;
	ArtsmiaDAO dao ;
	private Map<Integer, Integer> backVisit;
	private int LUN;
	private String classification;
public Model() {
	dao = new ArtsmiaDAO();
}
public void creaGrafo() {
	// creo il grafo;
	oggetti = new LinkedList <ArtObject>(dao.listObjects());
	relazioni = new LinkedList <Ex_Obj>(dao.getRelazioni());
	grafo = new SimpleWeightedGraph<Integer,DefaultWeightedEdge> (DefaultWeightedEdge.class);
	for (ArtObject o : oggetti) {
		grafo.addVertex(o.getId());
	}
		// definisco i veritici;
//	grafo.addVertex(i);
//	Graphs.addAllVertices(arg0, arg1);
	for (Ex_Obj r : relazioni) {
		if (!grafo.containsEdge(r.getO2(), r.getO1())) {
			
			Graphs.addEdgeWithVertices(grafo , r.getO1(), r.getO2(), r.getCont());
		}
		   
		
	}
	
	// creo gli archi;
//  grafo.addEdge(e1,e2);
//	Graphs.addEdge(g, sourceVertex, targetVertex, weight);
//  Graphs.addEdgeWithVertices(grafo , partenza, arrivo, peso)
//	grafo.setEdgeWeight(partenza, arrivo, peso);
	

	
	// valori
	System.out.println("N. vertici : "+grafo.vertexSet().size());
	System.out.println("N. archi : "+grafo.edgeSet().size());

}
public List<ArtObject> getOggetti() {
	return oggetti;
}

public int componenteConnessa(int id) {
	List <Integer> result = new ArrayList<Integer>();
	backVisit = new HashMap<>();
	GraphIterator <Integer, DefaultWeightedEdge> it = new DepthFirstIterator<>(this.grafo, id);
	//GraphIterator <Fermata, DefaultEdge> it = new BreadthFirstIterator<>(this.grafo, source);
    backVisit.put(id, null); // la radice non ha un padre
	/*//it.addTraversalListener(new Model.EdgeTraversedGraphListener()); non usare
	*/
	 
	it.addTraversalListener(new TraversalListener<Integer, DefaultWeightedEdge>() {

		@Override
		public void connectedComponentFinished(ConnectedComponentTraversalEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void connectedComponentStarted(ConnectedComponentTraversalEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void edgeTraversed(EdgeTraversalEvent<DefaultWeightedEdge> ev) {
			Integer sourceVertex  = grafo.getEdgeSource(ev.getEdge());
			Integer targetVertex = grafo.getEdgeTarget(ev.getEdge());
		
			if (!backVisit.containsKey(targetVertex) && backVisit.containsKey(sourceVertex)) {
				backVisit.put(targetVertex, sourceVertex);
			}else if (!backVisit.containsKey(sourceVertex) && backVisit.containsKey(targetVertex)) {
				backVisit.put(sourceVertex, targetVertex);
			}			
		}

		@Override
		public void vertexFinished(VertexTraversalEvent<Integer> arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void vertexTraversed(VertexTraversalEvent<Integer> arg0) {
			// TODO Auto-generated method stub
			
		}

	});
	
	
	while (it.hasNext()) {
		result.add(it.next());
	}
	System.out.println(backVisit);
	return result.size();
}


public List <Integer> camminoMax (int id, int lun){
	ArtObject ao=null ;
	for (ArtObject a : oggetti) {
		if (a.getId()==id)
			ao=a;
	}
	classification = ao.getClassification();
	int start = id;
	LUN = lun;
	List<Integer> result = new LinkedList<>();
	List<Integer> parziale = new LinkedList<>();
	parziale.add(start);
	cerca (start, 1 , result, parziale);
	
	return result;
}



public void cerca (int start, int i, List<Integer> result, List<Integer> parziale) {
	List <Integer> vicini = Graphs.neighborListOf(grafo, start);
	if (vicini.isEmpty()) {
		return;
	}
	if (i==LUN) {
		if (result.size()==0 || confrontaCammini(parziale, result)>0) {
			result.clear();
			result.addAll(parziale);
			System.out.println(parziale.toString());
			return;
		}
	}
	
	
	for (Integer o : vicini) {
		if (parziale.size()<LUN && !parziale.contains(o) && (result.size()==0 || valido(o))) {
			parziale.add(o);
			System.out.println(parziale.toString());
			cerca(o, i+1, result, parziale);
			parziale.remove(o);
		}
	}
	
	
}
private int confrontaCammini(List<Integer> parziale, List<Integer> result) {
	int cP=0;
	int cR=0;
	for (int p : parziale) {
		if (p!=parziale.get(parziale.size()-1)) {
		int p1 = parziale.get(parziale.indexOf(p)+1);
		cP+= grafo.getEdgeWeight(grafo.getEdge(p, p1));
		
		}
		
	}
	for (int p : result) {
		if (p!=result.get(result.size()-1)) {
		int p1 = result.get(result.indexOf(p)+1);
		cR+= grafo.getEdgeWeight(grafo.getEdge(p, p1));
		
		}
		
	}
	return cP-cR;
}
private boolean valido(Integer o) {
	ArtObject ao=null ;
	for (ArtObject a : oggetti) {
		if (a.getId()==o)
			ao=a;
	}
	if (ao.getClassification().equals(classification))
		return true;
	return false;
}
	
	
	
	
	
	
	
}










