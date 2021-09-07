package com.javatechie.aws.lambda;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javatechie.aws.lambda.domain.Order;
import com.javatechie.aws.lambda.respository.OrderDao;

@SpringBootApplication
@EnableFeignClients
public class SpringbootAwsLambdaApplication {

	@Autowired
	private OrderDao orderDao;

	@Autowired
	TmsxFeignClient client;

	@Value("${tmsxurl}")
	String tmsxurl;

	@Bean
	public Supplier<List<Order>> orders() {
		System.out.println("################ AWS lmada start ######################");
		try {
			List<Map<String, Object>> map = client.getCustomerAccounts();
			System.out.println(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("################ AWS lmada start ######################");
		return () -> orderDao.buildOrders();
	}

	@Bean
	public Supplier<List<Order>> tmsx() {
		try {
			System.out.println("################ java http start ######################");
			// Create a neat value object to hold the URL
			URL url = new URL(tmsxurl);

			// Open a connection(?) on the URL(??) and cast the response(???)
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// Now it's "open", we can set the request method, headers etc.
			connection.setRequestProperty("accept", "application/json");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Authorization", "Basic YXBpLnVzZXI6YWJjQDEyMzQ=");
			connection.setConnectTimeout(60 * 1000);
			connection.setReadTimeout(60 * 1000);

			// This line makes the request
			InputStream responseStream = connection.getInputStream();

			// Manually converting the response body InputStream to APOD using Jackson
			ObjectMapper mapper = new ObjectMapper();
			List<Map<String, Object>> map = mapper.readValue(responseStream.toString(),
					new TypeReference<List<Map<String, Object>>>() {
					});
			// Finally we have the response
			System.out.println(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("################ java http end ######################");
		return () -> orderDao.buildOrders();
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringbootAwsLambdaApplication.class, args);
	}

}
