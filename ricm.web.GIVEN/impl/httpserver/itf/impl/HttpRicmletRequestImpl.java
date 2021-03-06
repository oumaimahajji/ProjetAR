package httpserver.itf.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import httpserver.itf.HttpResponse;
import httpserver.itf.HttpRicmlet;
import httpserver.itf.HttpRicmletRequest;
import httpserver.itf.HttpRicmletResponse;
import httpserver.itf.HttpSession;

public class HttpRicmletRequestImpl extends HttpRicmletRequest{

	HashMap<String, String> m_arguments;
	
	public HttpRicmletRequestImpl(HttpServer hs, String method, String ressname, BufferedReader br) throws IOException {
		super(hs, method, ressname, br);
		m_arguments = new HashMap<String, String>();
	}

	@Override
	public HttpSession getSession() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getArg(String name) {	//returning the arguments that corresponds to this key
		return m_arguments.get(name);
	}

	@Override
	public String getCookie(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void process(HttpResponse resp) throws Exception {
		if (!m_method.equals("GET")) {	//we only handle GET
			System.out.println("This is not a GET method");
		} else {
			String m_class = null;
			System.out.println(m_ressname);
			if (!m_ressname.contains("?")) {	//if there are no arguments
				//changing the '/' into '.' so that we could get the path to the example in the package
				//we get 'examples' with substring(10,18) and the name of the example with the rest substring(19)
				m_class = m_ressname.substring(10,18)+ "." + m_ressname.substring(19);	
				System.out.println(m_class);
			} else {	//if there are arguments, we need to fetch them
				//getting the name of the class
				int n = m_ressname.indexOf("?");	//getting the position of the '?' in the request. 
													//This position indicates the end of the name of the class
				m_class = m_ressname.substring(10,18)+ "." + m_ressname.substring(19,n);
				//getting the arguments of the request
				String all_args[] = (m_ressname.substring(n+1)).split("&");	//we split the last part of the command that contains the arguments 
																			//that starts after '?' we also know that arguments are devided by '&' 
				int i = 0;
				while(i < all_args.length) {	//for each one of the arguments
					String one_arg[] = all_args[i].split("=");	//we create an array where we store the value of the argument and the key
					m_arguments.put(one_arg[0], one_arg[1]);	//we store the argument and the key
					i++;
				}
			}
			try {
				HttpRicmlet m_ricmlet = m_hs.getInstance(m_class);
				m_ricmlet.doGet(this, (HttpRicmletResponse) resp);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				resp.setReplyError(404, "Ricmlet not found");
			}
		}
	
	}
}
