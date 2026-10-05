import { RenderMode, ServerRoute } from '@angular/ssr';

export const serverRoutes: ServerRoute[] = [
  { path: 'login', renderMode: RenderMode.Prerender },
  { path: 'register', renderMode: RenderMode.Prerender },
  // The session lives in localStorage, so auth-dependent routes render in the browser.
  { path: '**', renderMode: RenderMode.Client },
];
