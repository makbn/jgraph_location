package io.github.makbn;

import java.util.ArrayList;
import java.util.LinkedList;

public class DirectedPath {

    private LinkedList<LocationVertex> vertices;

    public DirectedPath(LinkedList<LocationVertex> vertices) {
        this.vertices = vertices;
    }

    public DirectedPath(ArrayList<LocationVertex> vertices){
        this.vertices=new LinkedList<LocationVertex>();
        for(LocationVertex vertex:vertices){
            this.vertices.addLast(vertex);
        }
    }

    public LinkedList<LocationVertex> getVertices() {
        return vertices;
    }

    public void setVertices(LinkedList<LocationVertex> vertices) {
        this.vertices = vertices;
    }

    @Override
    public String toString() {
        String s="";
        for(LocationVertex locationVertex:vertices){
            s=s.concat(locationVertex.getpCity()+"-");
        }
        return s.substring(0,s.length()-1);
    }
}
