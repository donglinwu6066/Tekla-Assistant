package resources;

import java.util.ArrayList;
import java.util.List;

public class ConnectionInfo implements CellOutput{
	String type;
	ArrayList<Pair<Object, Object>> data;
	ConnectionInfo(String type){
		this.type = type;
		data = new ArrayList<Pair<Object, Object>>();
	}
	
	public void add(Pair<Object, Object> data) {
		this.data.add(data);
	}

	@Override
	public String toString() {
        return "type: " + type + ", data: " +data;
    }
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ConnectionInfo) {
			ConnectionInfo conInfo = (ConnectionInfo)obj;
			return type.equals(conInfo.type) && data.equals(conInfo.data);
		}
        return false;
    }
	
	@Override
	public List<Object> getInfo() {
		List<Object> info = new ArrayList <Object>();
		info.add(type);
		
		for(Pair<Object, Object> item : data) {
			info.add(item.getFirst());
			info.add(item.getSecond());
		}
		return info;
	}

}
