package io.vividcode.happyride.passengerservice.ui;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import io.vividcode.happyride.passengerservice.api.web.PassengerView;
import io.vividcode.happyride.passengerservice.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;

@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes, viewport-fit=cover")
@Theme(value = Lumo.class, variant = Lumo.LIGHT)
@PageTitle("乘客管理")
@Push
@Route("")
public class MainView extends AppLayout {

  @Autowired
  PassengerService passengerService;

  private Grid<PassengerView> grid;

  public MainView() {
    H3 heading = new H3("乘客管理");
    addToNavbar(heading);

    grid = new Grid<>();
    grid.addColumn(PassengerView::getName).setHeader("名字");
    grid.addColumn(PassengerView::getEmail).setHeader("Email");
    grid.addColumn(PassengerView::getMobilePhoneNumber).setHeader("电话");
    grid.addComponentColumn(this::createActions).setHeader("动作");

    setContent(grid);
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    grid.setItems(passengerService.findAll());
  }

  private HorizontalLayout createActions(PassengerView item) {
    return new HorizontalLayout(
        createTripButton(item)
    );
  }

  private Button createTripButton(PassengerView item) {
    return new Button("创建行程", event -> {
      Dialog dialog = new Dialog();
      dialog.open();
    });
  }
}
