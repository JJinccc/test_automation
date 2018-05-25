package com.ffan.qa.network;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.filter.LoggingFilter;

public class APIRequest {

	private UriBuilder uri;
	private Map<String, String> params = new HashMap<String, String>();
	private Map<String, String> headers = new HashMap<String, String>();
	private MediaType contentType = MediaType.APPLICATION_JSON_TYPE;
	private MediaType acceptType;
	private String httpMethod;
	private String body;
	
	private APIRequest(String uri, String method)
	{
		this.uri = UriBuilder.fromUri(uri);
		this.httpMethod = method;
	}
	
	public static APIRequest GET(String uri)
	{
		return new APIRequest(uri, HttpMethod.GET);
	}
	
	public static APIRequest POST(String uri)
	{
		return new APIRequest(uri, HttpMethod.POST);
	}
	
	public static APIRequest DELETE(String uri)
	{
		return new APIRequest(uri, HttpMethod.DELETE);
	}
	
	public static APIRequest PUT(String uri)
	{
		return new APIRequest(uri, HttpMethod.PUT);
	}
	
	public static APIRequest HEAD(String uri)
	{
		return new APIRequest(uri, HttpMethod.HEAD);
	}
	
	public APIRequest path(String value)
	{
		this.uri.path(value);
		return this;
	}
	
	public APIRequest param(String key, String value)
	{
		params.put(key, value);
		return this;
	}
	
	public APIRequest type(MediaType type)
	{
		this.contentType = type;
		return this;
	}
	
	public APIRequest accept(MediaType type)
	{
		this.acceptType = type;
		return this;
	}
	
	public APIRequest header(String key, String value)
	{
		headers.put(key, value);
		return this;
	}

	public APIRequest headers(Map<String, String> headers) {
		if (null != headers && !headers.isEmpty()) {
			this.headers = headers;
		}

		return this;
	}
	
	public APIRequest body(String body)
	{
		this.body = body;
		return this;
	}
	
	public APIResponse invoke()
	{
		ClientConfig config = new ClientConfig();
		
		config.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
		
		Client client = ClientBuilder.newClient(config);
		client.register(new LoggingFilter(Logger.getLogger(APIResponse.class.getName()),true));
		
		WebTarget webTarget = client.target(uri);
		
		if(!params.isEmpty())
		{
			for(Entry<String, String> key: params.entrySet())
			{
				webTarget = webTarget.queryParam(key.getKey(), key.getValue());
			}
		}
		
		Invocation.Builder invocationBuilder= webTarget.request();
		if(acceptType != null)
		{
			invocationBuilder = invocationBuilder.accept(acceptType);
		}

		if(!headers.isEmpty())
		{
			for(String key: headers.keySet())
			{
				invocationBuilder.header(key, headers.get(key));
			}
		}
		
		Response response;

		Long start = new Date().getTime();

		if(body == null)
		{
			response= invocationBuilder.method(httpMethod, Response.class);
		}
		else
		{
			response = invocationBuilder.method(httpMethod, Entity.entity(body, contentType), Response.class);
		}

		Long end = new Date().getTime();
		
		return new APIResponse(response, end - start);
	}
}
