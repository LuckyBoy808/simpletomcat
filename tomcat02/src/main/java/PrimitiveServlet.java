import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author zzc
 * @create 2020-04-15 13:16
 */
public class PrimitiveServlet implements Servlet {

    private Logger LOGGER = LoggerFactory.getLogger(PrimitiveServlet.class);

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        LOGGER.info("init");
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        LOGGER.info("servlet {} start", getClass().getSimpleName());

        PrintWriter out = servletResponse.getWriter();
        out.println("hello, roses are red");
        out.println("------------------------");
        out.println("violets are blue");

        LOGGER.info("servlet {} start", getClass().getSimpleName());
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {
        LOGGER.info("destroy");
    }
}
