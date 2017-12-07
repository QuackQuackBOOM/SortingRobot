import lejos.robotics.subsumption.Behavior;

public class BTconnect implements Behavior{
	public void action() {
		int read = MainClass.connection.read(MainClass.buffer, MainClass.MAX_READ);
		int newValue = 0;
//		LCD.drawChar('[', 3, 3);
//		for (int index= 0 ; index < read ; index++) {						
//			LCD.drawChar((char)buffer[index], index + 4, 3);
//		}
//		LCD.drawChar(']', read + 4, 3);
//		connection.write("Reply:".getBytes(), 6);
//		connection.write(buffer, read);
		for (int index = 0; index < read; index ++) {
			newValue += ((int)MainClass.buffer[index] - 48) * Math.pow(10, read - index -1);
		}
		MainClass.cValue = newValue;
		MainClass.connection.write("Reply:".getBytes(), 6);
		MainClass.connection.write(MainClass.buffer, read);
		
	}
	
	public void suppress() {
		
	}
	
	public boolean takeControl() {
		if (MainClass.connection != null) {
			if (MainClass.connection.available() > 0) {
				return true;
			}
		}
		return false;
	}
}
