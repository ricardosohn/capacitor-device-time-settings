import { WebPlugin } from '@capacitor/core';
import type {
  AutomaticTimeStatus,
  DeviceTimeSettingsPlugin,
} from './definitions';

export class DeviceTimeSettingsWeb
  extends WebPlugin
  implements DeviceTimeSettingsPlugin
{
  async getAutomaticTimeStatus(): Promise<AutomaticTimeStatus> {
    return {
      autoTime: true,
      autoTimeZone: true,
    };
  }

  async openDateSettings(): Promise<void> {
    return;
  }

  async startObserving(): Promise<void> {
    return;
  }

  async stopObserving(): Promise<void> {
    return;
  }
}
