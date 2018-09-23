package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Tools {
	
	public static List<Edge> pointsToEdges(List<Point> points){
		
		List<Edge> edges = new ArrayList<>();
		for(int j=0;j<points.size();j++) {	
			Point p1 = points.get(j);
			for(int i=0;i<points.size();i++){
				Point p2 = points.get(i);
				if(p1.equals(p2))
					continue;
				Edge edge = new Edge(p1,p2);
				edges.add(edge);
			}

		}
		return edges;
	}

	public static Tree2D naif(ArrayList<Point> points) {

		Point pere,fils,candidat;
		fils=null;
		candidat=null;
		pere=points.remove(0);
		Tree2D root = new Tree2D(pere,new ArrayList<Tree2D>());
		Tree2D myResult = root;
		int size = points.size();
		double distance,distanceTmp;
		distance=0;
		while(points.size()>0){
			System.out.println("1");
			for(int j=0;j<points.size();j++){
				candidat = points.get(j);
				System.out.println("2");
				if(fils == null){
					System.out.println("2.1");
					fils = candidat;
					distance=Math.sqrt(Math.pow(pere.getX()-candidat.getX(),2)
							+Math.pow(pere.getY()-candidat.getY(),2));
				}

				else{
					distanceTmp=Math.sqrt(Math.pow(pere.getX()-candidat.getX(),2)
							+Math.pow(pere.getY()-candidat.getY(),2));
					System.out.println("2.2");
					if(distance>distanceTmp){
						System.out.println("2.2.1");
						fils=candidat;
						distance=distanceTmp;
					}
				}
			}
			pere=points.remove(points.indexOf(fils));
			fils=null;
			Tree2D tmp = new Tree2D(pere,new ArrayList<Tree2D>());
			root.getSubTrees().add(tmp);
			root=tmp;
		}
		return myResult;
	}

	public static Tree2D kruskal(ArrayList<Point> points) {

		Map<Point,Integer> map=new HashMap<Point,Integer>();
		for(int i=0;i<points.size();i++){
			map.put(points.get(i), new Integer(i));
		}
		

		ArrayList<Edge> edges = new ArrayList<>();

		
		System.out.println(edges.size());
		edges.sort(new EdgeComparator());
		System.out.println(edges.size());

		ArrayList<Edge> treeEdges = new ArrayList<>();
		

		for(int i=0;treeEdges.size() < points.size()-1;i++){
			Edge edge = edges.get(i);
			Point p1= edge.getP1();
			Point p2= edge.getP2();
			Integer v1 = map.get(p1);
			Integer v2 = map.get(p2);
			if(! (v1.equals(v2)) ) {
				for(Point p : map.keySet())
					if(map.get(p).equals(v1))
						map.put(p, v2);
				treeEdges.add(edge);
			}
		}
		
		return edgesToTree(treeEdges,treeEdges.get(0).getP1());

	}

	public static ArrayList<Edge> kruskal(ArrayList<Point> points,ArrayList<Edge> edges) {

		Map<Point,Integer> map=new HashMap<Point,Integer>();
		for(int i=0;i<points.size();i++){
			map.put(points.get(i), new Integer(i));
		}
		System.out.println(edges.size());
		edges.sort(new EdgeComparator());
		System.out.println(edges.size());

		ArrayList<Edge> treeEdges = new ArrayList<>();
		

		for(int i=0;treeEdges.size() < points.size()-1;i++){
			Edge edge = edges.get(i);
			Point p1= edge.getP1();
			Point p2= edge.getP2();
			Integer v1 = map.get(p1);
			Integer v2 = map.get(p2);
			if(! (v1.equals(v2)) ) {
				for(Point p : map.keySet())
					if(map.get(p).equals(v1))
						map.put(p, v2);
				treeEdges.add(edge);
			}
		}
		
		return treeEdges;

	}

	public static Tree2D edgesToTree(ArrayList<Edge> edges,Point pere){
		Tree2D tree = new Tree2D(pere,new ArrayList<Tree2D>());
		ArrayList<Edge> sons = new ArrayList<Edge>();
		
		for(int i =0; i<edges.size(); i++){
			Edge edge = edges.get(i);
			Point a = edge.getP1();;
			Point b = edge.getP2();
			if(pere.equals(a))
				sons.add(edge);
			else
				if(pere.equals(b)){
					sons.add(edge);
				}	
		}
		
		
		for(int i = 0; i<sons.size();i++)
			edges.remove(sons.get(i));
		
		
		for(int i =0; i<sons.size(); i++){
			Edge edge = sons.get(i);
			Point a = edge.getP1();;
			Point b = edge.getP2();
			if(pere.equals(a))
				tree.getSubTrees().add(edgesToTree(edges,b));
			else
				if(pere.equals(b))
					tree.getSubTrees().add(edgesToTree(edges,a));
		}
		return tree;
	}
	
	public boolean noCycle2(ArrayList<Tree2D> edges,Tree2D edge,Map<Point,Integer> map){
		Point p1= edge.getRoot();
		Point p2= edge.getSubTrees().get(0).getRoot();
		System.out.println(p1+" "+p2);
		System.out.println(map.get(p1)+" "+map.get(p2));
		if(map.get(p1).equals(map.get(p2)))
			return false;
		else {
			for(Point p : map.keySet())
				if(map.get(p).equals(map.get(p1)))
					map.put(p, map.get(p2));
			edges.add(edge);
			return true;
		}
	}

	public  static int[][] calculShortestPaths(ArrayList<Point> points, int edgeThreshold) {

		double [][] dists =  new double[points.size()][points.size()];
		int[][] paths=new int[points.size()][points.size()];

		for(int i=0;i<points.size();i++){
			for(int j=0;j<points.size();j++){
				if(i==j){
					dists[i][j]=0;
					paths[i][j]=j;
				}
				else{
					double dist = points.get(i).distance(points.get(j));
					if(dist>=edgeThreshold)
						dists[i][j]=Double.POSITIVE_INFINITY;
					else{
						dists[i][j]= dist;
						paths[i][j] = j;
					}
				}
			}
		}
		for(int k = 0; k<points.size();k++){
			for(int i=0;i<points.size();i++){
				for(int j=0;j<points.size();j++){
					double d =dists[i][k]+dists[k][j];
					if(dists[i][j]>d){
						paths[i][j]= paths[i][k];
						dists[i][j]=d;
					}
				}
			}
		}




		return paths;
	}
	
	public static Tree2D calculSteiner(ArrayList<Point> points, int edgeThreshold, ArrayList<Point> hitPoints) {
		
		DistPath distPath = new Tools.DistPath(points, edgeThreshold);
		double [][] dists =  distPath.getDists();
		int[][] paths=distPath.getPaths();

		ArrayList<Edge> edgesK = new ArrayList<Edge>();
		for (int i = 0; i<hitPoints.size();i++){
			for (int j = 0; j<hitPoints.size();j++){
				if(i!=j)
				edgesK.add(new Edge(hitPoints.get(i),
								   hitPoints.get(j),
								   dists[points.indexOf(hitPoints.get(i))][points.indexOf(hitPoints.get(j))]));
			}
		}
	
		ArrayList<Edge> edgesKS = Tools.kruskal(hitPoints, edgesK);
		ArrayList<Edge> edgesG=new ArrayList<Edge>();
		ArrayList<Point> res = new ArrayList<Point>();
 		for(int i=0;i<edgesKS.size();i++){
			Edge e = edgesKS.get(i);
			ArrayList<Point> p = chemin (e,points,paths);
			for(Point p1 : p)
				if(!res.contains(p1))
					res.add(p1);
			for(int j = 0; j< p.size()-1; j++){
				edgesG.add(new Edge(p.get(j),p.get(j+1)));
				
			}
		}
 		edgesK = Tools.kruskal(res, edgesG);
		return Tools.edgesToTree(edgesG,edgesG.get(0).getP1());
	}
	
	public static ArrayList<Point> chemin(Edge e,ArrayList<Point> ps,int [][] pa){
		ArrayList<Point> res = new ArrayList<Point>();
		Point tmp = e.getP1();
		
 		while(!tmp.equals(e.getP2())){
 			res.add(tmp);
 			int i = ps.indexOf(tmp);
 			int j = ps.indexOf(e.getP2());
 			tmp = ps.get(pa[i][j]);
 		}
 		res.add(e.getP2());
 		return res;
	}
	public static class DistPath{
		
		private double [][] dists;
		private int[][] paths;
		public double[][] getDists() {
			return dists;
		}
		public int[][] getPaths() {
			return paths;
		}
		public DistPath (ArrayList<Point> points, int edgeThreshold) {

			dists =  new double[points.size()][points.size()];
			paths=new int[points.size()][points.size()];

			for(int i=0;i<points.size();i++){
				for(int j=0;j<points.size();j++){
					if(i==j){
						dists[i][j]=0;
						paths[i][j]=j;
					}
					else{
						double dist = points.get(i).distance(points.get(j));
						if(dist>=edgeThreshold)
							dists[i][j]=Double.POSITIVE_INFINITY;
						else{
							dists[i][j]= dist;
							paths[i][j] = j;
						}
					}
				}
			}
			for(int k = 0; k<points.size();k++){
				for(int i=0;i<points.size();i++){
					for(int j=0;j<points.size();j++){
						double d =dists[i][k]+dists[k][j];
						if(dists[i][j]>d){
							paths[i][j]= paths[i][k];
							dists[i][j]=d;
						}
					}
				}
			}
		}
	}
}
