package com.itl.mes.andon.api.bo;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.andon.api.constant.BOPrefixEnum;

import java.io.Serializable;

/**
 * @author 崔翀赫
 * @date 2020/12/15$
 * @since JDK1.8
 */
public class PushHandleBO implements Serializable {
  private static final String PREFIX = BOPrefixEnum.AP.getPrefix();

  private String bo;
  private String site;
  private String andonPush;

  public PushHandleBO(String bo) {
    this.bo = bo;
    String[] split = bo.split(":")[1].split(",");
    this.site = split[0];
    this.andonPush = split[1];
  }

  public PushHandleBO(String site, String andonPush) {

    this.site = site;
    this.andonPush = andonPush;

    this.bo =
        new StringBuilder(PREFIX).append(":").append(site).append(",").append(andonPush).toString();
  }

  public String getBo() {
    return bo;
  }

  public String getSite() {
    return site;
  }

  public String getAndonPush() {
    return andonPush;
  }
}
