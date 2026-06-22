const ICONOS_EMERGENCIA: Record<string, string> = {
  INCENDIO: '🔥',
  INUNDACION: '🌊',
  TERREMOTO: '⛰️',
  SEQUIA: '🌵',
  OTRO: '📦',
};

const IMAGENES_EMERGENCIA: Record<string, string> = {
  INCENDIO: 'assets/emergency-fire.jpg',
  INUNDACION: 'assets/emergency-flood.jpg',
  TERREMOTO: 'assets/emergency-earthquake.jpg',
  SEQUIA: 'assets/emergency-fire.jpg',
  OTRO: 'assets/hero-chile.jpg',
};

export function iconoDeEmergencia(tipoEmergencia?: string): string {
  return ICONOS_EMERGENCIA[tipoEmergencia ?? ''] ?? '📍';
}

export function imagenDeEmergencia(tipoEmergencia?: string): string {
  return IMAGENES_EMERGENCIA[tipoEmergencia ?? ''] ?? 'assets/hero-chile.jpg';
}
