package idv.allen.demo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="MEMBER", schema="SYSTEM")
@SequenceGenerator(name = "MemberSeq", sequenceName = "MEMBER_SEQ", allocationSize=1)
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member implements Serializable {
	
	private static final long serialVersionUID = 5887033688384131970L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MemberSeq")
	private Long id;
	
	@Column(name="NAME", length=20, nullable=false, unique=false)
	private String name;
	
	@Column(name="AGE", nullable=false)
	private Integer age;
	
	@Column(name="CREATE_DATE", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

}
