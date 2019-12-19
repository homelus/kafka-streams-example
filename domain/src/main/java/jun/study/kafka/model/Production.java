package jun.study.kafka.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "production")
@EqualsAndHashCode
public class Production {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long productionNo;
    private String name;
    private String desc;

}
