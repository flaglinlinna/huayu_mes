package com.app.base.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import com.system.session.service.MySessionService;

public abstract class WebController extends BaseController {

	protected String autoView(String name) {
        final Resource resource = getApplicationContext().getResource("classpath:/templates/" + name + ".html");
        if (resource != null && resource.exists()) {
            return name;
        }
        return "_" + name;
    }
}
