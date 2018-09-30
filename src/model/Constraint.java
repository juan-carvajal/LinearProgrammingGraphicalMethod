package model;

import java.util.ArrayList;

public class Constraint {

	public int id;
	public double xCoefficient;
	public double yCoefficient;
	public double rightCoefficient;
	public String type;
	public static final String LESS_OR_EQUAL="<=";
	public static final String MORE_OR_EQUAL=">=";
	public static final String EQUAL="=";
	public static final double DELTA=0.000001;
	
	public Constraint(int id,double xCoefficient, double yCoefficient, double rightCoefficient, String type) {
		super();
		this.id=id;
		if((xCoefficient==0 && yCoefficient==0) ) {
			throw new IllegalArgumentException("Inconsistent constraint specifications at constraint "+id+".");
		}
		
		this.xCoefficient = xCoefficient;
		this.yCoefficient = yCoefficient;
		this.rightCoefficient = rightCoefficient;
		this.type = type;
	}
	
	public double evaluate(Point p) {
            
              return (p.x*xCoefficient)+(p.y*yCoefficient);
            
	}
	
        public double giveYof(double x)throws Exception{
            if(this.yCoefficient==0){
		throw new Exception("Cannot calculate y value of a function with no y coefficient 0.");
            }else{
              return (this.rightCoefficient-(this.xCoefficient*x))/this.yCoefficient;
            }
            
        }
	
	public boolean isSatisfiedBy(Point p) {
		double evaluation=evaluate(p);
		if(this.type.equals(LESS_OR_EQUAL)) {
			return (evaluation<=this.rightCoefficient+DELTA);
		}else if(this.type.equals(MORE_OR_EQUAL)) {
			return (evaluation>=this.rightCoefficient-DELTA);
		}else {
			return (evaluation>=this.rightCoefficient-DELTA && evaluation<=this.rightCoefficient+DELTA);
		}
	}
	
	public ArrayList<Point> pointsOfIntersectionWithAxes(){
		ArrayList<Point>cuts=new ArrayList<Point>();
		double c1=this.rightCoefficient/this.yCoefficient;
               
		if(!Double.isInfinite(c1) && !Double.isNaN(c1)){
			cuts.add(new Point(0,c1));
		}
		double c2=this.rightCoefficient/this.xCoefficient;
              
		if(!Double.isInfinite(c2) && !Double.isNaN(c2)){
			cuts.add(new Point(c2,0));
		}
		
		return cuts;
	}
	
	public Point intersectionWith(Constraint c) {
		double topPart=(this.rightCoefficient*c.yCoefficient) - (c.rightCoefficient*this.yCoefficient);
		double bottomPart=(this.xCoefficient*c.yCoefficient)-(c.xCoefficient*this.yCoefficient);
		double x=topPart/bottomPart;
		double y;
                if(this.yCoefficient!=0){
                    y=(this.rightCoefficient-(this.xCoefficient*x))/this.yCoefficient;
                }else{
                    y=(c.rightCoefficient-(c.xCoefficient*x))/c.yCoefficient;
                }
//                System.out.println("TOP:"+topPart);
//                 System.out.println("BOTTOM:"+bottomPart);
		if(!Double.isNaN(x)  && !Double.isNaN(y)) {
			return new Point(x,y);
		}else {
			return null;
		}
		
		
	}
	
public String toString() {
	String separator="";
	if(this.yCoefficient>=0) {
		separator="+";
	}
	return this.xCoefficient+"X"+separator+this.yCoefficient+"Y"+this.type+this.rightCoefficient;
}
	
	
}
