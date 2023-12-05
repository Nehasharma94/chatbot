package com.entit.chatbot.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Service
public class Response {

	
		
		private  Map<String, Object> requestToWhatsapp;
	    private  Map<String, Object> requestFromWhatsapp;
	    private Map<String, String> errorResponse;
		
	    public Response requestToWhatsapp(Map<String, Object> requestToWhatsapp) {
	        this.requestToWhatsapp = requestToWhatsapp;
	        return this;
	      }
	    
	    public Response requestFromWhatsapp(Map<String, Object> requestFromWhatsapp) {
	        this.requestFromWhatsapp = requestFromWhatsapp;
	        return this;
	      }
	    
	    public Response errorResponse(Map<String, String> errorResponse) {
	        this.errorResponse = errorResponse;
	        return this;
	      }


		
	

}
