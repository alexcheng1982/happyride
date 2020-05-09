export interface Driver {
  id: string;
  driverId: string;
  state: string;
  posLat: number;
  posLng: number;
  isAvailable: boolean;
  isNotAvailable: boolean;
  isOffline: boolean;
}

export interface SimulatorAction {
  simulatorId: string;
  driverId: string;
  action: string;
}
