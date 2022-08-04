package io.vividcode.happyride.addressservice.service;

import io.vividcode.happyride.addressservice.api.AreaVO;
import io.vividcode.happyride.addressservice.model.Area;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional
public class AreaService {

  public Optional<AreaVO> getArea(Long areaCode, int ancestorLevel) {
    return Area.findByAreaCode(areaCode)
        .map(area -> AddressHelper
            .fromArea(area, this.getAreaWithHierarchy(area.parentCode, ancestorLevel)));
  }

  public List<AreaVO> getAreaWithHierarchy(Long areaCode, int level) {
    return this.getAreaWithHierarchy(Area.findByAreaCode(areaCode), level);
  }

  public List<AreaVO> getAreaWithHierarchy(Area area, int level) {
    return this.getAreaWithHierarchy(Optional.ofNullable(area), level);
  }

  private List<AreaVO> getAreaWithHierarchy(Optional<Area> areaToFind, int level) {
    int currentLevel = Math.max(level, 0);
    Optional<Area> currentArea = areaToFind;
    List<Area> areas = new ArrayList<>();
    while (currentLevel-- > 0 && currentArea.isPresent()) {
      Area area = currentArea.get();
      areas.add(area);
      currentArea = Area.findByAreaCode(area.parentCode);
    }
    return areas.stream().map(AddressHelper::fromArea).collect(Collectors.toList());
  }
}
