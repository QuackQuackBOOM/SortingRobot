import lejos.nxt.LCD;
import lejos.robotics.subsumption.Behavior;

public class Rotate implements Behavior{
	//true orientation
	private int isFacing = 1;
	//stage procedure is at
	private int stage = 0;
	
	public void action() {
		//go to stage of procedure then return to arbitrator
		switch(stage) {
		//rotate robot
		case 0:
			if (isFacing > MainClass.facing) {
				MainClass.pilot.rotate(-90,true);
			} else {
				MainClass.pilot.rotate(90,true);
			}
			stage +=1;
			break;
		//wait till finished rotating
		case 1:
			if (!MainClass.pilot.isMoving()) {
				stage +=1;
			}
			break;
		//increment isFacing and reset stage
		case 2:
			if (isFacing > MainClass.facing) {
				isFacing -=1;
			} else {
				isFacing +=1;
			}
			stage = 0;
			
			LCD.clear(2);
			LCD.drawString("face:" + isFacing, 0, 2);
			
			break;
		}
	}
	
	public void suppress() {
		
	}
	
	public boolean takeControl() {
		//if true facing in behaviour doesn't match match facing in main class
		return (isFacing != MainClass.facing);
	}
}
