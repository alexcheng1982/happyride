package io.vividcode.happyride.addressservice.service;

import io.vividcode.happyride.addressservice.domain.Area;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import lombok.Data;

@Data
public class AreaView {

  private Integer id;

  private Integer level;

  private Long parentCode;

  private Long areaCode;

  private String zipCode;

  private String cityCode;

  private String name;

  private String shortName;

  private String mergerName;

  private String pinyin;

  private BigDecimal lat;

  private BigDecimal lng;

  private List<AreaView> ancestors;

  public static AreaView fromArea(final Area area) {
    return fromArea(area, Collections.emptyList());
  }

  public static AreaView fromArea(final Area area, final List<AreaView> ancestors) {
    final AreaView areaView = new AreaView();
    areaView.setId(area.getId());
    areaView.setLevel(area.getLevel());
    areaView.setParentCode(area.getParentCode());
    areaView.setAreaCode(area.getAreaCode());
    areaView.setZipCode(area.getZipCode());
    areaView.setCityCode(area.getCityCode());
    areaView.setName(area.getName());
    areaView.setShortName(area.getShortName());
    areaView.setMergerName(area.getMergerName());
    areaView.setPinyin(area.getPinyin());
    areaView.setLat(area.getLat());
    areaView.setLng(area.getLng());
    areaView.setAncestors(ancestors);
    return areaView;
  }
}
