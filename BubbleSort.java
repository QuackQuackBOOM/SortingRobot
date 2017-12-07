import lejos.nxt.LCD;
import lejos.robotics.subsumption.Behavior;

public class BubbleSort implements Behavior{
	private int stage = 0;
	
	public void action() {
		switch (stage) {
		case 0:
			int[] array = MainClass.values.clone();
			int temp;
			boolean madeChange = true;
			while (madeChange) {
				madeChange = false;
				for (int i = 0; i < array.length-1; i++) {
					if (array[i] > array[i+1]) {
						madeChange = true;
						
						temp = array[i];
						array[i] = array[i+1];
						array[i+1] = temp;
						
						MainClass.movementQueue.addNode(i,-1);
						MainClass.movementQueue.addNode(i+1,i);
						MainClass.movementQueue.addNode(-1, i+1);
						
						LCD.clear(3);
						LCD.drawString("Qlen:" + MainClass.movementQueue.length, 0, 3);
					}
				}
			}
			stage +=1;
			break;
		case 1:
			MainClass.isFinished = true;
			stage = 0;
			break;
		}
	}
	
	public void suppress() {
		
	}
	
	public boolean takeControl() {
		return true;
	}
}
