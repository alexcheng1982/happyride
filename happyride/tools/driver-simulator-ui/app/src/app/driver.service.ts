import { Injectable } from '@angular/core';
import { Driver } from './model';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export type DriverListenerCallback = (drivers: Driver[]) => void;

@Injectable({
  providedIn: 'root',
})
export class DriverService {
  private eventSource: EventSource;

  constructor(private httpClient: HttpClient) {
    this.eventSource = new EventSource('/api/live');
  }

  addDriverListener(listener: DriverListenerCallback) {
    this.eventSource.addEventListener('driverUpdated', (event: any) => {
      const drivers: Driver[] = JSON.parse(event.data).map((driver) => ({
        ...driver,
        isAvailable: driver.state === 'AVAILABLE',
        isNotAvailable: driver.state === 'NOT_AVAILABLE',
        isOffline: driver.state === 'OFFLINE',
      }));
      listener(drivers);
    });
  }

  sendSimulatorAction(simulatorId: string, action: string): Observable<any> {
    return this.httpClient.post(`/api/${simulatorId}/${action}`, {});
  }

  addDriver() {
    return this.httpClient.post('/api/quickAdd', {});
  }
}
