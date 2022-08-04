import { Component, OnInit } from '@angular/core';
import { DriverService } from './driver.service';
import { Driver, SimulatorAction } from './model';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  constructor(
    private driverService: DriverService,
    private _snackBar: MatSnackBar
  ) {}

  drivers: Driver[] = [];

  ngOnInit(): void {
    this.driverService.addDriverListener((drivers) => {
      this.drivers = drivers;
    });
  }

  handleSimulatorAction(simulatorAction: SimulatorAction) {
    const { simulatorId, driverId, action } = simulatorAction;
    this.driverService
      .sendSimulatorAction(simulatorId, action)
      .toPromise()
      .then((_) =>
        this._showMessage(`Sent action ${action} to driver ${driverId}`)
      )
      .catch((_) =>
        this._showMessage(
          `Failed to send action ${action} to driver ${driverId}`
        )
      );
  }

  addDriver() {
    this.driverService
      .addDriver()
      .toPromise()
      .then((_) => this._showMessage('Added a new driver'))
      .catch((_) => this._showMessage('Failed to add a new driver'));
  }

  private _showMessage(message: string) {
    this._snackBar.open(message, null, { duration: 3000 });
  }
}
