package action;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public abstract class Azione {
	
	public Azione(){}

	public abstract String esegui(HttpServletRequest request) throws ServletException, IOException;
}
