package com.lu.gademo.controller;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BaseController {
	@Autowired
	protected HttpServletRequest request;
	
	@Autowired
	protected HttpServletResponse response;

	/**
     * 带参重定向
     *
     * @param path
     * @return
     */
    protected String redirect(String path) {
        return "redirect:" + path;
    }

    /**
     * 不带参重定向
     *
     * @param response
     * @param path
     * @return
     */
    protected String redirect(HttpServletResponse response, String path) {
        try {
            response.sendRedirect(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 获取分页请求
     * @return
     */
    protected PageRequest getPageRequest(){
    	int page = 1;
    	int size = 10;
    	Sort sort =null;
    	try {
    		String sortName = request.getParameter("sortName");
    		String sortOrder = request.getParameter("sortOrder");
    		if(StringUtils.isNoneBlank(sortName) && StringUtils.isNoneBlank(sortOrder)){
    			if(sortOrder.equalsIgnoreCase("desc")){
    				sort = Sort.by(Direction.DESC, sortName);
    			}else{
    				sort =Sort.by(Direction.ASC, sortName);
    			}
    		}
    		page = Integer.parseInt(request.getParameter("pageNumber"))-1;
    		size = Integer.parseInt(request.getParameter("pageSize"));
    		System.out.println("page%%%%"+page);
    		System.out.println("size%%%%"+size);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	PageRequest pageRequest = PageRequest.of(page, size);
    	return pageRequest;
    }
    
    /**
     * 获取分页请求
     * @param sort 排序条件
     * @return
     */
    protected PageRequest getPageRequest(Sort sort){
    	int page = 0;
    	int size = 10;
    	try {
    		String sortName = request.getParameter("sortName");
    		String sortOrder = request.getParameter("sortOrder");
    		if(StringUtils.isNoneBlank(sortName) && StringUtils.isNoneBlank(sortOrder)){
    			if(sortOrder.equalsIgnoreCase("desc")){
    				sort.and(Sort.by(Direction.DESC, sortName));
    			}else{
    				sort.and(Sort.by(Direction.ASC, sortName));
    			}
			}
    		page = Integer.parseInt(request.getParameter("pageNumber")) - 1;
    		size = Integer.parseInt(request.getParameter("pageSize"));
		} catch (Exception e) {
			e.printStackTrace();
		}
    	PageRequest pageRequest = PageRequest.of(page, size, sort);
    	return pageRequest;
    }

}
