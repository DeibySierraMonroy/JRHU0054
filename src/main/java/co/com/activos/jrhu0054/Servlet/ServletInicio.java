package co.com.activos.jrhu0054.Servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ServletInicio", value = "/ServletInicio")
public class ServletInicio extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        //TODO: Validar la session (comillas)
        String sessionUsuario = request.getParameter("usu_usuario");
  
      
      // sessionUsuario = "rDNVwajAKeyBCXBylhQaoIQUdkwZcDpexQcdZYkeBblLTksYDmjugfGUoKuYdZfIubfcwlcmIBmHtIthXgTJivzlaXCDxPmwCQoPWULMoVCanzCLqOiMVfuVzhfuBPjLRaXQdEtLBVjWvBxQmkPTvhRCyUKXkuYGwZqREvXrkRiQhsll";
         
        if (sessionUsuario == null) {
            response.sendRedirect("/Resource/error/errorSesion.xhtml");
        }
  
        session.setAttribute("usu_usuario", sessionUsuario);
        response.sendRedirect("Seguimiento.xhtml");

    }

}
