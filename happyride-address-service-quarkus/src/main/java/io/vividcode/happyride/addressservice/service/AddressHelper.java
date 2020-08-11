package io.vividcode.happyride.addressservice.service;

import io.vividcode.happyride.addressservice.api.AddressVO;
import io.vividcode.happyride.addressservice.api.AreaVO;
import io.vividcode.happyride.addressservice.model.Address;
import io.vividcode.happyride.addressservice.model.Area;
import java.util.Collections;
import java.util.List;

public class AddressHelper {

  public static AddressVO fromAddress(Address address) {
    return fromAddress(address, Collections.emptyList());
  }

  public static AddressVO fromAddress(Address address, List<AreaVO> areas) {
    AddressVO addressVO = new AddressVO();
    addressVO.setId(address.id);
    addressVO.setAreaId(address.area.id);
    addressVO.setAddressLine(address.addressLine);
    addressVO.setLng(address.lng);
    addressVO.setLat(address.lat);
    addressVO.setAreas(areas);
    return addressVO;
  }

  public static AreaVO fromArea(Area area) {
    return fromArea(area, Collections.emptyList());
  }

  public static AreaVO fromArea(Area area, List<AreaVO> ancestors) {
    AreaVO areaVO = new AreaVO();
    areaVO.setId(area.id);
    areaVO.setLevel(area.level);
    areaVO.setParentCode(area.parentCode);
    areaVO.setAreaCode(area.areaCode);
    areaVO.setZipCode(area.zipCode);
    areaVO.setCityCode(area.cityCode);
    areaVO.setName(area.name);
    areaVO.setShortName(area.shortName);
    areaVO.setMergerName(area.mergerName);
    areaVO.setPinyin(area.pinyin);
    areaVO.setLat(area.lat);
    areaVO.setLng(area.lng);
    areaVO.setAncestors(ancestors);
    return areaVO;
  }
}
