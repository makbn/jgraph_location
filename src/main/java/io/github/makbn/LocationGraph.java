package io.github.makbn;

import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.ListenableDirectedWeightedGraph;

import java.util.ArrayList;


public class LocationGraph<V extends LocationVertex, E extends PathEdge<V>> {
    private ArrayList<V> vertices;
    private ArrayList<PathEdge<V>> edges;

    public LocationGraph(Class<? extends E> edgeClass) {
        //super(edgeClass);
        this.vertices=new ArrayList<V>();
        this.edges=new ArrayList<PathEdge<V>>();
    }

    public LocationGraph(WeightedGraph<V, E> base) {
        //super(base);
        this.vertices=new ArrayList<V>();
        this.edges=new ArrayList<PathEdge<V>>();
    }


    //@Override
    public boolean addVertex(V v) {
        vertices.add(v);
        return true;
    }

    public boolean createEdge(V src,V dst){
        if(vertices.contains(src) && vertices.contains(dst)){

            PathEdge<V> pe=new PathEdge<V>(src,dst);
            if(edges.contains(pe)){
                pe=edges.get(edges.indexOf(pe));
                pe.setWeight(pe.getWeight()+1);
                return false;
            }
            return edges.add(pe);
        }

        return false;
    }


    public ArrayList<V> getVertices() {
        return vertices;
    }

    public ArrayList<PathEdge<V>> getEdges() {
        return edges;
    }


    public void print(){
        for(PathEdge p:edges){
            p.print();
        }
    }
}
