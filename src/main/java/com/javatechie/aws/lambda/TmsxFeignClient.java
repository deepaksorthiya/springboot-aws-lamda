package com.javatechie.aws.lambda;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "Tmsx", url = "${tmsxurl}", configuration = { FeignClientConfiguration.class })
public interface TmsxFeignClient {

	@RequestMapping(method = RequestMethod.GET, value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	List<Map<String, Object>> getCustomerAccounts();
}