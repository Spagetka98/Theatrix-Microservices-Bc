import { useEffect, useState } from "react";

const useTimer = (activityAvailability = true, milliSecLeft) => {
  const [available, setAvailable] = useState(activityAvailability);

  useEffect(() => {
    let timerOfAvalibility;

    const setTimer = () => {
      if (activityAvailability) return;
      timerOfAvalibility = setTimeout(
        () => setAvailable(true),
        milliSecLeft > 2147483647 ? 2147483647 : milliSecLeft
      );
    };

    setTimer();

    return () => {
      if (timerOfAvalibility) clearTimeout(timerOfAvalibility);
    };
  }, [activityAvailability, milliSecLeft]);

  return available;
};

export default useTimer;
