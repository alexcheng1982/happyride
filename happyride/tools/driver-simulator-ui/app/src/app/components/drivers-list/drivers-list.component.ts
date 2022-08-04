import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Driver, SimulatorAction } from '../../model';

@Component({
  selector: 'app-drivers-list',
  templateUrl: './drivers-list.component.html',
  styleUrls: ['./drivers-list.component.scss'],
})
export class DriversListComponent implements OnInit {
  @Input() drivers: Driver[];
  @Output() actions = new EventEmitter<SimulatorAction>();

  displayedColumns: string[] = ['id', 'state', 'position', 'actions'];

  constructor() {}

  ngOnInit(): void {}

  sendAction(driver: Driver, action: string) {
    this.actions.emit({
      simulatorId: driver.id,
      driverId: driver.driverId,
      action,
    });
  }
}
