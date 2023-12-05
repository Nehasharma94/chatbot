package com.entit.chatbot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.entit.chatbot.service.ChatbotService;
import com.entit.chatbot.service.Response;

@RestController
//@RequestMapping("/chatbotEntit-0.0.1-SNAPSHOT")
public class WebhookController {
	
	@Autowired
	private ChatbotService chatbotService;
	
	private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);
	

//	@Autowired
//	private RestTemplate restTemplate;
//
//	private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

//	String accessToken = "EAAS7iiYv4RwBO2yq1CmQiDE3fZCBNYcbjNGPS4huN11UbcOudBJRUiApApkiv6osTUik2AagA007wbDKWQZCVcsUK8YlJ7DDkn3KcyEqI2ZBC1b1Pc3WhHAmtbcaHVZBRw5WtJpmrpdWTt5U2WxxvJobDCBuMClRAsCQwKo7DiVof7AATo8Nm3jGEPwB75wLYE5NgkDLQ9Ashg66zc6ZCFCzMao4ZD";

	// To verify the callback url from the dashnboard side: cloud api side
	@GetMapping("/webhook")
	public ResponseEntity<Integer> handleWebhook(@RequestParam("hub.mode") String mode,
			 @RequestParam("hub.challenge") Integer challenge, @RequestParam("hub.verify_token") String verifyToken) {
			 System.out.println("Hii");
			 logger.info("Hiiii");
		     String myToken = "entit";	
		
		    logger.info("hub.mode: " + mode);
		    logger.info("hub.challenge: " + challenge);
		    logger.info(verifyToken);
		    System.out.println("hub.challenge: " + challenge);
		    System.out.println("hub.verify_token: " + verifyToken);


		if (mode != null && verifyToken != null && mode.equals("subscribe") && verifyToken.equals(myToken)) {
			return ResponseEntity.status(HttpStatus.OK).body(challenge);
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
	}

	@PostMapping("/webhook")
	public ResponseEntity<Response> sendResponseToUser(@RequestBody String webhookRequest) {

		System.out.println("Request" + webhookRequest);
	 Response response = chatbotService.sendResponseToUser(webhookRequest);
		return new ResponseEntity<>(response, HttpStatus.OK);

//		try {
//			// Parse the JSON string into a JSONObject
//			JSONObject jsonObject = new JSONObject(webhookRequest);
//
//			if (jsonObject.has("object") && jsonObject.has("entry")) {
//				JSONArray entryArray = jsonObject.getJSONArray("entry");
//				JSONObject entryObject = entryArray.getJSONObject(0);
//				JSONObject changesObject = entryObject.getJSONArray("changes").getJSONObject(0);
//
//				if (!changesObject.has("statuses")) {
//					JSONObject valueObject = changesObject.getJSONObject("value");
//					String messagingProduct = valueObject.get("messaging_product").toString();
//					String phoneNumberId = valueObject.getJSONObject("metadata").get("phone_number_id").toString();
//
//					JSONObject button = valueObject.getJSONArray("messages").getJSONObject(0).has("button")
//							? valueObject.getJSONArray("messages").getJSONObject(0)
//							: null;
//					String fromMobileNumber = valueObject.has("messages") ? valueObject.getJSONArray("messages").getJSONObject(0).get("from")
//							.toString() : null;
//					//System.out.println("button----" + button);
//					String payload = valueObject.getJSONArray("messages").getJSONObject(0).has("button")
//							? button.getJSONObject("button").get("payload").toString()
//							: null;
//
//					Map<String, Object> requestBody = Map.of("messaging_product", "whatsapp", "to", fromMobileNumber,
//							"type", "template", "template",
//							Map.of("name", "select_district", "language", Map.of("code", "en_US")
//
//							));
//					System.out.println("requestBody--" + requestBody);
//
//					System.out.println("Payload--------" + payload);
//
//					HttpHeaders headers = new HttpHeaders();
//					headers.setContentType(MediaType.APPLICATION_JSON);
//
//					HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
//
//					String apiUrl = "https://graph.facebook.com/v17.0/" + phoneNumberId + "/messages?access_token="
//							+ accessToken;
//					// logger.info("API URL: {}", apiUrl);
//					System.out.println("API url " + apiUrl);
//					try {
//						// Make the API call using RestTemplatex1x
//						ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity,
//								String.class);
//
//						// Check if the API call was successful (HTTP status code 200)
//						if (response.getStatusCode().is2xxSuccessful()) {
//							String responseBody = response.getBody();
//							// Process the response if needed
//							// ...
//
//							return ResponseEntity.ok("API call successful");
//						} else {
//							// Handle non-successful response
//							return ResponseEntity.status(response.getStatusCode()).body("API call failed");
//						}
//					} catch (Exception e) {
//						String errorMessage = "Exception ++ " + e.getMessage();
//						System.out.println(errorMessage);
//
//						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
//					}
//				} else {
//					return null;
//				}
//			} else {
//				return ResponseEntity.badRequest().body("Invalid request body");
//			}
//		} catch (JSONException e) {
//			// Handle JSON parsing exception
//			logger.error("Error parsing JSON request: {}", e.getMessage());
//			return ResponseEntity.badRequest().body("Error parsing JSON request");
//		}
	}

}
