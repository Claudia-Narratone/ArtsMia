package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private Graph<ArtObject, DefaultWeightedEdge> grafo;
	private Map<Integer, ArtObject> idMap;
	
	
	public Model() {
		idMap=new HashMap<Integer,ArtObject>();
	}

	public void creaGrafo() {
		this.grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		ArtsmiaDAO dao=new ArtsmiaDAO();
		
		dao.listObjects(idMap);
		//Aggiungo vertici
		Graphs.addAllVertices(this.grafo, idMap.values());
		
		//Aggiungo archi
		
		//APPROCCIO 1: doppio ciclo for sui vertici, dati due vertici controllo se sono collegati
		//fattibile solo se il numero di vertici è molto basso
		/*for(ArtObject a1:this.grafo.vertexSet()) {
			for(ArtObject a2:this.grafo.vertexSet()) {
				//devo collegare a1 e a2?
				//controllo se non esiste già l'arco
				int peso=dao.getPeso(a1, a2);
				if(peso>0) {
					if(!this.grafo.containsEdge(a1, a2)) {
						Graphs.addEdge(this.grafo, a1, a2, peso);
					}
				}
			}
		}*/
		
	
		//APPROCCIO 2: mi faccio dare dal db direttamente tutte le adiacenze
		for(Adiacenza a: dao.getAdiacenze()) {
			if(a.getPeso()>0) {
				Graphs.addEdge(this.grafo, idMap.get(a.getObj1()), idMap.get(a.getObj2()), a.getPeso());
			}
		}
		
		System.out.println(String.format("Grafo cretao! #Vertici %d, #archi &d", this.grafo.vertexSet().size(), this.grafo.edgeSet().size()));
	}
}
