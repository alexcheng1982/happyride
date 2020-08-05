package io.vividcode.happyride.addressservice.service;

import io.vividcode.happyride.addressservice.api.AddressVO;
import io.vividcode.happyride.addressservice.api.AreaVO;
import io.vividcode.happyride.addressservice.domain.Address;
import io.vividcode.happyride.addressservice.domain.Area;
import java.util.Collections;
import java.util.List;

public class AddressHelper {

  public static AddressVO fromAddress(Address address) {
    return fromAddress(address, Collections.emptyList());
  }

  public static AddressVO fromAddress(Address address,
      List<AreaVO> areas) {
    AddressVO addressVO = new AddressVO();
    addressVO.setId(address.getId());
    addressVO.setAreaId(address.getArea().getId());
    addressVO.setAreaCode(address.getArea().getAreaCode());
    addressVO.setAddressLine(address.getAddressLine());
    addressVO.setLng(address.getLng());
    addressVO.setLat(address.getLat());
    addressVO.setAreas(areas);
    return addressVO;
  }

  public static AreaVO fromArea(Area area) {
    return fromArea(area, Collections.emptyList());
  }

  public static AreaVO fromArea(Area area, List<AreaVO> ancestors) {
    AreaVO areaVO = new AreaVO();
    areaVO.setId(area.getId());
    areaVO.setLevel(area.getLevel());
    areaVO.setParentCode(area.getParentCode());
    areaVO.setAreaCode(area.getAreaCode());
    areaVO.setZipCode(area.getZipCode());
    areaVO.setCityCode(area.getCityCode());
    areaVO.setName(area.getName());
    areaVO.setShortName(area.getShortName());
    areaVO.setMergerName(area.getMergerName());
    areaVO.setPinyin(area.getPinyin());
    areaVO.setLat(area.getLat());
    areaVO.setLng(area.getLng());
    areaVO.setAncestors(ancestors);
    return areaVO;
  }
}
