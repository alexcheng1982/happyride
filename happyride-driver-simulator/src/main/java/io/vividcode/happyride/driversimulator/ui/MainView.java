package io.vividcode.happyride.driversimulator.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import io.vividcode.happyride.common.DriverState;
import io.vividcode.happyride.driversimulator.DriverSimulator;
import io.vividcode.happyride.driversimulator.DriverSimulatorRegistry;
import io.vividcode.happyride.driversimulator.DriverSimulatorSnapshot;
import io.vividcode.happyride.driversimulator.web.AddDriverRequest;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes, viewport-fit=cover")
@Theme(value = Lumo.class, variant = Lumo.LIGHT)
@PageTitle("司机模拟器")
@Push
@Route("")
public class MainView extends AppLayout {

  @Autowired
  DriverSimulatorRegistry driverSimulatorRegistry;

  private Grid<DriverSimulatorSnapshot> grid;
  private AtomicInteger driverId = new AtomicInteger(1);

  public MainView() {
    H3 heading = new H3("司机模拟器");
    addToNavbar(heading);

    grid = new Grid<>();
    grid.addColumn(DriverSimulatorSnapshot::getDriverId).setHeader("司机");
    grid.addColumn(DriverSimulatorSnapshot::getState).setHeader("状态");
    grid.addColumn(DriverSimulatorSnapshot::getPosLng).setHeader("经度");
    grid.addColumn(DriverSimulatorSnapshot::getPosLat).setHeader("纬度");
    grid.addComponentColumn(this::createActions).setHeader("动作");

    Div toolbar = new Div();
    Button addButton = new Button("添加司机");
    addButton.addClickListener(event -> this.addDriver());
    toolbar.add(addButton);

    Div mainDiv = new Div();
    mainDiv.add(toolbar, grid);
    setContent(mainDiv);
  }

  private List<DriverSimulatorSnapshot> getData() {
    return driverSimulatorRegistry.list().stream().map(DriverSimulator::dump)
        .collect(Collectors.toList());
  }

  private void addDriver() {
    AddDriverRequest request = new AddDriverRequest();
    request.setDriverId(Integer.toString(driverId.getAndIncrement()));
    driverSimulatorRegistry.add(request);
    updateData();
  }

  private HorizontalLayout createActions(DriverSimulatorSnapshot item) {
    return new HorizontalLayout(
        createStartButton(item),
        createStopButton(item)
    );
  }

  private Button createStartButton(DriverSimulatorSnapshot item) {
    Button button = new Button("启动", event -> {
      driverSimulatorRegistry.start(item.getId());
      updateData();
    });
    button.setVisible(item.getState() == DriverState.OFFLINE);
    return button;
  }

  private Button createStopButton(DriverSimulatorSnapshot item) {
    Button button = new Button("停止", event -> {
      driverSimulatorRegistry.stop(item.getId());
      updateData();
    });
    button.setVisible(item.getState() != DriverState.OFFLINE);
    return button;
  }

  @PostConstruct
  private void updateData() {
    grid.setItems(getData());
  }

  @Scheduled(fixedDelay = 5000)
  private void uiUpdateData() {
    getUI().ifPresent(ui -> ui.access(this::updateData));
  }
}
