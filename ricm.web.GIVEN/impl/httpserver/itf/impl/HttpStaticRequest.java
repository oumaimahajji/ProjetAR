package httpserver.itf.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;

import httpserver.itf.HttpRequest;
import httpserver.itf.HttpResponse;

public class HttpStaticRequest extends HttpRequest {
	static final String DEFAULT_FILE = "index.html";
	
	public HttpStaticRequest(HttpServer hs, String method, String ressname) throws IOException {
		super(hs, method, ressname);
	}
	
	public void process(HttpResponse resp) throws Exception {
		if (!m_method.equals("GET")) {	//we only handle GET
			System.out.println("This is not a GET method");
		} else {	//for functionning requests
			//looking for a file corresponding to the requested page
			File m_filerequest = new File(m_hs.getFolder() + m_ressname);
			if(!m_filerequest.isFile()) {	//if the file is not found
				resp.setReplyError(404, "File not found");
			} else {	//we have found the file, we can now handle the request
				//Let's handle the header first
				resp.setReplyOk();	//Write an OK start-line on the response output stream
				resp.setContentLength((int)m_filerequest.length());	 //setting the length of the file
				resp.setContentType(getContentType(m_ressname));	 //setting the type of the file
				PrintStream m_ps= resp.beginBody();	//printing the empty line to signal the end of the header
				//Let's handle the body now
				FileInputStream fis = new FileInputStream(m_filerequest);
				int size = fis.available();		//gets the size of the file
				byte[] content = new byte[size];	//the array where the content of the file is stored
				int n;
				while ((n = fis.read(content)) >= 0) {	//reading the content of the file and writing it in the printstream
					 m_ps.write(content, 0, n);
				}
				fis.close();
			}
		}
    }
	
	//Step1.5: we tested on the terminal with the command wget http://localhost:8080/FILES/index.html 
	//Step1.6: it worked http://172.18.0.1:8080/FILES/index.html
}
