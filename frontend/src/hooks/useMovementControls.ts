import { useEffect } from 'react';

export const useMovementControls = (
  onMove: (direction: string, jump: boolean) => void,
  enabled: boolean
) => {
  useEffect(() => {
    if (!enabled) return;

    const keys: { [key: string]: boolean } = {};

    const handleKeyDown = (e: KeyboardEvent) => {
      keys[e.key] = true;

      if (keys['ArrowLeft'] || keys['a'] || keys['A']) {
        onMove('left', keys[' '] || keys['ArrowUp'] || keys['w'] || keys['W']);
      } else if (keys['ArrowRight'] || keys['d'] || keys['D']) {
        onMove('right', keys[' '] || keys['ArrowUp'] || keys['w'] || keys['W']);
      } else if (keys[' '] || keys['ArrowUp'] || keys['w'] || keys['W']) {
        onMove('stop', true);
      }
    };

    const handleKeyUp = (e: KeyboardEvent) => {
      keys[e.key] = false;

      if ((e.key === 'ArrowLeft' || e.key === 'a' || e.key === 'A') && 
          !(keys['ArrowRight'] || keys['d'] || keys['D'])) {
        onMove('stop', false);
      } else if ((e.key === 'ArrowRight' || e.key === 'd' || e.key === 'D') && 
                 !(keys['ArrowLeft'] || keys['a'] || keys['A'])) {
        onMove('stop', false);
      }
    };

    window.addEventListener('keydown', handleKeyDown);
    window.addEventListener('keyup', handleKeyUp);

    return () => {
      window.removeEventListener('keydown', handleKeyDown);
      window.removeEventListener('keyup', handleKeyUp);
    };
  }, [onMove, enabled]);
};
