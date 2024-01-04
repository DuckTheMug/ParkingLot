package application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class visibleBlock {
	private final SimpleStringProperty block = new SimpleStringProperty();
	private final SimpleIntegerProperty curr = new SimpleIntegerProperty();
	private final SimpleIntegerProperty max = new SimpleIntegerProperty();
	
	public visibleBlock(String block, int curr, int max) {
		this.block.set(block);
		this.curr.set(curr);
		this.max.set(max);
	}
	public SimpleStringProperty getBlock() {
		return block;
	}

	public SimpleIntegerProperty getCurr() {
		return curr;
	}

	public SimpleIntegerProperty getMax() {
		return max;
	}
}
