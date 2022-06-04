package it.polito.tdp.crimes.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private EventsDao dao;
	private Graph<String, DefaultWeightedEdge> graph;
	private List<String> vertices;
	private List<Arco> edges;
	private List<String> result;
	private double max;
	
	public Model() {
		this.dao = new EventsDao();
	}
	
	public List<String> listEventCategory() {
		return this.dao.listEventCategory();
	}
	
	public List<LocalDate> listEventDate(){
		return this.dao.listEventDate();
	}

	public List<Arco> creaGrafo(String category, LocalDate date) {
		this.graph = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		this.vertices = this.dao.listEventType(category, date);
		Graphs.addAllVertices(this.graph, this.vertices);
		
		List<Arco> edges = this.dao.listEdges(category, date);
		double max = 0.0;
		double min = 10000.0;
		
		for(Arco a: edges) {
			DefaultWeightedEdge edge = this.graph.addEdge(a.getType1(), a.getType2());
			this.graph.setEdgeWeight(edge, a.getWeight());
			
			if(a.getWeight() > max) 
				max = a.getWeight();
			
			if(a.getWeight() < min) 
				min = a.getWeight();
		}
		
		double mean = (max+min)/2;
		this.edges = new ArrayList<>();
		
		for(Arco a: edges) {
			if(a.getWeight() <= mean) {
				this.edges.add(a);
			}
		}
		
		return this.edges;
	}

	public List<String> calcolaPercorso(Arco edge) {
		result = new ArrayList<String>();
		this.max = 0.0;
		String start = edge.getType1();
		String end = edge.getType2();
		
		List<String> parziale = new ArrayList<String>();
		parziale.add(start);
		
		this.ricorsiva(parziale, start, end, 0);
		
		return this.result;
	}
	
	public void ricorsiva(List<String> parziale, String start, String end, double w) {
		if(parziale.get(parziale.size()-1).equals(end)) {
			if(parziale.size() > result.size() && w > this.max) {
				this.max = w;
				this.result = new ArrayList<String>(parziale);
			}
			return;
		} else {
			for(String s: Graphs.neighborListOf(this.graph, start)) {
				if(!parziale.contains(s)) {
					parziale.add(s);
					w += this.graph.getEdgeWeight(this.graph.getEdge(start, s));
					this.ricorsiva(parziale, s, end, w);
					w -= this.graph.getEdgeWeight(this.graph.getEdge(start, s));
					parziale.remove(s);
				}
			}
		}
	}

	public double getMax() {
		return max;
	}
	
}
