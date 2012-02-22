
/*
    chatter-bot-api
    Copyright (C) 2011 pierredavidbelanger@gmail.com
 
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

	import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Random;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import com.skype.ChatMessage;
	import com.skype.ChatMessageAdapter;
	import com.skype.Skype;
	import com.skype.SkypeException;
import org.eclipse.swt.*;
	
public class ChatterBotApiTest {
	

    public static ChatterBotFactory factory;// = new ChatterBotFactory();
    public static ChatterBot bot1; // = factory.create(ChatterBotType.CLEVERBOT);
    public static ChatterBotSession bot1session; // = bot1.createSession();
    public static Stack<ChatMessage> st = new Stack<ChatMessage>();
    
    public static void main(String[] args) throws Exception {

    	//System.loadLibrary("./swt-win32-3325");
    	
    	factory = new ChatterBotFactory();
    	bot1 = factory.create(ChatterBotType.CLEVERBOT);
    	bot1session = bot1.createSession();
    	
    	Skype.setDeamon(false); // to prevent exiting from this program
        Skype.addChatMessageListener(new ChatMessageAdapter() {

        	Timer timer;
        	
            public void chatMessageReceived(ChatMessage received) throws SkypeException {
            	String s = "";
                if (received.getType().equals(ChatMessage.Type.SAID)) {
                	s = received.getContent();
                	
                	st.push(received);
                	
                	if(timer != null){
                		timer.cancel();
                	}
                	
                	timer = new Timer();
                	Calendar cal = Calendar.getInstance();
                	Random rand = new Random(cal.getTimeInMillis());
                	int seconds = rand.nextInt(5) + 2;
                	
                    timer.schedule(new SendReply(), seconds * 1000);
                    
                }
            }
        });
        
    }
    
}

class SendReply extends TimerTask {
    public void run() {
    	try {
    		ChatMessage m = ChatterBotApiTest.st.pop();
			String s = ChatterBotApiTest.bot1session.think(m.getContent());
			m.getChat().send(s);
			ChatterBotApiTest.st.clear();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
  }