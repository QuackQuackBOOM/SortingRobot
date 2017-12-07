import lejos.nxt.LCD;
import lejos.robotics.subsumption.Behavior;

public class MoveBlocks implements Behavior {	
	public void action() {
		//if claw is open
		if (MainClass.isClawOpen) {
			//if position of robot doesnt equal movementQueue.getFromPos() [first node in the linked list of movements]
			if (MainClass.getCurrentPos() != MainClass.movementQueue.getFromPos()) {
				//go to movementQueue.getFromPos()
				MainClass.setNextPos(MainClass.movementQueue.getFromPos());
				//if position of robot equals movementQueue.getFromPos()
			} else {
				//grab object
				MainClass.openClaw = false;
			}
		//if claw is NOT open
		} else {
			//same as above but for ToPos
			if (MainClass.getCurrentPos() != MainClass.movementQueue.getToPos()) {
				MainClass.setNextPos(MainClass.movementQueue.getToPos());
			} else {
				//release object
				MainClass.openClaw = true;
				//movement is complete so remove from movementQueue
				MainClass.movementQueue.removeNode();
				LCD.clear(3);
				LCD.drawString("Qlen:" + MainClass.movementQueue.length, 0, 3);
			}
		}
	}
	
	public void suppress() {
		
	}
	
	public boolean takeControl() {
		//if there are movements to be done
		return (!MainClass.movementQueue.isEmpty());
	}
}
