package utils.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import snake.ui.Display;
import utils.io.Logger;

/**
 * 
 * @author Cedric
 * @version 1.0
 * @category util</br></br>
 * 
 * This is a basic KeyListener which lets you listen for any key you want to listen.
 * 
 */
public class KeyManager implements KeyListener {

	// TODO: Add Key-Bindings-File-Adapter
	
	// ******************
	// * Private fields *
	// ******************
	private boolean[] keysPressed;
	
	private int keyBindings[];
	
	private ArrayList<Integer> listenTypedKeys;
	private Queue<Integer> otherKeysTyped;
	
	/**
	 * Constructor: Creates a new KeyManager Object
	 */
	public KeyManager() {
		keysPressed = new boolean[KeyEvent.RESERVED_ID_MAX];
		keyBindings = new int[KeyEvent.RESERVED_ID_MAX];
		// use default-keyBindings in usual cases
		Arrays.fill(keyBindings, -1);
		
		listenTypedKeys = new ArrayList<>();
		otherKeysTyped = new LinkedList<>();
	}
	
	// *******************
	// * Private Methods *
	// *******************
	/**
	 * Adds a new Key-Code to the typed keys.
	 * 
	 * @param keyCode KeyCode of the typed key
	 */
	private void activateTypedKey(int keyCode) {
		otherKeysTyped.add(new Integer(keyCode));
	}

	// ******************
	// * Public methods *
	// ******************
	
	public void changeKeyBinding(int keyCodeChange, int keyCodeNew) {
		keyBindings[keyCodeChange] = keyCodeNew;
	}
	
	public void removeKeyBinding(int keyCode) {
		keyBindings[keyCode] = -1;
	}
	
	public int getKeyBinding(int keyCode) {
		return keyBindings[keyCode];
	}
	
	// Game Keys
	/**
	 * Returns whether the given key is pressed or released.
	 * 
	 * @param keyCode The key you want to check
	 * @return
	 */
	public boolean isKeyDown(int keyCode) {
		return keyBindings[keyCode] == -1 ? keysPressed[keyCode] : keysPressed[keyBindings[keyCode]];
	}
	
	// Typed Keys
	public void addKeyToListener(int i) {
		Logger.gdL().logInfo("KeyListener for key " + i + " added");
		listenTypedKeys.add(new Integer(i));
	}
	
	public boolean removeKeyFromListener(int i) {
		Logger.gdL().logInfo("Trying to remove KeyListener for key " + i); // Update this line of code for independent use of Game
		return listenTypedKeys.remove(new Integer(i));
	}
	
	public int handleTypedInt() {
		if (otherKeysTyped.size() == 0)
			return -1;
		else
			return otherKeysTyped.remove();
	}

	
	// * Key-Listener *
	@Override
	public void keyTyped(KeyEvent e) {
		// NOT WORKING
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = 0;
		if (keyBindings[e.getKeyCode()] == -1)
			keyCode = e.getKeyCode();
		else
			keyCode = keyBindings[e.getKeyCode()];
		keysPressed[keyCode] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = 0;
		if (keyBindings[e.getKeyCode()] == -1)
			keyCode = e.getKeyCode();
		else
			keyCode = keyBindings[e.getKeyCode()];
		keysPressed[keyCode] = false;
				
		for (Integer i : listenTypedKeys) {
			if (i.equals(new Integer(keyCode))) {
				activateTypedKey(keyCode);
				break;
			}
		}
	}

}
