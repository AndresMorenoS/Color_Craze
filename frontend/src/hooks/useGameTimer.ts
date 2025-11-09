import { useState, useEffect } from 'react';

export const useGameTimer = (remainingTime: number | undefined) => {
  const [displayTime, setDisplayTime] = useState(0);

  useEffect(() => {
    if (remainingTime !== undefined) {
      setDisplayTime(remainingTime);
    }
  }, [remainingTime]);

  const formatTime = (seconds: number): string => {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins}:${secs.toString().padStart(2, '0')}`;
  };

  return { displayTime, formatTime };
};
