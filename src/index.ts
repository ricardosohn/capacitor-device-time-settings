import { registerPlugin } from '@capacitor/core';

import type { DeviceTimeSettingsPlugin } from './definitions';

const DeviceTimeSettings = registerPlugin<DeviceTimeSettingsPlugin>(
  'DeviceTimeSettings',
  {
    web: () => import('./web').then((mod) => new mod.DeviceTimeSettingsWeb()),
  },
);

export * from './definitions';
export { DeviceTimeSettings };
