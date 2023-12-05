package com.entit.chatbot.service;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.entit.chatbot.controller.WebhookController;

@Service
public class ChatbotService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private Response responseClass;

	private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

	String accessToken = "EAASX8iAvNMEBOzbZCsY3K1kto9Mr3ZCyF0cI25uy0FaXvAaPHaKbT5z1yPQoZC9ZBDXjgJpsY8F9K0JPQthFTUPED0Su6V5wsd5wENqTxZCTrSSPHKaU1w7PGeQp5ZCmzMUY3YuetDRscrhYXxDsnovFJqawkVy0ejhmJVrL1GBCZCsZA5CwafSZA7dezmydqIZCrgRIH8agnSQx5HOPuJhrTfPWelK78ZD";

	public Response sendResponseToUser(String webhookRequest) {

		try {
			// Parse the JSON string into a JSONObject
			JSONObject jsonObject = new JSONObject(webhookRequest);
			
			System.out.println("jsonobject-----------" + jsonObject);

			if (jsonObject.has("object") && jsonObject.has("entry")) {
				JSONArray entryArray = jsonObject.getJSONArray("entry");
				JSONObject entryObject = entryArray.getJSONObject(0);
				JSONObject changesObject = entryObject.getJSONArray("changes").getJSONObject(0);

				if (!changesObject.has("statuses")) {
					JSONObject valueObject = changesObject.getJSONObject("value");
					String messagingProduct = valueObject.get("messaging_product").toString();
					String phoneNumberId = valueObject.getJSONObject("metadata").get("phone_number_id").toString();
					if (valueObject.has("messages")) {
						JSONObject button = valueObject.getJSONArray("messages").getJSONObject(0).has("button")
								? valueObject.getJSONArray("messages").getJSONObject(0)
								: null;

						String fromMobileNumber = valueObject.has("messages")
								? valueObject.getJSONArray("messages").getJSONObject(0).get("from").toString()
								: null;
						String body = valueObject.has("messages")
								&& valueObject.getJSONArray("messages").getJSONObject(0).has("text")
										? valueObject.getJSONArray("messages").getJSONObject(0).getJSONObject("text")
												.getString("body")
										: null;
						// System.out.println("bodyyyy" + body);

						// Extract city from request and then choose the template name from db to send.
						String payloadCity = valueObject.getJSONArray("messages").getJSONObject(0).has("button")
								? button.getJSONObject("button").get("payload").toString()
								: null;
						String templateName = "";

						if (valueObject.has("messages")
								&& valueObject.getJSONArray("messages").getJSONObject(0).has("text")
								&& body.equalsIgnoreCase("hi")) {
							templateName = "select_district";
						} else {
							String query = "SELECT tm.template_name FROM template_master tm "
									+ "JOIN cities_master cm ON tm.city_id = cm.id " + "WHERE cm.city = ?";

							// You can use jdbcTemplate.queryForObject with the non-deprecated signature
							templateName = jdbcTemplate.queryForObject(query, String.class, payloadCity);
						}

//						Map<String, Object> requestBody = Map.of("messaging_product", "whatsapp", "to",
//								fromMobileNumber, "type", "template", "template",
//								Map.of("name", templateName, "language", Map.of("code", "en_US")
//
//								));
						Map<String, Object> requestBody = new HashMap<>();
						requestBody.put("messaging_product", "whatsapp");
						requestBody.put("to", fromMobileNumber);
						requestBody.put("type", "template");

						Map<String, Object> templateMap = new HashMap<>();
						templateMap.put("name", templateName);

						Map<String, Object> languageMap = new HashMap<>();
						languageMap.put("code", "en_US");

						templateMap.put("language", languageMap);

						requestBody.put("template", templateMap);

						
						System.out.println("requestBody--" + requestBody);

						System.out.println("Payload--------" + payloadCity);

						HttpHeaders headers = new HttpHeaders();
						headers.setContentType(MediaType.APPLICATION_JSON);

						HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

						String apiUrl = "https://graph.facebook.com/v17.0/" + phoneNumberId + "/messages?access_token="
								+ accessToken;
						// logger.info("API URL: {}", apiUrl);
						System.out.println("API url " + apiUrl);

						try {
							// Make the API call using RestTemplatex1x
							ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST,
									requestEntity, String.class);

							// Check if the API call was successful (HTTP status code 200)
							if (response.getStatusCode().is2xxSuccessful()) {
								String responseBody = response.getBody();
								// Process the response if needed
								// ...
							//	Map<String, Object> responses = new Hash	Map<>();
							//	return ResponseEntity.ok("API call successful");
								
								return responseClass.builder().requestFromWhatsapp(jsonObject.toMap()).requestToWhatsapp(requestBody).build();
							} else {
							    Map<String, String> errorMessage = new HashMap<>();
							    errorMessage.put("Error message", "Unsuccessful");
							    
							    return responseClass.builder().errorResponse(errorMessage).build();
							}

						}	    catch (Exception e) {
							    String errorMessage = "Exception ++ " + e.getMessage();
							    System.out.println(errorMessage);

							    Map<String, String> errorMap = new HashMap<>();
							    errorMap.put("Error message", "Unsuccessful2");

							    return responseClass.builder().errorResponse(errorMap).build();
							}

					} else {
						return null;
					}
				
				}else {
				    Map<String, String> errorMessage = new HashMap<>();
				   
				        errorMessage.put("Error message", "Invalid Request Body");
				    return responseClass.builder().errorResponse(errorMessage).build();
				}}
				else {
				    Map<String, String> errorMessage = new HashMap<>();
				   
				        errorMessage.put("Error message", "Message Body not found");
				    return responseClass.builder().errorResponse(errorMessage).build();
				}}
	

				 catch (JSONException e) {
				    // Handle JSON parsing exception
				    logger.error("Error parsing JSON request: {}", e.getMessage());

				    Map<String, String> errorMessage = new HashMap<>();
				    errorMessage.put("Error message", "Error Parsing JSON request");

				    return responseClass.builder().errorResponse(errorMessage).build();
				}
		
//				} else {
//					return responseClass.builder().errorResponse(Map.of("Error message", "Invalid Request Body" )).build();
//				}
//			} else {
//				return responseClass.builder().errorResponse(Map.of("Error message", "Message Object not found" )).build();
//			}
//		} catch (JSONException e) {
//			// Handle JSON parsing exception
//			logger.error("Error parsing JSON request: {}", e.getMessage());
//			return responseClass.builder().errorResponse(Map.of("Error message", "Error PArsing Json request" )).build();
//		}

	}

}
