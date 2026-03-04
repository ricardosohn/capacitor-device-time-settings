import type { PluginListenerHandle } from '@capacitor/core';

export interface AutomaticTimeStatus {
  autoTime: boolean;
  autoTimeZone: boolean;
}

export interface DeviceTimeSettingsPlugin {
  getAutomaticTimeStatus(): Promise<AutomaticTimeStatus>;
  openDateSettings(): Promise<void>;
  startObserving(): Promise<void>;
  stopObserving(): Promise<void>;
  addListener(
    eventName: 'timeSettingsChanged',
    listenerFunc: (status: AutomaticTimeStatus) => void,
  ): Promise<PluginListenerHandle>;
}
