/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package imagemorphing;

import java.util.LinkedList;

/**
 *
 * @author Samuel
 */
public class Triangulation {
    private LinkedList<Vector2D> pList;
    private LinkedList<Edge2D> eList;
    private LinkedList<Triangle2D> tList;
    public Triangulation(Vector2D a, Vector2D b, Vector2D c){
        pList = new LinkedList<Vector2D>();
        eList = new LinkedList<Edge2D>();
        tList = new LinkedList<Triangle2D>();
        pList.add(a);
        pList.add(b);
        pList.add(c);
        eList.add(new Edge2D(a, b));
        eList.add(new Edge2D(b, c));
        eList.add(new Edge2D(c, a));
        tList.add(new Triangle2D(
                eList.get(0),
                eList.get(1),
                eList.get(2)));
    }
    public boolean addPoint(Vector2D p){
        // If triangulation already contains point
        if(contains(p))
            return false;
        
        // Process outside cycle to avoid cycle/mod exception
        Triangle2D bound = null;
        for(Triangle2D at: tList){
            if(LAT.pointInTriangle(p, at)){
                bound = at;
                break;
            }
        }
        
        // Remove container triangle
        tList.remove(bound);
        Vector2D[] vertexes = bound.getVertexes();
        Edge2D 
                up = new Edge2D(vertexes[0], p),
                vp = new Edge2D(vertexes[1], p),
                wp = new Edge2D(vertexes[2], p);
        Edge2D[] edges = bound.getEdges();
        
        // Add new triangles
        tList.add(relatedTriangle(edges[0], up, vp, wp));
        tList.add(relatedTriangle(edges[1], up, vp, wp));
        tList.add(relatedTriangle(edges[2], up, vp, wp));
        
        // Add new Edges
        eList.add(up);
        eList.add(vp);
        eList.add(wp);
        
        // Add new point
        pList.add(p);
        return true;
    }
    public Triangle2D relatedTriangle(Edge2D b, Edge2D n0, Edge2D n1, Edge2D n2){
        if(b.shareWith(n0))
            if(b.shareWith(n1))
                return new Triangle2D(b, n0, n1);
            else
                return new Triangle2D(b, n0, n2);
        else
            return new Triangle2D(b, n1, n2);
    }
    public boolean contains(Vector2D p){
        for(Vector2D a: pList)
            if(p.compareTo(a) == 0)
                return true;
        return false;
    }
    public LinkedList<Edge2D> getEdgeList(){
        return eList;
    }
    public LinkedList<Vector2D> getPointCloud(){
        return pList;
    }
}