package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import algorithms.Tools.DistPath;

public class DefaultTeam {

	

	public ArrayList<Point> voisins(ArrayList<Point> points , Point p , int edgeThreshold){
		ArrayList<Point> v = new ArrayList<Point>();
		for(Point q : points ) {
			if( (p.distance(q) <= edgeThreshold) && (! q.equals(p)) )
				v.add(q);
		}
		return v;
	}
	public ArrayList<Point> calculConnectedDominatingSet(ArrayList<Point> points, int edgeThreshold) {
		ArrayList<Point> result = new ArrayList<Point>(); 
		ArrayList<Point> listvoisins = new ArrayList<Point>();
//				for(Point p :points ){
//					if(yapasarete(result, p , edgeThreshold) && !tout_le_monde_est_domine(points, result, edgeThreshold)){
//						result.add(p);
//					}
//				}

		for(Point p : points ){
			//if( i == max(points_copie, edgeThreshold)) {
				if(voisins(points, p, edgeThreshold).isEmpty()){
					result.add(p);
				}
				else {
					listvoisins = voisins(points, p , edgeThreshold);
					for( Point l : listvoisins ) {
						if( ! domine(result , l , edgeThreshold) ) {
							result.add(p);
						}
					}
				}
		}


		DistPath distPath = new Tools.DistPath(points, edgeThreshold);
		double [][] dists =  distPath.getDists();
		int[][] paths=distPath.getPaths();

		ArrayList<Edge> edgesK = new ArrayList<Edge>();
		for (int i = 0; i<result.size();i++){
			for (int j = 0; j<result.size();j++){
				if(i!=j)
					edgesK.add(new Edge(result.get(i),
							result.get(j),
							dists[points.indexOf(result.get(i))][points.indexOf(result.get(j))]));
			}
		}

		ArrayList<Edge> edgesKS = Tools.kruskal(result, edgesK);
		ArrayList<Edge> edgesG=new ArrayList<Edge>();
		result = new ArrayList<Point>();
		for(int i=0;i<edgesKS.size();i++){
			Edge e = edgesKS.get(i);
			ArrayList<Point> p = Tools.chemin (e,points,paths);
			for(Point p1 : p)
				if(!result.contains(p1))
					result.add(p1);
			for(int j = 0; j< p.size()-1; j++){
				edgesG.add(new Edge(p.get(j),p.get(j+1)));

			}
		}
		edgesG = Tools.kruskal(result, edgesG);
		result = new ArrayList<Point>();
		for(int i=0;i<edgesG.size();i++){
			Edge e = edgesG.get(i);
			if(!result.contains(e.getP1()))
				result.add(e.getP1());
			if(!result.contains(e.getP2()))
				result.add(e.getP2());	
		}

		if(tout_le_monde_est_domine(points, result, edgeThreshold))
			System.out.println("tout le monde est domine");



		//return amelioration1(points, result , edgeThreshold);//229
		return result;//234
	}


	public boolean domine(ArrayList<Point> S , Point d , int edgeThreshold){

		for(Point s : S){
			if(s.equals(d))
				return true;
			else
				if( s.distance(d) <= edgeThreshold ) 
					return true;

		}

		return false;

	}

	public boolean tout_le_monde_est_domine ( ArrayList<Point> points , ArrayList<Point> S , int edgeThreshold){
		for(Point p : points) {
			if( ! domine( S , p , edgeThreshold))
				return false ;
		}
		return true ;
	}	
	public ArrayList<Point> amelioration1 ( ArrayList<Point> points ,  ArrayList<Point> S , int edgeThreshold ){
		Point voisin1 = null , voisin2 = null , voisin3 = null , voisin4 = null , voisin5 = null;
		for( Point p : points ) {
			if( voisins(S, p, edgeThreshold).size() >=2 ) {
				for( Point s : S) {
					if( p.distance(s) <= edgeThreshold && !p.equals(s) ) {
						if( voisin1 == null ) {
							voisin1 = s ;
						}else {
							if( voisin2 == null ) {
								voisin2 = s ;
							}else {
								if( voisin3 == null ) {
									voisin3 = s ;
								}else {
									if( voisin4 == null ) {
										voisin4 = s ;
									}else {
										if( voisin5 == null ) {
											voisin5 = s ;
										}
									}
								}
							}
						}	
					}
				}
				//ici j'ai tous les voisins du noeud p	
				//S.add(p);
				if( voisin1 != null && voisin2 != null && voisin3 != null && voisin4 != null && voisin5 != null) {
					S.remove(voisin1);
					S.remove(voisin2);
					S.remove(voisin3);
					S.remove(voisin4);
					S.remove(voisin5);
					if( ! tout_le_monde_est_domine( points, S , edgeThreshold)) {
						S.add(voisin1);
						S.add(voisin2);
						S.add(voisin3);
						S.add(voisin4);
						S.add(voisin5);
					}
				}else {
					if( voisin1 != null && voisin2 != null && voisin3 != null && voisin4 != null ) {
						S.remove(voisin1);
						S.remove(voisin2);
						S.remove(voisin3);
						S.remove(voisin4);
						if( ! tout_le_monde_est_domine( points, S, edgeThreshold)) {
							S.add(voisin1);
							S.add(voisin2);
							S.add(voisin3);
							S.add(voisin4);
						}
					}else {
						if( voisin1 != null && voisin2 != null && voisin3 != null ) {
							S.remove(voisin1);
							S.remove(voisin2);
							S.remove(voisin3);
							if( ! tout_le_monde_est_domine( points, S, edgeThreshold)) {
								S.add(voisin1);
								S.add(voisin2);
								S.add(voisin3);
							}
						}else {
							if( voisin1 != null && voisin2 != null ) {
								S.remove(voisin1);
								S.remove(voisin2);
								if( ! tout_le_monde_est_domine( points, S, edgeThreshold)) {
									S.add(voisin1);
									S.add(voisin2);
								}
							}
						}
					}
				}
				//fin premiere amelioration
			}
		}
		return S;
	}

	public boolean yapasarete(ArrayList<Point> S , Point d , int edgeThreshold){
		for(Point p : S){
			if(p.distance(d) < edgeThreshold){
				return false;
			}
		}
		return true;//il a aucune arete avec les elts de S
	}
	//FILE PRINTER
	private void saveToFile(String filename,ArrayList<Point> result){
		int index=0;
		try {
			while(true){
				BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(filename+Integer.toString(index)+".points")));
				try {
					input.close();
				} catch (IOException e) {
					System.err.println("I/O exception: unable to close "+filename+Integer.toString(index)+".points");
				}
				index++;
			}
		} catch (FileNotFoundException e) {
			printToFile(filename+Integer.toString(index)+".points",result);
		}
	}
	private void printToFile(String filename,ArrayList<Point> points){
		try {
			PrintStream output = new PrintStream(new FileOutputStream(filename));
			int x,y;
			for (Point p:points) output.println(Integer.toString((int)p.getX())+" "+Integer.toString((int)p.getY()));
			output.close();
		} catch (FileNotFoundException e) {
			System.err.println("I/O exception: unable to create "+filename);
		}
	}

	//FILE LOADER
	private ArrayList<Point> readFromFile(String filename) {
		String line;
		String[] coordinates;
		ArrayList<Point> points=new ArrayList<Point>();
		try {
			BufferedReader input = new BufferedReader(
					new InputStreamReader(new FileInputStream(filename))
					);
			try {
				while ((line=input.readLine())!=null) {
					coordinates=line.split("\\s+");
					points.add(new Point(Integer.parseInt(coordinates[0]),
							Integer.parseInt(coordinates[1])));
				}
			} catch (IOException e) {
				System.err.println("Exception: interrupted I/O.");
			} finally {
				try {
					input.close();
				} catch (IOException e) {
					System.err.println("I/O exception: unable to close "+filename);
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println("Input file not found.");
		}
		return points;
	}
}