/**
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe.controllers.jsp;

import org.apache.catalina.Context;
import org.apache.jasper.EmbeddedServletOptions;
import org.apache.jasper.Options;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import psiprobe.Utils;
import psiprobe.controllers.AbstractContextHandlerController;

import java.io.File;
import java.io.FileInputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The Class ViewServletSourceController.
 */
public class ViewServletSourceController extends AbstractContextHandlerController {

  @Override
  protected ModelAndView handleContext(String contextName, Context context,
      HttpServletRequest request, HttpServletResponse response) throws Exception {

    String jspName = ServletRequestUtils.getStringParameter(request, "source", null);
    ServletContext sctx = context.getServletContext();
    ServletConfig scfg = (ServletConfig) context.findChild("jsp");
    Options opt = new EmbeddedServletOptions(scfg, sctx);
    String encoding = opt.getJavaEncoding();
    String content = null;

    if (jspName != null) {
      String servletName =
          getContainerWrapper().getTomcatContainer().getServletFileNameForJsp(context, jspName);

      if (servletName != null) {
        File servletFile = new File(servletName);
        if (servletFile.exists()) {
          try (FileInputStream fis = new FileInputStream(servletFile)) {
            content = Utils.highlightStream(jspName, fis, "java", encoding);
          }
        }
      }
    }
    return new ModelAndView(getViewName(), "content", content);
  }

}
