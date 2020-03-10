package io.vividcode.happyride.passengerservice.ui;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.validator.BigDecimalRangeValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import io.vividcode.happyride.passengerservice.api.web.PassengerView;
import io.vividcode.happyride.passengerservice.service.PassengerService;
import io.vividcode.happyride.passengerservice.service.TripServiceProxy;
import io.vividcode.happyride.tripservice.client.ApiException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes, viewport-fit=cover")
@Theme(value = Lumo.class, variant = Lumo.LIGHT)
@PageTitle("乘客管理")
@Push
@Route("")
@Slf4j
public class MainView extends AppLayout {

  @Autowired
  PassengerService passengerService;

  @Autowired
  TripServiceProxy tripServiceProxy;

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
      createTripDialog(item).open();
    });
  }

  private Dialog createTripDialog(PassengerView passengerView) {
    FormLayout formLayout = new FormLayout();
    Binder<SimpleCreateTripRequest> binder = new Binder<>();
    SimpleCreateTripRequest request = new SimpleCreateTripRequest();

    BigDecimalField startLng = new BigDecimalField();
    BigDecimalField startLat = new BigDecimalField();
    BigDecimalField endLng = new BigDecimalField();
    BigDecimalField endLat = new BigDecimalField();

    formLayout.addFormItem(startLng, "起始位置：经度");
    formLayout.addFormItem(startLat, "起始位置：纬度");
    formLayout.addFormItem(endLng, "结束位置：经度");
    formLayout.addFormItem(endLat, "结束位置：纬度");

    startLng.setRequiredIndicatorVisible(true);
    startLat.setRequiredIndicatorVisible(true);
    endLng.setRequiredIndicatorVisible(true);
    endLat.setRequiredIndicatorVisible(true);

    binder
        .forField(startLng)
        .asRequired("起始位置经度不能为空")
        .withValidator(validatorForLng())
        .bind(SimpleCreateTripRequest::getStartLng, SimpleCreateTripRequest::setStartLng);
    binder
        .forField(startLat)
        .asRequired("起始位置纬度不能为空")
        .withValidator(validatorForLat())
        .bind(SimpleCreateTripRequest::getStartLat, SimpleCreateTripRequest::setStartLat);
    binder
        .forField(endLng)
        .asRequired("结束位置经度不能为空")
        .withValidator(validatorForLng())
        .bind(SimpleCreateTripRequest::getEndLng, SimpleCreateTripRequest::setEndLng);
    binder
        .forField(endLat)
        .asRequired("结束位置纬度不能为空")
        .withValidator(validatorForLat())
        .bind(SimpleCreateTripRequest::getEndLat, SimpleCreateTripRequest::setEndLat);

    Button create = new Button("创建");
    Button close = new Button("关闭");
    HorizontalLayout actions = new HorizontalLayout();
    actions.add(create, close);

    Label infoLabel = new Label();

    Dialog dialog = new Dialog();
    dialog.setWidth("500px");
    dialog.setCloseOnOutsideClick(false);
    VerticalLayout main = new VerticalLayout();
    H3 heading = new H3("创建行程");
    main.add(heading, infoLabel, formLayout, actions);
    dialog.add(main);

    create.addClickListener(event -> {
      infoLabel.setText("");
      if (binder.writeBeanIfValid(request)) {
        try {
          tripServiceProxy.createTrip(request.toCreateTripRequest(passengerView.getId()));
          dialog.close();
          Notification.show(String.format("乘客 %s 的行程创建成功", passengerView.getName()), 3000,
              Position.BOTTOM_CENTER);
        } catch (ApiException e) {
          log.warn("创建行程失败", e);
          infoLabel.setText("创建行程失败");
        }
      } else {
        BinderValidationStatus<SimpleCreateTripRequest> validate = binder.validate();
        String errorText = validate.getFieldValidationStatuses()
            .stream().filter(BindingValidationStatus::isError)
            .map(BindingValidationStatus::getMessage)
            .map(Optional::get).distinct()
            .collect(Collectors.joining(", "));
        infoLabel.setText("创建出错： " + errorText);
      }
    });
    close.addClickListener(event -> dialog.close());

    return dialog;
  }

  private BigDecimalRangeValidator validatorForLng() {
    return new BigDecimalRangeValidator("经度范围是-180到180。", BigDecimal.valueOf(-180),
        BigDecimal.valueOf(180));
  }

  private BigDecimalRangeValidator validatorForLat() {
    return new BigDecimalRangeValidator("纬度范围是-90到90。", BigDecimal.valueOf(-90),
        BigDecimal.valueOf(90));
  }
}
