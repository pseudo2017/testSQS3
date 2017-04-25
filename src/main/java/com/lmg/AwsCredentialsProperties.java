package com.lmg;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "amazon.credentials")
public class AwsCredentialsProperties {


    private String param_assumed_role;
    private String param_region;
    private String param_sessionName;
    private String param_queue_name;
    private String param_queue_url;
    private String param_queue_name_new;
    
	public String getParam_queue_name_new() {
		return param_queue_name_new;
	}
	public void setParam_queue_name_new(String param_queue_name_new) {
		this.param_queue_name_new = param_queue_name_new;
	}
	public String getParam_assumed_role() {
		return param_assumed_role;
	}
	public void setParam_assumed_role(String param_assumed_role) {
		this.param_assumed_role = param_assumed_role;
	}
	public String getParam_queue_name() {
		return param_queue_name;
	}
	public void setParam_queue_name(String param_queue_name) {
		this.param_queue_name = param_queue_name;
	}
	public String getParam_queue_url() {
		return param_queue_url;
	}
	public void setParam_queue_url(String param_queue_url) {
		this.param_queue_url = param_queue_url;
	}
	public String getParam_region() {
		return param_region;
	}
	public void setParam_region(String param_region) {
		this.param_region = param_region;
	}
	public String getParam_sessionName() {
		return param_sessionName;
	}
	public void setParam_sessionName(String param_sessionName) {
		this.param_sessionName = param_sessionName;
	}
    


	
}
