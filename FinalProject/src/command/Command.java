package command;
import java.io.Serializable;

/**
 *	Command
 *
 * 	<p>This abstract class defines a serializable command that can be sent
 * 	and executed on either a client or server.<p>
 *  
 *  @author The Other Guys
 */

public abstract class Command<T> implements Serializable {
	private static final long serialVersionUID = -4838155228547508978L;
	
	private String source; // client or server name
	
	public Command(String source){
		this.source = source;
	}
	
	public abstract void execute(T executeOn);
	
	public void undo(T undoOn){
		// by default, commands cannot be undone
	}
	
	public String getSource() {
		return source;
	}
}
