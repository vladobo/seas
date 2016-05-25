/*
 * Copyright 2016 ITEA 12004 SEAS Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.thesmartenergy.seas;

import java.io.IOException;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

/**
 * This class filters the calls to ontologies, and dispatches to the ontology
 * resource
 *
 * @author maxime.lefrancois
 */
@WebFilter(urlPatterns = {"/*"})
public class MainFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = ((HttpServletRequest) request);
        String requestURI = req.getRequestURI();
        System.out.println("filter " + this.getClass().getSimpleName() + " for requestURI:" + requestURI);

        // filter calls to main page with Accept text/turtle or Accept application/rdf+xml
        if (requestURI.equals("/seas/")) {
            // check if accept is text/turtle or Accept application/rdf+xml. not true content negociation for now.
            String accept = req.getHeader("Accept");
            if (!accept.contains("*/*")
                    && !accept.contains("text/*")
                    && !accept.contains("*/html")
                    && !accept.contains("text/html")
                    && !accept.contains("application/xhtml+xml")) {
                String newURI = "/seas/ontology/seas";
                System.out.println("dispatching to " + newURI);
                req.getRequestDispatcher(newURI).forward(req, response);
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
