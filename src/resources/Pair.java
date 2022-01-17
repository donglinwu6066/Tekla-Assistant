package resources;

public class Pair<T, U> {         
	private T t;
	private U u;

    public Pair(T t, U u) {         
        this.t= t;
        this.u= u;
	}
    public void setFirst(T t) {
    	this.t = t;
    }
    public void setSecond(U u) {
    	this.u = u;
    }
    public T getFirst() {
    	return t;
    }
    public U getSecond() {
    	return u;
    }
	// print
    @Override
	public String toString() {
		return t.toString() + ":" + u.toString() ;
	}
	// print\
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Pair<?, ?>) {
			Pair<?, ?> pair = (Pair<?, ?>) obj;
			return t.equals(pair.t) && u.equals(pair.u);
		}
		return false;
	}
	@Override
    public int hashCode() {
           final int prime = 31;
           int result = 1;
           result = prime * result
                        + ((t == null) ? 0 : t.hashCode());
           result = prime * result
                   + ((u == null) ? 0 : u.hashCode());
           return result;
    }
 }
