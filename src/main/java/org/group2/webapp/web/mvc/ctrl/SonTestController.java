/**
 * 
 */
package org.group2.webapp.web.mvc.ctrl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Dam Cao Son
 * @Contact kunedo1104@gmail.com
 *
 */

@Controller
@RequestMapping("/son")
public class SonTestController {
	@RequestMapping(value = "{page}")
	public String sonIndex(@PathVariable("page") String page) {
		return page;
	}

	@RequestMapping("q")
	public String sonIndex1() {
		return "index";
	}
}
