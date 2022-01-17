package resources;

import java.util.List;

public interface CellOutput {
	final int MAX_ROW = 45;
	final int MAX_COLUMN = 12;
	public abstract List <Object> getInfo();
}
