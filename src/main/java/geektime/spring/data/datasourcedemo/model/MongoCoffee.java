package geektime.spring.data.datasourcedemo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MongoCoffee {
    @Id
    private String id;
    private String name;
    private Money price;
    private Date createTime;
    private Date updateTime;
}

