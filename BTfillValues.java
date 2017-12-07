import lejos.robotics.subsumption.Behavior;

public class BTfillValues implements Behavior{
	private int stage = 0;
	private int cIndex = 0;
	private int intThisRun;
	
	public void action() {
		if (cIndex < 0) {
			stage = 0;
		} else if (MainClass.values[cIndex] != -1) {
			stage = 0;
		}
		switch(stage) {
		case 0:
			cIndex = -1;
			MainClass.valuesHasEmpty = false;
			for (int i = 0; i < MainClass.values.length && cIndex == -1; i++) {
				if (MainClass.values[i] == -1) {
					cIndex = i;
					MainClass.valuesHasEmpty = true;
					stage +=1;
				}
			}
			break;
		case 1:
			if (MainClass.getCurrentPos() != cIndex) {
				MainClass.setNextPos(cIndex);
			} else {
				stage +=1;
			}
			break;
		case 2:
			if (MainClass.facing != 2) {
				MainClass.facing = 2;
			} else {
				stage +=1;
			}
			break;
		case 3:
			MainClass.pilot.travel(MainClass.GRAB_DIST);
			stage +=1;
			break;
		case 4:
			if (!MainClass.pilot.isMoving()) {
				stage +=1;
			}
			break;
		case 5:
			if (MainClass.lastValue != MainClass.cValue) {
				MainClass.lastValue = MainClass.cValue;
				intThisRun = MainClass.cValue;
				stage+=1;
			}
			break;
		case 6:
			MainClass.pilot.travel(-MainClass.GRAB_DIST);
			stage +=1;
			break;
		case 7:
			if (!MainClass.pilot.isMoving()) {
				stage +=1;
			}
			break;
		case 8:
			MainClass.values[cIndex] = intThisRun;
			stage = 0;
			cIndex = 0;
			
			
			
		}
		
	}
	
	public void suppress() {
		
	}
	
	public boolean takeControl() {
//		for (int i = 0; i < MainClass.Values.length; i++) {
//			if (MainClass.Value[i] == -1) {
//				return true;
//			}
//		}
//		return false;
		return (MainClass.valuesHasEmpty);
	}

}
