package idv.allen.demo;


import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestService {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void save() {
		
		if(entityManager.createQuery("SELECT m FROM Member m ", Member.class).getResultStream().findAny().isEmpty()==false) {
			LOGGER.info("Table is full");
			entityManager.clear();
			entityManager.close();
			return;
		}
		
		Member member;
		for(int i=1; i<1000001; i++) {
			member = Member.builder()
						   .name("abc" + Integer.valueOf(i).toString())
						   .age(i%1000)
						   .createDate(new Date())
						   .build();
			entityManager.persist(member);
			LOGGER.info(member);
		}
		entityManager.flush();
		entityManager.clear();
		entityManager.close();
		LOGGER.info("save done");
	}

	
	@Transactional(readOnly=true)
	public void stream(final ServletOutputStream os) throws Exception {
		
		try(Stream<Member> members = entityManager.createQuery("select m from Member m where m.id <= 10000", Member.class)
											  	  .getResultStream(); final ServletOutputStream o = os){
			LOGGER.debug("開始迴圈");
			members.forEachOrdered(member -> {
				entityManager.detach(member);
				LOGGER.debug(member);
				try {
					o.print("<BR>" + member.toString() + "</BR>");
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
				
			});
		}
	}


	@Transactional(readOnly=true)
	public void list(final ServletOutputStream os) throws Exception {
		List<Member> members = entityManager.createQuery("select m from Member m where m.id <= 10000", Member.class)
											.getResultList();
		LOGGER.debug("開始迴圈");
		
		try(final OutputStream o = os){
			for(Member member : members) {
				entityManager.detach(member);
				LOGGER.debug(member);
				try {
					os.print("<BR>" + member.toString() + "</BR>");
				} catch (IOException e) {
					e.printStackTrace();
					break;
				} 
			}
		}
		
	}

	@Cacheable(value="MemberCache", key = "#id")
	@Transactional(readOnly=true)
	public Member select(Long id) {
		Member member =  entityManager.createQuery("select m from Member m where m.id = :id", Member.class)
									  .setParameter("id", id)
									  .getSingleResult();
		LOGGER.debug("查找資料>>>" + member);
		return member;
	}
	
	@KafkaListener(topics="MyTopic", groupId="MyGroup")
	public void kafkaListener(@Headers MessageHeaders headers, @Payload String message) {
		LOGGER.debug("message >>> " + message);
		headers.keySet()
			   .forEach(key -> {
			       LOGGER.debug("key >>> " + key + ", value >>> " + headers.get(key));
		       });
	}

}
