package model;

import java.util.ArrayList;

public class Model {
	
	public double xCoefficient;
	public double yCoefficient;
	public boolean allowsNegatives;
	public String type;
	public ArrayList<Constraint> constraints;
	public static final String MAXIMIZE="MAXIMIZE";
	public static final String MINIMIZE="MINIMIZE";
	
	public Model(double xCoefficient, double yCoefficient, boolean allowsNegatives, String type) {
		super();
		this.xCoefficient = xCoefficient;
		this.yCoefficient = yCoefficient;
		this.allowsNegatives = allowsNegatives;
		this.type = type;
		this.constraints = new ArrayList<Constraint>();
	}
	
	public void addConstraint(Constraint c) {
		this.constraints.add(c);
	}
	
	public double evaluateObjetive(Point p) {
		return this.xCoefficient*p.x + this.yCoefficient*p.y;
	}
        
        public ArrayList<Point>giveAxisIntersections(){
            ArrayList<Point>points=new ArrayList<>();
            for (int i = 0; i < constraints.size(); i++) {
			points.addAll(constraints.get(i).pointsOfIntersectionWithAxes());
		}
            return points;
        }
	
	public ArrayList<Point>giveAllFeasiblePoints(){
		ArrayList<Point>points=new ArrayList<Point>();
		points.add(new Point(0,0));
		for (int i = 0; i < constraints.size(); i++) {
			points.addAll(constraints.get(i).pointsOfIntersectionWithAxes());
		}
		
		for (int i = 0; i < constraints.size(); i++) {
			for (int j = i+1; j < constraints.size(); j++) {
				Point p=constraints.get(i).intersectionWith(constraints.get(j));
				if(p!=null) {
					points.add(p);
				}else{
                                    System.out.println("Interseccion nula");
                                }
			}
		}
		
		if(!this.allowsNegatives) {
			for (int i = 0; i < points.size(); i++) {
				if(points.get(i).x<0 || points.get(i).y<0) {
					points.remove(i);
					i--;
				}
			}
		}
		
		for (int i = 0; i < points.size(); i++) {
			boolean feasible=true;
			for (int j = 0; j < constraints.size() && feasible; j++) {
				if(!constraints.get(j).isSatisfiedBy(points.get(i))) {
					feasible=false;
					System.out.println("Discarding point "+points.get(i).toString()+" because of constraint "+constraints.get(j).toString());
					points.remove(i);
					i--;
				}
			}
		}
		System.out.println("Feasible points:");
		for (int i = 0; i < points.size(); i++) {
			System.out.println(points.get(i).toString());
		}
		return points;
	}
	
	
	public Point giveOptimumPoint() {
		ArrayList<Point>feasiblePoints=this.giveAllFeasiblePoints();
		Point op=null;
		if(this.type.equals(this.MAXIMIZE)) {
			System.out.println("Maximum:");
			double max=Double.NEGATIVE_INFINITY;
			for (int i = 0; i < feasiblePoints.size(); i++) {
				if(evaluateObjetive(feasiblePoints.get(i))>max) {
					max=evaluateObjetive(feasiblePoints.get(i));
					op=feasiblePoints.get(i);
				}
			}
			
		}else {
			System.out.println("Minimum:");
			double min=Double.POSITIVE_INFINITY;
			for (int i = 0; i < feasiblePoints.size(); i++) {
				if(evaluateObjetive(feasiblePoints.get(i))<min) {
					min=evaluateObjetive(feasiblePoints.get(i));
					op=feasiblePoints.get(i);
				}
			}
		}
		return op;
	}
	
	
	public String toString() {
		String t="";
		String separator="";
		if(this.yCoefficient>=0) {
			separator="+";
		}
		if(this.type.equals(this.MAXIMIZE)) {
			t="MAXIMIZE";
		}else {
			t="MINIMIZE";
		}
		String st=t+" "+this.xCoefficient+"X"+separator+""+this.yCoefficient+"Y SUBJECT TO:"+"\n";
		if(!this.allowsNegatives) {
			st+="X,Y>=0"+"\n";
		}
		for (int i = 0; i < constraints.size(); i++) {
			st+=constraints.get(i).toString()+"\n";
		}
		return st;
	}
	
	
	

}
