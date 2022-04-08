package com.itl.mes.andon.api.bo;

import com.itl.mes.andon.api.constant.BOPrefixEnum;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * @author 崔翀赫
 * @date 2020/12/15$
 * @since JDK1.8
 */
public class GradeHandleBO implements Serializable {

  private static final String PREFIX = BOPrefixEnum.AG.getPrefix();

  private String bo;
  private String site;
  private int andonGrade;

  public GradeHandleBO(String bo) {
    this.bo = bo;
    String[] split = bo.split(":")[1].split(",");
    this.site = split[0];
    this.andonGrade = Integer.parseInt(split[1]);
  }

  public GradeHandleBO(String site, int andonGrade) {
    this.site = site;
    this.andonGrade = andonGrade;
    this.bo =
        new StringBuilder(PREFIX)
            .append(":")
            .append(site)
            .append(",")
            .append(andonGrade)
            .toString();
  }

  public String getBo() {
    return bo;
  }

  public String getSite() {
    return site;
  }

  public int getAndonGrade() {
    return andonGrade;
  }
}
