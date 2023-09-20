package com.wirepick.wpksmsencryptionapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.wirepick.wpksmsencryptionapp.model.EncMsg;
import com.wirepick.wpksmsencryptionapp.model.RSAPublicKey;
import com.wirepick.wpksmsencryptionapp.model.RawMsg;
import com.wirepick.wpksmsencryptionapp.model.ResponseDTO;
import com.wirepick.wpksmsencryptionapp.service.EncryptRSAData;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin("*")
public class EncryptSMSController {

	private static final Logger logger = LoggerFactory.getLogger(EncryptSMSController.class);

	@PutMapping(value = "/publickey/update")
	public ResponseEntity<ResponseDTO<String>> toUpdateWpkRSAPublicKey(@RequestBody RSAPublicKey publicKeyBase64String) {
		String res = EncryptRSAData.updatePublicKey(publicKeyBase64String);
		return res!=null ? ResponseEntity.ok(new ResponseDTO<String>("SUCCESS", res)): 
			ResponseEntity.ok(new ResponseDTO<String>("ERROR", null));
	}

	@PostMapping(value="/encrypt/sms")
    public static ResponseEntity<ResponseDTO<EncMsg>> sendEncryptSMS(@RequestBody RawMsg rawMsg) {
        
		EncMsg fullencData = null;
	
        try {
			fullencData = EncryptRSAData.encryptContentMessage(rawMsg.getMessage());

		} catch (Exception ex) {
			logger.error("Throwable thrown in encryptSmsMessage. Reason:" + ex.getMessage());
		}

		return fullencData!=null ? ResponseEntity.ok(new ResponseDTO<EncMsg>("SUCCESS", fullencData)):
			ResponseEntity.ok(new ResponseDTO<EncMsg>("ERROR", null));
    }
    
}
