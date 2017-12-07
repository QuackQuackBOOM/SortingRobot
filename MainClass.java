import java.io.DataInputStream;
import java.io.DataOutputStream;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

//TO DO
////Modify MovQUEUE so update LCD when modified
////Set initial Condition of Claw

public class MainClass {
	public static final int NUM_OF_SORTS = 1;
	public static final int NUM_OF_BLOCKS = 3;
	public static final int MAX_SPEED = 75;
	public static final double WHEEL_DIAMETER = 55;
	public static final double AXEL_LENGTH = 137.75;
	//Distance that pilot goes forward when grabbing item
	public static final int GRAB_DIST = 200;
	
	public static LightSensor ls = new LightSensor(SensorPort.S1);
	public static DifferentialPilot pilot = new DifferentialPilot(MainClass.WHEEL_DIAMETER, MainClass.AXEL_LENGTH, Motor.B, Motor.A);
	
	//Has algorithm finished
	public static boolean isFinished = false;
	//for alternating sorting algorithms (for when more than one)
	public static int sortIndex = 0;
	
	//Current position
	private static int currentPos = -1;
	//Position to go to (NextPos behaviour moves to this position)
	private static int nextPos = -1;
	//Direction robot is facing (1 = facing positive, 2 = facing objects, 3 = facing negative)
	public static int facing = 1;
	
	//Is claw open
	public static boolean isClawOpen = true;
	//What claw is changed to next time Claw behaviour is executed
	public static boolean openClaw = true;
	
	//A linked list array of moving an object into blank spot
	//movementQueue.addNode(int fromPos, int toPos)
	//Behavior MovQueue does 1) go to fromPos 2) close claw
	//3) go to toPos 4) open claw 5) delete that instruction from liked list
	public static MovQueue movementQueue = new MovQueue();
	
	//Hard coded values to be sorted
	public static int[] values = {-1,-1,-1};
	public static boolean valuesHasEmpty = true;
	
	public static BTConnection connection = null;
	public static DataInputStream dis;
	public static DataOutputStream dos;
	public static byte[] buffer;
	public static int MAX_READ = 30;
	public static int cValue = -1;
	public static int lastValue = -1;
	
	public static void main(String[] args) {
		//Calabrate Light Sensor
		ls.setLow(getHighLow("Dark")[0]);
		ls.setHigh(getHighLow("Bright")[1]);
		try{
			Button.ENTER.waitForPressAndRelease();
		} catch(Exception e){}
		
		pilot.setRotateSpeed(250);
		pilot.setTravelSpeed(50);
		Motor.C.setSpeed(50);
		
		buffer = new byte[MAX_READ];
		LCD.drawString("Waiting  ", 0, 0);
		MainClass.connection = Bluetooth.waitForConnection(0,  NXTConnection.RAW);
		LCD.clear();
		
		//##change claw to start pos
		
		//Display current and next pos (is updated by set and get methods)
		LCD.drawString("Cpos:-1", 0, 0);
		LCD.drawString("Npos:-1", 0, 1);
		LCD.refresh();
		
		Behavior[] ba = {
				//The sorting algorithm
				new BubbleSort(),
				//Moving ojects around
				new MoveBlocks(),
				//for when algorithm is finished
				new Reset(),
				//working the claw
				new Claw(),
				new BTfillValues(),
				//moving along line
				new NextPos(),
				//rotating robot
				new Rotate(),
				new BTconnect()
		};
		Arbitrator arb = new Arbitrator(ba);
		
		arb.start();
	}
	
	//For Calibration
	private static int[] getHighLow(String message) {
		int reading, low, high;
		
		LCD.drawString(message, 0,0);
		try{
			Button.ENTER.waitForPressAndRelease();
		} catch(Exception e){}
		reading = ls.getNormalizedLightValue();
		low = reading;
		high = reading;
		while (!Button.ESCAPE.isDown()) {
			reading = ls.getNormalizedLightValue();
			if (high < reading) {
				high = reading;
			} else if (low > reading) {
				low = reading;
			}
			LCD.drawInt(reading, 0, 1);
			LCD.drawInt(low, 0, 2);
			LCD.drawInt(high, 0, 3);
			LCD.refresh();
		}
		LCD.clear();
		return new int[] {low,high};
	}
	
	/////TEMP - instead of sensing values
	public static int getValue() {
		return values[currentPos];
	}
	/////TEMP - instead of sensing values - while sorting
	public static void switchValues(int index1, int index2) {
		int temp = values[index1];
		values[index1] = values[index2];
		values[index2] = temp;
	}
	
	//setters and getters are so LCD can be updated
	
	public static void setCurrentPos(int newPos) {
		if (newPos != currentPos) {
			currentPos = newPos;
			LCD.clear(0);
			LCD.drawString("Cpos:"+currentPos, 0, 0);
			LCD.refresh();
		}
	}
	public static int getCurrentPos() {
		return currentPos;
	}

	public static void setNextPos(int newPos) {
		if (newPos != nextPos) {
			nextPos = newPos;
			LCD.clear(1);
			LCD.drawString("Npos:"+nextPos, 0, 1);
			LCD.refresh();
		}
	}
	public static int getNextPos() {
		return nextPos;
	}
}
