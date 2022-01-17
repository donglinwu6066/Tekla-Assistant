package resources;

public class Point extends Pair<Integer, Integer>{
	Point(Integer t, Integer u){
		super(t, u);
	}
    public void jumpRow(Integer t) {
    	setFirst(getFirst() + t);
    }
    public void jumpColumn(Integer u) {
    	setSecond(getSecond() + u);
    }
    public int getRow() {
    	return getFirst();
    }
    public int getColumn() {
    	return getSecond();
    }
}
