[![Publish npm package](https://github.com/ricardosohn/capacitor-device-time-settings/actions/workflows/publish.yml/badge.svg)](https://github.com/ricardosohn/capacitor-device-time-settings/actions/workflows/publish.yml)

# @ricardosohn/capacitor-device-time-settings

Native Capacitor plugin to read and monitor automatic date/time settings on Android, with a helper to open the device Date & Time settings screen.

On Android, users can disable **Automatic date/time** and **Automatic time zone**, which may break some app flows.

This plugin provides a simple way to:

1. check current automatic time settings,
2. observe changes in real time,
3. open Date & Time settings directly.

## Features

- ✅ Read Android automatic time flags (`autoTime`, `autoTimeZone`)
- ✅ Observe runtime changes (`timeSettingsChanged` event)
- ✅ Open Date & Time settings screen (`openDateSettings`)
- ✅ Fallback to general Settings when date settings are unavailable
- ✅ Capacitor-friendly API (TypeScript + Android native implementation)

## Installation

```bash
npm install @ricardosohn/capacitor-device-time-settings
npx cap sync
```

## Platform support

| Platform | Status |
| --- | --- |
| Android | ✅ Supported |
| iOS | ⚠️ Not implemented |
| Web | ⚠️ Stub/no-op behavior expected |

## API

```ts
interface AutomaticTimeStatus {
  autoTime: boolean;
  autoTimeZone: boolean;
}

interface DeviceTimeSettingsPlugin {
  getAutomaticTimeStatus(): Promise<AutomaticTimeStatus>;
  openDateSettings(): Promise<void>;
  startObserving(): Promise<void>;
  stopObserving(): Promise<void>;
  addListener(
    eventName: 'timeSettingsChanged',
    listenerFunc: (status: AutomaticTimeStatus) => void,
  ): Promise<PluginListenerHandle>;
}
```

## Example usage

```ts
import { DeviceTimeSettings } from '@ricardosohn/capacitor-device-time-settings';

const status = await DeviceTimeSettings.getAutomaticTimeStatus();

if (!status.autoTime || !status.autoTimeZone) {
  await DeviceTimeSettings.openDateSettings();
}

await DeviceTimeSettings.startObserving();

const listener = await DeviceTimeSettings.addListener(
  'timeSettingsChanged',
  (nextStatus) => {
    console.log('Time settings changed', nextStatus);
  },
);

// later
await DeviceTimeSettings.stopObserving();
await listener.remove();
```

## Error handling notes

- `openDateSettings()` may open general Settings on some Android vendors/ROMs.
- Always re-check with `getAutomaticTimeStatus()` after returning to app.
- Consider backend-side timestamp validation for high-integrity flows.

## Keywords (npm)

`capacitor`, `plugin`, `android`, `time`, `timezone`, `date`, `settings`, `offline`, `geolocation`

## Repository description (short)

Capacitor Android plugin to check, observe, and open automatic Date & Time settings.

## License

MIT
