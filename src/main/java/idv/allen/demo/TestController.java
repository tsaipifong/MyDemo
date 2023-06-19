package idv.allen.demo;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	@Autowired
	private TestService testService;
	
	@Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
	
	@GetMapping("/Test")
	public String test() {
		LOGGER.debug("test");
		return "This is a test page!" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
	}
	
	@GetMapping("/Save")
	public String save() {
		LOGGER.debug("save");
		testService.save();
		return "Save done";
	}
	
	@GetMapping("/Stream")
	public void stream(HttpServletResponse response) throws Exception {
		ServletOutputStream os = response.getOutputStream();
		testService.stream(os);
	}
	
	@GetMapping("/List")
	public void list(HttpServletResponse response) throws Exception {
		ServletOutputStream os = response.getOutputStream();
		testService.list(os);
	}
	
	@GetMapping("/Select/{id}")
	public String select(@PathVariable Long id) {
		return testService.select(id).toString();
	}
	
	@GetMapping("/Send/{msg}")
	public void send(@PathVariable String msg) {
		kafkaTemplate.send("MyTopic", msg);
	}

}
