import {
  ApplicationConfig,
  provideBrowserGlobalErrorListeners,
  provideZoneChangeDetection,
  LOCALE_ID,
  DEFAULT_CURRENCY_CODE
} from '@angular/core';
import {provideRouter, withComponentInputBinding, withViewTransitions} from '@angular/router';

import {routes} from './app.routes';

import {provideHttpClient, withFetch, withInterceptors} from '@angular/common/http';

// IMPORTACIONES DE IDIOMA Y MONEDA
import localeEsPe from '@angular/common/locales/es-PE';
import {registerLocaleData} from '@angular/common';
import {authInterceptor} from './core/interceptors/auth-interceptor';

// REGISTRAR EL IDIOMA
registerLocaleData(localeEsPe, 'es-PE');

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({eventCoalescing: true}),
    provideRouter(routes, withComponentInputBinding(), withViewTransitions()),
    provideHttpClient(
      withFetch(),
      withInterceptors([authInterceptor]) // JWT INTERCEPTOR
    ),
    // CONFIGURAR EL IDIOMA Y MONEDA POR DEFECTO
    {provide: LOCALE_ID, useValue: 'es-PE'},
    {provide: DEFAULT_CURRENCY_CODE, useValue: 'PEN'},
  ]
};
