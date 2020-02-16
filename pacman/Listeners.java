package pacman;

/*
 * gets input from keyboard
 */

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Listeners extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
       switch (e.getKeyCode()) {
       	case KeyEvent.VK_LEFT:
    	   	GameClass.left = true;
           	break;
       	case KeyEvent.VK_RIGHT:
    	   	GameClass.right = true;
    	   	break;
   		case KeyEvent.VK_UP:
    	   	GameClass.up = true;
           	break;
   		case KeyEvent.VK_DOWN:
    	   	GameClass.down = true;
           	break;
           
   		case KeyEvent.VK_A:
    	   	GameClass.left = true;
   			break;
   		case KeyEvent.VK_D:
    	   	GameClass.right = true;
    	   	break;
   		case KeyEvent.VK_W:
    	   GameClass.up = true;
           break;
   		case KeyEvent.VK_S:
    	   GameClass.down = true;
           break;
           
   		case KeyEvent.VK_SPACE:
    	   GameClass.spacebar = true;
    	   break;
       }
    }
    
    @Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
    		GameClass.left = false;
    		break;
    	case KeyEvent.VK_RIGHT:
     	   	GameClass.right = false;
            break;
    	case KeyEvent.VK_UP:
     	   	GameClass.up = false;
            break;
        case KeyEvent.VK_DOWN:
     	   	GameClass.down = false;
            break;
            
        case KeyEvent.VK_A:
    		GameClass.left = false;
    		break;
    	case KeyEvent.VK_D:
     	   	GameClass.right = false;
            break;
    	case KeyEvent.VK_W:
     	   	GameClass.up = false;
            break;
        case KeyEvent.VK_S:
     	   	GameClass.down = false;
            break;
            
        case KeyEvent.VK_SPACE:
     	   	GameClass.spacebar = false;
     	   	break;
    	}
    }
 }
